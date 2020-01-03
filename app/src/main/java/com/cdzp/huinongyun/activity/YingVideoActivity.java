package com.cdzp.huinongyun.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.cdzp.huinongyun.MyApplication;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.bean.DeviceListResult;
import com.cdzp.huinongyun.bean.DeviceListResults;
import com.cdzp.huinongyun.bean.VideoInfoResponse;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.LanguageUtil;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.cdzp.huinongyun.util.ZoomListenter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.barlibrary.ImmersionBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.videogo.exception.BaseException;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZConstants.EZPTZAction;
import com.videogo.openapi.EZConstants.EZPTZCommand;
import com.videogo.openapi.EZConstants.EZRealPlayConstants;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.EZPlayer;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;
import com.zhy.autolayout.AutoLayoutActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.blankj.utilcode.constant.PermissionConstants.STORAGE;

/**
 * 萤石云视频
 */
public class YingVideoActivity extends AutoLayoutActivity implements View.OnClickListener, OnMenuItemClickListener {

    private Context context;
    /**
     * android 4.4以上沉浸式以及bar的管理
     */
    private ImmersionBar mImmersionBar;
    private FragmentManager fragmentManager;
    /**
     * 上下文菜单相关
     */
    private ContextMenuDialogFragment mMenuDialogFragment;
    private View include;
    private LinearLayout ll_kz, ll_title, ll_tj, ll_tj0;
    /**
     * 萤石云
     */
    private EZPlayer player;
    private Handler handler;
    private SurfaceHolder mHolder;
    /**
     * 提供一个浮层展示在屏幕中间
     */
    private QMUITipDialog mDialog;
    private boolean isPlay, isVideoLevel, isTalk, isClick = true;
    private QMUIBottomSheet bottomSheet;
    private List<String[]> listDev;
    private List<List<String[]>> listCam;
    private TextView tv_dev, tv_camera, tv_hd, tv_balanced, tv_kz, tv_dj, tv_hd1, tv_balanced1;
    private ImageView iv_play, iv_play1, im_top, im_bottom, im_left, im_right;
    private int position1 = -1, position2 = -1, videoLevel, videoLevels = -998;
    private ZoomListenter zListenter;
    private AnimationDrawable ad_top, ad_bottom, ad_left, ad_right;
    private String TAG = "YingVideoActivity";
    private EZPTZCommand mCommand;
    /**
     * 计时器
     */
    private CountDownTimer mDownTimer;
    private String mCIID;//灾情监控
    public static View floatView;
    public static SurfaceHolder floatHolder;
    /**
     * 萤石云
     */
    public static EZPlayer floatPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar
                .fitsSystemWindows(true)
                .statusBarColor(R.color.title)
                .init();
        setContentView(R.layout.activity_ying_video);
        init();
    }

    public void init() {
        context = this;
        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();

        try {
            mCIID = getIntent().getStringExtra("ZaiQing");
        } catch (Exception e) {
            mCIID = null;
        }

        include = findViewById(R.id.ayv_include);

        ll_title = findViewById(R.id.ll_ayv_title);
        ll_kz = findViewById(R.id.ll_ayv_kz);
        ll_tj = findViewById(R.id.ll_ayv_tj);
        ll_tj0 = findViewById(R.id.ll_ayv_tj0);

        mDownTimer = new CountDownTimer(3500, 3500) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                ll_tj.setVisibility(View.GONE);
            }
        };

        TextView tv = findViewById(R.id.tv_title);
        if (mCIID == null) {
            tv.setText(getString(R.string.video_str3));
        } else {
            tv.setText(getString(R.string.fou_sit_mon_str3));
        }
        RelativeLayout rl = findViewById(R.id.rl_right);
        rl.setVisibility(View.VISIBLE);

        fragmentManager = getSupportFragmentManager();
        List<MenuObject> menuObjects = new ArrayList<>();

        //全局bar相关设置
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


        tv_dev = findViewById(R.id.tv_ayv_dev);
        tv_camera = findViewById(R.id.tv_ayv_camera);
        iv_play = findViewById(R.id.iv_play_video);
        tv_hd = findViewById(R.id.tv_ayv_hd);
        tv_balanced = findViewById(R.id.tv_ayv_balanced);
//        tv_flunet = findViewById(R.id.tv_ayv_flunet);

        iv_play1 = findViewById(R.id.iv_play_video1);
        tv_hd1 = findViewById(R.id.tv_ayv_hd1);
        tv_balanced1 = findViewById(R.id.tv_ayv_balanced1);
