package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 实时报警数据
 */
public class AlarmRealSelResult {

    /**
     * success : true
     * CountNum : 5
     * SensorInfo :
     * data :
     * Msg : 获取成功!
     */

    private boolean success;
    private int CountNum;
    private String Msg;
    private List<SensorInfoBean> SensorInfo;
    private List<DataBean> data;

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

    public List<SensorInfoBean> getSensorInfo() {
        return SensorInfo;
    }

    public void setSensorInfo(List<SensorInfoBean> SensorInfo) {
        this.SensorInfo = SensorInfo;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class SensorInfoBean {
        /**
         * BaseName : 空气温度
         * ShortName : 空温
         * EnglishName : AirTemperature
         * Unit : ℃
         */

        private String BaseName;
        private String ShortName;
        private String EnglishName;
        private String Unit;

        public String getBaseName() {
            return BaseName;
        }

        public void setBaseName(String BaseName) {
            this.BaseName = BaseName;
        }

        public String getShortName() {
            return ShortName;
        }

        public void setShortName(String ShortName) {
            this.ShortName = ShortName;
        }

        public String getEnglishName() {
            return EnglishName;
        }

        public void setEnglishName(String EnglishName) {
            this.EnglishName = EnglishName;
        }

        public String getUnit() {
            return Unit;
        }

        public void setUnit(String Unit) {
            this.Unit = Unit;
        }
    }

    public static class DataBean {
        /**
         * Serial : 8fea2ea4-3df1-47c0-a28e-574a245dde83
         * EnterpriseID : 1
         * GreenhouseID : 16
         * SensorID : 64
         * Location : 1
         * AlarmTime : 2018-05-08 10:10:01
         * MaximizeValue : 80.00
         * MinValue : 30.00
         * AlarmType : 36
         * Status : 0
         * FReadTime :
         * ReadTime :
         * GreenhouseName : 温室
         * LocationName : 一区
         * TypeName : 空气温度
         * AlarmValue : 24.38
         * Unit : ℃
         */

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
        private String GreenhouseName;
        private String LocationName;
        private String TypeName;
        private String AlarmValue;
        private String Unit;

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

        public String getAlarmValue() {
            return AlarmValue;
        }

        public void setAlarmValue(String AlarmValue) {
            this.AlarmValue = AlarmValue;
        }

        public String getUnit() {
            return Unit;
        }

        public void setUnit(String Unit) {
            this.Unit = Unit;
        }
    }
}
