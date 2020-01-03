package com.cdzp.huinongyun.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.bean.GreenHouseSelResult;
import com.cdzp.huinongyun.bean.SocketResult;
import com.cdzp.huinongyun.message.SocketMessage;
import com.cdzp.huinongyun.message.WebMessage;
import com.cdzp.huinongyun.util.Data;
import com.cdzp.huinongyun.util.Operate;
import com.cdzp.huinongyun.util.Toasts;
import com.cdzp.huinongyun.util.WebServiceManage;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
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
 * 作者：张人文
 * 时间：2019/10/14 16:30
 * 邮箱：479696877@QQ.COM
 * 描述：移动喷灌机主体控制
 */
public class SprinklerFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Context context;
    private boolean isVisible, isInit = false;
    private QMUITipDialog mDialog;
    private QMUIBottomSheet mBottomSheet2;
    private int mPosition = -1, conTypeMark;
    private TextView tv_terminal, tv_current_location, tv_error_code, tv_run;
    private ImageView iv_water, iv_before, iv_below, iv_speed_one, iv_speed_two,
            iv_speed_three, iv_fertilization, iv_section;
    private Button btn_water, btn_before, btn_below, btn_speed_one, btn_speed_two,
            btn_speed_three, btn_section, btn_fertilization, btn_stop;
    private EditText et_start, et_end;
    private CountDownTimer mDownTimer, mDownTimer1;
    private TimerTask task;
    private Timer timer;
    private boolean isRequest;
    private long outputCoilStatus, inputCoilStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_sprinkler, container, false);
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

        mDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();


        task = new TimerTask() {
            @Override
            public void run() {
                if (isVisible && isRequest) {
                    isRequest = false;
//                    Log.e("", "------------------------------------------------------------ ");
                    getGreenhouseUserDev();
                }
            }
        };
        timer = new Timer();
        mDownTimer = new CountDownTimer(1000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                Log.e("", "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");
                getGreenhouseUserDev();
            }

            @Override
            public void onFinish() {
                mDialog.dismiss();
            }
        };
        mDownTimer1 = new CountDownTimer(5000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                Log.e("", "1111111111111111111111111111111111111111111111111111111111 ");
            }

            @Override
            public void onFinish() {
//                Log.e("", "0000000000000000000000000000000000000000000000000000000000000 ");
                isRequest = true;
            }
        };

        timer.schedule(task, 0, 5000);
        tv_terminal = view.findViewById(R.id.tv_fdc_terminal);
        tv_run = view.findViewById(R.id.tv_run);
        btn_stop = view.findViewById(R.id.btn_stop);
        btn_fertilization = view.findViewById(R.id.btn_fertilization);
        iv_water = view.findViewById(R.id.iv_water);
        iv_before = view.findViewById(R.id.iv_before);
        iv_below = view.findViewById(R.id.iv_below);
        iv_speed_one = view.findViewById(R.id.iv_speed_one);
        iv_fertilization = view.findViewById(R.id.iv_fertilization);
        iv_speed_two = view.findViewById(R.id.iv_speed_two);
        iv_speed_three = view.findViewById(R.id.iv_speed_three);
        iv_section = view.findViewById(R.id.iv_section);
        btn_water = view.findViewById(R.id.btn_water);
        btn_before = view.findViewById(R.id.btn_before);
        btn_below = view.findViewById(R.id.btn_below);
        btn_speed_one = view.findViewById(R.id.btn_speed_one);
        btn_speed_two = view.findViewById(R.id.btn_speed_two);
        btn_speed_three = view.findViewById(R.id.btn_speed_three);
        btn_section = view.findViewById(R.id.btn_section);
        et_start = view.findViewById(R.id.et_start);
        et_end = view.findViewById(R.id.et_end);
        tv_current_location = view.findViewById(R.id.tv_current_location);
        tv_error_code = view.findViewById(R.id.tv_error_code);
        view.findViewById(R.id.ll_fdc).setOnClickListener(this);
        btn_water.setOnClickListener(this);
        btn_fertilization.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_before.setOnClickListener(this);
        btn_below.setOnClickListener(this);
        btn_speed_one.setOnClickListener(this);
        btn_speed_two.setOnClickListener(this);
        btn_speed_three.setOnClickListener(this);
        btn_section.setOnClickListener(this);
        if (!isInit && isVisible) {
            isInit = true;
//            mDialog.show();
            registered();
            initData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SocketResult result) {
        Log.e("", "返回成功: ");
        if (result.isOK()) {
            switch (result.getLabel()) {
                case 451:
                    switch (speed) {
                        case 1:
                            Log.e("", "onMessageEvent: 速度一");
                            isRequest = false;
                            mDownTimer1.cancel();
                            mDownTimer1.start();
                            iv_speed_one.setBackground(context.getResources().getDrawable(R.drawable.fer_green_dot));
                            iv_speed_two.setBackground(context.getResources().getDrawable(R.drawable.fer_red_dot));
                            iv_speed_three.setBackground(context.getResources().getDrawable(R.drawable.fer_red_dot));
                            break;
                        case 2:
                            Log.e("", "onMessageEvent: 速度二");
                            isRequest = false;
                            mDownTimer1.cancel();
                            mDownTimer1.start();
                            iv_speed_one.setBackground(context.getResources().getDrawable(R.drawable.fer_red_dot));
                            iv_speed_two.setBackground(context.getResources().getDrawable(R.drawable.fer_green_dot));
                            iv_speed_three.setBackground(context.getResources().getDrawable(R.drawable.fer_red_dot));
                            break;
                        case 3:
                            Log.e("", "onMessageEvent: 速度三");
                            isRequest = false;
                            mDownTimer1.cancel();
                            mDownTimer1.start();
                            iv_speed_one.setBackground(context.getResources().getDrawable(R.drawable.fer_red_dot));
                            iv_speed_two.setBackground(context.getResources().getDrawable(R.drawable.fer_red_dot));
                            iv_speed_three.setBackground(context.getResources().getDrawable(R.drawable.fer_green_dot));
                            break;
                        default:
                            break;

                    }
                    break;
                case 155:
                    Log.e("", "onMessageEvent: 前进  后退   施肥 返回   155" + result.getOutputCoilStatus());
                    isRequest = false;
                    mDownTimer1.cancel();
                    mDownTimer1.start();
                    outputCoilStatus = result.getOutputCoilStatus();
                    updateDate(outputCoilStatus);
                    break;
                case 454:
                    isRequest = false;
                    mDownTimer1.cancel();
                    mDownTimer1.start();
                    switch (section) {
                        case 1:
                            Log.e("", ": 停止区间成功返回");
                            inputCoilStatus = (inputCoilStatus & ~((long) 0xff << 16));
                            inputCoilStatus = inputCoilStatus | ((long) 0x00 << 16);
                            iv_section.setBackground(context.getResources().getDrawable(R.drawable.fer_red_dot));
                            tv_run.setVisibility(View.GONE);
                            break;
                        case 2:
                            Log.e("", ": 开始区间成功返回");
                            inputCoilStatus = (inputCoilStatus & ~((long) 0xff << 16));
                            inputCoilStatus = inputCoilStatus | ((long) 0x01 << 16);
                            iv_section.setBackground(context.getResources().getDrawable(R.drawable.fer_green_dot));
                            tv_run.setVisibility(View.VISIBLE);
                            break;
                    }
                    section = 3;
                    break;
                case 456:
                    Toasts.showSuccess(context, "停止成功！");
                    isRequest = false;
                    mDownTimer1.cancel();
                    mDownTimer1.start();
                    outputCoilStatus = result.getOutputCoilStatus();
                    updateDate(outputCoilStatus);
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
//        Log.e("", "onMessageEvent:  数据返回数字  " + message.getLabel());
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
        if (pGhList != null && pGhList.size() > 0) {
            mPosition = 0;
            tv_terminal.setText(pGhList.get(mPosition)[1]);
            mDownTimer.cancel();
            mDialog.dismiss();
            mDownTimer.start();
        } else {
            mDialog.dismiss();
            Toasts.showInfo(context, getString(R.string.request_error8));
        }

    }


    private void parseGHUD(String s) {
        try {
            GreenHouseSelResult result = new Gson().fromJson(s, GreenHouseSelResult.class);
            if (result.isSuccess()) {
                if (result.getData() != null) {
                    if (result.getData().get(0).getPGhList() != null) {
//                        Log.e("", "1    : " + result.getData().get(0).getPGhList().size());
//                        Log.e("", "2    : " + result.getData().get(0).getPGhList().get(0).getOutputCoilStatus());
//                        Log.e("", "3   : " + result.getData().get(0).getPGhList().get(0).getInputCoilStatus());

                        long temOutputCoilStatus = Long.parseLong(result.getData().get(0).getPGhList().get(0).getOutputCoilStatus());
                        outputCoilStatus = temOutputCoilStatus;
                        inputCoilStatus = Long.parseLong(result.getData().get(0).getPGhList().get(0).getInputCoilStatus());

                        updateDate(temOutputCoilStatus);
                        mDialog.dismiss();
                    }
                }
            }
            isRequest = true;
        } catch (Exception e) {

        }
    }

    private void updateDate(long temOutputCoilStatus) {
        if ((temOutputCoilStatus & 1) != 0) {
            iv_water.setBackground(context.getResources().getDrawable(R.drawable.fer_green_dot));
        } else {
            iv_water.setBackground(context.getResources().getDrawable(R.drawable.fer_red_dot));
        }
        if ((temOutputCoilStatus & 1 << 1) != 0) {
            iv_before.setBackground(context.getResources().getDrawable(R.drawable.fer_green_dot));
        } else {
            iv_before.setBackground(context.getResources().getDrawable(R.drawable.fer_red_dot));
        }
        if ((temOutputCoilStatus & 1 << 2) != 0) {
            iv_below.setBackground(context.getResources().getDrawable(R.drawable.fer_green_dot));
        } else {
            iv_below.setBackground(context.getResources().getDrawable(R.drawable.fer_red_dot));
        }
        if ((temOutputCoilStatus & 1 << 3) != 0) {
            iv_speed_one.setBackground(context.getResources().getDrawable(R.drawable.fer_green_dot));
        } else {
            iv_speed_one.setBackground(context.getResources().getDrawable(R.drawable.fer_red_dot));
        }
        if ((temOutputCoilStatus & 1 << 4) != 0) {
            iv_speed_two.setBackground(context.getResources().getDrawable(R.drawable.fer_green_dot));
        } else {
            iv_speed_two.setBackground(context.getResources().getDrawable(R.drawable.fer_red_dot));
        }
        if ((temOutputCoilStatus & 1 << 5) != 0) {
            iv_speed_three.setBackground(context.getResources().getDrawable(R.drawable.fer_green_dot));
        } else {
            iv_speed_three.setBackground(context.getResources().getDrawable(R.drawable.fer_red_dot));
        }
        Log.e("", "updateDate:     "+(temOutputCoilStatus & 1 << 8) );
        if ((temOutputCoilStatus & 1 << 8) != 0) {
            iv_fertilization.setBackground(context.getResources().getDrawable(R.drawable.fer_green_dot));
        } else {
            iv_fertilization.setBackground(context.getResources().getDrawable(R.drawable.fer_red_dot));
        }
        if ((inputCoilStatus & ((long) 0xff << 16)) > 0) {//同时为1  证明是打开状态
            iv_section.setBackground(context.getResources().getDrawable(R.drawable.fer_green_dot));
            tv_run.setVisibility(View.VISIBLE);
        } else {
            iv_section.setBackground(context.getResources().getDrawable(R.drawable.fer_red_dot));
            tv_run.setVisibility(View.GONE);
        }
        float f = (float) (inputCoilStatus & ((long) 0xffff)) / 100;
        tv_current_location.setTextColor(context.getResources().getColor(R.color.title));
        tv_current_location.setText("当前位置：" + f + "米");
        if ((inputCoilStatus & ((long) 0xff << 24)) > 0) {
            tv_error_code.setTextColor(context.getResources().getColor(R.color.homewarn));
            tv_error_code.setText("报警编号：" + (int) (inputCoilStatus >> 24));

            ScaleAnimation scaleAnimation = new ScaleAnimation(1, 2, 1,
                    2, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(1000);
            scaleAnimation.setFillAfter(true);
            tv_error_code.startAnimation(scaleAnimation);

        } else {
            tv_error_code.setTextColor(context.getResources().getColor(R.color.title));
            tv_error_code.setText("当前无报警状态");

//            Log.e("", "updateDate: " + animator.isRunning());

        }
    }

    private void initData() {
        if (pGhList == null) {
            getGreenHouseSel();
        } else {
            if (pGhList.size() > 0) {
                mPosition = 0;
                tv_terminal.setText(pGhList.get(mPosition)[1]);
                mDownTimer.cancel();
                mDownTimer.start();
            } else Toasts.showError(context, getString(R.string.request_error8));
        }
    }

    private int speed = 0;//速度标签
    private int section = 3;//停止开始区间标记

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fdc:
                showBottomSheet();
                break;
            case R.id.btn_water:
                if ((outputCoilStatus & 1) > 0) {
                    Log.e("", "浇水: 关");
                    socketRequest(154, null, new long[]{1, 1});
                } else {
                    Log.e("", "浇水: 开");
                    socketRequest(153, null, new long[]{1, 1});
                }
                break;
            case R.id.btn_before:
                if ((outputCoilStatus & 1 << 1) > 0) {
                    Log.e("", "前进: 关");
                    socketRequest(154, null, new long[]{1, 2});
                } else {
                    Log.e("", "前进: 开");
                    socketRequest(153, null, new long[]{1, 2});
                }
                break;
            case R.id.btn_below:
                if ((outputCoilStatus & 1 << 2) > 0) {
                    Log.e("", "后退: 关");
                    socketRequest(154, null, new long[]{1, 4});
                } else {
                    Log.e("", "后退: 开");
                    socketRequest(153, null, new long[]{1, 4});
                }
                break;
            case R.id.btn_speed_one:
                Log.e("", "速度: 一按钮点击");
                socketRequest(450, 0, new long[]{0, 0});
                speed = 1;
                break;
            case R.id.btn_speed_two:
                Log.e("", "速度: 二按钮点击");
                socketRequest(450, 1, new long[]{0, 0});
                speed = 2;
                break;
            case R.id.btn_speed_three:
                Log.e("", "速度: 三按钮点击");
                socketRequest(450, 2, new long[]{0, 0});
                speed = 3;
                break;
            case R.id.btn_stop:
//                socketRequest(455, 0, new long[]{0, 0});
                Log.e("", "停止 按钮 点击");
                socketRequest(455, null, new long[]{1, 1 << 5});
                break;
            case R.id.btn_fertilization:
                if ((outputCoilStatus & (((long) 0x01 << 8))) > 0) {
                    Log.e("", "施肥按钮: 开");
                    socketRequest(154, null, new long[]{1, 1 << 8});
                } else {
                    Log.e("", "施肥按钮: 关");
                    socketRequest(153, null, new long[]{1, 1 << 8});
                }
                break;

            case R.id.btn_section:
                if ("".equals(et_start.getText().toString())) {
                    Toasts.showInfo(context, "请输入开始位置");
                } else if ("".equals(et_end.getText().toString())) {
                    Toasts.showInfo(context, "请输入结束位置");
                } else if (Float.parseFloat(et_start.getText().toString()) >= Float.parseFloat(et_end.getText().toString())) {
                    Toasts.showInfo(context, "结束位置必须大于开始位置哟！");
                } else {
                    if ((inputCoilStatus & ((long) 0xff << 16)) > 0) {//同时为1  证明是打开状态

                        section = 1;//发送关闭命令
                        socketRequest(453, new float[]{Float.parseFloat(et_start.getText().toString()),
                                Float.parseFloat(et_end.getText().toString()), 0, 0, 0, 0}, new long[]{0, 0});
                        Log.e("", "发送: 停止区间命令完成");
                    } else {
                        section = 2;//发送打开命令
                        socketRequest(452, new float[]{Float.parseFloat(et_start.getText().toString()),
                                Float.parseFloat(et_end.getText().toString()), 0, 0, 0, 0}, new long[]{0, 0});
                        Log.e("", "发送: 开始区间命令完成");
                    }
                }
                break;
            default:
                break;
        }
    }


    private void showBottomSheet() {
        if (pGhList != null) {
            if (pGhList.size() > 0) {
                if (mBottomSheet2 == null) {
                    QMUIBottomSheet.BottomListSheetBuilder mBuilder2 = new QMUIBottomSheet.BottomListSheetBuilder(context, true)
                            .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                                @Override
                                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                    if (pGhList.get(position)[6].equals("1")) {
                                        isRequest = false;
                                        WebServiceManage.dispose();
                                        mDownTimer.cancel();
                                        mPosition = position;
                                        tv_terminal.setText(pGhList.get(mPosition)[1]);
                                        conTypeMark = 0;
                                        dialog.dismiss();
//                                        mDialog.show();
                                        mDownTimer.start();
                                    } else {
                                        mDownTimer.cancel();
                                        isRequest = false;
                                        WebServiceManage.dispose();
                                        mPosition = position;
                                        tv_terminal.setText(pGhList.get(mPosition)[1]);
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
            if (mPosition >= 0 && pGhList != null && pGhList.size() > mPosition) {
                message.setRequestType(3);
            } else return;
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

    /**
     * 请求机器列表
     */
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
