package com.suse.susenews.recreation.fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.suse.susenews.utils.BaseFragment;
import com.suse.susenews.utils.MyApplication;

public class LectureFragment extends BaseFragment {
    public TextView title;
    private String stitle;

    public LectureFragment() {
        this("");
    }
    public LectureFragment(String title) {
        super(title);
    }


    @Override
    public View initView() {
        title = new TextView(MyApplication.getContext());
        title.setText("讲座");
        title.setTextSize(20);
        title.setGravity(Gravity.CENTER);
        Log.e("TAG", "讲座");
        return title;
    }
}
