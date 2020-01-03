package com.cdzp.huinongyun.bean;

/**
 * 日光温室设备信息结构
 */
public class HelioGreDeviceInfo {

    private String devName;//设备中文名
    private byte image;//设备头像编号
    private byte devCtlMode;//设备控制方式
    private byte temDevCtlMode;//设备控制方式(临时)
    private String enDevName;//设备英文名
    private byte zhengFanZhuan;//设备是否正反转
    private byte Localtion;    //区域
    private byte Stype;     //类型
    private byte onlimit;     //开限位
    private byte offlimit;     //关限位
    private byte regAdd1;//设备输出点1
    private byte regAdd2;//设备输出点2
    private byte timersCnt; //定时时间组数
    private int[] timers; //[8]元素个数固定为8
    //格式：打开时间       关闭时间
    //     时     分       时      分
    //  bit:24-31  bit:16-23  bit:8-15  bit:0-7
    private boolean one, two;//第一个和第二个是否开关

    public HelioGreDeviceInfo() {
        timers = new int[8];
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public byte getImage() {
        return image;
    }

    public void setImage(byte image) {
        this.image = image;
    }

    public byte getDevCtlMode() {
        return devCtlMode;
    }

    public void setDevCtlMode(byte devCtlMode) {
        this.devCtlMode = devCtlMode;
    }

    public byte getTemDevCtlMode() {
        return temDevCtlMode;
    }

    public void setTemDevCtlMode(byte temDevCtlMode) {
        this.temDevCtlMode = temDevCtlMode;
    }

    public String getEnDevName() {
        return enDevName;
    }

    public void setEnDevName(String enDevName) {
        this.enDevName = enDevName;
    }

    public byte getZhengFanZhuan() {
        return zhengFanZhuan;
    }

    public void setZhengFanZhuan(byte zhengFanZhuan) {
        this.zhengFanZhuan = zhengFanZhuan;
    }

    public byte getLocaltion() {
        return Localtion;
    }

    public void setLocaltion(byte localtion) {
        Localtion = localtion;
    }

    public byte getStype() {
        return Stype;
    }

    public void setStype(byte stype) {
        Stype = stype;
    }

    public byte getOnlimit() {
        return onlimit;
    }

    public void setOnlimit(byte onlimit) {
        this.onlimit = onlimit;
    }

    public byte getOfflimit() {
        return offlimit;
    }

    public void setOfflimit(byte offlimit) {
        this.offlimit = offlimit;
    }

    public byte getRegAdd1() {
        return regAdd1;
    }

    public void setRegAdd1(byte regAdd1) {
        this.regAdd1 = regAdd1;
    }

    public byte getRegAdd2() {
        return regAdd2;
    }

    public void setRegAdd2(byte regAdd2) {
        this.regAdd2 = regAdd2;
    }

    public byte getTimersCnt() {
        return timersCnt;
    }

    public void setTimersCnt(byte timersCnt) {
        this.timersCnt = timersCnt;
    }

    public int[] getTimers() {
        return timers;
    }

    public void setTimers(int[] timers) {
        this.timers = timers;
    }

    public boolean isOne() {
        return one;
    }

    public void setOne(boolean one) {
        this.one = one;
    }

    public boolean isTwo() {
        return two;
    }

    public void setTwo(boolean two) {
        this.two = two;
    }
}
