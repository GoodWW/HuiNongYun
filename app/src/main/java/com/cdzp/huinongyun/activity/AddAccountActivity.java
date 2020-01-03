package com.cdzp.huinongyun.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cdzp.huinongyun.BaseActivity;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.bean.GetRoleListResult;
import com.cdzp.huinongyun.bean.UniversalResult;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加账号
 */
public class AddAccountActivity extends BaseActivity {

    private Context context;
    private List<String> BaseName;
    private List<String> BaseValue;
    private TextView tv_aaa;
    private EditText et_acc, et_nick, et_phone;
    private int mPosition;
    private QMUITipDialog mDialog;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_add_account);
    }

    @Override
    public void dealLogicBeforeInitView() {
        context = this;
        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                WebServiceManage.dispose();
            }
        });
    }

    @Override
    public void initView() {
        TextView tv = findViewById(R.id.tv_title);
        tv.setText(R.string.add_acc);

        tv_aaa = findViewById(R.id.tv_aaa);
        et_acc = findViewById(R.id.et_aaa_acc);
        et_nick = findViewById(R.id.et_aaa_nick);
        et_phone = findViewById(R.id.et_aaa_phone);
        findViewById(R.id.ll_aaa).setOnClickListener(this);
        findViewById(R.id.btn_aaa).setOnClickListener(this);
        findViewById(R.id.ll_back).setOnClickListener(this);
    }

    @Override
    public void dealLogicAfterInitView() {

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_aaa:
                if (BaseName != null && BaseName.size() > 0) {
                    showBottomSheet();
                } else requestRole();
                break;
            case R.id.btn_aaa:
                if (tv_aaa.getText().length() > 0 && et_acc.getText().length() > 0 &&
                        et_nick.getText().length() > 0 && et_phone.getText().length() > 0) {
                    requestUserAdd();
                } else Toasts.showInfo(context, getString(R.string.request_error6));
                break;
            default:

                break;
        }
    }

    private void showBottomSheet() {
        QMUIBottomSheet.BottomListSheetBuilder mBuilder = new QMUIBottomSheet.BottomListSheetBuilder(context)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        mPosition = position;
                        tv_aaa.setText(BaseName.get(mPosition));
                        dialog.dismiss();
                    }
                });
        for (int i = 0; i < BaseName.size(); i++) {
            mBuilder.addItem(BaseName.get(i));
        }
        mBuilder.build().show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 1:
                    parseRole(message.getmResult());
                    break;
                case 2:
                    parseUserAdd(message.getmResult());
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

    private void parseUserAdd(String s) {
        UniversalResult result = new Gson().fromJson(s, UniversalResult.class);
        if (result.isSuccess()) {
            tv_aaa.setText("");
            et_acc.setText("");
            et_nick.setText("");
            et_phone.setText("");
            mDialog.dismiss();
            Toasts.showInfo(context, result.getMsg());
        } else {
            mDialog.dismiss();
            Toasts.showInfo(context, result.getMsg());
        }
    }

    private void parseRole(String s) {
        GetRoleListResult result = new Gson().fromJson(s, GetRoleListResult.class);
        if (result.isSuccess()) {
            if (result.getData().get(0).size() > 0) {
                BaseName = new ArrayList<>();
                BaseValue = new ArrayList<>();
                for (int i = 0; i < result.getData().get(0).size(); i++) {
                    BaseName.add(result.getData().get(0).get(i).getBaseName());
                    BaseValue.add(result.getData().get(0).get(i).getBaseValue());
                }
                mDialog.dismiss();
            } else {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.request_error8));
            }
        } else {
            mDialog.dismiss();
            Toasts.showInfo(context, result.getMsg());
        }
    }

    private void requestRole() {
        if (Data.userResult != null) {
            mDialog.show();
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            data.add(new String[]{"RoleID", Data.userResult.getData().get(0).getRoleID() + ""});
            WebServiceManage.requestData("GetRoleList",
                    "/StringService/BaseSet.asmx", data, 1, context);
        } else Toasts.showError(context, getString(R.string.request_error9));
    }

    private void requestUserAdd() {
        if (Data.userResult != null) {
            mDialog.show();
            et_acc.clearFocus();
            et_nick.clearFocus();
            et_phone.clearFocus();
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"RoleID", BaseValue.get(mPosition)});
            data.add(new String[]{"UserName", et_acc.getText().toString()});
            data.add(new String[]{"NickName", et_nick.getText().toString()});
            data.add(new String[]{"ContactMethod", et_phone.getText().toString()});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("UserAdd",
                    "/StringService/BaseSet.asmx", data, 2, context);
        } else Toasts.showError(context, getString(R.string.request_error9));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        requestRole();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
