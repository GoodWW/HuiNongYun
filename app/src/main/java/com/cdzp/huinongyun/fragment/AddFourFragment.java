package com.cdzp.huinongyun.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.activity.HomAddDevActivity;
import com.cdzp.huinongyun.bean.DeviceListResult;
import com.cdzp.huinongyun.bean.DeviceListResults;
import com.cdzp.huinongyun.bean.UniversalResult;
import com.cdzp.huinongyun.bean.Ys7SmIDResult;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加四情
 */
public class AddFourFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean isVisible, isInitData;
    private QMUITipDialog mDialog;
    private EditText et1, et2, et3, et4, et6;
    private TextView tv5;
    private OptionsPickerView mOptions;
    private List<DeviceListResults> list;
    private String disaster, cIID, devID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_add_four, container, false);
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
                if (!isInitData) {
                    isInitData = true;
                    initData();
                }
            }
        } else EventBus.getDefault().unregister(this);
    }

    private void initView() {
        context = getContext();
        mDialog = ((HomAddDevActivity) getActivity()).getmDialog();

        et1 = view.findViewById(R.id.et_faf1);
        et2 = view.findViewById(R.id.et_faf2);
        et3 = view.findViewById(R.id.et_faf3);
        et4 = view.findViewById(R.id.et_faf4);
        tv5 = view.findViewById(R.id.tv_faf5);
        et6 = view.findViewById(R.id.et_faf6);

        view.findViewById(R.id.ll_faf).setOnClickListener(this);
        view.findViewById(R.id.btn_faf).setOnClickListener(this);

        if (isVisible && !isInitData) {
            isInitData = true;
            eRegister();
            initData();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_faf:
                if (list != null) {
                    if (mOptions != null) {
                        mOptions.show();
                    } else Toasts.showInfo(context, getString(R.string.request_error8));
                } else {
                    initData();
                }
                break;
            case R.id.btn_faf:
                if (et1.getText().length() > 0 && et2.getText().length() > 0 &&
                        et3.getText().length() > 0 && et4.getText().length() > 0 &&
                        tv5.getText().length() > 0) {
                    if (disaster != null && disaster.length() > 0) {
                        request3();
                    } else {//没有开通直播
                        new QMUIDialog.MessageDialogBuilder(getActivity())
                                .setTitle(getString(R.string.fou_sit_mon_str7))
                                .setMessage(getString(R.string.faddfour_str7))
                                .addAction(getString(R.string.envir_str1), new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                })
                                .addAction(0, getString(R.string.aaa_btn), QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                        request3();
                                    }
                                })
                                .create().show();
                    }
                } else Toasts.showInfo(context, getString(R.string.request_error6));
                break;
            default:

                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 1:
                    parseResult(message.getmResult());
                    break;
                case 2:
                    parseResult2(message.getmResult());
                    break;
                case 3:
                    parseResult3(message.getmResult());
                    break;
                default:
                    mDialog.dismiss();
                    Toasts.showError(context, getString(R.string.request_error4));
                    break;
            }
        } else {
            mDialog.dismiss();
            Toasts.showError(context, message.getmResult());
        }
    }

    private void parseResult3(String s) {
        try {
            UniversalResult result = new Gson().fromJson(s, UniversalResult.class);
            if (result.isSuccess()) {
                et1.setText("");
                et2.setText("");
                et3.setText("");
                et4.setText("");
                et6.setText("");
                mDialog.dismiss();
                Toasts.showSuccess(context, getString(R.string.request_succ));
            } else {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.request_fail));
            }
        } catch (Exception e) {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.socket_error5));
        }
    }

    private void parseResult2(String s) {
        try {
            DeviceListResult result = new Gson().fromJson(s, DeviceListResult.class);
            if (result.isIsSuccess()) {
                list = new Gson().fromJson(result.getResult(),
                        new TypeToken<List<DeviceListResults>>() {
                        }.getType());
                initOptions();
                mDialog.dismiss();
            } else {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.faddfour_str6));
            }
        } catch (Exception e) {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.socket_error5));
        }
    }

    private void parseResult(String s) {
        try {
            Ys7SmIDResult result = new Gson().fromJson(s, Ys7SmIDResult.class);
            if (result.isSuccess()) {
                request2(result.getSmID());
            } else {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.faddfour_str6));
            }
        } catch (Exception e) {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.socket_error5));
        }
    }

    private void initOptions() {
        final List<String> list1 = new ArrayList<>();
        List<List<String>> list2 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            list1.add(list.get(i).getDeviceName());
            List<String> list3 = new ArrayList<>();
            for (int i1 = 0; i1 < list.get(i).getChannels().size(); i1++) {
                list3.add(list.get(i).getChannels().get(i1).getCameName());
            }
            list2.add(list3);
        }
        if (list1.size() > 0) {
            mOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    disaster = list.get(options1).getChannels().get(options2).getRtmp();
                    cIID = list.get(options1).getChannels().get(options2).getCIID();
                    devID = list.get(options1).getDevID() + "";
                    tv5.setText(list.get(options1).getDeviceName() + " " +
                            list.get(options1).getChannels().get(options2).getCameName());
                }
            }).build();
            mOptions.setPicker(list1, list2);
        }
    }

    private void initData() {
        if (Data.userResult != null) {
            mDialog.show();
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("GetYs7SmID",
                    "/StringService/DeviceSet.asmx", data, 1, context);
        } else Toasts.showError(context, getString(R.string.request_error9));
    }

    private void request2(String smID) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"SmID", smID});
        WebServiceManage.requestData2("SubdomainDeviceList",
                "http://101.200.193.60:9099/zpys7StringService.asmx", data, 2, context);
    }

    private void request3() {
        if (Data.userResult != null) {
            mDialog.show();
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"GrName", et1.getText().toString()});
            data.add(new String[]{"Insects", et2.getText().toString()});
            data.add(new String[]{"Seeding", et3.getText().toString()});
            data.add(new String[]{"SoilID", et4.getText().toString()});
            data.add(new String[]{"Disaster", disaster});
            data.add(new String[]{"CIID", cIID});
            data.add(new String[]{"DevID", devID});
            data.add(new String[]{"Remark", et6.getText().toString()});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("GroupDeviceRegist",
                    "/StringService/DeviceSet.asmx", data, 3, context);
        } else Toasts.showError(context, getString(R.string.request_error9));
    }

    private void eRegister() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        list = null;
    }
}
