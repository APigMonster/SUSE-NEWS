package com.suse.susenews.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suse.susenews.R;
import com.suse.susenews.bean.NewsListBean;
import com.suse.susenews.bean.Susenews;
import com.suse.susenews.search.fragment.ShowSearchHistory;
import com.suse.susenews.search.fragment.ShowSearchResult;
import com.suse.susenews.utils.CacheUtils;
import com.suse.susenews.utils.Constants;
import com.suse.susenews.utils.MyApplication;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.suse.susenews.search.fragment.ShowSearchHistory.SEARCH_HISTORY;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_search;
    private TextView tv_search;
    private ImageView iv_cancel;


    private List<String> data = new ArrayList<>();
    private List<Susenews> resultData = new ArrayList<>();
    private String sdata = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        et_search = findViewById(R.id.et_search);
        tv_search = findViewById(R.id.tv_search);
        iv_cancel = findViewById(R.id.iv_cancel);

        //设置取消和搜索监听
        iv_cancel.setOnClickListener(this);
        tv_search.setOnClickListener(this);

        //显示/隐藏 取消图标(判断EditText是否为空)
        getEditTextValue();

        //初始化数据(历史记录)
        sdata = CacheUtils.getString(MyApplication.getContext(), CacheUtils.SPNAME, SEARCH_HISTORY);
        data = Arrays.asList(sdata.split(","));//先把字符串以关键字","分割成字符串数组；再把字符串数组转化为列表
        Collections.reverse(data); //把列表翻转

        initFragment();
    }


    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_search, new ShowSearchHistory());
        transaction.commit();
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
                Toast.makeText(MyApplication.getContext(), "搜索内容不能为空", Toast.LENGTH_SHORT).show();
            } else {
                //判断是否存在
                if (!data.contains(getEditTextValue())) {
                    String value = sdata + getEditTextValue() + ",";
                    CacheUtils.putString(MyApplication.getContext(), CacheUtils.SPNAME, SEARCH_HISTORY, value);
                }
                //联网请求数据
                processData();
            }
        } else if (v == iv_cancel) {
            et_search.setText("");
        }
    }

    public void processData() {
        RequestParams params = new RequestParams(Constants.GETNEWS_URL +"/1/10/"+getEditTextValue());
        params.addParameter("searchType", "title");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                NewsListBean newsListBean = new Gson().fromJson(result, NewsListBean.class);
                resultData.addAll(newsListBean.getNewsList());

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fl_search, new ShowSearchResult(resultData, getEditTextValue()));
                transaction.commit();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
