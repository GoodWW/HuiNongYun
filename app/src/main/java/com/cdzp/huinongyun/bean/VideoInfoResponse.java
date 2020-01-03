package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * appkey
 */
public class VideoInfoResponse {

    /**
     * success : true
     * data :
     * Msg : 获取成功！
     */

    private boolean success;
    private String Msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * AppKey : cad1b5e58231481a9383cc3afd23867a
         * AccessToken : at.39xuujmpacydtmhwbfkp25kf0f5yi708-3us7l0120e-13b4ak5-vga1yidxc
         */

        private String AppKey;
        private String AccessToken;

        public String getAppKey() {
            return AppKey;
        }

        public void setAppKey(String AppKey) {
            this.AppKey = AppKey;
        }

        public String getAccessToken() {
            return AccessToken;
        }

        public void setAccessToken(String AccessToken) {
            this.AccessToken = AccessToken;
        }
    }
}
