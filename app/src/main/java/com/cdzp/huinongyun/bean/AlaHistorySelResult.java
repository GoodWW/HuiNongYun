package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 历史预警数据
 */
public class AlaHistorySelResult {

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
         * Serial : a7cad98d-cc1b-46c8-b95a-17e37e0a131c
         * EnterpriseID : 1
         * GreenhouseID : 16
         * SensorID : 16
         * Location : 1
         * AlarmTime : 2018-04-16 10:50:01
         * MaximizeValue : 80.00
         * MinValue : 50.00
         * AlarmType : 37
         * Status : 0
         * FReadTime :
         * ReadTime :
         * dsValue : 0
         * DoStatus : 未解除
         * DisposeTime : 2018-04-16 17:01:05
         * Remark :
         * GreenhouseName : 温室
         * OrderNo : 16
         * coord :
         * LocationName : 一区
         * TypeName : 空气湿度
         * Unit : %
         * MaxAlarmValue : 49.61
         * MinAlarmValue : 40.08
         */

        private String rn;
        private String Serial;
        private String EnterpriseID;
        private String GreenhouseID;
        private String SensorID;
        private String Location;
        private String AlarmTime;
        private String MaximizeValue;
        private String MinValue;
        private String AlarmType;
        private String Status;
        private String FReadTime;
        private String ReadTime;
        private String dsValue;
        private String DoStatus;
        private String DisposeTime;
        private String Remark;
        private String GreenhouseName;
        private String OrderNo;
        private String coord;
        private String LocationName;
        private String TypeName;
        private String Unit;
        private String MaxAlarmValue;
        private String MinAlarmValue;

        public String getRn() {
            return rn;
        }

        public void setRn(String rn) {
            this.rn = rn;
        }

        public String getSerial() {
            return Serial;
        }

        public void setSerial(String Serial) {
            this.Serial = Serial;
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

        public String getSensorID() {
            return SensorID;
        }

        public void setSensorID(String SensorID) {
            this.SensorID = SensorID;
        }

        public String getLocation() {
            return Location;
        }

        public void setLocation(String Location) {
            this.Location = Location;
        }

        public String getAlarmTime() {
            return AlarmTime;
        }

        public void setAlarmTime(String AlarmTime) {
            this.AlarmTime = AlarmTime;
        }

        public String getMaximizeValue() {
            return MaximizeValue;
        }

        public void setMaximizeValue(String MaximizeValue) {
            this.MaximizeValue = MaximizeValue;
        }

        public String getMinValue() {
            return MinValue;
        }

        public void setMinValue(String MinValue) {
            this.MinValue = MinValue;
        }

        public String getAlarmType() {
            return AlarmType;
        }

        public void setAlarmType(String AlarmType) {
            this.AlarmType = AlarmType;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getFReadTime() {
            return FReadTime;
        }

        public void setFReadTime(String FReadTime) {
            this.FReadTime = FReadTime;
        }

        public String getReadTime() {
            return ReadTime;
        }

        public void setReadTime(String ReadTime) {
            this.ReadTime = ReadTime;
        }

        public String getDsValue() {
            return dsValue;
        }

        public void setDsValue(String dsValue) {
            this.dsValue = dsValue;
        }

        public String getDoStatus() {
            return DoStatus;
        }

        public void setDoStatus(String DoStatus) {
            this.DoStatus = DoStatus;
        }

        public String getDisposeTime() {
            return DisposeTime;
        }

        public void setDisposeTime(String DisposeTime) {
            this.DisposeTime = DisposeTime;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }

        public String getGreenhouseName() {
            return GreenhouseName;
        }

        public void setGreenhouseName(String GreenhouseName) {
            this.GreenhouseName = GreenhouseName;
        }

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String OrderNo) {
            this.OrderNo = OrderNo;
        }

        public String getCoord() {
            return coord;
        }

        public void setCoord(String coord) {
            this.coord = coord;
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
    }
}
