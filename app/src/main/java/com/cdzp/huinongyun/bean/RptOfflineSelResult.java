package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 掉线统计数据
 */
public class RptOfflineSelResult {

    /**
     * success : true
     * CountNum : 1
     * data :
     * Msg : 获取成功!
     */

    private boolean success;
    private int CountNum;
    private String Msg;
    private List<List<DataBean>> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCountNum() {
        return CountNum;
    }

    public void setCountNum(int CountNum) {
        this.CountNum = CountNum;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public List<List<DataBean>> getData() {
        return data;
    }

    public void setData(List<List<DataBean>> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * rn : 1
         * COID : 21
         * CountTime : 2018-04-18
         * EnterpriseID : 1
         * GreenhouseID : 16
         * Total : 1
         * SumHour : 0.70
         * AvgMinute : 40.00
         * GreenhouseName : 温室
         */

        private String rn;
        private String COID;
        private String CountTime;
        private String EnterpriseID;
        private String GreenhouseID;
        private String Total;
        private String SumHour;
        private String AvgMinute;
        private String GreenhouseName;

        public String getRn() {
            return rn;
        }

        public void setRn(String rn) {
            this.rn = rn;
        }

        public String getCOID() {
            return COID;
        }

        public void setCOID(String COID) {
            this.COID = COID;
        }

        public String getCountTime() {
            return CountTime;
        }

        public void setCountTime(String CountTime) {
            this.CountTime = CountTime;
        }

        public String getEnterpriseID() {
            return EnterpriseID;
        }

        public void setEnterpriseID(String EnterpriseID) {
            this.EnterpriseID = EnterpriseID;
        }

        public String getGreenhouseID() {
            return GreenhouseID;
        }

        public void setGreenhouseID(String GreenhouseID) {
            this.GreenhouseID = GreenhouseID;
        }

        public String getTotal() {
            return Total;
        }

        public void setTotal(String Total) {
            this.Total = Total;
        }

        public String getSumHour() {
            return SumHour;
        }

        public void setSumHour(String SumHour) {
            this.SumHour = SumHour;
        }

        public String getAvgMinute() {
            return AvgMinute;
        }

        public void setAvgMinute(String AvgMinute) {
            this.AvgMinute = AvgMinute;
        }

        public String getGreenhouseName() {
            return GreenhouseName;
        }

        public void setGreenhouseName(String GreenhouseName) {
            this.GreenhouseName = GreenhouseName;
        }
    }
}
