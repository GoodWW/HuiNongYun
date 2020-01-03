package com.cdzp.huinongyun.bean;

/**
 * 在线施肥机通讯实体类
 */
public class OnlAutoCtlParamter {
    private String infoName = "";
    private byte StartCondition;//启动条件:0:时间段内，1:低液位启动
    private byte AutoObject;//控制对象：0：灌溉阀门，1：储液罐
    private byte weekDay;//时间段控制的星期选择，每一位表示一天，bit0表示星期一，bit6表示星期日
    private String startTime;//时间控制开始时间 yyyyMMddhhmmss
    private String stopTime;//时间控制结束时间 yyyyMMddhhmmss
    private byte storageTankNum;//储液罐：0~3号
    private short lowLevel;//低液位控制 液位低于设定值启动
    private short highLevel;//高液位停止 单位 ：cm
    private int runTime;// 每次开启时间 单位 ： s
    private int pauseTime;// 每次暂停时间 单位 ： s
    private byte maxValveNum;//最多允许开启阀门数
    private float ECSetValue;//EC设定值
    private float PHSetValue;//PH设定值
    private byte FertCh;//需要打开的 施肥阀 bit0- 酸， bit1-A …
    private long IrrValvesL;//关联阀门，低64位
    private long IrrValvesH;//关联阀门，高64位
    private byte timeCtrType; //时间控制类型，0-只执行一次，1-自定义星期，2-自定义间隔天数
    private byte manualCtrMode; //0-时间控制  1-水量控制
    private float waterLimit; // water limit (m3)
    private byte FertCtlMode;//配肥模式 0:EC  1:比例
    private float Proportion_A;//A桶比例
    private float Proportion_B;//B桶比例
    private float Proportion_C;//C桶比例
    private float Proportion_D;//D桶比例

    public String getInfoName() {
        return infoName;
    }

    public void setInfoName(String infoName) {
        this.infoName = infoName;
    }

    public byte getStartCondition() {
        return StartCondition;
    }

    public void setStartCondition(byte startCondition) {
        StartCondition = startCondition;
    }

    public byte getAutoObject() {
        return AutoObject;
    }

    public void setAutoObject(byte autoObject) {
        AutoObject = autoObject;
    }

    public byte getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(byte weekDay) {
        this.weekDay = weekDay;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public byte getStorageTankNum() {
        return storageTankNum;
    }

    public void setStorageTankNum(byte storageTankNum) {
        this.storageTankNum = storageTankNum;
    }

    public short getLowLevel() {
        return lowLevel;
    }

    public void setLowLevel(short lowLevel) {
        this.lowLevel = lowLevel;
    }

    public short getHighLevel() {
        return highLevel;
    }

    public void setHighLevel(short highLevel) {
        this.highLevel = highLevel;
    }

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public int getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(int pauseTime) {
        this.pauseTime = pauseTime;
    }

    public byte getMaxValveNum() {
        return maxValveNum;
    }

    public void setMaxValveNum(byte maxValveNum) {
        this.maxValveNum = maxValveNum;
    }

    public float getECSetValue() {
        return ECSetValue;
    }

    public void setECSetValue(float ECSetValue) {
        this.ECSetValue = ECSetValue;
    }

    public float getPHSetValue() {
        return PHSetValue;
    }

    public void setPHSetValue(float PHSetValue) {
        this.PHSetValue = PHSetValue;
    }

    public byte getFertCh() {
        return FertCh;
    }

    public void setFertCh(byte fertCh) {
        FertCh = fertCh;
    }

    public long getIrrValvesL() {
        return IrrValvesL;
    }

    public void setIrrValvesL(long irrValvesL) {
        IrrValvesL = irrValvesL;
    }

    public long getIrrValvesH() {
        return IrrValvesH;
    }

    public void setIrrValvesH(long irrValvesH) {
        IrrValvesH = irrValvesH;
    }

    public byte getTimeCtrType() {
        return timeCtrType;
    }

    public void setTimeCtrType(byte timeCtrType) {
        this.timeCtrType = timeCtrType;
    }

    public byte getManualCtrMode() {
        return manualCtrMode;
    }

    public void setManualCtrMode(byte manualCtrMode) {
        this.manualCtrMode = manualCtrMode;
    }

    public float getWaterLimit() {
        return waterLimit;
    }

    public void setWaterLimit(float waterLimit) {
        this.waterLimit = waterLimit;
    }

    public byte getFertCtlMode() {
        return FertCtlMode;
    }

    public void setFertCtlMode(byte fertCtlMode) {
        FertCtlMode = fertCtlMode;
    }

    public float getProportion_A() {
        return Proportion_A;
    }

    public void setProportion_A(float proportion_A) {
        Proportion_A = proportion_A;
    }

    public float getProportion_B() {
        return Proportion_B;
    }

    public void setProportion_B(float proportion_B) {
        Proportion_B = proportion_B;
    }

    public float getProportion_C() {
        return Proportion_C;
    }

    public void setProportion_C(float proportion_C) {
        Proportion_C = proportion_C;
    }

    public float getProportion_D() {
        return Proportion_D;
    }

    public void setProportion_D(float proportion_D) {
        Proportion_D = proportion_D;
    }
}
