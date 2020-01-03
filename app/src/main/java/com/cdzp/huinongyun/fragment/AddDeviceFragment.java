package com.cdzp.huinongyun.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.activity.CircleActivity;
import com.cdzp.huinongyun.activity.HomAddDevActivity;
import com.cdzp.huinongyun.activity.OnlineFert;
import com.cdzp.huinongyun.activity.PersonalActivity;
import com.cdzp.huinongyun.adapter.HomeDevAdapter;
import com.cdzp.huinongyun.bean.GHTSResult;
import com.cdzp.huinongyun.bean.WeatherResult;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Operate;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.blankj.utilcode.constant.PermissionConstants.STORAGE;

/**
 * 设备控制（首页）
 */
public class AddDeviceFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean isVisible,/*界面对用户是否可见，可见为true，不可见为false*/
            isInit = false;
    /**
     * 操作提示框库
     */
    private QMUITipDialog mDialog;
    /**
     * 刷新加载库
     */
    private SwipeRefreshLayout mRefreshLayout;
    /**
     * RecyclerView库
     */
    private SwipeMenuRecyclerView mRecyclerView;
    /**
     * 添加设备适配器
     */
    private HomeDevAdapter mAdapter;
    private RelativeLayout rl;
    /**
     * 获取天气数据的handler
     */
    private Handler handler;
    /**
     * 天气数据返回
     */
    private WeatherResult result;
    private LinearLayout ll;
    private TextView tv_city, tv_time, tv_weather, tv_value, tv_time1, tv_time2, tv_time3, tv_time4,
            tv_value1, tv_value2, tv_value3, tv_value4, tv_wind1, tv_wind2, tv_wind3, tv_wind4;
    private ImageView iv_weather, iv_weather1, iv_weather2, iv_weather3, iv_weather4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_add_device, container, false);
            initView();
        }
        return view;
    }

    /**
     * ViewPager同时加载多个fragment，以实现多tab页面快速切换, 但是fragment初始化时若加载的内容较多，就可能导致整个应用启动速度缓慢，
     * 影响用户体验。 为了提高用户体验，我们会使用一些懒加载方案，实现加载延迟。这时我们会用到getUserVisibleHint()
     * 与setUserVisibleHint()这两个方法。
     * 原文：https://blog.csdn.net/czhpxl007/article/details/51277319
     *
     * @param isVisibleToUser isVisibleToUser true if this fragment's UI is currently visible to the user (default),false if it is not.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible) {//可见
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
            if (!isInit && view != null) {
                isInit = true;
                mDialog.show();
                initData();//加载数据
            }
        } else {//不可见
            unRegister();
            if (mRefreshLayout != null) {
                mRefreshLayout.setRefreshing(false);
            }
        }
    }

    /**
     * 初始化数据
     */
    private void initView() {
        context = getContext();
        //Add the top margin size equals status bar's height for view.
        BarUtils.addMarginTopEqualStatusBarHeight(view.findViewById(R.id.ll_home1));
        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();

        ll = view.findViewById(R.id.ll_fad);
        tv_city = view.findViewById(R.id.tv_fad_city);
        tv_time = view.findViewById(R.id.tv_fad_time);
        tv_weather = view.findViewById(R.id.tv_fad_weather);
        tv_value = view.findViewById(R.id.tv_fad_value);
        tv_time1 = view.findViewById(R.id.tv_fad_time1);
        tv_time2 = view.findViewById(R.id.tv_fad_time2);
        tv_time3 = view.findViewById(R.id.tv_fad_time3);
        tv_time4 = view.findViewById(R.id.tv_fad_time4);
        tv_value1 = view.findViewById(R.id.tv_fad_value1);
        tv_value2 = view.findViewById(R.id.tv_fad_value2);
        tv_value3 = view.findViewById(R.id.tv_fad_value3);
        tv_value4 = view.findViewById(R.id.tv_fad_value4);
        tv_wind1 = view.findViewById(R.id.tv_fad_wind1);
        tv_wind2 = view.findViewById(R.id.tv_fad_wind2);
        tv_wind3 = view.findViewById(R.id.tv_fad_wind3);
        tv_wind4 = view.findViewById(R.id.tv_fad_wind4);
        iv_weather = view.findViewById(R.id.iv_fad_weather);
        iv_weather1 = view.findViewById(R.id.iv_fad_weather1);
        iv_weather2 = view.findViewById(R.id.iv_fad_weather2);
        iv_weather3 = view.findViewById(R.id.iv_fad_weather3);
        iv_weather4 = view.findViewById(R.id.iv_fad_weather4);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        updateWeather();//更新天气
                        break;
                    default:

                        break;
                }
            }
        };

        mRefreshLayout = view.findViewById(R.id.fad_RefreshLayout);
        mRecyclerView = view.findViewById(R.id.fad_mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(context.getResources().getColor(R.color.decoration),
                1, 1));
        mAdapter = new HomeDevAdapter(context);
        mRefreshLayout.setColorSchemeResources(R.color.title);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {//刷新
                initData();
            }
        });
        mRecyclerView.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                switchItemClick(position);//item点击事件
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        rl = view.findViewById(R.id.fad_rl);

        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                WebServiceManage.dispose();
                stopLoading();//关闭loading对话框
            }
        });

        view.findViewById(R.id.iv_fad_my).setOnClickListener(this);
        view.findViewById(R.id.fad_add_dev).setOnClickListener(this);
        view.findViewById(R.id.iv_fad_location).setOnClickListener(this);

        getWeather();

        if (!isInit && isVisible) {
            isInit = true;
            mDialog.show();
            initData();
        }
    }

    /**
     * item点击事件
     *
     * @param position
     */
    private void switchItemClick(int position) {
        Log.e("-->list", new Gson().toJson(mAdapter.getList().get(position)[0]));
//        Toasts.showSuccess(context," "+new Gson().toJson(mAdapter.getList().get(position)[0]));
        switch (mAdapter.getList().get(position)[0]) {
            case "1"://温室环控
            case "2"://大田水肥
            case "3"://水产养殖
            case "4"://畜牧养殖
            case "5"://智能网关
            case "6"://气象站
            case "7"://四情监测
            case "8"://日光温室
            case "9"://电磁阀主机
            case "10"://在线施肥机
//            case "11"://智能消毒机
            case "12"://移动喷灌机
                Data.GhType = mAdapter.getList().get(position)[0];
                Log.e("-->list", "====" + Data.GhType);
                Log.e("-->list", "==22==" + mAdapter.getList().get(position)[1]);
                Intent intent = new Intent(context, CircleActivity.class);
                intent.putExtra("data", new String[]{
                        mAdapter.getList().get(position)[0],
                        mAdapter.getList().get(position)[1]});
                unRegister();//取消注册
                stopLoading();//关闭loading对话框
                startActivity(intent);
                break;
//            case "10":
//                Intent intent1 = new Intent(context, OnlineFert.class);
//                unRegister();
//                stopLoading();
//                startActivity(intent1);
//                break;
            case "10001":
                unRegister();
                stopLoading();
                startActivity(HomAddDevActivity.class);
                break;
            default:

                break;
        }
    }

    /**
     * 加载数据
     */
    private void initData() {
        if (Data.userResult != null) {//封装数据请求
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("GreenHouseTypeSel",
                    "/StringService/DeviceSet.asmx", data, 1, context);
        } else {
            stopLoading();
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 1:
                    parseGHTResult(message);//获取主页终端模块
                    break;
                default:
                    stopLoading();
                    break;
            }
        } else {
            stopLoading();
            Toasts.showError(context, message.getmResult());
        }
    }

    /**
     * 关闭loading对话框
     */
    private void stopLoading() {
        mDialog.dismiss();
        mRefreshLayout.setRefreshing(false);
    }

    /**
     * 获取主页终端模块
     *
     * @param message
     */
    private void parseGHTResult(WebMessage message) {

        GHTSResult result = new Gson().fromJson(message.getmResult(), GHTSResult.class);
        if (result.isSuccess()) {//成功
            Log.e("", "数量: "+result.getData().get(0).size() );
            if (result.getData() != null && result.getData().size() > 0 && result.getData().get(0).size() > 0) {
                mAdapter.getList().clear();
                for (int i = 0; i < result.getData().get(0).size(); i++) {
                    int Alarm_cout = Integer.parseInt(result.getData().get(0).get(i).getAlarm_cout());
                    String strAlarm_cout;
                    if (Alarm_cout > 99) {
                        strAlarm_cout = "  99+  ";
                    } else strAlarm_cout = "  " + Alarm_cout + "  ";
                    String typeName;
                    switch (result.getData().get(0).get(i).getGhType()) {//判断类型
                        case "1":
                            typeName = getString(R.string.terminal_str1);
                            break;
                        case "2":
                            typeName = getString(R.string.terminal_str2);
                            break;
                        case "3":
                            typeName = getString(R.string.terminal_str3);
                            break;
                        case "4":
                            typeName = getString(R.string.terminal_str4);
                            break;
                        case "5":
                            typeName = getString(R.string.terminal_str5);
                            break;
                        case "6":
                            typeName = getString(R.string.terminal_str6);
                            break;
                        case "7":
                            typeName = getString(R.string.terminal_str7);
                            break;
                        case "8":
                            typeName = getString(R.string.terminal_str8);
                            break;
                        case "9":
                            typeName = getString(R.string.terminal_str9);
                            break;
                        case "10":
                            typeName = getString(R.string.terminal_str10);
                            break;
                        case "11":
                            typeName = getString(R.string.terminal_str101);
                            break;
                        case "12":
                            typeName = getString(R.string.terminal_str102);
                            break;
                        default:
                            typeName = result.getData().get(0).get(i).getTypeName();
                            break;
                    }
                    mAdapter.getList().add(new String[]{
                            result.getData().get(0).get(i).getGhType(),
                            typeName,
                            result.getData().get(0).get(i).getGh_cout(),
                            result.getData().get(0).get(i).getNotline(),
                            strAlarm_cout
                    });
                }
                mAdapter.getList().add(new String[]{
                        "10001", getString(R.string.hone_dev_item_footer), "", "", ""
                });
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setVisibility(View.VISIBLE);
                rl.setVisibility(View.GONE);
                if (mRefreshLayout.isRefreshing()) {//刷新成功
                    Toasts.showSuccess(context, getString(R.string.loading_success));
                }
                stopLoading();
            } else {//没有数据
                mRecyclerView.setVisibility(View.GONE);
                rl.setVisibility(View.VISIBLE);
                stopLoading();
                Toasts.showInfo(context, getString(R.string.request_error8));
            }
        } else {//失败
            stopLoading();
            Toasts.showInfo(context, result.getMsg());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_fad_my:
                startActivity(PersonalActivity.class);
                break;
            case R.id.fad_add_dev:
                startActivity(HomAddDevActivity.class);
                break;
            case R.id.iv_fad_location:
//                Toasts.showInfo(context, getString(R.string.request_error15));
//                getWeather();
                break;
            default:

                break;
        }
    }

    /**
     * 更新天气
     */
    private void updateWeather() {
        try {
            if (result.getError() == 0 && result.getStatus().equals("success")) {
                String weather = result.getResults().get(0).getWeather_data().get(0).getWeather();
                String weatherSp = weather.split("转")[0];
                if (weatherSp.contains("阴")) {
                    ll.setBackgroundResource(R.drawable.overcast_bg);
                    iv_weather.setBackgroundResource(R.drawable.overcast_min);
                    iv_weather1.setBackgroundResource(R.drawable.overcast_max);
                } else if (weatherSp.contains("云")) {
                    ll.setBackgroundResource(R.drawable.cloudy_bg);
                    iv_weather.setBackgroundResource(R.drawable.cloudy_min);
                    iv_weather1.setBackgroundResource(R.drawable.cloudy_max);
                } else if (weatherSp.contains("晴")) {
                    ll.setBackgroundResource(R.drawable.sunny_bg);
                    iv_weather.setBackgroundResource(R.drawable.sunny_min);
                    iv_weather1.setBackgroundResource(R.drawable.sunny_max);
                } else if (weatherSp.contains("雨")) {
                    ll.setBackgroundResource(R.drawable.rain_bg);
                    iv_weather.setBackgroundResource(R.drawable.rain_min);
                    iv_weather1.setBackgroundResource(R.drawable.rain_max);
                } else if (weatherSp.contains("雪")) {
                    ll.setBackgroundResource(R.drawable.snow_bg);
                    iv_weather.setBackgroundResource(R.drawable.snow_min);
                    iv_weather1.setBackgroundResource(R.drawable.snow_max);
                } else {
                    ll.setBackgroundResource(R.drawable.overcast_bg);
                    iv_weather.setBackgroundResource(R.drawable.overcast_min);
                    iv_weather1.setBackgroundResource(R.drawable.overcast_max);
                }

                tv_city.setText(result.getResults().get(0).getCurrentCity());
                tv_time.setText(result.getDate() + " " + result.getResults().get(0).getWeather_data().get(0).getDate().substring(0, 2));
                tv_weather.setText(result.getResults().get(0).getWeather_data().get(0).getWeather());
                tv_value.setText(result.getResults().get(0).getWeather_data().get(0).getDate().split("：")[1].replace("℃)", "°"));

                tv_time1.setText(result.getResults().get(0).getWeather_data().get(0).getDate().substring(0, 2));
                if (weather.length() > 5) {
                    if (weather.contains("转")) {
                        weather = weather.split("转")[0];
                    } else weather = weather.substring(0, 5);
                }
                tv_value1.setText(weather + " " +
                        result.getResults().get(0).getWeather_data().get(0).getTemperature().replace(" ", ""));
                tv_wind1.setText(result.getResults().get(0).getWeather_data().get(0).getWind());


                String weather2 = result.getResults().get(0).getWeather_data().get(1).getWeather();
                String weatherSp2 = weather2.split("转")[0];
                if (weatherSp2.contains("阴")) {
                    iv_weather2.setBackgroundResource(R.drawable.overcast_max);
                } else if (weatherSp2.contains("云")) {
                    iv_weather2.setBackgroundResource(R.drawable.cloudy_max);
                } else if (weatherSp2.contains("晴")) {
                    iv_weather2.setBackgroundResource(R.drawable.sunny_max);
                } else if (weatherSp2.contains("雨")) {
                    iv_weather2.setBackgroundResource(R.drawable.rain_max);
                } else if (weatherSp2.contains("雪")) {
                    iv_weather2.setBackgroundResource(R.drawable.snow_max);
                } else {
                    iv_weather2.setBackgroundResource(R.drawable.overcast_max);
                }
                tv_time2.setText(result.getResults().get(0).getWeather_data().get(1).getDate());
                if (weather2.length() > 5) {
                    if (weather2.contains("转")) {
                        weather2 = weather2.split("转")[0];
                    } else weather2 = weather2.substring(0, 5);
                }
                tv_value2.setText(weather2 + " " +
                        result.getResults().get(0).getWeather_data().get(1).getTemperature().replace(" ", ""));
                tv_wind2.setText(result.getResults().get(0).getWeather_data().get(1).getWind());


                String weather3 = result.getResults().get(0).getWeather_data().get(2).getWeather();
                String weatherSp3 = weather3.split("转")[0];
                if (weatherSp3.contains("阴")) {
                    iv_weather3.setBackgroundResource(R.drawable.overcast_max);
                } else if (weatherSp3.contains("云")) {
                    iv_weather3.setBackgroundResource(R.drawable.cloudy_max);
                } else if (weatherSp3.contains("晴")) {
                    iv_weather3.setBackgroundResource(R.drawable.sunny_max);
                } else if (weatherSp3.contains("雨")) {
                    iv_weather3.setBackgroundResource(R.drawable.rain_max);
                } else if (weatherSp3.contains("雪")) {
                    iv_weather3.setBackgroundResource(R.drawable.snow_max);
                } else {
                    iv_weather3.setBackgroundResource(R.drawable.overcast_max);
                }
                tv_time3.setText(result.getResults().get(0).getWeather_data().get(2).getDate());
                if (weather3.length() > 5) {
                    if (weather3.contains("转")) {
                        weather3 = weather3.split("转")[0];
                    } else weather3 = weather3.substring(0, 5);
                }
                tv_value3.setText(weather3 + " " +
                        result.getResults().get(0).getWeather_data().get(2).getTemperature().replace(" ", ""));
                tv_wind3.setText(result.getResults().get(0).getWeather_data().get(2).getWind());


                String weather4 = result.getResults().get(0).getWeather_data().get(3).getWeather();
                String weatherSp4 = weather4.split("转")[0];
                if (weatherSp4.contains("阴")) {
                    iv_weather4.setBackgroundResource(R.drawable.overcast_max);
                } else if (weatherSp4.contains("云")) {
                    iv_weather4.setBackgroundResource(R.drawable.cloudy_max);
                } else if (weatherSp4.contains("晴")) {
                    iv_weather4.setBackgroundResource(R.drawable.sunny_max);
                } else if (weatherSp4.contains("雨")) {
                    iv_weather4.setBackgroundResource(R.drawable.rain_max);
                } else if (weatherSp4.contains("雪")) {
                    iv_weather4.setBackgroundResource(R.drawable.snow_max);
                } else {
                    iv_weather4.setBackgroundResource(R.drawable.overcast_max);
                }
                tv_time4.setText(result.getResults().get(0).getWeather_data().get(3).getDate());
                if (weather4.length() > 5) {
                    if (weather4.contains("转")) {
                        weather4 = weather4.split("转")[0];
                    } else weather4 = weather4.substring(0, 5);
                }
                tv_value4.setText(weather4 + " " +
                        result.getResults().get(0).getWeather_data().get(3).getTemperature().replace(" ", ""));
                tv_wind4.setText(result.getResults().get(0).getWeather_data().get(3).getWind());

            } else {
                Toasts.showInfo(context, getString(R.string.request_error14));
            }
        } catch (Exception e) {

        }
        result = null;
    }

    /**
     * 获取天气
     */
    private void getWeather() {
        String city = "成都";
        if (Data.userResult != null && Data.userResult.getData().size() > 0 && Data.userResult.getData().get(0).getCity() != null) {
            city = Data.userResult.getData().get(0).getCity();
            try {
                city = URLEncoder.encode(city, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 创建okHttpClient对象
            OkHttpClient mOkHttpClient = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url("http://api.map.baidu.com/telematics/v3/weather?ak=04674f57bb78a6b0fa1aa467be9369c2&output=json&location="
                            + city)
                    .build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (PermissionUtils.isGranted(PermissionConstants.getPermissions(STORAGE))) {
                        readWeather();
                    } else {
                        requestPermission("", false);
                    }
//                    checkUpdate();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == 200) {
                        String info = response.body().string();
                        try {
                            result = new Gson().fromJson(info, WeatherResult.class);
                            handler.sendEmptyMessage(1);
                            if (result.getError() == 0 && result.getStatus().equals("success")) {
                                if (PermissionUtils.isGranted(PermissionConstants.getPermissions(STORAGE))) {
                                    saveWeather(info);
                                } else {
                                    requestPermission(info, true);
                                }
                            }
                        } catch (Exception e) {

                        }
                    } else {
                        if (PermissionUtils.isGranted(PermissionConstants.getPermissions(STORAGE))) {
                            readWeather();
                        } else {
                            requestPermission("", false);
                        }
                    }
//                    checkUpdate();
                }
            });
        } else {
            if (PermissionUtils.isGranted(PermissionConstants.getPermissions(STORAGE))) {
                readWeather();
            } else {
                requestPermission("", false);
            }
//            checkUpdate();
        }
    }


    /**
     * 从SD卡读取天气数据
     */
    private void readWeather() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory() + "/huinongyun/请勿操作(Do not operate).bb");
            if (file.exists()) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    byte[] buf = new byte[1024];
                    int len;
                    StringBuffer sb = new StringBuffer();
                    String s;
                    while ((len = fis.read(buf)) != -1) {
                        s = new String(buf, 0, len);
                        sb.append(s);
                    }
                    if (sb.length() > 0) {
                        result = new Gson().fromJson(sb.toString(), WeatherResult.class);
                        handler.sendEmptyMessage(1);
                    }
                } catch (Exception e) {

                } finally {
                    try {
                        if (fis != null) fis.close();
                    } catch (Exception e) {

                    }
                }
            }
        }
    }

    /**
     * 保持天气到SD卡
     *
     * @param info
     */
    private void saveWeather(String info) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String sdCardDir = Environment.getExternalStorageDirectory() + "/huinongyun/";
            File dirFile = new File(sdCardDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            File file = new File(dirFile, "请勿操作(Do not operate).bb");
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                out.write(info.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 请求SD卡读写权限
     *
     * @param info   写入的数据（天气数据）
     * @param isSave 写入还是读取数据
     */
    private void requestPermission(final String info, final boolean isSave) {
        PermissionUtils.permission(PermissionConstants.getPermissions(STORAGE))
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        if (isSave) {
                            saveWeather(info);
                        } else {
                            readWeather();
                        }
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

    /**
     * 检测更新
     */
    private void checkUpdate() {
        Observable.create(new ObservableOnSubscribe<String[]>() {
            @Override
            public void subscribe(final ObservableEmitter<String[]> emitter) throws Exception {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        InputStream is = null;
                        try {
                            URL url = new URL("http://101.200.193.60:8083/FGO/Download/hny.json");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setConnectTimeout(10000);
                            connection.setReadTimeout(5000);
                            connection.setRequestMethod("GET");
                            int responseCode = connection.getResponseCode();
                            if (responseCode == 200) {
                                is = connection.getInputStream();
                                String json = ConvertUtils.inputStream2String(is, "UTF-8");
                                JSONObject jsonObject = new JSONObject(json);
                                int newVersionCode = jsonObject.getInt("versionCode");
                                String fileSize = jsonObject.getString("fileSize");
                                if (AppUtils.getAppVersionCode() < newVersionCode) {
                                    emitter.onNext(new String[]{"0", fileSize});
                                } else emitter.onNext(new String[]{"3", fileSize});
                            } else emitter.onNext(new String[]{"2", ""});
                        } catch (Exception e) {
                            emitter.onNext(new String[]{"1", ""});
                        } finally {
                            try {
                                if (is != null) is.close();
                            } catch (IOException e) {

                            }
                        }
                    }
                }).start();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String[] strs) {
                        switch (strs[0]) {
                            case "0":
                                new QMUIDialog.MessageDialogBuilder(context)
                                        .setTitle(getString(R.string.fou_sit_mon_str7))
                                        .setMessage(getString(R.string.personal_str30) + strs[1] + "MB)")
                                        .addAction(getString(R.string.envir_str1), new QMUIDialogAction.ActionListener() {
                                            @Override
                                            public void onClick(QMUIDialog dialog, int index) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .addAction(getString(R.string.personal_str6), new QMUIDialogAction.ActionListener() {
                                            @Override
                                            public void onClick(QMUIDialog dialog, int index) {
                                                dialog.dismiss();
                                                //请求读写权限
                                                PermissionUtils.permission(PermissionConstants.getPermissions(STORAGE))
                                                        .callback(new PermissionUtils.SimpleCallback() {
                                                            @Override
                                                            public void onGranted() {
                                                                downFile();
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
                                        })
                                        .create().show();
                                break;
                            case "1":
//                                Toasts.showInfo(context, getString(R.string.request_error2));
                                break;
                            case "2":
//                                Toasts.showInfo(context, getString(R.string.personal_str29));
                                break;
                            case "3":
//                                Toasts.showInfo(context, getString(R.string.personal_str28));
                                break;
                            default:
//                                Toasts.showInfo(context, getString(R.string.request_error2));
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    /**
     * 下载apk
     */
    private void downFile() {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle(getString(R.string.personal_str31));
        dialog.setMax(100);
        dialog.setProgress(0);
        dialog.setCancelable(false);
        dialog.show();

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(final ObservableEmitter<Integer> emitter) throws Exception {
                String url = "http://101.200.193.60:8083/FGO/Download/huinongyun.apk";
                Request request = new Request.Builder().url(url).build();
                new OkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.onNext(-2);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code() != 200) {
                            emitter.onNext(-2);
                            return;
                        }
                        InputStream is = null;
                        byte[] buf = new byte[2048];
                        int len = 0;
                        FileOutputStream fos = null;
                        try {
                            //储存下载文件的目录
                            String savePath = Environment.getExternalStorageDirectory() + "/huinongyun/";
                            File dirFile = new File(savePath);
                            if (!dirFile.exists()) {
                                dirFile.mkdirs();
                            }
                            is = response.body().byteStream();
                            long total = response.body().contentLength();
                            File file = new File(savePath, "慧农云.apk");
                            fos = new FileOutputStream(file);
                            long sum = 0;
                            while ((len = is.read(buf)) != -1) {
                                fos.write(buf, 0, len);
                                sum += len;
                                int progress = (int) (sum * 1.0f / total * 100);
                                //下载中
                                emitter.onNext(progress);
                            }
                            fos.flush();
                            //下载完成
                            emitter.onNext(-1);
                        } catch (Exception e) {
                            emitter.onNext(-2);
                        } finally {
                            try {
                                if (is != null) is.close();
                                if (fos != null) fos.close();
                            } catch (IOException e) {

                            }
                        }
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        switch (integer) {
                            case -1:
                                dialog.dismiss();
                                AppUtils.installApp(Environment.getExternalStorageDirectory()
                                        + "/huinongyun/慧农云.apk");
                                break;
                            case -2:
                                dialog.dismiss();
                                Toasts.showInfo(context, getString(R.string.personal_str32));
                                break;
                            default:
                                if (integer >= 0 && integer <= 100) {
                                    dialog.setProgress(integer);
                                }
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                    }
                });
    }

    private void startActivity(Class<?> targetClass) {
        Intent intent = new Intent(context, targetClass);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
//        EventBus.getDefault().unregister(this);
//        mDialog.dismiss();
    }

    @Override
    public void onStop() {
        super.onStop();
//        EventBus.getDefault().unregister(this);
//        mDialog.dismiss();
    }

    /**
     * 取消注册
     */
    private void unRegister() {
        WebServiceManage.dispose();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
