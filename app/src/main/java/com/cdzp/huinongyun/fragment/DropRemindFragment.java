package com.cdzp.huinongyun.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.BarUtils;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.adapter.DroRemAdapter;
import com.cdzp.huinongyun.bean.GetOffListResult;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 掉线提醒
 */
public class DropRemindFragment extends Fragment implements View.OnClickListener {


    private View view;
    private Context context;
    private boolean isVisible, isInit = false;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private DroRemAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_drop_remind, container, false);
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
                mRefreshLayout.autoRefresh();
            }
        } else {
            WebServiceManage.dispose();
            EventBus.getDefault().unregister(this);
            if (mRefreshLayout != null) {
                mRefreshLayout.finishRefresh(0, false);
            }
        }
    }

    private void initView() {
        context = getContext();
        BarUtils.addMarginTopEqualStatusBarHeight(view.findViewById(R.id.tv_top_fdr));

        mRefreshLayout = view.findViewById(R.id.mRefreshLayout_fdr);
        mRefreshLayout.setEnableOverScrollBounce(false);//是否启用越界回弹
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(context));
        mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        mRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作

        mRecyclerView = view.findViewById(R.id.recycler_fdr);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//        recyclerView.addItemDecoration(new DefaultItemDecoration(context.getResources().getColor(R.color.itemDecoration)));
        mAdapter = new DroRemAdapter(context);
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                requestData();
            }
        });

        if (!isInit && isVisible) {
            isInit = true;
            mRefreshLayout.autoRefresh();
        }
    }

    private void requestData() {
        if (Data.userResult != null) {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("GetOffList",
                    "/StringService/DeviceSet.asmx", data, 1, context);
        } else {
            mRefreshLayout.finishRefresh(0, false);
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 1:
                    parseGOLResult(message.getmResult());
                    break;
                default:
                    mRefreshLayout.finishRefresh(0, false);
                    break;
            }
        } else {
            mRefreshLayout.finishRefresh(0, false);
            Toasts.showError(context, message.getmResult());
        }
    }

    private void parseGOLResult(String s) {
        GetOffListResult result = new Gson().fromJson(s, GetOffListResult.class);
        if (result.isSuccess()) {
            mAdapter.getList().clear();
            if (result.getData().size() > 0) {
                for (int i = 0; i < result.getData().size(); i++) {
                    String typeName;
                    switch (result.getData().get(i).getGhType() + "") {
                        case "1":
                            typeName = getString(R.string.terminal_str1);
                            break;
                        case "2":
                            typeName = getString(R.string.terminal_str2);
                            break;
                        case "3":
                            typeName = getString(R.string.terminal_str3);
                            break;
                        case "4":
                            typeName = getString(R.string.terminal_str4);
                            break;
                        case "5":
                            typeName = getString(R.string.terminal_str5);
                            break;
                        case "6":
                            typeName = getString(R.string.terminal_str6);
                            break;
                        case "7":
                            typeName = getString(R.string.terminal_str7);
                            break;
                        case "8":
                            typeName = getString(R.string.terminal_str8);
                            break;
                        case "9":
                            typeName = getString(R.string.terminal_str9);
                            break;
                        default:
                            typeName = result.getData().get(i).getTypeName();
                            break;
                    }
                    mAdapter.getList().add(new String[]{
                            "0", result.getData().get(i).getGhType() + "", typeName
                    });
                    for (int i1 = 0; i1 < result.getData().get(i).getData().size(); i1++) {
                        mAdapter.getList().add(new String[]{
                                "1", result.getData().get(i).getData().get(i1).getGreenhouseName() +
                                getString(R.string.drf_str1) +
                                result.getData().get(i).getData().get(i1).getOfMinute() + getString(R.string.drf_str2) +
                                result.getData().get(i).getData().get(i1).getOfHour() +
                                getString(R.string.drf_str3),
                                getString(R.string.drf_str4) + result.getData().get(i).getData().get(i1).getOfStartTime(),
                                getString(R.string.drf_str5) + result.getData().get(i).getData().get(i1).getDisplayNo(),
                                getString(R.string.drf_str6) + result.getData().get(i).getData().get(i1).getDisplayIP() +
                                        getString(R.string.drf_str7) +
                                        result.getData().get(i).getData().get(i1).getDisplayPort()
                        });
                    }
                }
                mAdapter.notifyDataSetChanged();
                mRefreshLayout.finishRefresh(0, true);
            } else {
                mAdapter.notifyDataSetChanged();
                mRefreshLayout.finishRefresh(0, true);
                Toasts.showInfo(context, getString(R.string.request_error8));
            }
        } else {
            mRefreshLayout.finishRefresh(0, true);
            Toasts.showInfo(context, result.getMsg());
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (isVisible && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
