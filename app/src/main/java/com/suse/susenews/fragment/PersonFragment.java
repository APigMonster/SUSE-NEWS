package com.suse.susenews.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suse.susenews.R;
import com.suse.susenews.activity.LoginActivity;
import com.suse.susenews.activity.MainActivity;
import com.suse.susenews.activity.PersonInfoActivity;
import com.suse.susenews.adapter.PersonRelateAdapter;
import com.suse.susenews.bean.LoginBean;
import com.suse.susenews.bean.PersonRelateBean;
import com.suse.susenews.bean.Student;
import com.suse.susenews.bean.StudentAppoed;
import com.suse.susenews.utils.BaseFragment;
import com.suse.susenews.utils.CacheUtils;
import com.suse.susenews.utils.Constants;
import com.suse.susenews.utils.MyApplication;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

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
    private TextView tv_person_name;
    private TextView tv_person_status;


    private String account;
    private String password;
    private StudentAppoed appoedData = null;
    private Student student;


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
        tv_person_name = view.findViewById(R.id.tv_person_name);
        tv_person_status = view.findViewById(R.id.tv_person_status);

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
        //得到登录的账号，作为联网请求时的参数
        account = CacheUtils.getString(MyApplication.getContext(), CacheUtils.SPNAME, "login_account");
        password = CacheUtils.getString(MyApplication.getContext(), CacheUtils.SPNAME, "login_password");
        //联网请求数据
        //processData();
        //设置点击监听
        rlPersonInfo.setOnClickListener(this);
        btn_exit_login.setOnClickListener(this);


    }

    private void setPersonAdapter() {
        adapter = new PersonRelateAdapter(data, mcontext);
        LinearLayoutManager manager = new LinearLayoutManager(mcontext);
        rv_person_relate.setLayoutManager(manager);
        rv_person_relate.setAdapter(adapter);

    }

    private void processData() {
        //获取学生的基本信息（利用登录时的方法)
        getStudentPersonInfo();
        //查询已预约的新闻
        selectStudentAppoed();
    }

    private void getStudentPersonInfo() {
        //联网请求
        RequestParams requestParams = new RequestParams(Constants.LOGIN_URL);
        requestParams.addQueryStringParameter("NO", account);
        requestParams.addQueryStringParameter("PWD", password);

        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LoginBean loginBean = new Gson().fromJson(result, LoginBean.class);
                if (loginBean.getResultCode().equals("200")) {
                    student = loginBean.getStudent();
                    setStudnetInfo();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "onError():ex:" + ex.getMessage() + "--isOnCallback:" + isOnCallback);
                if (ex.getMessage().equals("Network is unreachable")) {
                    Toast.makeText(MyApplication.getContext(), "未连接网络", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MyApplication.getContext(), "请求错误，请稍后再试", Toast.LENGTH_LONG).show();
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

    private void setStudnetInfo() {
        tv_person_name.setText(student.getName());
        tv_person_status.setText(student.getEdusys());
        //下载图片
        //Log.e("TAG", "image == " + Constants.UPLOAD_URL + student.getHeadpor());
        x.image().bind(iv_person_icon, Constants.UPLOAD_URL + student.getHeadpor());
    }

    private void selectStudentAppoed() {
        RequestParams params = new RequestParams(Constants.SELECT_APPOED_URL + account);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //Log.e("TAG", "appoedData.size() == " +appoedData.size());


                StudentAppoed studentAppoed = new Gson().fromJson(result, StudentAppoed.class);
                appoedData = studentAppoed;
                isinitListDataed = true;

                initListData();
                //设置头像下面列表的适配器
                setPersonAdapter();

                //Log.e("TAG", "studentAppoed == " +appoedData.getSusenewsList().size());
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

    private void initListData() {
        //Log.e("TAG", "appoData"+new Gson().toJson(appoedData));
        //判断 我的预约 是否需要显示红点
        String isShowRedPoint = isShowRedPoint(appoedData);
        data.clear();
        data.add(new PersonRelateBean(R.drawable.appointment__icon, "我的预约", isShowRedPoint));
        data.add(new PersonRelateBean(R.drawable.appointegral, "积分使用规则", "no"));
        data.add(new PersonRelateBean(R.drawable.wait, "待开发", "预留信息口"));
    }

    private String isShowRedPoint(StudentAppoed appoedData) {


        if (appoedData.getSusenewsList().size() != 0 && appoedData.getSusenewsList().get(0).getAppostauts() == 0) {
            return "yes";
        }
        return "no";
    }

    @Override
    public void onClick(View v) {
        if (v == rlPersonInfo) {
            Intent intent = new Intent(mcontext, PersonInfoActivity.class);
            intent.putExtra("student", new Gson().toJson(student));
            startActivity(intent);
        } else if (v == btn_exit_login) {
            //退出登录；清空账号，跳转到登录页面
            CacheUtils.putString(mcontext, CacheUtils.SPNAME, "login_account", "");
            startActivity(new Intent(mcontext, LoginActivity.class));
            //关闭页面
            MainActivity mainActivity = (MainActivity) mcontext;
            mainActivity.finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.e("TAG", "我有没有正在显示？");
        //getStudentPersonInfo();
        processData();
    }
}
