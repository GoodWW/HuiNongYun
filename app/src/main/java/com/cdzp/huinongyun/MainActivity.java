package com.cdzp.huinongyun;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.LocaleList;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;

import com.cdzp.huinongyun.activity.HomeActivity;
import com.cdzp.huinongyun.activity.LoginActivity;
import com.cdzp.huinongyun.bean.UserLoginResult;
import com.cdzp.huinongyun.bean.VideoTypeResponse;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.LanguageUtil;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.tencent.mars.xlog.Log;
import com.zhy.autolayout.AutoLayoutActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AutoLayoutActivity {

    private ImmersionBar mImmersionBar;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true).init();
        context = this;
        initData();
    }

    /**
     * 判断用户名密码
     */
    private void initData() {
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String account = sp.getString("ACCOUNT", "");
        final String password = sp.getString("PASSWORD", "");
        if (password.length() > 0) {//存在密码
            EventBus.getDefault().register(this);
            new CountDownTimer(3000, 3000) {//三秒倒计时
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    disposalData(account, password);//验证用户名密码
                }
            }.start();
        } else {//不存在密码
            new CountDownTimer(2000, 2000) {//二秒倒计时
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    startLogin();//跳转登录
                }
            }.start();
        }
    }

    private void startLogin() {
        EventBus.getDefault().unregister(this);
        finish();
        startActivity(LoginActivity.class);
    }

    /**
     * 获取数据返回
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage event) {
        if (event.isOk()) {
            switch (event.getLabel()) {
                case 1://返回用户名密码校验
                    parseUserLogin(event);
                    break;
                case 2://获取视频信息返回
                    parsePlayTypeResponse(event.getmResult());
                    break;
                default:
                    startLogin();
                    break;
            }
        } else {
            startLogin();
        }
    }

    /**
     * 视频信息解析
     * @param s
     */
    private void parsePlayTypeResponse(String s) {
        try {
            VideoTypeResponse response = new Gson().fromJson(s, VideoTypeResponse.class);
            if (response.isSuccess() && response.getData() != null && response.getData().size() > 0) {//成功
                Data.videoResponse = response;//保存视频数据
            }
        } catch (Exception e) {//失败
            Toasts.showError(context, getString(R.string.request_error16));
        }
        EventBus.getDefault().unregister(this);
        startActivity(HomeActivity.class);//跳转主页
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
            if (result.isSuccess()) {//请求成功
                if (result.getStatus() == 0 && result.getData().size() > 0 &&
                        result.getData().get(0) != null) {
                    Data.userResult = result;
                    GetPlayType();//获取视频信息
                } else {//失败跳转登录界面
                    startLogin();
                }
            } else {//失败 异常分析
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
                startLogin();//最终跳转登录界面
            }
        } catch (Exception e) {
            Toasts.showError(context, getString(R.string.request_error17));
            startLogin();//最终跳转登录界面
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

    /**
     * 验证用户账号密码
     * @param UserName
     * @param Pwd
     */
    private void disposalData(String UserName, String Pwd) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"UserName", UserName});
        data.add(new String[]{"Pwd", Pwd});
        WebServiceManage.requestData("UserLogin", "/StringService/BaseSet.asmx", data, 1, context);
    }

    private void startActivity(Class<?> targetClass) {
        Intent intent = new Intent(this, targetClass);
        startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences preferences = newBase.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String selectedLanguage = preferences.getString("Language", "");
        Locale locale;
        if (selectedLanguage.equals("English")) {
            locale = Locale.US;
        } else {
            locale = Locale.SIMPLIFIED_CHINESE;
        }
        super.attachBaseContext(LanguageUtil.attachBaseContext(newBase, locale));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
        EventBus.getDefault().unregister(this);
    }
}
