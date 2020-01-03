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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
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

import static com.cdzp.huinongyun.util.Data.pGhList;

/**
 * 水产参数设置
 */
public class AquProSetFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean isVisible, isInit = false;
    private TextView tv_dev;
    private QMUITipDialog mDialog;
    private int mPosition, conTypeMark;
    private CountDownTimer mDownTimer;
    private SetAutoParam param;
    private CheckBox cb_ffs1, cb_ffs2;
    private EditText et_RY1, et_RY2, et_SW1, et_SW2, et_PH1, et_PH2, et_EC1, et_EC2, et_time11, et_time12,
            et_time21, et_time22, et_time31, et_time32, et_time41, et_time42;
    private QMUIBottomSheet mBottomSheet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_aqu_pro_set, container, false);
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
                registered();
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
        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();

        tv_dev = view.findViewById(R.id.tv_faps_dev);
        cb_ffs1 = view.findViewById(R.id.cb_faps1);
        cb_ffs2 = view.findViewById(R.id.cb_faps2);
        et_RY1 = view.findViewById(R.id.et_RY1);
        et_RY2 = view.findViewById(R.id.et_RY2);
        et_SW1 = view.findViewById(R.id.et_SW1);
        et_SW2 = view.findViewById(R.id.et_SW2);
        et_PH1 = view.findViewById(R.id.et_PH1);
        et_PH2 = view.findViewById(R.id.et_PH2);
        et_EC1 = view.findViewById(R.id.et_EC1);
        et_EC2 = view.findViewById(R.id.et_EC2);
        et_time11 = view.findViewById(R.id.et_faps_time11);
        et_time12 = view.findViewById(R.id.et_faps_time12);
        et_time21 = view.findViewById(R.id.et_faps_time21);
        et_time22 = view.findViewById(R.id.et_faps_time22);
        et_time31 = view.findViewById(R.id.et_faps_time31);
        et_time32 = view.findViewById(R.id.et_faps_time32);
        et_time41 = view.findViewById(R.id.et_faps_time41);
        et_time42 = view.findViewById(R.id.et_faps_time42);

        cb_ffs1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (cb_ffs2.isChecked()) cb_ffs2.setChecked(false);
                } else {
                    if (!cb_ffs2.isChecked()) cb_ffs2.setChecked(true);
                }
            }
        });
        cb_ffs2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (cb_ffs1.isChecked()) cb_ffs1.setChecked(false);
                } else {
                    if (!cb_ffs1.isChecked()) cb_ffs1.setChecked(true);
                }
            }
        });

        mDownTimer = new CountDownTimer(9000, 2500) {
            @Override
            public void onTick(long millisUntilFinished) {
                socketRequest(162, null);
            }

            @Override
            public void onFinish() {
                mDialog.dismiss();
            }
        };

        view.findViewById(R.id.ll_faps).setOnClickListener(this);
        view.findViewById(R.id.btn_faps_set).setOnClickListener(this);
        view.findViewById(R.id.btn_faps_refresh).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_faps:
                showBottomSheet();
                break;
            case R.id.btn_faps_set:
                if (tv_dev.getText().length() > 0 && et_RY1.getText().length() > 0 && et_RY2.getText().length() > 0 &&
                        et_SW1.getText().length() > 0 && et_SW2.getText().length() > 0 &&
                        et_PH1.getText().length() > 0 && et_PH2.getText().length() > 0 &&
                        et_time11.getText().length() > 0 && et_time12.getText().length() > 0 &&
                        et_time21.getText().length() > 0 && et_time22.getText().length() > 0 &&
                        et_time31.getText().length() > 0 && et_time32.getText().length() > 0 &&
                        et_time41.getText().length() > 0 && et_time42.getText().length() > 0) {
                    setParam();
                } else Toasts.showInfo(context, getString(R.string.request_error6));
                break;
            case R.id.btn_faps_refresh:
                if (tv_dev.getText().length() > 0) {
                    if (pGhList.get(mPosition)[6].equals("1")) {
                        clearEditText();
                        mDialog.show();
                        mDownTimer.start();
                    } else
                        Toasts.showInfo(context, getString(R.string.parsetstr11));
                } else Toasts.showInfo(context, getString(R.string.warn_str12));
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SocketResult result) {
        conTypeMark = 0;
        if (result.isOK()) {
            switch (result.getLabel()) {
                case 156://获取成功
                    mDownTimer.cancel();
                    if (result.getmResult() instanceof SetAutoParam) {
                        param = (SetAutoParam) result.getmResult();
                        notifyDataSocket();
                    } else Toasts.showInfo(context, getString(R.string.request_error7));
                    mDialog.dismiss();
                    break;
                case 158:
                case 161:
                    Toasts.showSuccess(context, getString(R.string.parsetstr));
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
        if (pGhList != null) {
            if (pGhList.size() > 0) {
                mPosition = 0;
                tv_dev.setText(pGhList.get(mPosition)[1]);
                if (pGhList.get(mPosition)[6].equals("1")) {
                    mDialog.show();
                    mDownTimer.cancel();
                    mDownTimer.start();
                }
            } else Toasts.showInfo(context, getString(R.string.request_error8));
        } else getGreenHouseSel();
    }

    private void showBottomSheet() {
        if (pGhList != null) {
            if (pGhList.size() > 0) {
                if (mBottomSheet == null) {
                    BottomListSheetBuilder mBuilder = new BottomListSheetBuilder(context, true)
                            .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                                @Override
                                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                    mDownTimer.cancel();
                                    mPosition = position;
                                    tv_dev.setText(pGhList.get(mPosition)[1]);
                                    clearEditText();
                                    dialog.dismiss();
                                    if (pGhList.get(mPosition)[6].equals("1")) {
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
                    mBuilder.setCheckedIndex(mPosition);
                    mBottomSheet = mBuilder.build();
                }
                mBottomSheet.show();
            } else Toasts.showInfo(context, getString(R.string.request_error8));
        } else {
            getGreenHouseSel();
        }
    }

    private void setParam() {
        try {
            if (param != null) {
                param.getWenduAutoParam().setFallTopValue(Float.parseFloat(et_RY1.getText().toString()));
                param.getWenduAutoParam().setFallLowValue(Float.parseFloat(et_RY2.getText().toString()));
                param.getWenduAutoParam().setRaiseTopValue(Float.parseFloat(et_SW1.getText().toString()));
                param.getWenduAutoParam().setRaiseLowValue(Float.parseFloat(et_SW2.getText().toString()));
                param.getWenduAutoParam().setFallTopValue_2(Float.parseFloat(et_PH1.getText().toString()));
                param.getWenduAutoParam().setFallLowValue_2(Float.parseFloat(et_PH2.getText().toString()));
                param.getWenduAutoParam().setRaiseTopValue_2(Float.parseFloat(et_EC1.getText().toString()));
                param.getWenduAutoParam().setRaiseLowValue_2(Float.parseFloat(et_EC2.getText().toString()));
                param.getWenduAutoParam().setFallTopValue(Float.parseFloat(et_time11.getText().toString()));
                param.getWenduAutoParam().setFallLowValue(Float.parseFloat(et_time12.getText().toString()));
                param.getWenduAutoParam().setRaiseTopValue(Float.parseFloat(et_time21.getText().toString()));
                param.getWenduAutoParam().setRaiseLowValue(Float.parseFloat(et_time22.getText().toString()));
                param.getWenduAutoParam().setFallTopValue_2(Float.parseFloat(et_time31.getText().toString()));
                param.getWenduAutoParam().setFallLowValue_2(Float.parseFloat(et_time32.getText().toString()));
                param.getWenduAutoParam().setRaiseTopValue_2(Float.parseFloat(et_time41.getText().toString()));
                param.getWenduAutoParam().setRaiseLowValue_2(Float.parseFloat(et_time42.getText().toString()));
                if (cb_ffs1.isChecked()) {
                    param.setUse(true);
                } else param.setUse(false);
                socketRequest(158, param);
            } else Toasts.showInfo(context, getString(R.string.apsf_str1));
        } catch (Exception e) {
            Toasts.showInfo(context,getString(R.string.apsf_str2));
        }
    }

    private void clearEditText() {
        et_RY1.setText("");
        et_RY2.setText("");
        et_SW1.setText("");
        et_SW2.setText("");
        et_PH1.setText("");
        et_PH2.setText("");
        et_EC1.setText("");
        et_EC2.setText("");
        et_time11.setText("");
        et_time12.setText("");
        et_time21.setText("");
        et_time22.setText("");
        et_time31.setText("");
        et_time32.setText("");
        et_time41.setText("");
        et_time42.setText("");
    }

    private void socketRequest(int commandType, Object object) {
        if (Data.userResult != null && tv_dev.getText().length() > 0) {
            SocketMessage message = new SocketMessage();
            message.setZP_KEY_CLIENT(987656789);
            message.setRequestType(3);
            message.setmSender(Data.userResult.getData().get(0).getUserName());
            message.setmReceiver(Data.pGhList.get(mPosition)[2]);
            if (Data.pGhList.get(mPosition)[3].equals("1") && conTypeMark < 5) {
                message.setIp(Data.pGhList.get(mPosition)[4]);
                message.setPort(Integer.parseInt(Data.pGhList.get(mPosition)[5]));
                conTypeMark++;
                Log.d("aa", "小于5：" + conTypeMark);
            } else {
                Log.d("aa", "大于5：" + conTypeMark);
                message.setIp(Data.SERVER_IP);
                message.setPort(Data.SERVER_PORT);
            }
            message.setCommandType(commandType);
            message.setResult(object);
            Operate.getDefault().getSender().messageRequest(message);
        }
    }

    private void parseGreenhouse() {
        if (pGhList != null && pGhList.size() > 0) {
            mPosition = 0;
            tv_dev.setText(pGhList.get(mPosition)[1]);
            if (pGhList.get(mPosition)[6].equals("1")) {
                mDownTimer.cancel();
                mDownTimer.start();
            } else mDialog.dismiss();
        } else {
            mDialog.dismiss();
            Toasts.showInfo(context, getString(R.string.request_error8));
        }
    }

    private void notifyDataSocket() {
        et_RY1.setText(param.getWenduAutoParam().getFallTopValue() + "");
        et_RY2.setText(param.getWenduAutoParam().getFallLowValue() + "");
        et_SW1.setText(param.getWenduAutoParam().getRaiseTopValue() + "");
        et_SW2.setText(param.getWenduAutoParam().getRaiseLowValue() + "");
        et_PH1.setText(param.getWenduAutoParam().getFallTopValue_2() + "");
        et_PH2.setText(param.getWenduAutoParam().getFallLowValue_2() + "");
        et_EC1.setText(param.getWenduAutoParam().getRaiseTopValue_2() + "");
        et_EC2.setText(param.getWenduAutoParam().getRaiseLowValue_2() + "");
        et_time11.setText(param.getGuangzhaoAutoParam().getFallTopValue() + "");
        et_time12.setText(param.getGuangzhaoAutoParam().getFallLowValue() + "");
        et_time21.setText(param.getGuangzhaoAutoParam().getRaiseTopValue() + "");
        et_time22.setText(param.getGuangzhaoAutoParam().getRaiseLowValue() + "");
        et_time31.setText(param.getGuangzhaoAutoParam().getFallTopValue_2() + "");
        et_time32.setText(param.getGuangzhaoAutoParam().getFallLowValue_2() + "");
        et_time41.setText(param.getGuangzhaoAutoParam().getRaiseTopValue_2() + "");
        et_time42.setText(param.getGuangzhaoAutoParam().getRaiseLowValue_2() + "");
        if (param.isUse()) {
            cb_ffs1.setChecked(true);
        } else cb_ffs2.setChecked(true);
        Toasts.showSuccess(context, getString(R.string.parsetstr1));
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


    @Override
    public void onResume() {
        super.onResume();
        if (isVisible)
            registered();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDownTimer != null) mDownTimer.cancel();
        EventBus.getDefault().unregister(this);
    }

    private void registered() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }
}
