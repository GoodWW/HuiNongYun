package com.cdzp.huinongyun.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.cdzp.huinongyun.MyApplication;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.adapter.RealEnvirAdapter;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder;
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
 * 实时环境数据
 */
public class RealEnvirFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean isVisible, isInit = false, isToast = true;
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
    private OptionsPickerView mOptions;
    private boolean isSunlight = false;//是否是日光,电磁阀
    private boolean isEnglish;//是否中英文
    private String[] winds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_real_envir, container, false);
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
                eRegister();
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
//        if (context.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
//                .getString("Language", "").equals("English")) {
//            isEnglish = true;
//        } else {
//            isEnglish = false;
//        }
        isEnglish = ((MyApplication) (getActivity().getApplication())).isEnglish();

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

        tv_terminal = view.findViewById(R.id.tv_fre_terminal);
        tv_area = view.findViewById(R.id.tv_fre_area);
        mRecyclerView = view.findViewById(R.id.recycler_fre);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(context.getResources().getColor(R.color.decoration), 1, 1));
        mAdapter = new RealEnvirAdapter(context);
        mRecyclerView.setAdapter(mAdapter);

        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        winds = new String[]{getString(R.string.envir_str8),
                getString(R.string.envir_str9),
                getString(R.string.envir_str10),
                getString(R.string.envir_str11),
                getString(R.string.envir_str12),
                getString(R.string.envir_str13),
                getString(R.string.envir_str14),
                getString(R.string.envir_str15),
                getString(R.string.envir_str16)};

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

        view.findViewById(R.id.ll_fre2).setOnClickListener(this);
        view.findViewById(R.id.ll_fre3).setOnClickListener(this);

        timer.schedule(task, 0, 5000);

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
                        if (str[2].contains("雪")) {
                            if (data.getString(colName1[i]).equals("1.00")) {
                                str[1] = getString(R.string.envir_str17);
                            } else str[1] = getString(R.string.envir_str18);
                        } else if (str[2].contains("风向")) {
                            float wind = Float.parseFloat(data.getString(colName1[i]));
                            str[1] = winds[Math.round(wind / 45)];
                        }
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
            }else if (str.contains("水位")) {
                str = "17";
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
        if (isSunlight) {
            initOptions();
            if (pGhList.size() > 0 && cGhList.size() > 0 && cGhList.get(0).size() > 0) {
                mPosition1 = 0;
                mPosition2 = 0;
                tv_terminal.setText(pGhList.get(mPosition1)[1].replace(getString(R.string.parsetstr10), "") +
                        cGhList.get(mPosition1).get(mPosition2)[1].replace(getString(R.string.parsetstr10), ""));
                if (locList1.size() > 0 && locList1.get(0).size() > 0 && locList1.get(0).get(0).size() > 0) {
                    mPosition3 = 0;
                    tv_area.setText(locList1.get(mPosition1).get(mPosition2).get(mPosition3)[1]);
                    atOnceRequest();
                } else mDialog.dismiss();
            } else mDialog.dismiss();
        } else {
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
            String Location = "", GreenhouseID = "";
            if (isSunlight) {
                GreenhouseID = cGhList.get(mPosition1).get(mPosition2)[0];
                Location = locList1.get(mPosition1).get(mPosition2).get(mPosition3)[0];
            } else {
                GreenhouseID = pGhList.get(mPosition2)[0];
                Location = locList.get(mPosition2).get(mPosition3)[0];
            }
            if (Data.userResult != null) {
                List<String[]> data = new ArrayList<>();
                data.add(new String[]{"EnterpriseID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
                data.add(new String[]{"GreenhouseID", GreenhouseID});
                data.add(new String[]{"Location", Location});
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
        if (isSunlight) {
            if (pGhList == null || cGhList == null || locList1 == null) {
                mDialog.show();
                getTerminal();
            } else {
                initOptions();
                if (pGhList.size() > 0 && cGhList.size() > 0 && cGhList.get(0).size() > 0) {
                    mPosition1 = 0;
                    mPosition2 = 0;
                    tv_terminal.setText(pGhList.get(mPosition1)[1].replace(getString(R.string.parsetstr10), "")
                            + cGhList.get(mPosition1).get(mPosition2)[1].replace(getString(R.string.parsetstr10), ""));
                    if (locList1.size() > 0 && locList1.get(0).size() > 0 && locList1.get(0).get(0).size() > 0) {
                        mPosition3 = 0;
                        tv_area.setText(locList1.get(mPosition1).get(mPosition2).get(mPosition3)[1]);
                        mDialog.show();
                        atOnceRequest();
                    }
                }
            }
        } else {
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
                countDownTimer.cancel();
                isRequest = false;
                WebServiceManage.dispose();
                mAdapter.getList().clear();
                mAdapter.notifyDataSetChanged();

                mPosition1 = options1;
                mPosition2 = options2;

                tv_terminal.setText(pGhList.get(mPosition1)[1].replace(getString(R.string.parsetstr10), "")
                        + cGhList.get(mPosition1).get(mPosition2)[1].replace(getString(R.string.parsetstr10), ""));

                if (locList1.size() > mPosition1 && locList1.get(mPosition1).size() > mPosition2 &&
                        locList1.get(mPosition1).get(mPosition2).size() > 0) {
                    mPosition3 = 0;
                    tv_area.setText(locList1.get(mPosition1).get(mPosition2).get(mPosition3)[1]);
                    mDialog.show();
                    requestSenAreaSel();
                    countDownTimer.start();
                } else mPosition3 = -1;
            }
        }).build();
        mOptions.setPicker(gateway, terminal);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fre2:
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
                    } else getTerminal();
                }
                break;
            case R.id.ll_fre3:
                if (tv_terminal.getText().length() > 0) {
                    showBottomSheet3();
                } else Toasts.showInfo(context, getString(R.string.envir_fra_htv2));
                break;
            default:

                break;
        }
    }

    private void showBottomSheet3() {
        if (isSunlight) {
            if (locList1 != null && locList1.size() > mPosition1 && locList1.get(mPosition1).size() > mPosition2
                    && locList1.get(mPosition1).get(mPosition2).size() > 0) {
                BottomListSheetBuilder mBuilder3 = new BottomListSheetBuilder(context, true)
                        .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                countDownTimer.cancel();
                                isRequest = false;
                                WebServiceManage.dispose();
                                mAdapter.getList().clear();
                                mAdapter.notifyDataSetChanged();
                                mPosition3 = position;
                                tv_area.setText(locList1.get(mPosition1).get(mPosition2).get(mPosition3)[1]);
                                mDialog.show();
                                requestSenAreaSel();
                                countDownTimer.start();
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
    }

    private void showBottomSheet2() {
        if (pGhList.size() > 0) {
            if (mBottomSheet2 == null) {
                BottomListSheetBuilder mBuilder2 = new BottomListSheetBuilder(context, true)
                        .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
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
        if (!isInit && isVisible) {
            isInit = true;
            eRegister();
            initData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        isRequest = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (task != null) task.cancel();
        if (timer != null) timer.cancel();
    }
}
