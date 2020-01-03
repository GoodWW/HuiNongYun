package com.cdzp.huinongyun.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.activity.SolenoidActivity;
import com.cdzp.huinongyun.adapter.PlanIrrAdapter;
import com.cdzp.huinongyun.bean.SocketResult;
import com.cdzp.huinongyun.bean.SolGetCtlListResult;
import com.cdzp.huinongyun.bean.WVH_PlanParam;
import com.cdzp.huinongyun.message.SocketMessage;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.cdzp.huinongyun.util.Data.pGhList;

/**
 * 电磁阀计划灌溉
 */
public class SolPlanIrrFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean isVisible, isInit = false, isStart;
    private QMUITipDialog mDialog;
    private int mPosition1, mPosition2, conTypeMark;
    private TextView tv_terminal, tv_plan;
    private QMUIBottomSheet mBottomSheet1, mBottomSheet2;
    private CountDownTimer mDownTimer;
    private List<String[]> planList;
    private TextView tv1, tv2, tv3, tv6;
    private EditText et4, et5, et7, et8;
    private CheckBox cb;
    private AlertDialog irrDialog = null;
    private PlanIrrAdapter mAdapter;
    private TimePickerView pvTime1, pvTime2;
    private Date date1, startDate, endDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_sol_plan_irr, container, false);
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
        } else EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 666:
                    parseResult();
                    break;
                case 1:
                    parseResult1(message.getmResult());
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
        if (result.isOK()) {
            conTypeMark = 0;
            switch (result.getLabel()) {
                case 638:
                    mDownTimer.cancel();
                    if (result.getmResult() instanceof WVH_PlanParam) {
                        WVH_PlanParam param = (WVH_PlanParam) result.getmResult();
                        if (planList == null) {
                            initplanList(param);
                        } else {
                            tv1.setText(param.getFirst_run_date());
                            date1 = getTime11(param.getFirst_run_date());
                            if (param.getStart_time().length() == 4) {
                                StringBuffer buffer = new StringBuffer(param.getStart_time());
                                buffer.insert(2, ":");
                                tv2.setText(buffer.toString());
                                startDate = getTime21(tv2.getText().toString());
                            }
                            if (param.getEnd_time().length() == 4) {
                                StringBuffer buffer = new StringBuffer(param.getEnd_time());
                                buffer.insert(2, ":");
                                tv3.setText(buffer.toString());
                                endDate = getTime21(tv3.getText().toString());
                            }
                            et4.setText(param.getInterval() + "");
                            et5.setText(param.getWater_limit() + "");
                            et7.setText(param.getRunTime() + "");
                            et8.setText(param.getPushTime() + "");
                            cb.setChecked(param.isUsed());
                            setText6(param.getIrriZone_L(), param.getIrriZone_H());
                            if (mAdapter != null) {
                                mAdapter.setIrriZone_L(param.getIrriZone_L());
                                mAdapter.setIrriZone_H(param.getIrriZone_H());
                                mAdapter.notifyDataSetChanged();
                            }
                            mDialog.dismiss();
                        }
                    } else mDialog.dismiss();
                    break;
                case 639:
                    Toasts.showSuccess(context, getString(R.string.devmain_str7));
                    break;
                case 640:
                    Toasts.showError(context, getString(R.string.solenoid_str12));
                    break;
                case 637:
                    Toasts.showSuccess(context, getString(R.string.solenoid_str15));
                    mDialog.dismiss();
                    mDialog.show();
                    planList = null;
                    mDownTimer.cancel();
                    mDownTimer.start();
                    break;
                default:

                    mDialog.dismiss();
                    break;
            }
        } else {
            mDialog.dismiss();
            Toasts.showInfo(context, result.getRemark());
        }
    }

    private void initView() {
        context = getContext();
        mDialog = ((SolenoidActivity) getActivity()).getmDialog();

        tv_terminal = view.findViewById(R.id.tv_fspi_terminal);
        tv_plan = view.findViewById(R.id.tv_fspi_plan);

        tv1 = view.findViewById(R.id.tv_fspi1);
        tv2 = view.findViewById(R.id.tv_fspi2);
        tv3 = view.findViewById(R.id.tv_fspi3);
        et4 = view.findViewById(R.id.et_fspi4);
        et5 = view.findViewById(R.id.et_fspi5);
        et7 = view.findViewById(R.id.et_fspi7);
        et8 = view.findViewById(R.id.et_fspi8);
        tv6 = view.findViewById(R.id.tv_fspi6);
        cb = view.findViewById(R.id.cb_fspi);

        mDownTimer = new CountDownTimer(9000, 2500) {
            @Override
            public void onTick(long millisUntilFinished) {
                socketRequest(618, mPosition2);
            }

            @Override
            public void onFinish() {
                mDialog.dismiss();
            }
        };

        date1 = new Date();
        startDate = new Date();
        endDate = new Date();

        pvTime1 = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                date1 = date;
                tv1.setText(getTime1(date1));
            }
        })
                .isCyclic(true)
                .setType(new boolean[]{true, true, true, false, false, false})
                .build();

        pvTime2 = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (isStart) {
                    startDate = date;
                    tv2.setText(getTime2(startDate));
                } else {
                    endDate = date;
                    tv3.setText(getTime2(endDate));
                }
            }
        })
                .isCyclic(true)
                .setType(new boolean[]{false, false, false, true, true, false})
                .build();

        view.findViewById(R.id.ll_fspi1).setOnClickListener(this);
        view.findViewById(R.id.ll_fspi2).setOnClickListener(this);
        view.findViewById(R.id.ll_fspi3).setOnClickListener(this);
        view.findViewById(R.id.ll_fspi4).setOnClickListener(this);
        view.findViewById(R.id.ll_fspi5).setOnClickListener(this);
        view.findViewById(R.id.ll_fspi6).setOnClickListener(this);
        view.findViewById(R.id.btn_fspi).setOnClickListener(this);
        view.findViewById(R.id.btn_fspi_add).setOnClickListener(this);

        if (isVisible && !isInit) {
            isInit = true;
            initData();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_fspi1:
                showBottomSheet1();
                break;
            case R.id.ll_fspi2:
                showBottomSheet2();
                break;
            case R.id.ll_fspi3:
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date1);
                pvTime1.setDate(calendar);
                pvTime1.show();
                break;
            case R.id.ll_fspi4:
                isStart = true;
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(startDate);
                pvTime2.setDate(calendar1);
                pvTime2.show();
                break;
            case R.id.ll_fspi5:
                isStart = false;
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(endDate);
                pvTime2.setDate(calendar2);
                pvTime2.show();
                break;
            case R.id.ll_fspi6:
                showBottomSheet3();
                break;
            case R.id.btn_fspi:
                if (mAdapter != null) {
                    if (tv_terminal.getText().length() > 0 && tv_plan.getText().length() > 0 &&
                            tv1.getText().length() > 0 && tv2.getText().length() > 0 &&
                            tv3.getText().length() > 0 && et4.getText().length() > 0 &&
                            et5.getText().length() > 0 && et7.getText().length() > 0 &&
                            et8.getText().length() > 0) {
                        modifyPlan();
                    } else Toasts.showInfo(context, getString(R.string.request_error6));
                } else Toasts.showInfo(context, getString(R.string.solenoid_str11));
                break;
            case R.id.btn_fspi_add:
                String planStr;
                if (planList != null) {
                    planStr = getString(R.string.solenoid_str26) + planList.size() +
                            getString(R.string.solenoid_str27);
                } else planStr = getString(R.string.solenoid_str28);
                new QMUIDialog.MessageDialogBuilder(context)
                        .setTitle(getString(R.string.fou_sit_mon_str7))
                        .setMessage(planStr)
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
                                Toasts.showInfo(context, getString(R.string.solenoid_str14));
                                socketRequest(617, null);
                            }
                        })
                        .create().show();
                break;
            default:
                break;
        }
    }

    private void modifyPlan() {
        try {
            WVH_PlanParam param = new WVH_PlanParam();
            param.setSerialNum(mPosition2);
            param.setFirst_run_date(tv1.getText().toString());
            param.setStart_time(tv2.getText().toString().replace(":", ""));
            param.setEnd_time(tv3.getText().toString().replace(":", ""));
            param.setInterval(Short.parseShort(et4.getText().toString()));
            param.setWater_limit(Float.parseFloat(et5.getText().toString()));
            param.setRunTime(Short.parseShort(et7.getText().toString()));
            param.setPushTime(Short.parseShort(et8.getText().toString()));
            if (cb.isChecked()) {
                param.setUsed(true);
            } else param.setUsed(false);
            long irriZone_L = 0l, irriZone_H = 0l;
            for (int i = mAdapter.getIrrList_L().size() - 1; i >= 0; i--) {
                irriZone_L = irriZone_L << 1;
                if (mAdapter.getIrrList_L().get(i)) irriZone_L += 1;
            }
            for (int i = mAdapter.getIrrList_H().size() - 1; i >= 0; i--) {
                irriZone_H = irriZone_H << 1;
                if (mAdapter.getIrrList_H().get(i)) irriZone_H += 1;
            }
            param.setIrriZone_L(irriZone_L);
            param.setIrriZone_H(irriZone_H);
            Toasts.showInfo(context, getString(R.string.solenoid_str13));
            socketRequest(619, param);
        } catch (Exception e) {
            Toasts.showInfo(context, getString(R.string.apsf_str2));
        }
    }

    private void initplanList(WVH_PlanParam param) {
        planList = new ArrayList<>();

        String planLowBinary = Long.toBinaryString(param.getPlanLow());
        planLowBinary = new StringBuffer(planLowBinary).reverse().toString();
        for (int i = 0; i < planLowBinary.length(); i++) {
            if (planLowBinary.substring(i, i + 1).equals("1")) {
                planList.add(new String[]{(i + 1) + "", getString(R.string.fert_str21) + (i + 1)});
            }
        }


        String planHighBinary = Long.toBinaryString(param.getPlanHigh());
        planHighBinary = new StringBuffer(planHighBinary).reverse().toString();
        for (int i = 0; i < planHighBinary.length(); i++) {
            if (planHighBinary.substring(i, i + 1).equals("1")) {
                planList.add(new String[]{(i + 65) + "", getString(R.string.fert_str21) + (i + 65)});
            }
        }
        if (planList.size() > 0) {
            mPosition2 = 0;
            QMUIBottomSheet.BottomListSheetBuilder mBuilder = new QMUIBottomSheet.BottomListSheetBuilder(context, true)
                    .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                        @Override
                        public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                            mPosition2 = position;
                            tv_plan.setText(planList.get(mPosition2)[1]);
                            dialog.dismiss();
                            mDialog.show();
                            clearData();
                            mDownTimer.cancel();
                            mDownTimer.start();
                        }
                    });
            for (int i = 0; i < planList.size(); i++) {
                mBuilder.addItem(planList.get(i)[1]);
            }
            mBuilder.setCheckedIndex(mPosition2);
            mBottomSheet2 = mBuilder.build();
            tv_plan.setText(planList.get(mPosition2)[1]);
            solenoidGetCtlList();
        } else mDialog.dismiss();

    }

    private void showBottomSheet3() {
        if (mAdapter != null) {
            if (mAdapter.getList().size() > 0 && irrDialog != null) {
                irrDialog.show();
            } else Toasts.showInfo(context, getString(R.string.request_error8));
        } else {
            mDialog.show();
            solenoidGetCtlList();
        }
    }

    private void showBottomSheet2() {
        if (planList != null) {
            if (planList.size() > 0 && mBottomSheet2 != null) {
                mBottomSheet2.show();
            } else Toasts.showInfo(context, getString(R.string.request_error8));
        } else {
            mDialog.show();
            planList = null;
            mDownTimer.cancel();
            mDownTimer.start();
        }
    }

    private void showBottomSheet1() {
        if (pGhList != null) {
            if (pGhList.size() > 0) {
                if (mBottomSheet1 == null) {
                    QMUIBottomSheet.BottomListSheetBuilder mBuilder = new QMUIBottomSheet.BottomListSheetBuilder(context, true)
                            .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                                @Override
                                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                    mPosition1 = position;
                                    tv_terminal.setText(pGhList.get(mPosition1)[1]);
                                    dialog.dismiss();
                                    mDialog.show();
                                    planList = null;
                                    tv_plan.setText("");
                                    clearData();
                                    mDownTimer.cancel();
                                    mDownTimer.start();
                                }
                            });
                    for (int i = 0; i < pGhList.size(); i++) {
                        String str = pGhList.get(i)[1];
                        if (!str.contains(getString(R.string.dev_str2))) {
                            mBuilder.addItem(str);
                        } else break;
                    }
                    mBuilder.setCheckedIndex(mPosition1);
                    mBottomSheet1 = mBuilder.build();
                }
                mBottomSheet1.show();
            } else Toasts.showInfo(context, getString(R.string.request_error8));
        } else {
            getGreenHouseSel();
        }
    }

    private void initData() {
        if (pGhList == null) {
            getGreenHouseSel();
        } else {
            if (pGhList.size() > 0) {
                mPosition1 = 0;
                tv_terminal.setText(pGhList.get(mPosition1)[1]);
                if (pGhList.get(mPosition1)[6].equals("1")) {
                    mDialog.show();
                    planList = null;
                    mDownTimer.cancel();
                    mDownTimer.start();
                }
            } else Toasts.showError(context, getString(R.string.request_error8));
        }
    }

    /**
     * 发送请求
     *
     * @param commandType 命令类型
     * @param result      计划序号
     */
    private void socketRequest(int commandType, Object result) {
        if (Data.userResult != null && pGhList != null && pGhList.size() > mPosition1) {
            SocketMessage message = new SocketMessage();
            message.setZP_KEY_CLIENT(987656789);
            message.setRequestType(610);
            message.setmSender(Data.userResult.getData().get(0).getUserName());
            message.setmReceiver(pGhList.get(mPosition1)[2]);
            message.setCommandType(commandType);
            message.setResult(result);
            if (pGhList.get(mPosition1)[3].equals("1") && conTypeMark < 4) {
                message.setIp(pGhList.get(mPosition1)[4]);
                message.setPort(Integer.parseInt(pGhList.get(mPosition1)[5]));
                conTypeMark++;
            } else {
                message.setIp(Data.SERVER_IP);
                message.setPort(Data.SERVER_PORT);
            }
            Operate.getDefault().getSender().messageRequest(message);
        }
    }

    /**
     * 获取区域
     */
    private void solenoidGetCtlList() {
        if (Data.userResult != null) {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"gwID", pGhList.get(mPosition1)[0]});
            data.add(new String[]{"pageIndex", "1"});
            data.add(new String[]{"pageSize", "100"});
            data.add(new String[]{"userID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"authCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("SolenoidGetCtlList",
                    "/StringService/DeviceSet.asmx", data, 1, context);
        } else {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    private void getGreenHouseSel() {
        if (Data.userResult != null && Data.GhType != null) {
            mDialog.show();
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"Type", Data.GhType});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("GreenHouseSel",
                    "/StringService/DataInfo.asmx", data, 666, context);
        } else Toasts.showError(context, getString(R.string.request_error9));
    }

    private void parseResult1(String s) {
        try {
            SolGetCtlListResult result = new Gson().fromJson(s, SolGetCtlListResult.class);
            if (mAdapter == null) {
                mAdapter = new PlanIrrAdapter();
            } else {
                mAdapter.getList().clear();
                mAdapter.getIrrList_L().clear();
                mAdapter.getIrrList_H().clear();
            }
            if (result.isSuccess() && result.getData().size() > 0) {
                for (int i = 0; i < result.getData().size(); i++) {
                    int displayNo = Integer.parseInt(result.getData().get(i).getDisplayNo());
                    mAdapter.getList().add(new String[]{
                            result.getData().get(i).getGreenhouseName(),
                            result.getData().get(i).getIsUsed() + "",
                            result.getData().get(i).getIrrigationState()
                    });
                    if (displayNo < 64) {
                        mAdapter.getIrrList_L().add(false);
                        mAdapter.getTemIrrList_L().add(false);
                    } else {
                        mAdapter.getIrrList_H().add(false);
                        mAdapter.getTemIrrList_H().add(false);
                    }
                }
                if (irrDialog == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View contentView = LayoutInflater.from(context).inflate(R.layout.irrlayout, null);
                    RecyclerView mRecyclerView = contentView.findViewById(R.id.irr);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                    mRecyclerView.setAdapter(mAdapter);
                    builder.setTitle(getString(R.string.fert_str17));
                    builder.setView(contentView);
                    builder.setPositiveButton(getString(R.string.personal_str6), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAdapter.notifyDataHL();
                            setText6(mAdapter.getIrrList_L(), mAdapter.getIrrList_H());
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(getString(R.string.envir_str1), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAdapter.cancelHL();
                            dialog.dismiss();
                        }
                    });
                    irrDialog = builder.create();
                    irrDialog.setCancelable(false);
                }
                mAdapter.notifyDataSetChanged();
                mDownTimer.cancel();
                mDownTimer.start();
            } else {
                mAdapter.notifyDataSetChanged();
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.request_error8));
            }
        } catch (Exception e) {
            mDialog.dismiss();
            Toasts.showInfo(context, getString(R.string.request_error2));
        }
    }

    private void parseResult() {
        if (pGhList != null && pGhList.size() > 0) {
            mPosition1 = 0;
            tv_terminal.setText(pGhList.get(mPosition1)[1]);
            if (pGhList.get(mPosition1)[6].equals("1")) {
                planList = null;
                mDownTimer.cancel();
                mDownTimer.start();
            } else {
                mDialog.dismiss();
            }
        } else {
            mDialog.dismiss();
            Toasts.showInfo(context, getString(R.string.request_error8));
        }
    }

    private void clearData() {
        tv1.setText("");
        tv2.setText("");
        tv3.setText("");
        et4.setText("");
        et5.setText("");
        et7.setText("");
        et8.setText("");
        tv6.setText("");
        cb.setChecked(false);
    }

    private void setText6(long irriZone_l, long irriZone_h) {
        String str6 = "";
        for (int i = 0; i < 64; i++) {
            if ((irriZone_l & 1l << i) != 0) {
                if (str6.length() > 0) {
                    str6 = str6 + "," + (i + 1);
                } else str6 = (i + 1) + "";
            }
        }
        for (int i = 0; i < 36; i++) {
            if ((irriZone_h & 1l << i) != 0) {
                if (str6.length() > 0) {
                    str6 = str6 + "," + (i + 65);
                } else str6 = (i + 65) + "";
            }
        }
        tv6.setText(str6);
    }

    private void setText6(List<Boolean> irriZone_l, List<Boolean> irriZone_h) {
        String str6 = "";
        for (int i = 0; i < irriZone_l.size(); i++) {
            if (irriZone_l.get(i)) {
                if (str6.length() > 0) {
                    str6 = str6 + "," + (i + 1);
                } else str6 = (i + 1) + "";
            }
        }
        for (int i = 0; i < irriZone_h.size(); i++) {
            if (irriZone_h.get(i)) {
                if (str6.length() > 0) {
                    str6 = str6 + "," + (i + 65);
                } else str6 = (i + 65) + "";
            }
        }
        tv6.setText(str6);
    }

    private String getTime1(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    private Date getTime11(String date) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    private String getTime2(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(date);
    }

    private Date getTime21(String date) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    private void register() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
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
    }

}
