package com.cdzp.huinongyun.bean;

/**
 * 施肥机的当前状态数据
 */
public class CurrentStateResult {

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
         * CtlMod : 0
         * PlainMod : 1
         * ValvesNum : 16
         * FertCh : 7
         * RunStop : 1
         * EC : 0.13
         * PH : 3
         * WaterMeter : 400
         * LiquidLevel : 63
         * IrrValvesState : 384
         * FerChState : 0
         * PumpState : 5
         * Name : 1
         * ZoneNum : 4
         * GHType : 2
         * GHName : 大田测试
         */

        private int CtlMod;
        private int PlainMod;
        private int ValvesNum;
        private int FertCh;
        private int RunStop;
        private double EC;
        private double PH;
        private int WaterMeter;
        private int LiquidLevel;
        private long IrrValvesState;
        private int FerChState;
        private int PumpState;
        private String Name;
        private int ZoneNum;
        private int GHType;
        private String GHName;

        public int getCtlMod() {
            return CtlMod;
        }

        public void setCtlMod(int CtlMod) {
            this.CtlMod = CtlMod;
        }

        public int getPlainMod() {
            return PlainMod;
        }

        public void setPlainMod(int PlainMod) {
            this.PlainMod = PlainMod;
        }

        public int getValvesNum() {
            return ValvesNum;
        }

        public void setValvesNum(int ValvesNum) {
            this.ValvesNum = ValvesNum;
        }

        public int getFertCh() {
            return FertCh;
        }

        public void setFertCh(int FertCh) {
            this.FertCh = FertCh;
        }

        public int getRunStop() {
            return RunStop;
        }

        public void setRunStop(int RunStop) {
            this.RunStop = RunStop;
        }

        public double getEC() {
            return EC;
        }

        public void setEC(double EC) {
            this.EC = EC;
        }

        public double getPH() {
            return PH;
        }

        public void setPH(double PH) {
            this.PH = PH;
        }

        public int getWaterMeter() {
            return WaterMeter;
        }

        public void setWaterMeter(int WaterMeter) {
            this.WaterMeter = WaterMeter;
        }

        public int getLiquidLevel() {
            return LiquidLevel;
        }

        public void setLiquidLevel(int LiquidLevel) {
            this.LiquidLevel = LiquidLevel;
        }

        public long getIrrValvesState() {
            return IrrValvesState;
        }

        public void setIrrValvesState(long IrrValvesState) {
            this.IrrValvesState = IrrValvesState;
        }

        public int getFerChState() {
            return FerChState;
        }

        public void setFerChState(int FerChState) {
            this.FerChState = FerChState;
        }

        public int getPumpState() {
            return PumpState;
        }

        public void setPumpState(int PumpState) {
            this.PumpState = PumpState;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getZoneNum() {
            return ZoneNum;
        }

        public void setZoneNum(int ZoneNum) {
            this.ZoneNum = ZoneNum;
        }

        public int getGHType() {
            return GHType;
        }

        public void setGHType(int GHType) {
            this.GHType = GHType;
        }

        public String getGHName() {
            return GHName;
        }

        public void setGHName(String GHName) {
            this.GHName = GHName;
        }
    }
}
