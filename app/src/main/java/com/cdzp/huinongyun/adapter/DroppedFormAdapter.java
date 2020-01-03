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
 * 掉线报表适配器
 */
public class DroppedFormAdapter extends Adapter<DroppedFormAdapter.MyViewHolder> {

    private List<String[]> list;

    public DroppedFormAdapter() {
        list = new ArrayList<>();
    }

    public List<String[]> getList() {
        return list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dropped_form, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv1.setText(list.get(position)[0]);
        holder.tv2.setText(list.get(position)[1]);
        holder.tv3.setText(list.get(position)[2]);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends ViewHolder {
        private TextView tv1, tv2, tv3;

        public MyViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            tv1 = itemView.findViewById(R.id.tv_idrf1);
            tv2 = itemView.findViewById(R.id.tv_idrf2);
            tv3 = itemView.findViewById(R.id.tv_idrf3);
        }
    }
}
