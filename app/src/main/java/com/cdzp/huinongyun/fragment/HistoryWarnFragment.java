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
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.activity.WarningActivity;
import com.cdzp.huinongyun.adapter.HistoryWarnAdapter;
import com.cdzp.huinongyun.bean.AlaHistorySelResult;
import com.cdzp.huinongyun.bean.IntelTypeSelResult;
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
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.cdzp.huinongyun.util.Data.cGhList;
import static com.cdzp.huinongyun.util.Data.pGhList;

/**
 * 历史预警
 */
public class HistoryWarnFragment extends Fragment implements View.OnClickListener {

    private View view;
    private boolean isVisible, isGetData;
    private Context context;
    private QMUITipDialog mDialog;
    private TextView tv_terminal, tv_type, tv_status, tv_start, tv_end;
    private int mPosition0, mPosition1 = -1, mPosition2, mPosition3, PageNo = 1;
    private QMUIBottomSheet mBottomSheet1, mBottomSheet3;
    private List<String[]> intelType;
    private List<String[]> status;
    private TimePickerView pvTime;
    private boolean isStart;
    private Date startDate, endDate;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private HistoryWarnAdapter mAdapter;
    private OptionsPickerView mOptions;
    private boolean isSunlight = false;//是否是日光,电磁阀
    private boolean isEnglish;//是否中英文
    private List<String[]> sensorList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_history_warn, container, false);
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
                if (!isGetData) {
                    isGetData = true;
                    getData();
                }
            }
        } else {
            WebServiceManage.dispose();
            EventBus.getDefault().unregister(this);
            if (mRefreshLayout != null) stopLoading(false, false, false);
        }
    }

    private void init() {
        context = getContext();
        isEnglish = ((WarningActivity) getActivity()).isEnglish();

        if (Data.GhType != null) {
            switch (Data.GhType) {
                case "8":
                case "9":
                    isSunlight = true;
                    break;
                default:

                    break;
            }
        }

        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();

        tv_terminal = view.findViewById(R.id.tv_fhw_terminal);
        tv_type = view.findViewById(R.id.tv_fhw_type);
        tv_status = view.findViewById(R.id.tv_fhw_status);
        tv_start = view.findViewById(R.id.tv_fhw_start);
        tv_end = view.findViewById(R.id.tv_fhw_end);

        status = new ArrayList<>();
        status.add(new String[]{"-1", getString(R.string.warn_str14)});
        status.add(new String[]{"0", getString(R.string.warn_str15)});
        status.add(new String[]{"1", getString(R.string.warn_str16)});
        mPosition3 = 0;
        tv_status.setText(status.get(mPosition3)[1]);

        pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (isStart) {
                    startDate = date;
                    tv_start.setText(getTime(startDate));
                } else {
                    endDate = date;
                    tv_end.setText(getTime(endDate));
                }
                notifyData();
                mRefreshLayout.autoRefresh();
            }
        })
                .isCyclic(true)
                .setType(new boolean[]{true, true, true, true, true, true})
                .build();

        endDate = new Date();
        tv_end.setText(getTime(endDate));
        startDate = new Date();
        startDate.setTime(endDate.getTime() - 24 * 60 * 60 * 1000);
        tv_start.setText(getTime(startDate));


        mRefreshLayout = view.findViewById(R.id.refreshLayout_fhw);
        mRefreshLayout.setEnableOverScrollBounce(false);//是否启用越界回弹
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(context));//设置Header
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(context));//设置Footer
        mRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        mRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        mRefreshLayout.setEnableAutoLoadMore(false);//是否启用列表惯性滑动到底部时自动加载更多

        mRecyclerView = view.findViewById(R.id.recyclerView_fhw);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(context.getResources().getColor(R.color.decoration), 1, 1));
        mAdapter = new HistoryWarnAdapter(context, isEnglish);
        mRecyclerView.setAdapter(mAdapter);

        sensorList = new ArrayList<>();

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mAdapter.getList().clear();
                mAdapter.notifyDataSetChanged();
                PageNo = 1;
                mRefreshLayout.setNoMoreData(false);
                alarmHistorySel();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mAdapter.getList().size() > 0) {
                    PageNo++;
                }
                alarmHistorySel();
            }
        });

        view.findViewById(R.id.ll_fhw1).setOnClickListener(this);
        view.findViewById(R.id.ll_fhw2).setOnClickListener(this);
        view.findViewById(R.id.ll_fhw3).setOnClickListener(this);
        view.findViewById(R.id.ll_fhw4).setOnClickListener(this);
        view.findViewById(R.id.ll_fhw5).setOnClickListener(this);

        if (isVisible) {
            register();
            isGetData = true;
            getData();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fhw1:
                if (isSunlight) {
                    if (mOptions != null) {
                        mOptions.show();
                    } else getTerminal();
                } else {
                    if (pGhList != null) {
                        showBottomSheet1();
                    } else {
                        getTerminal();
                    }
                }
                break;
            case R.id.ll_fhw2:
                showBottomSheet2();
                break;
            case R.id.ll_fhw3:
                showBottomSheet3();
                break;
            case R.id.ll_fhw4:
                isStart = true;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                pvTime.setDate(calendar);
                pvTime.show();
                break;
            case R.id.ll_fhw5:
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 666:
                    parseGHSelResult();
                    break;
                case 1:
                    parseITSelResult(message.getmResult());
                    break;
                case 2:
                    parseAHSel(message.getmResult());
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

    private void showBottomSheet1() {
        if (pGhList.size() > 0) {
            if (mBottomSheet1 == null) {
                BottomListSheetBuilder mBuilder2 = new BottomListSheetBuilder(context, true)
                        .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                mPosition1 = position;
                                tv_terminal.setText(pGhList.get(mPosition1)[1]);
                                dialog.dismiss();
                                mDialog.show();
                                notifyData();
                                intelType = null;
                                mPosition2 = -1;
                                tv_type.setText("");
                                intelTypeSel();
                            }
                        });
                for (int i = 0; i < pGhList.size(); i++) {
                    String str = pGhList.get(i)[1];
                    if (!str.contains(getString(R.string.dev_str2)))
                        mBuilder2.addItem(str);
                }
                mBuilder2.setCheckedIndex(mPosition1);
                mBottomSheet1 = mBuilder2.build();
            }
            mBottomSheet1.show();
        } else Toasts.showInfo(context, getString(R.string.request_error8));
    }

    private void showBottomSheet2() {
        if (tv_terminal.getText().length() > 0) {
            if (intelType != null) {
                if (intelType.size() > 0) {
                    BottomListSheetBuilder mBuilder = new BottomListSheetBuilder(context, true)
                            .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                                @Override
                                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                    mPosition2 = position;
                                    tv_type.setText(intelType.get(mPosition2)[1]);
                                    dialog.dismiss();
                                    notifyData();
                                    mRefreshLayout.autoRefresh();
                                }
                            });
                    for (int i = 0; i < intelType.size(); i++) {
                        mBuilder.addItem(intelType.get(i)[1]);
                    }
                    mBuilder.setCheckedIndex(mPosition2);
                    mBuilder.build().show();
                } else {
                    Toasts.showInfo(context, getString(R.string.warn_str11));
                }
            } else {
                mDialog.show();
                intelTypeSel();
            }
        } else Toasts.showInfo(context, getString(R.string.warn_str12));
    }

    private void showBottomSheet3() {
        if (mBottomSheet3 == null) {
            BottomListSheetBuilder mBuilder = new BottomListSheetBuilder(context, true)
                    .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                        @Override
                        public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                            mPosition3 = position;
                            tv_status.setText(status.get(mPosition3)[1]);
                            dialog.dismiss();
                            notifyData();
                            mRefreshLayout.autoRefresh();
                        }
                    });
            for (int i = 0; i < status.size(); i++) {
                mBuilder.addItem(status.get(i)[1]);
            }
            mBuilder.setCheckedIndex(mPosition3);
            mBottomSheet3 = mBuilder.build();
        }
        mBottomSheet3.show();
    }

    private void getData() {
        if (isSunlight) {
            if (pGhList == null || cGhList == null) {
                getTerminal();
            } else {
                initOptions();
                if (pGhList.size() > 0 && cGhList.size() > 0 && cGhList.get(0).size() > 0) {
                    mPosition0 = 0;
                    mPosition1 = 0;
                    tv_terminal.setText(pGhList.get(mPosition0)[1].replace(getString(R.string.parsetstr10), "") +
                            cGhList.get(mPosition0).get(mPosition1)[1].replace(getString(R.string.parsetstr10), ""));
                    mDialog.show();
                    intelTypeSel();
                }
            }
        } else {
            if (pGhList == null) {
                getTerminal();
            } else {
                if (pGhList.size() > 0) {
                    mDialog.show();
                    mPosition1 = 0;
                    tv_terminal.setText(pGhList.get(mPosition1)[1]);
                    intelTypeSel();
                } else Toasts.showError(context, getString(R.string.warn_str10));
            }
        }
    }

    private void initOptions() {
        List<String> gateway = new ArrayList<>();
        List<List<String>> terminal = new ArrayList<>();
        for (int i = 0; i < pGhList.size(); i++) {
            gateway.add(pGhList.get(i)[1]);
            List<String> list = new ArrayList<>();
            if (i < cGhList.size() && cGhList.get(i).size() > 0) {
                for (int i1 = 0; i1 < cGhList.get(i).size(); i1++) {
                    String str = cGhList.get(i).get(i1)[1];
                    if (!str.contains(getString(R.string.dev_str2))) {
                        list.add(str);
                    } else if (i1 == cGhList.get(i).size() - 1 && list.size() == 0) {
                        list.add("");
                    }
                }
            } else {
                list.add("");
            }
            terminal.add(list);
        }
        mOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mPosition0 = options1;
                mPosition1 = options2;
                tv_terminal.setText(pGhList.get(mPosition0)[1].replace(getString(R.string.parsetstr10), "") +
                        cGhList.get(mPosition0).get(mPosition1)[1].replace(getString(R.string.parsetstr10), ""));
                mDialog.show();
                notifyData();
                intelType = null;
                mPosition2 = -1;
                tv_type.setText("");
                intelTypeSel();
            }
        }).build();
        mOptions.setPicker(gateway, terminal);
    }

    /**
     * 获取终端
     */
    private void getTerminal() {
        if (Data.userResult != null && Data.GhType != null) {
            mDialog.show();
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"Type", Data.GhType});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("GreenHouseSel",
                    "/StringService/DataInfo.asmx", data, 666, context);
        } else {
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    /**
     * 获取传感器类型
     */
    private void intelTypeSel() {
        if (Data.userResult != null) {
            String GreenhouseID = "";
            if (isSunlight) {
                GreenhouseID = cGhList.get(mPosition0).get(mPosition1)[0];
            } else {
                GreenhouseID = pGhList.get(mPosition1)[0];
            }
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"GreenhouseID", GreenhouseID});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("IntelTypeSel",
                    "/StringService/AlarmInfo.asmx", data, 1, context);
        } else {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    /**
     * 获取历史报警
     */
    private void alarmHistorySel() {
        if (tv_terminal.getText().length() > 0 && tv_type.getText().length() > 0 &&
                tv_status.getText().length() > 0 && tv_start.getText().length() > 0 &&
                tv_end.getText().length() > 0) {
            if (Data.userResult != null) {
                String GreenhouseIDs = "";
                if (isSunlight) {
                    GreenhouseIDs = cGhList.get(mPosition0).get(mPosition1)[0];
                } else {
                    GreenhouseIDs = pGhList.get(mPosition1)[0];
                }
                List<String[]> data = new ArrayList<>();
                data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
                data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
                data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
                data.add(new String[]{"GreenhouseIDs", GreenhouseIDs});
                data.add(new String[]{"AlarmType", intelType.get(mPosition2)[0]});
                data.add(new String[]{"DoStatus", status.get(mPosition3)[0]});
                data.add(new String[]{"StartTime", tv_start.getText().toString()});
                data.add(new String[]{"EndTime", tv_end.getText().toString()});
                data.add(new String[]{"PageNo", PageNo + ""});
                data.add(new String[]{"pageSize", "20"});
                WebServiceManage.requestData("AlarmHistorySel",
                        "/StringService/AlarmInfo.asmx", data, 2, context);
            } else {
                stopLoading(false, false, false);
                Toasts.showError(context, getString(R.string.request_error9));
            }
        } else {
            stopLoading(false, false, false);
            Toasts.showInfo(context, getString(R.string.request_error6));
        }
    }

    private void parseGHSelResult() {
        if (isSunlight) {
            initOptions();
            if (pGhList.size() > 0 && cGhList.size() > 0 && cGhList.get(0).size() > 0) {
                mPosition0 = 0;
                mPosition1 = 0;
                tv_terminal.setText(pGhList.get(mPosition0)[1].replace(getString(R.string.parsetstr10), "") +
                        cGhList.get(mPosition0).get(mPosition1)[1].replace(getString(R.string.parsetstr10), ""));
                mDialog.show();
                intelTypeSel();
            } else {
                mDialog.dismiss();
            }
        } else {
            if (pGhList.size() > 0) {
                mPosition1 = 0;
                tv_terminal.setText(pGhList.get(mPosition1)[1]);
                intelTypeSel();
            } else {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.warn_str10));
            }
        }
    }

    /**
     * 解析预警类型
     *
     * @param s
     */
    private void parseITSelResult(String s) {
        try {
            IntelTypeSelResult result = new Gson().fromJson(s, IntelTypeSelResult.class);
            if (result.isSuccess()) {
                if (result.getData().size() > 0) {
                    intelType = new ArrayList<>();
                    for (int i = 0; i < result.getData().size(); i++) {
                        String type = result.getData().get(i).getIntelType();
                        String typeName = result.getData().get(i).getIntelTypeName();
                        if (isEnglish) {
                            sensorList.clear();
                            for (int i1 = 0; i1 < result.getSensorInfo().size(); i1++) {
                                sensorList.add(new String[]{
                                        result.getSensorInfo().get(i1).getBaseName(),
                                        result.getSensorInfo().get(i1).getEnglishName()
                                });
                                if (typeName.equals(result.getSensorInfo().get(i1).getBaseName())) {
                                    typeName = result.getSensorInfo().get(i1).getEnglishName();
                                }
                            }
                        }
                        intelType.add(new String[]{type, typeName});
                    }
                    if (intelType.size() > 0) {
                        mPosition2 = 0;
                        tv_type.setText(intelType.get(mPosition2)[1]);
                        mDialog.dismiss();
                        notifyData();
                        mRefreshLayout.autoRefresh();
                    } else mDialog.dismiss();
                } else {
                    mDialog.dismiss();
                    Toasts.showInfo(context, getString(R.string.warn_str11));
                }
            } else {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.warn_str11));
            }
        } catch (Exception e) {
            mDialog.dismiss();
        }
    }

    /**
     * 解析历史预警信息
     *
     * @param s
     */
    private void parseAHSel(String s) {
        try {
            AlaHistorySelResult result = new Gson().fromJson(s, AlaHistorySelResult.class);
            if (result.isSuccess()) {
                if (result.getData().size() > 0 && result.getData().get(0).size() > 0) {
                    for (int i = 0; i < result.getData().get(0).size(); i++) {
                        double min = Double.parseDouble(result.getData().get(0).get(i).getMinValue());
                        double amin = Double.parseDouble(result.getData().get(0).get(i).getMinAlarmValue());
                        String alarm;
                        if (amin < min) {
                            amin = 0.1;
                            alarm = result.getData().get(0).get(i).getMinAlarmValue() +
                                    result.getData().get(0).get(i).getUnit();
                        } else {
                            amin = 1.1;
                            alarm = result.getData().get(0).get(i).getMaxAlarmValue() +
                                    result.getData().get(0).get(i).getUnit();
                        }
                        String greenhouseName, locationName, typeName, doStatus;
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
                                    greenhouseName = result.getData().get(0).get(i).getGreenhouseName();
                                    break;
                            }
                            locationName = result.getData().get(0).get(i).getLocation() + getString(R.string.dev_str1);
                            typeName = result.getData().get(0).get(i).getTypeName();
                            for (int i1 = 0; i1 < sensorList.size(); i1++) {
                                if (typeName.equals(sensorList.get(i1)[0])) {
                                    typeName = sensorList.get(i1)[1];
                                    break;
                                }
                            }
                            switch (result.getData().get(0).get(i).getDsValue()) {
                                case "0":
                                    doStatus = getString(R.string.warn_str15);
                                    break;
                                case "1":
                                    doStatus = getString(R.string.warn_str16);
                                    break;
                                default:
                                    doStatus = "unclear";
                                    break;
                            }
                        } else {
                            greenhouseName = result.getData().get(0).get(i).getGreenhouseName();
                            locationName = result.getData().get(0).get(i).getLocationName();
                            typeName = result.getData().get(0).get(i).getTypeName().replace(".", "")
                                    .replace(",", "").replace(" ", "");
                            doStatus = result.getData().get(0).get(i).getDoStatus();
                        }
                        String[] strs = new String[]{
                                greenhouseName,
                                locationName,
                                doStatus,
                                amin + "",
                                typeName,
                                alarm,
                                result.getData().get(0).get(i).getMinValue() + "~" +
                                        result.getData().get(0).get(i).getMaximizeValue()
                                        + result.getData().get(0).get(i).getUnit(),
                                result.getData().get(0).get(i).getAlarmTime() + " ~ " +
                                        result.getData().get(0).get(i).getDisposeTime(),
                        };
                        mAdapter.getList().add(strs);
                    }
                    mAdapter.notifyDataSetChanged();
                    stopLoading(true, true, false);
                } else stopLoading(true, true, true);
            } else {
                stopLoading(true, true, false);
                Toasts.showInfo(context, getString(R.string.warn_str13));
            }
        } catch (Exception e) {
            stopLoading(true, true, false);
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

    private void notifyData() {
        mAdapter.getList().clear();
        mAdapter.notifyDataSetChanged();
        PageNo = 1;
        mRefreshLayout.setNoMoreData(false);
    }

    private String getTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
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
    }

    private void register() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }
}
