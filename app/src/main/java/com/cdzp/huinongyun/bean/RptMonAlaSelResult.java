package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 报警报表数据
 */
public class RptMonAlaSelResult {

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
         * CMAID : 4
         * CountTime : 2018-04-01
         * EnterpriseID : 1
         * GreenhouseID : 16
         * Location : 2
         * AlarmType : 191
         * MaxAlarmValue : 24.93
         * MinAlarmValue : 13.54
         * Total : 2
         * SumHour : 113.42
         * AvgMinute : 3402.50
         * AvgValue :
         * EnterpriseName : 智棚测试企业
         * GreenhouseName : 温室
         * LocationName : 二区
         * TypeName : .空气温度
         * Unit : ℃
         */

        private String rn;
        private String CMAID;
        private String CountTime;
        private String EnterpriseID;
        private String GreenhouseID;
        private String Location;
        private String AlarmType;
        private String MaxAlarmValue;
        private String MinAlarmValue;
        private String Total;
        private String SumHour;
        private String AvgMinute;
        private String AvgValue;
        private String EnterpriseName;
        private String GreenhouseName;
        private String LocationName;
        private String TypeName;
        private String Unit;

        public String getRn() {
            return rn;
        }

        public void setRn(String rn) {
            this.rn = rn;
        }

        public String getCMAID() {
            return CMAID;
        }

        public void setCMAID(String CMAID) {
            this.CMAID = CMAID;
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

        public String getLocation() {
            return Location;
        }

        public void setLocation(String Location) {
            this.Location = Location;
        }

        public String getAlarmType() {
            return AlarmType;
        }

        public void setAlarmType(String AlarmType) {
            this.AlarmType = AlarmType;
        }

        public String getMaxAlarmValue() {
            return MaxAlarmValue;
        }

        public void setMaxAlarmValue(String MaxAlarmValue) {
            this.MaxAlarmValue = MaxAlarmValue;
        }

        public String getMinAlarmValue() {
            return MinAlarmValue;
        }

        public void setMinAlarmValue(String MinAlarmValue) {
            this.MinAlarmValue = MinAlarmValue;
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

        public String getAvgValue() {
            return AvgValue;
        }

        public void setAvgValue(String AvgValue) {
            this.AvgValue = AvgValue;
        }

        public String getEnterpriseName() {
            return EnterpriseName;
        }

        public void setEnterpriseName(String EnterpriseName) {
            this.EnterpriseName = EnterpriseName;
        }

        public String getGreenhouseName() {
            return GreenhouseName;
        }

        public void setGreenhouseName(String GreenhouseName) {
            this.GreenhouseName = GreenhouseName;
        }

        public String getLocationName() {
            return LocationName;
        }

        public void setLocationName(String LocationName) {
            this.LocationName = LocationName;
        }

        public String getTypeName() {
            return TypeName;
        }

        public void setTypeName(String TypeName) {
            this.TypeName = TypeName;
        }

        public String getUnit() {
            return Unit;
        }

        public void setUnit(String Unit) {
            this.Unit = Unit;
        }
    }
}
