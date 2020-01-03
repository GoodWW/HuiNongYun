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
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.cdzp.huinongyun.MyApplication;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.fragment.OnlReaTimeData;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.CircleMenuLayout;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.LanguageUtil;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.gyf.barlibrary.ImmersionBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhy.autolayout.AutoLayoutActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.cdzp.huinongyun.util.Data.pGhList;

/**
 * 终端控制页
 */
public class CircleActivity extends AutoLayoutActivity {

    private Context context;
    private TextView tv_ac;
    private String[] data;
    /**
     * 炫目的圆形菜单 秒秒钟高仿建行圆形菜单
     */
    private CircleMenuLayout mCircleMenuLayout;
    private ImmersionBar mImmersionBar;
    private boolean isEnglish;//是否中英文
    private QMUITipDialog mDialog;

    private String[] mItemTexts;
    private int[] mItemImgs;
    private Class mClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        context = this;
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();

        //Add the top margin size equals status bar's height for view.
        BarUtils.addMarginTopEqualStatusBarHeight(findViewById(R.id.ll_cir));
        initView();
    }

    public void initView() {
        tv_ac = findViewById(R.id.tv_ac);
        isEnglish = ((MyApplication) getApplication()).isEnglish();
        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();

        TextView tv1 = findViewById(R.id.tv_ac1);
        TextView tv2 = findViewById(R.id.tv_ac2);
        if (isEnglish) {
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            tv_ac.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        } else {
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
//            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
            tv_ac.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }

        try {
            data = getIntent().getStringArrayExtra("data");

            tv_ac.setText(data[1]);
            switch (data[0]) {
                case "1":
                case "9":
                case "12":
                    mItemTexts = new String[]{
                            getString(R.string.circle_hj),
                            getString(R.string.circle_bj),
                            getString(R.string.circle_yc),
                            getString(R.string.circle_sp),
                            getString(R.string.circle_tj)};
                    mItemImgs = new int[]{
                            R.drawable.system_hj,
                            R.drawable.system_jp,
                            R.drawable.system_yc,
                            R.drawable.system_sp,
                            R.drawable.system_tj};
                    break;
                case "2":
                case "10":
                    mItemTexts = new String[]{
                            getString(R.string.circle_hj),
                            getString(R.string.circle_bj),
                            getString(R.string.circle_sfj),
                            getString(R.string.circle_sp),
                            getString(R.string.circle_tj)};
                    mItemImgs = new int[]{
                            R.drawable.system_hj,
                            R.drawable.system_jp,
                            R.drawable.system_yc,
                            R.drawable.system_sp,
                            R.drawable.system_tj};
                    break;
                case "3":
                    mItemTexts = new String[]{
                            getString(R.string.circle_hj),
                            getString(R.string.circle_bj),
                            getString(R.string.circle_yc),
                            getString(R.string.circle_sp),
                            getString(R.string.circle_tj)};
                    mItemImgs = new int[]{
                            R.drawable.system_hj,
                            R.drawable.system_jp,
                            R.drawable.system_yc,
                            R.drawable.system_sp,
                            R.drawable.system_tj};
                    break;
                case "4":
                    mItemTexts = new String[]{
                            getString(R.string.circle_hj),
                            getString(R.string.circle_bj),
                            getString(R.string.circle_yc),
                            getString(R.string.circle_sp),
                            getString(R.string.circle_tj)};
                    mItemImgs = new int[]{
                            R.drawable.system_hj,
                            R.drawable.system_jp,
                            R.drawable.system_yc,
                            R.drawable.system_sp,
                            R.drawable.system_tj};
                    break;
                case "5":
                    mItemTexts = new String[]{
                            getString(R.string.circle_hj),
                            getString(R.string.circle_bj),
                            getString(R.string.circle_sp),
                            getString(R.string.circle_tj)};
                    mItemImgs = new int[]{
                            R.drawable.system_hj,
                            R.drawable.system_jp,
                            R.drawable.system_sp,
                            R.drawable.system_tj};
                    break;
                case "6":
                    mItemTexts = new String[]{
                            getString(R.string.circle_hj),
                            getString(R.string.circle_bj),
                            getString(R.string.circle_sp),
                            getString(R.string.circle_tj)};
                    mItemImgs = new int[]{
                            R.drawable.system_hj,
                            R.drawable.system_jp,
                            R.drawable.system_sp,
                            R.drawable.system_tj};
                    break;
                case "7":
                    mItemTexts = new String[]{
                            getString(R.string.circle_hj),
                            getString(R.string.circle_sq),
                            getString(R.string.circle_sp),
                            getString(R.string.circle_tj)};
                    mItemImgs = new int[]{
                            R.drawable.system_hj,
                            R.drawable.system_sq,
                            R.drawable.system_sp,
                            R.drawable.system_tj};
                    break;
                case "8":
                    mItemTexts = new String[]{
                            getString(R.string.circle_hj),
                            getString(R.string.circle_bj),
                            getString(R.string.circle_yc),
                            getString(R.string.circle_sp),
                            getString(R.string.circle_tj)};
                    mItemImgs = new int[]{
                            R.drawable.system_hj,
                            R.drawable.system_jp,
                            R.drawable.system_yc,
                            R.drawable.system_sp,
                            R.drawable.system_tj};
                    break;
                default:
                    mItemTexts = new String[]{
                            getString(R.string.circle_hj),
                            getString(R.string.circle_bj),
                            getString(R.string.circle_yc),
                            getString(R.string.circle_sq),
                            getString(R.string.circle_sp),
                            getString(R.string.circle_tj)};
                    mItemImgs = new int[]{
                            R.drawable.system_hj,
                            R.drawable.system_jp,
                            R.drawable.system_yc,
                            R.drawable.system_sq,
                            R.drawable.system_sp,
                            R.drawable.system_tj};
                    break;
            }

            mCircleMenuLayout = findViewById(R.id.id_menulayout);
            mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
            register();

            mCircleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
                @Override
                public void itemClick(View view, int pos) {
                    switchCircleMenu(pos);
                }

                @Override
                public void itemCenterClick(View view) {
                    finish();
                }
            });

        } catch (Exception e) {
            tv_ac.setText(getString(R.string.request_error7));
        }

        findViewById(R.id.ll_cir_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void switchCircleMenu(int pos) {
        if (data != null) {
            switch (data[0]) {
                case "1":
                    switch (pos) {
                        case 0://环境数据
                            startActivity(EnvirDataActivity.class);
                            break;
                        case 1://报警信息
                            startActivity(WarningActivity.class);
                            break;
                        case 2://远程控制
                            startActivity(RemoteControlActivity.class);
                            break;
                        case 3://视频信息
                            switchVideo();
                            break;
                        case 4://统计报表
                            startActivity(StatisticalFormActivity.class);
                            break;
                        default:

                            break;
                    }
                    break;
                case "2":
                    switch (pos) {
                        case 0:
                            startActivity(EnvirDataActivity.class);
                            break;
                        case 1:
                            startActivity(WarningActivity.class);
                            break;
                        case 2:
                            if (pGhList != null && pGhList.size() > 0) {
                                startActivity(FertActivity.class);
                            } else {
                                mClass = FertActivity.class;
                                getTerminal();
                            }
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
                case "3":
                    switch (pos) {
                        case 0:
                            startActivity(EnvirDataActivity.class);
                            break;
                        case 1:
                            startActivity(WarningActivity.class);
                            break;
                        case 2:
                            startActivity(RemoteControlActivity.class);
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
                case "4":
                    switch (pos) {
                        case 0:
                            startActivity(EnvirDataActivity.class);
                            break;
                        case 1:
                            startActivity(WarningActivity.class);
                            break;
                        case 2:
                            startActivity(RemoteControlActivity.class);
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
//                    Toasts.showSuccess(context, mItemTexts[pos]);
                    break;
                case "5":
                    switch (pos) {
                        case 0:
                            startActivity(EnvirDataActivity.class);
                            break;
                        case 1:
                            startActivity(WarningActivity.class);
                            break;
                        case 2:
                            switchVideo();
                            break;
                        case 3:
                            startActivity(StatisticalFormActivity.class);
                            break;
                        default:

                            break;
                    }
                    break;
                case "6":
                    switch (pos) {
                        case 0:
                            startActivity(EnvirDataActivity.class);
                            break;
                        case 1:
                            startActivity(WarningActivity.class);
                            break;
                        case 2:
                            switchVideo();
                            break;
                        case 3:
                            startActivity(StatisticalFormActivity.class);
                            break;
                        default:

                            break;
                    }
                    break;
                case "7":
                    switch (pos) {
                        case 0:
                            startActivity(EnvirDataActivity.class);
                            break;
                        case 1:
                            startActivity(FouSitMonActivity.class);
                            break;
                        case 2:
                            switchVideo();
                            break;
                        case 3:
                            startActivity(StatisticalFormActivity.class);
                            break;
                        default:

                            break;
                    }
                    break;
                case "8":
                    switch (pos) {
                        case 0:
                            startActivity(EnvirDataActivity.class);
                            break;
                        case 1:
                            startActivity(WarningActivity.class);
                            break;
                        case 2:
                            startActivity(RemoteControlActivity.class);
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
                case "9":
//                case "11":
                    switch (pos) {
                        case 0:
                            startActivity(EnvirDataActivity.class);
                            break;
                        case 1:
                            startActivity(WarningActivity.class);
                            break;
                        case 2:
                            startActivity(SolenoidActivity.class);
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
                case "10":
                    switch (pos) {
                        case 0:
                            startActivity(OnlReaTimeData.class);
                            break;
                        case 1:
//                            startActivity(WarningActivity.class);
                            break;
                        case 2:
                            if (pGhList != null && pGhList.size() > 0) {
                                startActivity(OnlineFert.class);
                            } else {
                                mClass = OnlineFert.class;
                                getTerminal();
                            }
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
                case "12":
                    switch (pos) {
                        case 0://环境数据
                            startActivity(EnvirDataActivity.class);
                            break;
                        case 1://报警信息
                            startActivity(WarningActivity.class);
                            break;
                        case 2://远程控制
                            startActivity(SprinklerActivity.class);
                            break;
                        case 3://视频信息
                            switchVideo();
                            break;
                        case 4://统计报表
                            startActivity(StatisticalFormActivity.class);
                            break;
                        default:

                            break;
                    }
//                    switch (pos) {
//                        case 0:
//                            startActivity(OnlReaTimeData.class);
//                            break;
//                        case 1:
//                            startActivity(WarningActivity.class);
//                            break;
//                        case 2:
//                            if (pGhList != null && pGhList.size() > 0) {
//
//                            } else {
//                                mClass = SprinklerActivity.class;
//                                getTerminal();
//                            }
//                            break;
//                        case 3:
//                            switchVideo();
//                            break;
//                        case 4:
//                            startActivity(StatisticalFormActivity.class);
//                            break;
//                        default:
//                            break;
//                    }
                    break;
                default:
                    Toasts.showSuccess(context, mItemTexts[pos]);
                    break;
            }
        } else Toasts.showError(context, getString(R.string.request_error7));
    }

    /**
     * 视频信息
     */
    private void switchVideo() {
        if (Data.videoResponse != null) {
            switch (Data.videoResponse.getData().get(0).getPalyType()) {
                case 1://萤石云视频
                    startActivity(YingVideoActivity.class);
                    break;
                case 2://海康HIDDNS
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

    private void startActivity(Class<?> targetClass) {
        Intent intent = new Intent(context, targetClass);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 666:
                    mDialog.dismiss();
                    if (pGhList != null && pGhList.size() > 0) {
                        if (mClass != null) startActivity(mClass);
                    } else Toasts.showInfo(context, getString(R.string.warn_str10));
                    break;
                default:
                    mDialog.dismiss();
                    break;
            }
        } else {
            mDialog.dismiss();
            Toasts.showError(context, message.getmResult());
        }
    }

    private void getTerminal() {
        if (Data.userResult != null && Data.GhType != null) {
            mDialog.show();
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"Type", Data.GhType});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("GreenHouseSel",
                    "/StringService/DataInfo.asmx", data, 666, context);
        } else {
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    private void register() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
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
    public void onResume() {
        super.onResume();
        register();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        register();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null) mImmersionBar.destroy();
        WebServiceManage.dispose();
        Data.pGhList = null;
        Data.locList = null;
    }
}
