package com.cdzp.huinongyun.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.fragment.OnlFert;
import com.cdzp.huinongyun.util.AdapterListener;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * 在线施肥机水罐水池配置适配器
 */
public class OnlWaterConfigAdapter extends RecyclerView.Adapter<OnlWaterConfigAdapter.MyViewHolder> {

    private Context context;

    public OnlWaterConfigAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.onl_water_config_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        switch (i) {
            case 0:
                myViewHolder.tv.setText(context.getString(R.string.onl_fert156));
                break;
            case 1:
                myViewHolder.tv.setText(context.getString(R.string.onl_fert157));
                break;
            case 2:
                myViewHolder.tv.setText(context.getString(R.string.onl_fert158));
                break;
            case 3:
                myViewHolder.tv.setText(context.getString(R.string.onl_fert159));
                break;
            case 4:
                myViewHolder.tv.setText(context.getString(R.string.onl_fert160));
                break;
            case 5:
                myViewHolder.tv.setText(context.getString(R.string.onl_fert161));
                break;
            case 6:
                myViewHolder.tv.setText(context.getString(R.string.onl_fert162));
                break;
            case 7:
                myViewHolder.tv.setText(context.getString(R.string.onl_fert163));
                break;
            case 8:
                myViewHolder.tv.setText(context.getString(R.string.onl_fert164));
                break;
            case 9:
                myViewHolder.tv.setText(context.getString(R.string.onl_fert165));
                break;
            case 10:
                myViewHolder.tv.setText(context.getString(R.string.onl_fert166));
                break;
            case 11:
                myViewHolder.tv.setText(context.getString(R.string.onl_fert167));
                break;
            case 12:
                myViewHolder.tv.setText(context.getString(R.string.onl_fert168));
                break;
            case 13:
                myViewHolder.tv.setText(context.getString(R.string.onl_fert169));
                break;
            case 14:
                myViewHolder.tv.setText(context.getString(R.string.onl_fert170));
                break;
            case 15:
                myViewHolder.tv.setText(context.getString(R.string.onl_fert171));
                break;
            case 16:
                myViewHolder.tv.setText(context.getString(R.string.onl_fert172));
                break;
            default:
                myViewHolder.tv.setText(context.getString(R.string.onl_fert173));
                break;
        }
        if (OnlFert.config.getData().getWList().get(i).getHasTank() == 1) {
            myViewHolder.tv2.setText(context.getString(R.string.onl_fert149));
        } else {
            myViewHolder.tv2.setText(context.getString(R.string.onl_fert150));
        }
    }

    @Override
    public int getItemCount() {
        return OnlFert.config == null ? 0 : OnlFert.config.getData().getWList().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv, tv2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            tv = itemView.findViewById(R.id.tv_owci1);
            tv2 = itemView.findViewById(R.id.tv_owci2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onClick(getAdapterPosition());
                }
            });
        }
    }

    private AdapterListener listener;

    public void setListener(AdapterListener listener) {
        this.listener = listener;
    }
}
