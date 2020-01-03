package com.cdzp.huinongyun.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.adapter.SeedlMonAdapter;
import com.cdzp.huinongyun.bean.GroupConResult;
import com.cdzp.huinongyun.bean.GroupImgResult;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.cdzp.huinongyun.activity.FouSitMonActivity.mList;

/**
 * 苗情监控
 */
public class SeedlMonitorFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private View view;
    private boolean isVisible, isStart, isRequest;
    private QMUITipDialog mDialog;
    private TextView tv_tart, tv_end, tv_MonPoi, tv_time;
    private int mPosition1 = -1, mPosition2 = -1;
    private QMUIBottomSheet mBottomSheet1, mBottomSheet2;
    private TimePickerView pvTime;
    private Date startDate, endDate;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private SeedlMonAdapter mAdapter;
    public static List<String[]> photoList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_seedl_monitor, container, false);
            initView();
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
                if (mList != null) {
                    if (!isRequest) {
                        isRequest = true;
                        mPosition1 = 0;
                        tv_MonPoi.setText(mList.get(mPosition1)[0]);
                        mRefreshLayout.autoRefresh();
                    }
                } else getData();
            }
        } else {
            EventBus.getDefault().unregister(this);
        }
    }


    private void initView() {
        context = getContext();
        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();

        tv_tart = view.findViewById(R.id.tv_fsm_start);
        tv_end = view.findViewById(R.id.tv_fsm_end);
        tv_MonPoi = view.findViewById(R.id.tv_fsm_MonPoi);
        tv_time = view.findViewById(R.id.tv_fsm_time);

        endDate = new Date();
        tv_end.setText(getTime(endDate));
        startDate = new Date();
        startDate.setTime(endDate.getTime() - 24 * 60 * 60 * 1000);
        tv_tart.setText(getTime(startDate));

        pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (isStart) {
                    startDate = date;
                    tv_tart.setText(getTime(startDate));
                } else {
                    endDate = date;
                    tv_end.setText(getTime(endDate));
                }
                if (endDate.getTime() - startDate.getTime() < 30l * 24 * 60 * 60 * 1000 + 10000) {
                    mRefreshLayout.autoRefresh();
                } else Toasts.showInfo(context, getString(R.string.time_warn1));

            }
        })
                .isCyclic(true)
                .setType(new boolean[]{true, true, true, false, false, false})
                .build();

        mRefreshLayout = view.findViewById(R.id.mRefreshLayout_fsm);
        mRefreshLayout.setEnableOverScrollBounce(false);//是否启用越界回弹
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(context));
        mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        mRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作

        mRecyclerView = view.findViewById(R.id.mRecyclerView_fsm);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        photoList = new ArrayList<>();
        mAdapter = new SeedlMonAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                groupGetImgList();
            }
        });

        view.findViewById(R.id.ll_fsm1).setOnClickListener(this);
        view.findViewById(R.id.ll_fsm2).setOnClickListener(this);
        view.findViewById(R.id.ll_fsm3).setOnClickListener(this);
        view.findViewById(R.id.ll_fsm4).setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 1:
                    parseGroupCon(message.getmResult());
                    break;
                case 2:
                    parseGroupImg(message.getmResult());
                    break;
                default:
                    mDialog.dismiss();
                    mRefreshLayout.finishRefresh(0, false);
                    break;
            }
        } else {
            mDialog.dismiss();
            mRefreshLayout.finishRefresh(0, false);
            Toasts.showError(context, message.getmResult());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fsm1:
                showBottomSheet1();
                break;
            case R.id.ll_fsm2:
                showBottomSheet2();
                break;
            case R.id.ll_fsm3:
                isStart = true;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                pvTime.setDate(calendar);
                pvTime.show();
                break;
            case R.id.ll_fsm4:
                isStart = false;
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(endDate);
                pvTime.setDate(calendar1);
                pvTime.show();
                break;
            default:

                break;
        }
    }

    private void thinkTime() {
        if (endDate.getTime() - startDate.getTime() < 30l * 24 * 60 * 60 * 1000 + 10000) {
            mRefreshLayout.autoRefresh();
        }
    }

    private void parseGroupImg(String s) {
        try {
            GroupImgResult result = new Gson().fromJson(s, GroupImgResult.class);
            if (result.isSuccess() && result.getData().size() > 0 && result.getData().get(0).size() > 0) {
                for (int i = 0; i < result.getData().get(0).size(); i++) {
                    photoList.add(new String[]{
                            "http://" + Data.url + result.getData().get(0).get(i).getFilePath(),
                            result.getData().get(0).get(i).getShowName()
                    });
                }
                mAdapter.notifyDataSetChanged();
                mRefreshLayout.finishRefresh(0, true);
            } else {
                mRefreshLayout.finishRefresh(0, true);
                Toasts.showInfo(context, getString(R.string.pest_mon_str6));
            }
        } catch (Exception e) {
            mRefreshLayout.finishRefresh(0, false);
            Toasts.showInfo(context, getString(R.string.request_error11));
        }
    }

    private void parseGroupCon(String s) {
        try {
            GroupConResult result = new Gson().fromJson(s, GroupConResult.class);
            mList = new ArrayList<>();
            if (result.isSuccess()) {
                if (result.getData().size() > 0 && result.getData().get(0).size() > 0) {
                    for (int i = 0; i < result.getData().size(); i++) {
                        mList.add(new String[]{
                                result.getData().get(0).get(i).getGrCoName(),
                                result.getData().get(0).get(i).getInsects(),
                                result.getData().get(0).get(i).getSeedling(),
                                result.getData().get(0).get(i).getConditon2()
                        });
                    }
                    mPosition1 = 0;
                    tv_MonPoi.setText(mList.get(mPosition1)[0]);
                } else {
                    Toasts.showInfo(context, getString(R.string.pest_mon_str1));
                }
            } else {
                Toasts.showInfo(context, getString(R.string.pest_mon_str1));
            }
        } catch (Exception e) {
            Toasts.showInfo(context, getString(R.string.request_error11));
        }
        mDialog.dismiss();
    }

    private void getData() {
        if (Data.userResult != null) {
            mDialog.show();
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("GroupGetPointList",
                    "/StringService/DeviceSet.asmx", data, 1, context);
        } else {
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    /**
     * 获取图片
     */
    private void groupGetImgList() {
        if (tv_MonPoi.getText().length() > 0 && tv_tart.getText().length() > 0 &&
                tv_end.getText().length() > 0) {
            if (endDate.getTime() - startDate.getTime() < 30l * 24 * 60 * 60 * 1000 + 10000) {
                if (Data.userResult != null) {
                    photoList.clear();
                    mAdapter.notifyDataSetChanged();
                    List<String[]> data = new ArrayList<>();
                    data.add(new String[]{"dataID", mList.get(mPosition1)[2]});
                    data.add(new String[]{"BeginDate", tv_tart.getText().toString()});
                    data.add(new String[]{"EndDate", tv_end.getText().toString()});
                    data.add(new String[]{"CheckTime", mPosition2 + ""});
                    data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
                    data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
                    WebServiceManage.requestData("GroupGetImgList",
                            "/StringService/DeviceSet.asmx", data, 2, context);
                } else {
                    mRefreshLayout.finishRefresh(0, false);
                    Toasts.showError(context, getString(R.string.request_error9));
                }
            } else {
                mRefreshLayout.finishRefresh(0, false);
                Toasts.showInfo(context, getString(R.string.time_warn1));
            }
        } else {
            mRefreshLayout.finishRefresh(0, false);
            Toasts.showError(context, getString(R.string.request_error6));
        }
    }

    private void showBottomSheet1() {
        if (mList != null) {
            if (mList.size() > 0) {
                if (mBottomSheet1 == null) {
                    BottomListSheetBuilder mBuilder2 = new BottomListSheetBuilder(context, true)
                            .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                                @Override
                                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                    mPosition1 = position;
                                    tv_MonPoi.setText(mList.get(mPosition1)[0]);
                                    thinkTime();
                                    dialog.dismiss();
                                }
                            });
                    for (int i = 0; i < mList.size(); i++) {
                        mBuilder2.addItem(mList.get(i)[0]);
                    }
                    mBuilder2.setCheckedIndex(mPosition1);
                    mBottomSheet1 = mBuilder2.build();
                }
                mBottomSheet1.show();
            } else Toasts.showInfo(context, getString(R.string.pest_mon_str1));
        } else getData();
    }

    private void showBottomSheet2() {
        if (mBottomSheet2 == null) {
            BottomListSheetBuilder mBuilder2 = new BottomListSheetBuilder(context, true)
                    .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                        @Override
                        public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                            if (position > 9) {
                                if (position != 24) {
                                    mPosition2 = position;
                                    tv_time.setText(mPosition2 + getString(R.string.pest_mon_str5));
                                } else {
                                    mPosition2 = -1;
                                    tv_time.setText(getString(R.string.pest_mon_str4));
                                }
                            } else {
                                mPosition2 = position;
                                tv_time.setText("0" + mPosition2 + getString(R.string.pest_mon_str5));
                            }
                            if (tv_MonPoi.getText().length() > 0) {
                                thinkTime();
                            }
                            dialog.dismiss();
                        }
                    });
            for (int i = 0; i < 24; i++) {
                if (i > 9) {
                    mBuilder2.addItem(i + getString(R.string.pest_mon_str5));
                } else
                    mBuilder2.addItem("0" + i + getString(R.string.pest_mon_str5));
            }
            mBuilder2.addItem(getString(R.string.pest_mon_str4));
            mBuilder2.setCheckedIndex(mPosition2);
            mBottomSheet2 = mBuilder2.build();
        }
        mBottomSheet2.show();
    }

    private String getTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible)
            register();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void register() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        photoList.clear();
        photoList = null;
    }
}
