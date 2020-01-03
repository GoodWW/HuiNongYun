package com.cdzp.huinongyun.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.util.MyCallback;
import com.zhy.autolayout.utils.AutoUtils;

public class FerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean[] check, fertChshow;
    private Context context;

    public FerAdapter(boolean[] check, Context context) {
        this.context = context;
        this.check = check;
    }

    public void setCheck(boolean[] check) {
        this.check = check;
    }

    public boolean[] getCheck() {
        return check;
    }

    public void setFertChshow(boolean[] fertChshow) {
        this.fertChshow = fertChshow;
    }

    public boolean[] getFertChshow() {
        return fertChshow;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FerAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.irr_adapter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder mHolder = (MyViewHolder) holder;
        switch (position) {
            case 0:
                mHolder.cb.setText(context.getString(R.string.fert_str19));
                break;
            case 1:
                mHolder.cb.setText("A");
                break;
            case 2:
                mHolder.cb.setText("B");
                break;
            case 3:
                mHolder.cb.setText("C");
                break;
            case 4:
                mHolder.cb.setText("D");
                break;
        }
        mHolder.cb.setBackgroundColor(0x00000000);
        if (fertChshow[position]) {
            mHolder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    check[position] = isChecked;
                    if (mcallback != null) {
                        mcallback.checkBox(position, isChecked);
                    }
                }
            });
//            mHolder.cb.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    boolean b = ((CheckBox) v).isChecked();
//                    check[position] = b;
//                    if (mcallback != null) {
//                        mcallback.checkBox(position, b);
//                    }
//                }
//            });
            mHolder.cb.setChecked(check[position]);
        } else {
            mHolder.cb.setBackgroundColor(0xFFdcdcdc);
            mHolder.cb.setClickable(false);
            mHolder.cb.setChecked(check[position]);
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
