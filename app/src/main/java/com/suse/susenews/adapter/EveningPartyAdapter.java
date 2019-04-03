package com.suse.susenews.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.suse.susenews.R;
import com.suse.susenews.activity.NewsDetailsActivity;
import com.suse.susenews.bean.MainNewsBean;
import com.suse.susenews.utils.CacheUtils;
import com.suse.susenews.utils.MyApplication;

import java.util.List;

public class EveningPartyAdapter extends RecyclerView.Adapter<EveningPartyAdapter.ViewHolder> {
    private List<MainNewsBean> mdata;
    private Context context;
    private String idArray = CacheUtils.getString(MyApplication.getContext(), CacheUtils.SPNAME, CacheUtils.READ_ARRAY_ID);

    public EveningPartyAdapter(List<MainNewsBean> mdata, Context context) {
        this.mdata = mdata;
        this.context = context;
    }

    @NonNull
    @Override
    public EveningPartyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_main_news, viewGroup, false);
        EveningPartyAdapter.ViewHolder viewHolder = new EveningPartyAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        MainNewsBean mainNewsBean = mdata.get(i);
        viewHolder.icon.setImageResource(mainNewsBean.getIcon());
        viewHolder.title.setText(mainNewsBean.getTitle());
        //判断是否点击(阅读)过
        if (idArray.contains(mainNewsBean.getId())) {
            viewHolder.title.setTextColor(Color.GRAY);
        } else {
            viewHolder.title.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public void notifyDataSetChanged(List<MainNewsBean> data) {
            this.mdata = data;
            this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView icon;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            icon = itemView.findViewById(R.id.mian_item_icon);
            title = itemView.findViewById(R.id.main_item_title);
        }
    }
}