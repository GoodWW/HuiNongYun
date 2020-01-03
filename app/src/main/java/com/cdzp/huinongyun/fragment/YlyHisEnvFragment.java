package com.cdzp.huinongyun.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.bean.YlyByFkFieldUuids;
import com.cdzp.huinongyun.bean.YlySoilResults;
import com.cdzp.huinongyun.bean.YlyWeaStaResults;
import com.cdzp.huinongyun.util.MyMarkerView;
import com.cdzp.huinongyun.util.Toasts;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog.MultiCheckableDialogBuilder;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
 * 历史环境数据（原力元特殊定制）
 */
public class YlyHisEnvFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean isVisible, isInit, isStart;
    private TextView tv_terminal, tv_start, tv_end;
    private LineChart mLineChart;
    private QMUITipDialog mDialog;
    private Description description;
    private List<String> strxAxis;
    private XAxis xAxis;
    private YAxis yAxis;
    private List<Float> min;
    private List<Float> max;
    private List<ILineDataSet> temLineList;
    private List<ILineDataSet> lineList;
    private TimePickerView pvTime;
    private Date startDate, endDate;
    private QMUIBottomSheet mBottomSheet;
    private String url = "http://101.207.139.26:10124/Devices/Garden/getDatas", jsonStr, deviceEui;
    private String startTime, endTime;
    private int type,mPosition;
    private String[] strLegend;
    private int[] CheckedItem;
    private List<String[]> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_yly_his_env, container, false);
            initView();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible) {
            if (view != null && !isInit) {
                isInit = true;
                getByFkFieldUuids();
            }
        } else {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fyhe1:
                if (list != null) {
                    if (list.size() > 0 && mBottomSheet != null) {
                        mBottomSheet.show();
                    } else Toasts.showInfo(context, getString(R.string.request_error8));
                } else {
                    getByFkFieldUuids();
                }
                break;
            case R.id.ll_fyhe2:
                isStart = true;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                pvTime.setDate(calendar);
                pvTime.show();
                break;
            case R.id.ll_fyhe3:
                isStart = false;
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(endDate);
                pvTime.setDate(calendar1);
                pvTime.show();
                break;
            case R.id.btn_fyhe_query:
                if (tv_terminal.getText().length() > 0 && tv_start.getText().length() > 0
                        && tv_end.getText().length() > 0) {
                    if (endDate.getTime() - startDate.getTime() < 3 * 24 * 60 * 60 * 1000 + 10000) {
                        requestData();
                    } else Toasts.showInfo(context, getString(R.string.envir_str7));
                } else Toasts.showInfo(context, getString(R.string.request_error6));
                break;
            case R.id.btn_fyhe_custom:
                showMultiChoiceDialog();
                break;
            default:

                break;
        }
    }

    private void initView() {
        context = getContext();
        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        mDialog.setCancelable(false);

        tv_terminal = view.findViewById(R.id.tv_fyhe_terminal);
        tv_start = view.findViewById(R.id.tv_fyhe_start);
        tv_end = view.findViewById(R.id.tv_fyhe_end);
        pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (isStart) {
                    startDate = date;
                    startTime = getTime(startDate);
                    jsonStr = "{\"deviceEui\":\"" + deviceEui + "\",\"startTime\":\"" + startTime + "\",\"endTime\":\"" + endTime + "\",\"type\":" + type + "}";
                    tv_start.setText(startTime);
                } else {
                    endDate = date;
                    endTime = getTime(endDate);
                    jsonStr = "{\"deviceEui\":\"" + deviceEui + "\",\"startTime\":\"" + startTime + "\",\"endTime\":\"" + endTime + "\",\"type\":" + type + "}";
                    tv_end.setText(endTime);
                }
            }
        })
                .isCyclic(true)
                .setType(new boolean[]{true, true, true, true, true, true})
                .build();
        endDate = new Date();
        endTime = getTime(endDate);
        tv_end.setText(endTime);
        startDate = new Date();
        startDate.setTime(endDate.getTime() - 24 * 60 * 60 * 1000);
        startTime = getTime(startDate);
        tv_start.setText(startTime);

        mLineChart = view.findViewById(R.id.mLinechart_fyhe);
        mLineChart.setNoDataText(getString(R.string.envir_nodata));
        mLineChart.setPinchZoom(true);//XY轴同时缩放
        //图例
        Legend legend = mLineChart.getLegend();
        legend.setWordWrapEnabled(true);//图例是否换行
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //描述
        description = new Description();
        description.setText("");
        mLineChart.setDescription(description);

        strxAxis = new ArrayList<>();

        //X轴
        xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴居底
        xAxis.setDrawGridLines(false);//去掉X轴网格
        xAxis.setGranularity(1);//设置X轴最小间隔
