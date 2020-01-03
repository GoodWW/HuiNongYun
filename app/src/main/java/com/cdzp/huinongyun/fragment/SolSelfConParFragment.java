package com.cdzp.huinongyun.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.activity.SolenoidActivity;
import com.cdzp.huinongyun.bean.SocketResult;
import com.cdzp.huinongyun.bean.SolGetAllResult;
import com.cdzp.huinongyun.bean.SolGetCtlListResult;
import com.cdzp.huinongyun.bean.WvhAutoParam;
import com.cdzp.huinongyun.message.SocketMessage;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Operate;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.cdzp.huinongyun.util.Data.pGhList;

/**
 * 电磁阀自控参数
 */
public class SolSelfConParFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean isVisible, isInit = false;
    private QMUITipDialog mDialog;
    private List<String[]> greHouList;
    private TextView tv_terminal, tv_area, tv_layer;
    private int mPosition1, mPosition2, mPosition3, conTypeMark;
    private QMUIBottomSheet mBottomSheet1, mBottomSheet2, mBottomSheet3;
    private EditText et1, et2, et3;
    private List<String> layerList;
    private CountDownTimer mDownTimer;
    private CheckBox cb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_sol_self_con_par, container, false);
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
            if (mDownTimer != null) mDownTimer.cancel();
            EventBus.getDefault().unregister(this);
        }
    }

    private void initView() {
        context = getContext();
        mDialog = ((SolenoidActivity) getActivity()).getmDialog();

        tv_terminal = view.findViewById(R.id.tv_fsscp_terminal);
        tv_area = view.findViewById(R.id.tv_fsscp_area);
        et1 = view.findViewById(R.id.et_fsscp1);
        et2 = view.findViewById(R.id.et_fsscp2);
        et3 = view.findViewById(R.id.et_fsscp3);
        tv_layer = view.findViewById(R.id.tv_fsscp_layer);
        cb = view.findViewById(R.id.cb_fsscp);

        mDownTimer = new CountDownTimer(9000, 2500) {
            @Override
            public void onTick(long millisUntilFinished) {
                try {
                    socketRequest(615, Integer.parseInt(greHouList.get(mPosition2)[2]));
                } catch (Exception e) {
                    mDownTimer.cancel();
                    mDialog.dismiss();
                    Toasts.showInfo(context, getString(R.string.request_error2));
                }
            }

            @Override
            public void onFinish() {
                mDialog.dismiss();
            }
        };

        view.findViewById(R.id.ll_fsscp1).setOnClickListener(this);
        view.findViewById(R.id.ll_fsscp2).setOnClickListener(this);
        view.findViewById(R.id.ll_fsscp3).setOnClickListener(this);
        view.findViewById(R.id.btn_fsscp).setOnClickListener(this);

        if (isVisible && !isInit) {
            isInit = true;
            register();
            initData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fsscp1:
                showBottomSheet1();
                break;
            case R.id.ll_fsscp2:
                showBottomSheet2();
                break;
            case R.id.ll_fsscp3:
                showBottomSheet3();
                break;
            case R.id.btn_fsscp:
                submit();
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
                    parseResult();
                    break;
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
        if (result.isOK()) {
            switch (result.getLabel()) {
                case 634:
                    mDownTimer.cancel();
                    if (result.getmResult() instanceof WvhAutoParam) {
                        WvhAutoParam param = (WvhAutoParam) result.getmResult();
                        et1.setText(param.getHum_top_value() + "");
                        et2.setText(param.getHum_low_value() + "");
                        et3.setText(param.getWater_limit() + "");
                        if (param.getTarget_layer() < layerList.size()) {
                            mPosition3 = param.getTarget_layer();
                            tv_layer.setText(layerList.get(mPosition3));
                        }
                        cb.setChecked(param.isUsed());
                    }
                    mDialog.dismiss();
                    break;
                case 635:
                    mDialog.dismiss();
                    Toasts.showSuccess(context, getString(R.string.parsetstr));
                    break;
                case 636:
                    mDialog.dismiss();
                    Toasts.showError(context, getString(R.string.solenoid_str10));
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

    private void initData() {
        if (pGhList == null) {
            getGreenHouseSel();
        } else {
            if (pGhList.size() > 0) {
                mPosition1 = 0;
                tv_terminal.setText(pGhList.get(mPosition1)[1]);
                if (pGhList.get(mPosition1)[6].equals("1")) {
                    mDialog.show();
                    solenoidGetCtlList();
                }
            } else Toasts.showError(context, getString(R.string.request_error8));
        }
    }

    private void showBottomSheet3() {
        if (layerList != null) {
            if (layerList.size() > 0) {
                if (mBottomSheet3 != null) {
                    mBottomSheet3.show();
                }
            } else Toasts.showInfo(context, getString(R.string.request_error8));
        } else if (tv_terminal.getText().length() > 0 && tv_area.getText().length() > 0) {
            mDialog.show();
            solenoidGetAllLayer();
        }
    }

    private void showBottomSheet2() {
        if (greHouList != null) {
            if (greHouList.size() > 0) {
                if (mBottomSheet2 != null) {
                    mBottomSheet2.show();
                } else Toasts.showInfo(context, getString(R.string.request_error2));
            } else Toasts.showInfo(context, getString(R.string.request_error8));
        } else if (tv_terminal.getText().length() > 0) {
            mDialog.show();
            solenoidGetCtlList();
        }
    }

    private void showBottomSheet1() {
        if (pGhList != null) {
            if (pGhList.size() > 0) {
                if (mBottomSheet1 == null) {
                    BottomListSheetBuilder mBuilder = new BottomListSheetBuilder(context, true)
                            .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                                @Override
                                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                    mPosition1 = position;
                                    tv_terminal.setText(pGhList.get(mPosition1)[1]);
                                    greHouList = null;
                                    dialog.dismiss();
                                    mDialog.show();
                                    clearData();
                                    solenoidGetCtlList();
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

    private void submit() {
        if (tv_terminal.getText().length() > 0 && tv_area.getText().length() > 0 &&
                et1.getText().length() > 0 && et2.getText().length() > 0 &&
                et3.getText().length() > 0 && tv_layer.getText().length() > 0) {
            try {
                mDialog.show();
                WvhAutoParam param = new WvhAutoParam();
                param.setSerial(Integer.parseInt(greHouList.get(mPosition2)[2]));
                param.setHum_top_value(Float.parseFloat(et1.getText().toString()));
                param.setHum_low_value(Float.parseFloat(et2.getText().toString()));
                param.setTarget_layer(mPosition3);
                param.setWater_limit(Float.parseFloat(et3.getText().toString()));
                param.setUsed(cb.isChecked());
                socketRequest(616, param);
            } catch (Exception e) {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.request_error2));
            }
        } else Toasts.showInfo(context, getString(R.string.request_error6));
    }

    /**
     * 发送请求
     *
     * @param commandType 命令类型
     * @param result      控制器序号+控制码
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

    private void parseResult2(String s) {
        try {
            if (layerList == null) {
                layerList = new ArrayList<>();
            }
            layerList.clear();
            SolGetAllResult result = new Gson().fromJson(s, SolGetAllResult.class);
            if (result.isSuccess()) {
                BottomListSheetBuilder mBuilder = new BottomListSheetBuilder(context)
                        .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                mPosition3 = position;
                                tv_layer.setText(layerList.get(mPosition3));
                                dialog.dismiss();
                            }
                        });
                switch (result.getData()) {
                    case 1:
                        layerList.add(getString(R.string.solenoid_str18));
                        mBuilder.addItem(getString(R.string.solenoid_str21));
                        mPosition3 = 0;
                        tv_layer.setText(layerList.get(mPosition3));
                        mBottomSheet3 = mBuilder.build();
                        mDownTimer.cancel();
                        mDownTimer.start();
                        break;
                    case 2:
                        layerList.add(getString(R.string.solenoid_str18));
                        layerList.add(getString(R.string.solenoid_str19));
                        mBuilder.addItem(getString(R.string.solenoid_str21));
                        mBuilder.addItem(getString(R.string.solenoid_str22));
                        mPosition3 = 0;
                        tv_layer.setText(layerList.get(mPosition3));
                        mBottomSheet3 = mBuilder.build();
                        mDownTimer.cancel();
                        mDownTimer.start();
                        break;
                    case 3:
                        layerList.add(getString(R.string.solenoid_str18));
                        layerList.add(getString(R.string.solenoid_str19));
                        layerList.add(getString(R.string.solenoid_str20));
                        mBuilder.addItem(getString(R.string.solenoid_str21));
                        mBuilder.addItem(getString(R.string.solenoid_str22));
                        mBuilder.addItem(getString(R.string.solenoid_str23));
                        mPosition3 = 0;
                        tv_layer.setText(layerList.get(mPosition3));
                        mBottomSheet3 = mBuilder.build();
                        mDownTimer.cancel();
                        mDownTimer.start();
                        break;
                    default:
                        mDialog.dismiss();
                        break;
                }
            } else mDialog.dismiss();
        } catch (Exception e) {
            mDialog.dismiss();
        }
    }

    private void parseResult1(String s) {
        try {
            if (greHouList == null) {
                greHouList = new ArrayList<>();
            }
            greHouList.clear();
            SolGetCtlListResult result = new Gson().fromJson(s, SolGetCtlListResult.class);
            if (result.isSuccess() && result.getData().size() > 0) {

                BottomListSheetBuilder mBuilder = new BottomListSheetBuilder(context, true)
                        .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                mPosition2 = position;
                                tv_area.setText(greHouList.get(mPosition2)[1]);
                                dialog.dismiss();
                                mDialog.show();
                                clearData();
                                mBottomSheet3 = null;
                                solenoidGetAllLayer();
                            }
                        });

                for (int i = 0; i < result.getData().size(); i++) {
                    if (result.getData().get(i).getIsUsed() == 1)
                        greHouList.add(new String[]{
                                result.getData().get(i).getGreenhouseID() + "",
                                result.getData().get(i).getGreenhouseName(),
                                result.getData().get(i).getDisplayNo()
                        });
                    mBuilder.addItem(greHouList.get(i)[1]);
                }

                mPosition2 = 0;
                mBuilder.setCheckedIndex(mPosition2);
                mBottomSheet2 = mBuilder.build();
                tv_area.setText(greHouList.get(mPosition2)[1]);
                solenoidGetAllLayer();
            } else {
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
                conTypeMark = 0;
                solenoidGetCtlList();
            } else {
                mDialog.dismiss();
            }
        } else {
            mDialog.dismiss();
            Toasts.showInfo(context, getString(R.string.request_error8));
        }
    }

    /**
     * 获取传感器土壤层数
     */
    private void solenoidGetAllLayer() {
        if (Data.userResult != null) {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"greenhouseID", greHouList.get(mPosition2)[0]});
            data.add(new String[]{"userID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"authCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("SolenoidGetAllLayer",
                    "/StringService/DeviceSet.asmx", data, 2, context);
        } else {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.request_error9));
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

    private void clearData() {
        et1.setText("");
        et2.setText("");
        et3.setText("");
        layerList = null;
        mBottomSheet3 = null;
        tv_layer.setText(getString(R.string.solenoid_str17));
        cb.setChecked(false);
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
