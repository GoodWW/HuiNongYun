package com.cdzp.huinongyun.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.activity.SolenoidActivity;
import com.cdzp.huinongyun.adapter.DiaAddAllAdapter;
import com.cdzp.huinongyun.adapter.SolHosConAdapter;
import com.cdzp.huinongyun.bean.SocketResult;
import com.cdzp.huinongyun.bean.SolGetCtlListResult;
import com.cdzp.huinongyun.message.SocketMessage;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.AdapterListener;
import com.cdzp.huinongyun.util.AdapterListenerTwo;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Operate;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.cdzp.huinongyun.util.Data.pGhList;

/**
 * 电磁阀主机控制
 */
public class SolHosConFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean isVisible, isInit = false, isRequest;
    private QMUITipDialog mDialog;
    private int mPosition, conTypeMark, clickPosition;
    private TextView tv_terminal;
    private QMUIBottomSheet mBottomSheet;
    private TimerTask task;
    private Timer timer;
    private RecyclerView mRecyclerView;
    private SolHosConAdapter mAdapter;
    private CountDownTimer mDownTimer, mDownTimer1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_sol_hos_con, container, false);
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
                }
                if (tv_terminal.getText().length() > 0 && pGhList != null && pGhList.size() > mPosition
                        && pGhList.get(mPosition)[6].equals("1")) {
                    isRequest = true;
                }
            }
        } else {
            if (mDownTimer != null) mDownTimer.cancel();
            isRequest = false;
            EventBus.getDefault().unregister(this);
        }
    }

    private DiaAddAllAdapter adapter;
    AlertDialog irrDialog = null;

    private void initView() {
        RecyclerView mRecyclerView;

        view.findViewById(R.id.btn_all_control).setOnClickListener(this);
        view.findViewById(R.id.btn_all_start).setOnClickListener(this);
        context = getContext();
        mDialog = ((SolenoidActivity) getActivity()).getmDialog();


        mDownTimer = new CountDownTimer(5000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                isRequest = true;
            }
        };

        mDownTimer1 = new CountDownTimer(3000, 3000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mAdapter.setOnClick(true);
            }
        };

        tv_terminal = view.findViewById(R.id.tv_fshc_terminal);
        mRecyclerView = view.findViewById(R.id.mRecyclerView_fshc);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(context.getResources().getColor(R.color.decoration), 1, 1));
        mAdapter = new SolHosConAdapter(context, new AdapterListener() {
            @Override
            public void onClick(int position) {
                try {
                    clickPosition = position;
                    switch (mAdapter.getList().get(position)[3]) {
                        case "0"://当前已关闭，发送打开命令
                            mDownTimer.cancel();
                            WebServiceManage.dispose();
                            isRequest = false;
                            mAdapter.setOnClick(false);
                            socketRequest(622, new int[]{
                                    Integer.parseInt(mAdapter.getList().get(position)[0]),
                                    255
                            });
                            mDownTimer.start();
                            mDownTimer1.cancel();
                            mDownTimer1.start();
                            break;
                        case "85"://当前开关状态未知，发送查询命令
                            mDownTimer.cancel();
                            WebServiceManage.dispose();
                            isRequest = false;
                            mAdapter.setOnClick(false);
                            socketRequest(622, new int[]{
                                    Integer.parseInt(mAdapter.getList().get(position)[0]),
                                    85
                            });
                            mDownTimer.start();
                            mDownTimer1.cancel();
                            mDownTimer1.start();
                            break;
                        case "255"://当前已打开，发送关闭命令
                            mDownTimer.cancel();
                            WebServiceManage.dispose();
                            isRequest = false;
                            mAdapter.setOnClick(false);
                            socketRequest(622, new int[]{
                                    Integer.parseInt(mAdapter.getList().get(position)[0]),
                                    0
                            });
                            mDownTimer.start();
                            mDownTimer1.cancel();
                            mDownTimer1.start();
                            break;
                        default:
                            Toasts.showInfo(context, getString(R.string.solenoid_str5));
                            break;
                    }
                } catch (Exception e) {
                    Toasts.showError(context, getString(R.string.request_error2));
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        task = new TimerTask() {
            @Override
            public void run() {
                if (isVisible && tv_terminal.getText().length() > 0 && isRequest) {
                    isRequest = false;
                    solenoidGetCtlList();
                }
            }
        };
        timer = new Timer();
        timer.schedule(task, 0, 6000);

        view.findViewById(R.id.ll_fshc).setOnClickListener(this);

        if (isVisible && !isInit) {
            isInit = true;
            register();
            initData();
        }
        getDialog();
    }

    List<String[]> listCache = new ArrayList<>();
    int index = 1;

    private void getDialog() {
        listCache.clear();
        for (int i = 0; i < mAdapter.getList().size() && mAdapter.getList().size() > 0; i++) {
            listCache.add(mAdapter.getList().get(i).clone());
            listCache.get(i)[3] = "255";
//            Log.e("aa", "走了: "+mAdapter.getList().size()+"============"+i );
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View contentView = LayoutInflater.from(context).inflate(R.layout.onl_irrlayout, null);
        RecyclerView mRecyclerView = contentView.findViewById(R.id.rv_onlirr);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        adapter = new DiaAddAllAdapter(context,

                new AdapterListenerTwo() {
                    @Override
                    public void onClick(int position) {

                        switch (listCache.get(position)[3]) {
                            case "0":
                                listCache.get(position)[3] = "255";
                                break;
                            case "85":
                                listCache.get(position)[3] = "255";
                                break;
                            case "255":
                                listCache.get(position)[3] = "0";
                                break;
                            default:
                                listCache.get(position)[3] = "0";
                                break;
                        }
                    }
                }
                , listCache);
        mRecyclerView.setAdapter(adapter);
        builder.setTitle(getString(R.string.all_valve));
        builder.setView(contentView);
        builder.setPositiveButton(getString(R.string.personal_str6), new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long IrrValvesL = 0;
                        long IrrValvesH = 0;
                        int i = 63;
                        if (listCache.size() < i) {
                            for (i = listCache.size() - 1; i >= 0; i--) {
                                IrrValvesL = IrrValvesL << 1;
                                if ("0".equals(listCache.get(i)[3])) IrrValvesL += 1;
                            }
                            IrrValvesH = 0;
                        } else {
                            for (int j = 63; j >= 0; j--) {
                                IrrValvesL = IrrValvesL << 1;
                                if ("0".equals(listCache.get(j)[3])) IrrValvesL += 1;
                            }

                            for (int k = 127; k >= 64; k--) {
                                IrrValvesH = IrrValvesH << 1;
                                if ("0".equals(listCache.get(k)[3])) IrrValvesH += 1;
                            }

                        }

//                        Log.e("aa", "高低位   :== "+IrrValvesH+"地位=="+ IrrValvesL+"大小"+listCache.size());
//                        boolean b = listCache == adapter.getList() ? true : false;
//                        boolean c = listCache.equals(mAdapter.getList());
//                        Log.e("aa", "=对错: " + b+"值==="+c);
                        socketRequest1(645, new long[]{IrrValvesH, IrrValvesL});
                        mDownTimer.start();
                        mDownTimer1.cancel();
                        mDownTimer1.start();
                    }
                });
        irrDialog = builder.create();
        irrDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
//                Log.e("aa", "取消: ");
                mDownTimer.start();
                mAdapter.setOnClick(true);
//                mAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fshc:
                showBottomSheet();
                break;
            case R.id.btn_all_start:
                irrDialog.show();
                mDownTimer.cancel();
                WebServiceManage.dispose();
                isRequest = false;
                mAdapter.setOnClick(false);
                getDialog();
                break;
            case R.id.btn_all_control:
//                Toasts.showInfo(context, "请稍后...");
                mDownTimer.cancel();
                WebServiceManage.dispose();
                isRequest = false;
                mAdapter.setOnClick(false);
                socketRequest1(645, new long[]{0, 0});
                mDownTimer.start();
                mDownTimer1.cancel();
                mDownTimer1.start();
                break;
            case 1:

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
                    parseResult();
                    break;
                case 1:
                    parseResult1(message.getmResult());
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SocketResult result) {
        if (result.isOK()) {
            switch (result.getLabel()) {
                case 646://一键停止和开启返回
                    byte b = (byte) result.getmResult();
//                    Log.e("aa", "b=0代表停止返回===b=1代表开启返回:   " + b);
                    if (b == 0) {

                        for (int i = 0; i < mAdapter.getList().size(); i++) {
//                            Log.e("aa", i+"值:   ="+ mAdapter.getList().get(i)[3]+"==" );
                            if (mAdapter.getList().get(i)[3].equals("255")) {
//                                Log.e("aa", i+"值:   =========================  "+ mAdapter.getList().get(i)[3] );
                                mAdapter.getList().get(i)[3] = "0";
                            }
                        }

                        Toasts.showSuccess(context, context.getString(R.string.fert_str7)+"!");
                    } else if (b == 1) {
//                        Log.e("aa", "onMessageEvent:   开始");
                     /*   for (int i = 0; i < mAdapter.getList().size(); i++) {
                            mAdapter.getList().get(i)[3] = "255";
                        }
*/
                        for (int i = 0; i < listCache.size(); i++) {
                            if (listCache.get(i)[3] == "0") {
                                mAdapter.getList().get(i)[3] = "255";
                            }
                        }
                        Toasts.showSuccess(context, context.getString(R.string.fert_str6)+"！");
                    }
//                    mDownTimer.cancel();
//                    mDownTimer1.cancel();
                    mDownTimer1.start();//////
                    mAdapter.setOnClick(true);
                    mAdapter.notifyDataSetChanged();
                    break;
                case 642://远程主机控制电磁阀(成功)
                    if (result.getmResult() instanceof int[] && clickPosition < mAdapter.getList().size()) {
                        int[] mResult = (int[]) result.getmResult();
                        String[] strs = mAdapter.getList().get(clickPosition);
                        switch (mResult[1]) {
                            case 0:
                                strs[3] = "0";
                                mAdapter.getList().set(clickPosition, strs);
                                mAdapter.notifyItemChanged(clickPosition);
                                break;
                            case 85:
                                strs[3] = "85";
                                mAdapter.getList().set(clickPosition, strs);
                                mAdapter.notifyItemChanged(clickPosition);
                                break;
                            case 255:
                                strs[3] = "255";
                                mAdapter.getList().set(clickPosition, strs);
                                mAdapter.notifyItemChanged(clickPosition);
                                break;
                            default:

                                break;
                        }
                    }
                    mDownTimer1.cancel();
                    mAdapter.setOnClick(true);
                    break;
                case 643://远程主机控制电磁阀(失败)
                    if (result.getmResult() instanceof int[]) {
                        int[] mResult = (int[]) result.getmResult();
                        switch (mResult[1]) {
                            case 1:
                                Toasts.showInfo(context, getString(R.string.solenoid_str6));
                                break;
                            case 2:
                                Toasts.showInfo(context, getString(R.string.solenoid_str7));
                                break;
                            case 3:
                                Toasts.showInfo(context, getString(R.string.solenoid_str8));
                                break;
                            default:

                                break;
                        }
                    }
                    mDownTimer1.cancel();
                    mAdapter.setOnClick(true);
                    break;

                default:

                    mDialog.dismiss();
                    break;
            }
        } else {
            mDialog.dismiss();
            Toasts.showInfo(context, result.getRemark());
        }
    }

    private void initData() {
        if (pGhList == null) {
            getGreenHouseSel();
        } else {
            if (pGhList.size() > 0) {
                mPosition = 0;
                tv_terminal.setText(pGhList.get(mPosition)[1]);
                if (pGhList.get(mPosition)[6].equals("1")) {
                    mDialog.show();
                    solenoidGetCtlList();
                }
            } else Toasts.showError(context, getString(R.string.request_error8));
        }
    }

    /**
     * 发送请求
     *
     * @param commandType 命令类型
     * @param result      控制器序号+控制码
     */
    private void socketRequest(int commandType, int[] result) {
        if (Data.userResult != null && pGhList != null && pGhList.size() > mPosition) {
            SocketMessage message = new SocketMessage();
            message.setZP_KEY_CLIENT(987656789);
            message.setRequestType(610);
            message.setmSender(Data.userResult.getData().get(0).getUserName());
            message.setmReceiver(pGhList.get(mPosition)[2]);
            message.setCommandType(commandType);
            message.setResult(result);
            if (pGhList.get(mPosition)[3].equals("1") && conTypeMark < 4) {
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

    /**
     * 发送请求
     *
     * @param commandType 命令类型
     * @param result      控制器序号+控制码
     */
    private void socketRequest1(int commandType, Object result) {
        if (Data.userResult != null && pGhList != null && pGhList.size() > mPosition) {
            SocketMessage message = new SocketMessage();
            message.setZP_KEY_CLIENT(987656789);
            message.setRequestType(610);
            message.setmSender(Data.userResult.getData().get(0).getUserName());
            message.setmReceiver(pGhList.get(mPosition)[2]);
            message.setCommandType(commandType);
            message.setResult(result);
            if (pGhList.get(mPosition)[3].equals("1") && conTypeMark < 4) {
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

    private void showBottomSheet() {
        if (pGhList != null) {
            if (pGhList.size() > 0) {
                if (mBottomSheet == null) {
                    BottomListSheetBuilder mBuilder = new BottomListSheetBuilder(context, true)
                            .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                                @Override
                                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                    mDownTimer.cancel();
                                    WebServiceManage.dispose();
                                    mPosition = position;
                                    tv_terminal.setText(pGhList.get(mPosition)[1]);
                                    if (pGhList.get(position)[6].equals("1")) {
                                        isRequest = true;
                                        conTypeMark = 0;
                                        dialog.dismiss();
                                        mDialog.show();
                                        mAdapter.getList().clear();
                                        mAdapter.notifyDataSetChanged();
                                        solenoidGetCtlList();
                                        mDownTimer.start();
                                    } else {
                                        isRequest = false;
                                        dialog.dismiss();
                                        Toasts.showInfo(context, getString(R.string.parsetstr11));
                                    }
                                }
                            });
                    for (int i = 0; i < pGhList.size(); i++) {
                        String str = pGhList.get(i)[1];
                        if (!str.contains(getString(R.string.dev_str2))) {
                            mBuilder.addItem(str);
                        } else break;
                    }
                    mBuilder.setCheckedIndex(mPosition);
                    mBottomSheet = mBuilder.build();
                }
                mBottomSheet.show();
            } else Toasts.showInfo(context, getString(R.string.request_error8));
        } else {
            getGreenHouseSel();
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

    private void solenoidGetCtlList() {
        if (Data.userResult != null && pGhList != null && pGhList.size() > mPosition) {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"gwID", pGhList.get(mPosition)[0]});
            data.add(new String[]{"pageIndex", "1"});
            data.add(new String[]{"pageSize", "128"});
            data.add(new String[]{"userID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"authCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("SolenoidGetCtlList",
                    "/StringService/DeviceSet.asmx", data, 1, context);
        } else {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.request_error9));
        }
    }

    private void parseResult1(String s) {
        try {
            SolGetCtlListResult result = new Gson().fromJson(s, SolGetCtlListResult.class);
            if (result.isSuccess() && result.getData().size() > 0) {
                if (mAdapter.getList().size() == 0) {
                    for (int i = 0; i < result.getData().size(); i++) {
                        if (result.getData().get(i).getIsUsed() == 1)
                            mAdapter.getList().add(new String[]{
                                    result.getData().get(i).getDisplayNo(),
                                    result.getData().get(i).getGreenhouseName(),
                                    result.getData().get(i).getInputCoilStatus() +
                                            getString(R.string.solenoid_str29),
                                    result.getData().get(i).getOutputCoilStatus() + ""
                            });
                    }
                } else if (result.getData().size() <= mAdapter.getList().size()) {
                    for (int i = 0; i < result.getData().size(); i++) {
                        if (result.getData().get(i).getIsUsed() == 1 &&
                                result.getData().get(i).getDisplayNo().equals(mAdapter.getList().get(i)[0]))
                            mAdapter.getList().set(i, new String[]{
                                    result.getData().get(i).getDisplayNo(),
                                    result.getData().get(i).getGreenhouseName(),
                                    result.getData().get(i).getInputCoilStatus() +
                                            getString(R.string.solenoid_str29),
                                    result.getData().get(i).getOutputCoilStatus() + ""
                            });
                    }
                }
                mAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
            } else {
//                Toasts.showInfo(context, getString(R.string.request_error8));
            }
        } catch (Exception e) {
//            Toasts.showInfo(context, getString(R.string.request_error2));
        }
        isRequest = true;
        mDialog.dismiss();
    }

    private void parseResult() {
        if (pGhList != null && pGhList.size() > 0) {
            mPosition = 0;
            tv_terminal.setText(pGhList.get(mPosition)[1]);
            if (pGhList.get(mPosition)[6].equals("1")) {
                conTypeMark = 0;
                solenoidGetCtlList();
            } else {
                mDialog.dismiss();
            }
        } else {
            mDialog.dismiss();
            Toasts.showInfo(context, getString(R.string.request_error8));
        }
    }

    private void register() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (task != null) task.cancel();
        if (timer != null) timer.cancel();
    }
}
