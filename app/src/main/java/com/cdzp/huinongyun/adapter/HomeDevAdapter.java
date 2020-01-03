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
 * 首页添加设备适配器
 */

public class HomeDevAdapter extends Adapter<HomeDevAdapter.MyViewHolder> {

    private Context context;
    private List<String[]> list;

    public HomeDevAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public List<String[]> getList() {
        return list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_dev_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        switch (list.get(position)[0]) {
            case "1":
                holder.iv.setBackground(context.getResources().getDrawable(R.drawable.home_dev_icon1));
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.tv_warn.setVisibility(View.VISIBLE);
                break;
            case "2":
                holder.iv.setBackground(context.getResources().getDrawable(R.drawable.home_dev_icon2));
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.tv_warn.setVisibility(View.VISIBLE);
                break;
            case "3":
                holder.iv.setBackground(context.getResources().getDrawable(R.drawable.home_dev_icon3));
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.tv_warn.setVisibility(View.VISIBLE);
                break;
            case "4":
                holder.iv.setBackground(context.getResources().getDrawable(R.drawable.home_dev_icon4));
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.tv_warn.setVisibility(View.VISIBLE);
                break;
            case "5":
                holder.iv.setBackground(context.getResources().getDrawable(R.drawable.home_dev_icon5));
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.tv_warn.setVisibility(View.VISIBLE);
                break;
            case "6":
                holder.iv.setBackground(context.getResources().getDrawable(R.drawable.home_dev_icon6));
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.tv_warn.setVisibility(View.VISIBLE);
                break;
            case "7":
                holder.iv.setBackground(context.getResources().getDrawable(R.drawable.home_dev_icon7));
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.tv_warn.setVisibility(View.VISIBLE);
                break;
            case "8":
                holder.iv.setBackground(context.getResources().getDrawable(R.drawable.home_dev_icon8));
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.tv_warn.setVisibility(View.VISIBLE);
                break;
            case "9":
                holder.iv.setBackground(context.getResources().getDrawable(R.drawable.home_dev_icon9));
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.tv_warn.setVisibility(View.VISIBLE);
                break;
            case "10001":
                holder.iv.setBackground(context.getResources().getDrawable(R.drawable.in_add_icon));
                holder.tv_status.setVisibility(View.GONE);
                holder.tv_warn.setVisibility(View.GONE);
                break;
            default:
                holder.iv.setBackground(context.getResources().getDrawable(R.drawable.home_dev_icon1));
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.tv_warn.setVisibility(View.VISIBLE);
                break;
        }
        holder.tv_name.setText(list.get(position)[1]);
        holder.tv_status.setText(context.getString(R.string.add_dev_frag_str1) + list.get(position)[2] +
                context.getString(R.string.add_dev_frag_str2) +
                list.get(position)[3] + context.getString(R.string.add_dev_frag_str3));
        if (list.get(position)[4].replace(" ", "").equals("0")) {
            holder.tv_warn.setVisibility(View.GONE);
        } else {
            holder.tv_warn.setVisibility(View.VISIBLE);
            holder.tv_warn.setText(list.get(position)[4]);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends ViewHolder {

        ImageView iv;
        TextView tv_name, tv_status, tv_warn;

        public MyViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            iv = itemView.findViewById(R.id.iv_hdi);
            tv_name = itemView.findViewById(R.id.tv_hdi_name);
            tv_status = itemView.findViewById(R.id.tv_hdi_status);
            tv_warn = itemView.findViewById(R.id.tv_hdi_warn);
        }
    }
}
