package com.cdzp.huinongyun.bean;

/**
 * Socket接受数据情况
 */

public class SocketResult {
    private boolean isOK = true;//是否发送或者接受数据的时候出错

    /**
     * label:接受数据完成后标记当前操作类型；
     * 155,点击开关按钮
     * 156,参数设置获取值
     * 158,保存自控参数成功
     * 160,设备控制初始化数据（DevConAutoParam）
     * 161,设置手动，自动，定时
     * .
     * .
     * .
     */
    private int label;

    private String mSender;//发送者
    private String mReceiver;//接受者
    private Object mResult;//解析后的数据类,根据上面label来确定强转类型
    private String remark = "";//备注
    private long outputCoilStatus;
    private long outputCoilStatus1;

    public long getOutputCoilStatus() {
        return outputCoilStatus;
    }

    public void setOutputCoilStatus(long outputCoilStatus) {
        this.outputCoilStatus = outputCoilStatus;
    }

    public long getOutputCoilStatus1() {
        return outputCoilStatus1;
    }

    public void setOutputCoilStatus1(long outputCoilStatus1) {
        this.outputCoilStatus1 = outputCoilStatus1;
    }

    public boolean isOK() {
        return isOK;
    }

    public void setOK(boolean OK) {
        isOK = OK;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public String getmSender() {
        return mSender;
    }

    public void setmSender(String mSender) {
        this.mSender = mSender;
    }

    public String getmReceiver() {
        return mReceiver;
    }

    public void setmReceiver(String mReceiver) {
        this.mReceiver = mReceiver;
    }

    public Object getmResult() {
        return mResult;
    }

    public void setmResult(Object mResult) {
        this.mResult = mResult;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        isOK = false;
        this.remark = remark;
    }
}
