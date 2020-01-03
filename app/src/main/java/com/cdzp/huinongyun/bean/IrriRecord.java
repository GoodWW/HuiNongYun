package com.cdzp.huinongyun.bean;

/**
 * 在线施肥机灌溉记录实体类
 */
public class IrriRecord {
    private String infoName; //运行日期 yyyy-MM-dd hh:mm:ss
    private byte autoObject;//控制对象：0：灌溉阀门自动，1：储液罐自动
    private int planRunTime; //计划运行时
    private long irrValvesL;//关联阀门，低64位
    private long irrValvesH;//关联阀门，高64位
    private int realRunTime; //实际运行时长
    private float ecSetValue;//EC设定值
    private float phSetValue;//PH设定值
    private float ecRealValue;//EC实际值
    private float phRealValue;//PH实际值
    private float irriVolume; //灌溉量
    private boolean isLevelCtl; //是否为液位控制，如果是，则计划运行时长字段显示”液位控制“
    private boolean hasStorageTank; //是否有储液罐，如果有，则计划EC/PH值显示”--“
    private byte tankNumber;   //储液罐编号,保留
    private boolean isVolumeCtl; //是否为数量控制
    private byte errorCode; // 0-表示正常执行
    private String irrValves;

    public String getInfoName() {
        return infoName;
    }

    public void setInfoName(String infoName) {
        this.infoName = infoName;
    }

    public byte getAutoObject() {
        return autoObject;
    }

    public void setAutoObject(byte autoObject) {
        this.autoObject = autoObject;
    }

    public int getPlanRunTime() {
        return planRunTime;
    }

    public void setPlanRunTime(int planRunTime) {
        this.planRunTime = planRunTime;
    }

    public long getIrrValvesL() {
        return irrValvesL;
    }

    public void setIrrValvesL(long irrValvesL) {
        this.irrValvesL = irrValvesL;
    }

    public long getIrrValvesH() {
        return irrValvesH;
    }

    public void setIrrValvesH(long irrValvesH) {
        this.irrValvesH = irrValvesH;
    }

    public int getRealRunTime() {
        return realRunTime;
    }

    public void setRealRunTime(int realRunTime) {
        this.realRunTime = realRunTime;
    }

    public float getEcSetValue() {
        return ecSetValue;
    }

    public void setEcSetValue(float ecSetValue) {
        this.ecSetValue = ecSetValue;
    }

    public float getPhSetValue() {
        return phSetValue;
    }

    public void setPhSetValue(float phSetValue) {
        this.phSetValue = phSetValue;
    }

    public float getEcRealValue() {
        return ecRealValue;
    }

    public void setEcRealValue(float ecRealValue) {
        this.ecRealValue = ecRealValue;
    }

    public float getPhRealValue() {
        return phRealValue;
    }

    public void setPhRealValue(float phRealValue) {
        this.phRealValue = phRealValue;
    }

    public float getIrriVolume() {
        return irriVolume;
    }

    public void setIrriVolume(float irriVolume) {
        this.irriVolume = irriVolume;
    }

    public boolean isLevelCtl() {
        return isLevelCtl;
    }

    public void setLevelCtl(boolean levelCtl) {
        isLevelCtl = levelCtl;
    }

    public boolean isHasStorageTank() {
        return hasStorageTank;
    }

    public void setHasStorageTank(boolean hasStorageTank) {
        this.hasStorageTank = hasStorageTank;
    }

    public byte getTankNumber() {
        return tankNumber;
    }

    public void setTankNumber(byte tankNumber) {
        this.tankNumber = tankNumber;
    }

    public boolean isVolumeCtl() {
        return isVolumeCtl;
    }

    public void setVolumeCtl(boolean volumeCtl) {
        isVolumeCtl = volumeCtl;
    }

    public byte getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(byte errorCode) {
        this.errorCode = errorCode;
    }

    public String getIrrValves() {
        return irrValves;
    }

    public void setIrrValves(String irrValves) {
        this.irrValves = irrValves;
    }
}