//        tv_flunet1 = findViewById(R.id.tv_ayv_flunet1);

        tv_kz = findViewById(R.id.tv_kz);
        tv_dj = findViewById(R.id.tv_dj);

        im_top = findViewById(R.id.im_top);
        im_top.setBackgroundResource(R.drawable.frame_1);
        im_bottom = findViewById(R.id.im_bottom);
        im_bottom.setBackgroundResource(R.drawable.frame_2);
        im_left = findViewById(R.id.im_left);
        im_left.setBackgroundResource(R.drawable.frame_3);
        im_right = findViewById(R.id.im_right);
        im_right.setBackgroundResource(R.drawable.frame_4);

        ad_top = (AnimationDrawable) im_top.getBackground();
        ad_bottom = (AnimationDrawable) im_bottom.getBackground();
        ad_left = (AnimationDrawable) im_left.getBackground();
        ad_right = (AnimationDrawable) im_right.getBackground();
        SurfaceView surfaceView = findViewById(R.id.ayv_mSurfaceView);
        zListenter = new ZoomListenter();
        zListenter.setListener(new ZoomListenter.Listener() {
            @Override
            public void callback(int num) {
                switch (num) {
                    case 1:
                        im_top.setVisibility(View.VISIBLE);
                        ad_top.start();
                        mCommand = EZPTZCommand.EZPTZCommandUp;
                        controlPTZ(EZPTZAction.EZPTZActionSTART);
                        if (ScreenUtils.isLandscape()) isClick = false;
                        break;
                    case 2:
                        im_bottom.setVisibility(View.VISIBLE);
                        ad_bottom.start();
                        mCommand = EZPTZCommand.EZPTZCommandDown;
                        controlPTZ(EZPTZAction.EZPTZActionSTART);
                        if (ScreenUtils.isLandscape()) isClick = false;
                        break;
                    case 3:
                        im_left.setVisibility(View.VISIBLE);
                        ad_left.start();
                        mCommand = EZPTZCommand.EZPTZCommandLeft;
                        controlPTZ(EZPTZAction.EZPTZActionSTART);
                        if (ScreenUtils.isLandscape()) isClick = false;
                        break;
                    case 4:
                        im_right.setVisibility(View.VISIBLE);
                        ad_right.start();
                        mCommand = EZPTZCommand.EZPTZCommandRight;
                        controlPTZ(EZPTZAction.EZPTZActionSTART);
                        if (ScreenUtils.isLandscape()) isClick = false;
                        break;
                    case 5:
                        mCommand = EZPTZCommand.EZPTZCommandZoomIn;
                        controlPTZ(EZPTZAction.EZPTZActionSTART);
                        if (ScreenUtils.isLandscape()) isClick = false;
                        break;
                    case 6:
                        mCommand = EZPTZCommand.EZPTZCommandZoomOut;
                        controlPTZ(EZPTZAction.EZPTZActionSTART);
                        if (ScreenUtils.isLandscape()) isClick = false;
                        break;
                    case 7:
                        stopCommand();
                        if (mCommand != null)
                            controlPTZ(EZPTZAction.EZPTZActionSTOP);
                        if (ScreenUtils.isLandscape() && isClick) {
                            switch (ll_tj.getVisibility()) {
                                case 0:
                                    ll_tj.setVisibility(View.GONE);
                                    break;
                                default:
                                    mDownTimer.cancel();
                                    ll_tj.setVisibility(View.VISIBLE);
                                    mDownTimer.start();
                                    break;
                            }
                        }
                        isClick = true;
                        break;
                    default:
                        Log.d("Video", "default：" + num);
                        break;
                }
            }
        });
        surfaceView.setOnTouchListener(zListenter);
        mHolder = surfaceView.getHolder();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case EZRealPlayConstants.MSG_REALPLAY_PLAY_SUCCESS:
                        isPlay = true;
                        iv_play.setBackgroundResource(R.drawable.stop);
                        iv_play1.setBackgroundResource(R.drawable.stop);
                        setColor();
                        if (isVideoLevel) {
                            isVideoLevel = false;
                            switch (videoLevel) {
                                case 1:
                                    tv_hd.setTextColor(getResources().getColor(R.color.video1));
                                    tv_hd1.setTextColor(getResources().getColor(R.color.video1));
                                    break;
                                case 2:
                                    tv_balanced.setTextColor(getResources().getColor(R.color.video1));
                                    tv_balanced1.setTextColor(getResources().getColor(R.color.video1));
                                    break;
                                case 3:
//                                    tv_flunet.setTextColor(getResources().getColor(R.color.video1));
//                                    tv_flunet1.setTextColor(getResources().getColor(R.color.video1));
                                    tv_balanced.setTextColor(getResources().getColor(R.color.video1));
                                    tv_balanced1.setTextColor(getResources().getColor(R.color.video1));
                                    break;
                            }
                        }
                        mDialog.dismiss();
                        break;
                    case EZRealPlayConstants.MSG_REALPLAY_PLAY_FAIL:
                        isPlay = false;
                        iv_play.setBackgroundResource(R.drawable.play);
                        iv_play1.setBackgroundResource(R.drawable.play);
                        mDialog.dismiss();
                        Toasts.showInfo(context, getString(R.string.video_str16));
                        break;
                    case EZRealPlayConstants.MSG_REALPLAY_STOP_SUCCESS:
                        isPlay = false;
                        iv_play.setBackgroundResource(R.drawable.play);
                        iv_play1.setBackgroundResource(R.drawable.play);
                        break;
                }
            }
        };

        findViewById(R.id.ll_back).setOnClickListener(this);
        rl.setOnClickListener(this);
        findViewById(R.id.ll_ayv1).setOnClickListener(this);
        findViewById(R.id.ll_ayv2).setOnClickListener(this);
        findViewById(R.id.ll_kz).setOnClickListener(this);
        findViewById(R.id.ll_pz).setOnClickListener(this);
        findViewById(R.id.ll_dj).setOnClickListener(this);
        findViewById(R.id.ll_xf).setOnClickListener(this);
        findViewById(R.id.iv_fullscreen_video).setOnClickListener(this);
        iv_play.setOnClickListener(this);
        tv_hd.setOnClickListener(this);
        tv_balanced.setOnClickListener(this);
