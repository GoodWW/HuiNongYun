package com.cdzp.huinongyun.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.activity.OnlFertConfig;
import com.cdzp.huinongyun.activity.OnlineFert;
import com.cdzp.huinongyun.adapter.FerAdapter;
import com.cdzp.huinongyun.adapter.IrrAdapter;
import com.cdzp.huinongyun.adapter.IrrOnlAdapter;
import com.cdzp.huinongyun.adapter.OnlIrrAdapter;
import com.cdzp.huinongyun.bean.ControlStatus;
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
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.cdzp.huinongyun.util.Data.pGhList;

/**
 * 在线施肥机
 */
public class OnlFert extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private String TAG = "OnlFert";
    private QMUIBottomSheet mBottomSheet1, mBottomSheet2, mBottomSheet3, mBottomSheet4;
    private RelativeLayout rl_storage, rl_manual, rl_cyg1, rl_cyg2, rl_cyg3, rl_cyg4,
            rl_handA1;
    private LinearLayout rl_handA2;
    private LinearLayout ll_fert1;
    private TextView tv_terminal, tv_parameter, tv_huishui, tv_huishuizhi, tv_cyg1, tv_cyg2, tv_cyg3,
            tv_qsg, tv_cyg4, tv_suan, tv_suan2, tv_a, tv_a2, tv_b, tv_b2, tv_c, tv_c2, tv_d, tv_d2,
            tv_ec1, tv_ec2, tv_gd, tv_water, tv_abnormal, tv_ph, tv_ls, tv_irr1, tv_irr2, tv_irr3,
            tv_irr4, tv_irr5, tv_irr6, tv_irr7, tv_irr8, tv_irr9, tv_irr10, tv_irr11, tv_irr12,
            tv_irr13, tv_irr14, tv_irr15, tv_irr16, tv_irr17, tv_irr18, tv_irr19, tv_irr20, tv_irr21,
            tv_irr22, tv_irr23, tv_irr24, tv_irr25, tv_irr26, tv_irr27, tv_irr28, tv_irr29, tv_irr30,
            tv_irr31, tv_irr32, tv_tw, tv_tw2, tv_xf, tv_fm, tv_start, tv_zyg, tv_monitor, tv_yxsc,
            tv_xf2, tv_handA, tv_zd1, tv_zd2;
    private ImageView iv_huishui, iv_huishui2, iv_cyg1, iv_cyg2, iv_cyg3, iv_cyg4, iv_cyg5, iv_cyg6,
            iv_cyg7, iv_cyg8, iv_qsg, iv_qsg1, iv_suan, iv_a, iv_b, iv_c, iv_d, iv_sl, iv_irr1, iv_irr2,
            iv_irr3, iv_irr4, iv_irr5, iv_irr6, iv_irr7, iv_irr8, iv_irr9, iv_irr10, iv_irr11,
            iv_irr12, iv_irr13, iv_irr14, iv_irr15, iv_irr16, iv_irr17, iv_irr18, iv_irr19,
            iv_irr20, iv_irr21, iv_irr22, iv_irr23, iv_irr24, iv_irr25, iv_irr26, iv_irr27, iv_irr28,
            iv_irr29, iv_irr30, iv_irr31, iv_irr32;
    private EditText et_time1, et_time2, et_time3, et_time4, et_time5, et_time6, et_ec, et_ph,
            et_yxsc;
    private View view_huishui, view_huishui2, view_huishui1;
    private CountDownTimer mDownTimer, mDownTimer1;
    private QMUITipDialog mDialog;
    private TimerTask task;
    private Timer timer;
    private boolean isVisible, isInit, isRequest;
    private int mPosition, conTypeMark, mPosition2, mPosition3, irrNum, fertRun, storageRun,
            runMode;
    private byte manualCtrMode, AutoObject, StartCondition, storageTankNum;
    public static FertConfiguration config;
    private OnlIrrAdapter mAdapter;
    private List<String[]> hasTanks;//储液罐
    private IrrOnlAdapter irradapter;
    private boolean[] fertChshow, FerCheck, FerCheck2;
    private FerAdapter feradapter, feradapter2;
    private AlertDialog IrrDialog = null, FerDialog = null, FerDialog2 = null, irrDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_onl_fert, container, false);
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
                if (tv_terminal.getText().length() > 0) {
                    isRequest = true;
                }
