package com.suse.susenews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suse.susenews.R;
import com.suse.susenews.bean.LoginBean;
import com.suse.susenews.bean.Student;
import com.suse.susenews.utils.CacheUtils;
import com.suse.susenews.utils.Constants;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class LoginActivity extends Activity {

    private EditText et_account;
    private EditText et_password;
    private TextView tv_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
    }

    private void findViews() {
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        tv_hint = findViewById(R.id.tv_hint);
    }

    public void login(View v){
        String account = et_account.getText().toString();
        String password = et_password.getText().toString();
        if (account.isEmpty() || password.isEmpty()){
            tv_hint.setText("用户名或密码不能为空");
        }else {
            //判断是否登录成功
            //联网请求
            RequestParams requestParams = new RequestParams(Constants.LOGIN_URL);
            requestParams.addQueryStringParameter("NO", account);
            requestParams.addQueryStringParameter("PWD", password);

            x.http().get(requestParams, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    LoginBean loginBean = new Gson().fromJson(result, LoginBean.class);
                    //Log.e("TAG", "积分为：" + loginBean.getStudent().getAppointegral());
                    if (loginBean.getResultCode().equals("200")) {
                        //Toast.makeText(LoginActivity.this, loginBean.getStudent().getName(), Toast.LENGTH_LONG).show();
                        //假如登录成功;保存用户名和密码
                        CacheUtils.putString(LoginActivity.this, CacheUtils.SPNAME, "login_account", et_account.getText().toString());
                        CacheUtils.putString(LoginActivity.this, CacheUtils.SPNAME, "login_password", et_password.getText().toString());
                        CacheUtils.putString(LoginActivity.this, CacheUtils.SPNAME, "appointegral", loginBean.getStudent().getAppointegral()+"");
                        CacheUtils.USER_ACCOUNT = et_account.getText().toString();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }else {
                        tv_hint.setText("*用户名或密码错误");
                        et_account.setText("");
                        et_password.setText("");
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.e("TAG", "onError():ex:"+ex.getMessage()+"--isOnCallback:"+isOnCallback);
                    if (ex.getMessage().equals("Network is unreachable")){
                        Toast.makeText(LoginActivity.this, "未连接网络", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(LoginActivity.this, "请求错误，请稍后再试", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Log.e("TAG", "onCancelled()" + cex.getMessage());
                }

                @Override
                public void onFinished() {
                    Log.e("TAG", "onFinished()");
                }
            });
        }
    }
}
