package com.suse.susenews.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.suse.susenews.R;
import com.suse.susenews.adapter.PersonSimpleInfoAdapter;
import com.suse.susenews.bean.PersonSimpleInfoBean;
import com.suse.susenews.utils.CacheUtils;

import java.util.ArrayList;
import java.util.List;

public class PersonInfoActivity extends Activity implements View.OnClickListener {

    private ImageView iv_person_back;
    private ImageView iv_head_portrait;
    private RelativeLayout rl_head_portrait;

    private RecyclerView rv_person_simple_info;
    private List<PersonSimpleInfoBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        findViews();
        //初始化数据
        initData();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_person_simple_info.setLayoutManager(manager);
        rv_person_simple_info.setAdapter(new PersonSimpleInfoAdapter(data, this));
    }

    private void initData() {
        //用户名应该从数据库查询得到
        String userName = CacheUtils.getString(PersonInfoActivity.this, CacheUtils.USER_ACCOUNT, CacheUtils.USER_NAME);
        String userSex = CacheUtils.getString(PersonInfoActivity.this,  CacheUtils.USER_ACCOUNT,CacheUtils.USER_SEX);
        data.add(new PersonSimpleInfoBean("用户名", userName));
        data.add(new PersonSimpleInfoBean("性别", userSex));
        data.add(new PersonSimpleInfoBean("更多", " "));
    }

    private void findViews() {
        iv_person_back = findViewById(R.id.iv_person_back);
        rl_head_portrait = findViewById(R.id.rl_head_portrait);
        rv_person_simple_info = findViewById(R.id.rv_person_simple_info);

        iv_person_back.setOnClickListener(this);
        rl_head_portrait.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == iv_person_back){
            finish();
        }else if (v == rl_head_portrait){
            setImageHead();
        }
    }

    private void setImageHead() {

    }
}
