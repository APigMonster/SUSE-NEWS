package com.suse.susenews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;

import com.suse.susenews.R;
import com.suse.susenews.utils.CacheUtils;

/**
 * 延迟2秒进入下一个界面
 */
public class WelcomeActivity extends Activity {


    public static final String START_MAIN = "start_main";
    private int WHAT_TO_ACTIVITY = 1;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            isStartMain();
        }
    };

    private void isStartMain() {
        boolean isStartMain = CacheUtils.getBoolean(WelcomeActivity.this, CacheUtils.SPNAME, START_MAIN);
        if (isStartMain) {
            String loginAccount = CacheUtils.getString(WelcomeActivity.this, CacheUtils.SPNAME, "login_account");
            //进入过主界面，直接进入主界面
            if (loginAccount.isEmpty()){
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            }else {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            }
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else {
            //没有进入过主界面，先进入引导页面
            startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //发送延迟消息
        mhandler.sendEmptyMessageDelayed(WHAT_TO_ACTIVITY, 2000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mhandler.removeMessages(WHAT_TO_ACTIVITY);
    }
}
