package com.cdzp.huinongyun.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CleanUtils;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.adapter.MyPagerAdapter;
import com.cdzp.huinongyun.fragment.PestMonFragment;
import com.cdzp.huinongyun.fragment.SeedlMonitorFragment;
import com.cdzp.huinongyun.fragment.SoilMoistureFragment;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.LanguageUtil;
import com.cdzp.huinongyun.util.MyViewPager;
import com.cdzp.huinongyun.util.Toasts;
import com.flyco.tablayout.SlidingTabLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 四情监测
 */
public class FouSitMonActivity extends AutoLayoutActivity implements View.OnClickListener, OnMenuItemClickListener {

    private Context context;
    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;
    public static List<String[]> mList;
    private ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fou_sit_mon);
        init();
    }

    public void init() {
        context = this;

        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fitsSystemWindows(true)
                .statusBarColor(R.color.title)
                .init();

        TextView tv = findViewById(R.id.tv_title);
        tv.setText(R.string.terminal_str71);
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
        MenuObject menu51 = new MenuObject();
        menu51.setResource(R.drawable.system_sp_menu);
        menu51.setTitle(getString(R.string.fou_sit_mon_str3));
        MenuObject menu6 = new MenuObject();
        menu6.setResource(R.drawable.system_tj_menu);
        menu6.setTitle(getString(R.string.circle_tj));
        MenuObject menu7 = new MenuObject();
        menu7.setResource(R.drawable.clear_photo);
        menu7.setTitle(getString(R.string.fou_sit_mon_str5));


        menuObjects.add(close);
        if (Data.GhType != null) {
            switch (Data.GhType) {
                case "1":
                    menuObjects.add(menu1);
                    menuObjects.add(menu2);
                    menuObjects.add(menu3);
                    menuObjects.add(menu5);
                    menuObjects.add(menu6);
                    break;
                case "2":

                    break;
                case "3":

                    break;
                case "4":

                    break;
                case "5":

                    break;
                case "6":

                    break;
                case "7":
                    menuObjects.add(menu1);
                    menuObjects.add(menu4);
                    menuObjects.add(menu5);
                    menuObjects.add(menu51);
                    menuObjects.add(menu6);
                    menuObjects.add(menu7);
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
                getString(R.string.fou_sit_mon_str1),
                getString(R.string.fou_sit_mon_str2),
                getString(R.string.fou_sit_mon_str4)
        };
        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(new PestMonFragment());
        mFragments.add(new SeedlMonitorFragment());
        mFragments.add(new SoilMoistureFragment());

        MyViewPager mViewPager = findViewById(R.id.vp_afsm);
        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(mAdapter);

        SlidingTabLayout tabLayout = findViewById(R.id.slid_afsm);
        tabLayout.setViewPager(mViewPager);

        rl.setOnClickListener(this);
        findViewById(R.id.ll_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
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
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
        mList = null;
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        switch (position) {
            case 1:
                startActivity(EnvirDataActivity.class);
                break;
            case 3:
                switchVideo();
                break;
            case 4:
                showAlertDialog();
                break;
            case 5:
                startActivity(StatisticalFormActivity.class);
                break;
            case 6:
                if (CleanUtils.cleanInternalCache()) {
                    new QMUIDialog.MessageDialogBuilder(context)
                            .setTitle(getString(R.string.fou_sit_mon_str7))
                            .setMessage(getString(R.string.fou_sit_mon_str8))
                            .addAction(getString(R.string.envir_str1), new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                }
                            })
                            .addAction(0, getString(R.string.fou_sit_mon_str9),
                                    QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                                        @Override
                                        public void onClick(QMUIDialog dialog, int index) {
                                            dialog.dismiss();
                                            AppUtils.relaunchApp();
                                        }
                                    })
                            .create().show();
                } else
                    Toasts.showInfo(context, getString(R.string.fou_sit_mon_str6));
                break;
            default:

                break;
        }
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

    private void showAlertDialog() {
        if (mList != null && mList.size() > 0) {
            String[] strs = new String[mList.size()];
            for (int i = 0; i < mList.size(); i++) {
                strs[i] = mList.get(i)[0];
            }
            new AlertDialog.Builder(context)
                    .setItems(strs, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, YingVideoActivity.class);
                            intent.putExtra("ZaiQing", mList.get(which)[3]);
                            finish();
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    private void startActivity(Class<?> targetClass) {
        finish();
        Intent intent = new Intent(context, targetClass);
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
}
