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
import com.cdzp.huinongyun.adapter.DroppedFormAdapter;
import com.cdzp.huinongyun.bean.RptOfflineSelResult;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cdzp.huinongyun.util.Data.cGhList;
import static com.cdzp.huinongyun.util.Data.pGhList;

/**
 * 掉线报表
 */
public class DroppedFormFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean isVisible, isRequest = false;
    private QMUITipDialog mDialog;
    private int mPosition0, mPosition1 = -1;
    private TextView tv_terminal, tv_time;
    private QMUIBottomSheet mBottomSheet1;
    private TimePickerView pvTime;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private DroppedFormAdapter mAdapter;
    private int PageIndex = 1;
    private OptionsPickerView mOptions;
    private boolean isSunlight = false;//是否是日光,电磁阀

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_dropped_form, container, false);
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
                if (!isRequest) {
                    isRequest = true;
                    getData();
                }
            }
        } else {
            unRegister();
            if (mRefreshLayout != null)
                stopLoading(false, false, false);
        }
    }

    private void init() {
        context = getContext();
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

        tv_terminal = view.findViewById(R.id.tv_fdrf_terminal);
        tv_time = view.findViewById(R.id.tv_fdrf_time);

        pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tv_time.setText(getTime(date));
                mAdapter.getList().clear();
                mAdapter.notifyDataSetChanged();
                if (tv_terminal.getText().length() > 0 && tv_time.getText().length() > 0) {
                    mRefreshLayout.autoRefresh();
                }
            }
        })
                .isCyclic(true)
                .setType(new boolean[]{true, true, false, false, false, false})
                .build();

        final Date mDate = new Date();
        tv_time.setText(getTime(mDate));

        mRefreshLayout = view.findViewById(R.id.mRefreshLayout_fdrf);
        mRefreshLayout.setEnableOverScrollBounce(false);//是否启用越界回弹
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(context));//设置Header
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(context));//设置Footer
        mRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        mRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        mRefreshLayout.setEnableAutoLoadMore(false);//是否启用列表惯性滑动到底部时自动加载更多

        mRecyclerView = view.findViewById(R.id.mRecyclerView_fdrf);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new DroppedFormAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mAdapter.getList().clear();
                mAdapter.notifyDataSetChanged();
                PageIndex = 1;
                rptOfflineSel();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mAdapter.getList().size() > 0) {
                    PageIndex++;
                } else PageIndex = 1;
                rptOfflineSel();
            }
        });

        view.findViewById(R.id.ll_fdrf1).setOnClickListener(this);
        view.findViewById(R.id.ll_fdrf2).setOnClickListener(this);

        if (isVisible && !isRequest) {
            isRequest = true;
            register();
            getData();
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
                    parseRptOffResult(message.getmResult());
                    break;
                default:
                    mDialog.dismiss();
                    stopLoading(false, false, false);
                    break;
            }
        } else {
            mDialog.dismiss();
            stopLoading(false, false, false);
            Toasts.showError(context, message.getmResult());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fdrf1:
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
            case R.id.ll_fdrf2:
                pvTime.show();
                break;
            default:

                break;
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
                                mAdapter.getList().clear();
                                mAdapter.notifyDataSetChanged();
                                if (tv_terminal.getText().length() > 0 && tv_time.getText().length() > 0) {
                                    mRefreshLayout.autoRefresh();
                                }
                            }
                        });
                for (int i = 0; i < pGhList.size(); i++) {
                    mBuilder2.addItem(pGhList.get(i)[1]);
                }
                mBuilder2.setCheckedIndex(mPosition1);
                mBottomSheet1 = mBuilder2.build();
            }
            mBottomSheet1.show();
        } else Toasts.showInfo(context, getString(R.string.request_error8));
    }

    private void parseRptOffResult(String s) {
        try {
            RptOfflineSelResult result = new Gson().fromJson(s, RptOfflineSelResult.class);
            if (result.isSuccess()) {
                if (result.getData().size() > 0 && result.getData().get(0).size() > 0) {
                    for (int i = 0; i < result.getData().get(0).size(); i++) {
                        mAdapter.getList().add(new String[]{
                                result.getData().get(0).get(i).getTotal(),
                                result.getData().get(0).get(i).getSumHour(),
                                result.getData().get(0).get(i).getAvgMinute()
                        });
                    }
                    mAdapter.notifyDataSetChanged();
                    stopLoading(true, true, false);
                } else {
                    stopLoading(true, true, true);
                }
            } else {
                stopLoading(true, true, false);
            }
        } catch (Exception e) {
            stopLoading(false, false, false);
            Toasts.showError(context, getString(R.string.request_error7));
        }
    }

    /**
     * 获取数据
     */
    private void rptOfflineSel() {
        if (Data.userResult != null && Data.GhType != null) {
            String GreenhouseID = "";
            if (isSunlight) {
                GreenhouseID = cGhList.get(mPosition0).get(mPosition1)[0];
            } else {
                GreenhouseID = pGhList.get(mPosition1)[0];
            }
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"GhID", GreenhouseID});
            data.add(new String[]{"StartTime", tv_time.getText().toString()});
            data.add(new String[]{"EndTime", tv_time.getText().toString()});
            data.add(new String[]{"PageSize", "18"});
            data.add(new String[]{"PageIndex", PageIndex + ""});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("RptOfflineSel",
                    "/StringService/DataInfo.asmx", data, 1, context);
        } else {
            stopLoading(false, false, false);
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }


    private void parseGHSelResult() {
        if (isSunlight) {
            initOptions();
            mDialog.dismiss();
            if (pGhList.size() > 0 && cGhList.size() > 0 && cGhList.get(0).size() > 0) {
                mPosition0 = 0;
                mPosition1 = 0;
                tv_terminal.setText(pGhList.get(mPosition0)[1].replace(getString(R.string.parsetstr10),"")+
                        cGhList.get(mPosition0).get(mPosition1)[1].replace(getString(R.string.parsetstr10),""));
                mRefreshLayout.autoRefresh();
            }
        } else {
            if (pGhList.size() > 0) {
                mPosition1 = 0;
                tv_terminal.setText(pGhList.get(mPosition1)[1]);
                mDialog.dismiss();
                if (tv_terminal.getText().length() > 0 && tv_time.getText().length() > 0) {
                    mRefreshLayout.autoRefresh();
                }
            } else {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.warn_str10));
            }
        }
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
                    tv_terminal.setText(pGhList.get(mPosition0)[1].replace(getString(R.string.parsetstr10),"")+
                            cGhList.get(mPosition0).get(mPosition1)[1].replace(getString(R.string.parsetstr10),""));
                    mRefreshLayout.autoRefresh();
                }
            }
        } else {
            if (pGhList == null) {
                getTerminal();
            } else {
                if (pGhList.size() > 0) {
                    mPosition1 = 0;
                    tv_terminal.setText(pGhList.get(mPosition1)[1]);
                    mRefreshLayout.autoRefresh();
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
                    list.add(cGhList.get(i).get(i1)[1]);
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
                tv_terminal.setText(pGhList.get(mPosition0)[1].replace(getString(R.string.parsetstr10),"") +
                        cGhList.get(mPosition0).get(mPosition1)[1].replace(getString(R.string.parsetstr10),""));
                mAdapter.getList().clear();
                mAdapter.notifyDataSetChanged();
                if (tv_terminal.getText().length() > 0 && tv_time.getText().length() > 0) {
                    mRefreshLayout.autoRefresh();
                }
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

    private String getTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        return formatter.format(date);
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
            if (!isLoad && PageIndex > 1) {
                PageIndex--;
            }
            mRefreshLayout.finishLoadMore(0, isLoad, isMore);
        }
    }

    private void register() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void unRegister() {
        WebServiceManage.dispose();
        EventBus.getDefault().unregister(this);
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
        if (mRefreshLayout != null)
            stopLoading(false, false, false);
    }
}
