package com.suse.susenews.fragment;

import android.view.View;

import com.suse.susenews.R;
import com.suse.susenews.utils.BaseFragment;
import com.suse.susenews.utils.MyApplication;

public class RecreationNewsFragment extends BaseFragment {
    @Override
    public View initView() {
        View view = View.inflate(MyApplication.getContext(), R.layout.recreation_news_layout, null);
        return view;
    }
}