//                if (!isInit) {
//                    isInit = true;
//                    initData();
//                }
            }
        } else {
            if (mDownTimer1 != null) mDownTimer1.cancel();
            WebServiceManage.dispose();
            EventBus.getDefault().unregister(this);
            isRequest = false;
        }
    }

    private void initView() {
        context = getContext();
        mDialog = ((OnlineFert) getActivity()).getmDialog();

        et_time1 = view.findViewById(R.id.et_onl_time1);
        et_time2 = view.findViewById(R.id.et_onl_time2);
        et_time3 = view.findViewById(R.id.et_onl_time3);
        et_time4 = view.findViewById(R.id.et_onl_time4);
        et_time5 = view.findViewById(R.id.et_onl_time5);
        et_time6 = view.findViewById(R.id.et_onl_time6);
        et_ec = view.findViewById(R.id.et_onl_ec);
        et_ph = view.findViewById(R.id.et_onl_ph);
        et_yxsc = view.findViewById(R.id.et_onl_yxsc);
        tv_terminal = view.findViewById(R.id.tv_onl_terminal);
        tv_parameter = view.findViewById(R.id.tv_onl_parameter);
        tv_huishui = view.findViewById(R.id.tv_onl_huishui);
        tv_huishuizhi = view.findViewById(R.id.tv_onl_huishuizhi);
        tv_cyg1 = view.findViewById(R.id.tv_onl_cyg1);
        tv_cyg2 = view.findViewById(R.id.tv_onl_cyg2);
        tv_cyg3 = view.findViewById(R.id.tv_onl_cyg3);
        tv_cyg4 = view.findViewById(R.id.tv_onl_cyg4);
        tv_qsg = view.findViewById(R.id.tv_onl_qsg);
        tv_suan = view.findViewById(R.id.tv_onl_suan);
        tv_suan2 = view.findViewById(R.id.tv_onl_suan2);
        tv_a = view.findViewById(R.id.tv_onl_a);
        tv_a2 = view.findViewById(R.id.tv_onl_a2);
        tv_b = view.findViewById(R.id.tv_onl_b);
        tv_b2 = view.findViewById(R.id.tv_onl_b2);
        tv_c = view.findViewById(R.id.tv_onl_c);
        tv_c2 = view.findViewById(R.id.tv_onl_c2);
        tv_d = view.findViewById(R.id.tv_onl_d);
        tv_d2 = view.findViewById(R.id.tv_onl_d2);
        tv_ec1 = view.findViewById(R.id.tv_onl_ec1);
        tv_ec2 = view.findViewById(R.id.tv_onl_ec2);
        tv_gd = view.findViewById(R.id.tv_onl_gd);
        tv_water = view.findViewById(R.id.tv_onl_water);
        tv_abnormal = view.findViewById(R.id.tv_onl_abnormal);
        tv_ph = view.findViewById(R.id.tv_onl_ph);
        tv_ls = view.findViewById(R.id.tv_onl_ls);
        tv_irr1 = view.findViewById(R.id.tv_onl_irr1);
        tv_irr2 = view.findViewById(R.id.tv_onl_irr2);
        tv_irr3 = view.findViewById(R.id.tv_onl_irr3);
        tv_irr4 = view.findViewById(R.id.tv_onl_irr4);
        tv_irr5 = view.findViewById(R.id.tv_onl_irr5);
        tv_irr6 = view.findViewById(R.id.tv_onl_irr6);
        tv_irr7 = view.findViewById(R.id.tv_onl_irr7);
        tv_irr8 = view.findViewById(R.id.tv_onl_irr8);
        tv_irr9 = view.findViewById(R.id.tv_onl_irr9);
        tv_irr10 = view.findViewById(R.id.tv_onl_irr10);
        tv_irr11 = view.findViewById(R.id.tv_onl_irr11);
        tv_irr12 = view.findViewById(R.id.tv_onl_irr12);
        tv_irr13 = view.findViewById(R.id.tv_onl_irr13);
        tv_irr14 = view.findViewById(R.id.tv_onl_irr14);
        tv_irr15 = view.findViewById(R.id.tv_onl_irr15);
        tv_irr16 = view.findViewById(R.id.tv_onl_irr16);
        tv_irr17 = view.findViewById(R.id.tv_onl_irr17);
        tv_irr18 = view.findViewById(R.id.tv_onl_irr18);
        tv_irr19 = view.findViewById(R.id.tv_onl_irr19);
        tv_irr20 = view.findViewById(R.id.tv_onl_irr20);
        tv_irr21 = view.findViewById(R.id.tv_onl_irr21);
        tv_irr22 = view.findViewById(R.id.tv_onl_irr22);
        tv_irr23 = view.findViewById(R.id.tv_onl_irr23);
        tv_irr24 = view.findViewById(R.id.tv_onl_irr24);
        tv_irr25 = view.findViewById(R.id.tv_onl_irr25);
        tv_irr26 = view.findViewById(R.id.tv_onl_irr26);
        tv_irr27 = view.findViewById(R.id.tv_onl_irr27);
        tv_irr28 = view.findViewById(R.id.tv_onl_irr28);
        tv_irr29 = view.findViewById(R.id.tv_onl_irr29);
        tv_irr30 = view.findViewById(R.id.tv_onl_irr30);
        tv_irr31 = view.findViewById(R.id.tv_onl_irr31);
        tv_irr32 = view.findViewById(R.id.tv_onl_irr32);
        tv_tw = view.findViewById(R.id.tv_onlfert_tw);
        tv_tw2 = view.findViewById(R.id.tv_onlfert_tw2);
        tv_xf = view.findViewById(R.id.tv_onlfert_xf);
        tv_fm = view.findViewById(R.id.tv_onlfert_fm);
        tv_start = view.findViewById(R.id.tv_onl_start);
        tv_zyg = view.findViewById(R.id.tv_onl_zyg);
        tv_monitor = view.findViewById(R.id.tv_onl_monitor);
        tv_yxsc = view.findViewById(R.id.tv_onl_yxsc);
        tv_xf2 = view.findViewById(R.id.tv_onl_xf2);
        tv_handA = view.findViewById(R.id.tv_onl_handA);
        tv_zd1 = view.findViewById(R.id.tv_onl_zd1);
        tv_zd2 = view.findViewById(R.id.tv_onl_zd2);
        iv_huishui = view.findViewById(R.id.iv_onl_huishui);
        iv_huishui2 = view.findViewById(R.id.iv_onl_huishui2);
        iv_cyg1 = view.findViewById(R.id.iv_onl_cyg1);
        iv_cyg2 = view.findViewById(R.id.iv_onl_cyg2);
        iv_cyg3 = view.findViewById(R.id.iv_onl_cyg3);
        iv_cyg4 = view.findViewById(R.id.iv_onl_cyg4);
        iv_cyg5 = view.findViewById(R.id.iv_onl_cyg5);
        iv_cyg6 = view.findViewById(R.id.iv_onl_cyg6);
        iv_cyg7 = view.findViewById(R.id.iv_onl_cyg7);
        iv_cyg8 = view.findViewById(R.id.iv_onl_cyg8);
        iv_qsg = view.findViewById(R.id.iv_onl_qsg);
        iv_qsg1 = view.findViewById(R.id.iv_onl_qsg1);
        iv_suan = view.findViewById(R.id.iv_onl_suan);
        iv_a = view.findViewById(R.id.iv_onl_a);
        iv_b = view.findViewById(R.id.iv_onl_b);
        iv_c = view.findViewById(R.id.iv_onl_c);
        iv_d = view.findViewById(R.id.iv_onl_d);
        iv_sl = view.findViewById(R.id.iv_onl_sl);
        iv_irr1 = view.findViewById(R.id.iv_onl_irr1);
        iv_irr2 = view.findViewById(R.id.iv_onl_irr2);
        iv_irr3 = view.findViewById(R.id.iv_onl_irr3);
        iv_irr4 = view.findViewById(R.id.iv_onl_irr4);
        iv_irr5 = view.findViewById(R.id.iv_onl_irr5);
        iv_irr6 = view.findViewById(R.id.iv_onl_irr6);
        iv_irr7 = view.findViewById(R.id.iv_onl_irr7);
        iv_irr8 = view.findViewById(R.id.iv_onl_irr8);
        iv_irr9 = view.findViewById(R.id.iv_onl_irr9);
        iv_irr10 = view.findViewById(R.id.iv_onl_irr10);
        iv_irr11 = view.findViewById(R.id.iv_onl_irr11);
        iv_irr12 = view.findViewById(R.id.iv_onl_irr12);
        iv_irr13 = view.findViewById(R.id.iv_onl_irr13);
        iv_irr14 = view.findViewById(R.id.iv_onl_irr14);
        iv_irr15 = view.findViewById(R.id.iv_onl_irr15);
        iv_irr16 = view.findViewById(R.id.iv_onl_irr16);
        iv_irr17 = view.findViewById(R.id.iv_onl_irr17);
        iv_irr18 = view.findViewById(R.id.iv_onl_irr18);
        iv_irr19 = view.findViewById(R.id.iv_onl_irr19);
        iv_irr20 = view.findViewById(R.id.iv_onl_irr20);
        iv_irr21 = view.findViewById(R.id.iv_onl_irr21);
        iv_irr22 = view.findViewById(R.id.iv_onl_irr22);
        iv_irr23 = view.findViewById(R.id.iv_onl_irr23);
        iv_irr24 = view.findViewById(R.id.iv_onl_irr24);
        iv_irr25 = view.findViewById(R.id.iv_onl_irr25);
        iv_irr26 = view.findViewById(R.id.iv_onl_irr26);
        iv_irr27 = view.findViewById(R.id.iv_onl_irr27);
        iv_irr28 = view.findViewById(R.id.iv_onl_irr28);
        iv_irr29 = view.findViewById(R.id.iv_onl_irr29);
        iv_irr30 = view.findViewById(R.id.iv_onl_irr30);
        iv_irr31 = view.findViewById(R.id.iv_onl_irr31);
        iv_irr32 = view.findViewById(R.id.iv_onl_irr32);
        view_huishui = view.findViewById(R.id.view_onl_huishui);
        view_huishui1 = view.findViewById(R.id.view_onl_huishui1);
        view_huishui2 = view.findViewById(R.id.view_onl_huishui2);
        rl_manual = view.findViewById(R.id.rl_onlfert_manual);
        rl_storage = view.findViewById(R.id.rl_onlfert_storage);
        rl_cyg1 = view.findViewById(R.id.rl_onl_cyg1);
        rl_cyg2 = view.findViewById(R.id.rl_onl_cyg2);
        rl_cyg3 = view.findViewById(R.id.rl_onl_cyg3);
        rl_cyg4 = view.findViewById(R.id.rl_onl_cyg4);
        rl_handA1 = view.findViewById(R.id.rl_onl_handAutomatic1);
        rl_handA2 = view.findViewById(R.id.rl_onl_handAutomatic2);
        ll_fert1 = view.findViewById(R.id.ll_onl_fert1);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View contentView = LayoutInflater.from(context).inflate(R.layout.onl_irrlayout, null);
        RecyclerView mRecyclerView = contentView.findViewById(R.id.rv_onlirr);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 8));
        mAdapter = new OnlIrrAdapter(context);
        mRecyclerView.setAdapter(mAdapter);
        builder.setTitle(getString(R.string.onl_fert54));
        builder.setView(contentView);
        builder.setPositiveButton(getString(R.string.personal_str6), null);
        irrDialog = builder.create();


        mDownTimer = new CountDownTimer(5000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mDialog.dismiss();
                Toasts.showError(context, getString(R.string.onl_fert191));
            }
        };

        mDownTimer1 = new CountDownTimer(4000, 4000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                isRequest = true;
            }
        };

        task = new TimerTask() {
            @Override
            public void run() {
                if (isRequest) {
                    isRequest = false;
                    getFertilizerState();
                }
            }
        };
        timer = new Timer();


        FerCheck = new boolean[5];
        FerCheck2 = new boolean[5];

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        View contentView1 = LayoutInflater.from(context).inflate(R.layout.irrlayout, null);
        RecyclerView mRecyclerView1 = contentView1.findViewById(R.id.irr);
        mRecyclerView1.setLayoutManager(new GridLayoutManager(context, 5));
        final String[] FerStr = new String[]{getString(R.string.fert_str19), "A", "B", "C", "D"};
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
                    tv_xf.setText(str.substring(0, str.length() - 1));
                } else tv_xf.setText("");
            }
        });
        feradapter.setFertChshow(new boolean[5]);
        mRecyclerView1.setAdapter(feradapter);
        builder1.setTitle(getString(R.string.fert_str18));
        builder1.setView(contentView1);
        builder1.setPositiveButton(getString(R.string.personal_str6), null);

        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
        View contentView2 = LayoutInflater.from(context).inflate(R.layout.irrlayout, null);
        RecyclerView mRecyclerView2 = contentView2.findViewById(R.id.irr);
        mRecyclerView2.setLayoutManager(new GridLayoutManager(context, 5));
        feradapter2 = new FerAdapter(FerCheck2, context);
        feradapter2.setCallback(new MyCallback() {
            @Override
            public void checkBox(int position, boolean b) {
                FerCheck2[position] = b;
                String str = "";
                for (int i = 0; i < FerCheck2.length; i++) {
                    if (FerCheck2[i]) {
                        str = str + FerStr[i] + "、";
                    }
                }
                if (str.length() > 0) {
                    tv_xf2.setText(str.substring(0, str.length() - 1));
                } else tv_xf2.setText("");
            }
        });
        feradapter2.setFertChshow(new boolean[5]);
        mRecyclerView2.setAdapter(feradapter2);
        builder2.setTitle(getString(R.string.fert_str18));
        builder2.setView(contentView2);
        builder2.setPositiveButton(getString(R.string.personal_str6), null);

        FerDialog = builder1.create();
        FerDialog2 = builder2.create();


        AlertDialog.Builder builder3 = new AlertDialog.Builder(context);
        View contentView3 = LayoutInflater.from(context).inflate(R.layout.irrlayout, null);
        RecyclerView mRecyclerView3 = contentView3.findViewById(R.id.irr);
        mRecyclerView3.setLayoutManager(new GridLayoutManager(context, 5));
        irrNum = 0;
        irradapter = new IrrOnlAdapter();
        irradapter.setCallback(new MyCallback() {
            @Override
            public void checkBox(int position, boolean b) {
                if (b) {
                    irrNum++;
                } else {
                    irrNum--;
                }
                tv_fm.setText(getString(R.string.fert_str44) + irrNum + getString(R.string.fert_str45));
            }
        });
        irradapter.setValvesNum(0);
        mRecyclerView3.setAdapter(irradapter);
        builder3.setTitle(getString(R.string.fert_str17));
        builder3.setView(contentView3);
        builder3.setPositiveButton(getString(R.string.personal_str6), null);
        IrrDialog = builder3.create();


        view.findViewById(R.id.ll_onl_terminal).setOnClickListener(this);
        view.findViewById(R.id.ll_onl_fert).setOnClickListener(this);
        view.findViewById(R.id.ll_onl_stor1).setOnClickListener(this);
        view.findViewById(R.id.ll_onl_stor2).setOnClickListener(this);
        view.findViewById(R.id.tv_onl_valve).setOnClickListener(this);
        view.findViewById(R.id.ll_onlfert_kz).setOnClickListener(this);
        view.findViewById(R.id.ll_onl_handAutomatic).setOnClickListener(this);
        view.findViewById(R.id.tv_onl_set).setOnClickListener(this);
        tv_start.setOnClickListener(this);
        tv_xf.setOnClickListener(this);
        tv_xf2.setOnClickListener(this);
        tv_fm.setOnClickListener(this);

        timer.schedule(task, 0, 4000);

