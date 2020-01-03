package com.cdzp.huinongyun.bean;

public class Ys7SmIDResult {

    /**
     * success : false
     * HasError : false
     * data : {"MaID":"1","SmName":"admin","SmNickName":"超级管理员","SmPWD":"123456"}
     * Msg : 企业无萤石云子账号
     * SmID : 0
     */

    private boolean success;
    private boolean HasError;
    private DataBean data;
    private String Msg;
    private String SmID;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isHasError() {
        return HasError;
    }

    public void setHasError(boolean HasError) {
        this.HasError = HasError;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public String getSmID() {
        return SmID;
    }

    public void setSmID(String SmID) {
        this.SmID = SmID;
    }

    public static class DataBean {
        /**
         * MaID : 1
         * SmName : admin
         * SmNickName : 超级管理员
         * SmPWD : 123456
         */

        private String MaID;
        private String SmName;
        private String SmNickName;
        private String SmPWD;

        public String getMaID() {
            return MaID;
        }

        public void setMaID(String MaID) {
            this.MaID = MaID;
        }

        public String getSmName() {
            return SmName;
        }

        public void setSmName(String SmName) {
            this.SmName = SmName;
        }

        public String getSmNickName() {
            return SmNickName;
        }

        public void setSmNickName(String SmNickName) {
            this.SmNickName = SmNickName;
        }

        public String getSmPWD() {
            return SmPWD;
        }

        public void setSmPWD(String SmPWD) {
            this.SmPWD = SmPWD;
        }
    }
}
