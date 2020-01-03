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
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.activity.WarningActivity;
import com.cdzp.huinongyun.adapter.WarnSetAdapter;
import com.cdzp.huinongyun.bean.IntelTypeGroSelResult;
import com.cdzp.huinongyun.bean.UniversalResult;
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

import java.util.ArrayList;
import java.util.List;

import static com.cdzp.huinongyun.util.Data.cGhList;
import static com.cdzp.huinongyun.util.Data.pGhList;

/**
 * 预警设置
 */
public class WarnSetFragment extends Fragment implements View.OnClickListener {

    private View view;
    private boolean isVisible, isInit;
    private Context context;
    private String TAG = "WarnSetFragment";
    private QMUITipDialog mDialog;
    private TextView tv_terminal, tv_type;
    private int mPosition0, mPosition1 = -1, mPosition2;
    private QMUIBottomSheet mBottomSheet1;
    private List<String[]> mSensor;
    private List<List<String[]>> mIntel;
    private RecyclerView mRecyclerView;
    private WarnSetAdapter mAdapter;
    private OptionsPickerView mOptions;
    private boolean isSunlight = false;//是否是日光,电磁阀
    private boolean isEnglish;//是否中英文

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_warn_set, container, false);
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
                if (!isInit) {
                    isInit = true;
                    getData();
                }
            }
        } else {
            WebServiceManage.dispose();
            EventBus.getDefault().unregister(this);
        }
    }

    private void init() {
        context = getContext();
        isEnglish = ((WarningActivity) getActivity()).isEnglish();
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

        tv_terminal = view.findViewById(R.id.tv_fws_terminal);
        tv_type = view.findViewById(R.id.tv_fws_type);

        mRecyclerView = view.findViewById(R.id.recyclerView_fws);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new WarnSetAdapter(context, isEnglish);
        mRecyclerView.setAdapter(mAdapter);

        view.findViewById(R.id.ll_fws1).setOnClickListener(this);
        view.findViewById(R.id.ll_fws2).setOnClickListener(this);
        view.findViewById(R.id.btn_fws).setOnClickListener(this);

        if (isVisible && !isInit) {
            isInit = true;
            register();
            getData();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fws1:
                if (isSunlight) {
                    if (mOptions != null) {
                        mOptions.show();
                    } else getTerminal();
                } else {
                    if (pGhList != null) {
                        showBottomSheet1();
                    } else {
                        getTerminal();
                    }
                }
                break;
            case R.id.ll_fws2:
                showBottomSheet2();
                break;
            case R.id.btn_fws:
                if (mAdapter.getList() != null && mAdapter.getList().size() > 0) {
                    saveIntelTypeList();
                } else Toasts.showInfo(context, getString(R.string.request_error8));
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
                case 1:
                    parseIntTypeGroSelResult(message.getmResult());
                    break;
                case 2:
                    parseSaveIntelTypeList(message.getmResult());
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

    private void getData() {
        if (isSunlight) {
            if (pGhList == null || cGhList == null) {
                getTerminal();
            } else {
                initOptions();
                if (pGhList.size() > 0 && cGhList.size() > 0 && cGhList.get(0).size() > 0) {
                    mDialog.show();
                    mPosition0 = 0;
                    mPosition1 = 0;
                    tv_terminal.setText(pGhList.get(mPosition0)[1].replace(getString(R.string.parsetstr10), "") +
                            cGhList.get(mPosition0).get(mPosition1)[1].replace(getString(R.string.parsetstr10), ""));
                    IntelTypeGroupSel();
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
                    IntelTypeGroupSel();
                } else {
                    Toasts.showInfo(context, getString(R.string.warn_str10));
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
                mSensor = null;
                mIntel = null;
                notifyData();
                mPosition2 = -1;
                tv_type.setText("");
                IntelTypeGroupSel();
            }
        }).build();
        mOptions.setPicker(gateway, terminal);
    }

    private void parseGHSelResult() {
        if (isSunlight) {
            initOptions();
            if (pGhList.size() > 0 && cGhList.size() > 0 && cGhList.get(0).size() > 0) {
                mPosition0 = 0;
                mPosition1 = 0;
                tv_terminal.setText(pGhList.get(mPosition0)[1].replace(getString(R.string.parsetstr10), "") +
                        cGhList.get(mPosition0).get(mPosition1)[1].replace(getString(R.string.parsetstr10), ""));
                IntelTypeGroupSel();
            } else mDialog.dismiss();
        } else {
            if (pGhList.size() > 0) {
                mPosition1 = 0;
                tv_terminal.setText(pGhList.get(mPosition1)[1]);
                IntelTypeGroupSel();
            } else {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.warn_str10));
            }
        }
    }

    private void IntelTypeGroupSel() {
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
            WebServiceManage.requestData("IntelTypeGroupSel",
                    "/StringService/AlarmInfo.asmx", data, 1, context);
        } else {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    /**
     * 设置
     */
    private void saveIntelTypeList() {
        if (Data.userResult != null) {
            mDialog.show();
            StringBuffer buffer = new StringBuffer();
            buffer.append("[");
            for (int i = 0; i < mAdapter.getList().size(); i++) {
                if (mAdapter.getList().get(i)[6].equals("1")) {
                    double max = Double.parseDouble(mAdapter.getList().get(i)[4]);
                    double min = Double.parseDouble(mAdapter.getList().get(i)[5]);
                    if (min > max) {
                        mDialog.dismiss();
                        Toasts.showInfo(context, mAdapter.getList().get(i)[7].split("\\(")[0] + getString(R.string.warn_str23));
                        return;
                    }
                }
                buffer.append("{");

                buffer.append("\"");
                buffer.append("IntelID");
                buffer.append("\"");
                buffer.append(":");
                buffer.append(mAdapter.getList().get(i)[0] + ",");

                buffer.append("\"");
                buffer.append("GreenhouseID");
                buffer.append("\"");
                buffer.append(":");
                buffer.append(mAdapter.getList().get(i)[1] + ",");

                buffer.append("\"");
                buffer.append("SensorType");
                buffer.append("\"");
                buffer.append(":");
                buffer.append(mAdapter.getList().get(i)[2] + ",");

                buffer.append("\"");
                buffer.append("IntelType");
                buffer.append("\"");
                buffer.append(":");
                buffer.append(mAdapter.getList().get(i)[3] + ",");

                buffer.append("\"");
                buffer.append("MaxValue");
                buffer.append("\"");
                buffer.append(":");
                buffer.append(mAdapter.getList().get(i)[4] + ",");

                buffer.append("\"");
                buffer.append("MinValue");
                buffer.append("\"");
                buffer.append(":");
                buffer.append(mAdapter.getList().get(i)[5] + ",");

                buffer.append("\"");
                buffer.append("IsCheck");
                buffer.append("\"");
                buffer.append(":");
                buffer.append(mAdapter.getList().get(i)[6]);

                buffer.append("}");
                if (i != mAdapter.getList().size() - 1)
                    buffer.append(",");
            }
            buffer.append("]");
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            data.add(new String[]{"models", buffer.toString()});
            WebServiceManage.requestData("SaveIntelTypeList",
                    "/StringService/AlarmInfo.asmx", data, 2, context);
        } else {
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    /**
     * 解析设置后返回数据
     *
     * @param s
     */
    private void parseSaveIntelTypeList(String s) {
        UniversalResult result = new Gson().fromJson(s, UniversalResult.class);
        mDialog.dismiss();
        if (result.isSuccess()) {
            Toasts.showSuccess(context, getString(R.string.parsetstr));
        } else
            Toasts.showError(context, getString(R.string.warn_str22));

    }

    /**
     * 解析数据
     *
     * @param s
     */
    private void parseIntTypeGroSelResult(String s) {
        try {
            IntelTypeGroSelResult result = new Gson().fromJson(s, IntelTypeGroSelResult.class);
            if (result.isSuccess()) {
                if (result.getData().size() > 0 && result.getData().size() > 0) {
                    mSensor = new ArrayList<>();
                    mIntel = new ArrayList<>();
                    for (int i = 0; i < result.getData().size(); i++) {
                        String typeNames;
                        if (isEnglish) {
                            typeNames = result.getData().get(i).getSensorTypeEnglishName();
                        } else {
                            typeNames = result.getData().get(i).getSensorTypeName();
                        }
                        mSensor.add(new String[]{
                                result.getData().get(i).getSensorType() + "",
                                typeNames
                        });
                        List<String[]> list = new ArrayList<>();
                        for (int i1 = 0; i1 < result.getData().get(i).getIntelType().size(); i1++) {
                            String typeName = result.getData().get(i).getIntelType().get(i1).getIntelTypeName();
                            if (isEnglish) {
                                for (int i2 = 0; i2 < result.getSensorInfo().size(); i2++) {
                                    if (typeName.equals(result.getSensorInfo().get(i2).getBaseName())) {
                                        typeName = result.getSensorInfo().get(i2).getEnglishName() + "(" +
                                                result.getData().get(i).getIntelType().get(i1).getUnit() + ")";
                                        break;
                                    }
                                }
                            } else {
                                typeName = typeName.replace(",", "").replace(".", "")
                                        .replace(" ", "") + "(" +
                                        result.getData().get(i).getIntelType().get(i1).getUnit() + ")";
                            }
                            list.add(new String[]{
                                    result.getData().get(i).getIntelType().get(i1).getIntelID(),
                                    result.getData().get(i).getIntelType().get(i1).getGreenhouseID(),
                                    result.getData().get(i).getIntelType().get(i1).getSensorType(),
                                    result.getData().get(i).getIntelType().get(i1).getIntelType(),
                                    result.getData().get(i).getIntelType().get(i1).getMaxValue(),
                                    result.getData().get(i).getIntelType().get(i1).getMinValue(),
                                    result.getData().get(i).getIntelType().get(i1).getIsCheck(),
                                    typeName
                            });
                        }
                        mIntel.add(list);
                    }

                    if (mSensor.size() > 0) {
                        mPosition2 = 0;
                        tv_type.setText(mSensor.get(mPosition2)[1]);
                        mAdapter.setList(mIntel.get(mPosition2));
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toasts.showInfo(context, getString(R.string.warn_str18));
                }
                mDialog.dismiss();
            } else {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.warn_str18));
            }
        } catch (Exception e) {
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
                                mSensor = null;
                                mIntel = null;
                                notifyData();
                                mPosition2 = -1;
                                tv_type.setText("");
                                IntelTypeGroupSel();
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

    private void showBottomSheet2() {
        if (tv_terminal.getText().length() > 0) {
            if (mSensor != null && mIntel != null) {
                if (mSensor.size() > 0 && mIntel.size() == mSensor.size()) {
                    BottomListSheetBuilder mBuilder = new BottomListSheetBuilder(context, true)
                            .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                                @Override
                                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                    mPosition2 = position;
                                    tv_type.setText(mSensor.get(mPosition2)[1]);
                                    mAdapter.setList(mIntel.get(mPosition2));
                                    mAdapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            });
                    for (int i = 0; i < mSensor.size(); i++) {
                        mBuilder.addItem(mSensor.get(i)[1]);
                    }
                    mBuilder.setCheckedIndex(mPosition2);
                    mBuilder.build().show();
                } else {
                    Toasts.showInfo(context, getString(R.string.warn_str18));
                }
            } else {
                mDialog.show();
                IntelTypeGroupSel();
            }
        } else Toasts.showInfo(context, getString(R.string.warn_str12));
    }

    private void notifyData() {
        mAdapter.setList(null);
        mAdapter.notifyDataSetChanged();
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

    private void register() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }
}