package com.suse.susenews.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suse.susenews.R;
import com.suse.susenews.adapter.RecreationNewsAdapter;
import com.suse.susenews.recreation.fragment.EveningPartyFragment;
import com.suse.susenews.recreation.fragment.LectureFragment;
import com.suse.susenews.recreation.fragment.PhysicalFragment;
import com.suse.susenews.utils.BaseFragment;
import com.suse.susenews.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class RecreationNewsFragment extends Fragment {

    private ViewPager vp_recreation_news;
    private TabLayout tablayout_recreation_news;

    private List<BaseFragment> data = new ArrayList<>();
    private boolean isinitListDataed;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme);
        LayoutInflater Inflater = inflater.cloneInContext(contextThemeWrapper);
        View view = Inflater.inflate(R.layout.recreation_news_layout, container, false);

        //初始化控件
        vp_recreation_news = view.findViewById(R.id.vp_recreation_news);
        tablayout_recreation_news = view.findViewById(R.id.tablayout_recreation_news);

        //初始化viewPager数据
        initListData();
        //是否执行过onCreateView()
        isinitListDataed = true;

        //设置适配器
        vp_recreation_news.setAdapter(new RecreationNewsAdapter(getChildFragmentManager(), data));
        //tablayout关联viewpager
        tablayout_recreation_news.setupWithViewPager(vp_recreation_news);
        tablayout_recreation_news.setTabMode(TabLayout.MODE_SCROLLABLE);

        return view;
    }


    private void initListData() {
        if (!isinitListDataed){
            data.add(new EveningPartyFragment("晚会"));
            data.add(new PhysicalFragment("体育"));
            data.add(new LectureFragment("讲座"));
        }
    }
}
