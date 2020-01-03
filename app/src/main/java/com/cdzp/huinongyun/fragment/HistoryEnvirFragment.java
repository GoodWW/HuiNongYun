package com.cdzp.huinongyun.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.cdzp.huinongyun.MyApplication;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.adapter.HisEnvFragAdapter;
import com.cdzp.huinongyun.adapter.HisEnvFragAdapter2;
import com.cdzp.huinongyun.bean.HisAnaResult;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.MyMarkerView;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog.CheckableDialogBuilder;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog.MultiCheckableDialogBuilder;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.cdzp.huinongyun.util.Data.cGhList;
import static com.cdzp.huinongyun.util.Data.locList;
import static com.cdzp.huinongyun.util.Data.locList1;
import static com.cdzp.huinongyun.util.Data.pGhList;

/**
 * 历史环境数据
 */
public class HistoryEnvirFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean isVisible, isInit = false;
    private QMUITipDialog mDialog;
    private LineChart mLineChart;
    private List<ILineDataSet> temLineList;
    private List<ILineDataSet> lineList;
    private List<Float> min;
    private List<Float> max;
    private XAxis xAxis;
    private YAxis yAxis;
    private Description description;
    private List<String> strxAxis;
    private String[] strLegend;
    private QMUIBottomSheet mBottomSheet2;
    private int mPosition1, mPosition2, mPosition3 = -1, checkedIndex;
    private TextView tv_terminal, tv_area, tv_tart, tv_end;
    private TimePickerView pvTime;
    private boolean isStart, isChart = true;
    private Date startDate, endDate;
    private int[] CheckedItem;
    private RecyclerView mRecyclerView, mRecyclerView1;
    private HisEnvFragAdapter mAdapter;
    private HisEnvFragAdapter2 mAdapter2;
    private LinearLayout ll;
    private Button btn_qh;
    private List<List<String[]>> list2;
    private boolean isSunlight = false;//是否是日光,电磁阀
    private OptionsPickerView mOptions;
    private boolean isEnglish;//是否中英文
    private String[] winds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_history_envir, container, false);
            initView();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible) {
            eRegister();
            if (!isInit && view != null) {
                isInit = true;
                initData();
            }
        } else {
            EventBus.getDefault().unregister(this);
        }
    }

    private void initView() {
        context = getContext();
        isEnglish = ((MyApplication) getActivity().getApplication()).isEnglish();
        if (Data.GhType != null) {
            switch (Data.GhType) {
                case "8":
                case "9":
                    isSunlight = true;
                    break;
                default:

                    break;
            }
        }

        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                WebServiceManage.dispose();
            }
        });

        tv_terminal = view.findViewById(R.id.tv_fhe_terminal);
        tv_area = view.findViewById(R.id.tv_fhe_area);
        tv_tart = view.findViewById(R.id.tv_fhe_start);
        tv_end = view.findViewById(R.id.tv_fhe_end);

        winds = new String[]{getString(R.string.envir_str8),
                getString(R.string.envir_str9),
                getString(R.string.envir_str10),
                getString(R.string.envir_str11),
                getString(R.string.envir_str12),
                getString(R.string.envir_str13),
                getString(R.string.envir_str14),
                getString(R.string.envir_str15),
                getString(R.string.envir_str16)};

        mLineChart = view.findViewById(R.id.mLinechart_fhe);

        mLineChart.setNoDataText(getString(R.string.envir_nodata));
        mLineChart.setPinchZoom(true);//XY轴同时缩放

        //动画
//        mLineChart.animateY(3000, Easing.EasingOption.Linear);
//        mLineChart.animateX(2000, Easing.EasingOption.Linear);

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
        //左Y轴
        yAxis = mLineChart.getAxisLeft();
