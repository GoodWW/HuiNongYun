package com.cdzp.huinongyun.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.KeyEvent;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.blankj.utilcode.util.AppUtils;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.fragment.AccManageFragment;
import com.cdzp.huinongyun.fragment.AddDeviceFragment;
import com.cdzp.huinongyun.fragment.DevMainFragment;
import com.cdzp.huinongyun.fragment.DropRemindFragment;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.LanguageUtil;
import com.cdzp.huinongyun.util.MyViewPager;
import com.cdzp.huinongyun.util.Operate;
import com.cdzp.huinongyun.util.Toasts;
import com.gyf.barlibrary.ImmersionBar;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 首页以及底部状态栏
 */
public class HomeActivity extends AutoLayoutActivity {

    private Context context;
    /**
     * android 4.4以上沉浸式以及bar的管理
     */
    private ImmersionBar mImmersionBar;
    /**
     * 底部导航栏菜单库
     */
    private BottomNavigationBar navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //bar初始化
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
        initview();
    }

    private void initview() {
        context = this;
        //初始化Socket
        Operate.initSocket(getApplicationContext());
        //iewPager相关初始化，数据绑定
        final MyViewPager viewPager = findViewById(R.id.mViewPager);
        final List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new AddDeviceFragment());//设备控制（首页）
        fragmentList.add(new DevMainFragment());//设备维护
        fragmentList.add(new DropRemindFragment());//掉线提醒
        fragmentList.add(new AccManageFragment());//账号管理
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        viewPager.setAdapter(pagerAdapter);//viewPager到这里结束

        //底部导航栏相关设置以及点击监听
        navigationBar = findViewById(R.id.bottom_navigation_bar);
        navigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        navigationBar
                .addItem(new BottomNavigationItem(R.drawable.nav_add_cu, R.string.hone_bottom1)).setActiveColor(R.color.title)
                .addItem(new BottomNavigationItem(R.drawable.nav_maintain, R.string.hone_bottom2)).setActiveColor(R.color.title)
                .addItem(new BottomNavigationItem(R.drawable.nav_remind, R.string.hone_bottom3)).setActiveColor(R.color.title)
                .addItem(new BottomNavigationItem(R.drawable.nav_admin, R.string.hone_bottom4)).setActiveColor(R.color.title)
                .initialise();

        navigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });//到此结束
    }

    private long mExitTime;

    //点击两次退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toasts.showInfo(context, getString(R.string.app_exit));
                mExitTime = System.currentTimeMillis();
            } else {
                Operate.getDefault().clear();
                Data.clear();
                AppUtils.exitApp();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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

    //退出时注销bar 和关闭socket
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null) mImmersionBar.destroy();
        Data.clear();
        Operate.getDefault().clear();
    }

}
