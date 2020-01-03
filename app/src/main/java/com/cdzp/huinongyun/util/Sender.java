package com.cdzp.huinongyun.util;

import android.content.Context;
import android.util.Log;

import com.cdzp.huinongyun.R;
import com.cdzp.huinongyun.bean.AutoDetails;
import com.cdzp.huinongyun.bean.DevConAutoParam;
import com.cdzp.huinongyun.bean.FertPlainDetails;
import com.cdzp.huinongyun.bean.HelioGreAutoParam;
import com.cdzp.huinongyun.bean.HelioGreParam;
import com.cdzp.huinongyun.bean.OnlAutoCtlParamter;
import com.cdzp.huinongyun.bean.PlainDetails;
import com.cdzp.huinongyun.bean.SetAutoParam;
import com.cdzp.huinongyun.bean.SocketResult;
import com.cdzp.huinongyun.bean.WVH_PlanParam;
import com.cdzp.huinongyun.bean.WvhAutoParam;
import com.cdzp.huinongyun.message.SocketMessage;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 数据发送类
 */

public class Sender {

    private DatagramSocket socket;
    private SocketMessage message;
    private Context context;
    private String TAG = "Sender";

    public Sender(Context context) {
        try {
            socket = new DatagramSocket();
            this.context = context;
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    private SocketResult sendRequest() {
        SocketResult result = new SocketResult();

        ByteArrayOutputStream block = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(block);

        try {
            out.writeShort(10);
            out.writeInt(message.getZP_KEY_CLIENT());
            out.writeInt(message.getRequestType());
            out.writeInt(message.getmSender().length() * 2);
            out.writeChars(message.getmSender());
            out.writeInt(message.getmReceiver().length() * 2);
            out.writeChars(message.getmReceiver());
            out.writeInt(message.getCommandType());
            switch (message.getRequestType()) {
                case 3://温室请求类型
                    switch (message.getCommandType()) {
                        case 153:
                        case 154:
                        case 455:
                            Log.e(TAG, "sendRequest: 来了" + (int) message.getCoilStatus());
                            out.writeLong(message.getCoilStatus());
                            break;
                        case 158:
                            writeSetData(out);
                            break;
                        case 161:
                            writeDevData(out);
                            break;
                        case 450:
//                        case 455:
                            Log.e(TAG, "sendRequest: 来了" + (int) message.getResult());
                            out.writeByte((int) message.getResult());
                            break;
                        case 452:
                        case 453:
                            float[] floats = (float[]) message.getResult();
                            out.writeFloat(floats[0]);
                            out.writeFloat(floats[1]);
                            out.writeFloat(floats[2]);
                            out.writeFloat(floats[3]);
                            out.writeFloat(floats[4]);
                            out.writeFloat(floats[5]);
                            Log.e(TAG, "sendRequest: 来了" + floats[0]);
                            Log.e(TAG, "sendRequest: 来了" + floats[1]);
                            break;
                        default:
                            break;
                    }
                    break;
                case 506://日光温室请求类型
                    out.writeInt(message.getPlayNo().length() * 2);
                    out.writeChars(message.getPlayNo());
                    switch (message.getCommandType()) {
                        case 518://获取设备信息
                            break;
                        case 512://打开开关
                        case 513://关闭开关
                            out.writeLong(message.getCoilStatus());
                            break;
                        case 514://保存设备信息
                            writeHelioGreeData(out);
                            break;
                        case 516://保存自控参数
                            writeHelioGreeSetData(out);
                            break;
                        default:
                            break;
                    }
                    break;
                case 308://施肥机请求类型
                    switch (message.getCommandType()) {
                        case 310://施肥机控制方式设置
                            out.writeByte(message.getFertType());
                            break;
                        case 311://手动启动施肥机
                            if (message.getResult() instanceof PlainDetails) {
                                writePlainDetails(out);
                            }
                            break;
                        case 312://手动停止施肥机(无内容)

                            break;
                        case 313://施肥机获取计划参数
                            if (message.getResult() instanceof Integer) {
                                int mResult = (int) message.getResult();
                                out.writeByte(mResult);
                            }
                            break;
                        case 314://施肥机修改计划参数
                            if (message.getResult() instanceof FertPlainDetails) {
                                writeFertPlainDetails(out);
                            }
                            break;
                        case 315://增加一条计划参数

                            break;
                        case 316://施肥机获取自动参数
                            if (message.getResult() instanceof Integer) {
                                int mResult = (int) message.getResult();
                                out.writeByte(mResult);
                            }
                            break;
                        case 317://施肥机修改自动参数
                            if (message.getResult() instanceof AutoDetails) {
                                writeAutoDetails(out);
                            }
                            break;
                        case 318://增加一条自动参数

                            break;
                        default:
                            break;
                    }
                    break;
                case 610://电磁阀请求类型
                    switch (message.getCommandType()) {
                        case 622://远程主机控制电磁阀
                            if (message.getResult() instanceof int[]) {
                                int[] mResult = (int[]) message.getResult();
                                out.writeByte(mResult[0]);
                                out.writeByte(mResult[1]);
                            }
                            break;
                        case 615://远程主机读取自控参数
                            if (message.getResult() instanceof Integer) {
                                int mResult = (int) message.getResult();
                                out.writeByte(mResult);
                            }
                            break;
                        case 616://远程主机修改自控参数
                            if (message.getResult() instanceof WvhAutoParam) {
                                writeWvhAutoParam(out);
                            }
                            break;
                        case 618://远程主机获取计划
                            if (message.getResult() instanceof Integer) {
                                int mResult = (int) message.getResult();
                                out.writeByte(mResult);
                            }
                            break;
                        case 619://远程主机修改计划
                            if (message.getResult() instanceof WVH_PlanParam) {
                                writeWVH_PlanParam(out);
                            }
                            break;
                        case 645:
                            if (message.getResult() instanceof long[]) {
                                long[] mResult = (long[]) message.getResult();
                                out.writeLong(mResult[0]);
                                out.writeLong(mResult[1]);
                            }
                            break;
                        case 617://远程主机新增计划
                            break;
                        case 614://远程主机修改电磁阀控制器名字
                            if (message.getResult() instanceof String[]) {
                                String[] strs = (String[]) message.getResult();
                                int displayNo = Integer.parseInt(strs[0]);
                                out.writeByte(displayNo);
                                out.writeInt(strs[1].length() * 2);
                                out.writeChars(strs[1]);
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case 708://在线施肥机
                    switch (message.getCommandType()) {
                        case 714://手动启动灌溉阀门
                        case 724://手动启动灌溉阀门
                            writeAutoCtlParamter(out);
                            break;
                        case 716://手动停止灌溉阀门
                            break;
                        case 718://获取计划参数
                            out.writeByte(0);
                            break;
                        case 712://施肥机控制方式设置
                            out.writeByte((int) message.getResult());
                            break;
                        case 720://修改计划参数
                            writeOnlAutoCtlParamter(out);
                            break;
                        case 728://获取灌溉记录
                            if (message.getResult() instanceof String) {
                                String time = (String) message.getResult();
                                out.writeInt(time.length() * 2);
                                out.writeChars(time);
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }

            DatagramPacket packet = new DatagramPacket(block.toByteArray(), block.size(),
                    InetAddress.getByName(message.getIp()), message.getPort());
            socket.send(packet);
            Log.e("aa", "sendRequest: 已经发送");//
            result.setOK(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void writeOnlAutoCtlParamter(DataOutputStream out) {
        try {
            if (message.getResult() instanceof ArrayList) {
                List<?> list = (ArrayList) message.getResult();
                if (list.size() > 0 && list.get(0) instanceof OnlAutoCtlParamter) {
                    List<OnlAutoCtlParamter> mList = (ArrayList) message.getResult();
                    out.writeByte(mList.size());
                    for (int i = 0; i < mList.size(); i++) {
                        out.writeInt(mList.get(i).getInfoName().length() * 2);
                        out.writeChars(mList.get(i).getInfoName());

                        out.writeByte(mList.get(i).getStartCondition());
                        out.writeByte(mList.get(i).getAutoObject());
                        out.writeByte(mList.get(i).getWeekDay());

                        out.writeInt(mList.get(i).getStartTime().length() * 2);
                        out.writeChars(mList.get(i).getStartTime());
                        out.writeInt(mList.get(i).getStopTime().length() * 2);
                        out.writeChars(mList.get(i).getStopTime());

                        out.writeByte(mList.get(i).getStorageTankNum());

                        out.writeShort(mList.get(i).getLowLevel());
                        out.writeShort(mList.get(i).getHighLevel());
                        out.writeShort(mList.get(i).getRunTime());
                        out.writeShort(mList.get(i).getPauseTime());

                        out.writeByte(mList.get(i).getMaxValveNum());

                        out.writeFloat(mList.get(i).getECSetValue());
                        out.writeFloat(mList.get(i).getPHSetValue());

                        out.writeByte(mList.get(i).getFertCh());

                        out.writeLong(mList.get(i).getIrrValvesL());
                        out.writeLong(mList.get(i).getIrrValvesH());

                        out.writeByte(mList.get(i).getTimeCtrType());
                        out.writeByte(mList.get(i).getManualCtrMode());

                        out.writeFloat(mList.get(i).getWaterLimit());

                        out.writeByte(mList.get(i).getFertCtlMode());

                        out.writeFloat(mList.get(i).getProportion_A());
                        out.writeFloat(mList.get(i).getProportion_B());
                        out.writeFloat(mList.get(i).getProportion_C());
                        out.writeFloat(mList.get(i).getProportion_D());
                    }
                } else {
                    out.writeByte(0);
                }
            }
        } catch (Exception e) {

        }
    }

    private void writeAutoCtlParamter(DataOutputStream out) {
        try {
            OnlAutoCtlParamter paramter = (OnlAutoCtlParamter) message.getResult();

            out.writeInt(paramter.getInfoName().length() * 2);
            out.writeChars(paramter.getInfoName());

            out.writeByte(paramter.getStartCondition());
            out.writeByte(paramter.getAutoObject());
            out.writeByte(paramter.getWeekDay());

            out.writeInt(paramter.getStartTime().length() * 2);
            out.writeChars(paramter.getStartTime());
            out.writeInt(paramter.getStopTime().length() * 2);
            out.writeChars(paramter.getStopTime());

            out.writeByte(paramter.getStorageTankNum());
            out.writeShort(paramter.getLowLevel());
            out.writeShort(paramter.getHighLevel());
            out.writeShort(paramter.getRunTime());
            out.writeShort(paramter.getPauseTime());
            out.writeByte(paramter.getMaxValveNum());
            out.writeFloat(paramter.getECSetValue());
            out.writeFloat(paramter.getPHSetValue());
            out.writeByte(paramter.getFertCh());
            out.writeLong(paramter.getIrrValvesL());
            out.writeLong(paramter.getIrrValvesH());
            out.writeByte(paramter.getTimeCtrType());
            out.writeByte(paramter.getManualCtrMode());
            out.writeFloat(paramter.getWaterLimit());
            out.writeByte(paramter.getFertCtlMode());
            out.writeFloat(paramter.getProportion_A());
            out.writeFloat(paramter.getProportion_B());
            out.writeFloat(paramter.getProportion_C());
            out.writeFloat(paramter.getProportion_D());
//            Log.d("aa", "sendRequest: 打包成功");
        } catch (Exception e) {

        }
    }

    private void writeAutoDetails(DataOutputStream out) {
        try {
            AutoDetails details = (AutoDetails) message.getResult();
            out.writeInt(details.getAutoName().length() * 2);
            out.writeChars(details.getAutoName());

            out.writeByte(details.getLocaltion());
            out.writeByte(details.getType());
            out.writeByte(details.getSensorIndex());
            out.writeFloat(details.getUpperLimit1());
            out.writeFloat(details.getLowerLimit1());
            out.writeFloat(details.getUpperLimit2());
            out.writeFloat(details.getLowerLimit2());
            out.writeByte(details.getAutoMode());
            out.writeByte(details.getECType());
            out.writeShort(details.getECSetValue());
            out.writeByte(details.getPHType());
            out.writeShort(details.getPHSetValue());
            out.writeByte(details.getFerCh());
            out.writeLong(details.getIrrigationValve());
            out.writeByte(details.getWaterQuantityCtlMode());
            out.writeShort(details.getWaterCtlValue());
            out.writeByte(details.getIsUsed());
            out.writeLong(details.getReserved());
        } catch (Exception e) {

        }
    }

    private void writeFertPlainDetails(DataOutputStream out) {
        try {
            FertPlainDetails details = (FertPlainDetails) message.getResult();
            out.writeInt(details.getPlainName().length() * 2);
            out.writeChars(details.getPlainName());
            out.writeInt(details.getPlainFirstDay().length() * 2);
            out.writeChars(details.getPlainFirstDay());
            out.writeInt(details.getPlainEndDay().length() * 2);
            out.writeChars(details.getPlainEndDay());
            out.writeInt(details.getStartTime().length() * 2);
            out.writeChars(details.getStartTime());
            out.writeInt(details.getStopTime().length() * 2);
            out.writeChars(details.getStopTime());

            out.writeByte(details.getIntervalDays());
            out.writeByte(details.getIntervalHours());
            out.writeByte(details.getIntervalMinutes());
            out.writeByte(details.getPlainMode());
            out.writeByte(details.getECType());

            out.writeShort(details.getECSetValue());
            out.writeByte(details.getPHType());
            out.writeShort(details.getPHSetValue());
            out.writeByte(details.getFerCh());

            out.writeLong(details.getIrrigationValve());
            out.writeByte(details.getWaterQuantityCtlMode());
            out.writeShort(details.getWaterCtlValue());
            out.writeByte(details.getIsUsed());
            out.writeLong(details.getReserved());

        } catch (Exception e) {

        }
    }

    private void writeWVH_PlanParam(DataOutputStream out) {
        try {
            WVH_PlanParam param = (WVH_PlanParam) message.getResult();
            out.writeByte(param.getSerialNum());
            out.writeInt(param.getFirst_run_date().length() * 2);
            out.writeChars(param.getFirst_run_date());
            out.writeInt(param.getStart_time().length() * 2);
            out.writeChars(param.getStart_time());
            out.writeInt(param.getEnd_time().length() * 2);
            out.writeChars(param.getEnd_time());
            out.writeShort(param.getInterval());
            out.writeFloat(param.getWater_limit());
            out.writeBoolean(param.isUsed());
            out.writeLong(param.getIrriZone_H());
            out.writeLong(param.getIrriZone_L());
            out.writeShort(param.getRunTime());
            out.writeShort(param.getPushTime());
        } catch (IOException e) {

        }
    }

    private void writeWvhAutoParam(DataOutputStream out) {
        try {
            WvhAutoParam param = (WvhAutoParam) message.getResult();
            out.writeByte(param.getSerial());
            out.writeFloat(param.getHum_top_value());
            out.writeFloat(param.getHum_low_value());
            out.writeByte(param.getTarget_layer());
            out.writeFloat(param.getWater_limit());
            out.writeBoolean(param.isUsed());
        } catch (IOException e) {

        }
    }

    private void writePlainDetails(DataOutputStream out) {
        try {
            PlainDetails pd = (PlainDetails) message.getResult();
            out.writeInt(pd.getPlainName().length() * 2);
            out.writeChars(pd.getPlainName());
            out.writeInt(pd.getPlainFirstDay().length() * 2);
            out.writeChars(pd.getPlainFirstDay());
            out.writeInt(pd.getPlainEndDay().length() * 2);
            out.writeChars(pd.getPlainEndDay());
            out.writeInt(pd.getStartTime().length() * 2);
            out.writeChars(pd.getStartTime());
            out.writeInt(pd.getStopTime().length() * 2);
            out.writeChars(pd.getStopTime());

            out.writeByte(pd.getIntervalDays());
            out.writeByte(pd.getIntervalHours());
            out.writeByte(pd.getIntervalMinutes());
            out.writeByte(pd.getPlainMode());
            out.writeByte(pd.getECType());

            out.writeShort(pd.getECSetValue());
            out.writeByte(pd.getPHType());
            out.writeShort(pd.getPHSetValue());
            out.writeByte(pd.getFerCh());

            out.writeLong(pd.getIrrigationValve());
            out.writeByte(pd.getWaterQuantityCtlMode());
            out.writeShort(pd.getWaterCtlValue());
            out.writeByte(pd.getIsUsed());
            out.writeLong(pd.getReserved());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeHelioGreeSetData(DataOutputStream out) {
        try {
            if (message.getResult() instanceof HelioGreParam) {
                HelioGreParam param = (HelioGreParam) message.getResult();
                out.writeByte(param.getWenduCtlMode());
                out.writeByte(param.getShiduCtlMode());
                out.writeByte(param.getGuangzhaoduCtlMode());
                out.writeByte(param.getReservedCtlMode());

                out.writeInt(param.getTime1());
                out.writeInt(param.getTime2());
                out.writeInt(param.getTime3());
                out.writeInt(param.getTime4());
                out.writeInt(param.getTime5());
                out.writeInt(param.getTime6());
                out.writeInt(param.getTime7());
                out.writeInt(param.getTime8());
                out.writeInt(param.getTime9());
                out.writeInt(param.getTime10());
                out.writeInt(param.getTime11());
                out.writeInt(param.getTime12());
                out.writeInt(param.getTime13());
                out.writeInt(param.getTime14());
                out.writeInt(param.getTime15());
                out.writeInt(param.getTime16());

                out.writeFloat(param.getFengsu());
                out.writeFloat(param.getWencha());

                out.writeByte(param.getJijie());
                out.writeByte(param.getYouxianji());

                out.writeInt(param.getAutoParamInfoGroupName().length() * 2);
                out.writeChars(param.getAutoParamInfoGroupName());

                out.writeInt(param.getStartTime().length() * 2);
                out.writeChars(param.getStartTime());

                out.writeInt(param.getEndTime().length() * 2);
                out.writeChars(param.getEndTime());

                out.writeBoolean(param.isUse());

                /**************wenduAutoParamTemp paramName**************/
                out.writeInt(param.getWdInfo().getParamName().length() * 2);
                out.writeChars(param.getWdInfo().getParamName());

                out.writeFloat(param.getWdInfo().getFallTopValue());
                out.writeFloat(param.getWdInfo().getFallLowValue());
                out.writeFloat(param.getWdInfo().getRaiseTopValue());
                out.writeFloat(param.getWdInfo().getRaiseLowValue());

                out.writeFloat(param.getWdInfo().getFallTopValue_2());
                out.writeFloat(param.getWdInfo().getFallLowValue_2());
                out.writeFloat(param.getWdInfo().getRaiseTopValue_2());
                out.writeFloat(param.getWdInfo().getRaiseLowValue_2());

                /**************guangzhaoAutoParamTemp paramName**************/
                out.writeInt(param.getGzInfo().getParamName().length() * 2);
                out.writeChars(param.getGzInfo().getParamName());

                out.writeFloat(param.getGzInfo().getFallTopValue());
                out.writeFloat(param.getGzInfo().getFallLowValue());
                out.writeFloat(param.getGzInfo().getRaiseTopValue());
                out.writeFloat(param.getGzInfo().getRaiseLowValue());

                out.writeFloat(param.getGzInfo().getFallTopValue_2());
                out.writeFloat(param.getGzInfo().getFallLowValue_2());
                out.writeFloat(param.getGzInfo().getRaiseTopValue_2());
                out.writeFloat(param.getGzInfo().getRaiseLowValue_2());
                /**************shiduAutoParamTemp paramName**************/
                out.writeInt(param.getSdInfo().getParamName().length() * 2);
                out.writeChars(param.getSdInfo().getParamName());

                out.writeFloat(param.getSdInfo().getFallTopValue());
                out.writeFloat(param.getSdInfo().getFallLowValue());
                out.writeFloat(param.getSdInfo().getRaiseTopValue());
                out.writeFloat(param.getSdInfo().getRaiseLowValue());

                out.writeFloat(param.getSdInfo().getFallTopValue_2());
                out.writeFloat(param.getSdInfo().getFallLowValue_2());
                out.writeFloat(param.getSdInfo().getRaiseTopValue_2());
                out.writeFloat(param.getSdInfo().getRaiseLowValue_2());
                /**************co2AutoParamTemp paramName**************/
                out.writeInt(param.getBlInfo().getParamName().length() * 2);
                out.writeChars(param.getBlInfo().getParamName());

                out.writeFloat(param.getBlInfo().getFallTopValue());
                out.writeFloat(param.getBlInfo().getFallLowValue());
                out.writeFloat(param.getBlInfo().getRaiseTopValue());
                out.writeFloat(param.getBlInfo().getRaiseLowValue());

                out.writeFloat(param.getBlInfo().getFallTopValue_2());
                out.writeFloat(param.getBlInfo().getFallLowValue_2());
                out.writeFloat(param.getBlInfo().getRaiseTopValue_2());
                out.writeFloat(param.getBlInfo().getRaiseLowValue_2());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeHelioGreeData(DataOutputStream out) {
        try {
            if (message.getResult() instanceof HelioGreAutoParam) {
                HelioGreAutoParam data = (HelioGreAutoParam) message.getResult();
                out.writeByte(data.getWenduCtlMode());
                out.writeByte(data.getShiduCtlMode());
                out.writeByte(data.getGuangzhaoduCtlMode());
                out.writeByte(data.getReservedCtlMode());

                out.writeInt(data.getTime1());
                out.writeInt(data.getTime2());
                out.writeInt(data.getTime3());
                out.writeInt(data.getTime4());
                out.writeInt(data.getTime5());
                out.writeInt(data.getTime6());
                out.writeInt(data.getTime7());
                out.writeInt(data.getTime8());
                out.writeInt(data.getTime9());
                out.writeInt(data.getTime10());
                out.writeInt(data.getTime11());
                out.writeInt(data.getTime12());
                out.writeInt(data.getTime13());
                out.writeInt(data.getTime14());
                out.writeInt(data.getTime15());
                out.writeInt(data.getTime16());

                out.writeFloat(data.getFengsu());
                out.writeFloat(data.getWencha());

                out.writeByte(data.getJijie());
                out.writeByte(data.getYouxianji());

                out.writeByte(data.getList().size());

                for (int i = 0; i < data.getList().size(); i++) {
                    out.writeInt(data.getList().get(i).getDevName().length() * 2);
                    out.writeChars(data.getList().get(i).getDevName());

                    out.writeByte(data.getList().get(i).getImage());
                    out.writeByte(data.getList().get(i).getTemDevCtlMode());

                    out.writeInt(data.getList().get(i).getEnDevName().length() * 2);
                    out.writeChars(data.getList().get(i).getEnDevName());

                    out.writeByte(data.getList().get(i).getZhengFanZhuan());
                    out.writeByte(data.getList().get(i).getLocaltion());
                    out.writeByte(data.getList().get(i).getStype());
                    out.writeByte(data.getList().get(i).getOnlimit());
                    out.writeByte(data.getList().get(i).getOfflimit());
                    out.writeByte(data.getList().get(i).getRegAdd1());
                    out.writeByte(data.getList().get(i).getRegAdd2());
                    out.writeByte(data.getList().get(i).getTimersCnt());

                    out.writeInt(data.getList().get(i).getTimers()[0]);
                    out.writeInt(data.getList().get(i).getTimers()[1]);
                    out.writeInt(data.getList().get(i).getTimers()[2]);
                    out.writeInt(data.getList().get(i).getTimers()[3]);
                    out.writeInt(data.getList().get(i).getTimers()[4]);
                    out.writeInt(data.getList().get(i).getTimers()[5]);
                    out.writeInt(data.getList().get(i).getTimers()[6]);
                    out.writeInt(data.getList().get(i).getTimers()[7]);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeDevData(DataOutputStream out) {
        try {
            if (message.getResult() instanceof DevConAutoParam) {
                DevConAutoParam data = (DevConAutoParam) message.getResult();
                out.writeByte(data.getWenduCtlMode());
                out.writeByte(data.getShiduCtlMode());
                out.writeByte(data.getGuangzhaoduCtlMode());
                out.writeByte(data.getReservedCtlMode());

                out.writeInt(data.getTime1());
                out.writeInt(data.getTime2());
                out.writeInt(data.getTime3());
                out.writeInt(data.getTime4());
                out.writeInt(data.getTime5());
                out.writeInt(data.getTime6());
                out.writeInt(data.getTime7());
                out.writeInt(data.getTime8());
                out.writeInt(data.getTime9());
                out.writeInt(data.getTime10());
                out.writeInt(data.getTime11());
                out.writeInt(data.getTime12());
                out.writeInt(data.getTime13());
                out.writeInt(data.getTime14());
                out.writeInt(data.getTime15());

                out.writeShort(data.getAutoParamZoneNum());

                for (int i = 0; i < data.getList().size(); i++) {
                    out.writeInt(data.getList().get(i).getDevName().length() * 2);
                    out.writeChars(data.getList().get(i).getDevName());

                    out.writeByte(data.getList().get(i).getImage());
                    out.writeByte(data.getList().get(i).getRegAdd());
                    out.writeByte(data.getList().get(i).getTemDevCtlMode());

                    out.writeInt(data.getList().get(i).getEnDevName().length() * 2);
                    out.writeChars(data.getList().get(i).getEnDevName());

                    out.writeByte(data.getList().get(i).getZhengFanZhuan());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeSetData(DataOutputStream out) {
        try {
            if (message.getResult() instanceof SetAutoParam) {
                SetAutoParam param = (SetAutoParam) message.getResult();
                out.writeByte(param.getWenduCtlMode());
                out.writeByte(param.getShiduCtlMode());
                out.writeByte(param.getGuangzhaoduCtlMode());
                out.writeByte(param.getReservedCtlMode());

                out.writeInt(param.getTime1());
                out.writeInt(param.getTime2());
                out.writeInt(param.getTime3());
                out.writeInt(param.getTime4());
                out.writeInt(param.getTime5());
                out.writeInt(param.getTime6());
                out.writeInt(param.getTime7());
                out.writeInt(param.getTime8());
                out.writeInt(param.getTime9());
                out.writeInt(param.getTime10());
                out.writeInt(param.getTime11());
                out.writeInt(param.getTime12());
                out.writeInt(param.getTime13());
                out.writeInt(param.getTime14());
                out.writeInt(param.getTime15());

                out.writeInt(param.getAutoParamInfoGroupName().length() * 2);
                out.writeChars(param.getAutoParamInfoGroupName());

                out.writeInt(param.getStartTime().length() * 2);
                out.writeChars(param.getStartTime());

                out.writeInt(param.getEndTime().length() * 2);
                out.writeChars(param.getEndTime());

                out.writeBoolean(param.isUse());

                /**************wenduAutoParamTemp paramName**************/
                out.writeInt(param.getWenduAutoParam().getParamName().length() * 2);
                out.writeChars(param.getWenduAutoParam().getParamName());

                out.writeFloat(param.getWenduAutoParam().getFallTopValue());
                out.writeFloat(param.getWenduAutoParam().getFallLowValue());
                out.writeFloat(param.getWenduAutoParam().getRaiseTopValue());
                out.writeFloat(param.getWenduAutoParam().getRaiseLowValue());

                out.writeFloat(param.getWenduAutoParam().getFallTopValue_2());
                out.writeFloat(param.getWenduAutoParam().getFallLowValue_2());
                out.writeFloat(param.getWenduAutoParam().getRaiseTopValue_2());
                out.writeFloat(param.getWenduAutoParam().getRaiseLowValue_2());

                /**************guangzhaoAutoParamTemp paramName**************/
                out.writeInt(param.getGuangzhaoAutoParam().getParamName().length() * 2);
                out.writeChars(param.getGuangzhaoAutoParam().getParamName());

                out.writeFloat(param.getGuangzhaoAutoParam().getFallTopValue());
                out.writeFloat(param.getGuangzhaoAutoParam().getFallLowValue());
                out.writeFloat(param.getGuangzhaoAutoParam().getRaiseTopValue());
                out.writeFloat(param.getGuangzhaoAutoParam().getRaiseLowValue());

                out.writeFloat(param.getGuangzhaoAutoParam().getFallTopValue_2());
                out.writeFloat(param.getGuangzhaoAutoParam().getFallLowValue_2());
                out.writeFloat(param.getGuangzhaoAutoParam().getRaiseTopValue_2());
                out.writeFloat(param.getGuangzhaoAutoParam().getRaiseLowValue_2());
                /**************shiduAutoParamTemp paramName**************/
                out.writeInt(param.getShiduAutoParam().getParamName().length() * 2);
                out.writeChars(param.getShiduAutoParam().getParamName());

                out.writeFloat(param.getShiduAutoParam().getFallTopValue());
                out.writeFloat(param.getShiduAutoParam().getFallLowValue());
                out.writeFloat(param.getShiduAutoParam().getRaiseTopValue());
                out.writeFloat(param.getShiduAutoParam().getRaiseLowValue());

                out.writeFloat(param.getShiduAutoParam().getFallTopValue_2());
                out.writeFloat(param.getShiduAutoParam().getFallLowValue_2());
                out.writeFloat(param.getShiduAutoParam().getRaiseTopValue_2());
                out.writeFloat(param.getShiduAutoParam().getRaiseLowValue_2());
                /**************co2AutoParamTemp paramName**************/
                out.writeInt(param.getCO2AutoParam().getParamName().length() * 2);
                out.writeChars(param.getCO2AutoParam().getParamName());

                out.writeFloat(param.getCO2AutoParam().getFallTopValue());
                out.writeFloat(param.getCO2AutoParam().getFallLowValue());
                out.writeFloat(param.getCO2AutoParam().getRaiseTopValue());
                out.writeFloat(param.getCO2AutoParam().getRaiseLowValue());

                out.writeFloat(param.getCO2AutoParam().getFallTopValue_2());
                out.writeFloat(param.getCO2AutoParam().getFallLowValue_2());
                out.writeFloat(param.getCO2AutoParam().getRaiseTopValue_2());
                out.writeFloat(param.getCO2AutoParam().getRaiseLowValue_2());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void messageRequest(SocketMessage message) {
        if (socket != null) {
            this.message = message;
            requestData();
        } else {
            SocketResult result = new SocketResult();
            result.setRemark(context.getString(R.string.socket_error6));
            EventBus.getDefault().post(result);
        }
    }

    private void requestData() {
        Observable.create(new ObservableOnSubscribe<SocketResult>() {
            @Override
            public void subscribe(ObservableEmitter<SocketResult> e) throws Exception {
                e.onNext(sendRequest());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<SocketResult>() {
                    @Override
                    public void accept(SocketResult result) throws Exception {
//                        EventBus.getDefault().post(result);
                    }
                });
    }

}
