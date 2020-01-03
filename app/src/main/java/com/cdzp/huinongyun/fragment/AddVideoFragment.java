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
import com.cdzp.huinongyun.bean.UniversalResult;
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
 * 添加视频
 */
public class AddVideoFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean isVisible, isHiDdns;
    private TextView tv2;
    private String mode = "1";
    private QMUITipDialog mDialog;
    private EditText et_1, et_3, et_4, et_5, et_6, et_7, et_8, et_9;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_add_video, container, false);
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

        et_1 = view.findViewById(R.id.et_fav1);
        tv2 = view.findViewById(R.id.tv_fav_2);
        et_3 = view.findViewById(R.id.et_fav3);
        et_4 = view.findViewById(R.id.et_fav4);
        et_5 = view.findViewById(R.id.et_fav5);
        et_6 = view.findViewById(R.id.et_fav6);
        et_7 = view.findViewById(R.id.et_fav7);
        et_8 = view.findViewById(R.id.et_fav8);
        et_9 = view.findViewById(R.id.et_fav9);
        tv2.setText("IPServer");
        view.findViewById(R.id.ll_fav).setOnClickListener(this);
        view.findViewById(R.id.btn_fav).setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 1:
                    parseResult(message);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fav:
                switchMode();
                break;
            case R.id.btn_fav:
                if (et_1.getText().length() > 0 && tv2.getText().length() > 0 &&
                        et_3.getText().length() > 0 && et_4.getText().length() > 0 &&
                        et_5.getText().length() > 0 && et_7.getText().length() > 0 &&
                        et_8.getText().length() > 0) {
                    if (isHiDdns) {
                        if (et_6.getText().length() > 0) {
                            requestVideo();
                        } else Toasts.showInfo(context, getString(R.string.request_error6));
                    } else requestVideo();
                } else Toasts.showInfo(context, getString(R.string.request_error6));
                break;
            default:

                break;
        }
    }

    private void parseResult(WebMessage message) {
        try {
            UniversalResult result = new Gson().fromJson(message.getmResult(), UniversalResult.class);
            if (result.isSuccess()) {
                et_1.setText("");
                et_5.setText("");
                et_6.setText("");
                et_7.setText("");
                et_8.setText("");
                et_9.setText("");
                if (!isHiDdns) {
                    et_3.setText("");
                    et_4.setText("");
                }
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

    private void switchMode() {
        if (isHiDdns) {
            mode = "1";
            tv2.setText("IPServer");
            et_3.setHint(getString(R.string.faddvid_str3));
            et_4.setHint(getString(R.string.faddvid_str5));
            et_6.setVisibility(View.GONE);
            et_3.setText("");
            et_4.setText("");
            isHiDdns = false;
        } else {
            mode = "2";
            tv2.setText("HIDDNS");
            et_3.setHint(getString(R.string.faddvid_str6));
            et_4.setHint(getString(R.string.faddvid_str7));
            et_6.setVisibility(View.VISIBLE);
            et_3.setText("183.136.184.61");
            et_4.setText("80");
            isHiDdns = true;
        }
    }

    private void requestVideo() {
        if (Data.userResult != null) {
            mDialog.show();
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"DvName", et_1.getText().toString()});
            data.add(new String[]{"DefaultMode", mode});
            data.add(new String[]{"ServerIP", et_3.getText().toString()});
            data.add(new String[]{"DevicePort", et_4.getText().toString()});
            data.add(new String[]{"Tagmark", et_6.getText().toString()});
            data.add(new String[]{"ServerIP2", et_3.getText().toString()});
            data.add(new String[]{"DevicePort2", et_5.getText().toString()});
            data.add(new String[]{"Tagmark2", et_4.getText().toString()});
            data.add(new String[]{"User", et_7.getText().toString()});
            data.add(new String[]{"Password", et_8.getText().toString()});
            data.add(new String[]{"Remark", et_9.getText().toString()});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("VideoRegist",
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
