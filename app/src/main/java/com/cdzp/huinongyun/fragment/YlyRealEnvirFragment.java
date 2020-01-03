package com.cdzp.huinongyun.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdzp.huinongyun.MyApplication;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.adapter.RealEnvirAdapter;
import com.cdzp.huinongyun.bean.YlyByFkFieldUuids;
import com.cdzp.huinongyun.bean.YlySoilResult;
import com.cdzp.huinongyun.bean.YlyWeaStaResult;
import com.cdzp.huinongyun.util.Toasts;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 实时环境数据（原力元特殊定制）
 */
public class YlyRealEnvirFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean isVisible, isToast = true, isRequest, isInit;
    private CountDownTimer countDownTimer;
    private TextView tv_terminal;
    private TimerTask task;
    private Timer timer;
    private QMUITipDialog mDialog;
    private String url, jsonStr;
    private Disposable disposable;
    private RecyclerView mRecyclerView;
    private RealEnvirAdapter mAdapter;
    private QMUIBottomSheet mBottomSheet;
    private List<String[]> list;
    private int mPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_yly_real_envir, container, false);
            initView();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible) {
            if (view != null) {
                if (tv_terminal.getText().length() > 0)
                    isRequest = true;
                if (!isInit) {
                    isInit = true;
                    getByFkFieldUuids();
                }
            }
        } else {
            if (countDownTimer != null) countDownTimer.cancel();
            isRequest = false;
        }
    }

    private void initView() {
        context = getContext();

        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        task = new TimerTask() {
            @Override
            public void run() {
                if (isRequest) {
                    isRequest = false;
                    requestData();
                }
            }
        };
        timer = new Timer();
        countDownTimer = new CountDownTimer(5000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                isRequest = true;
            }
        };

        tv_terminal = view.findViewById(R.id.tv_fyre_terminal);
        mRecyclerView = view.findViewById(R.id.recycler_fyre);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(context.getResources().getColor(R.color.decoration), 1, 1));
        mAdapter = new RealEnvirAdapter(context);
        mRecyclerView.setAdapter(mAdapter);

        view.findViewById(R.id.ll_fyre).setOnClickListener(this);

        timer.schedule(task, 0, 5000);
        if (isVisible && !isInit) {
            isInit = true;
            getByFkFieldUuids();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fyre:
                if (list != null) {
                    if (list.size() > 0 && mBottomSheet != null) {
                        mBottomSheet.show();
                    } else Toasts.showInfo(context, getString(R.string.request_error8));
                } else {
                    getByFkFieldUuids();
                }
                break;
            default:

                break;
        }
    }

    private void getByFkFieldUuids() {
        mDialog.show();
        url = "http://221.4.210.83:8046/Devices/Device/getEncoderDevicesByFkFieldUuids";
        jsonStr = "{\"fkFieldUuids\":\"5db24198-7458-11e8-b00a-0242ac110003,fbab83b0-7457-11e8-b00a-0242ac110003," +
                "e0c958f2-7456-11e8-b00a-0242ac110003,c2d074b4-7458-11e8-b00a-0242ac110003,ff772fd4-7458-11e8-b00a-0242ac110003," +
                "3d3d4664-7459-11e8-b00a-0242ac110003,73cc317c-7459-11e8-b00a-0242ac110003\",\"type\": 0}";
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                        RequestBody body = RequestBody.create(JSON, jsonStr);
                        Request request = new Request.Builder()
                                .url(url)
                                .post(body)
                                .build();
                        Call call = new OkHttpClient().newCall(request);
                        Response response = null;
                        try {
                            response = call.execute();
                            String str = response.body().string();
                            emitter.onNext(str);
                        } catch (IOException e) {
                            e.printStackTrace();
                            emitter.onError(e);
                        }
                    }
                }).start();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        list = new ArrayList<>();
                        try {
                            YlyByFkFieldUuids result = new Gson().fromJson(s, YlyByFkFieldUuids.class);
                            if (result.getRows() != null && result.getRows().size() > 0) {
                                for (int i = 0; i < result.getRows().size(); i++) {
                                    if (result.getRows().get(i).getType() == 3) {
                                        list.add(new String[]{"3",
                                                result.getRows().get(i).getCameraUuid(),
                                                result.getRows().get(i).getCameraName()});
                                    }
                                }
                                for (int i = 0; i < result.getRows().size(); i++) {
                                    if (result.getRows().get(i).getType() == 2) {
                                        list.add(new String[]{"2",
                                                result.getRows().get(i).getCameraUuid(),
                                                result.getRows().get(i).getCameraName()});
                                    }
                                }
                                if (list.size() > 0) {
                                    mPosition = 0;
                                    BottomListSheetBuilder mBuilder = new BottomListSheetBuilder(context, true)
                                            .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                                                @Override
                                                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                                    countDownTimer.cancel();
                                                    isRequest = false;
                                                    if (disposable != null) disposable.dispose();
                                                    mPosition = position;
                                                    switch (list.get(mPosition)[0]) {
                                                        case "2":
                                                            url = "http://221.4.210.83:8046/Devices/Garden/getLatestSensorData";
                                                            jsonStr = "{\"deviceEui\":\"" + list.get(mPosition)[1] + "\"}";
                                                            tv_terminal.setText(list.get(mPosition)[2]);
                                                            break;
                                                        case "3":
                                                            url = "http://221.4.210.83:8046/Devices/Garden/getLatestStationData";
                                                            jsonStr = "{\"deviceEui\":\"" + list.get(mPosition)[1] + "\"}";
                                                            tv_terminal.setText(list.get(mPosition)[2]);
                                                            break;
                                                    }
                                                    mAdapter.getList().clear();
                                                    mAdapter.notifyDataSetChanged();
                                                    dialog.dismiss();
                                                    mDialog.show();
                                                    requestData();
                                                    countDownTimer.start();
                                                }
                                            });
                                    for (int i = 0; i < list.size(); i++) {
                                        mBuilder.addItem(list.get(i)[2]);
                                    }
                                    mBuilder.setCheckedIndex(mPosition);
                                    mBottomSheet = mBuilder.build();
                                    if (list.get(mPosition)[0].equals("3")) {
                                        url = "http://221.4.210.83:8046/Devices/Garden/getLatestStationData";
                                        jsonStr = "{\"deviceEui\":\"" + list.get(mPosition)[1] + "\"}";
                                        tv_terminal.setText(list.get(mPosition)[2]);
                                        requestData();
                                    } else if (list.get(mPosition)[0].equals("2")) {
                                        url = "http://221.4.210.83:8046/Devices/Garden/getLatestSensorData";
                                        jsonStr = "{\"deviceEui\":\"" + list.get(mPosition)[1] + "\"}";
                                        tv_terminal.setText(list.get(mPosition)[2]);
                                        requestData();
                                    }
                                } else Toasts.showInfo(context, getString(R.string.request_error8));
                            } else Toasts.showInfo(context, getString(R.string.request_error8));
                        } catch (Exception e) {
                            Toasts.showInfo(context, getString(R.string.request_error7));
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

    private synchronized void requestData() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                        RequestBody body = RequestBody.create(JSON, jsonStr);
                        Request request = new Request.Builder()
                                .url(url)
                                .post(body)
                                .build();
                        Call call = new OkHttpClient().newCall(request);
                        Response response = null;
                        try {
                            response = call.execute();
                            String str = response.body().string();
                            emitter.onNext(str);
                        } catch (IOException e) {
                            e.printStackTrace();
                            emitter.onError(e);
                        }
                    }
                }).start();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(String s) {
                        try {
                            if (list.get(mPosition)[0].equals("2")) {//土壤
                                YlySoilResult result = new Gson().fromJson(s, YlySoilResult.class);
                                if (result.getCode().equals("200")) {
                                    mAdapter.getList().clear();
                                    mAdapter.getList().add(new String[]{
                                            "土壤温度",
                                            result.getData().getWenDu() + "℃",
                                            "3"
                                    });
                                    mAdapter.getList().add(new String[]{
                                            "土壤水分",
                                            result.getData().getShuiFen() + "%",
                                            "4"
                                    });
                                    mAdapter.notifyDataSetChanged();
                                    isToast = true;
                                } else ShowToast(result.getMessage());
                            } else if (list.get(mPosition)[0].equals("3")) {//气象站
                                YlyWeaStaResult result = new Gson().fromJson(s, YlyWeaStaResult.class);
                                if (result.getCode().equals("200")) {
                                    mAdapter.getList().clear();
                                    mAdapter.getList().add(new String[]{
                                            "光照强度",
                                            result.getData().getLightIntensity() + "Lux",
                                            "5"
                                    });
                                    mAdapter.getList().add(new String[]{
                                            "光照时长",
                                            result.getData().getLightDuration() + "h",
                                            "5"
                                    });
                                    mAdapter.getList().add(new String[]{
                                            "24时雨量",
                                            result.getData().getDailyRainFall() + "mm",
                                            "14"
                                    });
                                    mAdapter.getList().add(new String[]{
                                            " 降雨量  ",
                                            result.getData().getRainFall() + "mm",
                                            "14"
                                    });
                                    mAdapter.getList().add(new String[]{
                                            "空气温度",
                                            result.getData().getAirTemperature() + "℃",
                                            "1"
                                    });
                                    mAdapter.getList().add(new String[]{
                                            "空气湿度",
                                            result.getData().getAirHumidity() + "%",
                                            "2"
                                    });
                                    mAdapter.getList().add(new String[]{
                                            "     PH     ",
                                            result.getData().getPh(),
                                            "11"
                                    });
                                    mAdapter.getList().add(new String[]{
                                            "    风向    ",
                                            result.getData().getWindDirection() + "°",
                                            "13"
                                    });
                                    mAdapter.getList().add(new String[]{
                                            "    风速    ",
                                            result.getData().getWindSpeed() + "m/s",
                                            "13"
                                    });
                                    mAdapter.getList().add(new String[]{
                                            "    电压    ",
                                            result.getData().getVoltage() + "V",
                                            "0"
                                    });
                                    mAdapter.notifyDataSetChanged();
                                    isToast = true;
                                } else ShowToast(result.getMessage());
                            }
                        } catch (Exception e) {

                        }
                        isRequest = true;
                        mDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mDialog.dismiss();
                        ShowToast("获取异常");
                        isRequest = true;
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void ShowToast(String s) {
        if (isToast) {
            isToast = false;
            Toasts.showInfo(context, s);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) disposable.dispose();
        if (task != null) task.cancel();
        if (timer != null) timer.cancel();
    }

}
