package com.cdzp.huinongyun.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史环境数据界面适配器
 */

public class HisEnvFragAdapter extends Adapter<HisEnvFragAdapter.MyViewHolder> {

    private List<String[]> list;
    private List<String[]> temList;

    public HisEnvFragAdapter() {
        list = new ArrayList<>();
        temList = new ArrayList<>();
    }

    public List<String[]> getList() {
        return list;
    }

    public List<String[]> getTemList() {
        return temList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_his_env, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_type.setText(list.get(position)[0]);
        holder.tv_max.setText(list.get(position)[1]);
        holder.tv_min.setText(list.get(position)[2]);
        holder.tv_mean.setText(list.get(position)[3]);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends ViewHolder {

        private TextView tv_type, tv_max, tv_min, tv_mean;

        public MyViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            tv_type = itemView.findViewById(R.id.tv_ihe_type);
            tv_max = itemView.findViewById(R.id.tv_ihe_max);
            tv_min = itemView.findViewById(R.id.tv_ihe_min);
            tv_mean = itemView.findViewById(R.id.tv_ihe_mean);
        }
    }
}
