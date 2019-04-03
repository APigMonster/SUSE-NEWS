package com.suse.susenews.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suse.susenews.R;
import com.suse.susenews.activity.LoginActivity;
import com.suse.susenews.activity.MainActivity;
import com.suse.susenews.activity.PersonAppoActivity;
import com.suse.susenews.activity.PersonInfoActivity;
import com.suse.susenews.activity.PersonMsgActivity;
import com.suse.susenews.adapter.PersonRelateAdapter;
import com.suse.susenews.bean.PersonRelateBean;
import com.suse.susenews.utils.BaseFragment;
import com.suse.susenews.utils.CacheUtils;
import com.suse.susenews.utils.MyApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonFragment extends BaseFragment implements View.OnClickListener {

    public static final String ICON = "ICON";
    public static final String TITLE = "TITLE";

    private RelativeLayout rlPersonInfo;
    private Button btn_exit_login;

    private RecyclerView rv_person_relate;
    private List<PersonRelateBean> data = new ArrayList<>();
    private PersonRelateAdapter adapter;
    private ImageView iv_person_icon;
    private boolean isinitListDataed;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2019-03-12 10:12:12 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews(View view) {
        rlPersonInfo = view.findViewById(R.id.rl_person_info);
        iv_person_icon = view.findViewById(R.id.iv_person_icon);
        btn_exit_login = view.findViewById(R.id.btn_exit_login);

        rv_person_relate = view.findViewById(R.id.rv_person_relate);
    }

    @Override
    public View initView() {
        View view = View.inflate(MyApplication.getContext(), R.layout.person_layout, null);
        findViews(view);
        return view;
    }

    @Override
    public void initData() {
        rlPersonInfo.setOnClickListener(this);
        btn_exit_login.setOnClickListener(this);

        //设置头像

        //初始化数据，并设置适配器
        initListData();
        isinitListDataed = true;
        adapter = new PersonRelateAdapter(data, mcontext);
        LinearLayoutManager manager = new LinearLayoutManager(mcontext);
        rv_person_relate.setLayoutManager(manager);
        rv_person_relate.setAdapter(adapter);

    }

    private void initListData() {
        if (!isinitListDataed){
            data.add(new PersonRelateBean(R.drawable.appointment__icon, "我的预约"));
            data.add(new PersonRelateBean(R.drawable.my_msg, "我的信息"));
            data.add(new PersonRelateBean(R.drawable.wait, "待开发"));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == rlPersonInfo) {
            startActivity(new Intent(mcontext, PersonInfoActivity.class));
        } else if (v == btn_exit_login) {
            //退出登录；清空账号，跳转到登录页面
            CacheUtils.putString(mcontext, CacheUtils.SPNAME, "login_account", "");
            startActivity(new Intent(mcontext, LoginActivity.class));
            //关闭页面
            MainActivity mainActivity = (MainActivity) mcontext;
            mainActivity.finish();
        }
    }
}
