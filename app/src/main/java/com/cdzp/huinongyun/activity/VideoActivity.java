package com.cdzp.huinongyun.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.cdzp.huinongyun.BaseActivity;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.PlaySurfaceView;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.VoiceTalk1;
import com.cdzp.huinongyun.util.ZoomListenter;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_JPEGPARA;
import com.hikvision.netsdk.NET_DVR_PICCFG_V30;
import com.hikvision.netsdk.NET_DVR_RESOLVE_DEVICEINFO;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.blankj.utilcode.constant.PermissionConstants.STORAGE;

/**
 * 海康HIDDNS
 */
public class VideoActivity extends BaseActivity implements OnMenuItemClickListener {

    private Context context;
    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private QMUITipDialog dialog;
    private String VideoSelResult;
    private ImageButton btn_5, btn_spk, ib_kongzhi;
    private Spinner sp_5;

    private final String TAG = "VideoActivity";

    private PlaySurfaceView[] videoView = null;

    private int m_iLogID = -1; // return by NET_DVR_Login_v30
    private NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = null;
    private int m_iChanNum = 0; // channel number
    private int m_iStartChan = 0;
    private ZoomListenter zListenter = new ZoomListenter();

    private int idnum = -1;
    private SurfaceView videoView2;
    private int mobile = 0;
    private ImageButton video_ib1, video_ib2, video_ib3, video_ib4;
    private int tag = 1;
    private int Spacing;

