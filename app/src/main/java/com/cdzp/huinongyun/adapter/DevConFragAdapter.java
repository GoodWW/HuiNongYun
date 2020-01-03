package com.cdzp.huinongyun.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.bean.DevConAutoParam;
import com.cdzp.huinongyun.bean.DevOnClickInfo;
import com.cdzp.huinongyun.bean.HelioGreAutoParam;
import com.zhy.autolayout.utils.AutoUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 设备控制适配器
 */

public class DevConFragAdapter extends Adapter<DevConFragAdapter.MyViewHolder> {

    private DevConAutoParam data;
    private HelioGreAutoParam data1;
    private boolean isSunlight;
    private Context context;

    public DevConFragAdapter(boolean isSunlight, Context context) {
        this.isSunlight = isSunlight;
        this.context = context;
    }

    public DevConAutoParam getData() {
        return data;
    }

    public HelioGreAutoParam getData1() {
        return data1;
    }

    public void setData(DevConAutoParam data, HelioGreAutoParam data1, boolean isSunlight) {
        this.data = data;
        this.data1 = data1;
        this.isSunlight = isSunlight;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dev_con_frag, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (isSunlight) {
            switch (data1.getList().get(position).getImage()) {
                case 0:
                    holder.iv_head.setBackgroundResource(R.drawable.waizheyang);
                    break;
                case 1:
                    holder.iv_head.setBackgroundResource(R.drawable.neizheyang);
                    break;
                case 2:
                    holder.iv_head.setBackgroundResource(R.drawable.neizheyang);
                    break;
                case 3:
                    holder.iv_head.setBackgroundResource(R.drawable.dingchuang);
                    break;
                case 4:
                    holder.iv_head.setBackgroundResource(R.drawable.cechuang);
                    break;
                case 5:
                    holder.iv_head.setBackgroundResource(R.drawable.houchuang);
                    break;
                case 6:
                    holder.iv_head.setBackgroundResource(R.drawable.huanliufengji);
                    break;
                case 7:
                    holder.iv_head.setBackgroundResource(R.drawable.fengji);
                    break;
                case 8:
                    holder.iv_head.setBackgroundResource(R.drawable.shilianchuang);
                    break;
                case 9:
                    holder.iv_head.setBackgroundResource(R.drawable.beng);
                    break;
                case 10:
                    holder.iv_head.setBackgroundResource(R.drawable.fengji);
                    break;
                case 11:
                    holder.iv_head.setBackgroundResource(R.drawable.buguangdeng);
                    break;
                default:
                    holder.iv_head.setBackgroundResource(R.drawable.waizheyang);
                    break;
            }
            holder.tv1.setText(data1.getList().get(position).getDevName());
            switch (data1.getList().get(position).getDevCtlMode()) {
                case 0:
                    holder.tv2.setText(context.getString(R.string.rem_con_str3));
                    break;
                case 1:
                    holder.tv2.setText(context.getString(R.string.rem_con_str4));
                    break;
                case 2:
                    holder.tv2.setText(context.getString(R.string.rem_con_str5));
                    break;
                default:
                    holder.tv2.setText(context.getString(R.string.rem_con_str6));
                    break;
            }
            switch (data1.getList().get(position).getZhengFanZhuan()) {
                case 0:
                    holder.iv2.setVisibility(View.GONE);
                    if (data1.getList().get(position).isOne()) {
                        holder.tv3.setText(context.getString(R.string.rem_con_str12));
                        holder.iv1.setBackgroundResource(R.drawable.dev_item_click_on);
                    } else {
                        holder.tv3.setText(context.getString(R.string.rem_con_str7));
                        holder.iv1.setBackgroundResource(R.drawable.dev_item_click_off);
                    }
                    break;
                case 1:
                    holder.iv2.setVisibility(View.VISIBLE);
                    if (data1.getList().get(position).isOne()) {
                        holder.tv3.setText(context.getString(R.string.rem_con_str10));
                        holder.iv1.setBackgroundResource(R.drawable.dev_item_click_on);
                        holder.iv2.setBackgroundResource(R.drawable.dev_item_click_off);
                    } else if (data1.getList().get(position).isTwo()) {
                        holder.tv3.setText(context.getString(R.string.rem_con_str11));
                        holder.iv1.setBackgroundResource(R.drawable.dev_item_click_off);
                        holder.iv2.setBackgroundResource(R.drawable.dev_item_click_on);
                    } else {
                        holder.tv3.setText(context.getString(R.string.rem_con_str7));
                        holder.iv1.setBackgroundResource(R.drawable.dev_item_click_off);
                        holder.iv2.setBackgroundResource(R.drawable.dev_item_click_off);
                    }
//                    holder.iv2.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            EventBus.getDefault().post(new DevOnClickInfo(2, position));
//                        }
//                    });
                    break;
                case 2:
                    holder.iv2.setVisibility(View.VISIBLE);
                    if (data1.getList().get(position).isOne()) {
                        holder.tv3.setText(context.getString(R.string.rem_con_str8));
                        holder.iv1.setBackgroundResource(R.drawable.dev_item_click_on);
                        holder.iv2.setBackgroundResource(R.drawable.dev_item_click_off);
                    } else if (data1.getList().get(position).isTwo()) {
                        holder.tv3.setText(context.getString(R.string.rem_con_str9));
                        holder.iv1.setBackgroundResource(R.drawable.dev_item_click_off);
                        holder.iv2.setBackgroundResource(R.drawable.dev_item_click_on);
                    } else {
                        holder.tv3.setText(context.getString(R.string.rem_con_str7));
                        holder.iv1.setBackgroundResource(R.drawable.dev_item_click_off);
                        holder.iv2.setBackgroundResource(R.drawable.dev_item_click_off);
                    }
//                    holder.iv2.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            EventBus.getDefault().post(new DevOnClickInfo(2, position));
//                        }
//                    });
                    break;
                default:
                    holder.iv2.setVisibility(View.VISIBLE);
                    break;
            }

//            holder.iv1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    EventBus.getDefault().post(new DevOnClickInfo(1, position));
//                }
//            });
        } else {
            switch (data.getList().get(position).getImage()) {
                case 0:
                    holder.iv_head.setBackgroundResource(R.drawable.waizheyang);
                    break;
                case 1:
                    holder.iv_head.setBackgroundResource(R.drawable.neizheyang);
                    break;
                case 2:
                    holder.iv_head.setBackgroundResource(R.drawable.neizheyang);
                    break;
                case 3:
                    holder.iv_head.setBackgroundResource(R.drawable.dingchuang);
                    break;
                case 4:
                    holder.iv_head.setBackgroundResource(R.drawable.cechuang);
                    break;
                case 5:
                    holder.iv_head.setBackgroundResource(R.drawable.houchuang);
                    break;
                case 6:
                    holder.iv_head.setBackgroundResource(R.drawable.huanliufengji);
                    break;
                case 7:
                    holder.iv_head.setBackgroundResource(R.drawable.fengji);
                    break;
                case 8:
                    holder.iv_head.setBackgroundResource(R.drawable.shilianchuang);
                    break;
                case 9:
                    holder.iv_head.setBackgroundResource(R.drawable.beng);
                    break;
                case 10:
                    holder.iv_head.setBackgroundResource(R.drawable.fengji);
                    break;
                case 11:
                    holder.iv_head.setBackgroundResource(R.drawable.buguangdeng);
                    break;
                default:
                    holder.iv_head.setBackgroundResource(R.drawable.waizheyang);
                    break;
            }
            holder.tv1.setText(data.getList().get(position).getDevName());
            switch (data.getList().get(position).getDevCtlMode()) {
                case 0:
                    holder.tv2.setText(context.getString(R.string.rem_con_str3));
                    break;
                case 1:
                    holder.tv2.setText(context.getString(R.string.rem_con_str4));
                    break;
                case 2:
                    holder.tv2.setText(context.getString(R.string.rem_con_str5));
                    break;
                default:
                    holder.tv2.setText(context.getString(R.string.rem_con_str6));
                    break;
            }
            switch (data.getList().get(position).getZhengFanZhuan()) {
                case 0:
                    holder.iv2.setVisibility(View.GONE);
                    if (data.getList().get(position).isOne()) {
                        holder.tv3.setText(context.getString(R.string.rem_con_str12));
                        holder.iv1.setBackgroundResource(R.drawable.dev_item_click_on);
                    } else {
                        holder.tv3.setText(context.getString(R.string.rem_con_str7));
                        holder.iv1.setBackgroundResource(R.drawable.dev_item_click_off);
                    }
                    break;
                case 1:
                    holder.iv2.setVisibility(View.VISIBLE);
                    if (data.getList().get(position).isOne()) {
                        holder.tv3.setText(context.getString(R.string.rem_con_str10));
                        holder.iv1.setBackgroundResource(R.drawable.dev_item_click_on);
                        holder.iv2.setBackgroundResource(R.drawable.dev_item_click_off);
                    } else if (data.getList().get(position).isTwo()) {
                        holder.tv3.setText(context.getString(R.string.rem_con_str11));
                        holder.iv1.setBackgroundResource(R.drawable.dev_item_click_off);
                        holder.iv2.setBackgroundResource(R.drawable.dev_item_click_on);
                    } else {
                        holder.tv3.setText(context.getString(R.string.rem_con_str7));
                        holder.iv1.setBackgroundResource(R.drawable.dev_item_click_off);
                        holder.iv2.setBackgroundResource(R.drawable.dev_item_click_off);
                    }
//                    holder.iv2.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            EventBus.getDefault().post(new DevOnClickInfo(2, position));
//                        }
//                    });
                    break;
                case 2:
                    holder.iv2.setVisibility(View.VISIBLE);
                    if (data.getList().get(position).isOne()) {
                        holder.tv3.setText(context.getString(R.string.rem_con_str8));
                        holder.iv1.setBackgroundResource(R.drawable.dev_item_click_on);
                        holder.iv2.setBackgroundResource(R.drawable.dev_item_click_off);
                    } else if (data.getList().get(position).isTwo()) {
                        holder.tv3.setText(context.getString(R.string.rem_con_str9));
                        holder.iv1.setBackgroundResource(R.drawable.dev_item_click_off);
                        holder.iv2.setBackgroundResource(R.drawable.dev_item_click_on);
                    } else {
                        holder.tv3.setText(context.getString(R.string.rem_con_str7));
                        holder.iv1.setBackgroundResource(R.drawable.dev_item_click_off);
                        holder.iv2.setBackgroundResource(R.drawable.dev_item_click_off);
                    }
//                    holder.iv2.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            EventBus.getDefault().post(new DevOnClickInfo(2, position));
//                        }
//                    });
                    break;
                default:
                    holder.iv2.setVisibility(View.VISIBLE);
                    break;
            }

//            holder.iv1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    EventBus.getDefault().post(new DevOnClickInfo(1, position));
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        if (isSunlight) {
            return data1 == null ? 0 : data1.getList().size();
        } else {
            return data == null ? 0 : data.getList().size();
        }
    }

    class MyViewHolder extends ViewHolder implements View.OnClickListener {
        private ImageView iv_head, iv1, iv2;
        private TextView tv1, tv2, tv3;

        public MyViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            iv_head = itemView.findViewById(R.id.iv_idcf_head);
            iv1 = itemView.findViewById(R.id.iv_idcf1);
            iv2 = itemView.findViewById(R.id.iv_idcf2);
            tv1 = itemView.findViewById(R.id.tv_idcf1);
            tv2 = itemView.findViewById(R.id.tv_idcf2);
            tv3 = itemView.findViewById(R.id.tv_idcf3);
            itemView.findViewById(R.id.ll_idcf).setOnClickListener(this);
            iv1.setOnClickListener(this);
            iv2.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.ll_idcf:
                    EventBus.getDefault().post(new DevOnClickInfo(0, getAdapterPosition()));
                    break;
                case R.id.iv_idcf1:
                    EventBus.getDefault().post(new DevOnClickInfo(1, getAdapterPosition()));
                    break;
                case R.id.iv_idcf2:
                    EventBus.getDefault().post(new DevOnClickInfo(2, getAdapterPosition()));
                    break;
            }
        }
    }
}
