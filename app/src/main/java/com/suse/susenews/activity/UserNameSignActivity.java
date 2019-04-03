package com.suse.susenews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.suse.susenews.R;
import com.suse.susenews.utils.CacheUtils;

public class UserNameSignActivity extends Activity implements View.OnClickListener {

    private EditText et_ex_name;
    private Button btn_save_name;
    private ImageView iv_exname_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);
        //初始化页面
        findViews();
        //初始化数据
        Intent intent = getIntent();
        String name = intent.getStringExtra("VALUE");

        et_ex_name.setText(name);
        et_ex_name.setSelection(name.length());//将光标移至文字末尾
        et_ex_name.addTextChangedListener(new NameChange());
    }

    private void findViews() {
        et_ex_name = findViewById(R.id.et_ex_name);
        btn_save_name = findViewById(R.id.btn_save_name);
        iv_exname_back = findViewById(R.id.iv_exname_back);

        iv_exname_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    class NameChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            btn_save_name.setEnabled(true);
        }
    }

    public void saveName(View v) {
        String result = et_ex_name.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("RESULT", result);
        setResult(3,intent);
        finish();
    }
}
