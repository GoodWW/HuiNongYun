package com.cdzp.huinongyun.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdzp.huinongyun.BaseActivity;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.adapter.MyPagerAdapter;
import com.cdzp.huinongyun.fragment.FertAutoParFragment;
import com.cdzp.huinongyun.fragment.FertFragment;
import com.cdzp.huinongyun.fragment.FertPlanParFragment;
import com.cdzp.huinongyun.fragment.IrrigationRecord;
import com.cdzp.huinongyun.fragment.OnlFerPlan;
import com.cdzp.huinongyun.fragment.OnlFert;
import com.cdzp.huinongyun.fragment.OnlReaTimeData;
import com.cdzp.huinongyun.util.MyViewPager;
import com.flyco.tablayout.SlidingTabLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 在现施肥机Activity
 */
public class OnlineFert extends BaseActivity {

    private Context context;
    private QMUITipDialog mDialog;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_online_fert);
    }

    @Override
    public void dealLogicBeforeInitView() {
        context = this;
    }

    @Override
    public void initView() {
        TextView tv = findViewById(R.id.tv_title);
        tv.setText(getString(R.string.circle_sfj));
        findViewById(R.id.ll_back).setOnClickListener(this);
        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();

        String[] mTitles = new String[]{
                getString(R.string.fert_str1),
                getString(R.string.fert_str2),
                getString(R.string.onl_fert55)
        };
        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(new OnlFert());
        mFragments.add(new OnlFerPlan());
        mFragments.add(new IrrigationRecord());

        MyViewPager mViewPager = findViewById(R.id.mViewPager_onlfer);
        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setScroll(true);
        mViewPager.setAdapter(mAdapter);

        SlidingTabLayout tabLayout = findViewById(R.id.mTabLayout_onlfer);
        tabLayout.setViewPager(mViewPager);

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
            default:
                break;

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public QMUITipDialog getmDialog() {
        return mDialog;
    }
}