    private ImageView im_1, im_2, im_3, im_4;
    private AnimationDrawable ad1, ad2, ad3, ad4;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_video);
    }

    @Override
    public void dealLogicBeforeInitView() {
        context = this;
    }

    @Override
    public void initView() {
        TextView tv = findViewById(R.id.tv_title);
        tv.setText(getString(R.string.video_str3));
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

        if (Data.GhType!=null){

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

        dialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();

        final LinearLayout ll_tab = findViewById(R.id.ll_tab);

        ViewTreeObserver vto = ll_tab.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                ll_tab.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                Spacing += ll_tab.getHeight();
            }
        });

        final View incl = findViewById(R.id.incl);
        ViewTreeObserver vtoincl = incl.getViewTreeObserver();
        vtoincl.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                incl.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                Spacing += incl.getHeight();
            }
        });

        videoView2 = findViewById(R.id.videoView2);

        FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) videoView2.getLayoutParams();
        WindowManager wm = getWindowManager();
        int w = wm.getDefaultDisplay().getWidth();
        linearParams.height = w * 4 / 5;
        videoView2.setLayoutParams(linearParams);

        videoView2.setOnTouchListener(zListenter);
        zListenter.setListener(new ZoomListenter.Listener() {
            @Override
            public void callback(int num) {
                switch (num) {
                    case 1:
                        if (m_iLogID >= 0) {
                            HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, idnum, 21, 0);
                            mobile = 21;
                            im_1.setVisibility(View.VISIBLE);
                            ad1.start();
                        }
                        break;
                    case 2:
                        if (m_iLogID >= 0) {
                            HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, idnum, 22, 0);
                            mobile = 22;
                            im_2.setVisibility(View.VISIBLE);
                            ad2.start();
                        }
                        break;
                    case 3:
                        if (m_iLogID >= 0) {
                            HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, idnum, 23, 0);
                            mobile = 23;
                            im_3.setVisibility(View.VISIBLE);
                            ad3.start();
                        }
                        break;
                    case 4:
                        if (m_iLogID >= 0) {
                            HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, idnum, 24, 0);
                            mobile = 24;
                            im_4.setVisibility(View.VISIBLE);
                            ad4.start();
                        }
                        break;
                    case 5:
                        if (m_iLogID >= 0) {
                            HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, idnum, 11, 0);
                            mobile = 11;
                        }
                        break;
                    case 6:
                        if (m_iLogID >= 0) {
                            HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, idnum, 12, 0);
                            mobile = 12;
                        }
                        break;
                    case 7:
                        if (m_iLogID >= 0 && mobile != 0) {
                            HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, idnum, mobile, 1);
                            setim();
                            stopad();
                        }
                        break;

                    default:
                        break;
                }
            }
        });
        btn_5 = findViewById(R.id.btn_5);

        ib_kongzhi = findViewById(R.id.ib_kongzhi);
        ib_kongzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tag == 1) {
                    setkongzhi();
                } else {
                    Toasts.showInfo(context, "请选择一个视频");
                }
            }
        });

        btn_spk = findViewById(R.id.btn_spk);
        btn_spk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showspk();
            }
        });

        im_1 = findViewById(R.id.im_1);
        im_1.setBackgroundResource(R.drawable.frame_1);
        im_2 = findViewById(R.id.im_2);
        im_2.setBackgroundResource(R.drawable.frame_2);
        im_3 = findViewById(R.id.im_3);
        im_3.setBackgroundResource(R.drawable.frame_3);
        im_4 = findViewById(R.id.im_4);
        im_4.setBackgroundResource(R.drawable.frame_4);

        ad1 = (AnimationDrawable) im_1.getBackground();
        ad2 = (AnimationDrawable) im_2.getBackground();
        ad3 = (AnimationDrawable) im_3.getBackground();
        ad4 = (AnimationDrawable) im_4.getBackground();

        video_ib1 = findViewById(R.id.video_ib1);
        video_ib2 = findViewById(R.id.video_ib2);
        video_ib3 = findViewById(R.id.video_ib3);
        video_ib4 = findViewById(R.id.video_ib4);

        videoView = new PlaySurfaceView[tag];

        findViewById(R.id.ll_back).setOnClickListener(this);
        rl.setOnClickListener(this);
        video_ib1.setOnClickListener(this);
        video_ib2.setOnClickListener(this);
        video_ib3.setOnClickListener(this);
        video_ib4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
    }

    @Override
    public void dealLogicAfterInitView() {
//        initModeName();
        dialog.show();
        handler.sendEmptyMessageDelayed(3, 1000);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_5:
                if (PermissionUtils.isGranted(PermissionConstants.getPermissions(STORAGE))) {
                    captureScreen();
                } else {
                    requestPermission();
                }
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.video_ib1:
                setib();
                video_ib1.setBackgroundResource(R.drawable.h_1);
                setkongzhi1();
                stopMultiPreviews();
                tag = 1;
                videoView = new PlaySurfaceView[tag];
                if (m_iChanNum > 0) {
                    startMultiPreview(idnum);
                } else {
                    Toasts.showInfo(context, "无播放视频");
                }
                break;
            case R.id.video_ib2:
                setib();
                video_ib2.setBackgroundResource(R.drawable.h_4);
                setkongzhi1();
                stopMultiPreviews();
                tag = 4;
                videoView = new PlaySurfaceView[tag];
                if (m_iChanNum > 0) {
                    startMultiPreview(idnum);
                } else {
                    Toasts.showInfo(context, "无播放视频");
                }
                break;
            case R.id.video_ib3:
                setib();
                video_ib3.setBackgroundResource(R.drawable.h_9);
                setkongzhi1();
                stopMultiPreviews();
                tag = 9;
                videoView = new PlaySurfaceView[tag];
                if (m_iChanNum > 0) {
                    startMultiPreview(idnum);
                } else {
                    Toasts.showInfo(context, "无播放视频");
                }
                break;
            case R.id.video_ib4:
                setib();
                video_ib4.setBackgroundResource(R.drawable.h_16);
                setkongzhi1();
                stopMultiPreviews();
                tag = 16;
                videoView = new PlaySurfaceView[tag];
                if (m_iChanNum > 0) {
                    startMultiPreview(idnum);
                } else {
                    Toasts.showInfo(context, "无播放视频");
                }
                break;
            case R.id.rl_right:
                mMenuDialogFragment.show(fragmentManager, "ContextMenuDialogFragment");
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                    gsonvideo();
                    break;
                case 2:

                    break;
                case 5:
                    dialog.dismiss();
                    Toasts.showInfo(context, "获取视频失败");
                    break;
                case 3:
                    initvideo();
                    break;
                default:

                    break;
            }
        }
    };

    private void initvideo() {
        if (Data.videoResponse != null) {
            initeSdk();
            switch (Data.videoResponse.getData().get(0).getHikModel().getDefaultMode()) {
                case 1:
                    init(1);
                    break;
                case 2:
                    init(2);
                    break;
                default:

                    break;
            }
        } else Toasts.showInfo(context, getString(R.string.request_error9));
    }

    private String[] namess;

    private void initsp() {
        String[] names = new String[m_iChanNum];
        int num = 0;
        for (int i = 0; i < m_iChanNum; i++) {
            NET_DVR_PICCFG_V30 ndpv = new NET_DVR_PICCFG_V30();
            boolean b = HCNetSDK.getInstance().NET_DVR_GetDVRConfig(m_iLogID, 1002, m_iStartChan + i, ndpv);
            if (b) {
                String string;
                try {
                    string = new String(ndpv.sChanName, "GB2312");
                    String name = string.replaceAll("\0", "");
                    names[i] = name;
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                int nu = HCNetSDK.getInstance().NET_DVR_GetLastError();
                num = i;
                break;
            }
        }
        if (names.length == 0) {
            Toasts.showInfo(context, "未获取到视频");
            return;
        }

        List<String> list = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            if (names[i] != null)
                list.add(names[i]);
        }
        if (list.size() == 0) {
            Toasts.showInfo(context, "未获取到视频");
            return;
        }
        namess = list.toArray(new String[list.size()]);
        ArrayAdapter<String> adapters = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,
                namess);
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_5 = findViewById(R.id.sp_5);
        sp_5.setAdapter(adapters);
        sp_5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                stopMultiPreviews();
                videoView = new PlaySurfaceView[tag];
                idnum = arg2 + m_iStartChan;
                if (m_iChanNum > 0) {
                    startMultiPreview(idnum);
                } else {
                    Toasts.showInfo(context, "无播放视频");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        dialog.dismiss();
    }

    protected void stopad() {
        ad1.stop();
        ad2.stop();
        ad3.stop();
        ad4.stop();
    }

    protected void setim() {
        im_1.setVisibility(View.GONE);
        im_2.setVisibility(View.GONE);
        im_3.setVisibility(View.GONE);
        im_4.setVisibility(View.GONE);
    }

    protected void setkongzhi() {
        if (zListenter.isOntouch()) {
            zListenter.setOntouch(false);
            ib_kongzhi.setBackgroundResource(R.drawable.kongzhi_1);
        } else {
            zListenter.setOntouch(true);
            ib_kongzhi.setBackgroundResource(R.drawable.kongzhi_2);
        }
    }

    private boolean m_bTalkOn = false;

    protected void showspk() {
        try {
            if (m_bTalkOn == false) {
                if (VoiceTalk1.startVoiceTalk(m_iLogID) >= 0) {
                    m_bTalkOn = true;
                    btn_spk.setBackgroundResource(R.drawable.duijiang_2);
                    // btn_spk.setText("Stop");
                }
            } else {
                if (VoiceTalk1.stopVoiceTalk()) {
                    m_bTalkOn = false;
                    btn_spk.setBackgroundResource(R.drawable.duijiang_1);
                    // btn_spk.setText("Talk");
                }
            }
        } catch (Exception e) {

        }
    }

    private int nums;

    private void startMultiPreview(int num) {
        nums = num;
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);

        for (int i = 0; i < tag; i++) {
            if (videoView[i] == null) {
                videoView[i] = new PlaySurfaceView(context);
                // playView[i].setParam(metric.widthPixels);

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                switch (tag) {
                    case 1:
                        videoView[i].setHWnum(1, 1);
                        videoView[i].setParam(videoView2.getWidth(), videoView2.getHeight());
                        params.topMargin = Spacing;
                        params.leftMargin = 0;
                        break;
                    case 4:
                        videoView[i].setHWnum(2, 2);
                        videoView[i].setParam(videoView2.getWidth(), videoView2.getHeight());
                        params.topMargin = (i / 2) * videoView[i].getCurHeight() + Spacing;
                        params.leftMargin = (i % 2) * videoView[i].getCurWidth();
                        break;
                    case 9:
                        videoView[i].setHWnum(3, 3);
                        videoView[i].setParam(videoView2.getWidth(), videoView2.getHeight());
                        params.topMargin = (i / 3) * videoView[i].getCurHeight() + Spacing;
                        params.leftMargin = (i % 3) * videoView[i].getCurWidth();
                        break;
                    case 16:
                        videoView[i].setHWnum(4, 4);
                        videoView[i].setParam(videoView2.getWidth(), videoView2.getHeight());
                        params.topMargin = (i / 4) * videoView[i].getCurHeight() + Spacing;
                        params.leftMargin = (i % 4) * videoView[i].getCurWidth();
                        break;
                }

                params.gravity = Gravity.TOP | Gravity.LEFT;
                addContentView(videoView[i], params);
            }
            if (videoView[i].startPreview(m_iLogID, nums) < 0) {
                if (tag == 1) {
                    Toasts.showInfo(context, "预览失败");
                    return;
                }
                if (tag > 1 && (nums - m_iStartChan) < namess.length) {
                    nums++;
                    startPreviews(videoView[i], m_iLogID, nums);
                }
            }
            if (tag > 1) {
                nums++;
            }
            if (nums - m_iStartChan >= namess.length) {
                break;
            }
        }

    }

    private void startPreviews(PlaySurfaceView playSurfaceView, int m_iLogID2, int num) {
        if (playSurfaceView.startPreview(m_iLogID2, num) < 0) {
            if ((nums - m_iStartChan) < namess.length) {
                nums++;
                startPreviews(playSurfaceView, m_iLogID, nums);
            }
        }
    }

    private boolean first = true;
    private boolean sorf = false;

    private void init(int i) {
        try {
            if (m_iLogID < 0) {
                m_iLogID = loginDevice(i);
                if (m_iLogID < 0) {
                    dialog.dismiss();
                    Toasts.showInfo(context, "获取视频失败");
                    Log.e(TAG, "This device logins failed!");
                } else {
                    Log.d("aa", "m_iChanNum:" + m_iChanNum);
                    initsp();
                }
                Log.i(TAG, "Login sucess ****************************1***************************");
            } else {
                // whether we have logout
                if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(m_iLogID)) {
                    Log.e(TAG, " NET_DVR_Logout is failed!");
                }
                m_iLogID = -1;
            }
        } catch (Exception err) {
            Log.e(TAG, "error: " + err.toString());
        }
    }

    private int loginDevice(int i) {
        // get instance
        m_oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
        if (null == m_oNetDvrDeviceInfoV30) {
            Log.e(TAG, "HKNetDvrDeviceInfoV30 new is failed!");
            return -1;
        }
        int iLogID = -1;
        if (i == 2) {
            short port;
            String IPstr = "";
            NET_DVR_RESOLVE_DEVICEINFO info = new NET_DVR_RESOLVE_DEVICEINFO();
            boolean b = HCNetSDK.getInstance().NET_DVR_GetDVRIPByResolveSvr_EX("183.136.184.61", (short) 80,
                    Data.videoResponse.getData().get(0).getHikModel().getTagmark(),
                    (short) Data.videoResponse.getData().get(0).getHikModel().getTagmark().length(),
                    null, (short) 0, info);
            port = (short) info.dwPort;

            try {
                IPstr = new String(info.sGetIP, "utf-8");
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            String ip1 = IPstr.replaceAll("\0", "");
            int len = IPstr.length();
            iLogID = HCNetSDK.getInstance().NET_DVR_Login_V30(ip1, port,
                    Data.videoResponse.getData().get(0).getHikModel().getUser(),
                    Data.videoResponse.getData().get(0).getHikModel().getPassword(), m_oNetDvrDeviceInfoV30);
        } else if (i == 1) {
            iLogID = HCNetSDK.getInstance().NET_DVR_Login_V30(
                    Data.videoResponse.getData().get(0).getHikModel().getServerIP2(),
                    Integer.parseInt(Data.videoResponse.getData().get(0).getHikModel().getTagmark2()),
                    Data.videoResponse.getData().get(0).getHikModel().getUser(),
                    Data.videoResponse.getData().get(0).getHikModel().getPassword(), m_oNetDvrDeviceInfoV30);
        }
        Log.d("aa", "iLogID:" + iLogID);
        if (iLogID < 0) {
            Log.e(TAG, "NET_DVR_Login is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return -1;
        }
        if (m_oNetDvrDeviceInfoV30.byChanNum > 0) {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartDChan;
            m_iChanNum = m_oNetDvrDeviceInfoV30.byChanNum;
        } else if (m_oNetDvrDeviceInfoV30.byIPChanNum > 0) {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartDChan;
            m_iChanNum = m_oNetDvrDeviceInfoV30.byIPChanNum + m_oNetDvrDeviceInfoV30.byHighDChanNum * 256;
        }
        Log.i(TAG, "NET_DVR_Login is Successful!");

        return iLogID;
    }

    private void initeSdk() {
        // init net sdk
        try {
            if (!HCNetSDK.getInstance().NET_DVR_Init()) {
                Log.e(TAG, "HCNetSDK init is failed!");
            }
            HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, "/mnt/sdcard/sdklog/", true);
        } catch (Exception e) {
            Toasts.showInfo(context, "初始化出错");
        }
    }

    private void requestPermission() {
        PermissionUtils.permission(PermissionConstants.getPermissions(STORAGE))
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
//                        captureScreen();
                    }

                    @Override
                    public void onDenied() {

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

    private void captureScreen() {
        NET_DVR_JPEGPARA jpeg = new NET_DVR_JPEGPARA();
        // 设置图片的分辨率
        jpeg.wPicSize = 0xff;// 自适应
        // 设置图片质量
        jpeg.wPicQuality = 0;
        /** 1.userId 返回值 2.通道号 3.图像参数 4.路劲 */

        // 创建文件目录
        String string = getSDRootPath() + "/huinongyun/DiddnsImage/";
        File file = new File(string);
        if (!file.exists())
            file.mkdirs();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String curTime = sdf.format(new Date());
        if (idnum == -1) {
            Toasts.showInfo(context, "无法截图");
            return;
        }
        boolean is = HCNetSDK.getInstance().NET_DVR_CaptureJPEGPicture(m_iLogID, idnum, jpeg,
                string + "/" + namess[idnum - m_iStartChan] + " " + curTime + ".jpg");
//        System.out.println("videoView   " + is + "  " + HCNetSDK.getInstance().NET_DVR_GetLastError());

        if (is) Toasts.showInfo(context, "截取成功，保存在SDK的" + string + "下");
    }

    public String getSDRootPath() {
        File sdDir = null;
        // 判断
        boolean sdCardExist = hasSdcard();
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
            return sdDir.toString();
        } else {
            return null;
        }
    }

    public boolean hasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private void setib() {
        video_ib1.setBackgroundResource(R.drawable.b_1);
        video_ib2.setBackgroundResource(R.drawable.b_4);
        video_ib3.setBackgroundResource(R.drawable.b_9);
        video_ib4.setBackgroundResource(R.drawable.b_16);
    }

    private void setkongzhi1() {
        zListenter.setOntouch(false);
        ib_kongzhi.setBackgroundResource(R.drawable.kongzhi_1);
    }

    private void stopMultiPreviews() {
        if (videoView == null) {
            return;
        }
        for (int i = 0; i < tag; i++) {
            if (videoView[i] != null) {
                videoView[i].stopPreview();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMultiPreviews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMultiPreviews();
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
                                            startActivity(YingVideoActivity.class);
                                            break;
                                        case 1:
//                                            startActivity(VideoActivity.class, str);
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
}
