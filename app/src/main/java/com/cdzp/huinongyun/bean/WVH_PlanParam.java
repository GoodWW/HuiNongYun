package com.cdzp.huinongyun.bean;

/**
 * 电磁阀计划灌溉数据结构
 */
public class WVH_PlanParam {

    private long planHigh;
    private long planLow;
    private int serialNum;

    private String first_run_date;//首日运行日期
    private String start_time;    //起始时间
    private String end_time;    //结束时间
    private short interval;    //间隔天数
    private float water_limit;    //水量限制
    private boolean isUsed;        //是否启用
    private long IrriZone_H;    //灌溉区域高位，每一位表示一个区域，1有效
    private long IrriZone_L;    //灌溉区域低位，每一位表示一个区域，1有效
    private short runTime;   //运行时间
    private short pushTime;   //暂停时间

    public short getRunTime() {
        return runTime;
    }

    public void setRunTime(short runTime) {
        this.runTime = runTime;
    }

    public short getPushTime() {
        return pushTime;
    }

    public void setPushTime(short pushTime) {
        this.pushTime = pushTime;
    }

    public int getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(int serialNum) {
        this.serialNum = serialNum;
    }

    public long getPlanHigh() {
        return planHigh;
    }

    public void setPlanHigh(long planHigh) {
        this.planHigh = planHigh;
    }

    public long getPlanLow() {
        return planLow;
    }

    public void setPlanLow(long planLow) {
        this.planLow = planLow;
    }

    public String getFirst_run_date() {
        return first_run_date;
    }

    public void setFirst_run_date(String first_run_date) {
        this.first_run_date = first_run_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public short getInterval() {
        return interval;
    }

    public void setInterval(short interval) {
        this.interval = interval;
    }

    public float getWater_limit() {
        return water_limit;
    }

    public void setWater_limit(float water_limit) {
        this.water_limit = water_limit;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public long getIrriZone_H() {
        return IrriZone_H;
    }

    public void setIrriZone_H(long irriZone_H) {
        IrriZone_H = irriZone_H;
    }

    public long getIrriZone_L() {
        return IrriZone_L;
    }

    public void setIrriZone_L(long irriZone_L) {
        IrriZone_L = irriZone_L;
    }
}