//        tv_flunet.setOnClickListener(this);

        iv_play1.setOnClickListener(this);
        tv_hd1.setOnClickListener(this);
        tv_balanced1.setOnClickListener(this);
//        tv_flunet1.setOnClickListener(this);
        findViewById(R.id.iv_fullscreen_video1).setOnClickListener(this);
        findViewById(R.id.ll_ayv_tj).setOnClickListener(this);

        register();
//        if (EZOpenSDK.initLib(getApplication(), "025441f60a8d4364b068eb2a43c5bba3")) {
//            EZOpenSDK.getInstance().setAccessToken("944b2f43986204b24e10a576f15f508b");
//            mDialog.show();
//            getDeviceList();
//        } else {
//            Toasts.showInfo(context, getString(R.string.video_str10));
//        }
        getAppKey();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 1:
                    parseVideoGetPlayInfo(message.getmResult());
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

    /**
     * 初始化视频
     *
     * @param s
     */
    private void parseVideoGetPlayInfo(String s) {
        VideoInfoResponse response = new Gson().fromJson(s, VideoInfoResponse.class);
        Log.e(TAG, "测试" + response.getData().get(0).getAppKey());
        if (response.isSuccess()) {
            if (EZOpenSDK.initLib(getApplication(), response.getData().get(0).getAppKey())) {
                EZOpenSDK.getInstance().setAccessToken(response.getData().get(0).getAccessToken());
                getDeviceList();
            } else {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.video_str10));
            }
        } else {
            mDialog.dismiss();
            Toasts.showInfo(context, response.getMsg());
        }
    }

    /**
     * 获取设备和通道
     */
    private void getDeviceList() {
        if (mCIID == null && Data.videoResponse == null) {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.request_error9));
            return;
        }
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String mResult = "";
                try {
                    String nameSpace = "http://tempuri.org/";
                    String methodName;
                    String url = "http://101.200.193.60:9099/zpys7StringService.asmx";
                    String soapAction;
                    SoapObject soapObject;
                    if (mCIID == null) {
                        methodName = "SubdomainDeviceList";
//                        methodName = "SubdomainDeviceListOrderByName";//重庆数谷农场
                        soapAction = "http://tempuri.org/SubdomainDeviceList";
//                        soapAction = "http://tempuri.org/SubdomainDeviceListOrderByName";//重庆数谷农场
                        soapObject = new SoapObject(nameSpace, methodName);
                        soapObject.addProperty("SmID", Data.videoResponse.getData().get(0).getYs7SmID());
                    } else {
                        methodName = "LiveGetInfoByID";
                        soapAction = "http://tempuri.org/LiveGetInfoByID";
                        soapObject = new SoapObject(nameSpace, methodName);
                        soapObject.addProperty("CIID", mCIID);
                    }

                    // 生成调用WebService方法调用的soap信息，并且指定Soap版本
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                    envelope.bodyOut = soapObject;
                    // 是否调用DotNet开发的WebService
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(soapObject);

                    HttpTransportSE transport = new HttpTransportSE(url);
                    transport.debug = true;

                    try {
                        transport.call(soapAction, envelope);
                        mResult = envelope.getResponse().toString();
                    } catch (IOException e) {
                        Log.d(TAG, e.getMessage());
                    } catch (XmlPullParserException e) {
                        Log.d(TAG, e.getMessage());
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
                emitter.onNext(mResult);
            }
        }).subscribeOn(Schedulers.io())
                .map(new Function<String, List<DeviceListResults>>() {
                    @Override
                    public List<DeviceListResults> apply(String s) throws Exception {
                        if (s.length() > 0) {
                            DeviceListResult result = new Gson().fromJson(s, DeviceListResult.class);
                            if (result.isIsSuccess()) {
                                return new Gson().fromJson(result.getResult(), new TypeToken<List<DeviceListResults>>() {
                                }.getType());
                            } else return null;
                        } else return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<DeviceListResults>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<DeviceListResults> list) {
                        if (list != null) {
                            if (list.size() > 0) {
                                listDev = new ArrayList<>();
                                listCam = new ArrayList<>();
                                for (int i = 0; i < list.size(); i++) {
                                    listDev.add(new String[]{list.get(i).getDeviceName(), list.get(i).getDeviceSerial()});
                                    List<String[]> cam = new ArrayList<>();
                                    for (int i1 = 0; i1 < list.get(i).getChannels().size(); i1++) {
                                        cam.add(new String[]{list.get(i).getChannels().get(i1).getCameName()
                                                , list.get(i).getChannels().get(i1).getChannelNo() + ""});
                                    }
                                    listCam.add(cam);
                                }
                                position1 = 0;
                                tv_dev.setText(listDev.get(position1)[0]);
                                if (listCam.size() > 0) {
                                    position2 = 0;
                                    tv_camera.setText(listCam.get(position1).get(position2)[0]);
                                    startRealPlay();//开始播放
                                } else {
                                    mDialog.dismiss();
                                }
                            } else {
                                mDialog.dismiss();
                                Toasts.showInfo(context, getString(R.string.video_str12));
                            }
                        } else {
                            mDialog.dismiss();
                            Toasts.showInfo(context, getString(R.string.video_str11));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mDialog.dismiss();
                        Toasts.showInfo(context, getString(R.string.video_str11));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getAppKey() {
        if (Data.userResult != null) {
            mDialog.show();
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode() + ""});
            WebServiceManage.requestData("VideoGetPlayInfo", "/StringService/DeviceSet.asmx", data, 1, context);
        } else Toasts.showError(context, getString(R.string.request_error9));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_ayv1:
                showBottomSheet1();
                break;
            case R.id.ll_ayv2:
                showBottomSheet2();
                break;
            case R.id.iv_play_video:
                if (isPlay) {
                    if (player != null) {
                        player.stopRealPlay();
                    }
                } else {
                    if (tv_dev.getText().length() > 0) {
                        if (tv_camera.getText().length() > 0) {
                            mDialog.show();
                            startRealPlay();
                        } else Toasts.showInfo(context, getString(R.string.video_str15));
                    } else Toasts.showInfo(context, getString(R.string.video_str13));
                }
                break;
            case R.id.tv_ayv_hd:
                if (player != null && tv_dev.getText().length() > 0 && tv_camera.getText().length() > 0) {
                    videoLevel = 1;
                    videoLevels = EZConstants.EZVideoLevel.VIDEO_LEVEL_HD.getVideoLevel();
                    setVideoLevel(EZConstants.EZVideoLevel.VIDEO_LEVEL_HD.getVideoLevel());
                } else Toasts.showInfo(context, getString(R.string.request_error6));
                break;
            case R.id.tv_ayv_balanced:
                if (player != null && tv_dev.getText().length() > 0 && tv_camera.getText().length() > 0) {
//                    videoLevel = 2;
//                    setVideoLevel(EZConstants.EZVideoLevel.VIDEO_LEVEL_BALANCED.getVideoLevel());
                    videoLevel = 3;
                    videoLevels = EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET.getVideoLevel();
                    setVideoLevel(EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET.getVideoLevel());
                } else Toasts.showInfo(context, getString(R.string.request_error6));
                break;
//            case R.id.tv_ayv_flunet:
//                if (tv_dev.getText().length() > 0 && tv_camera.getText().length() > 0) {
//                    videoLevel = 3;
//                    setVideoLevel(EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET.getVideoLevel());
//                } else Toasts.showInfo(context, getString(R.string.request_error6));
//                break;
            case R.id.ll_kz:
                if (player != null && tv_dev.getText().length() > 0 && tv_camera.getText().length() > 0 && isPlay) {
                    if (zListenter.isOntouch()) {
                        zListenter.setOntouch(false);
                        tv_kz.setTextColor(getResources().getColor(R.color.tvRealEnvir));
                        Toasts.showInfo(context, getString(R.string.video_str19));
                    } else {
                        zListenter.setOntouch(true);
                        tv_kz.setTextColor(getResources().getColor(R.color.video2));
                        Toasts.showInfo(context, getString(R.string.video_str18));
                    }
                } else Toasts.showInfo(context, getString(R.string.video_str29));
                break;
            case R.id.ll_pz:
                if (player != null && tv_dev.getText().length() > 0 && tv_camera.getText().length() > 0) {
                    isSaveBitmap();
                } else Toasts.showInfo(context, getString(R.string.video_str29));
                break;
            case R.id.ll_dj:
                if (isTalk) {
                    stoptTalk();
                } else startTalk();
                break;
            case R.id.iv_fullscreen_video:
                if (ScreenUtils.isLandscape()) {
                    portrait();
                    ScreenUtils.setPortrait(this);
                } else {
                    if (tv_dev.getText().length() > 0 && tv_camera.getText().length() > 0) {
                        landscape();
                        ScreenUtils.setLandscape(this);
                    } else Toasts.showInfo(context, getString(R.string.video_str29));
                }
                break;
            case R.id.iv_play_video1:
                if (isPlay) {
                    if (player != null) {
                        player.stopRealPlay();
                    }
                } else {
                    if (tv_dev.getText().length() > 0) {
                        if (tv_camera.getText().length() > 0) {
                            mDialog.show();
                            startRealPlay();
                        } else Toasts.showInfo(context, getString(R.string.video_str15));
                    } else Toasts.showInfo(context, getString(R.string.video_str13));
                }
                break;
            case R.id.tv_ayv_hd1:
                if (player != null && tv_dev.getText().length() > 0 && tv_camera.getText().length() > 0) {
                    videoLevel = 1;
                    setVideoLevel(EZConstants.EZVideoLevel.VIDEO_LEVEL_HD.getVideoLevel());
                } else Toasts.showInfo(context, getString(R.string.video_str20));
                break;
            case R.id.tv_ayv_balanced1:
                if (player != null && tv_dev.getText().length() > 0 && tv_camera.getText().length() > 0) {
//                    videoLevel = 2;
//                    setVideoLevel(EZConstants.EZVideoLevel.VIDEO_LEVEL_BALANCED.getVideoLevel());
                    videoLevel = 3;
                    setVideoLevel(EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET.getVideoLevel());
                } else Toasts.showInfo(context, getString(R.string.video_str20));
                break;
//            case R.id.tv_ayv_flunet1:
//                if (tv_dev.getText().length() > 0 && tv_camera.getText().length() > 0) {
//                    videoLevel = 3;
//                    setVideoLevel(EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET.getVideoLevel());
//                } else Toasts.showInfo(context, getString(R.string.request_error6));
//                break;
            case R.id.iv_fullscreen_video1:
                if (ScreenUtils.isLandscape()) {
                    portrait();
                    ScreenUtils.setPortrait(this);
                } else {
                    landscape();
                    ScreenUtils.setLandscape(this);
                }
                break;
            case R.id.rl_right:
                mMenuDialogFragment.show(fragmentManager, "ContextMenuDialogFragment");
                break;
            case R.id.ll_xf:
                if (player != null && tv_dev.getText().length() > 0 && tv_camera.getText().length() > 0) {
                    initFloat();
                } else Toasts.showInfo(context, getString(R.string.video_str20));
                break;
            default:

                break;
        }
    }

    /**
     * 横屏设置
     */
    private void landscape() {
        BarUtils.setStatusBarVisibility(this, false);
        mImmersionBar.init();
        include.setVisibility(View.GONE);
        ll_title.setVisibility(View.GONE);
        ll_kz.setVisibility(View.GONE);
        ll_tj0.setVisibility(View.GONE);
        zListenter.setOntouch(true);
        tv_kz.setTextColor(getResources().getColor(R.color.video2));
        ll_tj.setVisibility(View.VISIBLE);
        mDownTimer.start();
    }

    /**
     * 竖屏设置
     */
    private void portrait() {
        BarUtils.setStatusBarVisibility(this, true);
        BarUtils.addMarginTopEqualStatusBarHeight(include);
        include.setVisibility(View.VISIBLE);
        ll_title.setVisibility(View.VISIBLE);
        ll_kz.setVisibility(View.VISIBLE);
        ll_tj0.setVisibility(View.VISIBLE);
        zListenter.setOntouch(false);
        tv_kz.setTextColor(getResources().getColor(R.color.tvRealEnvir));
        ll_tj.setVisibility(View.GONE);
    }

    /**
     * 选择清晰度
     *
     * @param videoLevel
     */
    private void setVideoLevel(final int videoLevel) {
        if (tv_dev.getText().length() > 0) {
            if (tv_camera.getText().length() > 0) {
                mDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EZOpenSDK.getInstance().setVideoLevel(listDev.get(position1)[1],
                                    Integer.parseInt(listCam.get(position1).get(position2)[1]),
                                    videoLevel);
                            isVideoLevel = true;
                            startRealPlay();
                        } catch (BaseException e) {
                            mDialog.dismiss();
                        }
                    }
                }).start();
            } else Toasts.showInfo(context, getString(R.string.video_str15));
        } else Toasts.showInfo(context, getString(R.string.video_str13));
    }

    private void showBottomSheet1() {
        if (listDev != null) {
            if (listDev.size() > 0) {
                if (bottomSheet == null) {
                    BottomListSheetBuilder mBuilder = new BottomListSheetBuilder(context, true)
                            .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                                @Override
                                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                    position1 = position;
                                    tv_dev.setText(listDev.get(position1)[0]);
                                    position2 = -1;
                                    tv_camera.setText("");
                                    zListenter.setOntouch(false);
                                    tv_kz.setTextColor(getResources().getColor(R.color.tvRealEnvir));
                                    dialog.dismiss();
                                    initTv_camera();
                                }
                            });
                    for (int i = 0; i < listDev.size(); i++) {
                        mBuilder.addItem(listDev.get(i)[0]);
                    }
                    mBuilder.setCheckedIndex(position1);
                    bottomSheet = mBuilder.build();
                }
                bottomSheet.show();
            } else
                Toasts.showInfo(context, getString(R.string.video_str12));
        } else {
            mDialog.show();
            getDeviceList();
        }
    }

    private void showBottomSheet2() {
        if (tv_dev.getText().length() > 0) {
            if (listCam != null && listCam.size() > position1 && listCam.get(position1).size() > 0) {
                BottomListSheetBuilder mBuilder = new BottomListSheetBuilder(context, true)
                        .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                position2 = position;
                                tv_camera.setText(listCam.get(position1).get(position2)[0]);
                                dialog.dismiss();
                                mDialog.show();
                                zListenter.setOntouch(false);
                                tv_kz.setTextColor(getResources().getColor(R.color.tvRealEnvir));
                                startRealPlay();
                            }
                        });
                for (int i = 0; i < listCam.get(position1).size(); i++) {
                    mBuilder.addItem(listCam.get(position1).get(i)[0]);
                }
                mBuilder.setCheckedIndex(position2);
                mBuilder.build().show();
            } else Toasts.showInfo(context, getString(R.string.video_str14));
        } else {
            Toasts.showInfo(context, getString(R.string.video_str13));
        }
    }

    private void initTv_camera() {
        if (listCam != null && listCam.size() > position1 && listCam.get(position1).size() > 0) {
            position2 = 0;
            tv_camera.setText(listCam.get(position1).get(position2)[0]);
            mDialog.show();
            zListenter.setOntouch(false);
            tv_kz.setTextColor(getResources().getColor(R.color.tvRealEnvir));
            startRealPlay();
        }
    }

    /**
     * 开始播放
     */
    private void startRealPlay() {
        if (player != null) player.stopRealPlay();
        try {
            player = EZOpenSDK.getInstance().createPlayer(listDev.get(position1)[1], Integer.parseInt(listCam.get(position1).get(position2)[1]));
            player.setHandler(handler);
            player.setSurfaceHold(mHolder);
            //延迟播放：防止stopRealPlay线程还没释放surface, startRealPlay线程已经开始使用surface
            tv_dev.postDelayed(new Runnable() {
                @Override
                public void run() {
                    player.startRealPlay();
                }
            }, 600);
        } catch (Exception e) {
            mDialog.dismiss();
            Toasts.showInfo(context, getString(R.string.video_str16));
        }
    }

    /**
     * 控制摄像头移动和缩放
     *
     * @param action
     */
    private void controlPTZ(final EZPTZAction action) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EZOpenSDK.getInstance().controlPTZ(listDev.get(position1)[1],
                            Integer.parseInt(listCam.get(position1).get(position2)[1]),
                            mCommand, action, 1);
                } catch (BaseException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 开启对讲
     */
    private void startTalk() {
        if (player != null && isPlay) {
            player.closeSound();
            player.startVoiceTalk();
            player.setVoiceTalkStatus(true);
            isTalk = true;
            tv_dj.setTextColor(getResources().getColor(R.color.video3));
            Toasts.showInfo(context, getString(R.string.video_str24));
        } else Toasts.showInfo(context, getString(R.string.video_str20));
    }

    /**
     * 关闭对讲
     */
    private void stoptTalk() {
        if (player != null && isPlay) {
            player.stopVoiceTalk();
            player.openSound();
            player.setVoiceTalkStatus(false);
            isTalk = false;
            tv_dj.setTextColor(getResources().getColor(R.color.tvRealEnvir));
            Toasts.showInfo(context, getString(R.string.video_str25));
        } else Toasts.showInfo(context, getString(R.string.video_str20));
    }

    private void stopCommand() {
        ad_top.stop();
        ad_bottom.stop();
        ad_left.stop();
        ad_right.stop();

        im_top.setVisibility(View.GONE);
        im_bottom.setVisibility(View.GONE);
        im_left.setVisibility(View.GONE);
        im_right.setVisibility(View.GONE);
    }

    private void setColor() {
        tv_hd.setTextColor(getResources().getColor(R.color.mWhite));
        tv_hd1.setTextColor(getResources().getColor(R.color.mWhite));
        tv_balanced.setTextColor(getResources().getColor(R.color.mWhite));
        tv_balanced1.setTextColor(getResources().getColor(R.color.mWhite));
//        tv_flunet.setTextColor(getResources().getColor(R.color.mWhite));
//        tv_flunet1.setTextColor(getResources().getColor(R.color.mWhite));
    }

    /**
     * 判断是否能拍照和权限问题
     */
    private void isSaveBitmap() {
        if (player != null && isPlay) {
            if (PermissionUtils.isGranted(PermissionConstants.getPermissions(STORAGE))) {
                saveBitmap();
            } else {
                requestPermission();
            }
        } else Toasts.showInfo(context, getString(R.string.video_str20));
    }

    /**
     * 请求SD卡读写权限
     */
    private void requestPermission() {
        PermissionUtils.permission(PermissionConstants.getPermissions(STORAGE))
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
//                        saveBitmap();
                    }

                    @Override
                    public void onDenied() {
                        Toasts.showInfo(context, getString(R.string.video_str22));
                    }
                })
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(ShouldRequest shouldRequest) {
                        shouldRequest.again(true);
                    }
                })
                .request();
    }

    /**
     * 保存图片
     */
    private void saveBitmap() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) // 判断是否可以对SDcard进行操作
        {
            String sdCardDir = Environment.getExternalStorageDirectory() + "/huinongyun/YingShiYunImage/";
            File dirFile = new File(sdCardDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
          //  File file = new File(sdCardDir, listCam.get(position1).get(position2)[0] + System.currentTimeMillis() + ".jpg");
            String fileName=listCam.get(position1).get(position2)[0].replace("/","").replace("\\","")
                    + System.currentTimeMillis() + ".jpg";
            File file = new File(sdCardDir, fileName);
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                player.capturePicture().compress(Bitmap.CompressFormat.JPEG, 100, out);
                Toasts.showInfo(context, getString(R.string.video_str21) + sdCardDir);
            } catch (Exception e) {
                Toasts.showInfo(context, getString(R.string.video_str23));
            } finally {
                if (out != null) {
                    try {
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else Toasts.showInfo(context, getString(R.string.video_str22));
    }

    /**
     * 横竖屏切换调用
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && ScreenUtils.isLandscape()) {
            portrait();
            ScreenUtils.setPortrait(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        register();
    }

    private void register() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.stopRealPlay();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stopRealPlay();
            player.release();
        }
        if (mImmersionBar != null) mImmersionBar.destroy();
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        if (Data.GhType != null) {
            switch (Data.GhType) {
                case "1":
                    switch (position) {
                        case 1:
                            startActivity(EnvirDataActivity.class);
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
                            startActivity(EnvirDataActivity.class);
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
                            startActivity(EnvirDataActivity.class);
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
                            startActivity(EnvirDataActivity.class);
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
                            startActivity(EnvirDataActivity.class);
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
                            startActivity(EnvirDataActivity.class);
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
                            startActivity(EnvirDataActivity.class);
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
                            startActivity(EnvirDataActivity.class);
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
                            startActivity(EnvirDataActivity.class);
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

    public void initFloat() {
        if (player != null) player.stopRealPlay();
        if (floatPlayer != null) floatPlayer.stopRealPlay();
        if (FloatWindow.get() != null) {
            FloatWindow.destroy();
        } else {
            Toasts.showInfo(context, getString(R.string.solenoid_str9));
            floatView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.video_floatview, null, false);
            SurfaceView surfaceView = floatView.findViewById(R.id.float_mSurfaceView);
            floatHolder = surfaceView.getHolder();
            floatView.findViewById(R.id.iv_float_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    FloatWindow.get().hide();
                    if (floatPlayer != null) floatPlayer.stopRealPlay();
                    FloatWindow.destroy();
                }
            });

            FloatWindow
                    .with(getApplicationContext())
                    .setView(floatView)
                    .setWidth(Screen.width, 0.9f) //设置悬浮控件宽高
                    .setHeight(Screen.width, 0.6f)
                    .setX(Screen.width, 0.05f)
                    .setY(Screen.height, 0.2f)
                    .setMoveType(MoveType.active, 100, -100)
//                    .setMoveStyle(500, new BounceInterpolator())
//                    .setFilter(true, A_Activity.class, C_Activity.class)
//                    .setViewStateListener(mViewStateListener)
//                    .setPermissionListener(mPermissionListener)
                    .setDesktopShow(true)
                    .build();
            floatPlayer();
            FloatWindow.get().show();
        }
    }

    private void floatPlayer() {
        if (videoLevels != -998) {
            //设置清晰度
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        EZOpenSDK.getInstance().setVideoLevel(listDev.get(position1)[1],
                                Integer.parseInt(listCam.get(position1).get(position2)[1]),
                                videoLevels);
                    } catch (BaseException e) {

                    }
                }
            }).start();
        }

        if (floatPlayer != null) floatPlayer.stopRealPlay();
        floatPlayer = EZOpenSDK.getInstance().createPlayer(listDev.get(position1)[1],
                Integer.parseInt(listCam.get(position1).get(position2)[1]));
        floatPlayer.setSurfaceHold(floatHolder);
        //延迟播放：防止stopRealPlay线程还没释放surface, startRealPlay线程已经开始使用surface
        floatView.postDelayed(new Runnable() {
            @Override
            public void run() {
                floatPlayer.startRealPlay();
            }
        }, 600);
    }

    protected void startActivity(Class<?> targetClass) {
        Intent intent = new Intent(context, targetClass);
        finish();
        startActivity(intent);
    }

    private void switchVideo() {
        if (Data.videoResponse != null) {
            switch (Data.videoResponse.getData().get(0).getPalyType()) {
                case 1:
//                    startActivity(YingVideoActivity.class, str);
                    break;
                case 2:
//                    startActivity(VideoActivity.class, str);
                    break;
                case 3:
                    new AlertDialog.Builder(context)
                            .setItems(new String[]{getString(R.string.video_str27),
                                    getString(R.string.video_str28)}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
//                                            startActivity(YingVideoActivity.class, str);
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
