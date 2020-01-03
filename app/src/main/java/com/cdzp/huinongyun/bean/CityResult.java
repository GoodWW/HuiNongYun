package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 城市接口数据
 */

public class CityResult {

    private boolean success;
    private String Msg;
    private List<List<DataBean>> data;

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

    public List<List<DataBean>> getData() {
        return data;
    }

    public void setData(List<List<DataBean>> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * AdID : 1
         * AdCode : 110000
         * AdName : 北京市
         * ParentCode :
         * Level : 1
         * RetrievalCode : BJS
         */

        private int AdID;
        private String AdCode;
        private String AdName;
        private String ParentCode;
        private int Level;
        private String RetrievalCode;

        public int getAdID() {
            return AdID;
        }

        public void setAdID(int AdID) {
            this.AdID = AdID;
        }

        public String getAdCode() {
            return AdCode;
        }

        public void setAdCode(String AdCode) {
            this.AdCode = AdCode;
        }

        public String getAdName() {
            return AdName;
        }

        public void setAdName(String AdName) {
            this.AdName = AdName;
        }

        public String getParentCode() {
            return ParentCode;
        }

        public void setParentCode(String ParentCode) {
            this.ParentCode = ParentCode;
        }

        public int getLevel() {
            return Level;
        }

        public void setLevel(int Level) {
            this.Level = Level;
        }

        public String getRetrievalCode() {
            return RetrievalCode;
        }

        public void setRetrievalCode(String RetrievalCode) {
            this.RetrievalCode = RetrievalCode;
        }
    }
}
