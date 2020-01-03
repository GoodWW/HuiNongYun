package com.cdzp.huinongyun.fragment;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.activity.FertActivity;
import com.cdzp.huinongyun.adapter.FerAdapter;
import com.cdzp.huinongyun.adapter.IrrAdapter;
import com.cdzp.huinongyun.bean.CurrentStateResult;
import com.cdzp.huinongyun.bean.PlainDetails;
import com.cdzp.huinongyun.bean.SocketResult;
import com.cdzp.huinongyun.message.SocketMessage;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.MyCallback;
import com.cdzp.huinongyun.util.Operate;
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
import java.util.Timer;
import java.util.TimerTask;

import static com.cdzp.huinongyun.util.Data.pGhList;

/**
 * 施肥机
 */
public class FertFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean isVisible, isInit, isRequest;
    private QMUITipDialog mDialog;
    private ImageView iv_fert1, iv_fert2, iv_acid1, iv_acid2, iv_acid3, iv_acid4, iv_acid5,
            iv_acid_valve1, iv_acid_valve2, iv_acid_valve3, iv_acid_valve4, iv_acid_valve5,
            iv_valve1, iv_valve2, iv_valve3, iv_valve4, iv_valve5, iv_valve6, iv_valve7, iv_valve8,
            iv_valve9, iv_valve10, iv_valve11, iv_valve12, iv_valve13, iv_valve14, iv_valve15, iv_valve16;
    private AnimationDrawable ad1, ad2;
    private TextView tv_terminal, tv_ec, tv_ph, tv_water, tv_fertilizer, tv_irrigation, tv_water_top,
            tv_mode, tv_fangshi, tv_water_value, tv_modes;
    private TimerTask task;
    private Timer timer;
    private LinearLayout ll_modes;
    private int mPosition, conTypeMark, mPosition2, mPosition3;
    private QMUIBottomSheet mBottomSheet1, mBottomSheet2, mBottomSheet3;
    private CountDownTimer mDownTimer, mDownTimer1;
    private IrrAdapter irradapter, valveadapter;
    private FerAdapter feradapter;
    private String[] FerStr;
    private boolean[] FerCheck = new boolean[5];
    private boolean[] check = new boolean[64];
    private AlertDialog IrrDialog = null, FerDialog = null, valveDialog;
    private boolean[] fertChshow;
    private int ValvesNum, type = -1, irrNum;
    private EditText et_water, et_ec, et_ph;
    private boolean isStart;//是否手动启动施肥机
    private Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_fert, container, false);
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
                if (tv_terminal.getText().length() > 0) {
                    isRequest = true;
                }
                if (!isInit) {
                    isInit = true;
                    initData();
                }
            }
        } else {
            if (mDownTimer1 != null) mDownTimer1.cancel();
            WebServiceManage.dispose();
            EventBus.getDefault().unregister(this);
            isRequest = false;
        }
    }

    private void initView() {
        context = getContext();
        mDialog = ((FertActivity) getActivity()).getmDialog();

        tv_water_top = view.findViewById(R.id.tv_fert_water_top);
        iv_fert1 = view.findViewById(R.id.iv_fert1);
        iv_fert2 = view.findViewById(R.id.iv_fert2);
        ad1 = (AnimationDrawable) iv_fert1.getBackground();
        ad2 = (AnimationDrawable) iv_fert2.getBackground();
        iv_acid1 = view.findViewById(R.id.iv_acid1);
        iv_acid2 = view.findViewById(R.id.iv_acid2);
        iv_acid3 = view.findViewById(R.id.iv_acid3);
        iv_acid4 = view.findViewById(R.id.iv_acid4);
        iv_acid5 = view.findViewById(R.id.iv_acid5);
        iv_acid_valve1 = view.findViewById(R.id.iv_acid_valve1);
        iv_acid_valve2 = view.findViewById(R.id.iv_acid_valve2);
        iv_acid_valve3 = view.findViewById(R.id.iv_acid_valve3);
        iv_acid_valve4 = view.findViewById(R.id.iv_acid_valve4);
        iv_acid_valve5 = view.findViewById(R.id.iv_acid_valve5);
        tv_ec = view.findViewById(R.id.tv_ec);
        tv_ph = view.findViewById(R.id.tv_ph);
        iv_valve1 = view.findViewById(R.id.iv_valve1);
        iv_valve2 = view.findViewById(R.id.iv_valve2);
        iv_valve3 = view.findViewById(R.id.iv_valve3);
        iv_valve4 = view.findViewById(R.id.iv_valve4);
        iv_valve5 = view.findViewById(R.id.iv_valve5);
        iv_valve6 = view.findViewById(R.id.iv_valve6);
        iv_valve7 = view.findViewById(R.id.iv_valve7);
        iv_valve8 = view.findViewById(R.id.iv_valve8);
        iv_valve9 = view.findViewById(R.id.iv_valve9);
        iv_valve10 = view.findViewById(R.id.iv_valve10);
        iv_valve11 = view.findViewById(R.id.iv_valve11);
        iv_valve12 = view.findViewById(R.id.iv_valve12);
        iv_valve13 = view.findViewById(R.id.iv_valve13);
        iv_valve14 = view.findViewById(R.id.iv_valve14);
        iv_valve15 = view.findViewById(R.id.iv_valve15);
        iv_valve16 = view.findViewById(R.id.iv_valve16);

        tv_terminal = view.findViewById(R.id.tv_fert_terminal);
        ll_modes = view.findViewById(R.id.ll_fert_modes);
        tv_water = view.findViewById(R.id.tv_fert_water);
        tv_water_value = view.findViewById(R.id.tv_fert_water_value);
        tv_mode = view.findViewById(R.id.tv_fert_mode);
        tv_fangshi = view.findViewById(R.id.tv_fert_fangshi);
        et_water = view.findViewById(R.id.et_fert_water);
        et_ec = view.findViewById(R.id.et_fert_ec);
        et_ph = view.findViewById(R.id.et_fert_ph);
        tv_fertilizer = view.findViewById(R.id.tv_fert_fertilizer);
        tv_irrigation = view.findViewById(R.id.tv_fert_irrigation);
        tv_modes = view.findViewById(R.id.tv_fert_modes);
        btn = view.findViewById(R.id.btn_fert_ok);

        task = new TimerTask() {
            @Override
            public void run() {
                if (isRequest) {
                    isRequest = false;
                    getCurrentState();
                }
            }
        };
        timer = new Timer();

        mDownTimer = new CountDownTimer(9000, 2500) {
            @Override
            public void onTick(long millisUntilFinished) {
                socketRequest(310, null, type);
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

        view.findViewById(R.id.ll_fert_terminal).setOnClickListener(this);
        view.findViewById(R.id.ll_fert_mode).setOnClickListener(this);
        view.findViewById(R.id.ll_fert_fangshi).setOnClickListener(this);
        view.findViewById(R.id.ll_fert_water).setOnClickListener(this);
        view.findViewById(R.id.btn_valve).setOnClickListener(this);
        tv_fertilizer.setOnClickListener(this);
        tv_irrigation.setOnClickListener(this);
        btn.setOnClickListener(this);


        timer.schedule(task, 0, 5000);
        if (isVisible && !isInit) {
            isInit = true;
            initData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fert_terminal:
                showBottomSheet1();
                break;
            case R.id.ll_fert_mode:
                showBottomSheet2();
                break;
            case R.id.ll_fert_water:
                if (tv_water.getText().toString().equals(getString(R.string.fert_str11))) {
                    mPosition2 = 2;
                    tv_water.setText(getString(R.string.fert_str12));
                    tv_water_value.setText("min");
                } else {
                    mPosition2 = 1;
                    tv_water.setText(getString(R.string.fert_str11));
                    tv_water_value.setText("m³");
                }
                break;
            case R.id.ll_fert_fangshi:
                showBottomSheet3();
                break;
            case R.id.tv_fert_fertilizer:
                showFer();
                break;
            case R.id.tv_fert_irrigation:
                showIrr();
                break;
            case R.id.btn_fert_ok:
                if (et_water.getText().length() > 0 && et_ec.getText().length() > 0 && et_ph.getText().length() > 0) {
                    if (isStart) {
                        mDialog.show();
                        socketRequest(312, null, -1);
                    } else {
                        submit();
                    }
                } else Toasts.showInfo(context, getString(R.string.request_error6));
                break;
            case R.id.btn_valve:
                if (valveDialog != null && !valveDialog.isShowing()) {
                    valveDialog.show();
                }
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
                    parseResult2(message.getmResult());
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
            conTypeMark = 0;
            switch (result.getLabel()) {
                case 319:
                    mDownTimer.cancel();
                    mDownTimer1.cancel();
                    WebServiceManage.dispose();
                    isRequest = false;
                    switch (type) {
                        case 0:
                            ll_modes.setVisibility(View.GONE);
                            tv_modes.setVisibility(View.VISIBLE);
                            tv_mode.setText(getString(R.string.fert_str21));
                            tv_modes.setText(getString(R.string.fert_str28));
                            break;
                        case 1:
                            ll_modes.setVisibility(View.VISIBLE);
                            tv_modes.setVisibility(View.GONE);
                            tv_mode.setText(getString(R.string.fert_str22));
                            isStart = false;
                            btn.setText(getString(R.string.fert_str16));
                            btn.setBackgroundResource(R.drawable.login_click_effect_in);
                            break;
                        case 2:
                            ll_modes.setVisibility(View.GONE);
                            tv_modes.setVisibility(View.VISIBLE);
                            tv_mode.setText(getString(R.string.fert_str23));
                            tv_modes.setText(getString(R.string.fert_str28));
                            break;
                    }
                    mDialog.dismiss();
                    Toasts.showSuccess(context, getString(R.string.socket_succ));
                    mDownTimer1.start();
                    break;
                case 320:
                    WebServiceManage.dispose();
                    isRequest = false;
                    isStart = true;
                    btn.setText(getString(R.string.fert_str20));
                    btn.setBackgroundResource(R.drawable.fert_onclick);
                    mDialog.dismiss();
                    Toasts.showSuccess(context, getString(R.string.fert_str6));
                    mDownTimer1.start();
                    break;
                case 321:
                    WebServiceManage.dispose();
                    isRequest = false;
                    isStart = false;
                    btn.setText(getString(R.string.fert_str16));
                    btn.setBackgroundResource(R.drawable.login_click_effect_in);
                    mDialog.dismiss();
                    Toasts.showSuccess(context, getString(R.string.fert_str7));
                    mDownTimer1.start();
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
            getTerminal();
        } else {
            if (pGhList.size() > 0) {
                mPosition = 0;
                tv_terminal.setText(pGhList.get(mPosition)[1]);
                getCurrentState();
            } else Toasts.showInfo(context, getString(R.string.warn_str10));
        }
    }

    /**
     * 发送请求
     *
     * @param commandType 命令类型
     * @param object      请求数据
     * @param fertType    施肥机控制方式
     */
    private void socketRequest(int commandType, Object object, int fertType) {
        if (Data.userResult != null && tv_terminal.getText().length() > 0) {
            SocketMessage message = new SocketMessage();
            message.setZP_KEY_CLIENT(987656789);
            message.setRequestType(308);
            message.setmSender(Data.userResult.getData().get(0).getUserName());
            message.setmReceiver(pGhList.get(mPosition)[2]);
            message.setCommandType(commandType);
            message.setResult(object);
            message.setFertType(fertType);

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
     * 获取值施肥机状态值
     */
    private void getCurrentState() {
        if (Data.userResult != null) {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"GreenhouseID", pGhList.get(mPosition)[0]});
            data.add(new String[]{"UserID", Data.userResult.getData().get(0).getUserID() + ""});
            data.add(new String[]{"AuthCode", Data.userResult.getData().get(0).getAuthCode()});
            WebServiceManage.requestData("GetCurrentState",
                    "/StringService/DeviceSet.asmx", data, 1, context);
        } else mDialog.dismiss();
    }

    private void submit() {
        mDialog.show();
        try {
            if (Double.parseDouble(et_water.getText().toString()) > 3000) {
                Toasts.showInfo(context, getString(R.string.fert_str29));
                return;
            }
            if (Double.parseDouble(et_ec.getText().toString()) > 5) {
                Toasts.showInfo(context, getString(R.string.fert_str30));
                return;
            }
            if (Double.parseDouble(et_ph.getText().toString()) > 9 || Double.parseDouble(et_ph.getText().toString()) < 4) {
                Toasts.showInfo(context, getString(R.string.fert_str31));
                return;
            }
            PlainDetails pd = new PlainDetails();
            pd.setPlainName("");
            pd.setPlainFirstDay("");
            pd.setPlainEndDay("");
            pd.setStartTime("");
            pd.setStopTime("");
            if (tv_fangshi.getText().toString().equals(getString(R.string.fert_str14))) {
                pd.setPlainMode((byte) 0);
                long IrrigationValve = 0;
                for (int i = 64; i > 0; i--) {
                    IrrigationValve = IrrigationValve << 1;
                    if (check[i - 1])
                        IrrigationValve = IrrigationValve + 1;
                }
                pd.setIrrigationValve(IrrigationValve);
            } else {
                pd.setPlainMode((byte) 1);
                int FerCh = 0;
                for (int i = 5; i > 0; i--) {
                    FerCh = FerCh << 1;
                    if (FerCheck[i - 1])
                        FerCh = FerCh + 1;
                }
                pd.setFerCh((byte) FerCh);

                long IrrigationValve = 0;
                for (int i = 64; i > 0; i--) {
                    IrrigationValve = IrrigationValve << 1;
                    if (check[i - 1])
                        IrrigationValve = IrrigationValve + 1;
                }
                pd.setIrrigationValve(IrrigationValve);
            }
            pd.setECSetValue((short) (Double.parseDouble(et_ec.getText().toString()) * 1000));
            pd.setPHSetValue((short) (Double.parseDouble(et_ph.getText().toString()) * 100));
            if (tv_water.getText().toString().equals(getString(R.string.fert_str11))) {
                pd.setWaterQuantityCtlMode((byte) 0);
                short s = (short) (Double.parseDouble(et_water.getText().toString()) * 100);
                pd.setWaterCtlValue(s);
            } else {
                pd.setWaterQuantityCtlMode((byte) 1);
                pd.setWaterCtlValue((short) Double.parseDouble(et_water.getText().toString()));
            }
            socketRequest(311, pd, -1);
        } catch (Exception e) {
            mDialog.dismiss();
            Toasts.showInfo(context, getString(R.string.fert_str32));
        }
    }

    private void showFer() {
        if (tv_fangshi.getText().toString().equals(getString(R.string.fert_str13))) {
            if (FerDialog != null) {
                if (!FerDialog.isShowing()) {
                    FerDialog.show();
                }
            } else Toasts.showInfo(context, getString(R.string.fert_str5));
        }
    }

    private void showIrr() {
        if (IrrDialog != null) {
            if (!IrrDialog.isShowing()) {
                IrrDialog.show();
            }
        } else Toasts.showInfo(context, getString(R.string.fert_str5));
    }

    private void parseResult2(String s) {
        try {
            CurrentStateResult result = new Gson().fromJson(s, CurrentStateResult.class);
            if (result.isSuccess() && result.getData() != null) {
                if ((result.getData().getPumpState() & 1) > 0) {
                    ad1.start();
                } else ad1.stop();
                if ((result.getData().getPumpState() & 2) > 0) {
                    ad2.start();
                } else ad2.stop();
                for (int i = 0; i < 6; i++) {
                    if ((result.getData().getLiquidLevel() & (1 << i)) != 0) {
                        setLiquidLevel(true, i);
                    } else {
                        setLiquidLevel(false, i);
                    }
                }
                fertChshow = new boolean[5];
                for (int i = 0; i < 5; i++) {
                    if ((result.getData().getFertCh() & (1 << i)) != 0) {
                        fertChshow[i] = true;
                    } else {
                        fertChshow[i] = false;
                    }
                }
                for (int i = 0; i < 5; i++) {
                    if ((result.getData().getFerChState() & (1 << i)) != 0) {
                        setFerChState(true, i);
                    } else {
                        setFerChState(false, i);
                    }
                }
                ValvesNum = result.getData().getValvesNum();
                boolean[] mCheck = new boolean[ValvesNum];//阀门状态

                for (int i = 0; i < ValvesNum; i++) {
                    if ((result.getData().getIrrValvesState() & (1 << i)) != 0) {
                        setIrrValvesState(true, i);
                        mCheck[i] = true;
                    } else {
                        setIrrValvesState(false, i);
                        mCheck[i] = false;
                    }
                }

                if (valveDialog == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View contentView = LayoutInflater.from(context).inflate(R.layout.irrlayout, null);
                    RecyclerView mRecyclerView = contentView.findViewById(R.id.irr);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(context, 6));
                    valveadapter = new IrrAdapter(mCheck);
                    valveadapter.setValvesNum(ValvesNum);
                    valveadapter.setValve(true);
                    mRecyclerView.setAdapter(valveadapter);
                    builder.setTitle(getString(R.string.fert_str10));
                    builder.setView(contentView);
                    builder.setPositiveButton(getString(R.string.personal_str6), null);
                    valveDialog = builder.create();
                } else {
                    valveadapter.setValvesNum(ValvesNum);
                    valveadapter.setCheck(mCheck);
                    valveadapter.notifyDataSetChanged();
                }


                tv_ec.setText("EC=" + result.getData().getEC());
                tv_ph.setText("PH=" + result.getData().getPH());

                switch (result.getData().getCtlMod()) {
                    case 0:
                        type = 0;
                        ll_modes.setVisibility(View.GONE);
                        tv_modes.setVisibility(View.VISIBLE);
                        tv_mode.setText(getString(R.string.fert_str21));
                        if (result.getData().getRunStop() > 0) {
                            tv_modes.setText(getString(R.string.fert_str24) + " " + result.getData().getName());
                        } else tv_modes.setText(getString(R.string.fert_str25));
                        break;
                    case 1:
                        type = 1;
                        ll_modes.setVisibility(View.VISIBLE);
                        tv_modes.setVisibility(View.GONE);
                        tv_mode.setText(getString(R.string.fert_str22));
                        if (result.getData().getRunStop() == 1) {
                            isStart = true;
                            btn.setText(getString(R.string.fert_str20));
                            btn.setBackgroundResource(R.drawable.fert_onclick);
                        } else {
                            isStart = false;
                            btn.setText(getString(R.string.fert_str16));
                            btn.setBackgroundResource(R.drawable.login_click_effect_in);
                        }
                        break;
                    case 2:
                        type = 2;
                        ll_modes.setVisibility(View.GONE);
                        tv_modes.setVisibility(View.VISIBLE);
                        tv_mode.setText(getString(R.string.fert_str23));
                        if (result.getData().getRunStop() > 0) {
                            tv_modes.setText(getString(R.string.fert_str26) + " " + result.getData().getName());
                        } else tv_modes.setText(getString(R.string.fert_str27));
                        break;
                }

                if (IrrDialog == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View contentView = LayoutInflater.from(context).inflate(R.layout.irrlayout, null);
                    RecyclerView mRecyclerView = contentView.findViewById(R.id.irr);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(context, 6));
                    check = new boolean[64];
                    irradapter = new IrrAdapter(check);
                    irradapter.setCallback(new MyCallback() {
                        @Override
                        public void checkBox(int position, boolean b) {
                            check[position] = b;
                            if (b) {
                                irrNum++;
                            } else {
                                irrNum--;
                            }
                            tv_irrigation.setText(getString(R.string.fert_str44) + irrNum + getString(R.string.fert_str45));
                        }
                    });
                    irradapter.setValvesNum(ValvesNum);
                    mRecyclerView.setAdapter(irradapter);
                    builder.setTitle(getString(R.string.fert_str17));
                    builder.setView(contentView);
                    builder.setPositiveButton(getString(R.string.personal_str6), null);
                    IrrDialog = builder.create();

                    tv_irrigation.setText("");
                }

                if (FerDialog == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View contentView = LayoutInflater.from(context).inflate(R.layout.irrlayout, null);
                    RecyclerView mRecyclerView = contentView.findViewById(R.id.irr);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(context, 5));
                    FerStr = new String[]{getString(R.string.fert_str19), "A", "B", "C", "D"};
                    FerCheck = new boolean[5];
                    feradapter = new FerAdapter(FerCheck, context);
                    feradapter.setCallback(new MyCallback() {
                        @Override
                        public void checkBox(int position, boolean b) {
                            FerCheck[position] = b;
                            String str = "";
                            for (int i = 0; i < FerCheck.length; i++) {
                                if (FerCheck[i]) {
                                    str = str + FerStr[i] + "、";
                                }
                            }
                            if (str.length() > 0) {
                                tv_fertilizer.setText(str.substring(0, str.length() - 1));
                            } else tv_fertilizer.setText("");
                        }
                    });
                    feradapter.setFertChshow(fertChshow);
                    mRecyclerView.setAdapter(feradapter);
                    builder.setTitle(getString(R.string.fert_str18));
                    builder.setView(contentView);
                    builder.setPositiveButton(getString(R.string.personal_str6), null);
                    FerDialog = builder.create();
                }

            } else {

            }
        } catch (Exception e) {
            Log.d("aa", "");
        }
        mDialog.dismiss();
        isRequest = true;
    }

    private void setIrrValvesState(boolean b, int i) {
        switch (i) {
            case 0:
                if (b) {
                    iv_valve1.setBackgroundResource(R.drawable.v_open);
                } else iv_valve1.setBackgroundResource(R.drawable.valve_up_close1);
                break;
            case 1:
                if (b) {
                    iv_valve2.setBackgroundResource(R.drawable.v_open);
                } else iv_valve2.setBackgroundResource(R.drawable.valve_up_close1);
                break;
            case 2:
                if (b) {
                    iv_valve3.setBackgroundResource(R.drawable.v_open);
                } else iv_valve3.setBackgroundResource(R.drawable.valve_up_close1);
                break;
            case 3:
                if (b) {
                    iv_valve4.setBackgroundResource(R.drawable.v_open);
                } else iv_valve4.setBackgroundResource(R.drawable.valve_up_close1);
                break;
            case 4:
                if (b) {
                    iv_valve5.setBackgroundResource(R.drawable.v_open);
                } else iv_valve5.setBackgroundResource(R.drawable.valve_up_close1);
                break;
            case 5:
                if (b) {
                    iv_valve6.setBackgroundResource(R.drawable.v_open);
                } else iv_valve6.setBackgroundResource(R.drawable.valve_up_close1);
                break;
            case 6:
                if (b) {
                    iv_valve7.setBackgroundResource(R.drawable.v_open);
                } else iv_valve7.setBackgroundResource(R.drawable.valve_up_close1);
                break;
            case 7:
                if (b) {
                    iv_valve8.setBackgroundResource(R.drawable.v_open);
                } else iv_valve8.setBackgroundResource(R.drawable.valve_up_close1);
                break;
            case 8:
                if (b) {
                    iv_valve9.setBackgroundResource(R.drawable.v_open);
                } else iv_valve9.setBackgroundResource(R.drawable.valve_up_close1);
                break;
            case 9:
                if (b) {
                    iv_valve10.setBackgroundResource(R.drawable.v_open);
                } else iv_valve10.setBackgroundResource(R.drawable.valve_up_close1);
                break;
            case 10:
                if (b) {
                    iv_valve11.setBackgroundResource(R.drawable.v_open);
                } else iv_valve11.setBackgroundResource(R.drawable.valve_up_close1);
                break;
            case 11:
                if (b) {
                    iv_valve12.setBackgroundResource(R.drawable.v_open);
                } else iv_valve12.setBackgroundResource(R.drawable.valve_up_close1);
                break;
            case 12:
                if (b) {
                    iv_valve13.setBackgroundResource(R.drawable.v_open);
                } else iv_valve13.setBackgroundResource(R.drawable.valve_up_close1);
                break;
            case 13:
                if (b) {
                    iv_valve14.setBackgroundResource(R.drawable.v_open);
                } else iv_valve14.setBackgroundResource(R.drawable.valve_up_close1);
                break;
            case 14:
                if (b) {
                    iv_valve15.setBackgroundResource(R.drawable.v_open);
                } else iv_valve15.setBackgroundResource(R.drawable.valve_up_close1);
                break;
            case 15:
                if (b) {
                    iv_valve16.setBackgroundResource(R.drawable.v_open);
                } else iv_valve16.setBackgroundResource(R.drawable.valve_up_close1);
                break;
        }
    }

    private void setFerChState(boolean b, int i) {
        switch (i) {
            case 0:
                if (b) {
                    iv_acid_valve1.setBackgroundResource(R.drawable.h_open);
                } else iv_acid_valve1.setBackgroundResource(R.drawable.valve_up_close);
                break;
            case 1:
                if (b) {
                    iv_acid_valve2.setBackgroundResource(R.drawable.h_open);
                } else iv_acid_valve2.setBackgroundResource(R.drawable.valve_up_close);
                break;
            case 2:
                if (b) {
                    iv_acid_valve3.setBackgroundResource(R.drawable.h_open);
                } else iv_acid_valve3.setBackgroundResource(R.drawable.valve_up_close);
                break;
            case 3:
                if (b) {
                    iv_acid_valve4.setBackgroundResource(R.drawable.h_open);
                } else iv_acid_valve4.setBackgroundResource(R.drawable.valve_up_close);
                break;
            case 4:
                if (b) {
                    iv_acid_valve5.setBackgroundResource(R.drawable.h_open);
                } else iv_acid_valve5.setBackgroundResource(R.drawable.valve_up_close);
                break;
        }
    }

    private void setLiquidLevel(boolean b, int i) {
        switch (i) {
            case 0:
                if (b) {
                    iv_acid1.setBackgroundResource(R.drawable.bucket_green);
                } else iv_acid1.setBackgroundResource(R.drawable.bucket_red);
                break;
            case 1:
                if (b) {
                    iv_acid2.setBackgroundResource(R.drawable.bucket_green);
                } else iv_acid2.setBackgroundResource(R.drawable.bucket_red);
                break;
            case 2:
                if (b) {
                    iv_acid3.setBackgroundResource(R.drawable.bucket_green);
                } else iv_acid3.setBackgroundResource(R.drawable.bucket_red);
                break;
            case 3:
                if (b) {
                    iv_acid4.setBackgroundResource(R.drawable.bucket_green);
                } else iv_acid4.setBackgroundResource(R.drawable.bucket_red);
                break;
            case 4:
                if (b) {
                    iv_acid5.setBackgroundResource(R.drawable.bucket_green);
                } else iv_acid5.setBackgroundResource(R.drawable.bucket_red);
                break;
            case 5:
                if (b) {
                    tv_water_top.setText(getString(R.string.fert_str8));
                } else tv_water_top.setText(getString(R.string.fert_str9));
                break;
        }
    }

    private void parseResult() {
        if (pGhList != null && pGhList.size() > 0) {
            mPosition = 0;
            tv_terminal.setText(pGhList.get(mPosition)[1]);
            getCurrentState();
        } else {
            mDialog.dismiss();
            Toasts.showInfo(context, getString(R.string.warn_str10));
        }
    }

    private void showBottomSheet1() {
        if (pGhList != null && pGhList.size() > 0) {
            if (mBottomSheet1 == null) {
                BottomListSheetBuilder mBuilder = new BottomListSheetBuilder(context, true)
                        .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                mDownTimer1.cancel();
                                isRequest = false;
                                mDownTimer.cancel();
                                WebServiceManage.dispose();
                                mPosition = position;
                                tv_terminal.setText(pGhList.get(mPosition)[1]);
                                dialog.dismiss();
                                mDialog.show();
                                IrrDialog = null;
                                FerDialog = null;
                                valveDialog = null;
                                getCurrentState();
                                mDownTimer1.start();
                            }
                        });
                for (int i = 0; i < pGhList.size(); i++) {
                    String str = pGhList.get(i)[1];
                    if (str.contains(getString(R.string.dev_str2))) break;
                    mBuilder.addItem(str);
                }
                mBuilder.setCheckedIndex(mPosition);
                mBottomSheet1 = mBuilder.build();
            }
            mBottomSheet1.show();
        } else Toasts.showInfo(context, getString(R.string.request_error8));
    }

    private void showBottomSheet2() {
        if (mBottomSheet2 == null) {
            BottomListSheetBuilder mBuilder = new BottomListSheetBuilder(context)
                    .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                        @Override
                        public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                            type = position;
                            mDownTimer1.cancel();
                            mDownTimer.cancel();
                            WebServiceManage.dispose();
                            isRequest = false;
                            dialog.dismiss();
                            mDialog.show();
                            mDownTimer.start();
                        }
                    });
            mBuilder.addItem(getString(R.string.fert_str21));
            mBuilder.addItem(getString(R.string.fert_str22));
            mBuilder.addItem(getString(R.string.fert_str23));
            mBottomSheet2 = mBuilder.build();
        }
        mBottomSheet2.show();
    }

    private void showBottomSheet3() {
        if (mBottomSheet3 == null) {
            BottomListSheetBuilder mBuilder = new BottomListSheetBuilder(context, true)
                    .setOnSheetItemClickListener(new BottomListSheetBuilder.OnSheetItemClickListener() {
                        @Override
                        public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                            switch (position) {
                                case 0:
                                    mPosition3 = 0;
                                    tv_fangshi.setText(getString(R.string.fert_str13));
                                    break;
                                case 1:
                                    mPosition3 = 1;
                                    tv_fangshi.setText(getString(R.string.fert_str14));
                                    break;
                            }
                            dialog.dismiss();
                        }
                    });
            mBuilder.addItem(getString(R.string.fert_str13));
            mBuilder.addItem(getString(R.string.fert_str14));
            mBuilder.setCheckedIndex(0);
            mBottomSheet3 = mBuilder.build();
        }
        mBottomSheet3.show();
    }

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