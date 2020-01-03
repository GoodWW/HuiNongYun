package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 墒情传感器所在区域
 */
public class SoilLocSelResult {

    /**
     * success : true
     * data :
     * Msg : 获取成功!
     */

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
         * BaseID : 19
         * ClassID : 11
         * EnterpriseID : 0
         * BaseName : 一区
         * BaseValue : 1
         * MaximizeValue :
         * MinValue :
         * Remark :
         * Status : 1
         * OrderNo : 2
         * ParentID :
         * ExtraCol1 :
         * ExtraCol2 :
         */

        private String BaseID;
        private String ClassID;
        private String EnterpriseID;
        private String BaseName;
        private String BaseValue;
        private String MaximizeValue;
        private String MinValue;
        private String Remark;
        private String Status;
        private String OrderNo;
        private String ParentID;
        private String ExtraCol1;
        private String ExtraCol2;

        public String getBaseID() {
            return BaseID;
        }

        public void setBaseID(String BaseID) {
            this.BaseID = BaseID;
        }

        public String getClassID() {
            return ClassID;
        }

        public void setClassID(String ClassID) {
            this.ClassID = ClassID;
        }

        public String getEnterpriseID() {
            return EnterpriseID;
        }

        public void setEnterpriseID(String EnterpriseID) {
            this.EnterpriseID = EnterpriseID;
        }

        public String getBaseName() {
            return BaseName;
        }

        public void setBaseName(String BaseName) {
            this.BaseName = BaseName;
        }

        public String getBaseValue() {
            return BaseValue;
        }

        public void setBaseValue(String BaseValue) {
            this.BaseValue = BaseValue;
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

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String OrderNo) {
            this.OrderNo = OrderNo;
        }

        public String getParentID() {
            return ParentID;
        }

        public void setParentID(String ParentID) {
            this.ParentID = ParentID;
        }

        public String getExtraCol1() {
            return ExtraCol1;
        }

        public void setExtraCol1(String ExtraCol1) {
            this.ExtraCol1 = ExtraCol1;
        }

        public String getExtraCol2() {
            return ExtraCol2;
        }

        public void setExtraCol2(String ExtraCol2) {
            this.ExtraCol2 = ExtraCol2;
        }
    }
}
