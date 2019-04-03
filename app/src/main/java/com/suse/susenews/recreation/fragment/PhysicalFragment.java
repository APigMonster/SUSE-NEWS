package com.suse.susenews.recreation.fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.suse.susenews.utils.BaseFragment;
import com.suse.susenews.utils.MyApplication;

public class PhysicalFragment extends BaseFragment{
    public TextView title;

    public PhysicalFragment(String title) {
        super(title);
    }
    public PhysicalFragment() {
        this("");
    }

    @Override
    public View initView() {
        title = new TextView(mcontext);
        title.setText("体育");
        title.setTextSize(20);
        title.setGravity(Gravity.CENTER);
        Log.e("TAG", "体育");
        return title;
    }
}
