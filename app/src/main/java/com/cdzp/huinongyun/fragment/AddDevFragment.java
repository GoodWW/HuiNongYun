package com.cdzp.huinongyun.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.activity.HomAddDevActivity;
import com.cdzp.huinongyun.bean.UniversalResult;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.scancode.ActivityScanerCode;
import com.cdzp.huinongyun.scancode.OnRxScanerListener;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.blankj.utilcode.constant.PermissionConstants.CAMERA;

/**
 * 添加终端
 */
public class AddDevFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private QMUITipDialog mDialog;
    private boolean isVisible;
    private EditText et_name, et_number, et_remark;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_add_dev, container, false);
            initView();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        } else EventBus.getDefault().unregister(this);
    }

    private void initView() {
        context = getContext();
        mDialog = ((HomAddDevActivity) getActivity()).getmDialog();

        et_name = view.findViewById(R.id.et_fad_name);
        et_number = view.findViewById(R.id.et_fad_number);
        et_remark = view.findViewById(R.id.et_fad_remark);

        view.findViewById(R.id.iv_scan).setOnClickListener(this);
        view.findViewById(R.id.btn_fad).setOnClickListener(this);
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

    private void parseResult(WebMessage message) {
        UniversalResult result = new Gson().fromJson(message.getmResult(), UniversalResult.class);
        if (result.isSuccess()) {
            et_name.setText("");
            et_number.setText("");
            et_remark.setText("");
            mDialog.dismiss();
            Toasts.showSuccess(context, getString(R.string.request_succ));
        } else {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.request_fail));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fad:
                if (et_name.getText().length() > 0 && et_number.getText().length() > 0) {
                    requestDev();
                } else Toasts.showInfo(context, getString(R.string.request_error6));
                break;
            case R.id.iv_scan:
                //请求相机权限
                PermissionUtils.permission(PermissionConstants.getPermissions(CAMERA))
                        .callback(new PermissionUtils.SimpleCallback() {
                            @Override
                            public void onGranted() {
                                ActivityScanerCode.setScanerListener(new OnRxScanerListener() {
                                    @Override
                                    public void onSuccess(String type, Result result) {
                                        et_number.setText(result.getText());
                                    }

                                    @Override
                                    public void onFail(String type, String message) {

                                    }
                                });
                                ActivityUtils.startActivity(ActivityScanerCode.class);
                            }

                            @Override
                            public void onDenied() {

                            }
                        })
                        .rationale(new PermissionUtils.OnRationaleListener() {
                            @Override
                            public void rationale(ShouldRequest shouldRequest) {
                                shouldRequest.again(true);
                            }
                        })
                        .request();
                break;
            default:

                break;
        }
    }

    private void requestDev() {
        if (Data.userResult != null) {
            mDialog.show();
            et_name.clearFocus();
            et_number.clearFocus();
            et_remark.clearFocus();
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"GhName", et_name.getText().toString()});
            data.add(new String[]{"GhNo", et_number.getText().toString()});
            data.add(new String[]{"Remark", et_remark.getText().toString()});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("GreenHouseRegist",
                    "/StringService/DeviceSet.asmx", data, 1, context);
        }
    }
}
