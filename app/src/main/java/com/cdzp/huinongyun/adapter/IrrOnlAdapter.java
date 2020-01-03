package com.cdzp.huinongyun.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.util.MyCallback;
import com.zhy.autolayout.utils.AutoUtils;

public class IrrOnlAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean[] check;
    private int valvesNum;

    public IrrOnlAdapter() {
        check = new boolean[128];
    }

    public boolean[] getCheck() {
        return check;
    }

    public void setCheck(boolean[] check) {
        this.check = check;
    }

    public void setValvesNum(int valvesNum) {
        this.valvesNum = valvesNum;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IrrOnlAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.irr_adapter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder mHolder = (MyViewHolder) holder;
        mHolder.cb.setText((position + 1) + "");
        mHolder.cb.setChecked(check[position]);
        if (position < valvesNum) {
            mHolder.cb.setBackgroundColor(0x00000000);
            mHolder.cb.setClickable(true);
        } else {
            mHolder.cb.setBackgroundColor(0xFFdcdcdc);
            mHolder.cb.setClickable(false);
        }
    }

    @Override
    public int getItemCount() {
        return check.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private CheckBox cb;

        public MyViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            cb = itemView.findViewById(R.id.cb);
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    check[getAdapterPosition()] = cb.isChecked();
                    if (mcallback != null) {
                        mcallback.checkBox(getAdapterPosition(), check[getAdapterPosition()]);
                    }
                }
            });

        }
    }

    private MyCallback mcallback;

    public void setCallback(MyCallback mcallback) {
        this.mcallback = mcallback;
    }
}
