package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 发送手机验证码数据
 */

public class RGVCResult {

    /**
     * success : false
     * data : []
     * Msg : 系统异常，请联系供应商！
     */

    private boolean success;
    private String Msg;
    private List<?> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
