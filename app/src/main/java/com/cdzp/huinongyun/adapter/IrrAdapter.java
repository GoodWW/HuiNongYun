package com.cdzp.huinongyun.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.util.MyCallback;
import com.zhy.autolayout.utils.AutoUtils;

public class IrrAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean[] check;
    private int valvesNum;
    private boolean isValve;//是否是阀门状态

    public IrrAdapter(boolean[] check) {
        this.check = check;
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

    public void setValve(boolean valve) {
        isValve = valve;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IrrAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.irr_adapter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder mHolder = (MyViewHolder) holder;
        mHolder.cb.setText((position + 1) + "");
        mHolder.cb.setBackgroundColor(0x00000000);
        if (isValve) {
            mHolder.cb.setChecked(check[position]);
            mHolder.cb.setClickable(false);
        } else {
            if (position < valvesNum) {
                mHolder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        check[position] = isChecked;
                        if (mcallback != null) {
                            mcallback.checkBox(position, isChecked);
                        }
                    }
                });
                mHolder.cb.setChecked(check[position]);
            } else {
                mHolder.cb.setBackgroundColor(0xFFdcdcdc);
                mHolder.cb.setClickable(false);
                mHolder.cb.setChecked(check[position]);
            }
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
        }
    }

    private MyCallback mcallback;

    public void setCallback(MyCallback mcallback) {
        this.mcallback = mcallback;
    }
}
