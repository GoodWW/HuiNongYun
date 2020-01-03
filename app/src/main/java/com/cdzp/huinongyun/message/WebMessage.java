package com.cdzp.huinongyun.message;

/**
 * Created by cdzp on 2018/3/1.
 */

public class WebMessage {
    private boolean isOk;//接口请求是否成功
    private int label;//标记请求哪个接口
    private String mResult;//接口返回的数据

    public WebMessage(boolean isOk, int label, String mResult) {
        this.isOk = isOk;
        this.label = label;
        this.mResult = mResult;
    }

    public boolean isOk() {
        return isOk;
    }

    public int getLabel() {
        return label;
    }

    public String getmResult() {
        return mResult;
    }
}
