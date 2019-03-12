package com.suse.susenews.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.suse.susenews.R;
import com.suse.susenews.activity.PersonAppoActivity;
import com.suse.susenews.activity.PersonMsgActivity;
import com.suse.susenews.bean.PersonRelateBean;
import com.suse.susenews.fragment.PersonFragment;

import java.util.List;
import java.util.Map;

public class PersonRelateAdapter extends RecyclerView.Adapter<PersonRelateAdapter.ViewHolder>{

    private List<PersonRelateBean> data;
    private Context context;

    public PersonRelateAdapter(List<PersonRelateBean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_person_adapter, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        //设置监听
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                switch (position){
                    case 0:context.startActivity(new Intent(context, PersonAppoActivity.class));
                        break;
                    case 1:context.startActivity(new Intent(context, PersonMsgActivity.class));
                        break;
                    default:
                        break;
                    }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        PersonRelateBean bean = data.get(i);
        viewHolder.icon.setImageResource(bean.getIcon());
        viewHolder.title.setText(bean.getTitle());
        //判断是否是最后一个，确定是否取消分割线
        if (i == data.size()-1){
            viewHolder.view_line.setVisibility(View.GONE);
        }
        //判断是否有新消息，确定是否显示红色提示小点
        isNews();
    }

    private void isNews() {
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView title;
        ImageView iv_red_point;
        View view_line;

        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.iv_icon);
            title = itemView.findViewById(R.id.tv_title);
            iv_red_point = itemView.findViewById(R.id.iv_red_point);
            view_line = itemView.findViewById(R.id.view_line);

            view = itemView;
        }
    }
}
