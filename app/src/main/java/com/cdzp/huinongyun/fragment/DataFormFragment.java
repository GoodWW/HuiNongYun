package com.cdzp.huinongyun.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.activity.StatisticalFormActivity;
import com.cdzp.huinongyun.adapter.DataFormAdapter;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cdzp.huinongyun.util.Data.cGhList;
import static com.cdzp.huinongyun.util.Data.locList;
import static com.cdzp.huinongyun.util.Data.locList1;
import static com.cdzp.huinongyun.util.Data.pGhList;

/**
 * 数据报表
 */
public class DataFormFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean isVisible, isRequest = false;
    private QMUITipDialog mDialog;
    private TextView tv_terminal, tv_qy, tv_time;
    private TimePickerView pvTime;
    private Date mDate;
    private int mPosition0, mPosition1, mPosition2 = -1;
    private QMUIBottomSheet mBottomSheet2;
    private RecyclerView mRecyclerView;
    private DataFormAdapter mAdapter;
    private OptionsPickerView mOptions;
    private boolean isSunlight = false;//是否是日光,电磁阀
    private boolean isEnglish;//是否中英文


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_data_form, container, false);
            init();
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
                if (!isRequest) {
                    isRequest = true;
                    initData();
                }
            }
        } else {
            unRegister();
        }
    }

    private void init() {
        context = getContext();
        isEnglish = ((StatisticalFormActivity) getActivity()).isEnglish();
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

        tv_terminal = view.findViewById(R.id.tv_fdf_terminal);
        tv_qy = view.findViewById(R.id.tv_fdf_qy);
        tv_time = view.findViewById(R.id.tv_fdf_time);

        pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mDate = date;
                tv_time.setText(getTime(mDate));
                mAdapter.getList().clear();
                mAdapter.notifyDataSetChanged();
                requestData();
            }
        })
                .isCyclic(true)
                .setType(new boolean[]{true, true, false, false, false, false})
                .build();

        mDate = new Date();
        tv_time.setText(getTime(mDate));

        mRecyclerView = view.findViewById(R.id.mRecyclerView_fdf);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(context.getResources().getColor(R.color.decoration), 1, 1));
        mAdapter = new DataFormAdapter(isEnglish);
        mRecyclerView.setAdapter(mAdapter);


        view.findViewById(R.id.ll_fdf1).setOnClickListener(this);
        view.findViewById(R.id.ll_fdf2).setOnClickListener(this);
        view.findViewById(R.id.ll_fdf3).setOnClickListener(this);

        if (isVisible && !isRequest) {
            isRequest = true;
            register();
            initData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 666:
                    parseGHSelResult();
                    break;
                case 1:
                    parseRptResult(message.getmResult());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fdf1:
                if (isSunlight) {
                    if (mOptions != null) {
                        mOptions.show();
                    } else {
                        getTerminal();
                    }
                } else {
                    if (pGhList != null) {
                        showBottomSheet2();
                    } else getTerminal();
                }
                break;
            case R.id.ll_fdf2:
                if (tv_terminal.getText().length() > 0) {
                    showBottomSheet3();
                } else Toasts.showInfo(context, getString(R.string.envir_fra_htv2));
                break;
            case R.id.ll_fdf3:
                pvTime.show();
                break;
            default:

                break;
        }
    }

    /**
     * 解析数据
     *
     * @param s
     */
    private void parseRptResult(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            boolean success = jsonObject.getBoolean("success");
            if (success) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (jsonArray.length() > 0) {
                    JSONArray jsonArray1 = jsonArray.getJSONArray(0);
                    if (jsonArray1 != null && jsonArray1.length() > 0) {
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                        if (jsonObject1 != null) {
                            String[] colNames = jsonObject.getString("colName").split("\\|");
                            if (colNames.length > 1) {
                                String[] colName = jsonObject.getString("colName").split("\\|")[0].split(",");
                                String units = jsonObject.getString("colName").split("\\|")[1];
                                if (units.substring(units.length() - 1, units.length()).equals(",")) {
                                    units = units + " ";
                                }
                                String[] colName1 = units.split(",");
                                if (colName.length > 2 && colName1.length == (colName.length - 2)) {
                                    for (int i = 2; i < colName.length; i++) {
                                        String[] str = jsonObject1.getString(colName[i]).split(",");
                                        String mColName = colName[i];
                                        if (isEnglish) {
                                            String[] colEnglishName = jsonObject.getString("colEnglishName").split(",");
                                            if (colName.length - 2 == colEnglishName.length) {
                                                mColName = colEnglishName[i - 2] + colName1[i - 2];
                                            } else if (colName.length - 2 == colEnglishName.length - 1) {
                                                if (i - 2 == colEnglishName.length) {
                                                    mColName = "";
                                                } else {
                                                    mColName = colEnglishName[i - 2] + colName1[i - 2];
                                                }
                                            }
                                        } else {
                                            mColName = mColName.replace("[", "")
                                                    .replace("]", "")
                                                    .replace(" ", "")
                                                    .replace(",", "")
                                                    .replace(".", "") + colName1[i - 2];
                                        }
                                        String[] strs = new String[]{
                                                mColName,
                                                str[0],
                                                str[1],
                                                str[2],
                                        };
                                        mAdapter.getList().add(strs);
                                    }
                                    mAdapter.notifyDataSetChanged();
                                } else Toasts.showInfo(context, getString(R.string.request_error8));
                            } else Toasts.showInfo(context, getString(R.string.request_error8));
                        } else Toasts.showInfo(context, getString(R.string.request_error8));
                    } else Toasts.showInfo(context, getString(R.string.request_error8));
                } else Toasts.showInfo(context, getString(R.string.request_error8));
            } else {
                Toasts.showInfo(context, getString(R.string.request_error8));
            }

        } catch (Exception e) {
            Toasts.showInfo(context, getString(R.string.request_error11));
        }
        mDialog.dismiss();
    }

    private void parseGHSelResult() {
        if (isSunlight) {
            initOptions();
            if (pGhList.size() > 0 && cGhList.size() > 0 && cGhList.get(0).size() > 0) {
                mPosition0 = 0;
                mPosition1 = 0;
                tv_terminal.setText(pGhList.get(mPosition0)[1].replace(getString(R.string.parsetstr10), "") +
                        cGhList.get(mPosition0).get(mPosition1)[1].replace(getString(R.string.parsetstr10), ""));
                if (locList1.size() > 0 && locList1.get(0).size() > 0 && locList1.get(0).get(0).size() > 0) {
                    mPosition2 = 0;
                    tv_qy.setText(locList1.get(mPosition0).get(mPosition1).get(mPosition2)[1]);
                    rptMonthDataSel();
                } else mDialog.dismiss();
            } else mDialog.dismiss();
        } else {
            if (pGhList.size() > 0) {
                mPosition1 = 0;
                tv_terminal.setText(pGhList.get(mPosition1)[1]);
                if (locList.size() > 0 && locList.get(mPosition1).size() > 0) {
                    mPosition2 = 0;
                    tv_qy.setText(locList.get(mPosition1).get(mPosition2)[1]);
                    rptMonthDataSel();
                } else mDialog.dismiss();
            } else {
                mDialog.dismiss();
                Toasts.showError(context, getString(R.string.request_error8));
            }
        }
    }

    private void initData() {
        if (isSunlight) {
            if (pGhList == null || cGhList == null || locList1 == null) {
                getTerminal();
            } else {
                initOptions();
                if (pGhList.size() > 0 && cGhList.size() > 0 && cGhList.get(0).size() > 0) {
                    mPosition0 = 0;
                    mPosition1 = 0;
                    tv_terminal.setText(pGhList.get(mPosition0)[1].replace(getString(R.string.parsetstr10), "") +
                            cGhList.get(mPosition0).get(mPosition1)[1].replace(getString(R.string.parsetstr10), ""));
                    if (locList1.size() > 0 && locList1.get(0).size() > 0 && locList1.get(0).get(0).size() > 0) {
                        mPosition2 = 0;
                        tv_qy.setText(locList1.get(mPosition0).get(mPosition1).get(mPosition2)[1]);
                        mDialog.show();
                        rptMonthDataSel();
                    }
                }
            }
        } else {
            if (pGhList == null || locList == null) {
                getTerminal();
            } else {
                if (pGhList.size() > 0) {
                    mPosition1 = 0;
                    tv_terminal.setText(pGhList.get(mPosition1)[1]);
                    if (locList.size() > 0 && locList.get(mPosition1).size() > 0) {
                        mPosition2 = 0;
                        tv_qy.setText(locList.get(mPosition1).get(mPosition2)[1]);
                        mDialog.show();
                        rptMonthDataSel();
                    }
                } else Toasts.showError(context, getString(R.string.request_error8));
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
                mPosition0 = options1;
                mPosition1 = options2;
                tv_terminal.setText(pGhList.get(mPosition0)[1].replace(getString(R.string.parsetstr10), "") +
                        cGhList.get(mPosition0).get(mPosition1)[1].replace(getString(R.string.parsetstr10), ""));

                mPosition2 = -1;
                tv_qy.setText("");
                mAdapter.getList().clear();
                mAdapter.notifyDataSetChanged();
            }
        }).build();
        mOptions.setPicker(gateway, terminal);
    }

    /**
     * 获取终端
     */
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

    private void requestData() {
        if (tv_terminal.getText().length() > 0 && tv_qy.getText().length() > 0 &&
                tv_time.getText().length() > 0) {
            mDialog.show();
            rptMonthDataSel();
        }
    }

    private void rptMonthDataSel() {
        if (Data.userResult != null && Data.GhType != null) {
            String Location = "", GreenhouseID = "";
            if (isSunlight) {
                GreenhouseID = cGhList.get(mPosition0).get(mPosition1)[0];
                Location = locList1.get(mPosition0).get(mPosition1).get(mPosition2)[0];
            } else {
                GreenhouseID = pGhList.get(mPosition1)[0];
                Location = locList.get(mPosition1).get(mPosition2)[0];
            }
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"EnterpriseID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"GreenhouseID", GreenhouseID});
            data.add(new String[]{"Location", Location});
            data.add(new String[]{"startTime", tv_time.getText().toString()});
            data.add(new String[]{"endTime", tv_time.getText().toString()});
            data.add(new String[]{"PageSize", "1"});
            data.add(new String[]{"PageIndex", "1"});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("RptMonthDataSel",
                    "/StringService/DataInfo.asmx", data, 1, context);
        } else {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    private void showBottomSheet2() {
        if (pGhList.size() > 0) {
            if (mBottomSheet2 == null) {
                BottomListSheetBuilder mBuilder2 = new BottomListSheetBuilder(context, true)
                        .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                mPosition1 = position;
                                mPosition2 = -1;
                                tv_terminal.setText(pGhList.get(mPosition1)[1]);
                                tv_qy.setText("");
                                dialog.dismiss();
                                mAdapter.getList().clear();
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                for (int i = 0; i < pGhList.size(); i++) {
                    mBuilder2.addItem(pGhList.get(i)[1]);
                }
                mBuilder2.setCheckedIndex(mPosition1);
                mBottomSheet2 = mBuilder2.build();
            }
            mBottomSheet2.show();
        } else Toasts.showInfo(context, getString(R.string.request_error8));
    }

    private void showBottomSheet3() {
        if (isSunlight) {
            if (locList1 != null && locList1.size() > mPosition0 && locList1.get(mPosition0).size() > mPosition1
                    && locList1.get(mPosition0).get(mPosition1).size() > 0) {
                BottomListSheetBuilder mBuilder3 = new BottomListSheetBuilder(context, true)
                        .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                mPosition2 = position;
                                tv_qy.setText(locList1.get(mPosition0).get(mPosition1).get(mPosition2)[1]);
                                dialog.dismiss();
                                mAdapter.getList().clear();
                                mAdapter.notifyDataSetChanged();
                                requestData();
                            }
                        });
                for (int i = 0; i < locList1.get(mPosition0).get(mPosition1).size(); i++) {
                    mBuilder3.addItem(locList1.get(mPosition0).get(mPosition1).get(i)[1]);
                }
                mBuilder3.setCheckedIndex(mPosition2);
                mBuilder3.build().show();
            } else Toasts.showInfo(context, getString(R.string.request_error8));
        } else {
            if (locList != null && locList.size() > mPosition1 && locList.get(mPosition1).size() > 0) {
                BottomListSheetBuilder mBuilder3 = new BottomListSheetBuilder(context, true)
                        .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                mPosition2 = position;
                                tv_qy.setText(locList.get(mPosition1).get(mPosition2)[1]);
                                dialog.dismiss();
                                mAdapter.getList().clear();
                                mAdapter.notifyDataSetChanged();
                                requestData();
                            }
                        });
                for (int i = 0; i < locList.get(mPosition1).size(); i++) {
                    mBuilder3.addItem(locList.get(mPosition1).get(i)[1]);
                }
                mBuilder3.setCheckedIndex(mPosition2);
                mBuilder3.build().show();
            } else Toasts.showInfo(context, getString(R.string.request_error8));
        }
    }

    private String getTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        return formatter.format(date);
    }

    private void register() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void unRegister() {
        WebServiceManage.dispose();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible) register();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
}
