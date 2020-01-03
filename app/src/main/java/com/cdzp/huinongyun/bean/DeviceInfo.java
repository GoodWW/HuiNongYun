package com.cdzp.huinongyun.bean;

/**
 * 设备控制初始化得到的数据
 */

public class DeviceInfo {
    private String devName;//设备中文名
    private byte image;//设备头像编号
    private byte regAdd;//设备端口编号
    private byte devCtlMode;//设备控制方式
    private byte temDevCtlMode;//设备控制方式(临时)
    private byte zhengFanZhuan;//设备是否正反转
    private String enDevName;//设备英文名
    private boolean one, two;//第一个和第二个是否开关

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

    public byte getRegAdd() {
        return regAdd;
    }

    public void setRegAdd(byte regAdd) {
        this.regAdd = regAdd;
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

    public byte getZhengFanZhuan() {
        return zhengFanZhuan;
    }

    public void setZhengFanZhuan(byte zhengFanZhuan) {
        this.zhengFanZhuan = zhengFanZhuan;
    }

    public String getEnDevName() {
        return enDevName;
    }

    public void setEnDevName(String enDevName) {
        this.enDevName = enDevName;
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
