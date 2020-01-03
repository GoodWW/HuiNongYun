package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 电磁阀控制器列表
 */
public class SolGetCtlListResult {

    /**
     * success : true
     * total : 66
     * data :
     * Msg : 操作成功
     */

    private boolean success;
    private int total;
    private String Msg;
    private List<DataBean> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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
         * GreenhouseID : 79
         * GreenhouseName : 灌溉1区
         * DisplayNo : 0
         * IrrigationState : 1
         * OutputCoilStatus : 255
         * InputCoilStatus : 40
         * IsUsed : 1
         */

        private int GreenhouseID;
        private String GreenhouseName;
        private String DisplayNo;
        private String IrrigationState;
        private int OutputCoilStatus;
        private int InputCoilStatus;
        private int IsUsed;

        public int getGreenhouseID() {
            return GreenhouseID;
        }

        public void setGreenhouseID(int GreenhouseID) {
            this.GreenhouseID = GreenhouseID;
        }

        public String getGreenhouseName() {
            return GreenhouseName;
        }

        public void setGreenhouseName(String GreenhouseName) {
            this.GreenhouseName = GreenhouseName;
        }

        public String getDisplayNo() {
            return DisplayNo;
        }

        public void setDisplayNo(String DisplayNo) {
            this.DisplayNo = DisplayNo;
        }

        public String getIrrigationState() {
            return IrrigationState;
        }

        public void setIrrigationState(String IrrigationState) {
            this.IrrigationState = IrrigationState;
        }

        public int getOutputCoilStatus() {
            return OutputCoilStatus;
        }

        public void setOutputCoilStatus(int OutputCoilStatus) {
            this.OutputCoilStatus = OutputCoilStatus;
        }

        public int getInputCoilStatus() {
            return InputCoilStatus;
        }

        public void setInputCoilStatus(int InputCoilStatus) {
            this.InputCoilStatus = InputCoilStatus;
        }

        public int getIsUsed() {
            return IsUsed;
        }

        public void setIsUsed(int IsUsed) {
            this.IsUsed = IsUsed;
        }
    }
}
