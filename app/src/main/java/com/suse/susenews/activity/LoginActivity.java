package com.suse.susenews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.suse.susenews.R;
import com.suse.susenews.utils.CacheUtils;

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
        String password = et_account.getText().toString();

        //判断是否登录成功
        //假如登录成功;保存用户名和密码
        CacheUtils.putString(LoginActivity.this, CacheUtils.SPNAME, "login_account", account);
        CacheUtils.putString(LoginActivity.this, CacheUtils.SPNAME, "login_password", password);
        CacheUtils.USER_ACCOUNT = account;
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}
