package com.suse.susenews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suse.susenews.R;
import com.suse.susenews.bean.LoginBean;
import com.suse.susenews.bean.Student;
import com.suse.susenews.utils.CacheUtils;
import com.suse.susenews.utils.Constants;
import com.suse.susenews.utils.MyApplication;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 延迟2秒进入下一个界面
 */
public class WelcomeActivity extends Activity {


    public static final String START_MAIN = "start_main";
    private int WHAT_TO_ACTIVITY = 1;
    private boolean isStartMain = false;
    private LoginBean loginBean;

    private String resultCode;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            intoMain();
        }
    };
    private String loginAccount;
    private String loginPassword;

    private void intoMain() {

        //进入过主界面，直接进入主界面
        resultCode = loginBean.getResultCode();
        if (resultCode.isEmpty()) {
            Toast.makeText(WelcomeActivity.this, "网络延迟，请稍后再试", Toast.LENGTH_SHORT).show();
        } else {
            loginMain();
        }

    }

    private void loginMain() {
        if ("100".equals(resultCode)) {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        } else {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        }
        overridePendingTransition(R.anim.right_in, R.anim.left_out);

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //校验数据
        isStartMain = CacheUtils.getBoolean(WelcomeActivity.this, CacheUtils.SPNAME, START_MAIN);

        isFinishGuide();


    }

    private void isFinishGuide() {

        loginAccount = CacheUtils.getString(WelcomeActivity.this, CacheUtils.SPNAME, "login_account");
        loginPassword = CacheUtils.getString(WelcomeActivity.this, CacheUtils.SPNAME, "login_password");

        if (isStartMain) {
            if (!loginAccount.isEmpty() && !loginPassword.isEmpty()){

                VerificationLogin();
                //发送延迟消息(及时发送，延迟执行)
                mhandler.sendEmptyMessageDelayed(WHAT_TO_ACTIVITY, 4000);//主要是给联网请求数据时间
            }else {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));

                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();

            }
        } else {
            //没有完成过引导，先进入引导页面
            startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));

            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();
        }
    }

    private void VerificationLogin() {

        RequestParams requestParams = new RequestParams(Constants.LOGIN_URL);
        requestParams.addQueryStringParameter("NO", loginAccount);
        requestParams.addQueryStringParameter("PWD", loginPassword);

        //Log.e("TAG", "loginAccount == " +loginAccount);

        x.http().get(requestParams, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                loginBean = new Gson().fromJson(result, LoginBean.class);
                //Log.e("TAG", "resultCode == " +loginBean.getResultCode().equals("200"));
                if (loginBean.getResultCode().equals("200")) {
                    CacheUtils.putString(MyApplication.getContext(), CacheUtils.SPNAME, "appointegral", loginBean.getStudent().getAppointegral() + "");
                }

                //CacheUtils.USER_ACCOUNT = et_account.getText().toString();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //联网失败清空延迟消息，否则loginBean为空出现闪退
                mhandler.removeMessages(WHAT_TO_ACTIVITY);
                Toast.makeText(WelcomeActivity.this, "请求错误:" + ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mhandler.removeMessages(WHAT_TO_ACTIVITY);
    }
}