//        yAxis.setDrawGridLines(false);//去掉Y轴网格

        mLineChart.setMarker(new MyMarkerView(context, R.layout.marker_layout));

        lineList = new ArrayList();
        temLineList = new ArrayList();
        min = new ArrayList();
        max = new ArrayList();
        list2 = new ArrayList();
        mLineChart.setData(null);

        endDate = new Date();
        tv_end.setText(getTime(endDate));
        startDate = new Date();
        startDate.setTime(endDate.getTime() - 24 * 60 * 60 * 1000);
        tv_tart.setText(getTime(startDate));

        pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (isStart) {
                    startDate = date;
                    tv_tart.setText(getTime(startDate));
                } else {
                    endDate = date;
                    tv_end.setText(getTime(endDate));
                }
            }
        })
                .isCyclic(true)
                .setType(new boolean[]{true, true, true, true, false, false})
                .build();

        mRecyclerView = view.findViewById(R.id.mRecyclerView_fhe);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(context.getResources().getColor(R.color.decoration), 1, 1));
        mAdapter = new HisEnvFragAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mPosition2 = -1;

        mRecyclerView1 = view.findViewById(R.id.mRecyclerView_fhe1);
        mRecyclerView1.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView1.addItemDecoration(new DefaultItemDecoration(context.getResources().getColor(R.color.decoration), 1, 1));
        mAdapter2 = new HisEnvFragAdapter2();
        mRecyclerView1.setAdapter(mAdapter2);

        ll = view.findViewById(R.id.ll_fhe6);
        btn_qh = view.findViewById(R.id.btn_fhe_qh);
        view.findViewById(R.id.ll_fhe1).setOnClickListener(this);
        view.findViewById(R.id.ll_fhe2).setOnClickListener(this);
        view.findViewById(R.id.ll_fhe3).setOnClickListener(this);
        view.findViewById(R.id.ll_fhe4).setOnClickListener(this);
        view.findViewById(R.id.ll_fhe5).setOnClickListener(this);
        view.findViewById(R.id.btn_fhe_query).setOnClickListener(this);
        view.findViewById(R.id.btn_fhe_custom).setOnClickListener(this);
        btn_qh.setOnClickListener(this);

        if (!isInit && isVisible) {
            isInit = true;
            eRegister();
            initData();
        }
    }

    private String getTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH");
        return formatter.format(date);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 666:
                    parseGHSelResult();
                    break;
                case 102:
                    parseHAResult(message.getmResult());
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

    private void parseHAResult(String s) {
        try {
            HisAnaResult result = new Gson().fromJson(s, HisAnaResult.class);
            if (result.isSuccess()) {
                if (result.getData() != null && result.getData().getXAxis() != null
                        && result.getData().getXAxis().size() > 0
                        && result.getData().getLegend() != null
                        && result.getData().getLegend().size() > 0
                        && result.getData().getData() != null
                        && result.getData().getData().size() > 0
                        && result.getData().getData().get(0).getData() != null
                        && result.getData().getData().get(0).getData().size() > 0) {
                    mLineChart.clear();
                    mLineChart.fitScreen();//重置缩放移动
                    strxAxis.clear();
                    strxAxis.addAll(result.getData().getXAxis());
                    xAxis.resetAxisMinimum();
                    xAxis.resetAxisMaximum();
                    xAxis.setAxisMinimum(0);
                    xAxis.setAxisMaximum(strxAxis.size() - 1);

                    strLegend = new String[result.getData().getData().size()];
                    CheckedItem = new int[result.getData().getData().size()];
                    lineList.clear();
                    temLineList.clear();
                    mAdapter.getTemList().clear();
                    mAdapter.getList().clear();
                    min.clear();
                    max.clear();
                    mAdapter2.getList().clear();
                    list2.clear();
//                min = new float[result.getData().get(0).getData().size()];
//                max = new float[result.getData().get(0).getData().size()];
                    for (int i = 0; i < result.getData().getData().size(); i++) {
                        min.add(result.getData().getData().get(i).getMin());
                        max.add(result.getData().getData().get(i).getMax());
                        String name = result.getData().getData().get(i).getName();
                        String shortName = "";

                        for (int i1 = 0; i1 < result.getSensorInfo().size(); i1++) {
                            if (name.contains(result.getSensorInfo().get(i1).getBaseName())) {
                                String[] names = name.split("\\(");
                                if (isEnglish) {
                                    if (names.length > 1)
                                        name = result.getSensorInfo().get(i1).getEnglishName() + "(" + names[1];
                                    shortName = name;
                                } else {
                                    name = name.replace(".", "").replace(" ", "")
                                            .replace(",", "").replace(":", "");
                                    shortName = result.getSensorInfo().get(i1).getShortName()
                                            .replace(".", "").replace(" ", "")
                                            .replace(",", "").replace(":", "");
                                    if (names.length > 1) {
                                        shortName = shortName + "(" + names[1];
                                    }
                                }
                                break;
                            } else if (i1 == result.getSensorInfo().size() - 1) {
                                name = name.replace(".", "").replace(" ", "")
                                        .replace(",", "").replace(":", "");
                                shortName = name;
                            }
                        }

                        strLegend[i] = name;
                        CheckedItem[i] = i;
                        mAdapter.getList().add(new String[]{
                                name,
                                result.getData().getData().get(i).getMax() + "",
                                result.getData().getData().get(i).getMin() + "",
                                result.getData().getData().get(i).getAvg() + ""
                        });
                        if (mAdapter.getList().get(mAdapter.getList().size() - 1)[0].contains("雪") ||
                                mAdapter.getList().get(mAdapter.getList().size() - 1)[0].contains("风向")) {
                            mAdapter.getList().get(mAdapter.getList().size() - 1)[1] = "---";
                            mAdapter.getList().get(mAdapter.getList().size() - 1)[2] = "---";
                            mAdapter.getList().get(mAdapter.getList().size() - 1)[3] = "---";
                        }
//                        if (isEnglish) {
//                            shortName = name;
////                            name = filtrate(name);
//                        } else {
//
//                            shortName = shortName.replace(".", "").replace(" ", "")
//                                    .replace(",", "").replace(":", "");
//                        }
                        addLine(result.getData().getData().get(i).getData(), shortName
                                , result.getData().getFullTime(), strLegend[i]);
                    }
                    Float[] mins = min.toArray(new Float[min.size()]);
                    Bubble(mins);
                    yAxis.setAxisMinimum(mins[0] - 10);
                    Float[] maxs = max.toArray(new Float[max.size()]);
                    Bubble(maxs);
                    yAxis.setAxisMaximum(maxs[maxs.length - 1] + 10);
                    temLineList.addAll(lineList);
                    mAdapter.getTemList().addAll(mAdapter.getList());
                    mLineChart.setData(new LineData(lineList));
                    description.setText(tv_terminal.getText().toString() + " " + tv_area.getText().toString()
                            + " " + tv_tart.getText().toString() + "~" + tv_end.getText().toString());
                    checkedIndex = 0;
                    if (isChart) {
                        if (strxAxis.size() > 10) {
                            mLineChart.animateX(2000, Easing.EasingOption.Linear);
                        }
                        mLineChart.invalidate();
                        mLineChart.notifyDataSetChanged();
                        mAdapter.notifyDataSetChanged();
                    } else {
                        if (list2.size() > 0) {
                            mAdapter2.getList().addAll(list2.get(checkedIndex));
                            mAdapter2.notifyDataSetChanged();
                        }
                    }
                    mDialog.dismiss();
                } else {
                    mLineChart.fitScreen();//重置缩放移动
                    mLineChart.setData(null);
                    description.setText(tv_terminal.getText().toString() + " " + tv_area.getText().toString()
                            + " " + tv_tart.getText().toString() + "~" + tv_end.getText().toString());
                    mAdapter.getTemList().clear();
                    mAdapter.getList().clear();
                    list2.clear();
                    if (isChart) {
                        mLineChart.invalidate();
                        mLineChart.notifyDataSetChanged();
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mAdapter2.getList().clear();
                        mAdapter2.notifyDataSetChanged();
                    }
                    mDialog.dismiss();
                    Toasts.showInfo(context, getString(R.string.request_error8));
                }
            } else {
                mLineChart.fitScreen();//重置缩放移动
                mLineChart.setData(null);
                description.setText(tv_terminal.getText().toString() + " " + tv_area.getText().toString()
                        + " " + tv_tart.getText().toString() + "~" + tv_end.getText().toString());
                mAdapter.getTemList().clear();
                mAdapter.getList().clear();
                list2.clear();
                if (isChart) {
                    mLineChart.invalidate();
                    mLineChart.notifyDataSetChanged();
                    mAdapter.notifyDataSetChanged();
                } else {
                    mAdapter2.getList().clear();
                    mAdapter2.notifyDataSetChanged();
                }
                mDialog.dismiss();
                Toasts.showInfo(context, result.getMsg());
            }
        } catch (Exception e) {
            mLineChart.fitScreen();//重置缩放移动
            mLineChart.setData(null);
            description.setText(tv_terminal.getText().toString() + " " + tv_area.getText().toString()
                    + " " + tv_tart.getText().toString() + "~" + tv_end.getText().toString());
            mAdapter.getTemList().clear();
            mAdapter.getList().clear();
            list2.clear();
            if (isChart) {
                mLineChart.invalidate();
                mLineChart.notifyDataSetChanged();
                mAdapter.notifyDataSetChanged();
            } else {
                mAdapter2.getList().clear();
                mAdapter2.notifyDataSetChanged();
            }
            mDialog.dismiss();
        }
    }

