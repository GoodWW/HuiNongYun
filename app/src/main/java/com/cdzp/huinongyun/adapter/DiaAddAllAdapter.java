package com.cdzp.huinongyun.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.util.AdapterListener;
import com.cdzp.huinongyun.util.AdapterListenerTwo;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 一键开启对话框适配器
 */
public class DiaAddAllAdapter extends Adapter<DiaAddAllAdapter.MyViewHolder> {

    private List<String[]> list;
    private Context context;
    private AdapterListenerTwo listener;
    private MyViewHolder holder;
    private int currentI;

    public DiaAddAllAdapter(Context context, AdapterListenerTwo listener, List<String[]> list) {
        this.context = context;
        this.listener = listener;
        this.list = list;
    }

    public List<String[]> getList() {
        return list;
    }


    public void setList(List<String[]> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dia_start_all, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        this.currentI = position;
        holder.tv1.setText(list.get(position)[1]);
        switch (list.get(position)[3]) {//
            case "0":
                holder.box.setChecked(true);
                Log.e("aa", "position: ===" + position);
                break;
            case "85":
                holder.box.setChecked(false);
                break;
            case "255":
                holder.box.setChecked(false);
                break;
            default:
                holder.box.setChecked(false);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv1;
        private CheckBox box;

        public MyViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            tv1 = itemView.findViewById(R.id.tv_ishc1);

            box = itemView.findViewById(R.id.cb_box);
            box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(getAdapterPosition());
                }
            });
            /*box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged( CompoundButton buttonView, boolean isChecked) {
                    listener.onCheckedChangeListener(currentI, buttonView,isChecked);
                }
            });*/
        }
    }
}
