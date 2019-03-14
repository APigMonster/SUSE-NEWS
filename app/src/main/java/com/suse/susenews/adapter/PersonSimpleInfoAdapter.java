package com.suse.susenews.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.suse.susenews.activity.UserNameActivity;
import com.suse.susenews.bean.PersonRelateBean;
import com.suse.susenews.bean.PersonSimpleInfoBean;
import com.suse.susenews.utils.CacheUtils;

import java.util.List;

public class PersonSimpleInfoAdapter extends RecyclerView.Adapter<PersonSimpleInfoAdapter.ViewHolder>{

    private List<PersonSimpleInfoBean> data;
    private Context context;

    public PersonSimpleInfoAdapter(List<PersonSimpleInfoBean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_person_simple_info_adapter, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        //设置监听
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                String value = data.get(position).getValue();
                switch (position){
                    case 0:
                        Intent intent = new Intent(context, UserNameActivity.class);
                        intent.putExtra("VALUE", value);
                        context.startActivity(intent);
                        break;
                    case 1:
                       //弹出一个单选dialog
                        showSingleDialog();
                        break;
                    default:
                        break;
                    }
            }

            private void showSingleDialog() {
                final String[] items = {"男", "女"};
                new AlertDialog.Builder(context)
                        .setTitle("性别")
                        .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //把选择的值存到数据库
                                CacheUtils.putString(context, CacheUtils.USER_SEX, items[which]);
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        PersonSimpleInfoBean bean = data.get(i);

        viewHolder.name.setText(bean.getName());
        viewHolder.value.setText(bean.getValue());

        //判断是否是最后一个，确定是否取消分割线
        if (i == data.size()-1){
            viewHolder.view_line.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView value;
        View view_line;

        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            value = itemView.findViewById(R.id.tv_value);
            view_line = itemView.findViewById(R.id.view_line);

            view = itemView;
        }
    }
}
