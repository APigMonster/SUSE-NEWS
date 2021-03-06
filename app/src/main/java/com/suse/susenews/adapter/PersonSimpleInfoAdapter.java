package com.suse.susenews.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suse.susenews.R;
import com.suse.susenews.activity.PersonInfoActivity;
import com.suse.susenews.bean.PersonSimpleInfoBean;

import java.util.List;

public class PersonSimpleInfoAdapter extends RecyclerView.Adapter<PersonSimpleInfoAdapter.ViewHolder> {

    private List<PersonSimpleInfoBean> data;
    private PersonInfoActivity context;

    public PersonSimpleInfoAdapter(List<PersonSimpleInfoBean> data, PersonInfoActivity context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_person_simple_info_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        PersonSimpleInfoBean bean = data.get(i);

        viewHolder.name.setText(bean.getName());
        viewHolder.value.setText(bean.getValue());

        //判断是否是最后一个，确定是否取消分割线
        if (i == data.size() - 1) {
            viewHolder.view_line.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView value;
        View view_line;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            value = itemView.findViewById(R.id.tv_value);
            view_line = itemView.findViewById(R.id.view_line);
        }
    }
}
