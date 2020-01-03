package com.cdzp.huinongyun.bean;

/**
 * 施肥机自动参数
 */
public class AutoDetails {

    private byte AutoDetail;//区域
    private String AutoName;
    private byte Localtion;//区域
    private byte Type; //传感器类型
    private byte sensorIndex;//传感器数组下标
    private float UpperLimit1;//上限
    private float LowerLimit1;//下限
    private float UpperLimit2;//上限
    private float LowerLimit2;//下限
    private byte AutoMode;//方式 施肥灌溉
    private byte ECType;// ec类型
    private short ECSetValue;//ec设定值
    private byte PHType;//ph类型
    private short PHSetValue;//ph设定值
    private byte FerCh;//进肥通道
    private long IrrigationValve;//灌溉阀
    private byte WaterQuantityCtlMode;//水量控制，0：水量，1：时间
    private short WaterCtlValue;//水量控制值：立方，分钟
    private byte isUsed;
    private long Reserved;

    public byte getAutoDetail() {
        return AutoDetail;
    }

    public void setAutoDetail(byte autoDetail) {
        AutoDetail = autoDetail;
    }

    public String getAutoName() {
        return AutoName;
    }

    public void setAutoName(String autoName) {
        AutoName = autoName;
    }

    public byte getLocaltion() {
        return Localtion;
    }

    public void setLocaltion(byte localtion) {
        Localtion = localtion;
    }

    public byte getType() {
        return Type;
    }

    public void setType(byte type) {
        Type = type;
    }

    public byte getSensorIndex() {
        return sensorIndex;
    }

    public void setSensorIndex(byte sensorIndex) {
        this.sensorIndex = sensorIndex;
    }

    public float getUpperLimit1() {
        return UpperLimit1;
    }

    public void setUpperLimit1(float upperLimit1) {
        UpperLimit1 = upperLimit1;
    }

    public float getLowerLimit1() {
        return LowerLimit1;
    }

    public void setLowerLimit1(float lowerLimit1) {
        LowerLimit1 = lowerLimit1;
    }

    public float getUpperLimit2() {
        return UpperLimit2;
    }

    public void setUpperLimit2(float upperLimit2) {
        UpperLimit2 = upperLimit2;
    }

    public float getLowerLimit2() {
        return LowerLimit2;
    }

    public void setLowerLimit2(float lowerLimit2) {
        LowerLimit2 = lowerLimit2;
    }

    public byte getAutoMode() {
        return AutoMode;
    }

    public void setAutoMode(byte autoMode) {
        AutoMode = autoMode;
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
