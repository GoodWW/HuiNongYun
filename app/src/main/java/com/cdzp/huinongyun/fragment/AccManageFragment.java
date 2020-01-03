package com.cdzp.huinongyun.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.blankj.utilcode.util.BarUtils;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.activity.AddAccountActivity;
import com.cdzp.huinongyun.adapter.AccManageAdapter;
import com.cdzp.huinongyun.bean.AccManageBean;
import com.cdzp.huinongyun.bean.GetRoleListResult;
import com.cdzp.huinongyun.bean.GetUserInfoResult;
import com.cdzp.huinongyun.bean.UniversalResult;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 账号管理
 */
public class AccManageFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean isVisible, isInit = false;
    private QMUITipDialog mDialog;
    private int PageIndex = 1;
    private ExpandableListView mExListView;
    private AccManageAdapter mAdapter;
    private SmartRefreshLayout mRefreshLayout;
    private String[] greenhouseID;
    private String[] greenhouseName;
    private boolean isEnglish;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_acc_manag, container, false);
            initView();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
            if (!isInit && view != null) {
                isInit = true;
                if (Data.userResult != null && Data.userResult.getData().get(0).getRoleID() < 4) {
                    mRefreshLayout.autoRefresh();
                } else {
                    mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
                    mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
                    Toasts.showInfo(context, getString(R.string.hone_amf1));
                }
            }
        } else {
            unRegister();
            if (mRefreshLayout != null) {
                mRefreshLayout.finishRefresh(0, false);
            }
        }
    }

    private void initView() {
        context = getContext();
        BarUtils.addMarginTopEqualStatusBarHeight(view.findViewById(R.id.ll_fam));
        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        if (context.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
                .getString("Language", "").equals("English")) {
            isEnglish = true;
        } else {
            isEnglish = false;
        }

        mRefreshLayout = view.findViewById(R.id.mRefreshLayout_fam);
        mRefreshLayout.setEnableOverScrollBounce(false);//是否启用越界回弹
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(context));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(context));
        mRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        mRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        mRefreshLayout.setEnableAutoLoadMore(false);//是否启用列表惯性滑动到底部时自动加载更多

        mExListView = view.findViewById(R.id.expand_fam);
        mExListView.setGroupIndicator(null);
        mAdapter = new AccManageAdapter(context, mDialog);
        mExListView.setAdapter(mAdapter);

        mExListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int count = mExListView.getExpandableListAdapter().getGroupCount();
                for (int j = 0; j < count; j++) {
                    if (j != groupPosition) {
                        mExListView.collapseGroup(j);
                    }
                }
            }
        });

        view.findViewById(R.id.rl_fam).setOnClickListener(this);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (Data.userResult != null && Data.userResult.getData().get(0).getRoleID() < 4) {
                    mAdapter.getList().clear();
                    mAdapter.notifyDataSetChanged();
                    mRefreshLayout.setNoMoreData(false);
                    if (greenhouseID == null) {
                        requestRole();
                    } else {
                        PageIndex = 1;
                        requestUserInfo();
                    }
                } else stopLoading(false, false, false);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (Data.userResult != null && Data.userResult.getData().get(0).getRoleID() < 4) {
                    if (greenhouseID == null) {
                        requestRole();
                    } else {
                        if (mAdapter.getList().size() > 0)
                            PageIndex++;
                        requestUserInfo();
                    }
                } else stopLoading(false, false, false);
            }
        });
        if (!isInit && isVisible) {
            isInit = true;
            if (Data.userResult != null && Data.userResult.getData().get(0).getRoleID() < 4) {
                mRefreshLayout.autoRefresh();
            } else {
                mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
                mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
                Toasts.showInfo(context, getString(R.string.hone_amf1));
            }
        }
    }

    private void requestRole() {
        if (Data.userResult != null) {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            data.add(new String[]{"RoleID", Data.userResult.getData().get(0).getRoleID() + ""});
            WebServiceManage.requestData("GetRoleList",
                    "/StringService/BaseSet.asmx", data, 1, context);
        } else {
            stopLoading(false, false, false);
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    private void requestUserInfo() {
        if (Data.userResult != null) {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            data.add(new String[]{"PageSize", "20"});
            data.add(new String[]{"PageIndex", PageIndex + ""});
            WebServiceManage.requestData("GetUserInfo",
                    "/StringService/BaseSet.asmx", data, 2, context);
        } else {
            stopLoading(false, false, false);
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 1:
                    parseRole(message.getmResult());
                    break;
                case 2:
                    parseUserInfo(message.getmResult());
                    break;
                case 3:
                    parseSavePower(message.getmResult());
                    break;
                default:
                    stopLoading(false, false, false);
                    mDialog.dismiss();
                    break;
            }
        } else {
            stopLoading(false, false, false);
            mDialog.dismiss();
            Toasts.showError(context, message.getmResult());
        }
    }

    private void parseSavePower(String s) {
        try {
            UniversalResult result = new Gson().fromJson(s, UniversalResult.class);
            if (result.isSuccess()) {
                mAdapter.setCheck();
                mDialog.dismiss();
                Toasts.showSuccess(context, result.getMsg());
            } else {
                mDialog.dismiss();
                Toasts.showError(context, result.getMsg());
            }
        } catch (Exception e) {
            mDialog.dismiss();
        }
    }

    private void parseUserInfo(String s) {
        try {
            GetUserInfoResult result = new Gson().fromJson(s, GetUserInfoResult.class);
            if (result.isSuccess()) {
                if (result.getData().get(0).size() > 0) {
                    for (int i = 0; i < result.getData().get(0).size(); i++) {
                        AccManageBean bean = new AccManageBean();
                        bean.setUserID(result.getData().get(0).get(i).getUserID());
                        bean.setUserName(result.getData().get(0).get(i).getUserName());
                        bean.setNickName(result.getData().get(0).get(i).getNickName());
                        if (isEnglish) {
                            bean.setRoleName(result.getData().get(0).get(i).getRoleEnglishName());
                        } else {
                            bean.setRoleName(result.getData().get(0).get(i).getRoleName());
                        }
                        bean.setGreenhouseID(greenhouseID);
                        bean.setGreenhouseName(greenhouseName);
                        boolean[] isCheck = new boolean[greenhouseID.length];
                        String[] mGhIDs = result.getData().get(0).get(i).getGhIDs().split(",");
                        for (int i1 = 0; i1 < mGhIDs.length; i1++) {
                            for (int i2 = 0; i2 < greenhouseID.length; i2++) {
                                if (mGhIDs[i1].equals(greenhouseID[i2])) {
                                    isCheck[i2] = true;
                                    break;
                                }
                            }
                        }
                        bean.setIsCheck(isCheck);
                        mAdapter.getList().add(bean);
                    }
                    mAdapter.notifyDataSetChanged();
                    stopLoading(true, true, false);
                } else {
                    stopLoading(true, true, true);
                }
            } else {
                stopLoading(true, true, false);
                Toasts.showInfo(context, result.getMsg());
            }
        } catch (Exception e) {
            stopLoading(true, true, false);
        }
    }

    private void parseRole(String s) {
        GetRoleListResult result = new Gson().fromJson(s, GetRoleListResult.class);
        if (result.isSuccess()) {
            if (result.getGhList().get(0).size() > 0) {
                List<String> listId = new ArrayList<>();
                List<String> listName = new ArrayList<>();
                for (int i = 0; i < result.getGhList().get(0).size(); i++) {
                    if (result.getGhList().get(0).get(i).getGwID() <= 0) {
                        listId.add(result.getGhList().get(0).get(i).getGreenhouseID() + "");
                        listName.add(result.getGhList().get(0).get(i).getGreenhouseName());
                    }
                }
                greenhouseID = listId.toArray(new String[listId.size()]);
                greenhouseName = listName.toArray(new String[listName.size()]);
                PageIndex = 1;
                requestUserInfo();
            } else {
                stopLoading(false, false, false);
                Toasts.showInfo(context, getString(R.string.request_error8));
            }
        } else {
            stopLoading(false, false, false);
            Toasts.showInfo(context, result.getMsg());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_fam:
                unRegister();
                Intent intent = new Intent(context, AddAccountActivity.class);
                startActivity(intent);
                break;
            default:
                Toasts.showInfo(context, "其他");
                break;
        }
    }

    private void stopLoading(boolean isRefresh, boolean isLoad, boolean isMore) {
        if (mRefreshLayout.getState() == RefreshState.Refreshing) {
            mRefreshLayout.finishRefresh(0, isRefresh);
        }
        if (mRefreshLayout.getState() == RefreshState.Loading) {
            if (!isLoad && PageIndex > 1) {
                PageIndex--;
            }
            mRefreshLayout.finishLoadMore(0, isLoad, isMore);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        stopLoading(false, false, false);
    }

    private void unRegister() {
        WebServiceManage.dispose();
        EventBus.getDefault().unregister(this);
    }
}
