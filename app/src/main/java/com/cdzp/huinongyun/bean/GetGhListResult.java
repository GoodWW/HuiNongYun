package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 设备维护数据
 */

public class GetGhListResult {

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
             * GreenhouseID : 11
             * GreenhouseName : 温室1#
             * DisplayNo : 01123456789
             * DisplayIP : 192.168.101.70
             * DisplayPort : 55629
             * GhType : 1
             * BaseName : 温室环控
             */

            private String rn;
            private String GreenhouseID;
            private String GreenhouseName;
            private String DisplayNo;
            private String DisplayIP;
            private String DisplayPort;
            private String GhType;
            private String BaseName;
            private String Remark;

            public String getRemark() {
                return Remark;
            }

            public void setRemark(String remark) {
                Remark = remark;
            }

            public String getRn() {
                return rn;
            }

            public void setRn(String rn) {
                this.rn = rn;
            }

            public String getGreenhouseID() {
                return GreenhouseID;
            }

            public void setGreenhouseID(String GreenhouseID) {
                this.GreenhouseID = GreenhouseID;
            }

            public String getGreenhouseName() {
                return GreenhouseName;
            }

            public void setGreenhouseName(String GreenhouseName) {
                this.GreenhouseName = GreenhouseName;
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

            public String getGhType() {
                return GhType;
            }

            public void setGhType(String GhType) {
                this.GhType = GhType;
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
