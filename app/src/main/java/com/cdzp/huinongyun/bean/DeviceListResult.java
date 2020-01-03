package com.cdzp.huinongyun.bean;

/**
 * 设备和通道数据
 */
public class DeviceListResult {

    /**
     * isSuccess : true
     * code : 200
     * message : 请求成功
     * result :
     * total : 100
     * id : 0
     */

    private boolean isSuccess;
    private int code;
    private String message;
    private String result;
    private int total;
    private int id;

    public boolean isIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
