package com.cdzp.huinongyun.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据报表适配器
 */
public class DataFormAdapter extends Adapter<DataFormAdapter.MyViewHolder> {

    private List<String[]> list;
    private boolean isEnglish;

    public DataFormAdapter(boolean isEnglish) {
        this.isEnglish = isEnglish;
        list = new ArrayList<>();
    }

    public List<String[]> getList() {
        return list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_form, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv1.setText(list.get(position)[0]);
        holder.tv2.setText(list.get(position)[1]);
        holder.tv3.setText(list.get(position)[2]);
        holder.tv4.setText(list.get(position)[3]);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends ViewHolder {

        private TextView tv1, tv2, tv3, tv4;

        public MyViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            tv1 = itemView.findViewById(R.id.tv_idf1);
            tv2 = itemView.findViewById(R.id.tv_idf2);
            tv3 = itemView.findViewById(R.id.tv_idf3);
            tv4 = itemView.findViewById(R.id.tv_idf4);
            if (isEnglish) {
                tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            } else {
                tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            }
        }
    }
}
