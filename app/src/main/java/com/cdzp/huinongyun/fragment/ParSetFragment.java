package com.cdzp.huinongyun.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.bean.HelioGreParam;
import com.cdzp.huinongyun.bean.SetAutoParam;
import com.cdzp.huinongyun.bean.SocketResult;
import com.cdzp.huinongyun.message.SocketMessage;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Operate;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.cdzp.huinongyun.util.Data.cGhList;
import static com.cdzp.huinongyun.util.Data.pGhList;

/**
 * 参数设置
 */
public class ParSetFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean isVisible, isInit = false;
    private QMUITipDialog mDialog;
    private int mPosition, mPosition1 = -1, mPosition2, conTypeMark;
    private QMUIBottomSheet mBottomSheet1, mBottomSheet2;
    private TextView tv_dev, tv_area;
    private String[] times;
    private CountDownTimer mDownTimer;
    private TextView aeraTwo1, aeraTwo2;
    private CheckBox wenduCB, guangzhaoCB, shiduCB;
    private EditText JWHLabel, JWLLable, SWHLabel, SWLLabel, ZGHLabel, ZGLLabel, BGHLabel, BGLLabel, CSHLabel, CSLLabel,
            JSHLabel, JSLLabel, JWHLabel_2, JWLLable_2, SWHLabel_2, SWLLabel_2, ZGHLabel_2, ZGLLabel_2, BGHLabel_2,
            BGLLabel_2, CSHLabel_2, CSLLabel_2, JSHLabel_2, JSLLabel_2, JCO2HLabel, JCO2LLabel, JCO2HLabel_2, JCO2LLabel_2,
            ZCO2HLabel, ZCO2LLabel, ZCO2HLabel_2, ZCO2LLabel_2;
    private SetAutoParam param;
    private HelioGreParam param1;

    private OptionsPickerView mOptions;
    private boolean isSunlight = false;//是否是日光温室

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_par_set, container, false);
            initView();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible) {
            registered();
            if (!isInit && view != null) {
                isInit = true;
//                mDialog.show();
                initData();
            }
        } else {
            EventBus.getDefault().unregister(this);
        }
    }

    private void initView() {
        context = getContext();
        if (Data.GhType != null) {
            switch (Data.GhType) {
                case "8":
                    isSunlight = true;
                    break;
                default:

                    break;
            }
        }
        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();

        tv_dev = view.findViewById(R.id.tv_fps_dev);

        tv_area = view.findViewById(R.id.tv_fps_area);

//      "唤醒", "黎明", "白天", "黄昏", "夜晚"
        times = new String[]{
                getString(R.string.parset_time1),
                getString(R.string.parset_time2),
                getString(R.string.parset_time3),
                getString(R.string.parset_time4),
                getString(R.string.parset_time5)
        };
        mPosition2 = 0;
        tv_area.setText(times[mPosition2]);

        aeraTwo1 = view.findViewById(R.id.aeraTwo1);
        aeraTwo2 = view.findViewById(R.id.aeraTwo2);

        wenduCB = view.findViewById(R.id.wenduCheckBox);
        guangzhaoCB = view.findViewById(R.id.guangzhaoCheckBox);
        shiduCB = view.findViewById(R.id.shiduCheckBox);

        JWHLabel = view.findViewById(R.id.JWHLabel);
        JWLLable = view.findViewById(R.id.JWLLable);
        SWHLabel = view.findViewById(R.id.SWHLabel);
        SWLLabel = view.findViewById(R.id.SWLLabel);

        ZGHLabel = view.findViewById(R.id.ZGHLabel);
        ZGLLabel = view.findViewById(R.id.ZGLLabel);
        BGHLabel = view.findViewById(R.id.BGHLabel);
        BGLLabel = view.findViewById(R.id.BGLLabel);

        CSHLabel = view.findViewById(R.id.CSHLabel);
        CSLLabel = view.findViewById(R.id.CSLLabel);
        JSHLabel = view.findViewById(R.id.JSHLabel);
        JSLLabel = view.findViewById(R.id.JSLLabel);

        JWHLabel_2 = view.findViewById(R.id.JWHLabel_2);
        JWLLable_2 = view.findViewById(R.id.JWLLable_2);
        SWHLabel_2 = view.findViewById(R.id.SWHLabel_2);
        SWLLabel_2 = view.findViewById(R.id.SWLLabel_2);

        ZGHLabel_2 = view.findViewById(R.id.ZGHLabel_2);
        ZGLLabel_2 = view.findViewById(R.id.ZGLLabel_2);
        BGHLabel_2 = view.findViewById(R.id.BGHLabel_2);
        BGLLabel_2 = view.findViewById(R.id.BGLLabel_2);

        CSHLabel_2 = view.findViewById(R.id.CSHLabel_2);
        CSLLabel_2 = view.findViewById(R.id.CSLLabel_2);
        JSHLabel_2 = view.findViewById(R.id.JSHLabel_2);
        JSLLabel_2 = view.findViewById(R.id.JSLLabel_2);

        JCO2HLabel = view.findViewById(R.id.JCO2HLabel);
        JCO2LLabel = view.findViewById(R.id.JCO2LLabel);
        JCO2HLabel_2 = view.findViewById(R.id.JCO2HLabel_2);
        JCO2LLabel_2 = view.findViewById(R.id.JCO2LLabel_2);

        ZCO2HLabel = view.findViewById(R.id.ZCO2HLabel);
        ZCO2LLabel = view.findViewById(R.id.ZCO2LLabel);
        ZCO2HLabel_2 = view.findViewById(R.id.ZCO2HLabel_2);
        ZCO2LLabel_2 = view.findViewById(R.id.ZCO2LLabel_2);

        mDownTimer = new CountDownTimer(9000, 2500) {
            @Override
            public void onTick(long millisUntilFinished) {
                getParam();
            }

            @Override
            public void onFinish() {
                mDialog.dismiss();
            }
        };

        if (isSunlight) {
            setVisGone2();
        }

        view.findViewById(R.id.ll_fps1).setOnClickListener(this);
        view.findViewById(R.id.ll_fps2).setOnClickListener(this);
        view.findViewById(R.id.btn_fps_set).setOnClickListener(this);
        view.findViewById(R.id.btn_fps_refresh).setOnClickListener(this);
        if (!isInit && isVisible) {
            isInit = true;
//            mDialog.show();
            initData();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SocketResult result) {
        conTypeMark = 0;
        if (result.isOK()) {
            switch (result.getLabel()) {
                case 156:
                    if (!isSunlight) {
                        mDownTimer.cancel();
                        if (result.getmResult() instanceof SetAutoParam) {
                            param = (SetAutoParam) result.getmResult();
                            notifyDataSocket(param);
                        } else Toasts.showInfo(context, getString(R.string.request_error7));
                        mDialog.dismiss();
                    }
                    break;
                case 158:
                    if (!isSunlight) {
                        Toasts.showSuccess(context, getString(R.string.parsetstr));
                    }
                    break;
                case 525:
                    if (isSunlight) {
                        mDownTimer.cancel();
                        if (result.getmResult() instanceof HelioGreParam) {
                            param1 = (HelioGreParam) result.getmResult();
                            notifyDataSocket1(param1);
                        } else Toasts.showInfo(context, getString(R.string.request_error7));
                        mDialog.dismiss();
                    }
                    break;
                case 517:
                    if (isSunlight) {
                        Toasts.showSuccess(context, getString(R.string.parsetstr));
                    }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 666:
                    parseGreenhouse();
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

    private void parseGreenhouse() {
        if (isSunlight) {
            initOptions();
            if (pGhList.size() > 0 && cGhList.size() > 0 && cGhList.get(0).size() > 0) {
                mPosition = 0;
                mPosition1 = 0;
                tv_dev.setText(pGhList.get(mPosition)[1].replace(getString(R.string.parsetstr10), "") + " " +
                        cGhList.get(mPosition).get(mPosition1)[1].replace(getString(R.string.parsetstr10), ""));
                if (pGhList.get(mPosition)[6].equals("1") && cGhList.get(mPosition).get(mPosition1)[6].equals("1")) {
                    mDialog.show();
                    mDownTimer.cancel();
                    mDownTimer.start();
                } else {
                    mDialog.dismiss();
                    clearEditText();
                    Toasts.showInfo(context, getString(R.string.parsetstr11));
                }
            } else mDialog.dismiss();
        } else {
            if (pGhList != null && pGhList.size() > 0) {
                mPosition1 = 0;
                tv_dev.setText(pGhList.get(mPosition1)[1]);
                getParam();
            } else {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.request_error8));
            }
        }
    }

    private void initData() {
        if (isSunlight) {
            if (pGhList == null || cGhList == null) {
                getGreenHouseSel();
            } else {
                initOptions();
                if (pGhList.size() > 0 && cGhList.size() > 0 && cGhList.get(0).size() > 0) {
                    mPosition = 0;
                    mPosition1 = 0;
                    tv_dev.setText(pGhList.get(mPosition)[1].replace(getString(R.string.parsetstr10), "") + " " +
                            cGhList.get(mPosition).get(mPosition1)[1].replace(getString(R.string.parsetstr10), ""));
                    if (pGhList.get(mPosition)[6].equals("1") && cGhList.get(mPosition).get(mPosition1)[6].equals("1")) {
                        mDialog.show();
                        mDownTimer.cancel();
                        mDownTimer.start();
                    } else {
                        clearEditText();
                        Toasts.showInfo(context, getString(R.string.parsetstr11));
                    }
                }
            }
        } else {
            if (pGhList != null && pGhList.size() > 0) {
                mPosition1 = 0;
                tv_dev.setText(pGhList.get(mPosition1)[1]);
                getParam();
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
                mPosition = options1;
                mPosition1 = options2;
                tv_dev.setText(pGhList.get(mPosition)[1].replace(getString(R.string.parsetstr10), "") + " " +
                        cGhList.get(mPosition).get(mPosition1)[1].replace(getString(R.string.parsetstr10), ""));
                if (pGhList.get(mPosition)[6].equals("1") && cGhList.get(mPosition).get(mPosition1)[6].equals("1")) {
                    mDialog.show();
                    mDownTimer.cancel();
                    mDownTimer.start();
                } else {
                    clearEditText();
                    Toasts.showInfo(context, getString(R.string.parsetstr11));
                }

            }
        }).build();
        mOptions.setPicker(gateway, terminal);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fps1:
                if (isSunlight) {
                    if (mOptions != null) {
                        mOptions.show();
                    } else getGreenHouseSel();
                } else {
                    showBottomSheet1();
                }
                break;
            case R.id.ll_fps2:
                if (isSunlight) {
                    if (pGhList != null && pGhList.size() > mPosition)
                        showBottomSheet2();
                } else {
                    if (pGhList != null && pGhList.size() > mPosition1)
                        showBottomSheet2();
                }
                break;
            case R.id.btn_fps_set:
                if (isSunlight) {
                    if (param1 != null) {
                        if (tv_dev.getText().length() > 0 && JWHLabel.getText().length() > 0 &&
                                JWLLable.getText().length() > 0 && SWHLabel.getText().length() > 0 &&
                                SWLLabel.getText().length() > 0 && ZGHLabel.getText().length() > 0 && ZGLLabel.getText().length() > 0 &&
                                BGHLabel.getText().length() > 0 && BGLLabel.getText().length() > 0 && CSHLabel.getText().length() > 0 &&
                                CSLLabel.getText().length() > 0 && JSHLabel.getText().length() > 0 && JSLLabel.getText().length() > 0 &&
                                JCO2HLabel.getText().length() > 0 && JCO2LLabel.getText().length() > 0 &&
                                ZCO2HLabel.getText().length() > 0 && ZCO2LLabel.getText().length() > 0) {
                            setParam1();
                        } else Toasts.showInfo(context, getString(R.string.request_error6));
                    } else Toasts.showInfo(context, getString(R.string.apsf_str1));
                } else {
                    if (param != null) {
                        if (param.getAutoParamZoneNumTemp() == 1) {
                            if (tv_dev.getText().length() > 0 && JWHLabel.getText().length() > 0 &&
                                    JWLLable.getText().length() > 0 && SWHLabel.getText().length() > 0 &&
                                    SWLLabel.getText().length() > 0 && ZGHLabel.getText().length() > 0 && ZGLLabel.getText().length() > 0 &&
                                    BGHLabel.getText().length() > 0 && BGLLabel.getText().length() > 0 && CSHLabel.getText().length() > 0 &&
                                    CSLLabel.getText().length() > 0 && JSHLabel.getText().length() > 0 && JSLLabel.getText().length() > 0 &&
                                    JCO2HLabel.getText().length() > 0 && JCO2LLabel.getText().length() > 0 && ZCO2HLabel.getText().length() > 0
                                    && ZCO2LLabel.getText().length() > 0) {
                                setParam();
                            } else Toasts.showInfo(context, getString(R.string.request_error6));
                        } else {
                            if (tv_dev.getText().length() > 0 && JWHLabel.getText().length() > 0 &&
                                    JWLLable.getText().length() > 0 && SWHLabel.getText().length() > 0 &&
                                    SWLLabel.getText().length() > 0 && ZGHLabel.getText().length() > 0 && ZGLLabel.getText().length() > 0 &&
                                    BGHLabel.getText().length() > 0 && BGLLabel.getText().length() > 0 && CSHLabel.getText().length() > 0 &&
                                    CSLLabel.getText().length() > 0 && JSHLabel.getText().length() > 0 && JSLLabel.getText().length() > 0 &&
                                    JWHLabel_2.getText().length() > 0 && JWLLable_2.getText().length() > 0 && SWHLabel_2.getText().length() > 0 &&
                                    SWLLabel_2.getText().length() > 0 && ZGHLabel_2.getText().length() > 0 && ZGLLabel_2.getText().length() > 0 &&
                                    BGHLabel_2.getText().length() > 0 && BGLLabel_2.getText().length() > 0 && CSHLabel_2.getText().length() > 0 &&
                                    CSLLabel_2.getText().length() > 0 && JSHLabel_2.getText().length() > 0 && JSLLabel_2.getText().length() > 0 &&
                                    JCO2HLabel.getText().length() > 0 && JCO2LLabel.getText().length() > 0 && JCO2HLabel_2.getText().length() > 0 &&
                                    JCO2LLabel_2.getText().length() > 0 && ZCO2HLabel.getText().length() > 0 && ZCO2LLabel.getText().length() > 0 &&
                                    ZCO2HLabel_2.getText().length() > 0 && ZCO2LLabel_2.getText().length() > 0) {
                                setParam();
                            } else Toasts.showInfo(context, getString(R.string.request_error6));
                        }
                    } else Toasts.showInfo(context, getString(R.string.apsf_str1));
                }
                break;
            case R.id.btn_fps_refresh:
                if (tv_dev.getText().length() > 0) {
                    getParam();
                } else Toasts.showInfo(context, getString(R.string.warn_str12));
                break;

            default:

                break;
        }
    }

    private void getParam() {
        clearEditText();
        switch (mPosition2) {
            case 0:
                if (isSunlight) {
                    socketRequest(520, null);
                } else {
                    socketRequest(162, null);
                }
                break;
            case 1:
                if (isSunlight) {
                    socketRequest(521, null);
                } else {
                    socketRequest(163, null);
                }
                break;
            case 2:
                if (isSunlight) {
                    socketRequest(522, null);
                } else {
                    socketRequest(164, null);
                }
                break;
            case 3:
                if (isSunlight) {
                    socketRequest(523, null);
                } else {
                    socketRequest(165, null);
                }
                break;
            case 4:
                if (isSunlight) {
                    socketRequest(524, null);
                } else {
                    socketRequest(166, null);
                }
                break;
            case 5://特殊定制
                if (isSunlight) {

                } else {
                    socketRequest(173, null);
                }
                break;
            default:

                break;
        }
    }

    private void setParam1() {
        try {
            if (wenduCB.isChecked()) {
                param1.setWenduCtlMode((byte) 1);
            } else param1.setWenduCtlMode((byte) 0);

            if (guangzhaoCB.isChecked()) {
                param1.setGuangzhaoduCtlMode((byte) 1);
            } else param1.setGuangzhaoduCtlMode((byte) 0);

            if (shiduCB.isChecked()) {
                param1.setShiduCtlMode((byte) 1);
            } else param1.setShiduCtlMode((byte) 0);

            param1.getWdInfo().setFallTopValue(Float.parseFloat((JWHLabel.getText().toString())));
            param1.getWdInfo().setFallLowValue(Float.parseFloat((JWLLable.getText().toString())));
            param1.getWdInfo().setRaiseTopValue(Float.parseFloat((SWHLabel.getText().toString())));
            param1.getWdInfo().setRaiseLowValue(Float.parseFloat((SWLLabel.getText().toString())));

            param1.getGzInfo().setFallTopValue(Float.parseFloat((ZGHLabel.getText().toString())));
            param1.getGzInfo().setFallLowValue(Float.parseFloat((ZGLLabel.getText().toString())));
            param1.getGzInfo().setRaiseTopValue(Float.parseFloat((BGHLabel.getText().toString())));
            param1.getGzInfo().setRaiseLowValue(Float.parseFloat((BGLLabel.getText().toString())));

            param1.getSdInfo().setFallTopValue(Float.parseFloat((CSHLabel.getText().toString())));
            param1.getSdInfo().setFallLowValue(Float.parseFloat((CSLLabel.getText().toString())));
            param1.getSdInfo().setRaiseTopValue(Float.parseFloat((JSHLabel.getText().toString())));
            param1.getSdInfo().setRaiseLowValue(Float.parseFloat((JSLLabel.getText().toString())));

            param1.getBlInfo().setFallTopValue(Float.parseFloat((JCO2HLabel.getText().toString())));
            param1.getBlInfo().setFallLowValue(Float.parseFloat((JCO2LLabel.getText().toString())));
            param1.getBlInfo().setRaiseTopValue(Float.parseFloat((ZCO2HLabel.getText().toString())));
            param1.getBlInfo().setRaiseLowValue(Float.parseFloat((ZCO2LLabel.getText().toString())));

            socketRequest(516, param1);

        } catch (Exception e) {
            Toasts.showInfo(context, getString(R.string.apsf_str2));
        }

    }

    private void setParam() {
        try {
            if (wenduCB.isChecked()) {
                param.setWenduCtlMode((byte) 1);
            } else param.setWenduCtlMode((byte) 0);

            if (guangzhaoCB.isChecked()) {
                param.setGuangzhaoduCtlMode((byte) 1);
            } else param.setGuangzhaoduCtlMode((byte) 0);

            if (shiduCB.isChecked()) {
                param.setShiduCtlMode((byte) 1);
            } else param.setShiduCtlMode((byte) 0);

            param.getWenduAutoParam().setFallTopValue(Float.parseFloat((JWHLabel.getText().toString())));
            param.getWenduAutoParam().setFallLowValue(Float.parseFloat((JWLLable.getText().toString())));
            param.getWenduAutoParam().setRaiseTopValue(Float.parseFloat((SWHLabel.getText().toString())));
            param.getWenduAutoParam().setRaiseLowValue(Float.parseFloat((SWLLabel.getText().toString())));

            param.getGuangzhaoAutoParam().setFallTopValue(Float.parseFloat((ZGHLabel.getText().toString())));
            param.getGuangzhaoAutoParam().setFallLowValue(Float.parseFloat((ZGLLabel.getText().toString())));
            param.getGuangzhaoAutoParam().setRaiseTopValue(Float.parseFloat((BGHLabel.getText().toString())));
            param.getGuangzhaoAutoParam().setRaiseLowValue(Float.parseFloat((BGLLabel.getText().toString())));

            param.getShiduAutoParam().setFallTopValue(Float.parseFloat((CSHLabel.getText().toString())));
            param.getShiduAutoParam().setFallLowValue(Float.parseFloat((CSLLabel.getText().toString())));
            param.getShiduAutoParam().setRaiseTopValue(Float.parseFloat((JSHLabel.getText().toString())));
            param.getShiduAutoParam().setRaiseLowValue(Float.parseFloat((JSLLabel.getText().toString())));

            param.getCO2AutoParam().setFallTopValue(Float.parseFloat((JCO2HLabel.getText().toString())));
            param.getCO2AutoParam().setFallLowValue(Float.parseFloat((JCO2LLabel.getText().toString())));
            param.getCO2AutoParam().setRaiseTopValue(Float.parseFloat((ZCO2HLabel.getText().toString())));
            param.getCO2AutoParam().setRaiseLowValue(Float.parseFloat((ZCO2LLabel.getText().toString())));

            if (param.getAutoParamZoneNumTemp() == 2) {
                param.getWenduAutoParam().setFallTopValue_2(Float.parseFloat((JWHLabel_2.getText().toString())));
                param.getWenduAutoParam().setFallLowValue_2(Float.parseFloat((JWLLable_2.getText().toString())));
                param.getWenduAutoParam().setRaiseTopValue_2(Float.parseFloat((SWHLabel_2.getText().toString())));
                param.getWenduAutoParam().setRaiseLowValue_2(Float.parseFloat((SWLLabel_2.getText().toString())));

                param.getGuangzhaoAutoParam().setFallTopValue_2(Float.parseFloat((ZGHLabel_2.getText().toString())));
                param.getGuangzhaoAutoParam().setFallLowValue_2(Float.parseFloat((ZGLLabel_2.getText().toString())));
                param.getGuangzhaoAutoParam().setRaiseTopValue_2(Float.parseFloat((BGHLabel_2.getText().toString())));
                param.getGuangzhaoAutoParam().setRaiseLowValue_2(Float.parseFloat((BGLLabel_2.getText().toString())));

                param.getShiduAutoParam().setFallTopValue_2(Float.parseFloat((CSHLabel_2.getText().toString())));
                param.getShiduAutoParam().setFallLowValue_2(Float.parseFloat((CSLLabel_2.getText().toString())));
                param.getShiduAutoParam().setRaiseTopValue_2(Float.parseFloat((JSHLabel_2.getText().toString())));
                param.getShiduAutoParam().setRaiseLowValue_2(Float.parseFloat((JSLLabel_2.getText().toString())));

                param.getCO2AutoParam().setFallTopValue_2(Float.parseFloat((JCO2HLabel_2.getText().toString())));
                param.getCO2AutoParam().setFallLowValue_2(Float.parseFloat((JCO2LLabel_2.getText().toString())));
                param.getCO2AutoParam().setRaiseTopValue_2(Float.parseFloat((ZCO2HLabel_2.getText().toString())));
                param.getCO2AutoParam().setRaiseLowValue_2(Float.parseFloat((ZCO2LLabel_2.getText().toString())));
            }

            socketRequest(158, param);
        } catch (Exception e) {
            Toasts.showInfo(context, getString(R.string.apsf_str2));
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
                                    mDownTimer.cancel();
                                    mPosition1 = position;
                                    tv_dev.setText(pGhList.get(mPosition1)[1]);
                                    clearEditText();
                                    dialog.dismiss();
                                    if (pGhList.get(mPosition1)[6].equals("1")) {
                                        mDialog.show();
                                        mDownTimer.start();
                                    } else {
                                        Toasts.showInfo(context, getString(R.string.parsetstr11));
                                    }
                                }
                            });
                    for (int i = 0; i < pGhList.size(); i++) {
                        String str = pGhList.get(i)[1];
                        if (!str.contains(getString(R.string.dev_str2)))
                            mBuilder.addItem(str);
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

    private void showBottomSheet2() {
        if (mBottomSheet2 == null) {
            BottomListSheetBuilder mBuilder = new BottomListSheetBuilder(context, true)
                    .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                        @Override
                        public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                            mDownTimer.cancel();
                            mPosition2 = position;
                            tv_area.setText(times[mPosition2]);
                            String status;
                            if (isSunlight) {
                                status = pGhList.get(mPosition)[6];
                            } else {
                                status = pGhList.get(mPosition1)[6];
                            }
                            clearEditText();
                            dialog.dismiss();
                            if (status.equals("1")) {
                                mDialog.show();
                                mDownTimer.start();
                            }
                        }
                    });
            for (int i = 0; i < times.length; i++) {
                mBuilder.addItem(times[i]);
            }
            mBuilder.setCheckedIndex(mPosition2);
            mBottomSheet2 = mBuilder.build();
        }
        mBottomSheet2.show();
    }

    private void notifyDataSocket1(HelioGreParam setAutoParam) {
        JWHLabel.setText(setAutoParam.getWdInfo().getFallTopValue() + "");
        JWLLable.setText(setAutoParam.getWdInfo().getFallLowValue() + "");
        SWHLabel.setText(setAutoParam.getWdInfo().getRaiseTopValue() + "");
        SWLLabel.setText(setAutoParam.getWdInfo().getRaiseLowValue() + "");

        ZGHLabel.setText(setAutoParam.getGzInfo().getFallTopValue() + "");
        ZGLLabel.setText(setAutoParam.getGzInfo().getFallLowValue() + "");
        BGHLabel.setText(setAutoParam.getGzInfo().getRaiseTopValue() + "");
        BGLLabel.setText(setAutoParam.getGzInfo().getRaiseLowValue() + "");

        CSHLabel.setText(setAutoParam.getSdInfo().getFallTopValue() + "");
        CSLLabel.setText(setAutoParam.getSdInfo().getFallLowValue() + "");
        JSHLabel.setText(setAutoParam.getSdInfo().getRaiseTopValue() + "");
        JSLLabel.setText(setAutoParam.getSdInfo().getRaiseLowValue() + "");

        JCO2HLabel.setText(setAutoParam.getBlInfo().getFallTopValue() + "");
        JCO2LLabel.setText(setAutoParam.getBlInfo().getFallLowValue() + "");
        ZCO2HLabel.setText(setAutoParam.getBlInfo().getRaiseTopValue() + "");
        ZCO2LLabel.setText(setAutoParam.getBlInfo().getRaiseLowValue() + "");

        if (setAutoParam.getWenduCtlMode() == 1) {
            wenduCB.setChecked(true);
        } else wenduCB.setChecked(false);
        if (setAutoParam.getGuangzhaoduCtlMode() == 1) {
            guangzhaoCB.setChecked(true);
        } else guangzhaoCB.setChecked(false);
        if (setAutoParam.getShiduCtlMode() == 1) {
            shiduCB.setChecked(true);
        } else shiduCB.setChecked(false);

        Toasts.showSuccess(context, getString(R.string.parsetstr1));

    }

    private void notifyDataSocket(SetAutoParam setAutoParam) {
        JWHLabel.setText(setAutoParam.getWenduAutoParam().getFallTopValue() + "");
        JWLLable.setText(setAutoParam.getWenduAutoParam().getFallLowValue() + "");
        SWHLabel.setText(setAutoParam.getWenduAutoParam().getRaiseTopValue() + "");
        SWLLabel.setText(setAutoParam.getWenduAutoParam().getRaiseLowValue() + "");

        ZGHLabel.setText(setAutoParam.getGuangzhaoAutoParam().getFallTopValue() + "");
        ZGLLabel.setText(setAutoParam.getGuangzhaoAutoParam().getFallLowValue() + "");
        BGHLabel.setText(setAutoParam.getGuangzhaoAutoParam().getRaiseTopValue() + "");
        BGLLabel.setText(setAutoParam.getGuangzhaoAutoParam().getRaiseLowValue() + "");

        CSHLabel.setText(setAutoParam.getShiduAutoParam().getFallTopValue() + "");
        CSLLabel.setText(setAutoParam.getShiduAutoParam().getFallLowValue() + "");
        JSHLabel.setText(setAutoParam.getShiduAutoParam().getRaiseTopValue() + "");
        JSLLabel.setText(setAutoParam.getShiduAutoParam().getRaiseLowValue() + "");

        JCO2HLabel.setText(setAutoParam.getCO2AutoParam().getFallTopValue() + "");
        JCO2LLabel.setText(setAutoParam.getCO2AutoParam().getFallLowValue() + "");
        ZCO2HLabel.setText(setAutoParam.getCO2AutoParam().getRaiseTopValue() + "");
        ZCO2LLabel.setText(setAutoParam.getCO2AutoParam().getRaiseLowValue() + "");

        switch (setAutoParam.getAutoParamZoneNumTemp()) {
            case 1:
                setVisGone2();
                break;
            case 2:
                aeraTwo1.setVisibility(View.VISIBLE);
                aeraTwo2.setVisibility(View.VISIBLE);

                JWHLabel_2.setVisibility(View.VISIBLE);
                JWLLable_2.setVisibility(View.VISIBLE);
                SWHLabel_2.setVisibility(View.VISIBLE);
                SWLLabel_2.setVisibility(View.VISIBLE);

                ZGHLabel_2.setVisibility(View.VISIBLE);
                ZGLLabel_2.setVisibility(View.VISIBLE);
                BGHLabel_2.setVisibility(View.VISIBLE);
                BGLLabel_2.setVisibility(View.VISIBLE);

                CSHLabel_2.setVisibility(View.VISIBLE);
                CSLLabel_2.setVisibility(View.VISIBLE);
                JSHLabel_2.setVisibility(View.VISIBLE);
                JSLLabel_2.setVisibility(View.VISIBLE);

                JCO2HLabel_2.setVisibility(View.VISIBLE);
                JCO2LLabel_2.setVisibility(View.VISIBLE);
                ZCO2HLabel_2.setVisibility(View.VISIBLE);
                ZCO2LLabel_2.setVisibility(View.VISIBLE);

                JWHLabel_2.setText(setAutoParam.getWenduAutoParam().getFallTopValue_2() + "");
                JWLLable_2.setText(setAutoParam.getWenduAutoParam().getFallLowValue_2() + "");
                SWHLabel_2.setText(setAutoParam.getWenduAutoParam().getRaiseTopValue_2() + "");
                SWLLabel_2.setText(setAutoParam.getWenduAutoParam().getRaiseLowValue_2() + "");

                ZGHLabel_2.setText(setAutoParam.getGuangzhaoAutoParam().getFallTopValue_2() + "");
                ZGLLabel_2.setText(setAutoParam.getGuangzhaoAutoParam().getFallLowValue_2() + "");
                BGHLabel_2.setText(setAutoParam.getGuangzhaoAutoParam().getRaiseTopValue_2() + "");
                BGLLabel_2.setText(setAutoParam.getGuangzhaoAutoParam().getRaiseLowValue_2() + "");

                CSHLabel_2.setText(setAutoParam.getShiduAutoParam().getFallTopValue_2() + "");
                CSLLabel_2.setText(setAutoParam.getShiduAutoParam().getFallLowValue_2() + "");
                JSHLabel_2.setText(setAutoParam.getShiduAutoParam().getRaiseTopValue_2() + "");
                JSLLabel_2.setText(setAutoParam.getShiduAutoParam().getRaiseLowValue_2() + "");

                JCO2HLabel_2.setText(setAutoParam.getCO2AutoParam().getFallTopValue_2() + "");
                JCO2LLabel_2.setText(setAutoParam.getCO2AutoParam().getFallLowValue_2() + "");
                ZCO2HLabel_2.setText(setAutoParam.getCO2AutoParam().getRaiseTopValue_2() + "");
                ZCO2LLabel_2.setText(setAutoParam.getCO2AutoParam().getRaiseLowValue_2() + "");
                break;
            default:

                break;
        }

        if (setAutoParam.getWenduCtlMode() == 1) {
            wenduCB.setChecked(true);
        } else wenduCB.setChecked(false);
        if (setAutoParam.getGuangzhaoduCtlMode() == 1) {
            guangzhaoCB.setChecked(true);
        } else guangzhaoCB.setChecked(false);
        if (setAutoParam.getShiduCtlMode() == 1) {
            shiduCB.setChecked(true);
        } else shiduCB.setChecked(false);

        Toasts.showSuccess(context, getString(R.string.parsetstr1));
    }

    private void setVisGone2() {
        aeraTwo1.setVisibility(View.GONE);
        aeraTwo2.setVisibility(View.GONE);

        JWHLabel_2.setVisibility(View.GONE);
        JWLLable_2.setVisibility(View.GONE);
        SWHLabel_2.setVisibility(View.GONE);
        SWLLabel_2.setVisibility(View.GONE);

        ZGHLabel_2.setVisibility(View.GONE);
        ZGLLabel_2.setVisibility(View.GONE);
        BGHLabel_2.setVisibility(View.GONE);
        BGLLabel_2.setVisibility(View.GONE);

        CSHLabel_2.setVisibility(View.GONE);
        CSLLabel_2.setVisibility(View.GONE);
        JSHLabel_2.setVisibility(View.GONE);
        JSLLabel_2.setVisibility(View.GONE);

        JCO2HLabel_2.setVisibility(View.GONE);
        JCO2LLabel_2.setVisibility(View.GONE);
        ZCO2HLabel_2.setVisibility(View.GONE);
        ZCO2LLabel_2.setVisibility(View.GONE);
    }

    private void socketRequest(int commandType, Object object) {
        if (Data.userResult != null) {
            SocketMessage message = new SocketMessage();
            if (isSunlight) {
                if (pGhList != null && cGhList != null && cGhList.size() > mPosition &&
                        cGhList.get(mPosition).size() > mPosition1) {
                    message.setRequestType(506);
                    message.setmReceiver(Data.pGhList.get(mPosition)[2]);
                    message.setPlayNo(cGhList.get(mPosition).get(mPosition1)[2]);
                    if (Data.pGhList.get(mPosition)[3].equals("1") && conTypeMark < 5) {
                        message.setIp(Data.pGhList.get(mPosition)[4]);
                        message.setPort(Integer.parseInt(Data.pGhList.get(mPosition)[5]));
                        conTypeMark++;
                    } else {
                        message.setIp(Data.SERVER_IP);
                        message.setPort(Data.SERVER_PORT);
                    }
                } else return;
            } else {
                if (mPosition1 >= 0 && Data.pGhList != null && Data.pGhList.size() > mPosition1) {
                    message.setRequestType(3);
                    message.setmReceiver(Data.pGhList.get(mPosition1)[2]);
                    if (Data.pGhList.get(mPosition1)[3].equals("1") && conTypeMark < 5) {
                        message.setIp(Data.pGhList.get(mPosition1)[4]);
                        message.setPort(Integer.parseInt(Data.pGhList.get(mPosition1)[5]));
                        conTypeMark++;
                    } else {
                        message.setIp(Data.SERVER_IP);
                        message.setPort(Data.SERVER_PORT);
                    }
                } else return;
            }
            message.setZP_KEY_CLIENT(987656789);
            message.setmSender(Data.userResult.getData().get(0).getUserName());
            message.setCommandType(commandType);
            message.setResult(object);
            Operate.getDefault().getSender().messageRequest(message);
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

    private void clearEditText() {
        wenduCB.setChecked(false);
        shiduCB.setChecked(false);
        guangzhaoCB.setChecked(false);
        JWHLabel.setText("");
        JWLLable.setText("");
        SWHLabel.setText("");
        SWLLabel.setText("");
        ZGHLabel.setText("");
        ZGLLabel.setText("");
        BGHLabel.setText("");
        BGLLabel.setText("");
        CSHLabel.setText("");
        CSLLabel.setText("");
        JSHLabel.setText("");
        JSLLabel.setText("");
        JWHLabel_2.setText("");
        JWLLable_2.setText("");
        SWHLabel_2.setText("");
        SWLLabel_2.setText("");
        ZGHLabel_2.setText("");
        ZGLLabel_2.setText("");
        BGHLabel_2.setText("");
        BGLLabel_2.setText("");
        CSHLabel_2.setText("");
        CSLLabel_2.setText("");
        JSHLabel_2.setText("");
        JSLLabel_2.setText("");
        JCO2HLabel.setText("");
        JCO2LLabel.setText("");
        JCO2HLabel_2.setText("");
        JCO2LLabel_2.setText("");
        ZCO2HLabel.setText("");
        ZCO2LLabel.setText("");
        ZCO2HLabel_2.setText("");
        ZCO2LLabel_2.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible)
            registered();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void registered() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }
}
