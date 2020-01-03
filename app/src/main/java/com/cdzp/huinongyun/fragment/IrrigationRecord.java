package com.cdzp.huinongyun.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.activity.OnlineFert;
import com.cdzp.huinongyun.adapter.DroppedFormAdapter;
import com.cdzp.huinongyun.adapter.IrrRecordAdapter;
import com.cdzp.huinongyun.bean.IrriRecord;
import com.cdzp.huinongyun.bean.SocketResult;
import com.cdzp.huinongyun.message.SocketMessage;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Operate;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
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

import static com.cdzp.huinongyun.util.Data.pGhList;

/**
 * 在线施肥机灌溉记录
 */
public class IrrigationRecord extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private String TAG = "OnlFerPlan";
    private boolean isVisible, isInit, isRequest, isStart;
    private int conTypeMark, mPosition;
    private QMUITipDialog mDialog;
    private TextView tv_terminal, tv_date;
    private TimePickerView pvTime;
    private Date mDate;
    private QMUIBottomSheet mBottomSheet1;
    private List<IrriRecord> mList;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private IrrRecordAdapter mAdapter;
    private CountDownTimer countDownTimer1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_irrigation_record, container, false);
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
                if (!isInit) {
                    isInit = true;
                    initData();
                }
            }
        } else {
            WebServiceManage.dispose();
            EventBus.getDefault().unregister(this);
            isRequest = false;
        }
    }

    private void initView() {
        context = getContext();
        mDialog = ((OnlineFert) getActivity()).getmDialog();
        tv_terminal = view.findViewById(R.id.tv_fir_terminal);
        tv_date = view.findViewById(R.id.tv_fir_date);

        mDate = new Date();
        tv_date.setText(getTime(mDate));
        pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mDate = date;
                tv_date.setText(getTime(mDate));
                mAdapter.setmList(null);
                mAdapter.notifyDataSetChanged();

                if (pGhList.get(mPosition)[6].equals("0")) {
                    Toasts.showInfo(context, getString(R.string.onl_fert192));
                    return;
                }
                mDialog.show();
                socketRequest(728, getTime1(mDate));
                countDownTimer1.cancel();
                countDownTimer1.start();
//                if (tv_terminal.getText().length() > 0) {
//                    socketRequest(728, getTime1(mDate));
//                }
            }
        })
                .isCyclic(true)
                .setType(new boolean[]{true, true, true, false, false, false})
                .build();

        countDownTimer1 = new CountDownTimer(5000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                finishRefresh(false);
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.onl_fert189));
            }
        };

        mRefreshLayout = view.findViewById(R.id.mRefreshLayout_fir);
//        mRefreshLayout.setEnableOverScrollBounce(false);//是否启用越界回弹
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(context));//设置Header
//        mRefreshLayout.setRefreshFooter(new ClassicsFooter(context));//设置Footer
        mRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
