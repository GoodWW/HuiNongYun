package com.cdzp.huinongyun.util;

import android.content.Context;
import android.util.Log;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.bean.AutoDetails;
import com.cdzp.huinongyun.bean.DevConAutoParam;
import com.cdzp.huinongyun.bean.DeviceInfo;
import com.cdzp.huinongyun.bean.FertPlainDetails;
import com.cdzp.huinongyun.bean.HelioGreAutoParam;
import com.cdzp.huinongyun.bean.HelioGreDeviceInfo;
import com.cdzp.huinongyun.bean.HelioGreParam;
import com.cdzp.huinongyun.bean.IrriRecord;
import com.cdzp.huinongyun.bean.OnlAutoCtlParamter;
import com.cdzp.huinongyun.bean.SetAutoParam;
import com.cdzp.huinongyun.bean.SetParamInfo;
import com.cdzp.huinongyun.bean.SocketResult;
import com.cdzp.huinongyun.bean.WVH_PlanParam;
import com.cdzp.huinongyun.bean.WvhAutoParam;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 数据接收类
 */

public class Receiver {
    private DatagramSocket socket;
    private boolean isResult = true;
    private Context context;
    private String TAG = "Receiver";
    private Disposable disposable;

    public Receiver(DatagramSocket socket, final Context context) {
        this.socket = socket;
        this.context = context;
        Observable.create(new ObservableOnSubscribe<SocketResult>() {
            @Override
            public void subscribe(ObservableEmitter<SocketResult> e) throws Exception {
                while (isResult) {
                    e.onNext(GetResult());
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<SocketResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(SocketResult socketResult) {
                        EventBus.getDefault().post(socketResult);
                    }

                    @Override
                    public void onError(Throwable e) {
                        SocketResult result = new SocketResult();
                        result.setRemark(context.getString(R.string.request_error7));
                        EventBus.getDefault().post(result);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public boolean isResult() {
        return isResult;
    }

    public void setResult(boolean result) {
        isResult = result;
    }

    private SocketResult GetResult() {
        SocketResult result = new SocketResult();//接受数据情况，返回给界面做处理
        byte[] inBuffer = new byte[1024 * 50];
        DatagramPacket inPacket = new DatagramPacket(inBuffer, 1024 * 50);
        ByteArrayInputStream block = null;
        DataInputStream in = null;
        try {
            socket.receive(inPacket);
            block = new ByteArrayInputStream(inPacket.getData());
            in = new DataInputStream(block);
//            Log.d("aa", "GetResult: 已接受到");
            int blockSize = in.readShort();
            int key = in.readInt();
            if (key == 123454321 || key == 987656789) {
                int requestType = in.readInt();
                result.setmSender(getString(in, result));
                result.setmReceiver(getString(in, result));
                int commandType = in.readInt();
                result.setLabel(commandType);
//                Log.d("aa", "响应：" + commandType);
                switch (requestType) {
                    case 3://温室
                        switch (commandType) {
                            case 155:
                                result.setOutputCoilStatus(in.readLong());
                                result.setLabel(155);
                                break;
                            case 156:
                                getSetAutoParam(in, result);
                                result.setLabel(156);
                                break;
                            case 158:
                                result.setLabel(158);
                                break;
                            case 160:
                                getDeviceInitData(in, result);
                                result.setLabel(160);
                                break;
                            case 161:
                                result.setLabel(161);
                                break;
                            case 456:
                                Log.e(TAG, "GetResult 456: 返回" );
                                result.setLabel(456);
                                break;
                            case 451:
                                Log.e(TAG, "GetResult:    451 返回" );
                                result.setLabel(451);
                                break;
                            case 454:
                                Log.e(TAG, "GetResult: 返回" );
                                result.setLabel(454);
                                break;
                            default:
                                result.setRemark(context.getString(R.string.socket_error4));
                                break;
                        }
                        break;
                    case 506://日光温室
                        switch (commandType) {
                            case 519://设备信息
                                getHelioGreenhouseData(in, result);
                                result.setLabel(519);
                                break;
                            case 526://开关状态返回
                                result.setOutputCoilStatus(in.readLong());
                                result.setOutputCoilStatus1(in.readLong());
                                result.setLabel(526);
                                break;
                            case 515://保存设备信息
                                result.setLabel(515);
                                break;
                            case 525://自控参数应答
                                getHelioGreSetData(in, result);
                                result.setLabel(525);
                                break;
                            case 517://自控参数设置应答
                                result.setLabel(517);
                                break;
                            default:
                                result.setRemark(context.getString(R.string.socket_error4));
                                break;
                        }
                        break;
                    case 308://施肥机
                        switch (commandType) {
                            case 319://修改控制方式应答
                                result.setLabel(319);
                                break;
                            case 320://手动启动施肥机应答
                                result.setLabel(320);
                                break;
                            case 321://手动停止施肥机应答
                                result.setLabel(321);
                                break;
                            case 322://获取计划参数应答
                                getFertPlainDetails(in, result);
                                break;
                            case 323://成功修改计划参数应答

                                break;
                            case 324://增加计划参数应答
                                result.setmResult(in.readUnsignedByte());
                                break;
                            case 325://获取自动参数应答
                                getAutoDetails(in, result);
                                break;
                            case 326://成功修改自动参数应答

                                break;
                            case 327://增加自动参数应答
                                result.setmResult(in.readUnsignedByte());
                                break;
                            default:
                                result.setRemark(context.getString(R.string.socket_error4));
                                break;
                        }
                        break;
                    case 610://电磁阀
                        switch (commandType) {
                            case 642://远程主机控制电磁阀响应码（成功）
                            case 643://远程主机控制电磁阀响应码（失败）
                                result.setmResult(new int[]{
                                        in.readUnsignedByte(),
                                        in.readUnsignedByte()
                                });
                                break;
                            case 634://读取电磁阀自控参数应答
                                getWvhAutoParam(in, result);
                                break;
                            case 635://远程主机修改自控参数成功应答
                            case 636://远程主机修改自控参数失败应答
                                break;
                            case 638://远程主机获取计划应答
                                getWVH_PlanParam(in, result);
                                break;
                            case 639://远程主机修改计划成功应答
                            case 640://远程主机修改计划失败应答（启用失败有些区域已经被设置为自动灌溉）
                                break;
                            case 637://远程主机新增计划成功应答
                                break;
                            case 633://远程主机修改电磁阀控制器名字成功应答

                                break;
                            case 646:
                               byte b = (byte) in.readUnsignedByte();
                                Log.e("aa", "bbb"+b);//
                                Log.e("aa", "接受: 646");//
                                result.setLabel(646);//再点
                                result.setmResult(b);
                                break;
                            default:
                                break;
                        }
                        break;
                    case 708://在线施肥机
                        switch (commandType) {
                            case 715://手动启动灌溉阀门
                                break;
                            case 719://获取计划参数应答
                                int num = in.readByte();
                                if (num >= 0)
                                    getAutoCtlParamter(num, in, result);
                                break;
                            case 713://施肥机控制方式设置应答
                                break;
                            case 721://修改计划参数应答
                                break;
                            case 729://获取灌溉记录应答
                                int num1 = in.readInt();
                                if (num1 >= 0) {
                                    getIrriRecord(num1, in, result);
                                }
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        result.setRemark(context.getString(R.string.socket_error3));
                        break;
                }
            } else result.setRemark(context.getString(R.string.socket_error2));

        } catch (IOException e) {
            result.setRemark(context.getString(R.string.socket_error5));
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException e) {

            }
            try {
                if (block != null) block.close();
            } catch (IOException e) {

            }
        }
        return result;
    }

    private void getIrriRecord(int num1, DataInputStream in, SocketResult result) {
        try {
            List<IrriRecord> list = new ArrayList<>();
            for (int i = 0; i < num1; i++) {
                IrriRecord irriRecord = new IrriRecord();
                irriRecord.setInfoName(getString(in, result));

                irriRecord.setAutoObject(in.readByte());

                int planRunTime = in.readShort() & 0xffff;
                irriRecord.setPlanRunTime(planRunTime);

                irriRecord.setIrrValvesL(in.readLong());
                irriRecord.setIrrValvesH(in.readLong());

                int realRunTime = in.readShort() & 0xffff;
                irriRecord.setRealRunTime(realRunTime);

                irriRecord.setEcSetValue(new BigDecimal(in.readFloat())
                        .setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                irriRecord.setPhSetValue(new BigDecimal(in.readFloat())
                        .setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                irriRecord.setEcRealValue(new BigDecimal(in.readFloat())
                        .setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                irriRecord.setPhRealValue(new BigDecimal(in.readFloat())
                        .setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                irriRecord.setIrriVolume(in.readFloat());

                irriRecord.setLevelCtl(in.readBoolean());
                irriRecord.setHasStorageTank(in.readBoolean());

                irriRecord.setTankNumber(in.readByte());

                irriRecord.setVolumeCtl(in.readBoolean());

                irriRecord.setErrorCode(in.readByte());
                list.add(irriRecord);
            }
            result.setmResult(list);
        } catch (Exception e) {
            result.setRemark(context.getString(R.string.socket_error5));
        }
    }

    private void getAutoCtlParamter(int num, DataInputStream in, SocketResult result) {
        try {
            List<OnlAutoCtlParamter> list = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                OnlAutoCtlParamter paramter = new OnlAutoCtlParamter();
                paramter.setInfoName(getString(in, result));

                paramter.setStartCondition(in.readByte());
                paramter.setAutoObject(in.readByte());
                paramter.setWeekDay(in.readByte());

                paramter.setStartTime(getString(in, result));
                paramter.setStopTime(getString(in, result));

                paramter.setStorageTankNum(in.readByte());

                paramter.setLowLevel(in.readShort());
                paramter.setHighLevel(in.readShort());
                int runTime = in.readShort() & 0xffff;
                int pauseTime = in.readShort() & 0xffff;
                paramter.setRunTime(runTime);
                paramter.setPauseTime(pauseTime);

                paramter.setMaxValveNum(in.readByte());

                paramter.setECSetValue(in.readFloat());
                paramter.setPHSetValue(in.readFloat());

                paramter.setFertCh(in.readByte());

                paramter.setIrrValvesL(in.readLong());
                paramter.setIrrValvesH(in.readLong());

                paramter.setTimeCtrType(in.readByte());
                paramter.setManualCtrMode(in.readByte());

                paramter.setWaterLimit(in.readFloat());

                paramter.setFertCtlMode(in.readByte());

                paramter.setProportion_A(in.readFloat());
                paramter.setProportion_B(in.readFloat());
                paramter.setProportion_C(in.readFloat());
                paramter.setProportion_D(in.readFloat());
                list.add(paramter);
            }
            result.setmResult(list);
        } catch (Exception e) {
            result.setRemark(context.getString(R.string.socket_error5));
        }
    }

    /**
     * 获取施肥机自动参数
     *
     * @param in
     * @param result
     */
    private void getAutoDetails(DataInputStream in, SocketResult result) {
        try {
            AutoDetails details = new AutoDetails();
            details.setAutoDetail(in.readByte());
            details.setAutoName(getString(in, result));
            details.setLocaltion(in.readByte());
            details.setType(in.readByte());
            details.setSensorIndex(in.readByte());
            details.setUpperLimit1(in.readFloat());
            details.setLowerLimit1(in.readFloat());
            details.setUpperLimit2(in.readFloat());
            details.setLowerLimit2(in.readFloat());
            details.setAutoMode(in.readByte());
            details.setECType(in.readByte());
            details.setECSetValue(in.readShort());
            details.setPHType(in.readByte());
            details.setPHSetValue(in.readShort());
            details.setFerCh(in.readByte());
            details.setIrrigationValve(in.readLong());
            details.setWaterQuantityCtlMode(in.readByte());
            details.setWaterCtlValue(in.readShort());
            details.setIsUsed(in.readByte());
            details.setReserved(in.readLong());
            result.setmResult(details);
        } catch (Exception e) {
            result.setRemark(context.getString(R.string.socket_error5));
        }
    }

    /**
     * 获取施肥机计划参数
     *
     * @param in
     * @param result
     */
    private void getFertPlainDetails(DataInputStream in, SocketResult result) {
        try {
            FertPlainDetails details = new FertPlainDetails();
            details.setMplan(in.readByte());
            details.setPlainName(getString(in, result));
            details.setPlainFirstDay(getString(in, result));
            details.setPlainEndDay(getString(in, result));
            details.setStartTime(getString(in, result));
            details.setStopTime(getString(in, result));

            details.setIntervalDays(in.readByte());
            details.setIntervalHours(in.readByte());
            details.setIntervalMinutes(in.readByte());
            details.setPlainMode(in.readByte());
            details.setECType(in.readByte());

            details.setECSetValue(in.readShort());
            details.setPHType(in.readByte());
            details.setPHSetValue(in.readShort());
            details.setFerCh(in.readByte());
            details.setIrrigationValve(in.readLong());
            details.setWaterQuantityCtlMode(in.readByte());
            details.setWaterCtlValue(in.readShort());
            details.setIsUsed(in.readByte());
            details.setReserved(in.readLong());

            result.setmResult(details);
        } catch (IOException e) {
            result.setRemark(context.getString(R.string.socket_error5));
        }
    }

    /**
     * 获取计划数据
     *
     * @param in
     * @param result
     */
    private void getWVH_PlanParam(DataInputStream in, SocketResult result) {
        try {
            WVH_PlanParam param = new WVH_PlanParam();
            param.setPlanHigh(in.readLong());
            param.setPlanLow(in.readLong());

            param.setFirst_run_date(getString(in, result));
            param.setStart_time(getString(in, result));
            param.setEnd_time(getString(in, result));
            param.setInterval(in.readShort());
            param.setWater_limit(in.readFloat());
            param.setUsed(in.readBoolean());
            param.setIrriZone_H(in.readLong());
            param.setIrriZone_L(in.readLong());
            param.setRunTime(in.readShort());
            param.setPushTime(in.readShort());
            result.setmResult(param);
        } catch (IOException e) {
            result.setRemark(context.getString(R.string.socket_error5));
        }

    }

    /**
     * 读取电磁阀自控参数
     *
     * @param in
     * @param result
     */
    private void getWvhAutoParam(DataInputStream in, SocketResult result) {
        try {
            WvhAutoParam param = new WvhAutoParam();
            param.setSerial(in.readByte());
            param.setHum_top_value(in.readFloat());
            param.setHum_low_value(in.readFloat());
            param.setTarget_layer(in.readByte());
            param.setWater_limit(in.readFloat());
            param.setUsed(in.readBoolean());
            result.setmResult(param);
        } catch (IOException e) {
            result.setRemark(context.getString(R.string.socket_error5));
        }
    }

    /**
     * 读取日光温室自控参数
     *
     * @param in
     * @param result
     */
    private void getHelioGreSetData(DataInputStream in, SocketResult result) {
        try {
            HelioGreParam param = new HelioGreParam();

            param.setWenduCtlMode(in.readByte());
            param.setShiduCtlMode(in.readByte());
            param.setGuangzhaoduCtlMode(in.readByte());
            param.setReservedCtlMode(in.readByte());

            param.setTime1(in.readInt());
            param.setTime2(in.readInt());
            param.setTime3(in.readInt());
            param.setTime4(in.readInt());
            param.setTime5(in.readInt());
            param.setTime6(in.readInt());
            param.setTime7(in.readInt());
            param.setTime8(in.readInt());
            param.setTime9(in.readInt());
            param.setTime10(in.readInt());
            param.setTime11(in.readInt());
            param.setTime12(in.readInt());
            param.setTime13(in.readInt());
            param.setTime14(in.readInt());
            param.setTime15(in.readInt());
            param.setTime16(in.readInt());

            param.setFengsu(in.readFloat());
            param.setWencha(in.readFloat());

            param.setJijie(in.readByte());
            param.setYouxianji(in.readByte());

            param.setAutoParamInfoGroupName(getString(in, result));
            param.setStartTime(getString(in, result));
            param.setEndTime(getString(in, result));
            param.setUse(in.readBoolean());

            SetParamInfo wdInfo = new SetParamInfo();
            wdInfo.setParamName(getString(in, result));
            wdInfo.setFallTopValue(in.readFloat());
            wdInfo.setFallLowValue(in.readFloat());
            wdInfo.setRaiseTopValue(in.readFloat());
            wdInfo.setRaiseLowValue(in.readFloat());
            wdInfo.setFallTopValue_2(in.readFloat());
            wdInfo.setFallLowValue_2(in.readFloat());
            wdInfo.setRaiseTopValue_2(in.readFloat());
            wdInfo.setRaiseLowValue_2(in.readFloat());
            param.setWdInfo(wdInfo);

            SetParamInfo gzInfo = new SetParamInfo();
            gzInfo.setParamName(getString(in, result));
            gzInfo.setFallTopValue(in.readFloat());
            gzInfo.setFallLowValue(in.readFloat());
            gzInfo.setRaiseTopValue(in.readFloat());
            gzInfo.setRaiseLowValue(in.readFloat());
            gzInfo.setFallTopValue_2(in.readFloat());
            gzInfo.setFallLowValue_2(in.readFloat());
            gzInfo.setRaiseTopValue_2(in.readFloat());
            gzInfo.setRaiseLowValue_2(in.readFloat());
            param.setGzInfo(gzInfo);

            SetParamInfo sdInfo = new SetParamInfo();
            sdInfo.setParamName(getString(in, result));
            sdInfo.setFallTopValue(in.readFloat());
            sdInfo.setFallLowValue(in.readFloat());
            sdInfo.setRaiseTopValue(in.readFloat());
            sdInfo.setRaiseLowValue(in.readFloat());
            sdInfo.setFallTopValue_2(in.readFloat());
            sdInfo.setFallLowValue_2(in.readFloat());
            sdInfo.setRaiseTopValue_2(in.readFloat());
            sdInfo.setRaiseLowValue_2(in.readFloat());
            param.setSdInfo(sdInfo);

            SetParamInfo blInfo = new SetParamInfo();
            blInfo.setParamName(getString(in, result));
            blInfo.setFallTopValue(in.readFloat());
            blInfo.setFallLowValue(in.readFloat());
            blInfo.setRaiseTopValue(in.readFloat());
            blInfo.setRaiseLowValue(in.readFloat());
            blInfo.setFallTopValue_2(in.readFloat());
            blInfo.setFallLowValue_2(in.readFloat());
            blInfo.setRaiseTopValue_2(in.readFloat());
            blInfo.setRaiseLowValue_2(in.readFloat());
            param.setBlInfo(blInfo);

            result.setmResult(param);
        } catch (IOException e) {
            result.setRemark(context.getString(R.string.socket_error5));
        }
    }

    /**
     * 读取日光温室设备数据
     *
     * @param in
     * @param result
     */
    private void getHelioGreenhouseData(DataInputStream in, SocketResult result) {
        try {
            HelioGreAutoParam param = new HelioGreAutoParam();

            param.setWenduCtlMode(in.readByte());
            param.setShiduCtlMode(in.readByte());
            param.setGuangzhaoduCtlMode(in.readByte());
            param.setReservedCtlMode(in.readByte());

            param.setTime1(in.readInt());
            param.setTime2(in.readInt());
            param.setTime3(in.readInt());
            param.setTime4(in.readInt());
            param.setTime5(in.readInt());
            param.setTime6(in.readInt());
            param.setTime7(in.readInt());
            param.setTime8(in.readInt());
            param.setTime9(in.readInt());
            param.setTime10(in.readInt());
            param.setTime11(in.readInt());
            param.setTime12(in.readInt());
            param.setTime13(in.readInt());
            param.setTime14(in.readInt());
            param.setTime15(in.readInt());
            param.setTime16(in.readInt());

            param.setFengsu(in.readFloat());
            param.setWencha(in.readFloat());

            param.setJijie(in.readByte());
            param.setYouxianji(in.readByte());

            byte b = in.readByte();
            for (int i = 0; i < b; i++) {
                HelioGreDeviceInfo info = new HelioGreDeviceInfo();
                info.setDevName(getString(in, result));
                info.setImage(in.readByte());
                info.setDevCtlMode(in.readByte());
                info.setTemDevCtlMode(info.getDevCtlMode());
                info.setEnDevName(getString(in, result));
                info.setZhengFanZhuan(in.readByte());
                info.setLocaltion(in.readByte());
                info.setStype(in.readByte());
                info.setOnlimit(in.readByte());
                info.setOfflimit(in.readByte());
                info.setRegAdd1(in.readByte());
                info.setRegAdd2(in.readByte());
                info.setTimersCnt(in.readByte());

                info.getTimers()[0] = in.readInt();
                info.getTimers()[1] = in.readInt();
                info.getTimers()[2] = in.readInt();
                info.getTimers()[3] = in.readInt();
                info.getTimers()[4] = in.readInt();
                info.getTimers()[5] = in.readInt();
                info.getTimers()[6] = in.readInt();
                info.getTimers()[7] = in.readInt();

                param.getList().add(info);
            }
            result.setmResult(param);
        } catch (IOException e) {
            result.setRemark(context.getString(R.string.socket_error5));
        }
    }

    /**
     * 读取温室自控参数
     *
     * @param in
     * @param result
     */
    private void getSetAutoParam(DataInputStream in, SocketResult result) {
        try {
            SetAutoParam param = new SetAutoParam();

            param.setWenduCtlMode(in.readByte());
            param.setShiduCtlMode(in.readByte());
            param.setGuangzhaoduCtlMode(in.readByte());
            param.setReservedCtlMode(in.readByte());

            param.setTime1(in.readInt());
            param.setTime2(in.readInt());
            param.setTime3(in.readInt());
            param.setTime4(in.readInt());
            param.setTime5(in.readInt());
            param.setTime6(in.readInt());
            param.setTime7(in.readInt());
            param.setTime8(in.readInt());
            param.setTime9(in.readInt());
            param.setTime10(in.readInt());
            param.setTime11(in.readInt());
            param.setTime12(in.readInt());
            param.setTime13(in.readInt());
            param.setTime14(in.readInt());
            param.setTime15(in.readInt());

            param.setAutoParamZoneNumTemp(in.readByte());

            param.setAutoParamInfoGroupName(getString(in, result));
            param.setStartTime(getString(in, result));
            param.setEndTime(getString(in, result));
            param.setUse(in.readBoolean());

            SetParamInfo wdInfo = new SetParamInfo();
            wdInfo.setParamName(getString(in, result));
            wdInfo.setFallTopValue(in.readFloat());
            wdInfo.setFallLowValue(in.readFloat());
            wdInfo.setRaiseTopValue(in.readFloat());
            wdInfo.setRaiseLowValue(in.readFloat());
            wdInfo.setFallTopValue_2(in.readFloat());
            wdInfo.setFallLowValue_2(in.readFloat());
            wdInfo.setRaiseTopValue_2(in.readFloat());
            wdInfo.setRaiseLowValue_2(in.readFloat());
            param.setWenduAutoParam(wdInfo);

            SetParamInfo gzInfo = new SetParamInfo();
            gzInfo.setParamName(getString(in, result));
            gzInfo.setFallTopValue(in.readFloat());
            gzInfo.setFallLowValue(in.readFloat());
            gzInfo.setRaiseTopValue(in.readFloat());
            gzInfo.setRaiseLowValue(in.readFloat());
            gzInfo.setFallTopValue_2(in.readFloat());
            gzInfo.setFallLowValue_2(in.readFloat());
            gzInfo.setRaiseTopValue_2(in.readFloat());
            gzInfo.setRaiseLowValue_2(in.readFloat());
            param.setGuangzhaoAutoParam(gzInfo);

            SetParamInfo sdInfo = new SetParamInfo();
            sdInfo.setParamName(getString(in, result));
            sdInfo.setFallTopValue(in.readFloat());
            sdInfo.setFallLowValue(in.readFloat());
            sdInfo.setRaiseTopValue(in.readFloat());
            sdInfo.setRaiseLowValue(in.readFloat());
            sdInfo.setFallTopValue_2(in.readFloat());
            sdInfo.setFallLowValue_2(in.readFloat());
            sdInfo.setRaiseTopValue_2(in.readFloat());
            sdInfo.setRaiseLowValue_2(in.readFloat());
            param.setShiduAutoParam(sdInfo);

            SetParamInfo co2Info = new SetParamInfo();
            co2Info.setParamName(getString(in, result));
            co2Info.setFallTopValue(in.readFloat());
            co2Info.setFallLowValue(in.readFloat());
            co2Info.setRaiseTopValue(in.readFloat());
            co2Info.setRaiseLowValue(in.readFloat());
            co2Info.setFallTopValue_2(in.readFloat());
            co2Info.setFallLowValue_2(in.readFloat());
            co2Info.setRaiseTopValue_2(in.readFloat());
            co2Info.setRaiseLowValue_2(in.readFloat());
            param.setCO2AutoParam(co2Info);

            result.setmResult(param);
        } catch (IOException e) {
            result.setRemark(context.getString(R.string.socket_error5));
        }
    }

    /**
     * 读取温室设备数据
     *
     * @param in
     * @param result
     */
    private void getDeviceInitData(DataInputStream in, SocketResult result) {
        try {
            DevConAutoParam param = new DevConAutoParam();

            param.setWenduCtlMode(in.readByte());
            param.setShiduCtlMode(in.readByte());
            param.setGuangzhaoduCtlMode(in.readByte());
            param.setReservedCtlMode(in.readByte());

            param.setTime1(in.readInt());
            param.setTime2(in.readInt());
            param.setTime3(in.readInt());
            param.setTime4(in.readInt());
            param.setTime5(in.readInt());
            param.setTime6(in.readInt());
            param.setTime7(in.readInt());
            param.setTime8(in.readInt());
            param.setTime9(in.readInt());
            param.setTime10(in.readInt());
            param.setTime11(in.readInt());
            param.setTime12(in.readInt());
            param.setTime13(in.readInt());
            param.setTime14(in.readInt());
            param.setTime15(in.readInt());

            param.setAutoParamZoneNum(in.readShort());

            List<DeviceInfo> list = new ArrayList<>();
            for (int i = 0; i < param.getAutoParamZoneNum(); i++) {
                DeviceInfo dev = new DeviceInfo();
                dev.setDevName(getString(in, result));
                dev.setImage(in.readByte());
                dev.setRegAdd(in.readByte());
                dev.setDevCtlMode(in.readByte());
                dev.setTemDevCtlMode(dev.getDevCtlMode());
                dev.setEnDevName(getString(in, result));
                dev.setZhengFanZhuan(in.readByte());
                list.add(dev);
            }
            param.setList(list);
            result.setmResult(param);
        } catch (IOException e) {
            result.setRemark(context.getString(R.string.socket_error5));
        }
    }

    /**
     * 读取字符串
     *
     * @param in
     * @param result
     * @return
     */
    private String getString(DataInputStream in, SocketResult result) {
        String str = "";
        try {
            int strLen = in.readInt();
            if (strLen < 0) strLen = 0;
            byte[] strByte = new byte[strLen];
            for (int i = 0; i < strLen; i++) {
                strByte[i] = in.readByte();
            }
            str = new String(strByte, "utf-16");
        } catch (IOException e) {
            result.setRemark(context.getString(R.string.socket_error5));
        }
        return str;
    }

    public void dispose() {
        if (disposable != null) disposable.dispose();
    }
}
