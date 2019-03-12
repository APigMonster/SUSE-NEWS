package com.suse.susenews.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.suse.susenews.R;
import com.suse.susenews.activity.SearchActivity;
import com.suse.susenews.adapter.MainNewsAdapter;
import com.suse.susenews.bean.MainNewsBean;
import com.suse.susenews.utils.BaseFragment;
import com.suse.susenews.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class MainNewsFragment extends BaseFragment {

    private RecyclerView rv_main_news;
    private List<MainNewsBean> data = new ArrayList<>();
    private EditText et_search;

    @Override
    public View initView() {
        View view = View.inflate(mcontext, R.layout.main_news_layout, null);
        rv_main_news  = view.findViewById(R.id.rv_main_news);
        et_search = view.findViewById(R.id.et_search);
        //设置点击监听
        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcontext.startActivity(new Intent(mcontext, SearchActivity.class));
            }
        });
        return view;
    }


    @Override
    public void initData() {
        initListData();
        //设置适配器
        LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext);
        rv_main_news.setLayoutManager(layoutManager);
        MainNewsAdapter adapter = new MainNewsAdapter(data, mcontext);
        rv_main_news.setAdapter(adapter);
    }

    private void initListData() {
        data.add(new MainNewsBean(R.mipmap.icon,"123451", "这是一个标题，这是标题1，对应第1个新闻"));
        data.add(new MainNewsBean(R.mipmap.icon,"123452", "这是一个标题，这是标题2，对应第2个新闻"));
        data.add(new MainNewsBean(R.mipmap.icon,"123453", "这是一个标题，这是标题3，对应第3个新闻"));
        data.add(new MainNewsBean(R.mipmap.icon,"123454", "这是一个标题，这是标题4，对应第4个新闻"));
        data.add(new MainNewsBean(R.mipmap.icon,"123455", "这是一个标题，这是标题5，对应第5个新闻"));
        data.add(new MainNewsBean(R.mipmap.icon,"123456", "这是一个标题，这是标题6，对应第6个新闻"));
        data.add(new MainNewsBean(R.mipmap.icon,"123457", "这是一个标题，这是标题7，对应第7个新闻"));
        data.add(new MainNewsBean(R.mipmap.icon,"123458", "这是一个标题，这是标题8，对应第8个新闻"));
        data.add(new MainNewsBean(R.mipmap.icon,"123459", "这是一个标题，这是标题9，对应第9个新闻"));
        data.add(new MainNewsBean(R.mipmap.icon,"1234510", "这是一个标题，这是标题10，对应第10个新闻"));
        data.add(new MainNewsBean(R.mipmap.icon,"1234511", "这是一个标题，这是标题11，对应第11个新闻"));
        data.add(new MainNewsBean(R.mipmap.icon,"1234512", "这是一个标题，这是标题12，对应第12个新闻"));
        data.add(new MainNewsBean(R.mipmap.icon,"1234513", "这是一个标题，这是标题13，对应第13个新闻"));
        data.add(new MainNewsBean(R.mipmap.icon,"1234514", "这是一个标题，这是标题14，对应第14个新闻"));
        data.add(new MainNewsBean(R.mipmap.icon,"1234515", "这是一个标题，这是标题15，对应第15个新闻"));
    }
}
