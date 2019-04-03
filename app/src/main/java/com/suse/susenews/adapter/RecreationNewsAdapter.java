package com.suse.susenews.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.suse.susenews.utils.BaseFragment;

import java.util.List;

public class RecreationNewsAdapter extends FragmentPagerAdapter {

    private final List<BaseFragment> data;

    public RecreationNewsAdapter(FragmentManager fm, List<BaseFragment> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int i) {
        return data.get(i);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return data.get(position).getTitle();
    }
}
