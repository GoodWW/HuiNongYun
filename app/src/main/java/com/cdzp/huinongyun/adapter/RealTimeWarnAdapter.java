package com.cdzp.huinongyun.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.util.TypedValue;
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
 * 实时预警适配器
 */
public class RealTimeWarnAdapter extends Adapter<RealTimeWarnAdapter.MyViewHolder> {

    private Context context;
    private List<String[]> list;
    private boolean isEnglish;

    public RealTimeWarnAdapter(Context context, boolean isEnglish) {
        this.context = context;
        this.isEnglish = isEnglish;
        list = new ArrayList<>();
    }

    public List<String[]> getList() {
        return list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_real_time_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv1.setText(list.get(position)[0]);
        holder.tv2.setText(list.get(position)[1]);
        holder.tv3.setText(list.get(position)[2]);
        switch (list.get(position)[3]) {
            case "0.1":
                holder.iv.setBackgroundResource(R.drawable.round_blue);
                holder.tv5.setTextColor(context.getResources().getColor(R.color.realtimewarn1));
                break;
            case "1.1":
                holder.iv.setBackgroundResource(R.drawable.round_red);
                holder.tv5.setTextColor(context.getResources().getColor(R.color.realtimewarn2));
                break;
            default:
                holder.iv.setBackgroundResource(R.drawable.round_green);
                holder.tv5.setTextColor(context.getResources().getColor(R.color.realtimewarn3));
                break;
        }
        holder.tv4.setText(list.get(position)[4]);
        holder.tv5.setText(list.get(position)[5]);
        holder.tv6.setText(list.get(position)[6]);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends ViewHolder {

        private TextView tv1, tv2, tv3, tv4, tv5, tv6;
        private ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            tv1 = itemView.findViewById(R.id.tv_irta1);
            tv2 = itemView.findViewById(R.id.tv_irta2);
            tv3 = itemView.findViewById(R.id.tv_irta3);
            tv4 = itemView.findViewById(R.id.tv_irta4);
            tv5 = itemView.findViewById(R.id.tv_irta5);
            tv6 = itemView.findViewById(R.id.tv_irta6);
            iv = itemView.findViewById(R.id.iv_irta);
            float px1, px2, px3, px4, px5;
            if (isEnglish) {
                px1 = 16;
                px2 = 14;
                px3 = 14;
                px4 = 14;
                px5 = 14;
            } else {
                px1 = 20;
                px2 = 18;
                px3 = 16;
                px4 = 16;
                px5 = 16;
            }
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, px1);
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, px2);
            tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, px3);
            tv4.setTextSize(TypedValue.COMPLEX_UNIT_SP, px4);
            tv5.setTextSize(TypedValue.COMPLEX_UNIT_SP, px5);
            tv6.setTextSize(TypedValue.COMPLEX_UNIT_SP, px3);
            AutoUtils.autoSize(itemView);
        }

    }
}
