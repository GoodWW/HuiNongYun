package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 监测点列表
 */
public class GroupConResult {

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
         * GrCoID : 1
         * GrCoName : FWTest
         * Insects : /GroupPic/201709281001
         * Seedling : /GroupPic/201709281001
         * Disaster : null
         * SoilID : 34
         * Conditon1 : 07180202009
         * Conditon2 : 1
         * Conditon3 : null
         * Conditon4 : null
         * Conditon5 : null
         * EnterpriseID : 1
         * GreenhouseID : 0
         * Status : 1
         * Remark : null
         * DisplayNo : null
         */

        private int GrCoID;
        private String GrCoName;
        private String Insects;
        private String Seedling;
        private Object Disaster;
        private int SoilID;
        private String Conditon1;
        private String Conditon2;
        private Object Conditon3;
        private Object Conditon4;
        private Object Conditon5;
        private int EnterpriseID;
        private int GreenhouseID;
        private int Status;
        private Object Remark;
        private Object DisplayNo;

        public int getGrCoID() {
            return GrCoID;
        }

        public void setGrCoID(int GrCoID) {
            this.GrCoID = GrCoID;
        }

        public String getGrCoName() {
            return GrCoName;
        }

        public void setGrCoName(String GrCoName) {
            this.GrCoName = GrCoName;
        }

        public String getInsects() {
            return Insects;
        }

        public void setInsects(String Insects) {
            this.Insects = Insects;
        }

        public String getSeedling() {
            return Seedling;
        }

        public void setSeedling(String Seedling) {
            this.Seedling = Seedling;
        }

        public Object getDisaster() {
            return Disaster;
        }

        public void setDisaster(Object Disaster) {
            this.Disaster = Disaster;
        }

        public int getSoilID() {
            return SoilID;
        }

        public void setSoilID(int SoilID) {
            this.SoilID = SoilID;
        }

        public String getConditon1() {
            return Conditon1;
        }

        public void setConditon1(String Conditon1) {
            this.Conditon1 = Conditon1;
        }

        public String getConditon2() {
            return Conditon2;
        }

        public void setConditon2(String Conditon2) {
            this.Conditon2 = Conditon2;
        }

        public Object getConditon3() {
            return Conditon3;
        }

        public void setConditon3(Object Conditon3) {
            this.Conditon3 = Conditon3;
        }

        public Object getConditon4() {
            return Conditon4;
        }

        public void setConditon4(Object Conditon4) {
            this.Conditon4 = Conditon4;
        }

        public Object getConditon5() {
            return Conditon5;
        }

        public void setConditon5(Object Conditon5) {
            this.Conditon5 = Conditon5;
        }

        public int getEnterpriseID() {
            return EnterpriseID;
        }

        public void setEnterpriseID(int EnterpriseID) {
            this.EnterpriseID = EnterpriseID;
        }

        public int getGreenhouseID() {
            return GreenhouseID;
        }

        public void setGreenhouseID(int GreenhouseID) {
            this.GreenhouseID = GreenhouseID;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int Status) {
            this.Status = Status;
        }

        public Object getRemark() {
            return Remark;
        }

        public void setRemark(Object Remark) {
            this.Remark = Remark;
        }

        public Object getDisplayNo() {
            return DisplayNo;
        }

        public void setDisplayNo(Object DisplayNo) {
            this.DisplayNo = DisplayNo;
        }
    }
}