//        if (isVisible && !isInit) {
//            isInit = true;
//            initData();
//        }
    }

    /**
     * 初始化数据，页面关闭之前只会执行一次
     */
    private void initData() {
        register();
        if (pGhList != null && pGhList.size() > 0) {
            mPosition = 0;
            tv_terminal.setText(pGhList.get(mPosition)[1]);
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
                case 2:
                    parseResult2(message.getmResult());
                    break;
                default:
                    mDialog.dismiss();
                    break;
            }
        } else {
            mDialog.dismiss();
            Toasts.showError(context, message.getmResult());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SocketResult result) {
        mDownTimer.cancel();
        mDialog.dismiss();
        if (result.isOK()) {
            switch (result.getLabel()) {
                case 715:
                    fertRun = 1;
                    tv_start.setText(getString(R.string.fert_str20));
                    Toasts.showSuccess(context, getString(R.string.fert_str6));
                    break;
                case 725:
                    storageRun = 1;
                    tv_start.setText(getString(R.string.fert_str20));
                    Toasts.showSuccess(context, getString(R.string.fert_str6));
                    break;
                case 717:
                    fertRun = 0;
                    tv_start.setText(getString(R.string.fert_str16));
                    Toasts.showInfo(context, getString(R.string.fert_str7));
                    break;
                case 727:
                    storageRun = 0;
                    tv_start.setText(getString(R.string.fert_str16));
                    Toasts.showInfo(context, getString(R.string.fert_str7));
                    break;
                case 713:
                    Toasts.showSuccess(context, getString(R.string.onl_fert53));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_onl_fert:
                showBottomSheet3();
                break;
            case R.id.ll_onl_terminal:
                showBottomSheet1();
                break;
            case R.id.ll_onlfert_kz:
                showBottomSheet2();
                break;
            case R.id.tv_onl_valve:
                irrDialog.show();
                break;
            case R.id.tv_onlfert_xf:
                if (FerDialog != null)
                    FerDialog.show();
                break;
            case R.id.tv_onlfert_fm:
                if (IrrDialog != null)
                    IrrDialog.show();
                break;
            case R.id.tv_onl_start:
                if (pGhList.get(mPosition)[6].equals("0")) {
                    Toasts.showInfo(context, getString(R.string.onl_fert192));
                    return;
                }
                if (AutoObject == 0) {
                    if (fertRun == 1) {
                        mDownTimer.cancel();
                        mDownTimer.start();
                        mDialog.show();
                        socketRequest(716, null);
                        restartTime();
                    } else requestBefore();
                } else {
                    if (storageRun == 1) {
                        mDownTimer.cancel();
                        mDownTimer.start();
                        mDialog.show();
                        socketRequest(726, null);
                        restartTime();
                    } else requestBefore1();
                }
                break;
            case R.id.ll_onl_stor1:
                showBottomSheet4();
                break;
            case R.id.ll_onl_stor2:
                if (hasTanks != null && hasTanks.size() > storageTankNum) {
                    if (hasTanks.get(storageTankNum)[1] != null) {
                        if (StartCondition == 0) {
                            StartCondition = 1;
                            tv_monitor.setText(getString(R.string.onl_fert34));
                            if (hasTanks.get(storageTankNum)[2] != null) {
                                tv_yxsc.setText(getString(R.string.onl_fert35));
                                et_yxsc.setText("");
                                et_yxsc.setVisibility(View.GONE);
                            } else {
                                tv_yxsc.setText(getString(R.string.onl_fert36));
                                et_yxsc.setVisibility(View.VISIBLE);
                            }
                        } else {
                            StartCondition = 0;
                            tv_monitor.setText(getString(R.string.onl_fert32));
                            tv_yxsc.setText(getString(R.string.onl_fert15));
                            et_yxsc.setVisibility(View.VISIBLE);
                        }
                    }
                }
                break;
            case R.id.tv_onl_xf2:
                if (FerDialog2 != null)
                    FerDialog2.show();
                break;
            case R.id.ll_onl_handAutomatic:
                if (pGhList.get(mPosition)[6].equals("0")) {
                    Toasts.showInfo(context, getString(R.string.onl_fert192));
                    return;
                }
                mDownTimer.cancel();
                mDownTimer.start();
                mDialog.show();
                if (runMode == 1) {
                    socketRequest(712, 0);
                } else {
                    socketRequest(712, 1);
                }
                break;
            case R.id.tv_onl_set:
                Intent intent = new Intent(context, OnlFertConfig.class);
                startActivity(intent);
                break;
            default:

                break;
        }
    }

    private void requestBefore1() {
        try {
            if (et_ec.getText().length() == 0 || et_ph.getText().length() == 0) {
                Toasts.showInfo(context, getString(R.string.request_error6));
                return;
            } else {
                float ec = Float.parseFloat(et_ec.getText().toString());
                if (ec < 0 || ec > 10) {
                    Toasts.showInfo(context, getString(R.string.onl_fert193));
                    return;
                }

                float ph = Float.parseFloat(et_ph.getText().toString());
                if (ph < 0 || ph > 14) {
                    Toasts.showInfo(context, getString(R.string.onl_fert193));
                    return;
                }
            }
            if (StartCondition == 0 && et_yxsc.getText().length() == 0) {
                Toasts.showInfo(context, getString(R.string.request_error6));
                return;
            }

            OnlAutoCtlParamter paramter = new OnlAutoCtlParamter();

            paramter.setAutoObject(AutoObject);
            paramter.setStartCondition(StartCondition);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            Calendar calendar = Calendar.getInstance();
            String dateName = df.format(calendar.getTime());
            paramter.setStartTime(dateName);
            int stopTime = 0;
            if (et_yxsc.length() > 0) {
                stopTime = Integer.parseInt(et_yxsc.getText().toString());
                stopTime = stopTime * 1000;
            }
            Date date = new Date();
            date.setTime(calendar.getTimeInMillis() + stopTime);
            String dateName2 = df.format(date);
            paramter.setStopTime(dateName2);
            paramter.setStorageTankNum(storageTankNum);
            short highLevel = 0;
            if (et_yxsc.length() > 0) {
                highLevel = Short.parseShort(et_yxsc.getText().toString());
            }
            paramter.setHighLevel(highLevel);
            float ECSetValue = 0;
            if (et_ec.length() > 0) {
                ECSetValue = Float.parseFloat(et_ec.getText().toString());
            }
            paramter.setECSetValue(ECSetValue);
            float PHSetValue = 0;
            if (et_ph.length() > 0) {
                PHSetValue = Float.parseFloat(et_ph.getText().toString());
            }
            paramter.setPHSetValue(PHSetValue);

            byte FertCh = 0;
            for (int i = 4; i >= 0; i--) {
                FertCh = (byte) (FertCh << 1);
                if (feradapter2.getCheck()[i]) FertCh += 1;
            }
            paramter.setFertCh(FertCh);

            paramter.setFertCtlMode((byte) config.getData().getRunParameter().getFertCtlMode());
            paramter.setProportion_A((float) config.getData().getRunParameter().getProportion_A());
            paramter.setProportion_B((float) config.getData().getRunParameter().getProportion_B());
            paramter.setProportion_C((float) config.getData().getRunParameter().getProportion_C());
            paramter.setProportion_D((float) config.getData().getRunParameter().getProportion_D());

            mDownTimer.cancel();
            mDownTimer.start();
            mDialog.show();
            socketRequest(724, paramter);
            restartTime();

        } catch (Exception e) {
            mDownTimer.cancel();
            mDialog.dismiss();
            Log.d(TAG, "requestBefore1: " + e.getMessage());
        }

    }

    private void requestBefore() {
        if (et_time1.getText().length() > 0 && et_time2.getText().length() > 0 &&
                et_time3.getText().length() > 0 && et_time4.getText().length() > 0 &&
                tv_fm.getText().length() > 0) {
//            if (hasTanks != null && hasTanks.size() > 0) {
//
//            } else {
//
//
//            }
        } else {
            Toasts.showInfo(context, getString(R.string.request_error6));
            return;
        }
        try {
            OnlAutoCtlParamter paramter = new OnlAutoCtlParamter();

            paramter.setManualCtrMode(manualCtrMode);
            paramter.setAutoObject(AutoObject);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            Calendar calendar = Calendar.getInstance();
            String dateName = df.format(calendar.getTime());
            paramter.setStartTime(dateName);
            int stopTime = 0;
            if (et_time1.length() > 0) {
                stopTime = Integer.parseInt(et_time1.getText().toString());
                stopTime = stopTime * 1000;
            }
            Date date = new Date();
            date.setTime(calendar.getTimeInMillis() + stopTime);
            String dateName2 = df.format(date);
            paramter.setStopTime(dateName2);
            short runTime = 0;
            if (et_time2.length() > 0) {
                runTime = Short.parseShort(et_time2.getText().toString());
            }
            paramter.setRunTime(runTime);
            short pauseTime = 0;
            if (et_time3.length() > 0) {
                pauseTime = Short.parseShort(et_time3.getText().toString());
            }
            paramter.setPauseTime(pauseTime);
            byte maxValveNum = 0;
            if (et_time4.length() > 0) {
                maxValveNum = Byte.parseByte(et_time4.getText().toString());
            }
            paramter.setMaxValveNum(maxValveNum);
            float ECSetValue = 0;
            if (et_time5.length() > 0) {
                ECSetValue = Float.parseFloat(et_time5.getText().toString());
            }
            paramter.setECSetValue(ECSetValue);
            float PHSetValue = 0;
            if (et_time6.length() > 0) {
                PHSetValue = Float.parseFloat(et_time6.getText().toString());
            }
            paramter.setPHSetValue(PHSetValue);
            byte FertCh = 0;
            for (int i = 4; i >= 0; i--) {
                FertCh = (byte) (FertCh << 1);
                if (feradapter.getCheck()[i]) FertCh += 1;
            }
            paramter.setFertCh(FertCh);

            long IrrValvesL = 0;
            for (int i = 63; i >= 0; i--) {
                IrrValvesL = IrrValvesL << 1;
                if (irradapter.getCheck()[i]) IrrValvesL += 1;
            }
            paramter.setIrrValvesL(IrrValvesL);

            long IrrValvesH = 0;
            for (int i = 127; i >= 64; i--) {
                IrrValvesH = IrrValvesH << 1;
                if (irradapter.getCheck()[i]) IrrValvesH += 1;
            }
            paramter.setIrrValvesH(IrrValvesH);

            paramter.setWaterLimit(Float.parseFloat(et_time1.getText().toString()));

            paramter.setFertCtlMode((byte) config.getData().getRunParameter().getFertCtlMode());
            paramter.setProportion_A((float) config.getData().getRunParameter().getProportion_A());
            paramter.setProportion_B((float) config.getData().getRunParameter().getProportion_B());
            paramter.setProportion_C((float) config.getData().getRunParameter().getProportion_C());
            paramter.setProportion_D((float) config.getData().getRunParameter().getProportion_D());

            mDownTimer.cancel();
            mDownTimer.start();
            mDialog.show();
            socketRequest(714, paramter);
            restartTime();

        } catch (Exception e) {
            mDialog.dismiss();
        }

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
            mDownTimer.cancel();
            mDialog.dismiss();
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
            if (config.isSuccess()) {
                //是否显示清水罐泵
                if (config.getData().getWList().get(0).getHasPump() == 1) {
                    iv_qsg.setVisibility(View.VISIBLE);
                } else iv_qsg.setVisibility(View.GONE);

                //是否显示主机泵
                if (config.getData().getBaseInfo().getHasInPump() == 1) {
                    iv_qsg1.setVisibility(View.VISIBLE);
                } else iv_qsg1.setVisibility(View.GONE);

                //是否显示回水罐
                if (config.getData().getWList().get(6).getHasTank() == 1) {
                    tv_huishui.setVisibility(View.VISIBLE);
                    //是否显示回水罐里的文字
                    if (config.getData().getWList().get(6).getHasLevel() == 1) {
                        tv_huishuizhi.setVisibility(View.VISIBLE);
                    } else tv_huishuizhi.setText("");
                    //是否显示回水罐的红绿点
                    if (config.getData().getWList().get(6).getHasPump() == 1) {
                        iv_huishui.setVisibility(View.VISIBLE);
                    } else iv_huishui.setVisibility(View.GONE);

                    iv_huishui2.setVisibility(View.VISIBLE);
                    view_huishui.setVisibility(View.VISIBLE);
                    view_huishui1.setVisibility(View.VISIBLE);
                    view_huishui2.setVisibility(View.VISIBLE);
                } else {
                    tv_huishui.setVisibility(View.GONE);
                    tv_huishuizhi.setVisibility(View.GONE);
                    iv_huishui.setVisibility(View.GONE);
                    iv_huishui2.setVisibility(View.GONE);
                    view_huishui.setVisibility(View.GONE);
                    view_huishui1.setVisibility(View.GONE);
                    view_huishui2.setVisibility(View.GONE);
                }


                if (config.getData().getWList().get(1).getHasLevel() == 1) {
                    tv_suan.setVisibility(View.VISIBLE);
                } else
                    tv_suan.setVisibility(View.INVISIBLE);

                if (config.getData().getWList().get(2).getHasLevel() == 1) {
                    tv_a.setVisibility(View.VISIBLE);
                } else
                    tv_a.setVisibility(View.INVISIBLE);


                if (config.getData().getWList().get(3).getHasLevel() == 1) {
                    tv_b.setVisibility(View.VISIBLE);
                } else
                    tv_b.setVisibility(View.INVISIBLE);


                if (config.getData().getWList().get(4).getHasLevel() == 1) {
                    tv_c.setVisibility(View.VISIBLE);
                } else
                    tv_c.setVisibility(View.INVISIBLE);


                if (config.getData().getWList().get(5).getHasLevel() == 1) {
                    tv_d.setVisibility(View.VISIBLE);
                } else
                    tv_d.setVisibility(View.INVISIBLE);


                hasTanks = new ArrayList<>();
                show1(7, rl_cyg1, tv_cyg1, iv_cyg1, iv_cyg2);
                show1(8, rl_cyg2, tv_cyg2, iv_cyg3, iv_cyg4);
                show1(9, rl_cyg3, tv_cyg3, iv_cyg5, iv_cyg6);
                show1(10, rl_cyg4, tv_cyg4, iv_cyg7, iv_cyg8);

                if (hasTanks.size() > 0) {
                    tv_xf.setEnabled(false);
                    et_time5.setEnabled(false);
                    et_time6.setEnabled(false);
                } else {
                    tv_xf.setEnabled(true);
                    et_time5.setEnabled(true);
                    et_time6.setEnabled(true);
                    et_time5.setText("1.5");
                    et_time6.setText("7");
                }


                fertChshow = new boolean[5];
                for (int i = 0; i < 5; i++) {
                    if (config.getData().getWList().get(i + 1).getHasTank() == 1)
                        fertChshow[i] = true;
                }
                FerCheck = new boolean[5];
                FerCheck2 = new boolean[5];

                tv_xf.setText("");
                tv_xf2.setText("");
                feradapter.setCheck(FerCheck);
                feradapter.setFertChshow(fertChshow);
                feradapter2.setCheck(FerCheck2);
                feradapter2.setFertChshow(fertChshow);
                feradapter.notifyDataSetChanged();
                feradapter2.notifyDataSetChanged();

                tv_fm.setText("");
                irrNum = 0;
                irradapter.setCheck(new boolean[128]);
                irradapter.setValvesNum(config.getData().getBaseInfo().getValveNum());
                irradapter.notifyDataSetChanged();


                //判断有没有储液罐
                if (hasTanks.size() == 0) {
                    ll_fert1.setVisibility(View.GONE);
                } else {
                    ll_fert1.setVisibility(View.VISIBLE);
                }
                getFertilizerState();
            } else {
                mDialog.dismiss();
            }
        } catch (Exception e) {
            mDialog.dismiss();
        }
    }

    private void show1(int index, View view, TextView tv, ImageView iv1, ImageView iv2) {
        //是否显示
        if (config.getData().getWList().get(index).getHasTank() == 1) {
            hasTanks.add(new String[]{getString(R.string.onl_fert37) + (index - 6), null, null});
            view.setVisibility(View.VISIBLE);
            //是否显示储液罐的文字
            if (config.getData().getWList().get(index).getHasLevel() == 1) {
                hasTanks.get(hasTanks.size() - 1)[1] = "";
                tv.setVisibility(View.VISIBLE);
                if (config.getData().getWList().get(index).getLevelType() == 0) {
                    hasTanks.get(hasTanks.size() - 1)[2] = "";
                }
            } else tv.setVisibility(View.INVISIBLE);
            //是否显示储液罐的第一个红绿点
            if (config.getData().getWList().get(index).getHasValve() == 1) {
                iv1.setVisibility(View.VISIBLE);
            } else iv1.setVisibility(View.GONE);
            //是否显示储液罐的第二个红绿点
            if (config.getData().getWList().get(index).getHasPump() == 1) {
                iv2.setVisibility(View.VISIBLE);
            } else iv2.setVisibility(View.GONE);

        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void show2(double fertDuty, TextView tv, ImageView iv) {
        tv.setText(fertDuty + "%");
        if (fertDuty <= 1) {
            iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
        } else
            iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
    }

    /**
     * 解析施肥机状态并且更新
     *
     * @param s
     */
    private void parseResult2(String s) {
        if (config == null) {
            mDownTimer.cancel();
            mDialog.dismiss();
            isRequest = false;
            return;
        }
        try {
            ControlStatus status = new Gson().fromJson(s, ControlStatus.class);
            if (status.isSuccess()) {
                //酸
                show2(status.getData().getFertAcid_Duty(), tv_suan2, iv_suan);
                //A
                show2(status.getData().getFertABCD_Duty().get(0), tv_a2, iv_a);
                //B
                show2(status.getData().getFertABCD_Duty().get(1), tv_b2, iv_b);
                //C
                show2(status.getData().getFertABCD_Duty().get(2), tv_c2, iv_c);
                //D
                show2(status.getData().getFertABCD_Duty().get(3), tv_d2, iv_d);

                String ec1 = new BigDecimal(status.getData().getEC_Pre())
                        .setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
                tv_ec1.setText(ec1);
                if ((status.getData().getSwitchLevel() & 1) != 0) {
                    tv_gd.setText("low");
                } else tv_gd.setText("high");
                tv_water.setText(getString(R.string.onl_fert5) + "\n" + status.getData().getWaterAmount());
//                if (status.getData().getErrorFlag() == 0) {
//                    tv_abnormal.setText(getString(R.string.onl_fert6) + "\n" + getString(R.string.onl_fert38));
//                } else
//                    tv_abnormal.setText(getString(R.string.onl_fert6) + "\n" + status.getData().getErrorFlag());
                switch (status.getData().getErrorFlag()) {
                    case 0:
                        tv_abnormal.setText(R.string.onl_fert195);
                        break;
                    case 1:
                        tv_abnormal.setText(R.string.onl_fert196);
                        break;
                    case 2:
                        tv_abnormal.setText(R.string.onl_fert197);
                        break;
                    case 3:
                        tv_abnormal.setText(R.string.onl_fert198);
                        break;
                    case 4:
                        tv_abnormal.setText(R.string.onl_fert199);
                        break;
                    case 5:
                        tv_abnormal.setText(R.string.onl_fert200);
                        break;
                    case 6:
                        tv_abnormal.setText(R.string.onl_fert201);
                        break;
                    case 7:
                        tv_abnormal.setText(R.string.onl_fert202);
                        break;
                    case 8:
                        tv_abnormal.setText(R.string.onl_fert203);
                        break;
                    case 9:
                        tv_abnormal.setText(R.string.onl_fert204);
                        break;
                    case 10:
                        tv_abnormal.setText(R.string.onl_fert205);
                        break;
                    case 11:
                        tv_abnormal.setText(R.string.onl_fert206);
                        break;
                    case 12:
                        tv_abnormal.setText(R.string.onl_fert207);
                        break;
                    case 13:
                        tv_abnormal.setText(R.string.onl_fert208);
                        break;
                    case 14:
                        tv_abnormal.setText(R.string.onl_fert209);
                        break;
                    case 15:
                        tv_abnormal.setText(R.string.onl_fert210);
                        break;
                    case 16:
                        tv_abnormal.setText(R.string.onl_fert211);
                        break;
                    case 17:
                        tv_abnormal.setText(R.string.onl_fert212);
                        break;
                    case 18:
                        tv_abnormal.setText(R.string.onl_fert213);
                        break;
                    case 19:
                        tv_abnormal.setText(R.string.onl_fert214);
                        break;
                    case 20:
                        tv_abnormal.setText(R.string.onl_fert215);
                        break;
                    case 21:
                        tv_abnormal.setText(R.string.onl_fert216);
                        break;
                    case 22:
                        tv_abnormal.setText(R.string.onl_fert217);
                        break;
                    case 23:
                        tv_abnormal.setText(R.string.onl_fert218);
                        break;
                    default:
                        tv_abnormal.setText("");
                        break;
                }
                String ec2 = "EC=" + new BigDecimal(status.getData().getEC1())
                        .setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
                tv_ec2.setText(ec2);
                String ph = "PH=" + new BigDecimal(status.getData().getPH1())
                        .setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
                tv_ph.setText(ph);
                tv_ls.setText(status.getData().getFlowSpeed() + "\nL/min");


                if ((status.getData().getPumpStatus() & 1) != 0) {
                    iv_sl.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_sl.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));

                //是否有水泵
                if (config.getData().getBaseInfo().getHasInPump() == 1) {
                    String str = Integer.toBinaryString(status.getData().getPumpStatus());
                    str = new StringBuffer(str).reverse().toString();
                    int beginIndex = config.getData().getBaseInfo().getInPumpCh();
                    beginIndex++;
                    for (int i1 = str.length(); i1 < beginIndex + 1; i1++) {
                        str = str + "0";
                    }
                    if (str.substring(beginIndex, beginIndex + 1).equals("1")) {
                        iv_qsg1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                    } else {
                        iv_qsg1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                    }
                }

                for (int i = 0; i < 11; i++) {
                    //设置罐子里的文字
                    if (config.getData().getWList().get(i).getHasLevel() == 1) {
                        if (config.getData().getWList().get(i).getLevelType() == 1) {
                            String str = new BigDecimal(status.getData().getCurrentLevelVec().get(
                                    config.getData().getWList().get(i).getCurrentCh()
                            )).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() + "\ncm";
                            switch (i) {
                                case 0://清水罐
                                    tv_qsg.setText(str);
                                    break;
                                case 1://酸
                                    tv_suan.setText(str);
                                    break;
                                case 2:
                                    tv_a.setText(str);
                                    break;
                                case 3:
                                    tv_b.setText(str);
                                    break;
                                case 4:
                                    tv_c.setText(str);
                                    break;
                                case 5:
                                    tv_d.setText(str);
                                    break;
                                case 6://回水消毒罐
                                    tv_huishuizhi.setText(str);
                                    break;
                                case 7:
                                    tv_cyg1.setText(str);
                                    break;
                                case 8:
                                    tv_cyg2.setText(str);
                                    break;
                                case 9:
                                    tv_cyg3.setText(str);
                                    break;
                                case 10:
                                    tv_cyg4.setText(str);
                                    break;
                                case 11:
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            int SwitchLevel = status.getData().getSwitchLevel();
                            String str = Integer.toBinaryString(SwitchLevel);
                            str = new StringBuffer(str).reverse().toString();
                            int beginIndex = config.getData().getWList().get(i).getHighLevelCh();
                            if (beginIndex > 63) beginIndex = 63;
                            for (int i1 = str.length(); i1 < beginIndex + 1; i1++) {
                                str = str + "0";
                            }
                            if (str.substring(beginIndex, beginIndex + 1).equals("1")) {
                                switch (i) {
                                    case 0://清水罐
                                        tv_qsg.setText(getString(R.string.onl_fert40));
                                        break;
                                    case 1:
                                        tv_suan.setText(getString(R.string.onl_fert40));
                                        break;
                                    case 2:
                                        tv_a.setText(getString(R.string.onl_fert40));
                                        break;
                                    case 3:
                                        tv_b.setText(getString(R.string.onl_fert40));
                                        break;
                                    case 4:
                                        tv_c.setText(getString(R.string.onl_fert40));
                                        break;
                                    case 5:
                                        tv_d.setText(getString(R.string.onl_fert40));
                                        break;
                                    case 6://回水消毒罐
                                        tv_huishuizhi.setText(getString(R.string.onl_fert40));
                                        break;
                                    case 7:
                                        tv_cyg1.setText(getString(R.string.onl_fert40));
                                        break;
                                    case 8:
                                        tv_cyg2.setText(getString(R.string.onl_fert40));
                                        break;
                                    case 9:
                                        tv_cyg3.setText(getString(R.string.onl_fert40));
                                        break;
                                    case 10:
                                        tv_cyg4.setText(getString(R.string.onl_fert40));
                                        break;
                                    case 11:
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                String str1 = Integer.toBinaryString(status.getData().getSwitchLevel());
                                str1 = new StringBuffer(str1).reverse().toString();
                                int beginIndex1 = config.getData().getWList().get(i).getLowLevelCh();
                                if (beginIndex1 > 63) beginIndex1 = 63;
                                for (int i1 = str1.length(); i1 < beginIndex1 + 1; i1++) {
                                    str1 = str1 + "0";
                                }
                                if (str1.substring(beginIndex1, beginIndex1 + 1).equals("1")) {
                                    switch (i) {
                                        case 0://清水罐
                                            tv_qsg.setText(getString(R.string.onl_fert41));
                                            break;
                                        case 1:
                                            tv_suan.setText(getString(R.string.onl_fert41));
                                            break;
                                        case 2:
                                            tv_a.setText(getString(R.string.onl_fert41));
                                            break;
                                        case 3:
                                            tv_b.setText(getString(R.string.onl_fert41));
                                            break;
                                        case 4:
                                            tv_c.setText(getString(R.string.onl_fert41));
                                            break;
                                        case 5:
                                            tv_d.setText(getString(R.string.onl_fert41));
                                            break;
                                        case 6://回水消毒罐
                                            tv_huishuizhi.setText(getString(R.string.onl_fert41));
                                            break;
                                        case 7:
                                            tv_cyg1.setText(getString(R.string.onl_fert41));
                                            break;
                                        case 8:
                                            tv_cyg2.setText(getString(R.string.onl_fert41));
                                            break;
                                        case 9:
                                            tv_cyg3.setText(getString(R.string.onl_fert41));
                                            break;
                                        case 10:
                                            tv_cyg4.setText(getString(R.string.onl_fert41));
                                            break;
                                        case 11:
                                            break;
                                        default:
                                            break;
                                    }
                                } else {
                                    switch (i) {
                                        case 0://清水罐
                                            tv_qsg.setText(getString(R.string.onl_fert42));
                                            break;
                                        case 1:
                                            tv_suan.setText(getString(R.string.onl_fert42));
                                            break;
                                        case 2:
                                            tv_a.setText(getString(R.string.onl_fert42));
                                            break;
                                        case 3:
                                            tv_b.setText(getString(R.string.onl_fert42));
                                            break;
                                        case 4:
                                            tv_c.setText(getString(R.string.onl_fert42));
                                            break;
                                        case 5:
                                            tv_d.setText(getString(R.string.onl_fert42));
                                            break;
                                        case 6://回水消毒罐
                                            tv_huishuizhi.setText(getString(R.string.onl_fert42));
                                            break;
                                        case 7:
                                            tv_cyg1.setText(getString(R.string.onl_fert42));
                                            break;
                                        case 8:
                                            tv_cyg2.setText(getString(R.string.onl_fert42));
                                            break;
                                        case 9:
                                            tv_cyg3.setText(getString(R.string.onl_fert42));
                                            break;
                                        case 10:
                                            tv_cyg4.setText(getString(R.string.onl_fert42));
                                            break;
                                        case 11:
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                        }
                    }
                    //是否有水泵
                    if (config.getData().getWList().get(i).getHasPump() == 1) {
                        String str = Integer.toBinaryString(status.getData().getPumpStatus());
                        str = new StringBuffer(str).reverse().toString();
                        int beginIndex = config.getData().getWList().get(i).getPumpCh();
                        beginIndex++;
                        if (beginIndex > 63) beginIndex = 63;
                        for (int i1 = str.length(); i1 < beginIndex + 1; i1++) {
                            str = str + "0";
                        }
                        if (str.substring(beginIndex, beginIndex + 1).equals("1")) {
                            switch (i) {
                                case 0://清水罐
                                    iv_qsg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                                    break;
                                case 6://回水消毒罐
                                    iv_huishui.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                                    iv_huishui.setVisibility(View.VISIBLE);
                                    break;
                                case 7:
                                    iv_cyg2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                                    break;
                                case 8:
                                    iv_cyg4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                                    break;
                                case 9:
                                    iv_cyg6.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                                    break;
                                case 10:
                                    iv_cyg8.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            switch (i) {
                                case 0://清水罐
                                    iv_qsg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                                    break;
                                case 6://回水消毒罐
                                    iv_huishui.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                                    iv_huishui.setVisibility(View.VISIBLE);
                                    break;
                                case 7:
                                    iv_cyg2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                                    break;
                                case 8:
                                    iv_cyg4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                                    break;
                                case 9:
                                    iv_cyg6.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                                    break;
                                case 10:
                                    iv_cyg8.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                                    break;
                                default:
                                    break;
                            }
                        }
                    } else {
                        switch (i) {
                            case 6://回水消毒罐
                                iv_huishui.setVisibility(View.GONE);
                                break;
                            default:
                                break;
                        }
                    }

                    //是否有电磁阀
                    if (config.getData().getWList().get(i).getHasValve() == 1) {
                        String str = Integer.toBinaryString(status.getData().getStorageValveStatus());
                        str = new StringBuffer(str).reverse().toString();
                        int beginIndex = config.getData().getWList().get(i).getValveCh();
                        if (beginIndex > 63) beginIndex = 63;
                        for (int i1 = str.length(); i1 < beginIndex + 1; i1++) {
                            str = str + "0";
                        }
                        if (str.substring(beginIndex, beginIndex + 1).equals("1")) {
                            switch (i) {
                                case 7:
                                    iv_cyg1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                                    break;
                                case 8:
                                    iv_cyg3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                                    break;
                                case 9:
                                    iv_cyg5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                                    break;
                                case 10:
                                    iv_cyg7.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            switch (i) {
                                case 7:
                                    iv_cyg1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                                    break;
                                case 8:
                                    iv_cyg3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                                    break;
                                case 9:
                                    iv_cyg5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                                    break;
                                case 10:
                                    iv_cyg7.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }

                //设置32个红绿灯
                List<Integer> irrListL = new ArrayList<>();
                List<Integer> irrListH = new ArrayList<>();

                if (config.getData().getWList().get(7).getHasTank() == 1) {
                    for (int i = 0; i < 64; i++) {
                        if ((config.getData().getBaseInfo().getTankOneValveL() & 1l << i) != 0) {
                            irrListL.add(i);
                        }
                    }
                    for (int i = 0; i < 64; i++) {
                        if ((config.getData().getBaseInfo().getTankOneValveH() & 1l << i) != 0) {
                            irrListH.add(i);
                        }
                    }
                }
                if (config.getData().getWList().get(8).getHasTank() == 1) {
                    for (int i = 0; i < 64; i++) {
                        if ((config.getData().getBaseInfo().getTankTwoValveL() & 1l << i) != 0) {
                            irrListL.add(i);
                        }
                    }
                    for (int i = 0; i < 64; i++) {
                        if ((config.getData().getBaseInfo().getTankTwoValveH() & 1l << i) != 0) {
                            irrListH.add(i);
                        }
                    }
                }
                if (config.getData().getWList().get(9).getHasTank() == 1) {
                    for (int i = 0; i < 64; i++) {
                        if ((config.getData().getBaseInfo().getTankThreeValveL() & 1l << i) != 0) {
                            irrListL.add(i);
                        }
                    }
                    for (int i = 0; i < 64; i++) {
                        if ((config.getData().getBaseInfo().getTankThreeValveH() & 1l << i) != 0) {
                            irrListH.add(i);
                        }
                    }
                }
                if (config.getData().getWList().get(10).getHasTank() == 1) {
                    for (int i = 0; i < 64; i++) {
                        if ((config.getData().getBaseInfo().getTankFourValveL() & 1l << i) != 0) {
                            irrListL.add(i);
                        }
                    }
                    for (int i = 0; i < 64; i++) {
                        if ((config.getData().getBaseInfo().getTankFourValveH() & 1l << i) != 0) {
                            irrListH.add(i);
                        }
                    }
                }

                String irrLow = Long.toBinaryString(status.getData().getIrrValvesStatusL());
                irrLow = new StringBuffer(irrLow).reverse().toString();
                String irrHigh = Long.toBinaryString(status.getData().getIrrValvesStatusH());
                irrHigh = new StringBuffer(irrHigh).reverse().toString();
                for (int i = 0; i < 64; i++) {
                    if (irrLow.length() < 64) {
                        irrLow = irrLow + "0";
                    } else break;
                }
                for (int i = 0; i < 64; i++) {
                    if (irrHigh.length() < 64) {
                        irrHigh = irrHigh + "0";
                    } else break;
                }
                int amount;
                List<String[]> irrList = new ArrayList<>();

                if (irrListL.size() < 32) {
                    amount = irrListL.size();
                } else amount = 32;
                for (int i = 0; i < amount; i++) {
                    if (irrLow.substring(irrListL.get(i), irrListL.get(i) + 1).equals("1")) {
                        irrList.add(new String[]{(irrListL.get(i) + 1) + "#", "1"});
                    } else irrList.add(new String[]{(irrListL.get(i) + 1) + "#", "0"});
                }
                if (irrListH.size() < 32) {
                    amount = irrListH.size();
                } else amount = 32;
                for (int i = 0; i < amount; i++) {
                    if (irrHigh.substring(irrListH.get(i), irrListH.get(i) + 1).equals("1")) {
                        irrList.add(new String[]{(irrListH.get(i) + 1) + "#", "1"});
                    } else irrList.add(new String[]{(irrListH.get(i) + 1) + "#", "0"});
                }

                for (int i = 0; i < irrList.size(); i++) {
                    switch (i) {
                        case 0:
                            tv_irr1.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 1:
                            tv_irr2.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 2:
                            tv_irr3.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 3:
                            tv_irr4.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 4:
                            tv_irr5.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 5:
                            tv_irr6.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr6.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr6.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 6:
                            tv_irr7.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr7.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr7.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 7:
                            tv_irr8.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr8.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr8.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 8:
                            tv_irr9.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr9.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr9.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 9:
                            tv_irr10.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr10.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr10.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 10:
                            tv_irr11.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr11.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr11.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 11:
                            tv_irr12.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr12.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr12.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 12:
                            tv_irr13.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr13.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr13.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 13:
                            tv_irr14.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr14.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr14.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 14:
                            tv_irr15.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr15.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr15.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 15:
                            tv_irr16.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr16.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr16.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 16:
                            tv_irr17.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr17.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr17.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 17:
                            tv_irr18.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr18.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr18.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 18:
                            tv_irr19.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr19.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr19.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 19:
                            tv_irr20.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr20.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr20.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 20:
                            tv_irr21.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr21.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr21.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 21:
                            tv_irr22.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr22.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr22.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 22:
                            tv_irr23.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr23.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr23.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 23:
                            tv_irr24.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr24.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr24.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 24:
                            tv_irr25.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr25.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr25.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 25:
                            tv_irr26.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr26.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr26.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 26:
                            tv_irr27.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr27.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr27.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 27:
                            tv_irr28.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr28.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr28.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 28:
                            tv_irr29.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr29.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr29.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 29:
                            tv_irr30.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr30.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr30.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 30:
                            tv_irr31.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr31.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr31.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        case 31:
                            tv_irr32.setText(irrList.get(i)[0]);
                            if (irrList.get(i)[1].equals("1")) {
                                iv_irr32.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                            } else
                                iv_irr32.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                            break;
                        default:
                            break;
                    }
                }

                //没有储液罐的时候设置32个水泵
                if (irrList.size() == 0) {
                    for (int i = 0; i < 32; i++) {
                        if ((status.getData().getIrrValvesStatusL() & 1l << i) != 0) {
                            setIrr(true, i, (i + 1) + "#");
                        } else setIrr(false, i, (i + 1) + "#");
                    }
                }

                //设置所有阀门里的数据
                for (int i = 0; i < 64; i++) {
                    if ((status.getData().getIrrValvesStatusL() & 1l << i) != 0) {
                        mAdapter.getIsGreen()[i] = true;
                    } else mAdapter.getIsGreen()[i] = false;
                }
                for (int i = 64; i < 128; i++) {
                    if ((status.getData().getIrrValvesStatusH() & 1l << i) != 0) {
                        mAdapter.getIsGreen()[i] = true;
                    } else mAdapter.getIsGreen()[i] = false;
                }
                mAdapter.notifyDataSetChanged();

                //判断按钮是启动还是停止
                if (AutoObject == 0) {
                    fertRun = status.getData().getFertRunStop();
                    if (fertRun == 1) {
                        tv_start.setText(getString(R.string.fert_str20));
                    } else tv_start.setText(getString(R.string.onl_fert24));
                } else {
                    storageRun = status.getData().getStorageRunStop();
                    if (storageRun == 1) {
                        tv_start.setText(getString(R.string.fert_str20));
                    } else tv_start.setText(getString(R.string.onl_fert24));
                }

                runMode = status.getData().getRunMode();
                setRunMode();
                if (status.getData().getFertRunStop() == 0) {
                    tv_zd1.setText(getString(R.string.onl_fert43));
                } else tv_zd1.setText(getString(R.string.onl_fert44));

                if (status.getData().getStorageRunStop() == 0) {
                    tv_zd2.setText(getString(R.string.onl_fert45));
                } else tv_zd2.setText(getString(R.string.onl_fert46));
            } else {
//                Toasts.showInfo(context, status.getMsg());
            }
        } catch (Exception e) {
            Log.d(TAG, "parseResult2: 解析异常  " + e.getMessage());
        }
        mDialog.dismiss();
        isRequest = true;
    }

    private void setRunMode() {
        if (runMode == 1) {
            rl_handA1.setVisibility(View.GONE);
            rl_handA2.setVisibility(View.VISIBLE);
            tv_handA.setText(getString(R.string.onl_fert47));
        } else {
            rl_handA1.setVisibility(View.VISIBLE);
            rl_handA2.setVisibility(View.GONE);
            tv_handA.setText(getString(R.string.onl_fert48));
        }
    }

    private void setIrr(boolean b, int i, String str) {
        switch (i) {
            case 0:
                tv_irr1.setText(str);
                if (b) {
                    iv_irr1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 1:
                tv_irr2.setText(str);
                if (b) {
                    iv_irr2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 2:
                tv_irr3.setText(str);
                if (b) {
                    iv_irr3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 3:
                tv_irr4.setText(str);
                if (b) {
                    iv_irr4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 4:
                tv_irr5.setText(str);
                if (b) {
                    iv_irr5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 5:
                tv_irr6.setText(str);
                if (b) {
                    iv_irr6.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr6.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 6:
                tv_irr7.setText(str);
                if (b) {
                    iv_irr7.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr7.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 7:
                tv_irr8.setText(str);
                if (b) {
                    iv_irr8.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr8.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 8:
                tv_irr9.setText(str);
                if (b) {
                    iv_irr9.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr9.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 9:
                tv_irr10.setText(str);
                if (b) {
                    iv_irr10.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr10.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 10:
                tv_irr11.setText(str);
                if (b) {
                    iv_irr11.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr11.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 11:
                tv_irr12.setText(str);
                if (b) {
                    iv_irr12.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr12.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 12:
                tv_irr13.setText(str);
                if (b) {
                    iv_irr13.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr13.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 13:
                tv_irr14.setText(str);
                if (b) {
                    iv_irr14.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr14.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 14:
                tv_irr15.setText(str);
                if (b) {
                    iv_irr15.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr15.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 15:
                tv_irr16.setText(str);
                if (b) {
                    iv_irr16.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr16.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 16:
                tv_irr17.setText(str);
                if (b) {
                    iv_irr17.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr17.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 17:
                tv_irr18.setText(str);
                if (b) {
                    iv_irr18.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr18.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 18:
                tv_irr19.setText(str);
                if (b) {
                    iv_irr19.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr19.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 19:
                tv_irr20.setText(str);
                if (b) {
                    iv_irr20.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr20.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 20:
                tv_irr21.setText(str);
                if (b) {
                    iv_irr21.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr21.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 21:
                tv_irr22.setText(str);
                if (b) {
                    iv_irr22.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr22.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 22:
                tv_irr23.setText(str);
                if (b) {
                    iv_irr23.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr23.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 23:
                tv_irr24.setText(str);
                if (b) {
                    iv_irr24.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr24.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 24:
                tv_irr25.setText(str);
                if (b) {
                    iv_irr25.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr25.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 25:
                tv_irr26.setText(str);
                if (b) {
                    iv_irr26.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr26.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 26:
                tv_irr27.setText(str);
                if (b) {
                    iv_irr27.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr27.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 27:
                tv_irr28.setText(str);
                if (b) {
                    iv_irr28.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr28.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 28:
                tv_irr29.setText(str);
                if (b) {
                    iv_irr29.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr29.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 29:
                tv_irr30.setText(str);
                if (b) {
                    iv_irr30.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr30.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 30:
                tv_irr31.setText(str);
                if (b) {
                    iv_irr31.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr31.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            case 31:
                tv_irr32.setText(str);
                if (b) {
                    iv_irr32.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_green_dot));
                } else
                    iv_irr32.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fer_red_dot));
                break;
            default:
                break;
        }
    }


    /**
     * 获取值施肥机状态
     */
    private void getFertilizerState() {
        if (Data.userResult != null && pGhList != null && pGhList.size() > 0) {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"GreenhouseID", pGhList.get(mPosition)[0]});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("GetFertilizerState",
                    "/StringService/DeviceSet.asmx", data, 2, context);
        } else mDialog.dismiss();
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
        } else mDialog.dismiss();
    }

    private void showBottomSheet1() {
        if (pGhList != null && pGhList.size() > 0) {
            if (mBottomSheet1 == null) {
                QMUIBottomSheet.BottomListSheetBuilder mBuilder = new QMUIBottomSheet.BottomListSheetBuilder(context, true)
                        .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                mDownTimer1.cancel();
                                isRequest = false;
                                mDownTimer.cancel();
                                WebServiceManage.dispose();
                                mPosition = position;
                                tv_terminal.setText(pGhList.get(mPosition)[1]);
                                dialog.dismiss();
                                clearData();
                                restartTime();
                                conTypeMark = 0;
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

    private void clearData() {
        config = null;
        hasTanks = null;
//        FerDialog = null;
//        IrrDialog = null;
        mBottomSheet2 = null;
        mBottomSheet3 = null;
        mBottomSheet4 = null;

        AutoObject = 0;
        tv_parameter.setText(getString(R.string.onl_fert23));
        tv_start.setText(getString(R.string.onl_fert24));
        rl_manual.setVisibility(View.VISIBLE);
        rl_storage.setVisibility(View.GONE);

        tv_xf.setText("");
        tv_xf2.setText("");
        if (feradapter != null && feradapter2 != null && FerCheck != null && fertChshow != null) {
            feradapter.setCheck(FerCheck);
            feradapter.setFertChshow(fertChshow);
            feradapter2.setCheck(FerCheck2);
            feradapter2.setFertChshow(fertChshow);
            feradapter.notifyDataSetChanged();
            feradapter2.notifyDataSetChanged();
        }

        for (int i = 0; i < 32; i++) {
            setIrr(false, i, "?");
        }

        if (irradapter != null) {
            tv_fm.setText("");
            irradapter.setCheck(new boolean[128]);
            irrNum = 0;
            irradapter.notifyDataSetChanged();
        }

        runMode = 0;
        rl_handA1.setVisibility(View.VISIBLE);
        rl_handA2.setVisibility(View.GONE);
        tv_handA.setText(getString(R.string.onl_fert48));

    }

    private void showBottomSheet3() {
        if (hasTanks != null && hasTanks.size() != 0) {
            if (mBottomSheet3 == null) {
                QMUIBottomSheet.BottomListSheetBuilder mBuilder = new QMUIBottomSheet.BottomListSheetBuilder(context, true)
                        .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                switch (position) {
                                    case 0:
                                        AutoObject = 0;
                                        tv_parameter.setText(getString(R.string.onl_fert23));
                                        if (fertRun == 0) {
                                            tv_start.setText(getString(R.string.onl_fert24));
                                        } else tv_start.setText(getString(R.string.fert_str20));
                                        rl_manual.setVisibility(View.VISIBLE);
                                        rl_storage.setVisibility(View.GONE);
                                        break;
                                    case 1:
                                        AutoObject = 1;
                                        tv_parameter.setText(getString(R.string.onl_fert49));
                                        if (storageRun == 0) {
                                            tv_start.setText(getString(R.string.onl_fert24));
                                        } else tv_start.setText(getString(R.string.fert_str20));
                                        //初始化储液罐选择
                                        int length = hasTanks.get(0)[0].length();
                                        storageTankNum = Byte.parseByte(hasTanks.get(0)[0].substring(length - 1, length));
                                        storageTankNum--;
                                        tv_zyg.setText(hasTanks.get(0)[0]);

                                        et_ec.setText("1.5");
                                        et_ph.setText("7");

                                        rl_storage.setVisibility(View.VISIBLE);
                                        rl_manual.setVisibility(View.GONE);
                                        break;
                                }
                                dialog.dismiss();
                            }
                        });
                mBuilder.addItem(getString(R.string.onl_fert23));
                mBuilder.addItem(getString(R.string.onl_fert50));
                mBuilder.setCheckedIndex(0);
                mBottomSheet3 = mBuilder.build();
            }
            mBottomSheet3.show();
        }
    }

    private void showBottomSheet2() {
        if (hasTanks != null && hasTanks.size() == 0) {
            if (mBottomSheet2 == null) {
                QMUIBottomSheet.BottomListSheetBuilder mBuilder = new QMUIBottomSheet.BottomListSheetBuilder(context, true)
                        .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                switch (position) {
                                    case 0:
                                        manualCtrMode = 0;
                                        tv_tw.setText(getString(R.string.onl_fert14));
                                        tv_tw2.setText(getString(R.string.onl_fert15));
                                        break;
                                    case 1:
                                        manualCtrMode = 1;
                                        tv_tw.setText(getString(R.string.onl_fert51));
                                        tv_tw2.setText(getString(R.string.onl_fert52));
                                        break;
                                }
                                dialog.dismiss();
                            }
                        });
                mBuilder.addItem(getString(R.string.onl_fert14));
                mBuilder.addItem(getString(R.string.onl_fert51));
                mBuilder.setCheckedIndex(0);
                mBottomSheet2 = mBuilder.build();
            }
            mBottomSheet2.show();
        }
    }

    private void showBottomSheet4() {
        if (mBottomSheet4 == null && hasTanks != null && hasTanks.size() > 0) {
            QMUIBottomSheet.BottomListSheetBuilder mBuilder = new QMUIBottomSheet.BottomListSheetBuilder(context, true)
                    .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                        @Override
                        public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                            StartCondition = 0;
                            tv_monitor.setText(getString(R.string.onl_fert32));
                            tv_yxsc.setText(getString(R.string.onl_fert15));
                            et_yxsc.setVisibility(View.VISIBLE);
                            int length = hasTanks.get(position)[0].length();
                            storageTankNum = Byte.parseByte(hasTanks.get(position)[0].substring(length - 1, length));
                            storageTankNum--;
                            tv_zyg.setText(hasTanks.get(position)[0]);
                            dialog.dismiss();
                        }
                    });
            for (int i = 0; i < hasTanks.size(); i++) {
                mBuilder.addItem(hasTanks.get(i)[0]);
            }
            mBuilder.setCheckedIndex(0);
            mBottomSheet4 = mBuilder.build();
        }
        if (mBottomSheet4 != null) mBottomSheet4.show();
    }

    private void restartTime() {
        mDownTimer1.cancel();
        isRequest = false;
        mDownTimer1.start();
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
        mDownTimer1.cancel();
        isRequest = false;
        mDownTimer.cancel();
        WebServiceManage.dispose();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (task != null) task.cancel();
        if (timer != null) timer.cancel();
        config = null;
    }

}
