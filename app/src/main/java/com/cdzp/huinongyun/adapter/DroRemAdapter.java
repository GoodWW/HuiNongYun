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
 * 掉线提醒适配器
 */

public class DroRemAdapter extends Adapter<ViewHolder> {

    private Context context;
    private List<String[]> list;

    public DroRemAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public List<String[]> getList() {
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new SkickyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_menu_sticky, parent, false));
        } else
            return new DroRemViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_drorem, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof DroRemViewHolder) {
            DroRemViewHolder mHolder = (DroRemViewHolder) holder;
            mHolder.tv_dro1.setText(list.get(position)[1]);
            mHolder.tv_dro2.setText(list.get(position)[2]);
            mHolder.tv_dro3.setText(list.get(position)[3]);
            mHolder.tv_dro4.setText(list.get(position)[4]);
        } else if (holder instanceof SkickyViewHolder) {
            SkickyViewHolder mHolder = (SkickyViewHolder) holder;
            switch (list.get(position)[1]) {
                case "1":
                    mHolder.iv.setBackgroundResource(R.drawable.home_dev_icon1);
                    mHolder.tv_sticky.setTextColor(context.getResources().getColor(R.color.tvMenusticky1));
                    break;
                case "2":
                    mHolder.iv.setBackgroundResource(R.drawable.home_dev_icon2);
                    mHolder.tv_sticky.setTextColor(context.getResources().getColor(R.color.tvMenusticky2));
                    break;
                case "3":
                    mHolder.iv.setBackgroundResource(R.drawable.home_dev_icon3);
                    mHolder.tv_sticky.setTextColor(context.getResources().getColor(R.color.tvMenusticky3));
                    break;
                case "4":
                    mHolder.iv.setBackgroundResource(R.drawable.home_dev_icon4);
                    mHolder.tv_sticky.setTextColor(context.getResources().getColor(R.color.tvMenusticky4));
                    break;
                case "5":
                    mHolder.iv.setBackgroundResource(R.drawable.home_dev_icon5);
                    mHolder.tv_sticky.setTextColor(context.getResources().getColor(R.color.tvMenusticky5));
                    break;
                case "6":
                    mHolder.iv.setBackgroundResource(R.drawable.home_dev_icon6);
                    mHolder.tv_sticky.setTextColor(context.getResources().getColor(R.color.tvMenusticky6));
                    break;
                case "7":
                    mHolder.iv.setBackgroundResource(R.drawable.home_dev_icon7);
                    mHolder.tv_sticky.setTextColor(context.getResources().getColor(R.color.tvMenusticky7));
                    break;
                case "8":
                    mHolder.iv.setBackgroundResource(R.drawable.home_dev_icon8);
                    mHolder.tv_sticky.setTextColor(context.getResources().getColor(R.color.tvMenusticky8));
                    break;
                case "9":
                    mHolder.iv.setBackgroundResource(R.drawable.home_dev_icon9);
                    mHolder.tv_sticky.setTextColor(context.getResources().getColor(R.color.tvMenusticky9));
                    break;
                default:
                    mHolder.iv.setBackgroundResource(R.drawable.home_dev_icon1);
                    mHolder.tv_sticky.setTextColor(context.getResources().getColor(R.color.tvMenusticky1));
                    break;
            }
            mHolder.tv_sticky.setText(list.get(position)[2]);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position)[0].equals("0")) {
            return 0;
        } else return 1;
    }

    class SkickyViewHolder extends ViewHolder {

        private ImageView iv;
        private TextView tv_sticky;

        public SkickyViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            iv = itemView.findViewById(R.id.iv_menu_sticky);
            tv_sticky = itemView.findViewById(R.id.tv_menu_sticky);
        }
    }

    class DroRemViewHolder extends ViewHolder {

        private TextView tv_dro1, tv_dro2, tv_dro3, tv_dro4;

        public DroRemViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            tv_dro1 = itemView.findViewById(R.id.tv_drorem1);
            tv_dro2 = itemView.findViewById(R.id.tv_drorem2);
            tv_dro3 = itemView.findViewById(R.id.tv_drorem3);
            tv_dro4 = itemView.findViewById(R.id.tv_drorem4);
        }
    }
}
