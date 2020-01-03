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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.cdzp.huinongyun.BaseActivity;
import com.cdzp.huinongyun.MyApplication;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.activity.OnlineFert;
import com.cdzp.huinongyun.adapter.RealEnvirAdapter;
import com.cdzp.huinongyun.bean.SocketResult;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.cdzp.huinongyun.util.Data.cGhList;
import static com.cdzp.huinongyun.util.Data.locList;
import static com.cdzp.huinongyun.util.Data.locList1;
import static com.cdzp.huinongyun.util.Data.pGhList;

/**
 * 在线施肥机实时数据
 */
public class OnlReaTimeData extends BaseActivity {

    private Context context;
    private boolean isToast = true;
    private TextView tv_terminal, tv_area;
    private QMUITipDialog mDialog;
    private int mPosition1, mPosition2, mPosition3 = -1;
    private QMUIBottomSheet mBottomSheet2;
    private boolean isRequest;
    private TimerTask task;
    private Timer timer;
    private CountDownTimer countDownTimer;
    private RecyclerView mRecyclerView;
    private RealEnvirAdapter mAdapter;
    private boolean isEnglish;//是否中英文

    @Override
    public void setContentLayout() {
        setContentView(R.layout.fragment_onl_rea_time_data);
    }

    @Override
    public void dealLogicBeforeInitView() {

    }

    @Override
    public void initView() {
        context = this;
        isEnglish = ((MyApplication) (getApplication())).isEnglish();

        TextView tv = findViewById(R.id.tv_title);
        tv.setText("环境数据");
        RelativeLayout rl = findViewById(R.id.rl_right);
        rl.setVisibility(View.INVISIBLE);

        tv_terminal = findViewById(R.id.tv_fortd_terminal);
        tv_area = findViewById(R.id.tv_fortd_area);
        mRecyclerView = findViewById(R.id.recycler_fortd);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(context.getResources().getColor(R.color.decoration), 1, 1));
        mAdapter = new RealEnvirAdapter(context);
        mRecyclerView.setAdapter(mAdapter);

        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();

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

        findViewById(R.id.ll_fortd1).setOnClickListener(this);
        findViewById(R.id.ll_fortd2).setOnClickListener(this);
        findViewById(R.id.ll_back).setOnClickListener(this);

