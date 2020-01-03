package com.cdzp.huinongyun.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cdzp.huinongyun.BaseActivity;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.bean.RGVCResult;
import com.cdzp.huinongyun.bean.UniversalResult;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity1 extends BaseActivity {

    private Context context;
    private TextView tv_code;
    private EditText et_phone, et_code;
    private QMUITipDialog mDialog;
    private CountDownTimer Timer;
    private int count;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_register1);
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
        tv.setText(getString(R.string.top_register_str));

        et_phone = findViewById(R.id.et_register_phone);
        et_code = findViewById(R.id.et_register_code);
        tv_code = findViewById(R.id.tv_register_code);
        tv_code.setOnClickListener(this);
        findViewById(R.id.btn_register_ok).setOnClickListener(this);
        findViewById(R.id.ll_back).setOnClickListener(this);
    }

    @Override
    public void dealLogicAfterInitView() {
        count = 59;
        Timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_code.setText(getString(R.string.register1_send_code) + "(" + (count--) + ")");
            }

            @Override
            public void onFinish() {
                tv_code.setTextColor(getResources().getColor(R.color.title));
                tv_code.setText(getString(R.string.register1_send_code));
                tv_code.setEnabled(true);
            }
        };
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.btn_register_ok:
                if (et_phone.getText().length() > 0) {
                    if (et_code.getText().length() > 0) {
                        requestPhoneCode();
                    } else
                        Toasts.showInfo(context, getString(R.string.register1_hint_code));
                } else
                    Toasts.showInfo(context, getString(R.string.register1_hint_phone));
                break;
            case R.id.tv_register_code:
                if (et_phone.getText().length() > 0) {
                    requestCode();
                } else
                    Toasts.showInfo(context, getString(R.string.register1_hint_phone));
                break;
            default:

                break;
        }
    }

    private void requestPhoneCode() {
        mDialog.show();
        et_phone.clearFocus();
        et_code.clearFocus();
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Phone", et_phone.getText().toString()});
        data.add(new String[]{"Code", et_code.getText().toString()});
        WebServiceManage.requestData("RegisterCheckValidateCode",
                "/StringService/BaseSet.asmx", data, 2, context);
    }

    private void requestCode() {
        mDialog.show();
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Phone", et_phone.getText().toString()});
        WebServiceManage.requestData("RegisterGetValidateCode", "/StringService/BaseSet.asmx", data, 1, context);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage event) {
        if (event.isOk()) {
            switch (event.getLabel()) {
                case 1:
                    parseCode(event);
                    break;
                case 2:
                    parsePhoneCode(event);
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

    private void parsePhoneCode(WebMessage event) {
        UniversalResult result = new Gson().fromJson(event.getmResult(), UniversalResult.class);
        if (result.isSuccess()) {
            Intent intent = new Intent(context, RegisterActivity2.class);
            intent.putExtra("phone", et_phone.getText().toString());
            EventBus.getDefault().unregister(this);
            mDialog.dismiss();
            startActivity(intent);
            finish();
        } else {
            mDialog.dismiss();
            Toasts.showInfo(context, result.getMsg());
        }
    }

    private void parseCode(WebMessage event) {
        RGVCResult result = new Gson().fromJson(event.getmResult(), RGVCResult.class);
        if (result.isSuccess()) {
            tv_code.setEnabled(false);
            mDialog.dismiss();
            tv_code.setTextColor(getResources().getColor(R.color.colorDeafult));
            tv_code.setText(getString(R.string.register1_send_code) + "(60)");
            count = 59;
            Timer.start();
        } else {
            mDialog.dismiss();
            Toasts.showInfo(context, result.getMsg());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Timer != null) Timer.cancel();
    }
}
