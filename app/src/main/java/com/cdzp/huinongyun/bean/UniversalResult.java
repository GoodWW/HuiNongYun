package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 简单数据普遍返回结果
 */

public class UniversalResult {

    /**
     * success : true
     * data : []
     * Msg : 验证码正确，提交成功!
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
