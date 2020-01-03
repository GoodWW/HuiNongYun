package com.cdzp.huinongyun.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.bean.AccManageBean;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 账号管理适配器
 */

public class AccManageAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<AccManageBean> list;
    private QMUITipDialog mDialog;
    private boolean[] tempCheck;
    private int tempGroupPosition;

    public AccManageAdapter(Context context, QMUITipDialog mDialog) {
        this.context = context;
        this.mDialog = mDialog;
        list = new ArrayList<>();
    }

    public List<AccManageBean> getList() {
        return list;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getGreenhouseID()[childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.accmana_group_item, parent, false);
            holder = new GroupViewHolder();
            holder.tv1 = convertView.findViewById(R.id.tv_afi1);
            holder.tv2 = convertView.findViewById(R.id.tv_afi2);
            holder.iv = convertView.findViewById(R.id.iv_agi);
            convertView.setTag(holder);
            AutoUtils.autoSize(convertView);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.tv1.setText(list.get(groupPosition).getUserName() + " | " + list.get(groupPosition).getNickName());
        holder.tv2.setText(context.getString(R.string.aaa_js) + list.get(groupPosition).getRoleName());
        if (isExpanded) {
            holder.iv.setBackgroundResource(R.drawable.list_arrow_up);
        } else holder.iv.setBackgroundResource(R.drawable.list_arrow_down);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.accmana_child_item, parent, false);
            holder = new ChildViewHolder();
            holder.recyclerView = convertView.findViewById(R.id.recycler_ac_item);
            holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
            holder.adapter = new ChildRecyclerAdapter(groupPosition);
            holder.recyclerView.setAdapter(holder.adapter);
            holder.btn = convertView.findViewById(R.id.btn_aci);
            convertView.setTag(holder);
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        holder.adapter.setGroupPosition(groupPosition);
        tempCheck = new boolean[list.get(groupPosition).getIsCheck().length];
        for (int i = 0; i < list.get(groupPosition).getIsCheck().length; i++) {
            tempCheck[i] = list.get(groupPosition).getIsCheck()[i];
        }
        holder.adapter.notifyDataSetChanged();
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSavePower(groupPosition);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupViewHolder {
        private TextView tv1, tv2;
        private ImageView iv;
    }

    class ChildViewHolder {
        private RecyclerView recyclerView;
        private ChildRecyclerAdapter adapter;
        private Button btn;
    }

    class ChildRecyclerAdapter extends RecyclerView.Adapter<RecViewHolder> {

        private int groupPosition;

        public ChildRecyclerAdapter(int groupPosition) {
            this.groupPosition = groupPosition;
        }

        public void setGroupPosition(int groupPosition) {
            this.groupPosition = groupPosition;
        }

        @Override
        public RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecViewHolder(LayoutInflater.from(context).inflate(R.layout.accmana_chid_recycler_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecViewHolder holder, final int position) {
//            holder.cb.setChecked(list.get(groupPosition).getIsCheck()[position]);

            holder.cb.setText(list.get(groupPosition).getGreenhouseName()[position]);
            holder.cb.setChecked(tempCheck[position]);
//            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    tempCheck[position] = isChecked;
//                }
//            });
//            holder.cb.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    CheckBox checkBox = (CheckBox) v;
//                    if (checkBox.isChecked()) {
//                        tempCheck[position] = true;
//                    } else {
//                        tempCheck[position] = false;
//                    }
//                }
//            });
        }

        @Override
        public int getItemCount() {
//            return list.get(groupPosition).getGreenhouseName().length;
            return tempCheck.length;
        }
    }

    public void setCheck() {
        list.get(tempGroupPosition).setIsCheck(tempCheck);
    }

    class RecViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cb;

        public RecViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            cb = itemView.findViewById(R.id.cb_acri);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    tempCheck[getAdapterPosition()] = isChecked;
                }
            });
        }
    }

    /**
     * 保存
     *
     * @param groupPosition
     */
    private void requestSavePower(int groupPosition) {
        if (Data.userResult != null) {
            mDialog.show();
            String mGhIDs = "";
            for (int i = 0; i < tempCheck.length; i++) {
                if (tempCheck[i]) {
                    mGhIDs = mGhIDs + list.get(groupPosition).getGreenhouseID()[i] + ",";
                }
            }
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"ID", list.get(groupPosition).getUserID()});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            data.add(new String[]{"GhIDs", mGhIDs});
            tempGroupPosition = groupPosition;
            WebServiceManage.requestData("SavePower",
                    "/StringService/BaseSet.asmx", data, 3, context);
        } else Toasts.showError(context, context.getString(R.string.request_error9));
    }
}
