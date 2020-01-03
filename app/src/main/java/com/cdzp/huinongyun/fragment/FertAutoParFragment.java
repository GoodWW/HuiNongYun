package com.cdzp.huinongyun.fragment;

import android.content.Context;
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

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.activity.FertActivity;
import com.cdzp.huinongyun.adapter.FerAdapter;
import com.cdzp.huinongyun.adapter.IrrAdapter;
import com.cdzp.huinongyun.bean.AutoDetails;
import com.cdzp.huinongyun.bean.CurrentStateResult;
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
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.cdzp.huinongyun.util.Data.pGhList;

/**
 * 施肥机自动参数
 */
public class FertAutoParFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean isVisible, isInit = false;
    private QMUITipDialog mDialog;
    private QMUIBottomSheet mBottomSheet1, mBottomSheet2;
    private int mPosition1, mPosition2, conTypeMark;
    private TextView tv_terminal, tv_parameter, tv_ffap1, tv_ffap2, tv_ffap3, tv_ffap4, tv_ffap5, tv_ffap6;
    private EditText et_ffap1, et_ffap2, et_ffap3, et_ffap4, et_ffap5;
    private int ValvesNum;
    private AlertDialog IrrDialog = null, FerDialog = null;
    private IrrAdapter irradapter;
    private FerAdapter feradapter;
    private boolean[] FerCheck = new boolean[5];
    private boolean[] fertChshow;
    private String[] FerStr;
    private boolean[] check = new boolean[64];
    private CountDownTimer mDownTimer;
    private AutoDetails details;
    private CheckBox cb_ffap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_fert_auto_par, container, false);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_ffap_terminal:
                showBottomSheet1();
                break;
            case R.id.ll_ffap_parameter:
                showBottomSheet2();
                break;
            case R.id.ll_ffap1:
                if (tv_ffap2.getText().toString().equals(getString(R.string.fert_str13))) {
                    tv_ffap2.setText(getString(R.string.fert_str14));
                } else tv_ffap2.setText(getString(R.string.fert_str13));
                break;
            case R.id.ll_ffap2:
                if (tv_ffap2.getText().toString().equals(getString(R.string.fert_str13))) {
                    if (FerDialog != null) {
                        if (!FerDialog.isShowing())
                            FerDialog.show();
                    } else Toasts.showInfo(context, getString(R.string.fertplan_str3));
                }
                break;
            case R.id.ll_ffap3:
                if (IrrDialog != null) {
                    if (!IrrDialog.isShowing())
                        IrrDialog.show();
                } else Toasts.showInfo(context, getString(R.string.fertplan_str4));
                break;
            case R.id.ll_ffap4:
                if (tv_ffap5.getText().toString().equals(getString(R.string.fert_str11))) {
                    tv_ffap5.setText(getString(R.string.fert_str12));
                    tv_ffap6.setText("min");
                } else {
                    tv_ffap5.setText(getString(R.string.fert_str11));
                    tv_ffap6.setText("m³");
                }
                break;
            case R.id.btn_ffap:
                if (details != null) {
                    if (tv_ffap2.getText().length() > 0 && et_ffap1.getText().length() > 0 &&
                            et_ffap2.getText().length() > 0 && et_ffap3.getText().length() > 0 &&
                            et_ffap4.getText().length() > 0 && et_ffap5.getText().length() > 0) {
                        saveData();
                    } else Toasts.showInfo(context, getString(R.string.request_error6));
                } else Toasts.showInfo(context, getString(R.string.fertplan_str5));
                break;
            case R.id.ib_ffap:
                new QMUIDialog.MessageDialogBuilder(context)
                        .setTitle(getString(R.string.fou_sit_mon_str7))
                        .setMessage(getString(R.string.fert_str50))
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
                                mDialog.show();
                                socketRequest(318, null);
                            }
                        })
                        .create().show();
                break;
            default:

                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 1:
                    parseResult2(message.getmResult());
                    break;
                default:
                    break;
            }
        }
        mPosition2 = 0;
        mDownTimer.cancel();
        mDownTimer.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SocketResult result) {
        if (result.isOK()) {
            switch (result.getLabel()) {
                case 325:
                    mDownTimer.cancel();
                    if (result.getmResult() instanceof AutoDetails) {
                        details = (AutoDetails) result.getmResult();
                        setTextData();
                    }
                    mDialog.dismiss();
                    break;
                case 326:
                    Toasts.showSuccess(context, getString(R.string.socket_succ));
                    break;
                case 327:
                    if (result.getmResult() instanceof Integer) {
                        int automatic = (int) result.getmResult();
                        BottomListSheetBuilder mBuilder = new BottomListSheetBuilder(context, true)
                                .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                        mPosition2 = position;
                                        tv_parameter.setText(getString(R.string.fert_str3) + (mPosition2 + 1));
                                        details = null;
                                        dialog.dismiss();
                                        mDialog.show();
                                        cleanData();
                                        mDownTimer.cancel();
                                        mDownTimer.start();
                                    }
                                });
                        for (int i = 0; i < automatic; i++) {
                            mBuilder.addItem(getString(R.string.fert_str3) + (i + 1));
                        }
                        if (mPosition2 < automatic)
                            mBuilder.setCheckedIndex(mPosition2);
                        mBottomSheet2 = mBuilder.build();
                        Toasts.showSuccess(context, getString(R.string.solenoid_str15));
                    }
                    mDialog.dismiss();
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
        mDialog = ((FertActivity) getActivity()).getmDialog();
        FerStr = new String[]{getString(R.string.fert_str19), "A", "B", "C", "D"};

        tv_terminal = view.findViewById(R.id.tv_ffap_terminal);
        tv_parameter = view.findViewById(R.id.tv_ffap_parameter);
        tv_ffap1 = view.findViewById(R.id.tv_ffap1);
        tv_ffap2 = view.findViewById(R.id.tv_ffap2);
        tv_ffap3 = view.findViewById(R.id.tv_ffap3);
        tv_ffap4 = view.findViewById(R.id.tv_ffap4);
        tv_ffap5 = view.findViewById(R.id.tv_ffap5);
        tv_ffap6 = view.findViewById(R.id.tv_ffap6);
        et_ffap1 = view.findViewById(R.id.et_ffap1);
        et_ffap2 = view.findViewById(R.id.et_ffap2);
        et_ffap3 = view.findViewById(R.id.et_ffap3);
        et_ffap4 = view.findViewById(R.id.et_ffap4);
        et_ffap5 = view.findViewById(R.id.et_ffap5);
        cb_ffap = view.findViewById(R.id.cb_ffap);

        mDownTimer = new CountDownTimer(9000, 2500) {
            @Override
            public void onTick(long millisUntilFinished) {
                socketRequest(316, mPosition2);
            }

            @Override
            public void onFinish() {
                mDialog.dismiss();
            }
        };

        view.findViewById(R.id.ll_ffap_terminal).setOnClickListener(this);
        view.findViewById(R.id.ll_ffap_parameter).setOnClickListener(this);
        view.findViewById(R.id.ll_ffap1).setOnClickListener(this);
        view.findViewById(R.id.ll_ffap2).setOnClickListener(this);
        view.findViewById(R.id.ll_ffap3).setOnClickListener(this);
        view.findViewById(R.id.ll_ffap4).setOnClickListener(this);
        view.findViewById(R.id.btn_ffap).setOnClickListener(this);
        view.findViewById(R.id.ib_ffap).setOnClickListener(this);

        if (isVisible && !isInit) {
            isInit = true;
            initData();
        }
    }

    private void initData() {
        if (pGhList != null && pGhList.size() > 0) {
            mPosition1 = 0;
            tv_terminal.setText(pGhList.get(mPosition1)[1]);
            if (pGhList.get(mPosition1)[6].equals("1")) {
                mDialog.show();
                getCurrentState();
            }
        }
    }

    private void showBottomSheet2() {
        if (mBottomSheet2 != null) {
            mBottomSheet2.show();
        } else if (pGhList != null && tv_terminal.getText().length() > 0 &&
                pGhList.size() > mPosition1 && pGhList.get(mPosition1)[6].equals("1")) {
            mDialog.show();
            mPosition2 = 0;
            getCurrentState();
        }
    }

    private void showBottomSheet1() {
        if (pGhList != null && pGhList.size() > 0) {
            if (mBottomSheet1 == null) {
                BottomListSheetBuilder mBuilder = new BottomListSheetBuilder(context, true)
                        .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                mPosition1 = position;
                                tv_terminal.setText(pGhList.get(mPosition1)[1]);
                                mPosition2 = 0;
                                mBottomSheet2 = null;
                                cleanData();
                                tv_parameter.setText("");
                                FerDialog = null;
                                IrrDialog = null;
                                details = null;
                                dialog.dismiss();
                                if (pGhList.get(mPosition1)[6].equals("1")) {
                                    mDialog.show();
                                    getCurrentState();
                                }
                            }
                        });
                for (int i = 0; i < pGhList.size(); i++) {
                    String str = pGhList.get(i)[1];
                    if (str.contains(getString(R.string.dev_str2))) break;
                    mBuilder.addItem(str);
                }
                mBuilder.setCheckedIndex(mPosition1);
                mBottomSheet1 = mBuilder.build();
            }
            mBottomSheet1.show();
        } else Toasts.showInfo(context, getString(R.string.request_error8));
    }

    private void setTextData() {
        if (details.getAutoDetail() > 0) {
            if (mBottomSheet2 == null) {
                BottomListSheetBuilder mBuilder = new BottomListSheetBuilder(context, true)
                        .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                mPosition2 = position;
                                tv_parameter.setText(getString(R.string.fert_str3) + (mPosition2 + 1));
                                details = null;
                                dialog.dismiss();
                                mDialog.show();
                                cleanData();
                                mDownTimer.cancel();
                                mDownTimer.start();
                            }
                        });
                for (int i = 0; i < details.getAutoDetail(); i++) {
                    mBuilder.addItem(getString(R.string.fert_str3) + (i + 1));
                }
                mBuilder.setCheckedIndex(mPosition2);
                mBottomSheet2 = mBuilder.build();
                mPosition2 = 0;
                tv_parameter.setText(getString(R.string.fert_str3) + "1");
            }

            try {
                tv_ffap1.setText((details.getSensorIndex() + 1) + " " + getString(R.string.fert_str54));
                et_ffap1.setText(details.getUpperLimit1() + "");
                et_ffap2.setText(details.getLowerLimit1() + "");
                if (details.getAutoMode() == 0) {
                    tv_ffap2.setText(getString(R.string.fert_str14));
                } else tv_ffap2.setText(getString(R.string.fert_str13));
                et_ffap3.setText((double) details.getECSetValue() / 1000 + "");
                et_ffap4.setText((double) details.getPHSetValue() / 100 + "");
                if (details.getWaterQuantityCtlMode() == 0) {
                    tv_ffap5.setText(getString(R.string.fert_str11));
                    tv_ffap6.setText("m³");
                    double d = (double) details.getWaterCtlValue() / 100;
                    et_ffap5.setText(d + "");
                } else if (details.getWaterQuantityCtlMode() == 1) {
                    tv_ffap5.setText(getString(R.string.fert_str12));
                    tv_ffap6.setText("min");
                    et_ffap5.setText(details.getWaterCtlValue() + "");
                }
                if (details.getIsUsed() == 1) {
                    cb_ffap.setChecked(true);
                } else cb_ffap.setChecked(false);

                if (FerDialog != null) {
                    FerCheck = new boolean[5];
                    String str = "";
                    for (int i = 0; i < 5; i++) {
                        if ((details.getFerCh() & 1 << i) != 0) {
                            FerCheck[i] = true;
                            str = str + FerStr[i] + "、";
                        } else {
                            FerCheck[i] = false;
                        }
                    }
                    if (str.length() > 0) {
                        tv_ffap3.setText(str.substring(0, str.length() - 1));
                    } else tv_ffap3.setText("");
                    feradapter.setCheck(FerCheck);
                    feradapter.notifyDataSetChanged();
                }

                if (IrrDialog != null) {
                    check = new boolean[64];
                    int irrNum = 0;
                    for (int i = 0; i < 64; i++) {
                        if ((details.getIrrigationValve() & 1l << i) != 0) {
                            check[i] = true;
                            irrNum++;
                        } else {
                            check[i] = false;
                        }
                    }
                    tv_ffap4.setText(getString(R.string.fert_str44) + irrNum + getString(R.string.fert_str45));
                    irradapter.setCheck(check);
                    irradapter.notifyDataSetChanged();
                }

            } catch (Exception e) {
                Toasts.showInfo(context, getString(R.string.warn_str5));
            }

        } else Toasts.showInfo(context, getString(R.string.fertplan_str2));

    }

    private void parseResult2(String s) {
        try {
            CurrentStateResult result = new Gson().fromJson(s, CurrentStateResult.class);
            ValvesNum = result.getData().getValvesNum();
            fertChshow = new boolean[5];
            for (int i = 0; i < 5; i++) {
                if ((result.getData().getFertCh() & (1 << i)) != 0) {
                    fertChshow[i] = true;
                } else {
                    fertChshow[i] = false;
                }
            }

            //初始化施肥弹窗
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View contentView = LayoutInflater.from(context).inflate(R.layout.irrlayout, null);
            RecyclerView mRecyclerView = contentView.findViewById(R.id.irr);
            mRecyclerView.setLayoutManager(new GridLayoutManager(context, 5));
            FerCheck = new boolean[5];
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
                        tv_ffap3.setText(str.substring(0, str.length() - 1));
                    } else tv_ffap3.setText("");
                }
            });
            feradapter.setFertChshow(fertChshow);
            mRecyclerView.setAdapter(feradapter);
            builder.setTitle(getString(R.string.fert_str18));
            builder.setView(contentView);
            builder.setPositiveButton(getString(R.string.personal_str6), null);
            FerDialog = builder.create();


            //初始化灌溉弹窗
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            View contentView1 = LayoutInflater.from(context).inflate(R.layout.irrlayout, null);
            RecyclerView mRecyclerView1 = contentView1.findViewById(R.id.irr);
            mRecyclerView1.setLayoutManager(new GridLayoutManager(context, 6));
            check = new boolean[64];
            irradapter = new IrrAdapter(check);
            irradapter.setCallback(new MyCallback() {
                @Override
                public void checkBox(int position, boolean b) {
                    int irrNum = 0;
                    for (int i = 0; i < irradapter.getCheck().length; i++) {
                        if (irradapter.getCheck()[i]) {
                            irrNum++;
                        }
                    }
                    tv_ffap4.setText(getString(R.string.fert_str44) + irrNum + getString(R.string.fert_str45));
                }
            });
            irradapter.setValvesNum(ValvesNum);
            mRecyclerView1.setAdapter(irradapter);
            builder1.setTitle(getString(R.string.fert_str17));
            builder1.setView(contentView1);
            builder1.setPositiveButton(getString(R.string.personal_str6), null);
            IrrDialog = builder1.create();

        } catch (Exception e) {

        }
    }

    private void saveData() {
        try {
            if (Float.parseFloat(et_ffap1.getText().toString()) > 100) {
                Toasts.showInfo(context, getString(R.string.fert_str51));
                return;
            }
            if (Float.parseFloat(et_ffap2.getText().toString()) > 100) {
                Toasts.showInfo(context, getString(R.string.fert_str52));
                return;
            }
            if (Float.parseFloat(et_ffap1.getText().toString()) < Float.parseFloat(et_ffap2.getText().toString())) {
                Toasts.showInfo(context, getString(R.string.fert_str53));
                return;
            }
            if (Double.parseDouble(et_ffap3.getText().toString()) > 5) {
                Toasts.showInfo(context, getString(R.string.fert_str30));
                return;
            }
            if (Double.parseDouble(et_ffap4.getText().toString()) > 9 || Double.parseDouble(et_ffap4.getText().toString()) < 4) {
                Toasts.showInfo(context, getString(R.string.fert_str31));
                return;
            }
            if (Double.parseDouble(et_ffap5.getText().toString()) > 3000) {
                Toasts.showInfo(context, getString(R.string.fert_str29));
                return;
            }
            details.setUpperLimit1(Float.parseFloat(et_ffap1.getText().toString()));
            details.setLowerLimit1(Float.parseFloat(et_ffap2.getText().toString()));
            if (tv_ffap2.equals(getString(R.string.fert_str14))) {
                details.setAutoMode((byte) 0);
                long IrrigationValve = 0;
                for (int i = 64; i > 0; i--) {
                    IrrigationValve = IrrigationValve << 1;
                    if (check[i - 1])
                        IrrigationValve = IrrigationValve + 1;
                }
                details.setIrrigationValve(IrrigationValve);
            } else {
                details.setAutoMode((byte) 1);
                int FerCh = 0;
                for (int i = 5; i > 0; i--) {
                    FerCh = FerCh << 1;
                    if (FerCheck[i - 1])
                        FerCh = FerCh + 1;
                }
                details.setFerCh((byte) FerCh);

                long IrrigationValve = 0;
                for (int i = 64; i > 0; i--) {
                    IrrigationValve = IrrigationValve << 1;
                    if (check[i - 1])
                        IrrigationValve = IrrigationValve + 1;
                }
                details.setIrrigationValve(IrrigationValve);
            }
            details.setECSetValue((short) (Double.parseDouble(et_ffap3.getText().toString()) * 1000));
            details.setPHSetValue((short) (Double.parseDouble(et_ffap4.getText().toString()) * 100));
            if (tv_ffap5.getText().toString().equals(getString(R.string.fert_str11))) {
                details.setWaterQuantityCtlMode((byte) 0);
                details.setWaterCtlValue((short) (Double.parseDouble(et_ffap5.getText().toString()) * 100));
            } else {
                details.setWaterQuantityCtlMode((byte) 1);
                details.setWaterCtlValue((short) Double.parseDouble(et_ffap5.getText().toString()));
            }
            if (cb_ffap.isChecked()) {
                details.setIsUsed((byte) 1);
            } else details.setIsUsed((byte) 0);

            socketRequest(317, details);
        } catch (Exception e) {
            Toasts.showInfo(context, getString(R.string.apsf_str2));
        }
    }

    /**
     * 发送请求
     *
     * @param commandType 命令类型
     * @param object      请求数据
     */
    private void socketRequest(int commandType, Object object) {
        if (Data.userResult != null && tv_terminal.getText().length() > 0) {
            SocketMessage message = new SocketMessage();
            message.setZP_KEY_CLIENT(987656789);
            message.setRequestType(308);
            message.setmSender(Data.userResult.getData().get(0).getUserName());
            message.setmReceiver(pGhList.get(mPosition1)[2]);
            message.setCommandType(commandType);
            message.setResult(object);

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
     * 获取值施肥机状态值
     */
    private void getCurrentState() {
        if (Data.userResult != null) {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"GreenhouseID", pGhList.get(mPosition1)[0]});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("GetCurrentState",
                    "/StringService/DeviceSet.asmx", data, 1, context);
        } else {
            mPosition2 = 0;
            mDownTimer.cancel();
            mDownTimer.start();
        }
    }

    private void cleanData() {
        tv_ffap1.setText("");
        tv_ffap2.setText("");
        tv_ffap3.setText("");
        tv_ffap4.setText("");
        et_ffap1.setText("");
        et_ffap2.setText("");
        et_ffap3.setText("");
        et_ffap4.setText("");
        et_ffap5.setText("");
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
