package com.cdzp.huinongyun.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.bean.IrriRecord;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * 在线施肥机灌溉记录适配器
 */
public class IrrRecordAdapter extends RecyclerView.Adapter<IrrRecordAdapter.MyViewHolder> {

    private List<IrriRecord> mList;
    private Context context;

    public IrrRecordAdapter(Context context) {
        this.context = context;
    }

    public void setmList(List<IrriRecord> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.irr_record_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        switch (mList.get(i).getAutoObject()) {
            case 0:
                myViewHolder.tv_iri1.setText(context.getString(R.string.onl_fert111));
                myViewHolder.tv_iri2.setText(mList.get(i).getIrrValves());
                break;
            case 1:
                myViewHolder.tv_iri1.setText(context.getString(R.string.onl_fert37));
                myViewHolder.tv_iri2.setText("TankNo" + (mList.get(i).getTankNumber() + 1));
                break;
            default:
                myViewHolder.tv_iri1.setText(context.getString(R.string.request_error2));
                myViewHolder.tv_iri2.setText(context.getString(R.string.request_error2));
                break;
        }
        myViewHolder.tv_iri3.setText(mList.get(i).getInfoName());

        if (mList.get(i).isLevelCtl()) {
            myViewHolder.tv_iri4.setText(context.getString(R.string.onl_fert112));
        } else {
            if (mList.get(i).isVolumeCtl()) {
                myViewHolder.tv_iri4.setText(context.getString(R.string.onl_fert113) +
                        mList.get(i).getPlanRunTime() + "m³");
            } else {
                myViewHolder.tv_iri4.setText(context.getString(R.string.onl_fert113) +
                        mList.get(i).getPlanRunTime());
            }
        }

        if (mList.get(i).isHasStorageTank() && mList.get(i).getAutoObject() == 0) {
            myViewHolder.tv_iri5.setText("---");
            myViewHolder.tv_iri6.setText("---");
        } else {
//            myViewHolder.tv_iri5.setText("PH(ph)：：" + mList.get(i).getEcSetValue());
//            myViewHolder.tv_iri6.setText("EC(mS)：" + mList.get(i).getPhSetValue());
            myViewHolder.tv_iri5.setText("PH(ph)：：" + mList.get(i).getPhSetValue());
            myViewHolder.tv_iri6.setText("EC(mS)：" + mList.get(i).getEcSetValue());
        }
        myViewHolder.tv_iri7.setText(context.getString(R.string.onl_fert113) +
                mList.get(i).getRealRunTime());
        myViewHolder.tv_iri8.setText("PH(ph)：" + mList.get(i).getPhRealValue());
        myViewHolder.tv_iri9.setText("EC(mS)：" + mList.get(i).getEcRealValue());
        myViewHolder.tv_iri10.setText(context.getString(R.string.onl_fert114) + mList.get(i).getIrriVolume());
        if (mList.get(i).getErrorCode() == 0) {
            myViewHolder.tv_iri11.setText(context.getString(R.string.onl_fert115));
        } else myViewHolder.tv_iri11.setText(context.getString(R.string.onl_fert116) +
                mList.get(i).getErrorCode());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_iri1, tv_iri2, tv_iri3, tv_iri4, tv_iri5, tv_iri6, tv_iri7, tv_iri8,
                tv_iri9, tv_iri10, tv_iri11;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            tv_iri1 = itemView.findViewById(R.id.tv_iri1);
            tv_iri2 = itemView.findViewById(R.id.tv_iri2);
            tv_iri3 = itemView.findViewById(R.id.tv_iri3);
            tv_iri4 = itemView.findViewById(R.id.tv_iri4);
            tv_iri5 = itemView.findViewById(R.id.tv_iri5);
            tv_iri6 = itemView.findViewById(R.id.tv_iri6);
            tv_iri7 = itemView.findViewById(R.id.tv_iri7);
            tv_iri8 = itemView.findViewById(R.id.tv_iri8);
            tv_iri9 = itemView.findViewById(R.id.tv_iri9);
            tv_iri10 = itemView.findViewById(R.id.tv_iri10);
            tv_iri11 = itemView.findViewById(R.id.tv_iri11);
        }
    }
}
