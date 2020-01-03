package com.cdzp.huinongyun.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.cdzp.huinongyun.adapter.DevConFragAdapter;
import com.cdzp.huinongyun.bean.DevConAutoParam;
import com.cdzp.huinongyun.bean.DevOnClickInfo;
import com.cdzp.huinongyun.bean.GreenHouseSelResult;
import com.cdzp.huinongyun.bean.HelioGreAutoParam;
import com.cdzp.huinongyun.bean.SocketResult;
import com.cdzp.huinongyun.message.SocketMessage;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Operate;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.cdzp.huinongyun.util.Data.cGhList;
import static com.cdzp.huinongyun.util.Data.pGhList;

/**
 * 设备控制
 */
public class DeviceControlFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean isVisible, isInit = false;
    private QMUITipDialog mDialog;
    private QMUIBottomSheet mBottomSheet2;
    private int mPosition = -1, conTypeMark;
    private TextView tv_terminal;
    private CountDownTimer mDownTimer, mDownTimer1;
    private TimerTask task;
    private Timer timer;
    private boolean isRequest, isOutput;
    private DevConFragAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private long outputCoilStatus;
    private OptionsPickerView mOptions;
    private boolean isSunlight = false;//是否是日光温室
    private int mPosition1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_device_control, container, false);
            initView();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible) {
            registered();
            if (!isInit && view != null) {
                isInit = true;
//                mDialog.show();
                initData();
            }
        } else {
            EventBus.getDefault().unregister(this);
        }
    }

    private void initView() {
        context = getContext();
        if (Data.GhType != null) {
            switch (Data.GhType) {
                case "8":
                    isSunlight = true;
                    break;
                default:

                    break;
            }
        }
        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();

        mRecyclerView = view.findViewById(R.id.mRecyclerView_fdc);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(context.getResources().getColor(R.color.decoration), 1, 1));
        mAdapter = new DevConFragAdapter(isSunlight, context);
        mRecyclerView.setAdapter(mAdapter);

        task = new TimerTask() {
            @Override
            public void run() {
                if (isVisible && isRequest) {
                    isRequest = false;
                    getGreenhouseUserDev();
                }
            }
        };
        timer = new Timer();
        mDownTimer = new CountDownTimer(9000, 2500) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isSunlight) {
                    socketRequest(518, null, new long[]{0, 0});
                } else {
                    socketRequest(159, null, new long[]{0, 0});
                }
            }

            @Override
            public void onFinish() {
                mDialog.dismiss();
            }
        };
        mDownTimer1 = new CountDownTimer(5000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                isRequest = true;
            }
        };

        timer.schedule(task, 0, 5000);

        tv_terminal = view.findViewById(R.id.tv_fdc_terminal);
        view.findViewById(R.id.ll_fdc).setOnClickListener(this);
        if (!isInit && isVisible) {
            isInit = true;
//            mDialog.show();
            registered();
            initData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SocketResult result) {
        if (result.isOK()) {
            switch (result.getLabel()) {
                case 155://温室开关
                    if (!isSunlight) {
                        mDownTimer1.cancel();
                        WebServiceManage.dispose();
                        isRequest = false;
                        //是否是当前选择的类型
                        if (pGhList.get(mPosition)[2].equals(result.getmSender())) {
                            outputCoilStatus = result.getOutputCoilStatus();
                            notifyDataSocket();
                        }
                        mDownTimer1.start();
                        conTypeMark = 0;
                    }
                    break;
                case 160://温室数据返回
                    if (!isSunlight) {
                        mDownTimer.cancel();
                        mDownTimer1.cancel();
                        isRequest = false;
                        WebServiceManage.dispose();
                        DevConAutoParam devData = (DevConAutoParam) result.getmResult();
                        mAdapter.setData(devData, null, isSunlight);
                        mAdapter.notifyDataSetChanged();
                        isOutput = true;
                        outputCoilStatus = 0;
                        getGreenhouseUserDev();
                        mDownTimer1.start();
                        conTypeMark = 0;
                        mDialog.dismiss();
                    } else mDialog.dismiss();
                    break;
                case 161:
                    //判断是否是温室并且是否是当前选择的类型
                    if (!isSunlight && pGhList.get(mPosition)[2].equals(result.getmSender())) {
                        mAdapter.getData().getList().get(tmpPosition).setDevCtlMode(
                                mAdapter.getData().getList().get(tmpPosition).getTemDevCtlMode()
                        );
                        mAdapter.notifyDataSetChanged();
                        conTypeMark = 0;
                        Toasts.showSuccess(context, getString(R.string.socket_succ));
                    }
                    break;
                case 519://日光温室数据返回
                    if (isSunlight) {
                        mDownTimer.cancel();
                        mDownTimer1.cancel();
                        isRequest = false;
                        WebServiceManage.dispose();
                        HelioGreAutoParam devData1 = (HelioGreAutoParam) result.getmResult();
                        mAdapter.setData(null, devData1, isSunlight);
                        mAdapter.notifyDataSetChanged();
                        isOutput = true;
                        outputCoilStatus = 0;
                        getGreenhouseUserDev();
                        mDownTimer1.start();
                        conTypeMark = 0;
                        mDialog.dismiss();
                    } else mDialog.dismiss();
                    break;
                case 526:
                    if (isSunlight) {
                        mDownTimer1.cancel();
                        WebServiceManage.dispose();
                        outputCoilStatus = result.getOutputCoilStatus();
                        isRequest = false;
                        mDownTimer1.start();
                        notifyDataSocket();
                        conTypeMark = 0;
                    }
                    break;
                case 515:
                    if (isSunlight) {
                        mAdapter.getData1().getList().get(tmpPosition).setDevCtlMode(
                                mAdapter.getData1().getList().get(tmpPosition).getTemDevCtlMode()
                        );
                        mAdapter.notifyDataSetChanged();
                        conTypeMark = 0;
                        Toasts.showSuccess(context, getString(R.string.socket_succ));
                    }
                    break;
                default:
                    conTypeMark = 0;
                    mDialog.dismiss();
                    break;
            }
        } else {
            conTypeMark = 0;
            mDialog.dismiss();
            Toasts.showInfo(context, result.getRemark());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebMessage message) {
        if (message.isOk()) {
            switch (message.getLabel()) {
                case 666:
                    parseGreenhouse();
                    break;
                case 10:
                    parseGHUD(message.getmResult());
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

    private void parseGreenhouse() {
        if (isSunlight) {
            initOptions();
            if (pGhList.size() > 0 && cGhList.size() > 0 && cGhList.get(0).size() > 0) {
                mPosition = 0;
                mPosition1 = 0;
                tv_terminal.setText(pGhList.get(mPosition)[1].replace(getString(R.string.parsetstr10), "") + " " +
                        cGhList.get(mPosition).get(mPosition1)[1].replace(getString(R.string.parsetstr10), ""));
                if (pGhList.get(mPosition)[6].equals("1") && cGhList.get(mPosition).get(mPosition1)[6].equals("1")) {
                    mDownTimer.cancel();
                    conTypeMark = 0;
                    mAdapter.setData(null, null, isSunlight);
                    mAdapter.notifyDataSetChanged();
                    mDownTimer.start();
                } else {
                    mAdapter.setData(null, null, isSunlight);
                    mAdapter.notifyDataSetChanged();
                    mDialog.dismiss();
                }
            } else {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.request_error8));
            }
        } else {
            if (pGhList != null && pGhList.size() > 0) {
                mPosition = 0;
                tv_terminal.setText(pGhList.get(mPosition)[1]);
                mDownTimer.cancel();
                if (pGhList.get(mPosition)[6].equals("1")) {
                    conTypeMark = 0;
                    mDownTimer.start();
                } else {
                    mDialog.dismiss();
                }
            } else {
                mDialog.dismiss();
                Toasts.showInfo(context, getString(R.string.request_error8));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DevOnClickInfo info) {
        switch (info.getLabel()) {
            case 0:
                showSingleChoiceDialog(info.getPosition());
                break;
            case 1:
                if (isSunlight) {
                    byte regAdd = mAdapter.getData1().getList().get(info.getPosition()).getRegAdd1();
                    long coilStatus = (1l << regAdd);
                    if ((outputCoilStatus & (1l << regAdd)) > 0) {
                        socketRequest(513, null, new long[]{1, coilStatus});
                    } else
                        socketRequest(512, null, new long[]{1, coilStatus});
                } else {
                    byte regAdd = mAdapter.getData().getList().get(info.getPosition()).getRegAdd();
                    long coilStatus = (1l << regAdd);
                    if ((outputCoilStatus & (1l << regAdd)) > 0) {
                        socketRequest(154, null, new long[]{1, coilStatus});
                    } else
                        socketRequest(153, null, new long[]{1, coilStatus});
                }
                break;
            case 2:
                if (isSunlight) {
                    byte regAdd2 = mAdapter.getData1().getList().get(info.getPosition()).getRegAdd1();
                    long coilStatus = (1l << (regAdd2 + 1));
                    if ((outputCoilStatus & (1l << (regAdd2 + 1))) > 0) {
                        socketRequest(513, null, new long[]{1, coilStatus});
                    } else
                        socketRequest(512, null, new long[]{1, coilStatus});
                } else {
                    byte regAdd2 = mAdapter.getData().getList().get(info.getPosition()).getRegAdd();
                    long coilStatus2 = (1l << (regAdd2 + 1));
                    if ((outputCoilStatus & (1l << (regAdd2 + 1))) > 0) {
                        socketRequest(154, null, new long[]{1, coilStatus2});
                    } else
                        socketRequest(153, null, new long[]{1, coilStatus2});
                }
                break;
            default:

                break;
        }
    }

    private void parseGHUD(String s) {
        try {
            GreenHouseSelResult result = new Gson().fromJson(s, GreenHouseSelResult.class);
            if (result.isSuccess()) {
                if (result.getData() != null) {
                    if (isSunlight) {
                        if (result.getData().get(0).getCGhList() != null) {
                            for (int i = 0; i < result.getData().get(0).getCGhList().size(); i++) {
                                if (cGhList.get(mPosition).get(mPosition1)[0].equals(result.getData().get(0).getCGhList().get(i).getGreenhouseID())) {
                                    long temOutputCoilStatus = Long.parseLong(result.getData().get(0).getCGhList().get(i).getOutputCoilStatus());
                                    if (isOutput) {
                                        isOutput = false;
                                        outputCoilStatus = temOutputCoilStatus;
                                        notifyDataSocket();
                                    } else if (outputCoilStatus != temOutputCoilStatus) {
                                        outputCoilStatus = temOutputCoilStatus;
                                        notifyDataSocket();
                                    }
                                    break;
                                }
                            }
                        }
                    } else {
                        if (result.getData().get(0).getPGhList() != null) {
                            for (int i = 0; i < result.getData().get(0).getPGhList().size(); i++) {
                                if (pGhList.get(mPosition)[0].equals(result.getData().get(0).getPGhList().get(i).getGreenhouseID())) {
                                    long temOutputCoilStatus = Long.parseLong(result.getData().get(0).getPGhList().get(i).getOutputCoilStatus());
                                    if (isOutput) {
                                        isOutput = false;
                                        outputCoilStatus = temOutputCoilStatus;
                                        notifyDataSocket();
                                    } else if (outputCoilStatus != temOutputCoilStatus) {
                                        outputCoilStatus = temOutputCoilStatus;
                                        notifyDataSocket();
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            isRequest = true;
        } catch (Exception e) {

        }
    }

    private void notifyDataSocket() {
        if (isSunlight) {
            if (mAdapter.getData1() != null && mAdapter.getData1().getList() != null) {
                for (int i = 0; i < mAdapter.getData1().getList().size(); i++) {
                    byte ZhengFanZhuan = mAdapter.getData1().getList().get(i).getZhengFanZhuan();
                    byte RegAdd = mAdapter.getData1().getList().get(i).getRegAdd1();
                    if ((outputCoilStatus & (1l << RegAdd)) > 0) {
                        mAdapter.getData1().getList().get(i).setOne(true);
                    } else {
                        mAdapter.getData1().getList().get(i).setOne(false);
                    }
                    if (ZhengFanZhuan > 0) {
                        if ((outputCoilStatus & (1l << (RegAdd + 1))) > 0) {
                            mAdapter.getData1().getList().get(i).setTwo(true);
                        } else {
                            mAdapter.getData1().getList().get(i).setTwo(false);
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        } else {
            if (mAdapter.getData() != null && mAdapter.getData().getList() != null) {
                for (int i = 0; i < mAdapter.getData().getList().size(); i++) {
                    byte ZhengFanZhuan = mAdapter.getData().getList().get(i).getZhengFanZhuan();
                    byte RegAdd = mAdapter.getData().getList().get(i).getRegAdd();
                    if ((outputCoilStatus & (1l << RegAdd)) > 0) {
                        mAdapter.getData().getList().get(i).setOne(true);
                    } else {
                        mAdapter.getData().getList().get(i).setOne(false);
                    }
                    if (ZhengFanZhuan > 0) {
                        if ((outputCoilStatus & (1l << (RegAdd + 1))) > 0) {
                            mAdapter.getData().getList().get(i).setTwo(true);
                        } else {
                            mAdapter.getData().getList().get(i).setTwo(false);
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initData() {
        if (isSunlight) {
            if (pGhList == null || cGhList == null) {
                getGreenHouseSel();
            } else {
                initOptions();
                if (pGhList.size() > 0 && cGhList.size() > 0 && cGhList.get(0).size() > 0) {
                    mPosition = 0;
                    mPosition1 = 0;
                    tv_terminal.setText(pGhList.get(mPosition)[1].replace(getString(R.string.parsetstr10), "") + " " +
                            cGhList.get(mPosition).get(mPosition1)[1].replace(getString(R.string.parsetstr10), ""));
                    if (pGhList.get(mPosition)[6].equals("1") && cGhList.get(mPosition).get(mPosition1)[6].equals("1")) {
                        mDialog.show();
                        mDownTimer.cancel();
                        conTypeMark = 0;
                        mAdapter.setData(null, null, isSunlight);
                        mAdapter.notifyDataSetChanged();
                        mDownTimer.start();
                    } else {
                        mAdapter.setData(null, null, isSunlight);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        } else {
            if (pGhList == null) {
                getGreenHouseSel();
            } else {
                if (pGhList.size() > 0) {
                    mPosition = 0;
                    tv_terminal.setText(pGhList.get(mPosition)[1]);
                    mDownTimer.cancel();
                    if (pGhList.get(mPosition)[6].equals("1")) {
                        mDialog.show();
                        conTypeMark = 0;
                        mDownTimer.start();
                    }
                } else Toasts.showError(context, getString(R.string.request_error8));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fdc:
                if (isSunlight) {
                    if (mOptions != null) {
                        mOptions.show();
                    } else getGreenHouseSel();
                } else
                    showBottomSheet();
                break;
            default:

                break;
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
                mPosition = options1;
                mPosition1 = options2;
                tv_terminal.setText(pGhList.get(mPosition)[1].replace(getString(R.string.parsetstr10), "") + " " +
                        cGhList.get(mPosition).get(mPosition1)[1].replace(getString(R.string.parsetstr10), ""));
                isRequest = false;
                WebServiceManage.dispose();
                mDownTimer.cancel();
                if (pGhList.get(mPosition)[6].equals("1") && cGhList.get(mPosition).get(mPosition1)[6].equals("1")) {
                    mDialog.show();
                    conTypeMark = 0;
                    mAdapter.setData(null, null, isSunlight);
                    mAdapter.notifyDataSetChanged();
                    mDownTimer.start();
                } else {
                    mAdapter.setData(null, null, isSunlight);
                    mAdapter.notifyDataSetChanged();
                    Toasts.showInfo(context, getString(R.string.parsetstr11));
                }

            }
        }).build();
        mOptions.setPicker(gateway, terminal);
    }

    private int tmpPosition;

    private void showSingleChoiceDialog(final int position) {
        final String[] items = new String[]{
                context.getString(R.string.rem_con_str3),
                context.getString(R.string.rem_con_str4),
                context.getString(R.string.rem_con_str5)
        };
        final int checkedIndex;
        if (isSunlight) {
            checkedIndex = mAdapter.getData1().getList().get(position).getDevCtlMode();
        } else {
            checkedIndex = mAdapter.getData().getList().get(position).getDevCtlMode();
        }
        new QMUIDialog.CheckableDialogBuilder(context)
                .setCheckedIndex(checkedIndex)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (checkedIndex != which) {
                            tmpPosition = position;
                            if (isSunlight) {
                                mAdapter.getData1().getList().get(position).setTemDevCtlMode((byte) which);
                                socketRequest(514, mAdapter.getData1(), new long[]{0, 0});
                            } else {
                                mAdapter.getData().getList().get(position).setTemDevCtlMode((byte) which);
                                socketRequest(161, mAdapter.getData(), new long[]{0, 0});
                            }
                        }
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    private void showBottomSheet() {
        if (pGhList != null) {
            if (pGhList.size() > 0) {
                if (mBottomSheet2 == null) {
                    BottomListSheetBuilder mBuilder2 = new BottomListSheetBuilder(context, true)
                            .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                                @Override
                                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                    if (pGhList.get(position)[6].equals("1")) {
                                        isRequest = false;
                                        WebServiceManage.dispose();
                                        mDownTimer.cancel();
                                        mPosition = position;
                                        tv_terminal.setText(pGhList.get(mPosition)[1]);
                                        conTypeMark = 0;
                                        mAdapter.setData(null, null, isSunlight);
                                        mAdapter.notifyDataSetChanged();
                                        dialog.dismiss();
                                        mDialog.show();
                                        mDownTimer.start();
                                    } else {
                                        mDownTimer.cancel();
                                        isRequest = false;
                                        WebServiceManage.dispose();
                                        mPosition = position;
                                        tv_terminal.setText(pGhList.get(mPosition)[1]);
                                        mAdapter.setData(null, null, isSunlight);
                                        mAdapter.notifyDataSetChanged();
                                        dialog.dismiss();
                                        Toasts.showInfo(context, getString(R.string.parsetstr11));
                                    }
                                }
                            });
                    for (int i = 0; i < pGhList.size(); i++) {
                        String str = pGhList.get(i)[1];
                        if (!str.contains(getString(R.string.dev_str2)))
                            mBuilder2.addItem(str);
                    }
                    mBuilder2.setCheckedIndex(mPosition);
                    mBottomSheet2 = mBuilder2.build();
                }
                mBottomSheet2.show();
            } else Toasts.showInfo(context, getString(R.string.request_error8));
        } else {
            getGreenHouseSel();
        }
    }

    /**
     * 发送请求
     *
     * @param commandType 命令类型
     * @param object      请求数据
     * @param coilStatus  开关状态
     */
    private void socketRequest(int commandType, Object object, long[] coilStatus) {
        if (Data.userResult != null) {
            SocketMessage message = new SocketMessage();
            message.setZP_KEY_CLIENT(987656789);
            if (isSunlight) {
                if (pGhList != null && cGhList != null && cGhList.size() > mPosition &&
                        cGhList.get(mPosition).size() > mPosition1) {
                    message.setRequestType(506);
                    message.setPlayNo(cGhList.get(mPosition).get(mPosition1)[2]);
                } else return;
            } else {
                if (mPosition >= 0 && pGhList != null && pGhList.size() > mPosition) {
                    message.setRequestType(3);
                } else return;
            }
            message.setmSender(Data.userResult.getData().get(0).getUserName());
            message.setmReceiver(pGhList.get(mPosition)[2]);
            message.setCommandType(commandType);
            message.setResult(object);
            if (coilStatus[0] == 1) {
                message.setCoilStatus(coilStatus[1]);
            }
            if (pGhList.get(mPosition)[3].equals("1") && conTypeMark < 5) {
                message.setIp(pGhList.get(mPosition)[4]);
                message.setPort(Integer.parseInt(pGhList.get(mPosition)[5]));
                conTypeMark++;
            } else {
                message.setIp(Data.SERVER_IP);
                message.setPort(Data.SERVER_PORT);
            }
            Operate.getDefault().getSender().messageRequest(message);
        }
    }

    private void getGreenHouseSel() {
        if (Data.userResult != null && Data.GhType != null) {
            mDialog.show();
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"Type", Data.GhType});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("GreenHouseSel",
                    "/StringService/DataInfo.asmx", data, 666, context);
        } else Toasts.showError(context, getString(R.string.request_error9));
    }

    private void getGreenhouseUserDev() {
        if (Data.userResult != null && Data.GhType != null) {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"EntID", Data.userResult.getData().get(0).getEnterpriseID() + ""});
            data.add(new String[]{"Type", Data.GhType});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("GreenHouseSel",
                    "/StringService/DataInfo.asmx", data, 10, context);
        } else Toasts.showError(context, getString(R.string.request_error9));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible)
            registered();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void registered() {
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
