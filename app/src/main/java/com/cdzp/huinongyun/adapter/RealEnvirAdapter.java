package com.cdzp.huinongyun.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 实时环境数据适配器
 */

public class RealEnvirAdapter extends Adapter<RealEnvirAdapter.MyViewHolder> {

    private Context context;
    private List<String[]> list;

    public RealEnvirAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public List<String[]> getList() {
        return list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rea_evn, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        switch (list.get(position)[2]) {
            case "0":
                holder.iv.setBackgroundResource(R.drawable.ambient_mr);
                break;
            case "1":
                holder.iv.setBackgroundResource(R.drawable.ambient_kqwd);
                break;
            case "2":
                holder.iv.setBackgroundResource(R.drawable.ambient_kqsd);
                break;
            case "3":
                holder.iv.setBackgroundResource(R.drawable.ambient_trwd);
                break;
            case "4":
                holder.iv.setBackgroundResource(R.drawable.ambient_trsd);
                break;
            case "5":
                holder.iv.setBackgroundResource(R.drawable.ambient_gzcd);
                break;
            case "6":
                holder.iv.setBackgroundResource(R.drawable.ambient_swwd);
                break;
            case "7":
                holder.iv.setBackgroundResource(R.drawable.ambient_swsd);
                break;
            case "8":
                holder.iv.setBackgroundResource(R.drawable.ambient_sngz);
                break;
            case "9":
                holder.iv.setBackgroundResource(R.drawable.ambient_co2);
                break;
            case "10":
                holder.iv.setBackgroundResource(R.drawable.ambient_ec);
                break;
            case "11":
                holder.iv.setBackgroundResource(R.drawable.ambient_ph);
                break;
            case "12":
                holder.iv.setBackgroundResource(R.drawable.ambient_ry);
                break;
            case "13":
                holder.iv.setBackgroundResource(R.drawable.ambient_snfs);
                break;
            case "14":
                holder.iv.setBackgroundResource(R.drawable.ambient_sx);
                break;
            case "15":
                holder.iv.setBackgroundResource(R.drawable.ambient_sw);
                break;
            case "16":
                holder.iv.setBackgroundResource(R.drawable.ambient_pm);
                break;
            case "17":
                holder.iv.setBackgroundResource(R.drawable.ambient_swei);
                break;
            default:
                holder.iv.setBackgroundResource(R.drawable.ambient_mr);
                break;
        }
        holder.tv1.setText(list.get(position)[0]);
        holder.tv2.setText(list.get(position)[1]);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends ViewHolder {
        private ImageView iv;
        private TextView tv1, tv2;

        public MyViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            iv = itemView.findViewById(R.id.iv_ire);
            tv1 = itemView.findViewById(R.id.tv_ire1);
            tv2 = itemView.findViewById(R.id.tv_ire2);
        }
    }

}
