package com.cdzp.huinongyun.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cdzp.huinongyun.BaseActivity;
import com.cdzp.huinongyun.MyApplication;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.bean.UserLoginResult;
import com.cdzp.huinongyun.bean.VideoTypeResponse;
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
import java.util.Locale;

public class LoginActivity extends BaseActivity {

    private Context context;
    private QMUITipDialog mDialog;
    private SharedPreferences sp;
    private EditText et_account, et_password;
    public static String account, password;
    private boolean isEn;
    private TextView tv_switch;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void dealLogicBeforeInitView() {
        context = this;
        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(getString(R.string.login_loading))
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
        et_account = findViewById(R.id.et_login_account);
        et_password = findViewById(R.id.et_login_password);
        tv_switch = findViewById(R.id.tv_al_switch);
    }

    @Override
    public void dealLogicAfterInitView() {
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String mLanguage = sp.getString("Language", "");
        if (mLanguage.equals("") || mLanguage.equals("Chinese")) {
            isEn = false;
            tv_switch.setText("English");
        } else {//English
            isEn = true;
            tv_switch.setText("中文");
        }
        et_account.setText(sp.getString("ACCOUNT", ""));
        et_password.setText(sp.getString("PASSWORD", ""));

        // 设置光标在最后
        et_account.setSelection(et_account.getText().length());
        et_password.setSelection(et_password.getText().length());

        EventBus.getDefault().register(this);

        tv_switch.setOnClickListener(this);
        findViewById(R.id.btn_login_in).setOnClickListener(this);
        findViewById(R.id.btn_login_register).setOnClickListener(this);
//        findViewById(R.id.tv_login_forget).setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_login_in:
                if (et_account.getText().length() > 0) {
                    if (et_password.getText().length() > 0) {
                        disposalData();
                    } else
                        Toasts.showInfo(context, getString(R.string.password_on_warning));
                } else
                    Toasts.showInfo(context, getString(R.string.account_on_warning));
                break;
            case R.id.btn_login_register:
                startActivity(RegisterActivity1.class);
                break;
            case R.id.tv_al_switch:
                changeAppLanguage();
                break;
//            case R.id.tv_login_forget:
//
//                break;
            default:

                break;
        }
    }

    /**
     * 更改应用语言
     */
    public void changeAppLanguage() {
        SharedPreferences.Editor editor = sp.edit();
        if (isEn) {
            editor.putString("Language", "Chinese");
            ((MyApplication) getApplication()).setEnglish(false);
        } else {
            editor.putString("Language", "English");
            ((MyApplication) getApplication()).setEnglish(true);
        }
        editor.commit();
        recreate();
    }

    /**
     * 整理数据并请求WebService登录接口
     */
    private void disposalData() {
        mDialog.show();
        et_account.clearFocus();
        et_password.clearFocus();
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"UserName", et_account.getText().toString()});
        data.add(new String[]{"Pwd", et_password.getText().toString()});
        WebServiceManage.requestData("UserLogin", "/StringService/BaseSet.asmx", data, 1, context);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage event) {
        if (event.isOk()) {
            switch (event.getLabel()) {
                case 1:
                    parseUserLogin(event);
                    break;
                case 2:
                    parsePlayTypeResponse(event.getmResult());
                    break;
                default:
                    mDialog.dismiss();
                    Toasts.showError(context, getString(R.string.request_error4));
                    break;
            }
        } else {
            mDialog.dismiss();
            Toasts.showError(context, event.getmResult());
        }
    }

    private void parsePlayTypeResponse(String s) {
        try {
            VideoTypeResponse response = new Gson().fromJson(s, VideoTypeResponse.class);
            if (response.isSuccess() && response.getData() != null && response.getData().size() > 0) {
                Data.videoResponse = response;
            }
        } catch (Exception e) {
            Toasts.showError(context, getString(R.string.request_error16));
        }
        EventBus.getDefault().unregister(this);
        startActivity(HomeActivity.class);
        mDialog.dismiss();
        finish();
    }

    /**
     * 用户登录数据解析
     *
     * @param event
     */
    private void parseUserLogin(WebMessage event) {
        try {
            UserLoginResult result = new Gson().fromJson(event.getmResult(), UserLoginResult.class);
            if (result.isSuccess()) {
                if (result.getStatus() == 0 && result.getData().size() > 0 &&
                        result.getData().get(0) != null) {
                    Editor editor = sp.edit();
                    editor.putString("ACCOUNT", et_account.getText().toString());
                    editor.putString("PASSWORD", et_password.getText().toString());
                    editor.apply();
                    Data.userResult = result;
                    GetPlayType();
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
     * 获取视频播放类型
     */
    private void GetPlayType() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
        data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
        data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode() + ""});
        WebServiceManage.requestData("VideoGetPlayType", "/StringService/DeviceSet.asmx", data, 2, context);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (account != null && password != null) {
            et_account.setText(account);
            et_password.setText(password);
            et_account.setSelection(et_account.getText().length());
            et_password.setSelection(et_password.getText().length());
            account = null;
            password = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

}
