package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 掉线提醒接口数据
 */

public class GetOffListResult {

    /**
     * success : true
     * Msg : 获取成功
     */

    private boolean success;
    private String Msg;
    private List<DataBeanX> data;

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

    public List<DataBeanX> getData() {
        return data;
    }

    public void setData(List<DataBeanX> data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * GhType : 1
         * TypeName : 温室环控
         */

        private int GhType;
        private String TypeName;
        private List<DataBean> data;

        public int getGhType() {
            return GhType;
        }

        public void setGhType(int GhType) {
            this.GhType = GhType;
        }

        public String getTypeName() {
            return TypeName;
        }

        public void setTypeName(String TypeName) {
            this.TypeName = TypeName;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * rn : 1
             * OfStartTime : 2018-01-12 11:30:00
             * OfEndTime :
             * GhType : 1
             * GreenhouseName : 温室1#
             * GreenhouseID : 11
             * DisplayNo : 01123456789
             * DisplayIP : 192.168.101.70
             * DisplayPort : 55629
             * OfMinute : 89167
             * OfHour : 1486
             * BaseName : 温室环控
             */

            private String rn;
            private String OfStartTime;
            private String OfEndTime;
            private String GhType;
            private String GreenhouseName;
            private String GreenhouseID;
            private String DisplayNo;
            private String DisplayIP;
            private String DisplayPort;
            private String OfMinute;
            private String OfHour;
            private String BaseName;

            public String getRn() {
                return rn;
            }

            public void setRn(String rn) {
                this.rn = rn;
            }

            public String getOfStartTime() {
                return OfStartTime;
            }

            public void setOfStartTime(String OfStartTime) {
                this.OfStartTime = OfStartTime;
            }

            public String getOfEndTime() {
                return OfEndTime;
            }

            public void setOfEndTime(String OfEndTime) {
                this.OfEndTime = OfEndTime;
            }

            public String getGhType() {
                return GhType;
            }

            public void setGhType(String GhType) {
                this.GhType = GhType;
            }

            public String getGreenhouseName() {
                return GreenhouseName;
            }

            public void setGreenhouseName(String GreenhouseName) {
                this.GreenhouseName = GreenhouseName;
            }

            public String getGreenhouseID() {
                return GreenhouseID;
            }

            public void setGreenhouseID(String GreenhouseID) {
                this.GreenhouseID = GreenhouseID;
            }

            public String getDisplayNo() {
                return DisplayNo;
            }

            public void setDisplayNo(String DisplayNo) {
                this.DisplayNo = DisplayNo;
            }

            public String getDisplayIP() {
                return DisplayIP;
            }

            public void setDisplayIP(String DisplayIP) {
                this.DisplayIP = DisplayIP;
            }

            public String getDisplayPort() {
                return DisplayPort;
            }

            public void setDisplayPort(String DisplayPort) {
                this.DisplayPort = DisplayPort;
            }

            public String getOfMinute() {
                return OfMinute;
            }

            public void setOfMinute(String OfMinute) {
                this.OfMinute = OfMinute;
            }

            public String getOfHour() {
                return OfHour;
            }

            public void setOfHour(String OfHour) {
                this.OfHour = OfHour;
            }

            public String getBaseName() {
                return BaseName;
            }

            public void setBaseName(String BaseName) {
                this.BaseName = BaseName;
            }
        }
    }
}
