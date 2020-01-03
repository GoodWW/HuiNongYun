package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 温室设备控制参数
 */

public class DevConAutoParam {
    private byte wenduCtlMode;//温度控制模式0：手动；1：自动
    private byte shiduCtlMode;//0：手动；1：自动
    private byte guangzhaoduCtlMode;//0：手动；1：自动
    private byte reservedCtlMode;//0：手动；1：自动
    private short autoParamZoneNum;//自控区域数量
    //预留时间，单位秒
    private int time1;
    private int time2;
    private int time3;
    private int time4;
    private int time5;
    private int time6;
    private int time7;
    private int time8;
    private int time9;
    private int time10;
    private int time11;
    private int time12;
    private int time13;
    private int time14;
    private int time15;

    private List<DeviceInfo> list;

    public byte getWenduCtlMode() {
        return wenduCtlMode;
    }

    public void setWenduCtlMode(byte wenduCtlMode) {
        this.wenduCtlMode = wenduCtlMode;
    }

    public byte getShiduCtlMode() {
        return shiduCtlMode;
    }

    public void setShiduCtlMode(byte shiduCtlMode) {
        this.shiduCtlMode = shiduCtlMode;
    }

    public byte getGuangzhaoduCtlMode() {
        return guangzhaoduCtlMode;
    }

    public void setGuangzhaoduCtlMode(byte guangzhaoduCtlMode) {
        this.guangzhaoduCtlMode = guangzhaoduCtlMode;
    }

    public byte getReservedCtlMode() {
        return reservedCtlMode;
    }

    public void setReservedCtlMode(byte reservedCtlMode) {
        this.reservedCtlMode = reservedCtlMode;
    }

    public short getAutoParamZoneNum() {
        return autoParamZoneNum;
    }

    public void setAutoParamZoneNum(short autoParamZoneNum) {
        this.autoParamZoneNum = autoParamZoneNum;
    }

    public int getTime1() {
        return time1;
    }

    public void setTime1(int time1) {
        this.time1 = time1;
    }

    public int getTime2() {
        return time2;
    }

    public void setTime2(int time2) {
        this.time2 = time2;
    }

    public int getTime3() {
        return time3;
    }

    public void setTime3(int time3) {
        this.time3 = time3;
    }

    public int getTime4() {
        return time4;
    }

    public void setTime4(int time4) {
        this.time4 = time4;
    }

    public int getTime5() {
        return time5;
    }

    public void setTime5(int time5) {
        this.time5 = time5;
    }

    public int getTime6() {
        return time6;
    }

    public void setTime6(int time6) {
        this.time6 = time6;
    }

    public int getTime7() {
        return time7;
    }

    public void setTime7(int time7) {
        this.time7 = time7;
    }

    public int getTime8() {
        return time8;
    }

    public void setTime8(int time8) {
        this.time8 = time8;
    }

    public int getTime9() {
        return time9;
    }

    public void setTime9(int time9) {
        this.time9 = time9;
    }

    public int getTime10() {
        return time10;
    }

    public void setTime10(int time10) {
        this.time10 = time10;
    }

    public int getTime11() {
        return time11;
    }

    public void setTime11(int time11) {
        this.time11 = time11;
    }

    public int getTime12() {
        return time12;
    }

    public void setTime12(int time12) {
        this.time12 = time12;
    }

    public int getTime13() {
        return time13;
    }

    public void setTime13(int time13) {
        this.time13 = time13;
    }

    public int getTime14() {
        return time14;
    }

    public void setTime14(int time14) {
        this.time14 = time14;
    }

    public int getTime15() {
        return time15;
    }

    public void setTime15(int time15) {
        this.time15 = time15;
    }

    public List<DeviceInfo> getList() {
        return list;
    }

    public void setList(List<DeviceInfo> list) {
        this.list = list;
    }
}
