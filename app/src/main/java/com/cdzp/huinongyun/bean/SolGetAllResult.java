package com.cdzp.huinongyun.bean;

/**
 * 传感器土壤层数
 */
public class SolGetAllResult {

    /**
     * success : true
     * data : 2
     * Msg : 操作成功
     */

    private boolean success;
    private int data;
    private String Msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }
}
