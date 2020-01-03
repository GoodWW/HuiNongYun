package com.cdzp.huinongyun.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.activity.WarningActivity;
import com.cdzp.huinongyun.adapter.RealTimeWarnAdapter;
import com.cdzp.huinongyun.bean.AlarmRealSelResult;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 实时预警
 */
public class RealTimeWarnFragment extends Fragment {

    private View view;
    private boolean isVisible;
    private Context context;
    private String TAG = "RealTimeWarnFragment";

    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private RealTimeWarnAdapter mAdapter;
    private int PageNo = 1;
    private boolean isEnglish;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_real_time_warn, container, false);
            init();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible) {
            if (view != null) {
                register();
            }
        } else {
            WebServiceManage.dispose();
            EventBus.getDefault().unregister(this);
            if (mRefreshLayout != null) stopLoading(false, false, false);
        }
    }

    private void init() {
        context = getContext();

        mRefreshLayout = view.findViewById(R.id.refreshLayout_frtw);
        mRefreshLayout.setEnableOverScrollBounce(false);//是否启用越界回弹
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(context));//设置Header
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(context));//设置Footer
        mRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        mRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        mRefreshLayout.setEnableAutoLoadMore(false);//是否启用列表惯性滑动到底部时自动加载更多

        mRecyclerView = view.findViewById(R.id.recyclerView_frtw);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(context.getResources().getColor(R.color.decoration),1,1));

        isEnglish = ((WarningActivity) getActivity()).isEnglish();
        mAdapter = new RealTimeWarnAdapter(context, isEnglish);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mAdapter.getList().clear();
                mAdapter.notifyDataSetChanged();
                mRefreshLayout.setNoMoreData(false);
                PageNo = 1;
                requestAlarmRealSel();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mAdapter.getList().size() > 0) {
                    PageNo++;
                }
                requestAlarmRealSel();
            }
        });

        if (isVisible) {
            register();
            mRefreshLayout.autoRefresh();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 1:
                    parseResult(message.getmResult());
                    break;
                default:
                    stopLoading(false, false, false);
                    break;
            }
        } else {
            stopLoading(false, false, false);
            Toasts.showError(context, message.getmResult());
        }
    }

    private void parseResult(String s) {
        try {
            AlarmRealSelResult result = new Gson().fromJson(s, AlarmRealSelResult.class);
            if (result.isSuccess()) {
                if (result.getData() != null && result.getData().size() > 0 &&
                        result.getData().get(0) != null && result.getData().size() > 0) {
                    for (int i = 0; i < result.getData().size(); i++) {
                        double min = Double.parseDouble(result.getData().get(i).getMinValue());
                        double alarm = Double.parseDouble(result.getData().get(i).getAlarmValue());
                        if (alarm < min) {
                            alarm = 0.1;
                        } else {
                            alarm = 1.1;
                        }
                        String greenhouseName, locationName, typeName;
                        if (isEnglish) {
                            switch (Data.GhType) {
                                case "1":
                                    greenhouseName = getString(R.string.terminal_str11);
                                    break;
                                case "2":
                                    greenhouseName = getString(R.string.terminal_str21);
                                    break;
                                case "3":
                                    greenhouseName = getString(R.string.terminal_str31);
                                    break;
                                case "4":
                                    greenhouseName = getString(R.string.terminal_str41);
                                    break;
                                case "5":
                                    greenhouseName = getString(R.string.terminal_str51);
                                    break;
                                case "6":
                                    greenhouseName = getString(R.string.terminal_str61);
                                    break;
                                case "7":
                                    greenhouseName = getString(R.string.terminal_str71);
                                    break;
                                case "8":
                                    greenhouseName = getString(R.string.terminal_str81);
                                    break;
                                default:
                                    greenhouseName = result.getData().get(i).getGreenhouseName();
                                    break;
                            }
                            locationName = result.getData().get(i).getLocation() + getString(R.string.dev_str1);
                            typeName = result.getData().get(i).getTypeName();
                            for (int i1 = 0; i1 < result.getSensorInfo().size(); i1++) {
                                if (typeName.equals(result.getSensorInfo().get(i1).getBaseName())) {
                                    typeName = result.getSensorInfo().get(i1).getEnglishName();
                                    break;
                                }
                            }

                        } else {
                            greenhouseName = result.getData().get(i).getGreenhouseName();
                            locationName = result.getData().get(i).getLocationName();
                            typeName = result.getData().get(i).getTypeName().replace(".", "")
                                    .replace(",", "").replace(" ", "");
                        }
                        mAdapter.getList().add(new String[]{
                                greenhouseName,
                                locationName,
                                result.getData().get(i).getAlarmTime(),
                                alarm + "",
                                typeName,
                                result.getData().get(i).getAlarmValue() + result.getData().get(i).getUnit(),
                                result.getData().get(i).getMinValue() + "~" + result.getData().get(i).getMaximizeValue()
                                        + result.getData().get(i).getUnit()
                        });
                    }
                    mAdapter.notifyDataSetChanged();
                    stopLoading(true, true, false);
                } else {
                    stopLoading(true, true, true);
                }
            } else {
                stopLoading(false, false, false);
//                Toasts.showInfo(context, getString(R.string.warn_str5));
            }
        } catch (Exception e) {
            stopLoading(false, false, false);
//            Toasts.showInfo(context, getString(R.string.warn_str6));
        }
    }

    private void requestAlarmRealSel() {
        if (Data.userResult != null && Data.GhType != null) {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"Type", Data.GhType});
            data.add(new String[]{"PageNo", PageNo + ""});
            data.add(new String[]{"pageSize", 20 + ""});
            WebServiceManage.requestData("AlarmRealSel",
                    "/StringService/AlarmInfo.asmx", data, 1, context);
        } else {
            stopLoading(false, false, false);
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    /**
     * @param isRefresh 是否刷新成功
     * @param isLoad    是否加载成功
     * @param isMore    是否没有跟多数据了
     */
    private void stopLoading(boolean isRefresh, boolean isLoad, boolean isMore) {
        if (mRefreshLayout.getState() == RefreshState.Refreshing) {
            mRefreshLayout.finishRefresh(0, isRefresh);
        }
        if (mRefreshLayout.getState() == RefreshState.Loading) {
            if (!isLoad && PageNo > 1) {
                PageNo--;
            }
            mRefreshLayout.finishLoadMore(0, isLoad, isMore);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible) register();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        stopLoading(false, false, false);
    }

    private void register() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }
}
