package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 获取终端下所有传感器类型 （按传感器类型分类） 数据
 */
public class IntelTypeGroSelResult {

    /**
     * success : true
     * SensorInfo :
     * data :
     * Msg : 获取成功!
     */

    private boolean success;
    private String Msg;
    private List<SensorInfoBean> SensorInfo;
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
         * SensorType : 9
         * SensorTypeName : 五位一体
         * SensorTypeEnglishName : Sensor-5
         * IntelType :
         */

        private int SensorType;
        private String SensorTypeName;
        private String SensorTypeEnglishName;
        private List<IntelTypeBean> IntelType;

        public int getSensorType() {
            return SensorType;
        }

        public void setSensorType(int SensorType) {
            this.SensorType = SensorType;
        }

        public String getSensorTypeName() {
            return SensorTypeName;
        }

        public void setSensorTypeName(String SensorTypeName) {
            this.SensorTypeName = SensorTypeName;
        }

        public String getSensorTypeEnglishName() {
            return SensorTypeEnglishName;
        }

        public void setSensorTypeEnglishName(String SensorTypeEnglishName) {
            this.SensorTypeEnglishName = SensorTypeEnglishName;
        }

        public List<IntelTypeBean> getIntelType() {
            return IntelType;
        }

        public void setIntelType(List<IntelTypeBean> IntelType) {
            this.IntelType = IntelType;
        }

        public static class IntelTypeBean {
            /**
             * GreenhouseID : 16
             * SensorType : 9
             * SensorTypeName : 五位一体
             * IntelType : 36
             * IntelTypeName : 空气温度
             * SensorTypeEnglishName : Sensor-5
             * Unit : ℃
             * IntelID : 20
             * IsCheck : 20
             * MaxValue : 80.00
             * MinValue : 30.00
             */

            private String GreenhouseID;
            private String SensorType;
            private String SensorTypeName;
            private String IntelType;
            private String IntelTypeName;
            private String SensorTypeEnglishName;
            private String Unit;
            private String IntelID;
            private String IsCheck;
            private String MaxValue;
            private String MinValue;

            public String getGreenhouseID() {
                return GreenhouseID;
            }

            public void setGreenhouseID(String GreenhouseID) {
                this.GreenhouseID = GreenhouseID;
            }

            public String getSensorType() {
                return SensorType;
            }

            public void setSensorType(String SensorType) {
                this.SensorType = SensorType;
            }

            public String getSensorTypeName() {
                return SensorTypeName;
            }

            public void setSensorTypeName(String SensorTypeName) {
                this.SensorTypeName = SensorTypeName;
            }

            public String getIntelType() {
                return IntelType;
            }

            public void setIntelType(String IntelType) {
                this.IntelType = IntelType;
            }

            public String getIntelTypeName() {
                return IntelTypeName;
            }

            public void setIntelTypeName(String IntelTypeName) {
                this.IntelTypeName = IntelTypeName;
            }

            public String getSensorTypeEnglishName() {
                return SensorTypeEnglishName;
            }

            public void setSensorTypeEnglishName(String SensorTypeEnglishName) {
                this.SensorTypeEnglishName = SensorTypeEnglishName;
            }

            public String getUnit() {
                return Unit;
            }

            public void setUnit(String Unit) {
                this.Unit = Unit;
            }

            public String getIntelID() {
                return IntelID;
            }

            public void setIntelID(String IntelID) {
                this.IntelID = IntelID;
            }

            public String getIsCheck() {
                return IsCheck;
            }

            public void setIsCheck(String IsCheck) {
                this.IsCheck = IsCheck;
            }

            public String getMaxValue() {
                return MaxValue;
            }

            public void setMaxValue(String MaxValue) {
                this.MaxValue = MaxValue;
            }

            public String getMinValue() {
                return MinValue;
            }

            public void setMinValue(String MinValue) {
                this.MinValue = MinValue;
            }
        }
    }
}
