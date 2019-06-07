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
import com.suse.susenews.bean.Susenews;
import com.suse.susenews.utils.CacheUtils;
import com.suse.susenews.utils.Constants;
import com.suse.susenews.utils.MyApplication;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

public class MainNewsAdapter extends RecyclerView.Adapter<MainNewsAdapter.ViewHolder> {

    private List<Susenews> mdata;
    private Context context;
    private String idArray = CacheUtils.getString(MyApplication.getContext(), CacheUtils.SPNAME, CacheUtils.READ_ARRAY_ID);

    public MainNewsAdapter(List<Susenews> mdata, Context context) {
        this.mdata = mdata;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_main_news, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                String url = mdata.get(position).getContent();
                String id = mdata.get(position).getId() + "";
                if (!idArray.contains(id)) {
                    CacheUtils.putString(MyApplication.getContext(), CacheUtils.SPNAME, CacheUtils.READ_ARRAY_ID, idArray + id + ",");
                    //点击时设置字体为灰色
                    viewHolder.title.setTextColor(Color.GRAY);
                }
                mdata.get(position).setReadcounts(mdata.get(position).getReadcounts()+1);
                //修改阅读次数（联网修改阅读次数）
                RequestParams params = new RequestParams(Constants.UPDATA_NEWS_URL);
                params.addParameter("newsId", id);
                params.addParameter("type", "readcounts");
                params.addParameter("update", mdata.get(position).getReadcounts());
                x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.i("TAG", result);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.e("TAG", ex.getMessage());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
                //跳转到详情Activity
                Intent intent = new Intent(context, NewsDetailsActivity.class);
                intent.putExtra("url", Constants.UPLOAD_URL + url);
                context.startActivity(intent);
            }

        });
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Susenews susenews = mdata.get(i);
        //viewHolder.icon.setImageResource(susenews.getIcon());
        viewHolder.title.setText(susenews.getTitle());
        int readCount = susenews.getReadcounts();
        viewHolder.tv_readed.setText("阅读:"+readCount);
        x.image().bind(viewHolder.icon, Constants.UPLOAD_URL + susenews.getImaget());
        //判断是否点击(阅读)过
        if (idArray.contains(susenews.getId() + "")) {
            viewHolder.title.setTextColor(Color.GRAY);
        } else {
            viewHolder.title.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView icon;
        TextView title;
        TextView tv_readed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            icon = itemView.findViewById(R.id.mian_item_icon);
            title = itemView.findViewById(R.id.main_item_title);
            tv_readed = itemView.findViewById(R.id.tv_readed);
        }
    }
}
