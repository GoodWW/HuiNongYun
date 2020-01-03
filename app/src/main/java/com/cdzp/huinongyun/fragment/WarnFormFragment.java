package com.cdzp.huinongyun.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.cdzp.huinongyun.bean.IntelTypeSelResult;
import com.cdzp.huinongyun.bean.RptMonAlaSelResult;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cdzp.huinongyun.util.Data.cGhList;
import static com.cdzp.huinongyun.util.Data.pGhList;

/**
 * 报警报表
 */
public class WarnFormFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean isVisible, isRequest = false;
    private QMUITipDialog mDialog;
    private TextView tv_terminal, tv_type, tv_time, tv1, tv2, tv3, tv4;
    private TimePickerView pvTime;
    private Date mDate;
    private int mPosition0, mPosition1 = -1, mPosition2;
    private List<String[]> intelType;
    private QMUIBottomSheet mBottomSheet1;
    private OptionsPickerView mOptions;
    private boolean isSunlight = false;//是否是日光,电磁阀
    private boolean isEnglish;//是否中英文

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_warn_form, container, false);
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
                    getData();
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

        tv_terminal = view.findViewById(R.id.tv_fwf_terminal);
        tv_type = view.findViewById(R.id.tv_fwf_type);
        tv_time = view.findViewById(R.id.tv_fwf_time);
        pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mDate = date;
                tv_time.setText(getTime(mDate));
                mDialog.show();
                notifyData();
                rptMonthAlarmSel();
            }
        })
                .isCyclic(true)
                .setType(new boolean[]{true, true, false, false, false, false})
                .build();

        mDate = new Date();
        tv_time.setText(getTime(mDate));

        tv1 = view.findViewById(R.id.tv_fwf1);
        tv2 = view.findViewById(R.id.tv_fwf2);
        tv3 = view.findViewById(R.id.tv_fwf3);
        tv4 = view.findViewById(R.id.tv_fwf4);

        view.findViewById(R.id.ll_fwf1).setOnClickListener(this);
        view.findViewById(R.id.ll_fwf2).setOnClickListener(this);
        view.findViewById(R.id.ll_fwf3).setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 666:
                    parseGHSelResult();
                    break;
                case 1:
                    parseITSelResult(message.getmResult());
                    break;
                case 2:
                    parseRptMonResult(message.getmResult());
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
            case R.id.ll_fwf1:
                if (isSunlight) {
                    if (mOptions != null) {
                        mOptions.show();
                    } else {
                        getTerminal();
                    }
                } else {
                    if (pGhList != null) {
                        showBottomSheet1();
                    } else {
                        getTerminal();
                    }
                }
                break;
            case R.id.ll_fwf2:
                showBottomSheet2();
                break;
            case R.id.ll_fwf3:
                pvTime.show();
                break;
            default:

                break;
        }
    }

    private void showBottomSheet2() {
        if (tv_terminal.getText().length() > 0) {
            if (intelType != null) {
                if (intelType.size() > 0) {
                    BottomListSheetBuilder mBuilder = new BottomListSheetBuilder(context, true)
                            .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                                @Override
                                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                    mPosition2 = position;
                                    tv_type.setText(intelType.get(mPosition2)[1]);
                                    dialog.dismiss();
                                    mDialog.show();
                                    notifyData();
                                    rptMonthAlarmSel();
                                }
                            });
                    for (int i = 0; i < intelType.size(); i++) {
                        mBuilder.addItem(intelType.get(i)[1]);
                    }
                    mBuilder.setCheckedIndex(mPosition2);
                    mBuilder.build().show();
                } else {
                    Toasts.showInfo(context, getString(R.string.warn_str11));
                }
            } else {
                mDialog.show();
                intelTypeSel();
            }
        } else Toasts.showInfo(context, getString(R.string.warn_str12));
    }

    private void showBottomSheet1() {
        if (pGhList.size() > 0) {
            if (mBottomSheet1 == null) {
                BottomListSheetBuilder mBuilder2 = new BottomListSheetBuilder(context, true)
                        .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                mPosition1 = position;
                                tv_terminal.setText(pGhList.get(mPosition1)[1]);
                                dialog.dismiss();
                                mDialog.show();
                                intelType = null;
                                mPosition2 = -1;
                                tv_type.setText("");
                                notifyData();
                                intelTypeSel();
                            }
                        });
                for (int i = 0; i < pGhList.size(); i++) {
                    String str = pGhList.get(i)[1];
                    if (!str.contains(getString(R.string.dev_str2)))
                        mBuilder2.addItem(str);
                }
                mBuilder2.setCheckedIndex(mPosition1);
                mBottomSheet1 = mBuilder2.build();
            }
            mBottomSheet1.show();
        } else Toasts.showInfo(context, getString(R.string.request_error8));
    }

    private void parseRptMonResult(String s) {
        RptMonAlaSelResult result = new Gson().fromJson(s, RptMonAlaSelResult.class);
        if (result.isSuccess()) {
            if (result.getData().size() > 0 && result.getData().get(0).size() > 0) {
                if (isEnglish) {
                    tv1.setText(result.getData().get(0).get(0).getLocation() + getString(R.string.dev_str1));
                } else tv1.setText(result.getData().get(0).get(0).getLocationName());
                tv2.setText(result.getData().get(0).get(0).getMaxAlarmValue() + "/" +
                        result.getData().get(0).get(0).getMinAlarmValue() +
                        result.getData().get(0).get(0).getUnit());
                tv3.setText(result.getData().get(0).get(0).getTotal() + "/" +
                        result.getData().get(0).get(0).getSumHour() + "h");
                tv4.setText(result.getData().get(0).get(0).getAvgMinute() + "min");
            }
        } else {
            Toasts.showInfo(context, getString(R.string.request_error8));
        }
        mDialog.dismiss();
    }

    /**
     * 获取数据
     */
    private void rptMonthAlarmSel() {
        if (tv_terminal.getText().length() > 0 && tv_type.getText().length() > 0 &&
                tv_time.getText().length() > 0) {
            if (Data.userResult != null) {
                String GreenhouseID = "";
                if (isSunlight) {
                    GreenhouseID = cGhList.get(mPosition0).get(mPosition1)[0];
                } else {
                    GreenhouseID = pGhList.get(mPosition1)[0];
                }
                List<String[]> data = new ArrayList<>();
                data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
                data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
                data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
                data.add(new String[]{"GhID", GreenhouseID});
                data.add(new String[]{"AlarmType", intelType.get(mPosition2)[0]});
                data.add(new String[]{"StartTime", tv_time.getText().toString()});
                data.add(new String[]{"EndTime", tv_time.getText().toString()});
                data.add(new String[]{"PageSize", "1"});
                data.add(new String[]{"PageIndex", "1"});
                WebServiceManage.requestData("RptMonthAlarmSel",
                        "/StringService/AlarmInfo.asmx", data, 2, context);
            } else {
                mDialog.dismiss();
                Toasts.showError(context, getString(R.string.request_error9));
            }
        } else {
            mDialog.dismiss();
        }
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

    /**
     * 解析预警类型
     *
     * @param s
     */
    private void parseITSelResult(String s) {
        try {
            IntelTypeSelResult result = new Gson().fromJson(s, IntelTypeSelResult.class);
            if (result.isSuccess()) {
                if (result.getData().size() > 0) {
                    intelType = new ArrayList<>();
                    for (int i = 0; i < result.getData().size(); i++) {
                        String typeName = result.getData().get(i).getIntelTypeName();
                        if (isEnglish) {
                            for (int i1 = 0; i1 < result.getSensorInfo().size(); i1++) {
                                if (typeName.equals(result.getSensorInfo().get(i1).getBaseName())) {
                                    typeName = result.getSensorInfo().get(i1).getEnglishName();
                                    break;
                                }
                            }
                        }
                        intelType.add(new String[]{
                                result.getData().get(i).getIntelType(),
                                typeName
                        });
                    }
                    if (intelType.size() > 0) {
                        mPosition2 = 0;
                        tv_type.setText(intelType.get(mPosition2)[1]);
                        rptMonthAlarmSel();
                    } else mDialog.dismiss();
                } else {
                    mDialog.dismiss();
                    Toasts.showInfo(context, getString(R.string.warn_str11));
                }
            } else {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.warn_str11));
            }
        } catch (Exception e) {
            mDialog.dismiss();
        }
    }

    /**
     * 获取传感器类型
     */
    private void intelTypeSel() {
        if (Data.userResult != null) {
            String GreenhouseID = "";
            if (isSunlight) {
                GreenhouseID = cGhList.get(mPosition0).get(mPosition1)[0];
            } else {
                GreenhouseID = pGhList.get(mPosition1)[0];
            }
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"GreenhouseID", GreenhouseID});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("IntelTypeSel",
                    "/StringService/AlarmInfo.asmx", data, 1, context);
        } else {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    private void parseGHSelResult() {
        if (isSunlight) {
            initOptions();
            if (pGhList.size() > 0 && cGhList.size() > 0 && cGhList.get(0).size() > 0) {
                mPosition0 = 0;
                mPosition1 = 0;
                tv_terminal.setText(pGhList.get(mPosition0)[1].replace(getString(R.string.parsetstr10), "") +
                        cGhList.get(mPosition0).get(mPosition1)[1].replace(getString(R.string.parsetstr10), ""));
                mDialog.show();
                intelTypeSel();
            }
        } else {
            if (pGhList.size() > 0) {
                mPosition1 = 0;
                tv_terminal.setText(pGhList.get(mPosition1)[1]);
                intelTypeSel();
            } else {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.warn_str10));
            }
        }
    }

    private void getData() {
        if (isSunlight) {
            if (pGhList == null || cGhList == null) {
                getTerminal();
            } else {
                initOptions();
                if (pGhList.size() > 0 && cGhList.size() > 0 && cGhList.get(0).size() > 0) {
                    mPosition0 = 0;
                    mPosition1 = 0;
                    tv_terminal.setText(pGhList.get(mPosition0)[1].replace(getString(R.string.parsetstr10), "") +
                            cGhList.get(mPosition0).get(mPosition1)[1].replace(getString(R.string.parsetstr10), ""));
                    mDialog.show();
                    intelTypeSel();
                }
            }
        } else {
            if (pGhList == null) {
                getTerminal();
            } else {
                if (pGhList.size() > 0) {
                    mDialog.show();
                    mPosition1 = 0;
                    tv_terminal.setText(pGhList.get(mPosition1)[1]);
                    intelTypeSel();
                } else Toasts.showError(context, getString(R.string.warn_str10));
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
                    String str = cGhList.get(i).get(i1)[1];
                    if (!str.contains(getString(R.string.dev_str2))) {
                        list.add(str);
                    } else if (i1 == cGhList.get(i).size() - 1 && list.size() == 0) {
                        list.add("");
                    }
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
                mDialog.show();
                intelType = null;
                mPosition2 = -1;
                tv_type.setText("");
                notifyData();
                intelTypeSel();
            }
        }).build();
        mOptions.setPicker(gateway, terminal);
    }

    private void notifyData() {
        tv1.setText("");
        tv2.setText("");
        tv3.setText("");
        tv4.setText("");
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
