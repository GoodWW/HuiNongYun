package com.cdzp.huinongyun.bean;

/**
 * 日光温室自控参数
 */
public class HelioGreParam {
    private byte wenduCtlMode;//温度控制方式
    private byte shiduCtlMode;//湿度控制方式
    private byte guangzhaoduCtlMode;//光照控制方式
    private byte reservedCtlMode;//预留控制方式
    private int time1;   //自然通风时间
    private int time2;   //光干扰时间
    private int time3;  //大风机间隔时间
    private int time4;  //加温开时间
    private int time5;  //加温关时间
    private int time6;   //循环风机运行时间
    private int time7;  //循环风机停止时间
    private int time8;  //充气泵启动时间
    private int time9;  //充气泵停止时间
    private int time10;  //卷膜保护时间
    private int time11;   //遮阳电机保护时间
    private int time12;   //顶窗电机保护时间
    private int time13;  //侧窗保护时间
    private int time14;  //拉幕保护时间
    private int time15;  //微喷运行时间
    private int time16;  //微喷关时间
    private float fengsu;   //风速上限
    private float wencha;  //通风温差
    private byte jijie;     //季节
    private byte youxianji; //优先级

    private String autoParamInfoGroupName;//自控参数名
    private String startTime;//起始时间
    private String endTime;//结束时间
    private boolean isUse;//是否启用

    private SetParamInfo wdInfo;//温度自控参数
    private SetParamInfo gzInfo;//光照自控参数
    private SetParamInfo sdInfo;//湿度自控参数
    private SetParamInfo blInfo;//保留

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

    public int getTime16() {
        return time16;
    }

    public void setTime16(int time16) {
        this.time16 = time16;
    }

    public float getFengsu() {
        return fengsu;
    }

    public void setFengsu(float fengsu) {
        this.fengsu = fengsu;
    }

    public float getWencha() {
        return wencha;
    }

    public void setWencha(float wencha) {
        this.wencha = wencha;
    }

    public byte getJijie() {
        return jijie;
    }

    public void setJijie(byte jijie) {
        this.jijie = jijie;
    }

    public byte getYouxianji() {
        return youxianji;
    }

    public void setYouxianji(byte youxianji) {
        this.youxianji = youxianji;
    }

    public String getAutoParamInfoGroupName() {
        return autoParamInfoGroupName;
    }

    public void setAutoParamInfoGroupName(String autoParamInfoGroupName) {
        this.autoParamInfoGroupName = autoParamInfoGroupName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isUse() {
        return isUse;
    }

    public void setUse(boolean use) {
        isUse = use;
    }

    public SetParamInfo getWdInfo() {
        return wdInfo;
    }

    public void setWdInfo(SetParamInfo wdInfo) {
        this.wdInfo = wdInfo;
    }

    public SetParamInfo getGzInfo() {
        return gzInfo;
    }

    public void setGzInfo(SetParamInfo gzInfo) {
        this.gzInfo = gzInfo;
    }

    public SetParamInfo getSdInfo() {
        return sdInfo;
    }

    public void setSdInfo(SetParamInfo sdInfo) {
        this.sdInfo = sdInfo;
    }

    public SetParamInfo getBlInfo() {
        return blInfo;
    }

    public void setBlInfo(SetParamInfo blInfo) {
        this.blInfo = blInfo;
    }
}
