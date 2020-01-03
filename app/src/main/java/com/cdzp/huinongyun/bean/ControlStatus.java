package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 施肥机状态结构体
 */
public class ControlStatus {

    /**
     * success : true
     * data :
     * Msg : 操作成功
     */

    private boolean success;
    private DataBean data;
    private String Msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public static class DataBean {
        /**
         * RunMode : 0
         * FertRunStop : 0
         * StorageRunStop : 0
         * EC1 : 0.132442  ------EC后面
         * EC2 : 0.126358
         * EC_Pre : 0.131225   ------EC前
         * EC1Temp : -92.0178
         * EC2Temp : -95.2447
         * EC_PreTemp : -92.6397
         * PH1 : 6.55151  ------PH
         * PH2 : 0
         * CurrentLevelVec : [0,-1.24841,-1.25,-1.25,-1.25,-1.25]
         * SwitchLevel : 0   ------混肥桶的高低（第0位：1高）
         * IrrValvesStatusL : 0
         * IrrValvesStatusH : 0
         * PumpStatus : 0
         * FertValveStatus : 0
         * WaterAmount : 0   ------水量
         * RunTime : 0
         * FlowSpeed : 0   ------流速
         * ErrorFlag : 0   ------异常（0正常，其他显示数据）
         * ValveRunStatus : 0
         * StorageRunStatus : 0
         * StorageValveStatus : 0
         * FertAcid_Duty : 0
         * FertABCD_Duty : [0,0,0,0]
         */

        private int RunMode;
        private int FertRunStop;
        private int StorageRunStop;
        private double EC1;
        private double EC2;
        private double EC_Pre;
        private double EC1Temp;
        private double EC2Temp;
        private double EC_PreTemp;
        private double PH1;
        private double PH2;
        private int SwitchLevel;
        private long IrrValvesStatusL;
        private long IrrValvesStatusH;
        private int PumpStatus;
        private int FertValveStatus;
        private double WaterAmount;
        private int RunTime;
        private double FlowSpeed;
        private int ErrorFlag;
        private int ValveRunStatus;
        private int StorageRunStatus;
        private int StorageValveStatus;
        private double FertAcid_Duty;
        private List<Double> CurrentLevelVec;
        private List<Double> FertABCD_Duty;

        public int getRunMode() {
            return RunMode;
        }

        public void setRunMode(int RunMode) {
            this.RunMode = RunMode;
        }

        public int getFertRunStop() {
            return FertRunStop;
        }

        public void setFertRunStop(int FertRunStop) {
            this.FertRunStop = FertRunStop;
        }

        public int getStorageRunStop() {
            return StorageRunStop;
        }

        public void setStorageRunStop(int StorageRunStop) {
            this.StorageRunStop = StorageRunStop;
        }

        public double getEC1() {
            return EC1;
        }

        public void setEC1(double EC1) {
            this.EC1 = EC1;
        }

        public double getEC2() {
            return EC2;
        }

        public void setEC2(double EC2) {
            this.EC2 = EC2;
        }

        public double getEC_Pre() {
            return EC_Pre;
        }

        public void setEC_Pre(double EC_Pre) {
            this.EC_Pre = EC_Pre;
        }

        public double getEC1Temp() {
            return EC1Temp;
        }

        public void setEC1Temp(double EC1Temp) {
            this.EC1Temp = EC1Temp;
        }

        public double getEC2Temp() {
            return EC2Temp;
        }

        public void setEC2Temp(double EC2Temp) {
            this.EC2Temp = EC2Temp;
        }

        public double getEC_PreTemp() {
            return EC_PreTemp;
        }

        public void setEC_PreTemp(double EC_PreTemp) {
            this.EC_PreTemp = EC_PreTemp;
        }

        public double getPH1() {
            return PH1;
        }

        public void setPH1(double PH1) {
            this.PH1 = PH1;
        }

        public double getPH2() {
            return PH2;
        }

        public void setPH2(double PH2) {
            this.PH2 = PH2;
        }

        public int getSwitchLevel() {
            return SwitchLevel;
        }

        public void setSwitchLevel(int SwitchLevel) {
            this.SwitchLevel = SwitchLevel;
        }

        public long getIrrValvesStatusL() {
            return IrrValvesStatusL;
        }

        public void setIrrValvesStatusL(long IrrValvesStatusL) {
            this.IrrValvesStatusL = IrrValvesStatusL;
        }

        public long getIrrValvesStatusH() {
            return IrrValvesStatusH;
        }

        public void setIrrValvesStatusH(long IrrValvesStatusH) {
            this.IrrValvesStatusH = IrrValvesStatusH;
        }

        public int getPumpStatus() {
            return PumpStatus;
        }

        public void setPumpStatus(int PumpStatus) {
            this.PumpStatus = PumpStatus;
        }

        public int getFertValveStatus() {
            return FertValveStatus;
        }

        public void setFertValveStatus(int FertValveStatus) {
            this.FertValveStatus = FertValveStatus;
        }

        public double getWaterAmount() {
            return WaterAmount;
        }

        public void setWaterAmount(double WaterAmount) {
            this.WaterAmount = WaterAmount;
        }

        public int getRunTime() {
            return RunTime;
        }

        public void setRunTime(int RunTime) {
            this.RunTime = RunTime;
        }

        public double getFlowSpeed() {
            return FlowSpeed;
        }

        public void setFlowSpeed(double FlowSpeed) {
            this.FlowSpeed = FlowSpeed;
        }

        public int getErrorFlag() {
            return ErrorFlag;
        }

        public void setErrorFlag(int ErrorFlag) {
            this.ErrorFlag = ErrorFlag;
        }

        public int getValveRunStatus() {
            return ValveRunStatus;
        }

        public void setValveRunStatus(int ValveRunStatus) {
            this.ValveRunStatus = ValveRunStatus;
        }

        public int getStorageRunStatus() {
            return StorageRunStatus;
        }

        public void setStorageRunStatus(int StorageRunStatus) {
            this.StorageRunStatus = StorageRunStatus;
        }

        public int getStorageValveStatus() {
            return StorageValveStatus;
        }

        public void setStorageValveStatus(int StorageValveStatus) {
            this.StorageValveStatus = StorageValveStatus;
        }

        public double getFertAcid_Duty() {
            return FertAcid_Duty;
        }

        public void setFertAcid_Duty(double FertAcid_Duty) {
            this.FertAcid_Duty = FertAcid_Duty;
        }

        public List<Double> getCurrentLevelVec() {
            return CurrentLevelVec;
        }

        public void setCurrentLevelVec(List<Double> CurrentLevelVec) {
            this.CurrentLevelVec = CurrentLevelVec;
        }

        public List<Double> getFertABCD_Duty() {
            return FertABCD_Duty;
        }

        public void setFertABCD_Duty(List<Double> FertABCD_Duty) {
            this.FertABCD_Duty = FertABCD_Duty;
        }
    }
}
