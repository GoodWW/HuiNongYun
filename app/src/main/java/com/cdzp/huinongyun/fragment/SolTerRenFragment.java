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

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.activity.SolenoidActivity;
import com.cdzp.huinongyun.adapter.SolTerRenAdapter;
import com.cdzp.huinongyun.bean.SocketResult;
import com.cdzp.huinongyun.bean.SolGetCtlListResult;
import com.cdzp.huinongyun.message.SocketMessage;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Operate;
import com.cdzp.huinongyun.util.SolTerRenAdaListener;
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

import static com.cdzp.huinongyun.util.Data.pGhList;

/**
 * 电磁阀终端改名
 */
public class SolTerRenFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean isVisible, isInit = false;
    private QMUITipDialog mDialog;
    private TextView tv_terminal;
    private int mPosition, conTypeMark, mAdaPosition;
    private RecyclerView mRecyclerView;
    private QMUIBottomSheet mBottomSheet;
    private SolTerRenAdapter mAdapter;
    private String str = "";
    private CountDownTimer mDownTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_sol_ter_ren, container, false);
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
            }
        } else EventBus.getDefault().unregister(this);
    }

    private void initView() {
        context = getContext();
        mDialog = ((SolenoidActivity) getActivity()).getmDialog();

        tv_terminal = view.findViewById(R.id.tv_fstr_terminal);
        mRecyclerView = view.findViewById(R.id.mRecyclerView_fstr);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//        mRecyclerView.addItemDecoration(new DefaultItemDecoration(context.getResources().getColor(R.color.decoration), 1, 1));
        mAdapter = new SolTerRenAdapter(context);

        mDownTimer = new CountDownTimer(3000, 3000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mAdapter.setOnClick(true);
            }
        };
        mAdapter.setListener(new SolTerRenAdaListener() {
            @Override
            public void onClick(int position, String str1) {
                mAdaPosition = position;
                str = str1;
                mAdapter.setOnClick(false);
                mDownTimer.cancel();
                mDownTimer.start();
                socketRequest(614, new String[]{
                        mAdapter.getList().get(position)[0],
                        str
                });
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        view.findViewById(R.id.ll_fstr).setOnClickListener(this);

        if (isVisible && !isInit) {
            isInit = true;
            register();
            initData();
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
            mDownTimer.cancel();
            mAdapter.setOnClick(true);
            switch (result.getLabel()) {
                case 633:
                    mAdapter.getList().get(mAdaPosition)[1] = str;
                    Toasts.showSuccess(context, getString(R.string.devmain_str7));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fstr:
                showBottomSheet();
                break;
            default:

                break;
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

    private void showBottomSheet() {
        if (pGhList != null) {
            if (pGhList.size() > 0) {
                if (mBottomSheet == null) {
                    BottomListSheetBuilder mBuilder = new BottomListSheetBuilder(context, true)
                            .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                                @Override
                                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                    dialog.dismiss();
                                    mDialog.show();
                                    mAdapter.getList().clear();
                                    mAdapter.notifyDataSetChanged();
                                    solenoidGetCtlList();
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

    /**
     * 发送请求
     *
     * @param commandType 命令类型
     * @param result      修改内容
     */
    private void socketRequest(int commandType, Object result) {
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

    private void solenoidGetCtlList() {
        if (Data.userResult != null) {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"gwID", pGhList.get(mPosition)[0]});
            data.add(new String[]{"pageIndex", "1"});
            data.add(new String[]{"pageSize", "100"});
            data.add(new String[]{"userID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"authCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("SolenoidGetCtlList",
                    "/StringService/DeviceSet.asmx", data, 1, context);
        } else {
            mDialog.dismiss();
            Toasts.showError(context, getString(R.string.request_error9));
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

    private void parseResult1(String s) {
        try {
            SolGetCtlListResult result = new Gson().fromJson(s, SolGetCtlListResult.class);
            if (result.isSuccess() && result.getData().size() > 0) {
                mAdapter.getList().clear();
                for (int i = 0; i < result.getData().size(); i++) {
                    mAdapter.getList().add(new String[]{
                            result.getData().get(i).getDisplayNo(),
                            result.getData().get(i).getGreenhouseName()
                    });
                }
                mAdapter.notifyDataSetChanged();
            } else {
                Toasts.showInfo(context, getString(R.string.request_error8));
            }
        } catch (Exception e) {
            Toasts.showInfo(context, getString(R.string.request_error2));
        }
        mDialog.dismiss();
    }

    private void parseResult() {
        if (pGhList != null && pGhList.size() > 0) {
            mPosition = 0;
            tv_terminal.setText(pGhList.get(mPosition)[1]);
            if (pGhList.get(mPosition)[6].equals("1")) {
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
}
