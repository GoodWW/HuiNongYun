package com.cdzp.huinongyun.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.cdzp.huinongyun.R;

import java.util.ArrayList;
import java.util.List;

public class PlanIrrAdapter extends Adapter<PlanIrrAdapter.MyViewHolder> {

    private List<String[]> list;
    private List<Boolean> irrList_L, temIrrList_L;
    private List<Boolean> irrList_H, temIrrList_H;

    public PlanIrrAdapter() {
        list = new ArrayList<>();
        irrList_L = new ArrayList<>();
        irrList_H = new ArrayList<>();
        temIrrList_L = new ArrayList<>();
        temIrrList_H = new ArrayList<>();
    }

    public List<String[]> getList() {
        return list;
    }

    public List<Boolean> getIrrList_L() {
        return irrList_L;
    }

    public List<Boolean> getIrrList_H() {
        return irrList_H;
    }

    public void setIrriZone_L(long binary_L) {
        for (int i = 0; i < irrList_L.size(); i++) {
            if ((binary_L & 1l << i) != 0) {
                irrList_L.set(i, true);
                temIrrList_L.set(i, true);
            } else {
                irrList_L.set(i, false);
                temIrrList_L.set(i, false);
            }
        }
    }

    public void setIrriZone_H(long binary_H) {
        for (int i = 0; i < irrList_H.size(); i++) {
            if ((binary_H & 1l << i) != 0) {
                irrList_H.set(i, true);
                temIrrList_H.set(i, true);
            } else {
                irrList_H.set(i, false);
                temIrrList_H.set(i, false);
            }
        }
    }

    public void notifyDataHL() {
        irrList_L.clear();
        irrList_L.addAll(temIrrList_L);
        irrList_H.clear();
        irrList_H.addAll(temIrrList_H);
    }

    public void cancelHL() {
        temIrrList_L.clear();
        temIrrList_L.addAll(irrList_L);
        temIrrList_H.clear();
        temIrrList_H.addAll(irrList_H);
        notifyDataSetChanged();
    }

    public List<Boolean> getTemIrrList_L() {
        return temIrrList_L;
    }

    public List<Boolean> getTemIrrList_H() {
        return temIrrList_H;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.irr_adapter_item2, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (list.get(position)[1].equals("1") && !list.get(position)[2].equals("1")) {
            holder.cb.setClickable(true);
            holder.cb.setBackgroundColor(0x00ffffff);
            if (position < 64 && position < temIrrList_L.size()) {
                holder.cb.setChecked(temIrrList_L.get(position));
            } else if (position > 63 && position - 64 < temIrrList_H.size()) {
                holder.cb.setChecked(temIrrList_H.get(position - 64));
            }
        } else {
            holder.cb.setClickable(false);
            holder.cb.setBackgroundColor(0xFFdcdcdc);
            if (position < 64 && position < temIrrList_L.size()) {
                temIrrList_L.set(position, false);
                holder.cb.setChecked(temIrrList_L.get(position));
            } else if (position > 63 && position - 64 < temIrrList_H.size()) {
                temIrrList_H.set(position - 64, false);
                holder.cb.setChecked(temIrrList_H.get(position - 64));
            }
        }
        holder.cb.setText(list.get(position)[0]);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends ViewHolder {
        private CheckBox cb;

        public MyViewHolder(View itemView) {
            super(itemView);
            cb = itemView.findViewById(R.id.cb);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = getAdapterPosition();
                    if (position < 64 && position < temIrrList_L.size()) {
                        temIrrList_L.set(position, isChecked);
                    } else if (position > 63 && position - 64 < temIrrList_H.size()) {
                        temIrrList_H.set(position - 64, isChecked);
                    }
                }
            });
        }
    }
}
