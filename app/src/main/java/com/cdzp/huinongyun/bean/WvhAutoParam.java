package com.cdzp.huinongyun.bean;

/**
 * 电磁阀自控参数
 */
public class WvhAutoParam {

    private int serial;  //序号,对应电磁阀控制器序号，
    private float hum_top_value; //土湿上限值
    private float hum_low_value;//土湿下限值
    private int target_layer;//目标土层
    private float water_limit;   //水量限制
    private boolean isUsed;        //是否启用

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public float getHum_top_value() {
        return hum_top_value;
    }

    public void setHum_top_value(float hum_top_value) {
        this.hum_top_value = hum_top_value;
    }

    public float getHum_low_value() {
        return hum_low_value;
    }

    public void setHum_low_value(float hum_low_value) {
        this.hum_low_value = hum_low_value;
    }

    public int getTarget_layer() {
        return target_layer;
    }

    public void setTarget_layer(int target_layer) {
        this.target_layer = target_layer;
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
}
