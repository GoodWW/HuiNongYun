package com.cdzp.huinongyun.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdzp.huinongyun.MyApplication;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.adapter.RealEnvirAdapter;
import com.cdzp.huinongyun.bean.SoilLocSelResult;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 土壤墒情
 */
public class SoilMoistureFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private View view;
    private boolean isVisible, isInit, isRequest, isToast = true;
    private QMUITipDialog mDialog;
    private List<String[]> list;
    private QMUIBottomSheet mBottomSheet;
    private int mPosition = -1;
    private TextView tv_area;
    private boolean isEnglish;
    private RecyclerView mRecyclerView;
    private RealEnvirAdapter mAdapter;
    private TimerTask task;
    private Timer timer;
    private CountDownTimer countDownTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_soil_moisture, container, false);
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
                register();
                if (!isInit) {
                    isInit = true;
                    initData();
                } else {
                    WebServiceManage.dispose();
                    isRequest = true;
                }
            }
        } else {
            if (countDownTimer != null) countDownTimer.cancel();
            isRequest = false;
            WebServiceManage.dispose();
            EventBus.getDefault().unregister(this);
        }
    }


    private void initView() {
        context = getContext();
        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        isEnglish = ((MyApplication) getActivity().getApplication()).isEnglish();

        tv_area = view.findViewById(R.id.tv_fsom_area);
        mRecyclerView = view.findViewById(R.id.tv_fsom_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(context.getResources().getColor(R.color.decoration), 1, 1));
        mAdapter = new RealEnvirAdapter(context);
        mRecyclerView.setAdapter(mAdapter);

        task = new TimerTask() {
            @Override
            public void run() {
                if (isRequest) {
                    isRequest = false;
                    requestSenAreaSel();
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

        timer.schedule(task, 0, 5000);

        view.findViewById(R.id.ll_fsom1).setOnClickListener(this);
        if (!isInit && isVisible) {
            isInit = true;
            initData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (isVisible) {
            if (message.isOk()) {
                switch (message.getLabel()) {
                    case 1:
                        parseSoilLocationSel(message.getmResult());
                        break;
                    case 2:
                        parseSoilSensorSel(message.getmResult());
                        break;
                    default:
                        mDialog.dismiss();
                        break;
                }
            } else {
                mDialog.dismiss();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fsom1:
                showBottomSheet();
                break;
            default:

                break;
        }
    }

    private void showBottomSheet() {
        if (list != null) {
            if (list.size() > 0) {
                if (mBottomSheet == null) {
                    BottomListSheetBuilder mBuilder2 = new BottomListSheetBuilder(context, true)
                            .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                                @Override
                                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                    isRequest = false;
                                    WebServiceManage.dispose();
                                    countDownTimer.cancel();
                                    isRequest = false;
                                    countDownTimer.start();
                                    mPosition = position;
                                    tv_area.setText(list.get(mPosition)[1]);
                                    dialog.dismiss();
                                    mDialog.show();
                                    mAdapter.getList().clear();
                                    mAdapter.notifyDataSetChanged();
                                    requestSenAreaSel();
                                }
                            });
                    for (int i = 0; i < list.size(); i++) {
                        mBuilder2.addItem(list.get(i)[1]);
                    }
                    mBuilder2.setCheckedIndex(mPosition);
                    mBottomSheet = mBuilder2.build();
                }
                mBottomSheet.show();
            } else Toasts.showInfo(context, getString(R.string.request_error8));
        } else initData();
    }

    private void parseSoilSensorSel(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            boolean success = jsonObject.getBoolean("success");
            if (success) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (jsonArray.length() > 0) {
                    mAdapter.getList().clear();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String[] colNames = jsonObject.getString("colName").split("\\|");
                    String[] colName1 = colNames[0].split(",");
                    if (colNames.length > 1 && colNames[1].substring(colNames[1].length() - 1, colNames[1].length()).equals(",")) {
                        colNames[1] = colNames[1] + " ";
                    }
                    String[] colName2 = colNames[1].split(",");
                    String colEnglishName = jsonObject.getString("colEnglishName");
                    if (colEnglishName.substring(colEnglishName.length() - 1, colEnglishName.length()).equals(",")) {
                        colEnglishName = colEnglishName + " ";
                    }
                    String[] colEnglishNames = colEnglishName.split(",");
                    for (int i = 1; i < colName1.length; i++) {
                        String[] str = new String[3];
                        if (isEnglish) {
                            str[0] = colEnglishNames[i - 1];
                        } else {
                            str[0] = colName1[i].replace("[", "").replace("]", "");
                        }
                        str[1] = jsonObject1.getString(colName1[i]) + colName2[i - 1].
                                replace("(", "").replace(")", "");
                        str[2] = colName1[i].replace("[", "").replace("]", "")
                                .replace(".", "").replace(" ", "")
                                .replace(",", "").replace(":", "");
                        str[2] = switchImage(str[2]);
                        mAdapter.getList().add(str);
                    }
                    isToast = true;
                } else {
                    showToast(getString(R.string.request_error8));
                }
            } else {
                showToast(getString(R.string.request_error8));
            }
        } catch (Exception e) {
            showToast(getString(R.string.request_error11));
        }
        mAdapter.notifyDataSetChanged();
        isRequest = true;
        mDialog.dismiss();
    }

    private String switchImage(String str) {
        if (str != null) {
            if (str.contains("室外温度")) {
                str = "6";
            } else if (str.contains("室外湿度")) {
                str = "7";
            } else if (str.contains("室外光照")) {
                str = "8";
            } else if (str.contains("土壤温度")) {
                str = "3";
            } else if (str.contains("土壤湿度")) {
                str = "4";
            } else if (str.contains("土温")) {
                str = "3";
            } else if (str.contains("土湿")) {
                str = "4";
            } else if (str.contains("CO2")) {
                str = "9";
            } else if (str.contains("EC")) {
                str = "10";
            } else if (str.contains("PH")) {
                str = "11";
            } else if (str.contains("溶氧")) {
                str = "12";
            } else if (str.contains("空温")) {
                str = "1";
            } else if (str.contains("温度")) {
                str = "1";
            } else if (str.contains("湿度")) {
                str = "2";
            } else if (str.contains("光照强度")) {
                str = "5";
            } else if (str.contains("照度")) {
                str = "5";
            } else if (str.contains("光")) {
                str = "5";
            } else if (str.contains("风")) {
                str = "13";
            } else if (str.contains("雨")) {
                str = "14";
            } else if (str.contains("雪")) {
                str = "14";
            } else if (str.contains("水温")) {
                str = "15";
            } else {
                str = "0";
            }
        } else {
            str = "0";
        }
        return str;
    }

    private void showToast(String s) {
        if (isToast) {
            isToast = false;
            Toasts.showInfo(context, s);
        }
    }

    private void parseSoilLocationSel(String s) {
        try {
            list = new ArrayList<>();
            SoilLocSelResult result = new Gson().fromJson(s, SoilLocSelResult.class);
            if (result.isSuccess() && result.getData().size() > 0 && result.getData().get(0).size() > 0) {
                for (int i = 0; i < result.getData().get(0).size(); i++) {
                    String BaseName;
                    if (isEnglish) {
                        BaseName = result.getData().get(0).get(i).getBaseValue() + getString(R.string.dev_str1);
                    } else {
                        BaseName = result.getData().get(0).get(i).getBaseName();
                    }
                    list.add(new String[]{
                            result.getData().get(0).get(i).getBaseValue(),
                            BaseName
                    });
                }
                mPosition = 0;
                tv_area.setText(list.get(mPosition)[1]);
                requestSenAreaSel();
                countDownTimer.start();
            } else {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.request_error8));
            }
        } catch (Exception e) {
            mDialog.dismiss();
        }
    }

    /**
     * 请求实时数据
     */
    private void requestSenAreaSel() {
        if (tv_area.getText().length() > 0) {
            if (Data.userResult != null) {
                List<String[]> data = new ArrayList<>();
                data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
                data.add(new String[]{"GhID", "1"});
                data.add(new String[]{"Location", list.get(mPosition)[0]});
                data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
                data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
                WebServiceManage.requestData("SoilSensorSel",
                        "/StringService/DataInfo.asmx", data, 2, context);
            } else {
                mDialog.dismiss();
            }
        } else {
            mDialog.dismiss();
        }
    }

    private void initData() {
        if (Data.userResult != null) {
            mDialog.show();
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("SoilLocationSel",
                    "/StringService/DataInfo.asmx", data, 1, context);
        } else {
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible)
            register();
    }

    @Override
    public void onPause() {
        super.onPause();
        countDownTimer.cancel();
        isRequest = false;
        WebServiceManage.dispose();
        EventBus.getDefault().unregister(this);
    }

    private void register() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (task != null) task.cancel();
        if (timer != null) timer.cancel();
    }
}