//    private String filtrate(String name) {
//        String str = name;
//        if (!str.contains("(")) {
//            return str;
//        }
//        String units = name.substring(name.lastIndexOf("("), name.length());
//        if (str.contains("空气温度")) {
//            str = "空温" + units;
//        } else if (str.contains("空气湿度")) {
//            str = "空湿" + units;
//        } else if (str.contains("土壤温度")) {
//            str = "土温" + units;
//        } else if (str.contains("土壤湿度")) {
//            str = "土湿" + units;
//        } else if (str.contains("室外光照")) {
//            str = "室外光" + units;
//        } else if (str.contains("光照")) {
//            str = "光照" + units;
//        } else if (str.contains("室外温度")) {
//            str = "室外温" + units;
//        } else if (str.contains("室外湿度")) {
//            str = "室外湿" + units;
//        } else if (str.contains("CO2")) {
//            str = "CO2" + units;
//        } else if (str.contains("EC")) {
//            str = "EC" + units;
//        } else if (str.contains("PH")) {
//            str = "PH" + units;
//        } else if (str.contains("风速")) {
//            str = "风速" + units;
//        } else if (str.contains("雨雪")) {
//            str = "雨雪" + units;
//        } else if (str.contains("溶氧")) {
//            str = "溶氧" + units;
//        } else if (str.contains("水温")) {
//            str = "水温" + units;
//        } else {
//            str = name;
//        }
//        return str;
//    }

    private void addLine(List<Float> data, String label, List<String> mlist2, String str2) {
        List<Entry> entries = new ArrayList<>();
        List<String[]> list = new ArrayList<>();
        //取单位
        String[] str = str2.split("\\(");
        String unit = "";
        if (str.length == 2) {
            unit = str[1].replace(")", "");
        }
        for (int i = 0; i < data.size(); i++) {
            entries.add(new Entry(i, data.get(i)));
            if (data.size() == mlist2.size()) {
                list.add(new String[]{
                        str[0],
                        mlist2.get(i),
                        data.get(i) + unit
                });
            } else {
                list.add(new String[]{
                        str[0],
                        "",
                        data.get(i) + unit
                });
            }
            if (str[0].contains("雪")) {
                if (list.get(i)[2].equals("1.0")) {
                    list.get(i)[2] = getString(R.string.envir_str17);
                } else list.get(i)[2] = getString(R.string.envir_str18);
            } else if (str[0].contains("风向")) {
                try {
                    float wind = Float.parseFloat(list.get(i)[2].replace("°", ""));
                    list.get(i)[2] = winds[Math.round(wind / 45)];
                } catch (Exception e) {

                }
            }
        }
        list2.add(list);
        LineDataSet lineDataSet = new LineDataSet(entries, label);
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

    private void parseGHSelResult() {
        if (isSunlight) {
            initOptions();
            if (pGhList.size() > 0 && cGhList.size() > 0 && cGhList.get(0).size() > 0
                    && locList1.size() > 0 && locList1.get(0).size() > 0) {
                mPosition1 = 0;
                mPosition2 = 0;
                tv_terminal.setText(pGhList.get(mPosition1)[1].replace(getString(R.string.parsetstr10), "") +
                        cGhList.get(mPosition1).get(mPosition2)[1].replace(getString(R.string.parsetstr10), ""));
                if (locList1.get(0).get(0).size() > 0) {
                    mPosition3 = 0;
                    Log.e("dsfds     ", "parseGHSelResult:    "+locList1.get(mPosition1).get(mPosition2).get(mPosition3)[1]);
                    tv_area.setText(locList1.get(mPosition1).get(mPosition2).get(mPosition3)[1]);
                }
            }
        } else {
            if (pGhList.size() > 0) {
                mPosition2 = 0;
                tv_terminal.setText(pGhList.get(mPosition2)[1]);
                if (locList.size() > 0 && locList.get(mPosition2).size() > 0) {
                    mPosition3 = 0;
                    tv_area.setText(locList.get(mPosition2).get(mPosition3)[1]);
                    requestHisAna();
                }
            } else {
                mDialog.dismiss();
            }
        }
    }

    private void initData() {
        if (isSunlight) {
            if (pGhList == null || cGhList == null || locList1 == null) {
                mDialog.show();
                getTerminal();
            } else {
                initOptions();
                if (pGhList.size() > 0 && cGhList.size() > 0 && cGhList.get(0).size() > 0
                        && locList1.size() > 0 && locList1.get(0).size() > 0) {
                    mPosition1 = 0;
                    mPosition2 = 0;
                    tv_terminal.setText(pGhList.get(mPosition1)[1].replace(getString(R.string.parsetstr10), "") +
                            cGhList.get(mPosition1).get(mPosition2)[1].replace(getString(R.string.parsetstr10), ""));
                    if (locList1.get(0).get(0).size() > 0) {
                        mPosition3 = 0;
                        tv_area.setText(locList1.get(mPosition1).get(mPosition2).get(mPosition3)[1]);
                        requestHisAna();
                    }
                }
            }
        } else {
            if (pGhList == null || locList == null) {
                mDialog.show();
                getTerminal();
            } else {
                if (pGhList.size() > 0) {
                    mPosition2 = 0;
                    tv_terminal.setText(pGhList.get(mPosition2)[1]);
                    if (locList.size() > 0 && locList.get(0).size() > 0) {
                        mPosition3 = 0;
                        tv_area.setText(locList.get(mPosition2).get(mPosition3)[1]);
                        requestHisAna();
                    }
                }
            }
        }
    }

    private void getTerminal() {
        if (Data.userResult != null && Data.GhType != null) {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"Type", Data.GhType});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("GreenHouseSel",
                    "/StringService/DataInfo.asmx", data, 666, context);
        } else {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    private void requestHisAna() {
        if (Data.userResult != null) {
            String Location = "", GreenhouseID = "";
            if (isSunlight) {
                GreenhouseID = cGhList.get(mPosition1).get(mPosition2)[0];
                Location = locList1.get(mPosition1).get(mPosition2).get(mPosition3)[0];
            } else {
                GreenhouseID = pGhList.get(mPosition2)[0];
                Location = locList.get(mPosition2).get(mPosition3)[0];
            }
            mDialog.show();
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"EnterpriseID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"GreenhouseID", GreenhouseID});
            data.add(new String[]{"Location", Location});
            data.add(new String[]{"startTime", tv_tart.getText().toString() + ":00:00"});
            data.add(new String[]{"endTime", tv_end.getText().toString() + ":00:00"});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("HistoryAnalysis",
                    "/StringService/DataInfo.asmx", data, 102, context);
        }
    }

    private void initOptions() {
        List<String> gateway = new ArrayList<>();
        List<List<String>> terminal = new ArrayList<>();
        for (int i = 0; i < pGhList.size(); i++) {
            gateway.add(pGhList.get(i)[1]);
            List<String> list = new ArrayList<>();
            if (i < cGhList.size() && cGhList.get(i).size() > 0) {
                for (int i1 = 0; i1 < cGhList.get(i).size(); i1++) {
                    list.add(cGhList.get(i).get(i1)[1]);
                }
            } else {
                list.add("");
            }
            terminal.add(list);
        }
        mOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tv_area.setText("");
                mPosition1 = options1;
                mPosition2 = options2;
                mPosition3 = -1;
                tv_terminal.setText(pGhList.get(mPosition1)[1].replace(getString(R.string.parsetstr10), "") +
                        cGhList.get(mPosition1).get(mPosition2)[1].replace(getString(R.string.parsetstr10), ""));
            }
        }).build();
        mOptions.setPicker(gateway, terminal);
    }

    private void showBottomSheet2() {
        if (pGhList.size() > 0) {
            if (mBottomSheet2 == null) {
                BottomListSheetBuilder mBuilder2 = new BottomListSheetBuilder(context, true)
                        .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                mPosition2 = position;
                                tv_terminal.setText(pGhList.get(mPosition2)[1]);
                                mPosition3 = -1;
                                tv_area.setText("");
                                dialog.dismiss();
                            }
                        });
                for (int i = 0; i < pGhList.size(); i++) {
                    mBuilder2.addItem(pGhList.get(i)[1]);
                }
                mBuilder2.setCheckedIndex(mPosition2);
                mBottomSheet2 = mBuilder2.build();
            }
            mBottomSheet2.show();
        } else Toasts.showInfo(context, getString(R.string.request_error8));
    }

    private void showBottomSheet3() {
        if (isSunlight) {
            if (locList1 != null && locList1.size() > mPosition1 && locList1.get(mPosition1).size() > mPosition2
                    && locList1.get(mPosition1).get(mPosition2).size() > 0) {
                BottomListSheetBuilder mBuilder3 = new BottomListSheetBuilder(context, true)
                        .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                mPosition3 = position;
                                tv_area.setText(locList1.get(mPosition1).get(mPosition2).get(mPosition3)[1]);
                                dialog.dismiss();
                            }
                        });
                for (int i = 0; i < locList1.get(mPosition1).get(mPosition2).size(); i++) {
                    mBuilder3.addItem(locList1.get(mPosition1).get(mPosition2).get(i)[1]);
                }
                mBuilder3.setCheckedIndex(mPosition3);
                mBuilder3.build().show();
            } else Toasts.showInfo(context, getString(R.string.request_error8));
        } else {
            if (locList != null && locList.size() > mPosition2 && locList.get(mPosition2).size() > 0) {
                BottomListSheetBuilder mBuilder3 = new BottomListSheetBuilder(context, true)
                        .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                mPosition3 = position;
                                tv_area.setText(locList.get(mPosition2).get(mPosition3)[1]);
                                dialog.dismiss();
                            }
                        });
                for (int i = 0; i < locList.get(mPosition2).size(); i++) {
                    mBuilder3.addItem(locList.get(mPosition2).get(i)[1]);
                }
                mBuilder3.setCheckedIndex(mPosition3);
                mBuilder3.build().show();
            } else Toasts.showInfo(context, getString(R.string.request_error8));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fhe2:
                if (isSunlight) {
                    if (mOptions != null) {
                        mOptions.show();
                    } else {
                        mDialog.show();
                        getTerminal();
                    }
                } else {
                    if (pGhList != null) {
                        showBottomSheet2();
                    } else initData();
                }
                break;
            case R.id.ll_fhe3:
                if (tv_terminal.getText().length() > 0) {
                    showBottomSheet3();
                } else Toasts.showInfo(context, getString(R.string.envir_fra_htv2));
                break;
            case R.id.btn_fhe_query:
                if (tv_area.getText().length() > 0 && tv_terminal.getText().length() > 0 &&
                        tv_tart.getText().length() > 0 && tv_end.getText().length() > 0) {
                    if (endDate.getTime() - startDate.getTime() < 3 * 24 * 60 * 60 * 1000 + 10000) {
                        requestHisAna();
                    } else Toasts.showInfo(context, getString(R.string.envir_str7));
                } else Toasts.showInfo(context, getString(R.string.request_error6));
                break;
            case R.id.btn_fhe_custom:
                if (isChart) {
                    showMultiChoiceDialog();
                } else showMultiChoiceDialog1();
                break;
            case R.id.ll_fhe4:
                isStart = true;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                pvTime.setDate(calendar);
                pvTime.show();
                break;
            case R.id.ll_fhe5:
                isStart = false;
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(endDate);
                pvTime.setDate(calendar1);
                pvTime.show();
                break;
            case R.id.btn_fhe_qh:
                if (isChart) {
                    isChart = false;
                    btn_qh.setText(getString(R.string.envir_fra_htv9));
                    ll.setVisibility(View.GONE);
                    mLineChart.setVisibility(View.GONE);
                    mRecyclerView1.setVisibility(View.VISIBLE);
                    if (list2.size() > 0) {
                        if (strLegend != null && strLegend.length == list2.size()) {
                            mAdapter2.getList().clear();
                            mAdapter2.getList().addAll(list2.get(checkedIndex));
                            mAdapter2.notifyDataSetChanged();
                        }
                    } else if (mAdapter2.getList().size() > 0) {
                        mAdapter2.getList().clear();
                        mAdapter2.notifyDataSetChanged();
                    }
                } else {
                    isChart = true;
                    btn_qh.setText(getString(R.string.envir_fra_htv8));
                    ll.setVisibility(View.VISIBLE);
                    mLineChart.setVisibility(View.VISIBLE);
                    mRecyclerView1.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            default:

                break;
        }
    }

    private void showMultiChoiceDialog1() {
        if (list2.size() > 0 && strLegend != null && strLegend.length == list2.size()) {
            CheckableDialogBuilder checkableBuilder = new CheckableDialogBuilder(context)
                    .setCheckedIndex(checkedIndex)
                    .addItems(strLegend, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkedIndex = which;
                            mAdapter2.getList().clear();
                            mAdapter2.getList().addAll(list2.get(checkedIndex));
                            dialog.dismiss();
                            mAdapter2.notifyDataSetChanged();
                        }
                    });
            checkableBuilder.setCheckedIndex(checkedIndex);
            checkableBuilder.create().show();
        }
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
        mAdapter.getList().clear();
        List<Float> mins = new ArrayList<>();
        List<Float> maxs = new ArrayList<>();
        for (int i = 0; i < CheckedItem.length; i++) {
            lineList.add(temLineList.get(CheckedItem[i]));
            mAdapter.getList().add(mAdapter.getTemList().get(CheckedItem[i]));
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
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isVisible)
            eRegister();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void eRegister() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }
}
