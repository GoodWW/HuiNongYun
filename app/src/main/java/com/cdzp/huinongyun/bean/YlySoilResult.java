package com.cdzp.huinongyun.bean;

/**
 * 原力元土壤数据
 */
public class YlySoilResult {

    /**
     * code : 200
     * message : success
     * data :
     */

    private String code;
    private String message;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 2139
         * deviceEui : 4a770538000001
         * deviceType : 2
         * timeStam : 2018-06-11 12:13:41
         * wenDu : 24.6
         * shuiFen : 18.2
         */

        private int id;
        private String deviceEui;
        private int deviceType;
        private String timeStam;
        private String wenDu;
        private String shuiFen;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDeviceEui() {
            return deviceEui;
        }

        public void setDeviceEui(String deviceEui) {
            this.deviceEui = deviceEui;
        }

        public int getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(int deviceType) {
            this.deviceType = deviceType;
        }

        public String getTimeStam() {
            return timeStam;
        }

        public void setTimeStam(String timeStam) {
            this.timeStam = timeStam;
        }

        public String getWenDu() {
            return wenDu;
        }

        public void setWenDu(String wenDu) {
            this.wenDu = wenDu;
        }

        public String getShuiFen() {
            return shuiFen;
        }

        public void setShuiFen(String shuiFen) {
            this.shuiFen = shuiFen;
        }
    }
}
