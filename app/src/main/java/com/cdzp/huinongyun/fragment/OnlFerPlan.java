package com.cdzp.huinongyun.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.activity.OnlineFert;
import com.cdzp.huinongyun.adapter.FerAdapter;
import com.cdzp.huinongyun.adapter.IrrOnlAdapter;
import com.cdzp.huinongyun.bean.FertConfiguration;
import com.cdzp.huinongyun.bean.OnlAutoCtlParamter;
import com.cdzp.huinongyun.bean.SocketResult;
import com.cdzp.huinongyun.message.SocketMessage;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.MyCallback;
import com.cdzp.huinongyun.util.Operate;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

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
 * 在线施肥机计划参数
 */
public class OnlFerPlan extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private String TAG = "OnlFerPlan";
    private boolean isVisible, isRequest, isStart, isInit, isShow;
    private int conTypeMark;
    private View ll_fofp10, view_onl10, ll_fofp51, view_fofp51, ll_fofp7, view_fofp7, ll_fofp8,
            ll_fofp3, view_fofp3, ll_fofp4, view_fofp4, ll_fofp9, view_fofp9, ll_fofp12,
            ll_fofp11, ll_fofp13, view_fofp13, view_fofp14, ll_fofp14, view_fofp8, ll_fofp15,
            view_fofp15, ll_fofp16, view_fofp16, ll_fofp17, view_fofp17, view_fofp33, ll_fofp33,
            view_fofp44, ll_fofp44, ll_fofp5, view_fofp5, ll_fofp6, view_fofp6;
    private TextView tv_terminal, tv_parameter, tv_fofp1, tv_fofp2, tv_fofp3, tv_fofp4, tv_fofp5,
            tv_fofp6, tv_fofp7, tv_fofp8, tv_fofp9, tv_fofp10;
    private EditText et_fofp1, et_fofp2, et_fofp3, et_fofp4, et_fofp5,
            et_fofp6, et_fofp7, et_fofp8, et_fofp9, et_weekDay, et_fofp51, et_fofp16, et_fofp17;
    private int mPosition, mPosition2, irrNum, checkedItem, checkedItem2, checkedItem3 = -1,
            checkedItem4 = -1, checkedItem5 = -1, checkedItem9;
    private QMUIBottomSheet mBottomSheet1, mBottomSheet2;
    private List<OnlAutoCtlParamter> mList;
    private FerAdapter feradapter;
    private AlertDialog IrrDialog, FerDialog;
    private String[] FerStr;
    private IrrOnlAdapter irradapter;
    private boolean[] FerCheck = new boolean[5], strsCheck41;
    private AlertDialog.Builder builder1, builder2, builder3, builder4, builder5, builder9;
    private AlertDialog mDialog41, mDialog42;
    private byte AutoObject, StartCondition, storageTankNum;
    private String[] strs, strs2, strs3, strs4, strs41, strs5, strs9;
    private String strWeekDay;
    private TimePickerView pvTime;
    private Date startDate, endDate;
    private FertConfiguration config;
    private List<String[]> hasTanks, hasTanks1;//储液罐
    private QMUITipDialog mDialog;
    private CountDownTimer countDownTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_onl_fer_plan, container, false);
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
                if (tv_terminal.getText().length() > 0) {
                    isRequest = true;
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
        FerStr = new String[]{getString(R.string.fert_str19), "A", "B", "C", "D"};
        strs4 = new String[]{getString(R.string.onl_fert56), getString(R.string.onl_fert57),
                getString(R.string.onl_fert58)};
//        strs41 = new String[]{getString(R.string.onl_fert59), getString(R.string.onl_fert60),
//                getString(R.string.onl_fert61), getString(R.string.onl_fert62),
//                getString(R.string.onl_fert63), getString(R.string.onl_fert64)};
        strs41 = new String[]{getString(R.string.onl_fert59), getString(R.string.onl_fert60),
                getString(R.string.onl_fert61), getString(R.string.onl_fert62),
                getString(R.string.onl_fert63), getString(R.string.onl_fert64), getString(R.string.onl_fert65)};
        strsCheck41 = new boolean[7];
        strs5 = new String[]{getString(R.string.onl_fert14), getString(R.string.onl_fert51)};
        strs = new String[]{getString(R.string.onl_fert66), getString(R.string.onl_fert37)};
        strs2 = new String[]{getString(R.string.onl_fert67), getString(R.string.onl_fert68)};
        strs9 = new String[]{getString(R.string.onl_fert82), getString(R.string.onl_fert83)};
        builder1 = new AlertDialog.Builder(context);
        builder5 = new AlertDialog.Builder(context);

        pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (isStart) {
                    startDate = date;
                    tv_fofp4.setText(getTime(startDate));
                } else {
                    endDate = date;
                    tv_fofp5.setText(getTime(endDate));
                }
            }
        })
                .isCyclic(true)
                .isDialog(true)
                .setType(new boolean[]{true, true, true, true, true, false})
                .build();

        tv_terminal = view.findViewById(R.id.tv_fofp_terminal);
        tv_parameter = view.findViewById(R.id.tv_fofp_parameter);
        tv_fofp1 = view.findViewById(R.id.tv_fofp1);
        tv_fofp2 = view.findViewById(R.id.tv_fofp2);
        tv_fofp3 = view.findViewById(R.id.tv_fofp3);
        tv_fofp4 = view.findViewById(R.id.tv_fofp4);
        tv_fofp5 = view.findViewById(R.id.tv_fofp5);
        tv_fofp6 = view.findViewById(R.id.tv_fofp6);
        tv_fofp7 = view.findViewById(R.id.tv_fofp7);
        tv_fofp8 = view.findViewById(R.id.tv_fofp8);
        tv_fofp9 = view.findViewById(R.id.tv_fofp9);
        tv_fofp10 = view.findViewById(R.id.tv_fofp10);
        et_fofp1 = view.findViewById(R.id.et_fofp1);
        et_fofp2 = view.findViewById(R.id.et_fofp2);
        et_fofp3 = view.findViewById(R.id.et_fofp3);
        et_fofp4 = view.findViewById(R.id.et_fofp4);
        et_fofp5 = view.findViewById(R.id.et_fofp5);
        et_fofp6 = view.findViewById(R.id.et_fofp6);
        et_fofp7 = view.findViewById(R.id.et_fofp7);
        et_fofp8 = view.findViewById(R.id.et_fofp8);
        et_fofp9 = view.findViewById(R.id.et_fofp9);
        et_fofp51 = view.findViewById(R.id.et_fofp51);
        et_fofp16 = view.findViewById(R.id.et_fofp16);
        et_fofp17 = view.findViewById(R.id.et_fofp17);
        ll_fofp10 = view.findViewById(R.id.ll_fofp10);
        view_onl10 = view.findViewById(R.id.view_onl10);
        ll_fofp51 = view.findViewById(R.id.ll_onl_fofp51);
        view_fofp51 = view.findViewById(R.id.view_onl_fofp51);
        ll_fofp7 = view.findViewById(R.id.ll_fofp7);
        view_fofp7 = view.findViewById(R.id.view_fofp7);
        ll_fofp3 = view.findViewById(R.id.ll_onl_fofp3);
        view_fofp3 = view.findViewById(R.id.view_onl_fofp3);
        ll_fofp4 = view.findViewById(R.id.ll_onl_fofp4);
        view_fofp4 = view.findViewById(R.id.view_onl_fofp4);
        ll_fofp9 = view.findViewById(R.id.ll_fofp9);
        view_fofp9 = view.findViewById(R.id.view_fofp9);
        ll_fofp12 = view.findViewById(R.id.ll_onl_fofp12);
        ll_fofp11 = view.findViewById(R.id.ll_onl_fofp11);
        ll_fofp13 = view.findViewById(R.id.ll_onl_fofp13);
        view_fofp13 = view.findViewById(R.id.view_onl_fofp13);
        ll_fofp14 = view.findViewById(R.id.ll_onl_fofp14);
        view_fofp14 = view.findViewById(R.id.view_onl_fofp14);
        ll_fofp16 = view.findViewById(R.id.ll_onl_fofp16);
        view_fofp16 = view.findViewById(R.id.view_onl_fofp16);
        ll_fofp17 = view.findViewById(R.id.ll_onl_fofp17);
        view_fofp17 = view.findViewById(R.id.view_onl_fofp17);
        ll_fofp8 = view.findViewById(R.id.ll_fofp8);
        view_fofp8 = view.findViewById(R.id.view_fofp8);
        ll_fofp15 = view.findViewById(R.id.ll_fofp15);
        view_fofp15 = view.findViewById(R.id.view_fofp15);
        view_fofp33 = view.findViewById(R.id.view_fofp33);
        ll_fofp33 = view.findViewById(R.id.ll_fofp3);
        ll_fofp44 = view.findViewById(R.id.ll_fofp4);
        view_fofp44 = view.findViewById(R.id.view_fofp4);
        ll_fofp5 = view.findViewById(R.id.ll_fofp5);
        view_fofp5 = view.findViewById(R.id.view_fofp5);
        ll_fofp6 = view.findViewById(R.id.ll_fofp6);
        view_fofp6 = view.findViewById(R.id.view_fofp6);

        countDownTimer = new CountDownTimer(5000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.onl_fert189));
            }
        };

        view.findViewById(R.id.ll_fofp_terminal).setOnClickListener(this);
        view.findViewById(R.id.ll_fofp_parameter).setOnClickListener(this);
        view.findViewById(R.id.ib_fofp).setOnClickListener(this);
        view.findViewById(R.id.ll_fofp1).setOnClickListener(this);
        view.findViewById(R.id.ll_fofp2).setOnClickListener(this);
        view.findViewById(R.id.btn_ffap1).setOnClickListener(this);
        view.findViewById(R.id.btn_ffap2).setOnClickListener(this);
        view.findViewById(R.id.btn_ffap3).setOnClickListener(this);
        view.findViewById(R.id.btn_ffap4).setOnClickListener(this);
        ll_fofp6.setOnClickListener(this);
        ll_fofp5.setOnClickListener(this);
        ll_fofp33.setOnClickListener(this);
        ll_fofp44.setOnClickListener(this);
        ll_fofp8.setOnClickListener(this);
        ll_fofp7.setOnClickListener(this);
        ll_fofp9.setOnClickListener(this);
        ll_fofp10.setOnClickListener(this);
    }

    private void initData() {
        register();
        if (pGhList != null && pGhList.size() > 0) {
            mPosition = 0;
            tv_terminal.setText(pGhList.get(mPosition)[1]);
            mList = null;
            mBottomSheet2 = null;
            mPosition2 = 0;
            countDownTimer.cancel();
            countDownTimer.start();
            mDialog.show();
            getFertilizerConfig();
        } else Toasts.showInfo(context, getString(R.string.warn_str10));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 1:
                    parseResult1(message.getmResult());
                    break;
                default:
                    countDownTimer.cancel();
                    mDialog.dismiss();
                    break;
            }
        } else {
            countDownTimer.cancel();
            mDialog.dismiss();
            Toasts.showError(context, message.getmResult());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SocketResult result) {
        countDownTimer.cancel();
        if (result.isOK()) {
            conTypeMark = 0;
            switch (result.getLabel()) {
                case 719:
                    if (result.getmResult() instanceof ArrayList) {
                        List<?> list = (ArrayList) result.getmResult();
                        if (list.size() > 0 && list.get(0) instanceof OnlAutoCtlParamter) {
                            mList = (ArrayList) result.getmResult();
                            mPosition2 = 0;
                            tv_parameter.setText(getString(R.string.fertplan_str1) + "1");
                            notifyData();
                        } else Toasts.showInfo(context, getString(R.string.fertplan_str2));
                    }
                    break;
                case 721:
                    Toasts.showSuccess(context, getString(R.string.onl_fert69));
                    break;
                default:
                    break;
            }
        }
        mDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fofp_terminal:
                showBottomSheet1();
                break;
            case R.id.ll_fofp_parameter:
                if (pGhList.get(mPosition)[6].equals("0")) {
                    Toasts.showInfo(context, getString(R.string.onl_fert192));
                    return;
                }
                showBottomSheet2();
                break;
            case R.id.ll_fofp1://控制对象
                if (mList != null && mList.size() > 0 && tv_parameter.getText().length() > 0)
                    showDialog1();
                break;
            case R.id.ll_fofp2://启动条件
                if (mList != null && mList.size() > 0 && tv_parameter.getText().length() > 0)
                    showDialog2();
                break;
            case R.id.ll_fofp3://星期选择
                if (mList != null && mList.size() > 0 && tv_parameter.getText().length() > 0)
                    showDialog4();
                break;
            case R.id.ll_fofp4://开始时间
                if (mList != null && mList.size() > 0 && tv_parameter.getText().length() > 0) {
                    isStart = true;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(startDate);
                    pvTime.setDate(calendar);
                    pvTime.show();
                }
                break;
            case R.id.ll_fofp5://结束时间
                if (mList != null && mList.size() > 0 && tv_parameter.getText().length() > 0) {
                    isStart = false;
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTime(endDate);
                    pvTime.setDate(calendar1);
                    pvTime.show();
                }
                break;
            case R.id.ll_fofp6://结束条件
                if (mList != null && mList.size() > 0 && tv_parameter.getText().length() > 0)
                    showDialog5();
                break;
            case R.id.ll_fofp7://进肥通道
                if (mList != null && mList.size() > 0 && tv_parameter.getText().length() > 0) {
                    if (FerDialog != null) {
                        FerDialog.show();
                    }
                }
                break;
            case R.id.ll_fofp8://灌溉通道
                if (mList != null && mList.size() > 0 && tv_parameter.getText().length() > 0) {
                    if (IrrDialog != null) {
                        IrrDialog.show();
                    }
                }
                break;
            case R.id.ll_fofp9://配肥方式
                if (mList != null && mList.size() > 0 && tv_parameter.getText().length() > 0)
                    showDialog9();
                break;
            case R.id.ll_fofp10://储液罐
                if (mList != null && mList.size() > 0 && tv_parameter.getText().length() > 0)
                    showDialog3();
                break;
            case R.id.btn_ffap1:
                if (pGhList.get(mPosition)[6].equals("0")) {
                    Toasts.showInfo(context, getString(R.string.onl_fert192));
                    return;
                }
                if (mList != null) {
                    if (mList.size() > 0) {
                        save();
                    } else Toasts.showSuccess(context, getString(R.string.onl_fert70));
                }
                break;
            case R.id.btn_ffap2:
                if (pGhList.get(mPosition)[6].equals("0")) {
                    Toasts.showInfo(context, getString(R.string.onl_fert192));
                    return;
                }
                if (mList != null && mList.size() > 0 && mPosition2 < mList.size()) {
                    new QMUIDialog.MessageDialogBuilder(context)
                            .setTitle(getString(R.string.onl_fert71))
                            .setMessage(getString(R.string.fertplan_str1) + (mPosition2 + 1))
                            .addAction(getString(R.string.envir_str1), new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                }
                            })
                            .addAction(getString(R.string.personal_str6), new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                    delete();
                                }
                            })
                            .create().show();
                }
                break;
            case R.id.btn_ffap3:
                if (pGhList.get(mPosition)[6].equals("0")) {
                    Toasts.showInfo(context, getString(R.string.onl_fert192));
                    return;
                }
                if (mList != null) {
                    new QMUIDialog.MessageDialogBuilder(context)
                            .setTitle(getString(R.string.onl_fert72))
                            .addAction(getString(R.string.envir_str1), new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                }
                            })
                            .addAction(getString(R.string.personal_str6), new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                    deleteAll();
                                }
                            })
                            .create().show();
                }
                break;
            case R.id.btn_ffap4:
                if (pGhList.get(mPosition)[6].equals("0")) {
                    Toasts.showInfo(context, getString(R.string.onl_fert192));
                    return;
                }
                if (mList != null) {
                    new QMUIDialog.MessageDialogBuilder(context)
                            .setTitle(getString(R.string.onl_fert73))
                            .addAction(getString(R.string.envir_str1), new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                }
                            })
                            .addAction(getString(R.string.personal_str6), new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                    countDownTimer.cancel();
                                    countDownTimer.start();
                                    mDialog.show();
                                    socketRequest(720, mList);
                                }
                            })
                            .create().show();
                }
                break;
            case R.id.ib_fofp:
                if (pGhList.get(mPosition)[6].equals("0")) {
                    Toasts.showInfo(context, getString(R.string.onl_fert192));
                    return;
                }
                if (mList == null) {
                    mList = new ArrayList<>();
                }
                if (mList.size() < 64) {
                    new QMUIDialog.MessageDialogBuilder(context)
                            .setTitle(getString(R.string.onl_fert74))
                            .addAction(getString(R.string.envir_str1), new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                }
                            })
                            .addAction(getString(R.string.personal_str6), new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                    addInfo();
                                }
                            })
                            .create().show();
                } else Toasts.showSuccess(context, getString(R.string.onl_fert75));
                break;
            default:

                break;
        }
    }

    /**
     * 解析施肥机配置
     *
     * @param s
     */
    private void parseResult1(String s) {
        try {
            config = new Gson().fromJson(s, FertConfiguration.class);
            hasTanks = new ArrayList<>();
            hasTanks1 = new ArrayList<>();
            if (config.getData().getWList().get(7).getHasTank() == 1) {
                hasTanks.add(new String[]{getString(R.string.onl_fert89),
                        config.getData().getWList().get(7).getHasLevel() + "",
                        config.getData().getWList().get(7).getLevelType() + ""});
            }
            if (config.getData().getWList().get(8).getHasTank() == 1) {
                hasTanks.add(new String[]{getString(R.string.onl_fert90),
                        config.getData().getWList().get(8).getHasLevel() + "",
                        config.getData().getWList().get(8).getLevelType() + ""});
            }
            if (config.getData().getWList().get(9).getHasTank() == 1) {
                hasTanks.add(new String[]{getString(R.string.onl_fert91),
                        config.getData().getWList().get(9).getHasLevel() + "",
                        config.getData().getWList().get(9).getLevelType() + ""});
            }
            if (config.getData().getWList().get(10).getHasTank() == 1) {
                hasTanks.add(new String[]{getString(R.string.onl_fert92),
                        config.getData().getWList().get(10).getHasLevel() + "",
                        config.getData().getWList().get(10).getLevelType() + ""});
            }
            for (int i = 0; i < hasTanks.size(); i++) {
                if (hasTanks.get(i)[1].equals("1")) {
                    hasTanks1.add(hasTanks.get(i));
                }
            }
            isShow = true;
            socketRequest(718, null);
        } catch (Exception e) {
            countDownTimer.cancel();
            mDialog.dismiss();
        }
    }

    private void deleteAll() {
        mList.clear();
        mBottomSheet2 = null;
        mPosition2 = 0;
        tv_parameter.setText("");
        emptyTv();
        Toasts.showSuccess(context, getString(R.string.onl_fert76));
    }

    private void delete() {
        mList.remove(mPosition2);
        if (mList.size() > 0) {
            mPosition2 = 0;
            tv_parameter.setText(getString(R.string.fertplan_str1) + "1");
            initBottomSheet2();
            notifyData();
        } else {
            mBottomSheet2 = null;
            mPosition2 = 0;
            tv_parameter.setText("");
            emptyTv();
        }
        Toasts.showSuccess(context, getString(R.string.onl_fert77));
    }

    private void emptyTv() {
        tv_fofp1.setText("");
        tv_fofp2.setText("");
        tv_fofp3.setText("");
        tv_fofp4.setText("");
        tv_fofp5.setText("");
        tv_fofp6.setText("");
        et_fofp1.setText("");
        et_fofp2.setText("");
        tv_fofp10.setText("");
    }

    private void addInfo() {
        OnlAutoCtlParamter paramter = new OnlAutoCtlParamter();
        paramter.setECSetValue(1);
        paramter.setPHSetValue(7);
        paramter.setProportion_A(1);
        paramter.setProportion_B(1);
        paramter.setProportion_C(1);
        paramter.setProportion_D(1);
        paramter.setMaxValveNum((byte) 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        paramter.setInfoName(date.getTime() + "");
        paramter.setStartTime(sdf.format(date));
        paramter.setStopTime(sdf.format(date));
        mList.add(paramter);
        mPosition2 = 0;
        tv_parameter.setText(getString(R.string.fertplan_str1) + "1");
        initBottomSheet2();
        notifyData();
        Toasts.showSuccess(context, getString(R.string.onl_fert78));
    }

    private void notifyData() {
        if (mList != null && mList.size() > mPosition2) {
            try {
                AutoObject = mList.get(mPosition2).getAutoObject();
                if (AutoObject == 1) {
                    if (hasTanks == null || hasTanks.size() == 0) {
                        AutoObject = 0;
                    }
                }
                checkedItem = AutoObject;
                if (checkedItem == 0 || checkedItem == 1) {
                    tv_fofp1.setText(strs[checkedItem]);
                }

                StartCondition = mList.get(mPosition2).getStartCondition();
                if (StartCondition == 1) {
                    if (hasTanks1 == null || hasTanks1.size() == 0) {
                        StartCondition = 0;
                    }
                }
                checkedItem2 = StartCondition;
                if (checkedItem2 == 0 || checkedItem2 == 1) {
                    tv_fofp2.setText(strs2[checkedItem2]);
                }

                //解析储液罐
                storageTankNum = mList.get(mPosition2).getStorageTankNum();
                initBuilder3();

                switch (mList.get(mPosition2).getTimeCtrType()) {
                    case 0:
                        checkedItem4 = 0;
                        tv_fofp3.setText(getString(R.string.onl_fert79));
                        break;
                    case 1:
                        checkedItem4 = 1;
                        for (int i = 0; i < 7; i++) {
                            if ((mList.get(mPosition2).getWeekDay() & 1 << i) != 0) {
                                strsCheck41[i] = true;
                            } else strsCheck41[i] = false;
                        }
                        setTv3();
                        break;
                    case 2:
                        checkedItem4 = 2;
                        strWeekDay = mList.get(mPosition2).getWeekDay() + "";
                        tv_fofp3.setText(getString(R.string.onl_fert80) + strWeekDay +
                                getString(R.string.onl_fert81));
                        break;
                    default:
                        checkedItem4 = -1;
                        tv_fofp3.setText(getString(R.string.request_error2));
                        break;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                if (mList.get(mPosition2).getStartTime() != null) {
                    if (mList.get(mPosition2).getStartTime().length() > 0) {
                        startDate = sdf.parse(mList.get(mPosition2).getStartTime());
                    } else {
                        startDate = new Date();
                    }
                }
                tv_fofp4.setText(getTime(startDate));

                endDate = sdf.parse(mList.get(mPosition2).getStopTime());
                if (mList.get(mPosition2).getStopTime() != null) {
                    if (mList.get(mPosition2).getStopTime().length() > 0) {
                        endDate = sdf.parse(mList.get(mPosition2).getStopTime());
                    } else {
                        endDate = new Date();
                    }
                }
                tv_fofp5.setText(getTime(endDate));

                //控制方式
                checkedItem5 = mList.get(mPosition2).getManualCtrMode();
                if (checkedItem5 == 0 || checkedItem5 == 1) {
                    tv_fofp6.setText(strs5[checkedItem5]);
                }
                et_fofp51.setText(mList.get(mPosition2).getWaterLimit() + "");

                if (FerDialog == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View contentView = LayoutInflater.from(context).inflate(R.layout.irrlayout, null);
                    RecyclerView mRecyclerView = contentView.findViewById(R.id.irr);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(context, 5));

                    feradapter = new FerAdapter(FerCheck, context);
                    feradapter.setCallback(new MyCallback() {
                        @Override
                        public void checkBox(int position, boolean b) {
                            FerCheck[position] = b;
                            String str = "";
                            for (int i = 0; i < FerCheck.length; i++) {
                                if (FerCheck[i]) {
                                    str = str + FerStr[i] + "、";
                                }
                            }
                            if (str.length() > 0) {
                                tv_fofp7.setText(str.substring(0, str.length() - 1));
                            } else tv_fofp7.setText("");
                        }
                    });
                    mRecyclerView.setAdapter(feradapter);
                    builder.setTitle(getString(R.string.fert_str18));
                    builder.setView(contentView);
                    builder.setPositiveButton(getString(R.string.personal_str6), null);
                    FerDialog = builder.create();
                }

                String str = "";
                for (int i = 0; i < FerCheck.length; i++) {
                    if ((mList.get(mPosition2).getFertCh() & 1 << i) != 0) {
                        FerCheck[i] = true;
                        str = str + FerStr[i] + "、";
                    } else FerCheck[i] = false;
                }

                boolean[] fertChshow = new boolean[5];
                for (int i = 0; i < 5; i++) {
                    if (config.getData().getWList().get(i + 1).getHasTank() == 1) {
                        fertChshow[i] = true;
                    } else
                        fertChshow[i] = false;
                }

                feradapter.setFertChshow(fertChshow);
                feradapter.setCheck(FerCheck);
                if (str.length() > 0) {
                    tv_fofp7.setText(str.substring(0, str.length() - 1));
                } else tv_fofp7.setText("");
                feradapter.notifyDataSetChanged();

                int ValveNum = config.getData().getBaseInfo().getValveNum();
                irrNum = 0;
                if (IrrDialog == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View contentView = LayoutInflater.from(context).inflate(R.layout.irrlayout, null);
                    RecyclerView mRecyclerView = contentView.findViewById(R.id.irr);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(context, 5));
                    irradapter = new IrrOnlAdapter();
                    irradapter.setCallback(new MyCallback() {
                        @Override
                        public void checkBox(int position, boolean b) {
                            if (b) {
                                irrNum++;
                            } else {
                                irrNum--;
                            }
                            tv_fofp8.setText(getString(R.string.fert_str44) + irrNum +
                                    getString(R.string.fert_str45));
                        }
                    });
                    mRecyclerView.setAdapter(irradapter);
                    builder.setTitle(getString(R.string.fert_str17));
                    builder.setView(contentView);
                    builder.setPositiveButton(getString(R.string.personal_str6), null);
                    IrrDialog = builder.create();
                }
                for (int i = 0; i < 64; i++) {
                    if (i > ValveNum) break;
                    if ((mList.get(mPosition2).getIrrValvesL() & 1l << i) != 0) {
                        irrNum++;
                        irradapter.getCheck()[i] = true;
                    } else irradapter.getCheck()[i] = false;
                }
                for (int i = 64; i < 128; i++) {
                    if (i > ValveNum) break;
                    if ((mList.get(mPosition2).getIrrValvesH() & 1l << i) != 0) {
                        irrNum++;
                        irradapter.getCheck()[i] = true;
                    } else irradapter.getCheck()[i] = false;
                }
                irradapter.setValvesNum(ValveNum);
                tv_fofp8.setText(getString(R.string.fert_str44) + irrNum +
                        getString(R.string.fert_str45));
                irradapter.notifyDataSetChanged();

                et_fofp1.setText(mList.get(mPosition2).getRunTime() + "");
                et_fofp2.setText(mList.get(mPosition2).getPauseTime() + "");
                et_fofp3.setText(mList.get(mPosition2).getECSetValue() + "");
                et_fofp4.setText(mList.get(mPosition2).getPHSetValue() + "");
                et_fofp5.setText(mList.get(mPosition2).getMaxValveNum() + "");

                if (mList.get(mPosition2).getFertCtlMode() == 0) {
                    checkedItem9 = 0;
                } else {
                    checkedItem9 = 1;
                }
                tv_fofp9.setText(strs9[checkedItem9]);

                et_fofp6.setText(mList.get(mPosition2).getProportion_A() + "");
                et_fofp7.setText(mList.get(mPosition2).getProportion_B() + "");
                et_fofp8.setText(mList.get(mPosition2).getProportion_C() + "");
                et_fofp9.setText(mList.get(mPosition2).getProportion_D() + "");

                isHide();
                if (isShow) {
                    isShow = false;
                    Toasts.showSuccess(context, getString(R.string.parsetstr1));
                }
            } catch (Exception e) {
                Log.d(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void isHide() {
        switch (AutoObject) {
            case 0:
                //储液罐
                ll_fofp10.setVisibility(View.GONE);
                view_onl10.setVisibility(View.GONE);
                //星期选择
                ll_fofp33.setVisibility(View.VISIBLE);
                view_fofp33.setVisibility(View.VISIBLE);
                //开始时间
                ll_fofp44.setVisibility(View.VISIBLE);
                view_fofp44.setVisibility(View.VISIBLE);
                //结束时间
                ll_fofp5.setVisibility(View.VISIBLE);
                view_fofp5.setVisibility(View.VISIBLE);
                //结束条件
                ll_fofp6.setVisibility(View.VISIBLE);
                view_fofp6.setVisibility(View.VISIBLE);
                //水量控制
                if (hasTanks != null && hasTanks.size() == 0 && checkedItem5 == 1) {
                    ll_fofp51.setVisibility(View.VISIBLE);
                    view_fofp51.setVisibility(View.VISIBLE);
                } else {
                    ll_fofp51.setVisibility(View.GONE);
                    view_fofp51.setVisibility(View.GONE);
                }

                if (hasTanks != null && hasTanks.size() > 0) {
                    //EC
                    ll_fofp3.setVisibility(View.GONE);
                    view_fofp3.setVisibility(View.GONE);
                    //PH
                    ll_fofp4.setVisibility(View.GONE);
                    view_fofp4.setVisibility(View.GONE);
                } else {
                    //EC
                    ll_fofp3.setVisibility(View.VISIBLE);
                    view_fofp3.setVisibility(View.VISIBLE);
                    //PH
                    ll_fofp4.setVisibility(View.VISIBLE);
                    view_fofp4.setVisibility(View.VISIBLE);
                }
                //开启时间
                ll_fofp13.setVisibility(View.VISIBLE);
                view_fofp13.setVisibility(View.VISIBLE);
                //暂停时间
                ll_fofp14.setVisibility(View.VISIBLE);
                view_fofp14.setVisibility(View.VISIBLE);
                //最多阀门数
                ll_fofp15.setVisibility(View.VISIBLE);
                view_fofp15.setVisibility(View.VISIBLE);
                //灌溉通道
                ll_fofp8.setVisibility(View.VISIBLE);
                view_fofp8.setVisibility(View.VISIBLE);
                if (hasTanks != null && hasTanks.size() > 0) {
                    //进肥通道
                    ll_fofp7.setVisibility(View.GONE);
                    view_fofp7.setVisibility(View.GONE);
                    //配肥方式
                    ll_fofp9.setVisibility(View.GONE);
                    view_fofp9.setVisibility(View.GONE);
                    //配肥比例
                    ll_fofp12.setVisibility(View.GONE);
                    ll_fofp11.setVisibility(View.GONE);
                } else {
                    //进肥通道
                    ll_fofp7.setVisibility(View.VISIBLE);
                    view_fofp7.setVisibility(View.VISIBLE);
                    //配肥方式
                    ll_fofp9.setVisibility(View.VISIBLE);
                    view_fofp9.setVisibility(View.VISIBLE);
                    //配肥比例
                    ll_fofp12.setVisibility(View.VISIBLE);
                    ll_fofp11.setVisibility(View.VISIBLE);
                }
                //启动液位值
                ll_fofp16.setVisibility(View.GONE);
                view_fofp16.setVisibility(View.GONE);
                //停止液位值
                ll_fofp17.setVisibility(View.GONE);
                view_fofp17.setVisibility(View.GONE);
                break;
            case 1:
                //储液罐
                ll_fofp10.setVisibility(View.VISIBLE);
                view_onl10.setVisibility(View.VISIBLE);
                //开启时间
                ll_fofp13.setVisibility(View.GONE);
                view_fofp13.setVisibility(View.GONE);
                //暂停时间
                ll_fofp14.setVisibility(View.GONE);
                view_fofp14.setVisibility(View.GONE);
                //最多阀门数
                ll_fofp15.setVisibility(View.GONE);
                view_fofp15.setVisibility(View.GONE);
                //灌溉通道
                ll_fofp8.setVisibility(View.GONE);
                view_fofp8.setVisibility(View.GONE);
                //进肥通道
                ll_fofp7.setVisibility(View.VISIBLE);
                view_fofp7.setVisibility(View.VISIBLE);
                //EC
                ll_fofp3.setVisibility(View.VISIBLE);
                view_fofp3.setVisibility(View.VISIBLE);
                //PH
                ll_fofp4.setVisibility(View.VISIBLE);
                view_fofp4.setVisibility(View.VISIBLE);
                //配肥方式
                ll_fofp9.setVisibility(View.VISIBLE);
                view_fofp9.setVisibility(View.VISIBLE);
                //配肥比例
                ll_fofp12.setVisibility(View.VISIBLE);
                ll_fofp11.setVisibility(View.VISIBLE);
                //结束条件
                ll_fofp6.setVisibility(View.VISIBLE);
                view_fofp6.setVisibility(View.VISIBLE);
                switch (StartCondition) {
                    case 0:
                        //星期选择
                        ll_fofp33.setVisibility(View.VISIBLE);
                        view_fofp33.setVisibility(View.VISIBLE);
                        //开始时间
                        ll_fofp44.setVisibility(View.VISIBLE);
                        view_fofp44.setVisibility(View.VISIBLE);
                        //结束时间
                        ll_fofp5.setVisibility(View.VISIBLE);
                        view_fofp5.setVisibility(View.VISIBLE);
                        //启动液位值
                        ll_fofp16.setVisibility(View.GONE);
                        view_fofp16.setVisibility(View.GONE);
                        //停止液位值
                        ll_fofp17.setVisibility(View.GONE);
                        view_fofp17.setVisibility(View.GONE);
                        //结束条件
                        ll_fofp6.setVisibility(View.VISIBLE);
                        view_fofp6.setVisibility(View.VISIBLE);
                        switch (checkedItem5) {
                            case 0:
                                //水量控制
                                ll_fofp51.setVisibility(View.GONE);
                                view_fofp51.setVisibility(View.GONE);
                                break;
                            case 1:
                                //水量控制
                                ll_fofp51.setVisibility(View.VISIBLE);
                                view_fofp51.setVisibility(View.VISIBLE);
                                break;
                            default:
                                break;
                        }
                        break;
                    case 1:
                        //星期选择
                        ll_fofp33.setVisibility(View.GONE);
                        view_fofp33.setVisibility(View.GONE);
                        //开始时间
                        ll_fofp44.setVisibility(View.GONE);
                        view_fofp44.setVisibility(View.GONE);
                        //结束时间
                        ll_fofp5.setVisibility(View.GONE);
                        view_fofp5.setVisibility(View.GONE);
                        //结束条件
                        ll_fofp6.setVisibility(View.GONE);
                        view_fofp6.setVisibility(View.GONE);
                        //水量控制
                        ll_fofp51.setVisibility(View.GONE);
                        view_fofp51.setVisibility(View.GONE);
                        //启动液位值
                        ll_fofp16.setVisibility(View.VISIBLE);
                        view_fofp16.setVisibility(View.VISIBLE);
                        //停止液位值
                        ll_fofp17.setVisibility(View.VISIBLE);
                        view_fofp17.setVisibility(View.VISIBLE);
                        if (hasTanks1 != null && hasTanks1.size() > 0) {
                            if (hasTanks1.get(checkedItem3)[2].equals("0")) {
                                et_fofp16.setText("");
                                et_fofp17.setText("");
                                et_fofp16.setHint(getString(R.string.onl_fert68));
                                et_fofp17.setHint(getString(R.string.onl_fert84));
                                et_fofp16.setEnabled(false);
                                et_fofp17.setEnabled(false);
                            } else {
                                et_fofp16.setHint("");
                                et_fofp17.setHint("");
                                et_fofp16.setText(mList.get(mPosition2).getLowLevel() + "");
                                et_fofp17.setText(mList.get(mPosition2).getHighLevel() + "");
                                et_fofp16.setEnabled(true);
                                et_fofp17.setEnabled(true);
                            }
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    private void save() {
        try {
            mList.get(mPosition2).setAutoObject(AutoObject);
            //保存启动条件
            mList.get(mPosition2).setStartCondition(StartCondition);

            switch (checkedItem) {
                case 0:
                    saveWeek();
                    saveTime();
                    saveManualCtrMode();
                    //保存开启暂停时间
                    if (et_fofp1.getText().length() > 0) {
                        mList.get(mPosition2).setRunTime(Integer.parseInt(et_fofp1.getText().toString()));
                    } else mList.get(mPosition2).setRunTime(0);

                    if (et_fofp2.getText().length() > 0) {
                        mList.get(mPosition2).setPauseTime(Integer.parseInt(et_fofp2.getText().toString()));
                    } else mList.get(mPosition2).setPauseTime(0);

                    //保存最多阀门数
                    if (et_fofp5.getText().length() > 0) {
                        mList.get(mPosition2).setMaxValveNum(Byte.parseByte(et_fofp5.getText().toString()));
                    } else mList.get(mPosition2).setMaxValveNum((byte) 0);
                    //保存灌溉通道
                    saveIrr();

                    //没有输液管的情况
                    if (hasTanks == null || hasTanks.size() == 0) {
                        //保存吸肥通道
                        saveFertCh();
                        //保存EC，PH
                        if (et_fofp3.getText().length() > 0) {
                            mList.get(mPosition2).setECSetValue(Float.parseFloat(et_fofp3.getText().toString()));
                        } else mList.get(mPosition2).setECSetValue(0);
                        if (et_fofp4.getText().length() > 0) {
                            mList.get(mPosition2).setPHSetValue(Float.parseFloat(et_fofp4.getText().toString()));
                        } else mList.get(mPosition2).setPHSetValue(0);
                        //保存配肥模式
                        mList.get(mPosition2).setFertCtlMode((byte) checkedItem9);
                        if (et_fofp6.getText().length() > 0) {
                            mList.get(mPosition2).setProportion_A(Float.parseFloat(et_fofp6.getText().toString()));
                        } else mList.get(mPosition2).setProportion_A(0);
                        if (et_fofp7.getText().length() > 0) {
                            mList.get(mPosition2).setProportion_B(Float.parseFloat(et_fofp7.getText().toString()));
                        } else mList.get(mPosition2).setProportion_B(0);
                        if (et_fofp8.getText().length() > 0) {
                            mList.get(mPosition2).setProportion_C(Float.parseFloat(et_fofp8.getText().toString()));
                        } else mList.get(mPosition2).setProportion_C(0);
                        if (et_fofp9.getText().length() > 0) {
                            mList.get(mPosition2).setProportion_D(Float.parseFloat(et_fofp9.getText().toString()));
                        } else mList.get(mPosition2).setProportion_D(0);
                    }
                    break;
                case 1:
                    //保存储液罐
                    mList.get(mPosition2).setStorageTankNum(storageTankNum);
                    //保存吸肥通道
                    saveFertCh();
                    //保存EC，PH
                    if (et_fofp3.getText().length() > 0) {
                        mList.get(mPosition2).setECSetValue(Float.parseFloat(et_fofp3.getText().toString()));
                    } else mList.get(mPosition2).setECSetValue(0);
                    if (et_fofp4.getText().length() > 0) {
                        mList.get(mPosition2).setPHSetValue(Float.parseFloat(et_fofp4.getText().toString()));
                    } else mList.get(mPosition2).setPHSetValue(0);
                    //保存配肥模式
                    mList.get(mPosition2).setFertCtlMode((byte) checkedItem9);
                    if (et_fofp6.getText().length() > 0) {
                        mList.get(mPosition2).setProportion_A(Float.parseFloat(et_fofp6.getText().toString()));
                    } else mList.get(mPosition2).setProportion_A(0);
                    if (et_fofp7.getText().length() > 0) {
                        mList.get(mPosition2).setProportion_B(Float.parseFloat(et_fofp7.getText().toString()));
                    } else mList.get(mPosition2).setProportion_B(0);
                    if (et_fofp8.getText().length() > 0) {
                        mList.get(mPosition2).setProportion_C(Float.parseFloat(et_fofp8.getText().toString()));
                    } else mList.get(mPosition2).setProportion_C(0);
                    if (et_fofp9.getText().length() > 0) {
                        mList.get(mPosition2).setProportion_D(Float.parseFloat(et_fofp9.getText().toString()));
                    } else mList.get(mPosition2).setProportion_D(0);

                    if (StartCondition == 0) {
                        saveWeek();
                        saveTime();
                        saveManualCtrMode();
                    } else {
                        //保存低液位启动值
                        if (et_fofp16.getText().length() > 0) {
                            mList.get(mPosition2).setLowLevel(Short.parseShort(et_fofp16.getText().toString()));
                        }
                        if (et_fofp17.getText().length() > 0) {
                            mList.get(mPosition2).setHighLevel(Short.parseShort(et_fofp17.getText().toString()));
                        }
                    }
                    break;
                default:
                    break;
            }
            Toasts.showSuccess(context, getString(R.string.devmain_str7));
        } catch (Exception e) {
            Toasts.showError(context, getString(R.string.onl_fert85));
        }
    }

    private void saveIrr() {
        long IrrValvesL = 0;
        for (int i = 63; i >= 0; i--) {
            IrrValvesL = IrrValvesL << 1;
            if (irradapter.getCheck()[i]) IrrValvesL += 1;
        }
        mList.get(mPosition2).setIrrValvesL(IrrValvesL);

        long IrrValvesH = 0;
        for (int i = 127; i >= 64; i--) {
            IrrValvesH = IrrValvesH << 1;
            if (irradapter.getCheck()[i]) IrrValvesH += 1;
        }
        mList.get(mPosition2).setIrrValvesH(IrrValvesH);
    }

    private void saveFertCh() {
        byte FertCh = 0;
        for (int i = 4; i >= 0; i--) {
            FertCh = (byte) (FertCh << 1);
            if (FerCheck[i]) FertCh += 1;
        }
        mList.get(mPosition2).setFertCh(FertCh);
    }

    /**
     * 保存结束条件
     */
    private void saveManualCtrMode() {
        try {
            switch (checkedItem5) {
                case 0:
                    mList.get(mPosition2).setManualCtrMode((byte) 0);
                    break;
                case 1:
                    mList.get(mPosition2).setManualCtrMode((byte) 1);
                    mList.get(mPosition2).setWaterLimit(Byte.parseByte(et_fofp51.getText().toString()));
                    break;
                default:
                    Toasts.showError(context, getString(R.string.onl_fert86));
                    break;
            }
        } catch (Exception e) {
            Toasts.showError(context, getString(R.string.onl_fert87));
        }
    }

    private void saveTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        mList.get(mPosition2).setStartTime(sdf.format(startDate));
        mList.get(mPosition2).setStopTime(sdf.format(endDate));
    }

    private void saveWeek() {
        if (tv_fofp3.getText().length() > 0) {
            try {
                switch (checkedItem4) {
                    case 0:
                        mList.get(mPosition2).setTimeCtrType((byte) 0);
                        break;
                    case 1:
                        mList.get(mPosition2).setTimeCtrType((byte) 1);
                        int weekDay = 0;
                        for (int i = 6; i >= 0; i--) {
                            weekDay = weekDay << 1;
                            if (strsCheck41[i]) weekDay += 1;
                        }
                        mList.get(mPosition2).setWeekDay((byte) weekDay);
                        break;
                    case 2:
                        mList.get(mPosition2).setTimeCtrType((byte) 2);
                        mList.get(mPosition2).setWeekDay(Byte.parseByte(strWeekDay));
                        break;
                }
            } catch (Exception e) {
                Toasts.showError(context, getString(R.string.devmain_str8));
            }
        } else Toasts.showError(context, getString(R.string.onl_fert88));
    }

    private void showDialog9() {
        if (builder9 == null) {
            builder9 = new AlertDialog.Builder(context);
        }
        builder9.setSingleChoiceItems(strs9, checkedItem9, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkedItem9 = which;
                tv_fofp9.setText(strs9[checkedItem9]);
                dialog.dismiss();
            }
        });
        builder9.show();
    }

    /**
     * 结束条件
     */
    private void showDialog5() {
        if (AutoObject == 0 && hasTanks != null && hasTanks.size() > 0) {
            checkedItem5 = 0;
            tv_fofp6.setText(strs5[checkedItem5]);
//            isHide();
            return;
        }
        if (checkedItem == 0) {
            if (hasTanks == null || hasTanks.size() == 0) {
                showDialog51();
            }
        } else {
            showDialog51();
        }
    }

    private void showDialog51() {
        builder5.setSingleChoiceItems(strs5, checkedItem5, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkedItem5 = which;
                tv_fofp6.setText(strs5[checkedItem5]);
                isHide();
                dialog.dismiss();
            }
        });
        builder5.show();
    }

    /**
     * 星期选择
     */
    private void showDialog4() {
        if (builder4 == null) {
            builder4 = new AlertDialog.Builder(context);
        }
        builder4.setSingleChoiceItems(strs4, checkedItem4, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkedItem4 = which;
                switch (checkedItem4) {
                    case 0:
                        tv_fofp3.setText(strs4[checkedItem4]);
                        break;
                    case 1:
                        showDialog41();
                        break;
                    case 2:
                        showDialog42();
                        break;
                }
                dialog.dismiss();
            }
        });
        builder4.show();
    }

    private void showDialog42() {
        if (mDialog42 == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(getString(R.string.onl_fert58));
            View contentView = LayoutInflater.from(context).inflate(R.layout.onlfer_weekday_edit, null);
            et_weekDay = contentView.findViewById(R.id.et_onl_weekDay);
            builder.setView(contentView);
            builder.setPositiveButton(getString(R.string.personal_str6), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (et_weekDay.getText().length() > 0) {
                        strWeekDay = et_weekDay.getText().toString();
                    } else {
                        strWeekDay = "0";
                    }
                    tv_fofp3.setText(getString(R.string.onl_fert80) + strWeekDay +
                            getString(R.string.onl_fert81));
                }
            });
            mDialog42 = builder.create();
            mDialog42.setCancelable(false);
        }
        et_weekDay.setText(strWeekDay);
        mDialog42.show();
    }

    private void showDialog41() {
        if (mDialog41 == null) {
            AlertDialog.Builder builder41 = new AlertDialog.Builder(context);
            builder41.setMultiChoiceItems(strs41, strsCheck41, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    strsCheck41[which] = isChecked;
                }
            });
            builder41.setPositiveButton(getString(R.string.personal_str6),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setTv3();
                        }
                    });
            mDialog41 = builder41.create();
            mDialog41.setCancelable(false);
        }
        mDialog41.show();
    }

    private void showDialog3() {
        if (builder3 != null && strs3 != null && strs3.length > 0) {
            builder3.setSingleChoiceItems(strs3, checkedItem3, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkedItem3 = which;
                    int length = strs3[checkedItem3].length();
                    storageTankNum = Byte.parseByte(strs3[checkedItem3].substring(length - 1, length));
                    storageTankNum--;
                    tv_fofp10.setText(strs3[checkedItem3]);
                    isHide();
                    dialog.dismiss();
                }
            });
            builder3.show();
        }
    }

    //初始化储液罐
    private void initBuilder3() {
        if (hasTanks != null && hasTanks.size() > 0) {
            switch (StartCondition) {
                case 0:
                    strs3 = new String[hasTanks.size()];
                    hasTanks1 = new ArrayList<>();
                    for (int i = 0; i < hasTanks.size(); i++) {
                        strs3[i] = hasTanks.get(i)[0];
                        hasTanks1.add(hasTanks.get(i));
                    }
                    builder3 = new AlertDialog.Builder(context);
                    checkedItem3 = 0;
                    if (strs3.length > 0) {
                        int length = strs3[checkedItem3].length();
                        storageTankNum = Byte.parseByte(strs3[checkedItem3].substring(length - 1, length));
                        storageTankNum--;
                        tv_fofp10.setText(strs3[checkedItem3]);
                    }
                    break;
                case 1:
                    List<String> list = new ArrayList<>();
                    hasTanks1 = new ArrayList<>();
                    for (int i = 0; i < hasTanks.size(); i++) {
                        if (hasTanks.get(i)[1].equals("1")) {
                            list.add(hasTanks.get(i)[0]);
                            hasTanks1.add(hasTanks.get(i));
                        }
                    }
                    strs3 = list.toArray(new String[list.size()]);
                    builder3 = new AlertDialog.Builder(context);
                    checkedItem3 = 0;
                    if (strs3.length > 0) {
                        int length = strs3[checkedItem3].length();
                        storageTankNum = Byte.parseByte(strs3[checkedItem3].substring(length - 1, length));
                        storageTankNum--;
                        tv_fofp10.setText(strs3[checkedItem3]);
                    }
                    break;
                default:
                    builder3 = null;
                    break;
            }
        } else builder3 = null;
    }

    private void showDialog2() {
        if (AutoObject == 1 && hasTanks1 != null && hasTanks1.size() > checkedItem3
                && hasTanks1.get(checkedItem3)[1].equals("1")) {
            if (builder2 == null) {
                builder2 = new AlertDialog.Builder(context);
            }
            builder2.setSingleChoiceItems(strs2, checkedItem2, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkedItem2 = which;
                    StartCondition = (byte) checkedItem2;
                    tv_fofp2.setText(strs2[checkedItem2]);

                    checkedItem5 = 0;
                    tv_fofp6.setText(strs5[checkedItem5]);

                    initBuilder3();
                    isHide();
                    dialog.dismiss();
                }
            });
            builder2.show();
        }
    }

    private void showDialog1() {
        if (hasTanks != null && hasTanks.size() > 0) {
            builder1.setSingleChoiceItems(strs, checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkedItem = which;
                    AutoObject = (byte) checkedItem;
                    tv_fofp1.setText(strs[checkedItem]);

                    checkedItem2 = 0;
                    StartCondition = (byte) checkedItem2;
                    tv_fofp2.setText(strs2[checkedItem2]);

                    checkedItem5 = 0;
                    tv_fofp6.setText(strs5[checkedItem5]);

                    initBuilder3();
                    isHide();
                    dialog.dismiss();
                }
            });
            builder1.show();
        }
    }

    private void setTv10() {
        int length = hasTanks.get(checkedItem3)[0].length();
        storageTankNum = Byte.parseByte(hasTanks.get(checkedItem3)[0].substring(length - 1, length));
        storageTankNum--;
        tv_fofp10.setText(strs3[checkedItem3]);
    }

    private void setTv3() {
        String str = "";
        for (int i = 0; i < strsCheck41.length; i++) {
            if (strsCheck41[i]) {
                str = str + strs41[i] + "、";
            }
        }
        if (str.length() > 0) {
            tv_fofp3.setText(str.substring(0, str.length() - 1));
        } else tv_fofp3.setText("");
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
        } else {
            countDownTimer.cancel();
            mDialog.dismiss();
        }
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
                                tv_parameter.setText("");
                                dialog.dismiss();
                                mList = null;
                                mBottomSheet2 = null;
                                mPosition2 = 0;
                                emptyTv();
                                config = null;
                                conTypeMark = 0;
                                countDownTimer.cancel();
                                countDownTimer.start();
                                mDialog.show();
                                getFertilizerConfig();
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

    private void showBottomSheet2() {
        if (mList != null) {
            if (mList.size() > 0) {
                if (mBottomSheet2 == null) {
                    QMUIBottomSheet.BottomListSheetBuilder mBuilder = new QMUIBottomSheet.BottomListSheetBuilder(context, true)
                            .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                                @Override
                                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                    mPosition2 = position;
                                    tv_parameter.setText(getString(R.string.fertplan_str1) + (position + 1));
                                    dialog.dismiss();
                                    notifyData();
                                }
                            });
                    for (int i = 0; i < mList.size(); i++) {
                        mBuilder.addItem(getString(R.string.fertplan_str1) + (i + 1));
                    }
                    mBuilder.setCheckedIndex(mPosition2);
                    mBottomSheet2 = mBuilder.build();
                }
                mBottomSheet2.show();
            } else Toasts.showInfo(context, getString(R.string.fertplan_str2));
        } else {
            countDownTimer.cancel();
            countDownTimer.start();
            mDialog.show();
            socketRequest(718, null);
        }
    }

    private void initBottomSheet2() {
        if (mList != null) {
            if (mList.size() > 0) {
                QMUIBottomSheet.BottomListSheetBuilder mBuilder = new QMUIBottomSheet.BottomListSheetBuilder(context, true)
                        .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                mPosition2 = position;
                                tv_parameter.setText(getString(R.string.fertplan_str1) + (position + 1));
                                dialog.dismiss();
                                notifyData();
                            }
                        });
                for (int i = 0; i < mList.size(); i++) {
                    mBuilder.addItem(getString(R.string.fertplan_str1) + (i + 1));
                }
                mBuilder.setCheckedIndex(mPosition2);
                mBottomSheet2 = mBuilder.build();
            }
        }
    }

    /**
     * 获取值施肥机配置
     */
    private void getFertilizerConfig() {
        if (Data.userResult != null && pGhList != null && pGhList.size() > 0) {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"GreenhouseID", pGhList.get(mPosition)[0]});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("GetFertilizerConfig",
                    "/StringService/DeviceSet.asmx", data, 1, context);
        } else {
            countDownTimer.cancel();
            mDialog.dismiss();
        }
    }

    private String getTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        mList = null;
    }

}
