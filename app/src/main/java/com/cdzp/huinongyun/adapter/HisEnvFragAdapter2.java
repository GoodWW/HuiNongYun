package com.cdzp.huinongyun.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

public class HisEnvFragAdapter2 extends Adapter<HisEnvFragAdapter2.MyViewHolder> {

    private List<String[]> list;

    public HisEnvFragAdapter2() {
        list = new ArrayList<>();
    }

    public List<String[]> getList() {
        return list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_his_env2, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_type.setText(list.get(position)[0]);
        holder.tv_time.setText(list.get(position)[1]);
        holder.tv_value.setText(list.get(position)[2]);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends ViewHolder {

        private TextView tv_time, tv_value, tv_type;

        public MyViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            tv_type = itemView.findViewById(R.id.tv_ihe2_type);
            tv_time = itemView.findViewById(R.id.tv_ihe2_time);
            tv_value = itemView.findViewById(R.id.tv_ihe2_value);
        }
    }
}
