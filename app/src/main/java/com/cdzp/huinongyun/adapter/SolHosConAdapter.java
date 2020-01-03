package com.cdzp.huinongyun.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.util.AdapterListener;
import com.cdzp.huinongyun.util.Toasts;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 电磁阀主机控制适配器
 */
public class SolHosConAdapter extends Adapter<SolHosConAdapter.MyViewHolder> {

    private List<String[]> list;
    private Context context;
    private AdapterListener listener;
    private boolean isOnClick;//防止频繁点击
    private MyViewHolder holder;

    public SolHosConAdapter(Context context, AdapterListener listener) {
        this.context = context;
        this.listener = listener;
        list = new ArrayList<>();
        isOnClick = true;
    }

    public List<String[]> getList() {
        return list;
    }

    public void setOnClick(boolean onClick) {
        isOnClick = onClick;
    }

    public void setList(List<String[]> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_sol_hos_con, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv1.setText(list.get(position)[1]);
        holder.tv2.setText(list.get(position)[2]);
        switch (list.get(position)[3]) {//
            case "0":
                holder.tv3.setText(context.getString(R.string.solenoid_str30));
                holder.btn.setText(context.getString(R.string.solenoid_str31));
                holder.btn.setBackgroundResource(R.drawable.item_sol_hos_off_click);
                break;
            case "85":
                holder.tv3.setText(context.getString(R.string.solenoid_str32));
                holder.btn.setText(context.getString(R.string.solenoid_str33));
                holder.btn.setBackgroundResource(R.drawable.item_sol_hos_inquire_click);
                break;
            case "255":
                holder.tv3.setText(context.getString(R.string.solenoid_str34));
                holder.btn.setText(context.getString(R.string.solenoid_str35));
                holder.btn.setBackgroundResource(R.drawable.item_sol_hos_on_click);
                break;
            default:
                holder.tv3.setText(context.getString(R.string.request_error2));
                holder.btn.setText(context.getString(R.string.request_error2));
                break;
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv1, tv2, tv3;
        private Button btn;

        public MyViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            tv1 = itemView.findViewById(R.id.tv_ishc1);
            tv2 = itemView.findViewById(R.id.tv_ishc2);
            tv3 = itemView.findViewById(R.id.tv_ishc3);
            btn = itemView.findViewById(R.id.btn_ishc);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isOnClick) {
                        listener.onClick(getAdapterPosition());
                    } else Toasts.showInfo(context, context.getString(R.string.solenoid_str9));
                }
            });
        }
    }
}