//        mRefreshLayout.setEnableAutoLoadMore(false);//是否启用列表惯性滑动到底部时自动加载更多
        mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能

        mRecyclerView = view.findViewById(R.id.mRecyclerView_fir);
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(context.getResources().
                getColor(R.color.decoration), 1, 1));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new IrrRecordAdapter(context);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (tv_date.getText().length() > 0 && tv_terminal.getText().length() > 0) {
                    if (pGhList.get(mPosition)[6].equals("0")) {
                        Toasts.showInfo(context, getString(R.string.onl_fert192));
                        finishRefresh(false);
                        return;
                    }
                    countDownTimer1.cancel();
                    countDownTimer1.start();
                    socketRequest(728, getTime1(mDate));
                } else {
                    finishRefresh(false);
                    Toasts.showInfo(context, getString(R.string.request_error6));
                }
            }
        });

        view.findViewById(R.id.ll_fir_terminal).setOnClickListener(this);
        view.findViewById(R.id.ll_fir_date).setOnClickListener(this);
    }

    private void initData() {
        if (pGhList != null && pGhList.size() > 0) {
            mPosition = 0;
            tv_terminal.setText(pGhList.get(mPosition)[1]);
            mDialog.show();
            countDownTimer1.cancel();
            countDownTimer1.start();
            socketRequest(728, getTime1(mDate));
        } else Toasts.showInfo(context, getString(R.string.warn_str10));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SocketResult result) {
        if (result.isOK()) {
            conTypeMark = 0;
            switch (result.getLabel()) {
                case 729:
                    if (result.getmResult() instanceof ArrayList) {
                        List<?> list = (ArrayList) result.getmResult();
                        if (list.size() > 0 && list.get(0) instanceof IrriRecord) {
                            mList = (ArrayList) result.getmResult();
                            notifyData();
                        } else {
                            finishRefresh(true);
                            Toasts.showInfo(context, getString(R.string.onl_fert190));
                        }
                    }
                    break;
                default:
                    finishRefresh(true);
                    break;
            }
        } else finishRefresh(true);
        countDownTimer1.cancel();
        mDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fir_terminal:
                showBottomSheet1();
                break;
            case R.id.ll_fir_date:
                pvTime.show();
                break;
            default:
                break;
        }
    }

    private void notifyData() {
        for (int i = 0; i < mList.size(); i++) {
            StringBuilder builder = new StringBuilder();
            if (mList.get(i).getAutoObject() == 0) {
                for (int i1 = 0; i1 < 64; i1++) {
                    if ((mList.get(i).getIrrValvesL() & 1l << i1) != 0) {
                        if (builder.length() > 0) {
                            builder.append(",");
                        }
                        builder.append((i1 + 1));
                        builder.append("#");
                    }
                }
                for (int i1 = 0; i1 < 64; i1++) {
                    if ((mList.get(i).getIrrValvesH() & 1l << i1) != 0) {
                        if (builder.length() > 0) {
                            builder.append(",");
                        }
                        builder.append((i1 + 64));
                        builder.append("#");
                    }
                }
            }
            mList.get(i).setIrrValves(builder.toString());
        }
        countDownTimer1.cancel();
        mAdapter.setmList(mList);
        mAdapter.notifyDataSetChanged();
        finishRefresh(true);
    }

    private void socketRequest(int commandType, Object object) {
        if (Data.userResult != null && pGhList != null) {
            SocketMessage message = new SocketMessage();
            message.setZP_KEY_CLIENT(987656789);
            message.setRequestType(708);
            message.setmSender(Data.userResult.getData().get(0).getUserName());
            message.setmReceiver(pGhList.get(mPosition)[2]);
            message.setCommandType(commandType);
            message.setResult(object);

            if (pGhList.get(mPosition)[3].equals("1") && conTypeMark < 4) {
                message.setIp(pGhList.get(mPosition)[4]);
                message.setPort(Integer.parseInt(pGhList.get(mPosition)[5]));
                conTypeMark++;
            } else {
                message.setIp(Data.SERVER_IP);
                message.setPort(Data.SERVER_PORT);
            }
            Operate.getDefault().getSender().messageRequest(message);
        } else mDialog.show();
    }

    private void showBottomSheet1() {
        if (pGhList != null && pGhList.size() > 0) {
            if (mBottomSheet1 == null) {
                QMUIBottomSheet.BottomListSheetBuilder mBuilder = new QMUIBottomSheet.BottomListSheetBuilder(context, true)
                        .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                mPosition = position;
                                tv_terminal.setText(pGhList.get(mPosition)[1]);
                                dialog.dismiss();
                                mAdapter.setmList(null);
                                mAdapter.notifyDataSetChanged();

                                if (pGhList.get(mPosition)[6].equals("0")) {
                                    Toasts.showInfo(context, getString(R.string.onl_fert192));
                                    return;
                                }

                                mDialog.show();
                                countDownTimer1.cancel();
                                countDownTimer1.start();
                                socketRequest(728, getTime1(mDate));
//                                if (tv_date.getText().length() > 0) {
//                                    socketRequest(728, getTime1(mDate));
//                                }
                            }
                        });
                for (int i = 0; i < pGhList.size(); i++) {
                    String str = pGhList.get(i)[1];
                    if (str.contains(getString(R.string.dev_str2))) break;
                    mBuilder.addItem(str);
                }
                mBuilder.setCheckedIndex(mPosition);
                mBottomSheet1 = mBuilder.build();
            }
            mBottomSheet1.show();
        } else Toasts.showInfo(context, getString(R.string.request_error8));
    }

    /**
     * @param isRefresh 是否刷新成功
     */
    private void finishRefresh(boolean isRefresh) {
        if (mRefreshLayout.getState() == RefreshState.Refreshing) {
            mRefreshLayout.finishRefresh(0, isRefresh);
        }
    }

    private String getTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    private String getTime1(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(date);
    }

    private void register() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible) {
            register();
            if (!isInit) {
                isInit = true;
                initData();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
