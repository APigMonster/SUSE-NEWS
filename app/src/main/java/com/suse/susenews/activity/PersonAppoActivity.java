package com.suse.susenews.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.suse.susenews.R;
import com.suse.susenews.adapter.SearchResultAdapter;
import com.suse.susenews.adapter.ShowAppoAdapter;
import com.suse.susenews.bean.PersonRelateBean;
import com.suse.susenews.bean.StudentAppoed;
import com.suse.susenews.utils.CacheUtils;
import com.suse.susenews.utils.Constants;
import com.suse.susenews.utils.MyApplication;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PersonAppoActivity extends Activity {

    private RecyclerView rv_appo_show;
    private String account;
    private List<StudentAppoed.SusenewsListBean> data = new ArrayList<>();
    private ShowAppoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_appo);
        //getIntent().getStringExtra("DATA")
       //Log.e("TAG", getIntent().getStringExtra("DATA"));
        //初始化视图
        rv_appo_show = findViewById(R.id.rv_appo_show);

        account = CacheUtils.getString(MyApplication.getContext(), CacheUtils.SPNAME, "login_account");

    }

    private void getData() {
        RequestParams params = new RequestParams(Constants.SELECT_APPOED_URL + account);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //Log.e("TAG", "appoedData.size() == " +appoedData.size());
                StudentAppoed studentAppoed = new Gson().fromJson(result, StudentAppoed.class);
                data.clear();
                data.addAll(studentAppoed.getSusenewsList());
                //Log.i("TAG", data.get(0).getSusenews().getId()+"");
                //Log.e("TAG", "data.size == " + data.size());
                //给recycleview设置适配器
                setAdatper();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "ex:--==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void setAdatper() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(PersonAppoActivity.this);
        adapter = new ShowAppoAdapter(data, PersonAppoActivity.this);
        rv_appo_show.setLayoutManager(layoutManager);
        rv_appo_show.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //请求数据
        getData();
    }
}
