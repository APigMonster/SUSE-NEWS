package com.suse.susenews.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.suse.susenews.R;
import com.suse.susenews.utils.BaseFragment;

public class MainFragment extends BaseFragment {

    private FrameLayout fl_base;
    private RadioGroup rg_base;
    private MainNewsFragment mMainNewsFragment = new MainNewsFragment();
    private PersonFragment mPersonFragment = new PersonFragment();
    private RecreationNewsFragment mRecreationNewsFragment = new RecreationNewsFragment();

    @Override
    public View initView() {
        View view = View.inflate(mcontext, R.layout.main_layout, null);
        fl_base = view.findViewById(R.id.fl_base);
        rg_base = view.findViewById(R.id.rg_base);
        return view;
    }

    @Override
    public void initData() {
        //设置默认选中第一个radiobutton
        rg_base.check(R.id.rb_main);
        //设置默认选中第一个页面
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_base, mMainNewsFragment);
        transaction.commit();
        //给RadioButton设置监听
        rg_base.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_main:
                    FragmentManager fragmentManager1 = getChildFragmentManager();
                    FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
                    transaction1.replace(R.id.fl_base, mMainNewsFragment);
                    transaction1.commit();
                    break;
                case R.id.rb_recreation:
                    FragmentManager fragmentManager2 = getChildFragmentManager();
                    FragmentTransaction transaction2 = fragmentManager2.beginTransaction();
                    transaction2.replace(R.id.fl_base, mRecreationNewsFragment);
                    transaction2.commit();
                    break;
                case R.id.rb_person:
                    FragmentManager fragmentManager3 = getChildFragmentManager();
                    FragmentTransaction transaction3 = fragmentManager3.beginTransaction();
                    transaction3.replace(R.id.fl_base, mPersonFragment);
                    transaction3.commit();
                    break;
                default:
                    break;
            }
        }
    }
}
