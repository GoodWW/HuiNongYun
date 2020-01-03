package com.cdzp.huinongyun.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.util.MyCallback;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 在线施肥机
 */
public class OnlIrrAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean[] isGreen;
    private Context context;

    public OnlIrrAdapter(Context context) {
        this.context = context;
        isGreen = new boolean[128];
    }

    public boolean[] getIsGreen() {
        return isGreen;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OnlIrrAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.onlirr_adapter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder mHolder = (MyViewHolder) holder;
        if (isGreen[position]) {
            mHolder.iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
        } else
            mHolder.iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
        mHolder.tv.setText((position + 1) + "");
    }

    @Override
    public int getItemCount() {
        return isGreen.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            iv = itemView.findViewById(R.id.iv_onlirr_item);
            tv = itemView.findViewById(R.id.tv_onlirr_item);
        }
    }
}
