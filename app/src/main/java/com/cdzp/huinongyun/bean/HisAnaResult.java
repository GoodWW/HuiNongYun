package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 环境数据历史数据
 */

public class HisAnaResult {

    /**
     * success : true
     * data :
     * SensorInfo :
     * Msg : 获取成功
     */

    private boolean success;
    private DataBeanX data;
    private String Msg;
    private List<SensorInfoBean> SensorInfo;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
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

    public static class DataBeanX {
        private List<String> fullTime;
        private List<String> xAxis;
        private List<String> legend;
        private List<DataBean> data;

        public List<String> getFullTime() {
            return fullTime;
        }

        public void setFullTime(List<String> fullTime) {
            this.fullTime = fullTime;
        }

        public List<String> getXAxis() {
            return xAxis;
        }

        public void setXAxis(List<String> xAxis) {
            this.xAxis = xAxis;
        }

        public List<String> getLegend() {
            return legend;
        }

        public void setLegend(List<String> legend) {
            this.legend = legend;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * name : 空气温度(℃)
             * type : line
             * data :
             * min : 18.33
             * max : 23.23
             * avg : 19.39
             */

            private String name;
            private String type;
            private Float min;
            private Float max;
            private Float avg;
            private List<Float> data;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public Float getMin() {
                return min;
            }

            public void setMin(Float min) {
                this.min = min;
            }

            public Float getMax() {
                return max;
            }

            public void setMax(Float max) {
                this.max = max;
            }

            public Float getAvg() {
                return avg;
            }

            public void setAvg(Float avg) {
                this.avg = avg;
            }

            public List<Float> getData() {
                return data;
            }

            public void setData(List<Float> data) {
                this.data = data;
            }
        }
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
}
