package com.cdzp.huinongyun.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdzp.huinongyun.BaseActivity;
import com.cdzp.huinongyun.MyApplication;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.adapter.MyPagerAdapter;
import com.cdzp.huinongyun.fragment.SolHosConFragment;
import com.cdzp.huinongyun.fragment.SolPlanIrrFragment;
import com.cdzp.huinongyun.fragment.SolSelfConParFragment;
import com.cdzp.huinongyun.fragment.SolTerRenFragment;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.MyViewPager;
import com.cdzp.huinongyun.util.Toasts;
import com.flyco.tablayout.SlidingTabLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 无限电磁阀远程控制
 */
public class SolenoidActivity extends BaseActivity implements OnMenuItemClickListener {

    private Context context;
    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private boolean isEnglish;
    private QMUITipDialog mDialog;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_fert);
    }

    @Override
    public void dealLogicBeforeInitView() {
        context = this;
    }

    @Override
    public void initView() {
        TextView tv = findViewById(R.id.tv_title);
        tv.setText(getString(R.string.terminal_str9));
        RelativeLayout rl = findViewById(R.id.rl_right);
        rl.setVisibility(View.VISIBLE);

        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();

        fragmentManager = getSupportFragmentManager();
        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.icn_close);
        MenuObject menu1 = new MenuObject();
        menu1.setResource(R.drawable.system_hj_menu);
        menu1.setTitle(getString(R.string.circle_hj));
        MenuObject menu2 = new MenuObject();
        menu2.setResource(R.drawable.system_jp_menu);
        menu2.setTitle(getString(R.string.circle_bj));
        MenuObject menu3 = new MenuObject();
        menu3.setResource(R.drawable.system_yc_menu);
        menu3.setTitle(getString(R.string.circle_yc));
        MenuObject menu5 = new MenuObject();
        menu5.setResource(R.drawable.system_sp_menu);
        menu5.setTitle(getString(R.string.circle_sp));
        MenuObject menu6 = new MenuObject();
        menu6.setResource(R.drawable.system_tj_menu);
        menu6.setTitle(getString(R.string.circle_tj));

        menuObjects.add(close);
        menuObjects.add(menu1);
        menuObjects.add(menu2);
        menuObjects.add(menu3);
        menuObjects.add(menu5);
        menuObjects.add(menu6);

        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(menuObjects);
        menuParams.setClosableOutside(true);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);

        isEnglish = ((MyApplication) getApplication()).isEnglish();

        String[] mTitles = new String[]{
                getString(R.string.solenoid_str1),
                getString(R.string.solenoid_str2),
                getString(R.string.solenoid_str3),
                getString(R.string.solenoid_str4)
        };
        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(new SolHosConFragment());
        mFragments.add(new SolSelfConParFragment());
        mFragments.add(new SolPlanIrrFragment());
        mFragments.add(new SolTerRenFragment());

        MyViewPager mViewPager = findViewById(R.id.mViewPager_fer);
        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(mAdapter);

        SlidingTabLayout tabLayout = findViewById(R.id.mTabLayout_fer);
        tabLayout.setViewPager(mViewPager);

        findViewById(R.id.ll_back).setOnClickListener(this);
        rl.setOnClickListener(this);
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
            case R.id.rl_right:
                mMenuDialogFragment.show(fragmentManager, "ContextMenuDialogFragment");
                break;
            default:

                break;
        }
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        switch (position) {
            case 1:
                startActivity(EnvirDataActivity.class);
                break;
            case 2:
                startActivity(WarningActivity.class);
                break;
            case 3:

                break;
            case 4:
                switchVideo();
                break;
            case 5:
                startActivity(StatisticalFormActivity.class);
                break;
            default:

                break;
        }
    }

    protected void startActivity(Class<?> targetClass) {
        finish();
        Intent intent = new Intent(context, targetClass);
        startActivity(intent);
    }

    private void switchVideo() {
        if (Data.videoResponse != null) {
            switch (Data.videoResponse.getData().get(0).getPalyType()) {
                case 1:
                    startActivity(YingVideoActivity.class);
                    break;
                case 2:
                    startActivity(VideoActivity.class);
                    break;
                case 3:
                    new AlertDialog.Builder(context)
                            .setItems(new String[]{getString(R.string.video_str27),
                                    getString(R.string.video_str28)}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            startActivity(YingVideoActivity.class);
                                            break;
                                        case 1:
                                            startActivity(VideoActivity.class);
                                            break;
                                        default:

                                            break;
                                    }
                                    dialog.dismiss();
                                }
                            }).show();
                    break;
                default:

                    break;
            }
        } else Toasts.showInfo(context, getString(R.string.request_error9));
    }

    public boolean isEnglish() {
        return isEnglish;
    }

    public QMUITipDialog getmDialog() {
        return mDialog;
    }
}