//        xAxis.setSpaceMin(4f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int label = (int) value;
                if (label < strxAxis.size() && label >= 0) {
                    return strxAxis.get(label);
                } else return value + "";
            }
        });

        //取消右Y轴
        mLineChart.getAxisRight().setEnabled(false);

        yAxis = mLineChart.getAxisLeft();
//        yAxis.setDrawGridLines(false);//去掉Y轴网格

        mLineChart.setMarker(new MyMarkerView(context, R.layout.marker_layout));

        lineList = new ArrayList();
        temLineList = new ArrayList();
        min = new ArrayList();
        max = new ArrayList();

        view.findViewById(R.id.ll_fyhe1).setOnClickListener(this);
        view.findViewById(R.id.ll_fyhe2).setOnClickListener(this);
        view.findViewById(R.id.ll_fyhe3).setOnClickListener(this);
        view.findViewById(R.id.btn_fyhe_query).setOnClickListener(this);
        view.findViewById(R.id.btn_fyhe_custom).setOnClickListener(this);

        if (isVisible && !isInit) {
            isInit = true;
            getByFkFieldUuids();
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
                                                    mPosition = position;
                                                    switch (list.get(mPosition)[0]) {
                                                        case "2":
                                                            deviceEui = list.get(mPosition)[1];
                                                            type = 2;
                                                            jsonStr = "{\"deviceEui\":\"" + deviceEui + "\",\"startTime\":\"" + startTime + "\",\"endTime\":\"" + endTime + "\",\"type\":" + type + "}";
                                                            tv_terminal.setText(list.get(mPosition)[2]);
                                                            break;
                                                        case "3":
                                                            deviceEui = list.get(mPosition)[1];
                                                            type = 3;
                                                            jsonStr = "{\"deviceEui\":\"" + deviceEui + "\",\"startTime\":\"" + startTime + "\",\"endTime\":\"" + endTime + "\",\"type\":" + type + "}";
                                                            tv_terminal.setText(list.get(mPosition)[2]);
                                                            break;
                                                    }
                                                    dialog.dismiss();
                                                }
                                            });
                                    for (int i = 0; i < list.size(); i++) {
                                        mBuilder.addItem(list.get(i)[2]);
                                    }
                                    mBuilder.setCheckedIndex(mPosition);
                                    mBottomSheet = mBuilder.build();
                                    if (list.get(mPosition)[0].equals("3")) {
                                        deviceEui = list.get(mPosition)[1];
                                        type = 3;
                                        jsonStr = "{\"deviceEui\":\"" + deviceEui + "\",\"startTime\":\"" + startTime + "\",\"endTime\":\"" + endTime + "\",\"type\":" + type + "}";
                                        tv_terminal.setText(list.get(mPosition)[2]);
                                        requestData();
                                    } else if (list.get(mPosition)[0].equals("2")) {
                                        deviceEui = list.get(mPosition)[1];
                                        type = 2;
                                        jsonStr = "{\"deviceEui\":\"" + deviceEui + "\",\"startTime\":\"" + startTime + "\",\"endTime\":\"" + endTime + "\",\"type\":" + type + "}";
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

    private void showMultiChoiceDialog() {
        if (mLineChart.getData() != null && strLegend != null && strLegend.length > 0) {
            final MultiCheckableDialogBuilder multiBuilder = new MultiCheckableDialogBuilder(context)
                    .setCheckedItems(CheckedItem)
                    .addItems(strLegend, null);
            multiBuilder.setTitle(getString(R.string.envir_str));
            multiBuilder.addAction(getString(R.string.envir_str1), new QMUIDialogAction.ActionListener() {
                @Override
                public void onClick(QMUIDialog dialog, int index) {
                    dialog.dismiss();
                }
            });
            multiBuilder.addAction(getString(R.string.envir_str2), new QMUIDialogAction.ActionListener() {
                @Override
                public void onClick(QMUIDialog dialog, int index) {
                    CheckedItem = multiBuilder.getCheckedItemIndexes();
                    dialog.dismiss();
                    afreshDraw();
                }
            });
            multiBuilder.create().show();
        }
    }

    private void afreshDraw() {
        mLineChart.clear();
        lineList.clear();
        List<Float> mins = new ArrayList<>();
        List<Float> maxs = new ArrayList<>();
        for (int i = 0; i < CheckedItem.length; i++) {
            lineList.add(temLineList.get(CheckedItem[i]));
            mins.add(min.get(CheckedItem[i]));
            maxs.add(max.get(CheckedItem[i]));
        }

        if (mins.size() > 0) {
            //重新给Y轴设置最大最小值
            Float[] fMin = mins.toArray(new Float[mins.size()]);
            Bubble(fMin);
            yAxis.setAxisMinimum(fMin[0] - 10);
            Float[] fMax = maxs.toArray(new Float[maxs.size()]);
            Bubble(fMax);
            yAxis.setAxisMaximum(fMax[fMax.length - 1] + 10);
        }

        //设置动画
        if (strxAxis.size() > 10) {
            mLineChart.animateX(2000, Easing.EasingOption.Linear);
        }
        mLineChart.setData(new LineData(lineList));
        mLineChart.invalidate();
        mLineChart.notifyDataSetChanged();
    }

    private void requestData() {
        mDialog.show();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                        RequestBody body = RequestBody.create(JSON, jsonStr);
                        Request request = new Request.Builder()
                                .url("http://221.4.210.83:8046/Devices/Garden/getDatas")
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
                        try {
                            switch (list.get(mPosition)[0]) {
                                case "2":
                                    YlySoilResults result1 = new Gson().fromJson(s, YlySoilResults.class);
                                    if (result1.getCode().equals("200") && result1.getData() != null
                                            && result1.getData().size() > 0) {
                                        setData(result1, 2);
                                    } else {
                                        Toasts.showInfo(context, getString(R.string.request_error8));
                                        setNoData();
                                    }
                                    break;
                                case "3":
                                    YlyWeaStaResults result2 = new Gson().fromJson(s, YlyWeaStaResults.class);
                                    if (result2.getCode().equals("200") && result2.getData() != null
                                            && result2.getData().size() > 0) {
                                        setData(result2, 10);
                                    } else {
                                        Toasts.showInfo(context, getString(R.string.request_error8));
                                        setNoData();
                                    }
                                    break;
                                default:
                                    setNoData();
                                    break;
                            }
                        } catch (Exception e) {
                            setNoData();
                        }
                        mDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        setNoData();
                        mDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setData(YlyWeaStaResults result, int num) {
        try {
            if (result.getData().size() > 100) {
                Toasts.showInfo(context, "数据太多");
                return;
            }
            mLineChart.clear();
            mLineChart.fitScreen();//重置缩放移动
            strxAxis.clear();
            lineList.clear();
            temLineList.clear();
            min.clear();
            max.clear();
            for (int i = 0; i < result.getData().size(); i++) {
                String timeStam = result.getData().get(i).getTimeStam();
                if (timeStam.length() == 19) {
                    strxAxis.add(timeStam.substring(8, 19));
                } else strxAxis.add(timeStam);
            }

            strLegend = new String[num];
            CheckedItem = new int[num];
            for (int i = 0; i < num; i++) {
                String str;
                switch (i) {
                    case 0:
                        str = "光强(Lux)";
                        strLegend[0] = "光照强度(Lux)";
                        break;
                    case 1:
                        str = "光照时长(h)";
                        strLegend[1] = "光照时长(h)";
                        break;
                    case 2:
                        str = "24时雨量(mm)";
                        strLegend[2] = "24小时雨量(mm)";
                        break;
                    case 3:
                        str = "降雨量(mm)";
                        strLegend[3] = "降雨量(mm)";
                        break;
                    case 4:
                        str = "空温(℃)";
                        strLegend[4] = "空气温度(℃)";
                        break;
                    case 5:
                        str = "空湿(%)";
                        strLegend[5] = "空气湿度(%)";
                        break;
                    case 6:
                        str = "PH";
                        strLegend[6] = "PH";
                        break;
                    case 7:
                        str = "风向(°)";
                        strLegend[7] = "风向(°)";
                        break;
                    case 8:
                        str = "风速(m/s)";
                        strLegend[8] = "风速(m/s)";
                        break;
                    case 9:
                        str = "电压(V)";
                        strLegend[9] = "电压(V)";
                        break;
                    default:
                        str = "无";
                        strLegend[i] = "无";
                        break;
                }
                CheckedItem[i] = i;
                List<Entry> entries = new ArrayList<>();
                List<Float> list = new ArrayList<>();
                for (int i1 = 0; i1 < result.getData().size(); i1++) {
                    float value = 0;
                    switch (i) {
                        case 0:
                            value = Float.parseFloat(result.getData().get(i1).getLightIntensity());
                            break;
                        case 1:
                            value = Float.parseFloat(result.getData().get(i1).getLightDuration());
                            break;
                        case 2:
                            value = Float.parseFloat(result.getData().get(i1).getDailyRainFall());
                            break;
                        case 3:
                            value = Float.parseFloat(result.getData().get(i1).getRainFall());
                            break;
                        case 4:
                            value = Float.parseFloat(result.getData().get(i1).getAirTemperature());
                            break;
                        case 5:
                            value = Float.parseFloat(result.getData().get(i1).getAirHumidity());
                            break;
                        case 6:
                            value = Float.parseFloat(result.getData().get(i1).getPh());
                            break;
                        case 7:
                            value = Float.parseFloat(result.getData().get(i1).getWindDirection());
                            break;
                        case 8:
                            value = Float.parseFloat(result.getData().get(i1).getWindSpeed());
                            break;
                        case 9:
                            value = Float.parseFloat(result.getData().get(i1).getVoltage());
                            break;
                        default:
                            break;
                    }
                    entries.add(new Entry(i1, value));
                    list.add(value);
                }
                //排序找出最大小值
                Float[] f1 = list.toArray(new Float[list.size()]);
                Bubble(f1);
                min.add(f1[0]);
                max.add(f1[f1.length - 1]);

                LineDataSet lineDataSet = new LineDataSet(entries, str);
                Random random = new Random();
                int r = random.nextInt(256);
                int g = random.nextInt(256);
                int b = random.nextInt(256);
                int color = Color.rgb(r, g, b);
                lineDataSet.setColor(color);
                lineDataSet.setCircleColor(color);
                lineDataSet.setDrawCircleHole(false);
                lineDataSet.setValueFormatter(new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        return value + "";
                    }
                });
                lineList.add(lineDataSet);
            }
            temLineList.addAll(lineList);
            //设置Y轴最大小值
            Float[] mins = min.toArray(new Float[min.size()]);
            Bubble(mins);
            yAxis.setAxisMinimum(mins[0] - 10);
            Float[] maxs = max.toArray(new Float[max.size()]);
            Bubble(maxs);
            yAxis.setAxisMaximum(maxs[maxs.length - 1] + 10);

            mLineChart.setData(new LineData(lineList));
            description.setText(tv_terminal.getText().toString() + " " + startTime + "~" + endTime);
            if (strxAxis.size() > 10) {
                mLineChart.animateX(2000, Easing.EasingOption.Linear);
            }
            mLineChart.invalidate();
            mLineChart.notifyDataSetChanged();
        } catch (Exception e) {
            setNoData();
        }
    }

    private void setNoData() {
        mLineChart.fitScreen();//重置缩放移动
        mLineChart.setData(null);
        description.setText(tv_terminal.getText().toString() + " " + startTime + "~" + endTime);
        mLineChart.invalidate();
        mLineChart.notifyDataSetChanged();
    }

    private void setData(YlySoilResults result, int num) {
        try {
            if (result.getData().size() > 100) {
                Toasts.showInfo(context, "数据太多");
                return;
            }
            mLineChart.fitScreen();//重置缩放移动
            strxAxis.clear();
            lineList.clear();
            temLineList.clear();
            min.clear();
            max.clear();

            for (int i = 0; i < result.getData().size(); i++) {
                String timeStam = result.getData().get(i).getTimeStam();
                if (timeStam.length() == 19) {
                    strxAxis.add(timeStam.substring(8, 19));
                } else strxAxis.add(timeStam);
            }

            strLegend = new String[num];
            CheckedItem = new int[num];
            for (int i = 0; i < num; i++) {
                String str;
                switch (i) {
                    case 0:
                        str = "土壤温度(℃)";
                        strLegend[0] = str;
                        break;
                    case 1:
                        str = "土壤水分(%)";
                        strLegend[1] = str;
                        break;
                    default:
                        str = "无";
                        strLegend[i] = str;
                        break;
                }
                CheckedItem[i] = i;
                List<Entry> entries = new ArrayList<>();
                List<Float> list = new ArrayList<>();
                for (int i1 = 0; i1 < result.getData().size(); i1++) {
                    float value = 0;
                    switch (i) {
                        case 0:
                            value = Float.parseFloat(result.getData().get(i1).getWenDu());
                            break;
                        case 1:
                            value = Float.parseFloat(result.getData().get(i1).getShuiFen());
                            break;
                        default:
                            break;
                    }
                    entries.add(new Entry(i1, value));
                    list.add(value);
                }
                //排序找出最大小值
                Float[] f1 = list.toArray(new Float[list.size()]);
                Bubble(f1);
                min.add(f1[0]);
                max.add(f1[f1.length - 1]);

                LineDataSet lineDataSet = new LineDataSet(entries, str);
                Random random = new Random();
                int r = random.nextInt(256);
                int g = random.nextInt(256);
                int b = random.nextInt(256);
                int color = Color.rgb(r, g, b);
                lineDataSet.setColor(color);
                lineDataSet.setCircleColor(color);
                lineDataSet.setDrawCircleHole(false);
                lineDataSet.setValueFormatter(new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        return value + "";
                    }
                });
                lineList.add(lineDataSet);
            }
            temLineList.addAll(lineList);
            //设置Y轴最大小值
            Float[] mins = min.toArray(new Float[min.size()]);
            Bubble(mins);
            yAxis.setAxisMinimum(mins[0] - 10);
            Float[] maxs = max.toArray(new Float[max.size()]);
            Bubble(maxs);
            yAxis.setAxisMaximum(maxs[maxs.length - 1] + 10);

            mLineChart.setData(new LineData(lineList));
            description.setText(tv_terminal.getText().toString() + " " + startTime + "~" + endTime);
            if (strxAxis.size() > 10) {
                mLineChart.animateX(2000, Easing.EasingOption.Linear);
            }
            mLineChart.invalidate();
            mLineChart.notifyDataSetChanged();
        } catch (Exception e) {
            setNoData();
        }
    }

    private void Bubble(Float[] r) {
        int low = 0;
        int high = r.length - 1; //设置变量的初始值
        float tmp;
        int j;
        while (low < high) {
            for (j = low; j < high; ++j) //正向冒泡,找到最大者
                if (r[j] > r[j + 1]) {
                    tmp = r[j];
                    r[j] = r[j + 1];
                    r[j + 1] = tmp;
                }
            --high;                 //修改high值, 前移一位
            for (j = high; j > low; --j) //反向冒泡,找到最小者
                if (r[j] < r[j - 1]) {
                    tmp = r[j];
                    r[j] = r[j - 1];
                    r[j - 1] = tmp;
                }
            ++low;                  //修改low值,后移一位
        }
    }

    private String getTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }
}
