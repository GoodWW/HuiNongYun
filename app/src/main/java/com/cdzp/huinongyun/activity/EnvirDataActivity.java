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
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.adapter.MyPagerAdapter;
import com.cdzp.huinongyun.fragment.HistoryEnvirFragment;
import com.cdzp.huinongyun.fragment.RealEnvirFragment;
import com.cdzp.huinongyun.fragment.YlyHisEnvFragment;
import com.cdzp.huinongyun.fragment.YlyRealEnvirFragment;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.MyViewPager;
import com.cdzp.huinongyun.util.Toasts;
import com.flyco.tablayout.SlidingTabLayout;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 环境数据
 */
public class EnvirDataActivity extends BaseActivity implements OnMenuItemClickListener {

    private Context context;
    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_envir_data);
    }

    @Override
    public void dealLogicBeforeInitView() {
        context = this;
    }

    @Override
    public void initView() {
        TextView tv = findViewById(R.id.tv_title);
        tv.setText(getString(R.string.title1));
        RelativeLayout rl = findViewById(R.id.rl_right);
        rl.setVisibility(View.VISIBLE);

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
        MenuObject menu4 = new MenuObject();
        menu4.setResource(R.drawable.system_sq_menu);
        menu4.setTitle(getString(R.string.circle_sq));
        MenuObject menu5 = new MenuObject();
        menu5.setResource(R.drawable.system_sp_menu);
        menu5.setTitle(getString(R.string.circle_sp));
        MenuObject menu6 = new MenuObject();
        menu6.setResource(R.drawable.system_tj_menu);
        menu6.setTitle(getString(R.string.circle_tj));
        MenuObject menu7 = new MenuObject();
        menu7.setResource(R.drawable.system_yc_menu);
        menu7.setTitle(getString(R.string.circle_sfj));

        menuObjects.add(close);
        if (Data.GhType != null) {
            switch (Data.GhType) {
                case "1":
                case "9":
                    menuObjects.add(menu1);
                    menuObjects.add(menu2);
                    menuObjects.add(menu3);
                    menuObjects.add(menu5);
                    menuObjects.add(menu6);
                    break;
                case "2":
                    menuObjects.add(menu1);
                    menuObjects.add(menu2);
                    menuObjects.add(menu7);
                    menuObjects.add(menu5);
                    menuObjects.add(menu6);
                    break;
                case "3":
                    menuObjects.add(menu1);
                    menuObjects.add(menu2);
                    menuObjects.add(menu3);
                    menuObjects.add(menu5);
                    menuObjects.add(menu6);
                    break;
                case "4":
                    menuObjects.add(menu1);
                    menuObjects.add(menu2);
                    menuObjects.add(menu3);
                    menuObjects.add(menu5);
                    menuObjects.add(menu6);
                    break;
                case "5":
                    menuObjects.add(menu1);
                    menuObjects.add(menu2);
                    menuObjects.add(menu5);
                    menuObjects.add(menu6);
                    break;
                case "6":
                    menuObjects.add(menu1);
                    menuObjects.add(menu2);
                    menuObjects.add(menu5);
                    menuObjects.add(menu6);
                    break;
                case "7":
                    menuObjects.add(menu1);
                    menuObjects.add(menu4);
                    menuObjects.add(menu5);
                    menuObjects.add(menu6);
                    break;
                case "8":
                    menuObjects.add(menu1);
                    menuObjects.add(menu2);
                    menuObjects.add(menu3);
                    menuObjects.add(menu5);
                    menuObjects.add(menu6);
                    break;
                default:

                    break;
            }
        }

        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(menuObjects);
        menuParams.setClosableOutside(true);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);

        String[] mTitles = new String[]{
                getString(R.string.envir_fra1),
                getString(R.string.envir_fra2)
        };
        List<Fragment> mFragments = new ArrayList<>();
        if (Data.userResult != null && Data.userResult.getData().get(0).getUserName().equals("ylyzh")) {
            mFragments.add(new YlyRealEnvirFragment());
            mFragments.add(new YlyHisEnvFragment());
        } else {
            mFragments.add(new RealEnvirFragment());
            mFragments.add(new HistoryEnvirFragment());
        }


        MyViewPager mViewPager = findViewById(R.id.vp_aed);
        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(mAdapter);

        SlidingTabLayout tabLayout = findViewById(R.id.slid_aed);
        tabLayout.setViewPager(mViewPager);

        rl.setOnClickListener(this);
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
            case R.id.rl_right:
                mMenuDialogFragment.show(fragmentManager, "ContextMenuDialogFragment");
                break;
            default:

                break;
        }
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        if (Data.GhType != null) {
            switch (Data.GhType) {
                case "1":
                    switch (position) {
                        case 1:
//                        startActivity(EnvirDataActivity.class);
                            break;
                        case 2:
                            startActivity(WarningActivity.class);
                            break;
                        case 3:
                            startActivity(RemoteControlActivity.class);
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
                    break;
                case "2":
                    switch (position) {
                        case 1:
//                            startActivity(EnvirDataActivity.class);
                            break;
                        case 2:
                            startActivity(WarningActivity.class);
                            break;
                        case 3:
                            startActivity(FertActivity.class);
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
                    break;
                case "3":
                    switch (position) {
                        case 1:
//                        startActivity(EnvirDataActivity.class);
                            break;
                        case 2:
                            startActivity(WarningActivity.class);
                            break;
                        case 3:
                            startActivity(RemoteControlActivity.class);
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
                    break;
                case "4":
                    switch (position) {
                        case 1:
//                        startActivity(EnvirDataActivity.class);
                            break;
                        case 2:
                            startActivity(WarningActivity.class);
                            break;
                        case 3:
                            startActivity(RemoteControlActivity.class);
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
                    break;
                case "5":
                    switch (position) {
                        case 1:
//                            startActivity(EnvirDataActivity.class);
                            break;
                        case 2:
                            startActivity(WarningActivity.class);
                            break;
                        case 3:
                            switchVideo();
                            break;
                        case 4:
                            startActivity(StatisticalFormActivity.class);
                            break;
                        default:

                            break;
                    }
                    break;
                case "6":
                    switch (position) {
                        case 1:
//                            startActivity(EnvirDataActivity.class);
                            break;
                        case 2:
                            startActivity(WarningActivity.class);
                            break;
                        case 3:
                            switchVideo();
                            break;
                        case 4:
                            startActivity(StatisticalFormActivity.class);
                            break;
                        default:

                            break;
                    }
                    break;
                case "7":
                    switch (position) {
                        case 1:
//                            startActivity(EnvirDataActivity.class);
                            break;
                        case 2:
                            startActivity(FouSitMonActivity.class);
                            break;
                        case 3:
                            switchVideo();
                            break;
                        case 4:
                            startActivity(StatisticalFormActivity.class);
                            break;
                        default:

                            break;
                    }
                    break;
                case "8":
                    switch (position) {
                        case 1:
//                        startActivity(EnvirDataActivity.class,"1");
                            break;
                        case 2:
                            startActivity(WarningActivity.class);
                            break;
                        case 3:
                            startActivity(RemoteControlActivity.class);
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
                    break;
                case "9":
                    switch (position) {
                        case 1:
//                        startActivity(EnvirDataActivity.class);
                            break;
                        case 2:
                            startActivity(WarningActivity.class);
                            break;
                        case 3:
                            startActivity(SolenoidActivity.class);
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
                    break;
                default:

                    break;
            }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
