package com.cdzp.huinongyun.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.adapter.MyPagerAdapter;
import com.cdzp.huinongyun.fragment.OnlBasicConfiguration;
import com.cdzp.huinongyun.fragment.OnlOperatingParameters;
import com.cdzp.huinongyun.fragment.OnlWaterConfiguration;
import com.cdzp.huinongyun.util.LanguageUtil;
import com.cdzp.huinongyun.util.MyViewPager;
import com.flyco.tablayout.SlidingTabLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OnlFertConfig extends AutoLayoutActivity {

    private ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fitsSystemWindows(true)
                .statusBarColor(R.color.title)
                .init();
        setContentView(R.layout.activity_onl_fert_config);
        initView();
    }

    private void initView() {
        TextView tv = findViewById(R.id.tv_title);
        tv.setText(getString(R.string.onl_fert118));
        String[] mTitles = new String[]{
                getString(R.string.onl_fert119),
                getString(R.string.onl_fert120),
                getString(R.string.onl_fert121)
        };

        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(new OnlOperatingParameters());
        mFragments.add(new OnlBasicConfiguration());
        mFragments.add(new OnlWaterConfiguration());

        MyViewPager mViewPager = findViewById(R.id.mViewPager_onlferconfig);
        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setScroll(true);
        mViewPager.setAdapter(mAdapter);

        SlidingTabLayout tabLayout = findViewById(R.id.mTabLayout_onlferconfig);
        tabLayout.setViewPager(mViewPager);

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //必须调用该方法，防止内存泄漏，不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
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
}
