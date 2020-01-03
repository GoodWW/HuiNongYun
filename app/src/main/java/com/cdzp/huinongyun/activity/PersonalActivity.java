package com.cdzp.huinongyun.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.cdzp.huinongyun.BaseActivity;
import com.cdzp.huinongyun.MyApplication;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.bean.UserLoginResult;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Operate;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PersonalActivity extends BaseActivity {

    private Context context;
    private TextView tv1, tv2;
    private AlertDialog pawDialog = null;
    private EditText et1, et2, et3;
    private QMUITipDialog mDialog;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_personal);
    }

    @Override
    public void dealLogicBeforeInitView() {
        context = this;
        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
    }

    @Override
    public void initView() {
        TextView tv = findViewById(R.id.tv_title);
        tv.setText(getString(R.string.personal_str1));
        RelativeLayout rl = findViewById(R.id.rl_right);
        rl.setVisibility(View.INVISIBLE);
        tv1 = findViewById(R.id.tv_ap1);
        tv2 = findViewById(R.id.tv_ap2);
        if (Data.userResult != null && Data.userResult.getData() != null && Data.userResult.getData().size() > 0) {
            if (Data.userResult.getData().get(0).getUserName() != null) {
                tv1.setText(Data.userResult.getData().get(0).getUserName());
            }
            if (Data.userResult.getData().get(0).getContactMethod() != null) {
                if (Data.userResult.getData().get(0).getContactMethod().length() == 11) {
                    tv2.setText(getString(R.string.personal_str3) + Data.userResult.getData().get(0).getContactMethod().substring(0, 3)
                            + "****" + Data.userResult.getData().get(0).getContactMethod().substring(7, 11));
                } else
                    tv2.setText(getString(R.string.personal_str4) + Data.userResult.getData().get(0).getContactMethod());
            }
        }

        EventBus.getDefault().register(this);

        findViewById(R.id.ll_ap1).setOnClickListener(this);
        findViewById(R.id.ll_ap2).setOnClickListener(this);
        findViewById(R.id.ll_ap3).setOnClickListener(this);
        findViewById(R.id.ll_ap4).setOnClickListener(this);
        findViewById(R.id.ll_ap5).setOnClickListener(this);
        findViewById(R.id.ll_ap6).setOnClickListener(this);
        findViewById(R.id.ll_back).setOnClickListener(this);
    }

    @Override
    public void dealLogicAfterInitView() {

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                EventBus.getDefault().unregister(this);
                finish();
                break;
            case R.id.ll_ap1:
                startActivity(PerDetailsActivity.class);
                break;
            case R.id.ll_ap2:
                if (pawDialog == null) {
                    initpawDialog();
                } else if (!pawDialog.isShowing()) {
                    et1.setText("");
                    et2.setText("");
                    et3.setText("");
                    et1.clearFocus();
                    et2.clearFocus();
                    et3.clearFocus();
                    pawDialog.show();
                }
                break;
            case R.id.ll_ap3:
                Uri uri = Uri.parse("http://www.hn-yun.cn:8099/Product/Index");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.ll_ap4:
                startActivity(AboutActivity.class);
                break;
            case R.id.ll_ap5:
                new QMUIDialog.MessageDialogBuilder(context)
                        .setTitle(getString(R.string.fou_sit_mon_str7))
                        .setMessage(getString(R.string.personal_str5))
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
                                changeAppLanguage();
                            }
                        })
                        .create().show();
                break;
            case R.id.ll_ap6:
                new QMUIDialog.MessageDialogBuilder(context)
                        .setTitle(getString(R.string.fou_sit_mon_str7))
                        .setMessage(getString(R.string.personal_str7))
                        .addAction(getString(R.string.envir_str1), new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction(getString(R.string.personal_str6), new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                SharedPreferences.Editor editor = context.getSharedPreferences("userInfo",
                                        Context.MODE_PRIVATE).edit();
                                editor.putString("PASSWORD", "");
                                editor.commit();
                                dialog.dismiss();
                                Operate.getDefault().clear();
                                Data.clear();
                                AppUtils.relaunchApp();
                            }
                        })
                        .create().show();
                break;
            case R.id.tv_cpd_no:
                pawDialog.dismiss();
                break;
            case R.id.tv_cpd_ok:
                if (et1.getText().length() > 0 && et2.getText().length() > 0
                        && et3.getText().length() > 0) {
                    if (et2.getText().toString().equals(et3.getText().toString())) {
                        ChangePwd();
                    } else Toasts.showInfo(context, getString(R.string.request_error5));
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
                    parse(message.getmResult());
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

    private void parse(String s) {
        try {
            UserLoginResult result = new Gson().fromJson(s, UserLoginResult.class);
            if (result.isSuccess()) {
                if (result.getStatus() == 0 && result.getData().size() > 0 &&
                        result.getData().get(0) != null) {
                    SharedPreferences.Editor editor = getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit();
                    editor.putString("PASSWORD", "");
                    editor.commit();
                    mDialog.dismiss();
                    pawDialog.dismiss();
                    QMUIDialog dialog = new QMUIDialog.MessageDialogBuilder(context)
                            .setTitle(getString(R.string.fou_sit_mon_str7))
                            .setMessage(getString(R.string.personal_str8))
                            .addAction(getString(R.string.personal_str6), new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                    Operate.getDefault().clear();
                                    Data.clear();
                                    AppUtils.relaunchApp();
                                }
                            }).create();
                    dialog.setCancelable(false);
                    dialog.show();
                } else {
                    mDialog.dismiss();
                    Toasts.showInfo(context, getString(R.string.request_error17));
                }
            } else {
                mDialog.dismiss();
                switch (result.getStatus()) {
                    case -1:
                        Toasts.showInfo(context, getString(R.string.login_request_error1));
                        break;
                    case -2:
                        Toasts.showInfo(context, getString(R.string.login_request_error2));
                        break;
                    case -3:
                        Toasts.showInfo(context, getString(R.string.login_request_error3));
                        break;
                    case -4:
                        Toasts.showInfo(context, getString(R.string.login_request_error4));
                        break;
                    default:
                        Toasts.showInfo(context, getString(R.string.request_error17));
                        break;
                }
            }
        } catch (Exception e) {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.request_error17));
        }
    }


    /**
     * 修改密码
     */
    private void ChangePwd() {
        if (Data.userResult != null) {
            mDialog.show();
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"UserName", Data.userResult.getData().get(0).getUserName()});
            data.add(new String[]{"Pwd", et1.getText().toString()});
            data.add(new String[]{"NewPwd", et2.getText().toString()});
            WebServiceManage.requestData("ChangePwd", "/StringService/BaseSet.asmx", data, 1, context);
        } else Toasts.showError(context, getString(R.string.request_error9));
    }


    private void initpawDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context)
                .inflate(R.layout.change_password_dialog, null, false);
        et1 = view.findViewById(R.id.et_cpd1);
        et2 = view.findViewById(R.id.et_cpd2);
        et3 = view.findViewById(R.id.et_cpd3);
        view.findViewById(R.id.tv_cpd_no).setOnClickListener(this);
        view.findViewById(R.id.tv_cpd_ok).setOnClickListener(this);
        builder.setView(view);
        pawDialog = builder.show();
    }

    /**
     * 更改应用语言并重启
     */
    public void changeAppLanguage() {
        SharedPreferences.Editor editor = getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit();
        if (((MyApplication) getApplication()).isEnglish()) {
            editor.putString("Language", "Chinese");
        } else {
            editor.putString("Language", "English");
        }
        editor.commit();
        Operate.getDefault().clear();
        Data.clear();
        AppUtils.relaunchApp();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

}
