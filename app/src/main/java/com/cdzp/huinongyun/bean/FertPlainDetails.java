package com.cdzp.huinongyun.bean;

/**
 * 施肥机计划参数
 */
public class FertPlainDetails {

    private byte mplan;
    private String PlainName;//此处可忽略
    private String PlainFirstDay;//首日运行//此处可忽略
    private String PlainEndDay;//结束运行//此处可忽略
    private String StartTime;//此处可忽略
    private String StopTime;//此处可忽略

    private byte IntervalDays;//间隔天数//此处可忽略
    private byte IntervalHours;//间隔小时//此处可忽略
    private byte IntervalMinutes;//间隔分钟//此处可忽略
    private byte PlainMode;//方式：1:施肥、0:灌溉
    private byte ECType;// ec类型 =0

    private short ECSetValue;//ec设定值 单位 uS/cm

    private byte PHType;//ph类型=0
    private short PHSetValue;//ph设定值 单位 0.01PH
    private byte FerCh;//进肥通道选择
    private long IrrigationValve;//灌溉阀选择
    private byte WaterQuantityCtlMode;//水量控制，0：水量，1：时间
    private short WaterCtlValue;//水量控制值：立方、分钟
    private byte isUsed;//此处可忽略
    private long Reserved;

    public byte getMplan() {
        return mplan;
    }

    public void setMplan(byte mplan) {
        this.mplan = mplan;
    }

    public String getPlainName() {
        return PlainName;
    }

    public void setPlainName(String plainName) {
        PlainName = plainName;
    }

    public String getPlainFirstDay() {
        return PlainFirstDay;
    }

    public void setPlainFirstDay(String plainFirstDay) {
        PlainFirstDay = plainFirstDay;
    }

    public String getPlainEndDay() {
        return PlainEndDay;
    }

    public void setPlainEndDay(String plainEndDay) {
        PlainEndDay = plainEndDay;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getStopTime() {
        return StopTime;
    }

    public void setStopTime(String stopTime) {
        StopTime = stopTime;
    }

    public byte getIntervalDays() {
        return IntervalDays;
    }

    public void setIntervalDays(byte intervalDays) {
        IntervalDays = intervalDays;
    }

    public byte getIntervalHours() {
        return IntervalHours;
    }

    public void setIntervalHours(byte intervalHours) {
        IntervalHours = intervalHours;
    }

    public byte getIntervalMinutes() {
        return IntervalMinutes;
    }

    public void setIntervalMinutes(byte intervalMinutes) {
        IntervalMinutes = intervalMinutes;
    }

    public byte getPlainMode() {
        return PlainMode;
    }

    public void setPlainMode(byte plainMode) {
        PlainMode = plainMode;
    }

    public byte getECType() {
        return ECType;
    }

    public void setECType(byte ECType) {
        this.ECType = ECType;
    }

    public short getECSetValue() {
        return ECSetValue;
    }

    public void setECSetValue(short ECSetValue) {
        this.ECSetValue = ECSetValue;
    }

    public byte getPHType() {
        return PHType;
    }

    public void setPHType(byte PHType) {
        this.PHType = PHType;
    }

    public short getPHSetValue() {
        return PHSetValue;
    }

    public void setPHSetValue(short PHSetValue) {
        this.PHSetValue = PHSetValue;
    }

    public byte getFerCh() {
        return FerCh;
    }

    public void setFerCh(byte ferCh) {
        FerCh = ferCh;
    }

    public long getIrrigationValve() {
        return IrrigationValve;
    }

    public void setIrrigationValve(long irrigationValve) {
        IrrigationValve = irrigationValve;
    }

    public byte getWaterQuantityCtlMode() {
        return WaterQuantityCtlMode;
    }

    public void setWaterQuantityCtlMode(byte waterQuantityCtlMode) {
        WaterQuantityCtlMode = waterQuantityCtlMode;
    }

    public short getWaterCtlValue() {
        return WaterCtlValue;
    }

    public void setWaterCtlValue(short waterCtlValue) {
        WaterCtlValue = waterCtlValue;
    }

    public byte getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(byte isUsed) {
        this.isUsed = isUsed;
    }

    public long getReserved() {
        return Reserved;
    }

    public void setReserved(long reserved) {
        Reserved = reserved;
    }
}