        timer.schedule(task, 0, 5000);

    }

    @Override
    public void dealLogicAfterInitView() {

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.ll_fortd1:
                if (pGhList != null) {
                    showBottomSheet2();
                } else getTerminal();
                break;
            case R.id.ll_fortd2:
                if (tv_terminal.getText().length() > 0) {
                    showBottomSheet3();
                } else Toasts.showInfo(context, getString(R.string.envir_fra_htv2));
                break;
            case R.id.ll_back:
                finish();
                break;
            default:

                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 666:
                    parseGHSelResult();
                    break;
                case 2:
                    parseSASResult(message.getmResult());
                    break;
                default:
                    mDialog.dismiss();
                    break;
            }
        } else {
            mDialog.dismiss();
            if (message.getLabel() == 2) {
                isRequest = true;
                showToast(message.getmResult());
            } else Toasts.showError(context, message.getmResult());
        }
    }

    private void parseSASResult(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            boolean success = jsonObject.getBoolean("success");
            if (success) {
                String[] colNames = jsonObject.getString("colName").split("\\|");
                if (colNames.length > 1 && colNames[1].substring(colNames[1].length() - 1, colNames[1].length()).equals(",")) {
                    colNames[1] = colNames[1] + " ";
                }
                String[] colName1 = colNames[0].split(",");
                if (colName1.length > 1) {
                    mAdapter.getList().clear();
                    String[] colName2 = colNames[1].split(",");
                    String colEngStr = jsonObject.getString("colEnglishName");
                    if (colEngStr.substring(colEngStr.length() - 1, colEngStr.length()).equals(",")) {
                        colEngStr = colEngStr + " ";
                    }
                    String[] colEngName = colEngStr.split(",");
                    JSONObject data = jsonObject.getJSONArray("data").getJSONObject(0);
                    for (int i = 1; i < colName1.length; i++) {
                        String[] str = new String[3];
                        if (isEnglish) {
                            str[0] = colEngName[i - 1];
                        } else {
                            str[0] = colName1[i].replace("[", "").replace("]", "")
                                    .replace(".", "").replace(" ", "")
                                    .replace("ε", "").replace(":", "")
                                    .replace("!", "").replace("-", "");
                        }//.,:, ,[,],ε,!,-
                        str[1] = data.getString(colName1[i]) + colName2[i - 1].
                                replace("(", "").replace(")", "");
                        str[2] = colName1[i].replace("[", "").replace("]", "")
                                .replace(".", "").replace(" ", "")
                                .replace("ε", "").replace(":", "")
                                .replace("!", "").replace("-", "");
                        str[2] = switchImage(str[2]);
                        mAdapter.getList().add(str);
                    }
                    isToast = true;
                }
            } else {
                showToast(getString(R.string.request_error8));
            }
        } catch (Exception e) {
            mAdapter.getList().clear();
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
            } else if (str.contains("PM")) {
                str = "16";
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

    private void parseGHSelResult() {
        if (pGhList.size() > 0 && locList.size() > 0) {
            mPosition2 = 0;
            tv_terminal.setText(pGhList.get(mPosition2)[1]);
            if (locList.get(0).size() > 0) {
                mPosition3 = 0;
                tv_area.setText(locList.get(mPosition2).get(mPosition3)[1]);
                atOnceRequest();
            } else mDialog.dismiss();
        } else {
            mDialog.dismiss();
        }
    }

    /**
     * 立即请求一次数据
     */
    private void atOnceRequest() {
        countDownTimer.cancel();
        isRequest = false;
        WebServiceManage.dispose();
        requestSenAreaSel();
        countDownTimer.start();
    }

    /**
     * 请求实时数据
     */
    private void requestSenAreaSel() {
        if (tv_terminal.getText().length() > 0 && tv_area.getText().length() > 0) {
            if (Data.userResult != null) {
                List<String[]> data = new ArrayList<>();
                data.add(new String[]{"EnterpriseID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
                data.add(new String[]{"GreenhouseID", pGhList.get(mPosition2)[0]});
                data.add(new String[]{"Location", locList.get(mPosition2).get(mPosition3)[0]});
                data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
                data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
                WebServiceManage.requestData("SensorAreaSel",
                        "/StringService/DataInfo.asmx", data, 2, context);
            } else {
                mDialog.dismiss();
            }
        } else {
            mDialog.dismiss();
        }
    }

    private void initData() {
        if (pGhList == null || locList == null) {
            mDialog.show();
            getTerminal();
        } else {
            if (pGhList.size() > 0 && locList.size() > 0) {
                mPosition2 = 0;
                tv_terminal.setText(pGhList.get(mPosition2)[1]);
                if (locList.get(mPosition2).size() > 0) {
                    mPosition3 = 0;
                    tv_area.setText(locList.get(mPosition2).get(mPosition3)[1]);
                    mDialog.show();
                    atOnceRequest();
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

    private void showBottomSheet3() {
        if (locList != null && locList.size() > mPosition2 && locList.get(mPosition2).size() > 0) {
            QMUIBottomSheet.BottomListSheetBuilder mBuilder3 = new QMUIBottomSheet.BottomListSheetBuilder(context, true)
                    .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                        @Override
                        public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                            isRequest = false;
                            WebServiceManage.dispose();
                            countDownTimer.cancel();
                            mAdapter.getList().clear();
                            mAdapter.notifyDataSetChanged();
                            mPosition3 = position;
                            tv_area.setText(locList.get(mPosition2).get(mPosition3)[1]);
                            mDialog.show();
                            requestSenAreaSel();
                            countDownTimer.start();
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

    private void showBottomSheet2() {
        if (pGhList.size() > 0) {
            if (mBottomSheet2 == null) {
                QMUIBottomSheet.BottomListSheetBuilder mBuilder2 = new QMUIBottomSheet.BottomListSheetBuilder(context, true)
                        .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                countDownTimer.cancel();
                                isRequest = false;
                                WebServiceManage.dispose();
                                mAdapter.getList().clear();
                                mAdapter.notifyDataSetChanged();
                                mPosition2 = position;
                                tv_terminal.setText(pGhList.get(mPosition2)[1]);
                                tv_area.setText("");

                                if (locList.size() > mPosition2 && locList.get(mPosition2).size() > 0) {
                                    mPosition3 = 0;
                                    tv_area.setText(locList.get(mPosition2).get(mPosition3)[1]);
                                    mDialog.show();
                                    requestSenAreaSel();
                                    countDownTimer.start();
                                } else
                                    mPosition3 = -1;
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

    private void eRegister() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            if (tv_area != null && tv_area.getText().length() > 0 && tv_terminal != null && tv_terminal.getText().length() > 0) {
                isRequest = true;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        eRegister();
        initData();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        if (countDownTimer != null) countDownTimer.cancel();
        isRequest = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
        if (task != null) task.cancel();
        if (timer != null) timer.cancel();
    }

}
