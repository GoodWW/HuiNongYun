package com.cdzp.huinongyun.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.activity.HomAddDevActivity;
import com.cdzp.huinongyun.bean.PowSubCamListResult;
import com.cdzp.huinongyun.bean.RegDeviceResult;
import com.cdzp.huinongyun.bean.RegSubdomainResult;
import com.cdzp.huinongyun.bean.UniversalResult;
import com.cdzp.huinongyun.bean.Ys7SmIDResult;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加萤石云
 */
public class AddYinVidFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean isVisible;
    private QMUITipDialog mDialog;
    private String smID, devClass = "1";
    private EditText et1, et3, et4, et5;
    private TextView tv2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_add_yin_vid, container, false);
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
                eRegister();
            }
        } else EventBus.getDefault().unregister(this);
    }

    private void initView() {
        context = getContext();
        mDialog = ((HomAddDevActivity) getActivity()).getmDialog();

        et1 = view.findViewById(R.id.et_fayv1);
        tv2 = view.findViewById(R.id.tv_fayv_2);
        tv2.setText(getString(R.string.faddvid_str16));
        et3 = view.findViewById(R.id.et_fayv3);
        et4 = view.findViewById(R.id.et_fayv4);
        et5 = view.findViewById(R.id.et_fayv5);

        view.findViewById(R.id.ll_fayv).setOnClickListener(this);
        view.findViewById(R.id.btn_fayv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fayv:
                if (devClass.equals("1")) {
                    devClass = "2";
                    tv2.setText(getString(R.string.faddvid_str17));
                } else {
                    devClass = "1";
                    tv2.setText(getString(R.string.faddvid_str16));
                }
                break;
            case R.id.btn_fayv:
                if (et1.getText().length() > 0 && et3.getText().length() > 0 &&
                        et4.getText().length() > 0) {
                    request1();
                } else Toasts.showInfo(context, getString(R.string.request_error6));
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
                    parseResult1(message.getmResult());
                    break;
                case 11:
                    parseResult11(message.getmResult());
                    break;
                case 12:
                    parseResult12(message.getmResult());
                    break;
                case 2:
                    parseResult2(message.getmResult());
                    break;
                case 3:
                    parseResult3(message.getmResult());
                    break;
                default:
                    mDialog.dismiss();
                    Toasts.showError(context, getString(R.string.request_error4));
                    break;
            }
        } else {
            mDialog.dismiss();
            Toasts.showError(context, message.getmResult());
        }
    }

    private void parseResult3(String s) {
        try {
            PowSubCamListResult result = new Gson().fromJson(s, PowSubCamListResult.class);
            if (result.isIsSuccess()) {
                et1.setText("");
                et3.setText("");
                et4.setText("");
                et5.setText("");
                mDialog.dismiss();
                Toasts.showSuccess(context, getString(R.string.request_succ));
            } else {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.request_fail));
            }
        } catch (Exception e) {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.socket_error5));
        }
    }

    private void parseResult2(String s) {
        try {
            RegDeviceResult result = new Gson().fromJson(s, RegDeviceResult.class);
            if (result.isIsSuccess()) {
                request3(result.getId() + "");
            } else {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.request_fail));
            }
        } catch (Exception e) {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.socket_error5));
        }
    }

    private void parseResult12(String s) {
        try {
            UniversalResult result = new Gson().fromJson(s, UniversalResult.class);
            if (result.isSuccess()) {
                request2(smID);
            } else {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.request_fail));
            }
        } catch (Exception e) {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.socket_error5));
        }
    }

    private void parseResult11(String s) {
        try {
            RegSubdomainResult result = new Gson().fromJson(s, RegSubdomainResult.class);
            if (result.isIsSuccess()) {
//                Log.d("aa", "注册子账号request12");
                smID = result.getId() + "";
                request12(smID);
            } else {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.request_fail));
            }
        } catch (Exception e) {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.socket_error5));
        }
    }

    private void parseResult1(String s) {
        try {
            Ys7SmIDResult result = new Gson().fromJson(s, Ys7SmIDResult.class);
            if (result.isSuccess()) {
//                Log.d("aa", "不注册子账号");
                smID = result.getSmID();
                request2(smID);
            } else {
                if (!result.isHasError()) {
//                    Log.d("aa", "注册子账号request11");
                    request11(result);
                } else {
                    mDialog.dismiss();
                    Toasts.showInfo(context, getString(R.string.request_fail));
                }
            }
        } catch (Exception e) {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.socket_error5));
        }
    }

    private void request12(String smID) {
        if (Data.userResult != null) {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"SmID", smID});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("SetYs7SmID",
                    "/StringService/DeviceSet.asmx", data, 12, context);
        } else {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    private void request11(Ys7SmIDResult result) {
        if (Data.userResult != null) {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"MaID", result.getData().getMaID()});
            data.add(new String[]{"SmName", result.getData().getSmName()});
            data.add(new String[]{"SmNickName", result.getData().getSmNickName()});
            data.add(new String[]{"SmPWD", result.getData().getSmPWD()});
            WebServiceManage.requestData2("a1_RegSubdomain",
                    "http://101.200.193.60:9099/zpys7RegisterString.asmx", data, 11, context);
        } else {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    private void request2(String smID) {
        if (Data.userResult != null) {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"DevName", et1.getText().toString()});
            data.add(new String[]{"DevClass", devClass});
            data.add(new String[]{"DeviceSerial", et3.getText().toString()});
            data.add(new String[]{"ValidateCode", et4.getText().toString()});
            data.add(new String[]{"Remark", et5.getText().toString()});
            data.add(new String[]{"SmID", smID});
            WebServiceManage.requestData2("a2_RegDevice",
                    "http://101.200.193.60:9099/zpys7RegisterString.asmx", data, 2, context);
        } else {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    private void request3(String devID) {
        if (Data.userResult != null) {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"DevID", devID});
            data.add(new String[]{"SmID", smID});
            WebServiceManage.requestData2("a3_PowerSubCameraList",
                    "http://101.200.193.60:9099/zpys7RegisterString.asmx", data, 3, context);
        } else {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    private void request1() {
        if (Data.userResult != null) {
            mDialog.show();
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("GetYs7SmID",
                    "/StringService/DeviceSet.asmx", data, 1, context);
        } else Toasts.showError(context, getString(R.string.request_error9));
    }

    private void eRegister() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }
}
