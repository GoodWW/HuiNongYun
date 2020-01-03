package com.cdzp.huinongyun.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.util.SolTerRenAdaListener;
import com.cdzp.huinongyun.util.Toasts;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 电磁阀终端改名适配器
 */
public class SolTerRenAdapter extends Adapter<SolTerRenAdapter.MyViewHolder> {

    private List<String[]> list;
    private SolTerRenAdaListener listener;
    private boolean isOnClick;//防止频繁操作
    private Context context;

    public SolTerRenAdapter(Context context) {
        this.context = context;
        isOnClick = true;
        list = new ArrayList<>();
    }

    public List<String[]> getList() {
        return list;
    }

    public void setListener(SolTerRenAdaListener listener) {
        this.listener = listener;
    }

    public void setOnClick(boolean onClick) {
        isOnClick = onClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sol_ter_ren, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.et.setText(list.get(position)[1]);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends ViewHolder {
        private EditText et;

        public MyViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            et = itemView.findViewById(R.id.et_istr);
            itemView.findViewById(R.id.btn_istr).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isOnClick) {
                        if (listener != null) {
                            listener.onClick(getAdapterPosition(), et.getText().toString());
                        }
                    } else Toasts.showInfo(context, context.getString(R.string.solenoid_str9));
                }
            });
        }
    }
}
