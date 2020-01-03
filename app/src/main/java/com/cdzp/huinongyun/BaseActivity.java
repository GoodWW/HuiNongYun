package com.cdzp.huinongyun;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.cdzp.huinongyun.util.LanguageUtil;
import com.gyf.barlibrary.ImmersionBar;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.Locale;


public abstract class BaseActivity extends AutoLayoutActivity implements View.OnClickListener {

    private ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置Activity竖屏
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fitsSystemWindows(true)
                .statusBarColor(R.color.title)
                .init();
        //让布局向上移来显示软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentLayout();
        dealLogicBeforeInitView();
        initView();
        dealLogicAfterInitView();
    }

    public abstract void setContentLayout();

    public abstract void dealLogicBeforeInitView();

    public abstract void initView();

    public abstract void dealLogicAfterInitView();

    public abstract void onClickEvent(View v);

    @Override
    public void onClick(View view) {
        onClickEvent(view);
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

    protected void startActivity(Class<?> targetClass) {
        Intent intent = new Intent(this, targetClass);
        startActivity(intent);
    }

    protected void startActivity(Class<?> targetClass, Bundle bundle) {
        Intent intent = new Intent(this, targetClass);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //必须调用该方法，防止内存泄漏，不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
    }
}
