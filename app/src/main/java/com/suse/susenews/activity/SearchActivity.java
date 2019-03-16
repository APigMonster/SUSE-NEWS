package com.suse.susenews.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suse.susenews.R;
import com.suse.susenews.utils.CacheUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends Activity implements View.OnClickListener {

    public static final String SEARCH_HISTORY = "search_history";
    private EditText et_search;
    private TextView tv_search;
    private ImageView iv_cancel;
    private ListView lv_search_history;
    private RelativeLayout rl_search_history;
    private List<String> data = new ArrayList<>();
    private String sdata = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        et_search = findViewById(R.id.et_search);
        tv_search = findViewById(R.id.tv_search);
        iv_cancel = findViewById(R.id.iv_cancel);
        lv_search_history = findViewById(R.id.lv_search_history);
        rl_search_history = findViewById(R.id.rl_search_history);

        //设置取消和搜索监听
        iv_cancel.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        //显示/隐藏 取消图标(判断EditText是否为空)
        getEditTextValue();

        //初始化数据(历史记录)
        sdata = CacheUtils.getString(SearchActivity.this, CacheUtils.SPNAME, SEARCH_HISTORY);
        data = Arrays.asList(sdata.split(","));//先把字符串以关键字","分割成字符串数组；再把字符串数组转化为列表
        Collections.reverse(data); //把列表翻转
        //设置适配器
        lv_search_history.setAdapter(new ArrayAdapter<>(SearchActivity.this, R.layout.item_array_adapter, data));
        //判断历史记录 视图是否隐藏
        isShowHistory(sdata);
    }

    private void isShowHistory(String sdata) {
        if (sdata.isEmpty()) {
            rl_search_history.setVisibility(View.GONE);
        }
    }

    public void emptySearchHistory(View v) {
        rl_search_history.setVisibility(View.GONE);
        CacheUtils.putString(SearchActivity.this, CacheUtils.SPNAME, SEARCH_HISTORY, "");
    }

    private String getEditTextValue() {
        String value = et_search.getText().toString();
        if (TextUtils.isEmpty(et_search.getText())) {
            return null;
        } else {
            return value;
        }
    }


    @Override
    public void onClick(View v) {
        if (v == tv_search) {
            if (getEditTextValue() == null) {
                Toast.makeText(SearchActivity.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
            } else {
                //判断是否存在
                if (!Arrays.asList(data).contains(getEditTextValue())) {
                    String value = sdata + getEditTextValue() + ",";
                    CacheUtils.putString(SearchActivity.this, CacheUtils.SPNAME, SEARCH_HISTORY, value);
                    Toast.makeText(SearchActivity.this, "搜索 : " + getEditTextValue(), Toast.LENGTH_SHORT).show();
                }
            }
        } else if (v == iv_cancel) {
            et_search.setText("");
        }
    }
}
