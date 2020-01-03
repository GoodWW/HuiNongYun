package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 传感器的设置信息（中文、英文、短名称、单位）
 */
public class SensorInfoSelResult {

    /**
     * success : true
     * data :
     * Msg : 获取成功!
     */

    private boolean success;
    private String Msg;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
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
