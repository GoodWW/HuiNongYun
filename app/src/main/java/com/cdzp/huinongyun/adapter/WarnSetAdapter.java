package com.cdzp.huinongyun.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * 预警设置适配器
 */
public class WarnSetAdapter extends Adapter<WarnSetAdapter.MyViewHolder> {

    private Context context;
    private List<String[]> list;
    private boolean isEnglish;

    public WarnSetAdapter(Context context, boolean isEnglish) {
        this.context = context;
        this.isEnglish = isEnglish;
    }

    public List<String[]> getList() {
        return list;
    }

    public void setList(List<String[]> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_warn_set, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv.setText(list.get(position)[7]);

        if (list.get(position)[6].equals("-1")) {
            holder.cb.setChecked(false);
        } else holder.cb.setChecked(true);

        holder.et1.setText(list.get(position)[4]);
        holder.et2.setText(list.get(position)[5]);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class MyViewHolder extends ViewHolder {

        private CheckBox cb;
        private TextView tv;
        private EditText et1, et2;

        public MyViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            cb = itemView.findViewById(R.id.cb_iws);
            tv = itemView.findViewById(R.id.tv_iws);
            et1 = itemView.findViewById(R.id.et_iws1);
            et2 = itemView.findViewById(R.id.et_iws2);
            float px1;
            if (isEnglish) {
                px1 = 12;
            } else {
                px1 = 14;
            }
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, px1);

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        list.get(getAdapterPosition())[6] = "1";
                    } else list.get(getAdapterPosition())[6] = "-1";
                }
            });

            et1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 0) {
                        list.get(getAdapterPosition())[4] = s.toString();
                    } else {
                        list.get(getAdapterPosition())[4] = "0";
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            et2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 0) {
                        list.get(getAdapterPosition())[5] = s.toString();
                    } else {
                        list.get(getAdapterPosition())[5] = "0";
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
    }
}
