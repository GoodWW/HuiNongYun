package com.cdzp.huinongyun.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.cdzp.huinongyun.BaseActivity;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.adapter.MyPagerAdapter;
import com.cdzp.huinongyun.fragment.AddDevFragment;
import com.cdzp.huinongyun.fragment.AddFourFragment;
import com.cdzp.huinongyun.fragment.AddVideoFragment;
import com.cdzp.huinongyun.fragment.AddYinVidFragment;
import com.cdzp.huinongyun.util.MyViewPager;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.flyco.tablayout.SlidingTabLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加设备
 */
public class HomAddDevActivity extends BaseActivity {

    private Context context;
    private QMUITipDialog mDialog;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_home_add_dev);
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
        tv.setText(getString(R.string.hone_add_dev0));

        String[] mTitles = new String[]{
                getString(R.string.hone_add_dev1),
                getString(R.string.hone_add_dev2),
                getString(R.string.hone_add_dev3),
                getString(R.string.hone_add_dev4)
        };
        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(new AddDevFragment());
        mFragments.add(new AddVideoFragment());
        mFragments.add(new AddYinVidFragment());
        mFragments.add(new AddFourFragment());


        MyViewPager mViewPager = findViewById(R.id.ahad_mViewPager);
        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(mAdapter);

        SlidingTabLayout tabLayout = findViewById(R.id.mTabLayout);
        tabLayout.setViewPager(mViewPager);


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
            default:

                break;
        }
    }

    public QMUITipDialog getmDialog() {
        return mDialog;
    }
}
