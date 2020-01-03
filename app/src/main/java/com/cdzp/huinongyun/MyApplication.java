package com.cdzp.huinongyun;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.util.Log;

import com.blankj.utilcode.util.Utils;

import java.util.Locale;

public class MyApplication extends Application {

    private boolean isEnglish;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);

        //中英文切换
//        Locale locale;
        if (getSharedPreferences("userInfo", Context.MODE_PRIVATE)
                .getString("Language", "").equals("English")) {
            isEnglish = true;
        } else {
            isEnglish = false;
        }
//        if (!isEnglish) {
//            locale = Locale.SIMPLIFIED_CHINESE;
//        } else {//English
//            locale = Locale.US;
//        }
//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        Configuration configuration = getResources().getConfiguration();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            configuration.setLocale(locale);
//        } else {
//            configuration.locale = locale;
//        }
//        getResources().updateConfiguration(configuration, metrics);

    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        Locale locale;
//        if (!isEnglish) {
//            locale = Locale.SIMPLIFIED_CHINESE;
//        } else {//English
//            locale = Locale.US;
//        }
//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        Configuration configuration = getResources().getConfiguration();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            configuration.setLocale(locale);
//        } else {
//            configuration.locale = locale;
//        }
//        getResources().updateConfiguration(configuration, metrics);
//    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //解决工程方法数超过64K
        MultiDex.install(this);
    }
//
//
//    private Context attachBaseContexts(Context context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 8.0需要使用createConfigurationContext处理
//            return updateResources(context);
//        } else {
//            return context;
//        }
//    }
//
//    private Context updateResources(Context context) {
//        Resources resources = context.getResources();
//        Locale locale;
//        boolean isEnglish;
//        if (context.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
//                .getString("Language", "").equals("English")) {
//            isEnglish = true;
//        } else {
//            isEnglish = false;
//        }
//        if (!isEnglish) {
//            locale = Locale.SIMPLIFIED_CHINESE;
//        } else {//English
//            locale = Locale.US;
//        }
//
//        Configuration configuration = resources.getConfiguration();
//        configuration.setLocale(locale);
//        configuration.setLocales(new LocaleList(locale));
//        return context.createConfigurationContext(configuration);
//    }

    public boolean isEnglish() {
        return isEnglish;
    }

    public void setEnglish(boolean english) {
        isEnglish = english;
    }
}
