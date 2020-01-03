package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 在线施肥机配置信息
 */
public class FertConfiguration {

    /**
     * success : true
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

        private RunParameterBean RunParameter;
        private BaseInfoBean BaseInfo;

        /**
         * 水罐信息：清水罐+酸母液罐+A肥罐+B肥罐+C肥罐
         * +D肥罐+回水消毒罐+一号储液罐+二号储液罐+三号储液罐
         * +四号储液罐+一号回液池+二号回液池+三号回液池+四号回液池+五号回液池+六号回液池
         */
        private List<WListBean> WList;

        public RunParameterBean getRunParameter() {
            return RunParameter;
        }

        public void setRunParameter(RunParameterBean RunParameter) {
            this.RunParameter = RunParameter;
        }

        public BaseInfoBean getBaseInfo() {
            return BaseInfo;
        }

        public void setBaseInfo(BaseInfoBean BaseInfo) {
            this.BaseInfo = BaseInfo;
        }

        public List<WListBean> getWList() {
            return WList;
        }

        public void setWList(List<WListBean> WList) {
            this.WList = WList;
        }

        public static class RunParameterBean {
            /**
             * FertSCI : 0.2
             * FertPHCI : 10
             * FertACI : 10
             * FertBCI : 10
             * FertCCI : 10
             * FertDCI : 10
             * PH_S : 7
             * PH_PH : 3
             * PH_A : 7
             * PH_B : 7
             * PH_C : 7
             * PH_D : 7
             * ClearTime : 0
             * ErrEC : 1
             * ErrPH : 10
             * HighECAlarm : 2
             * LowPHAlarm : 3
             * FertCtlMode : 1
             * Proportion_A : 1
             * Proportion_B : 1
             * Proportion_C : 1
             * Proportion_D : 1
             * PertreatECvalue : 0
             */

            private double FertSCI;
            private double FertPHCI;
            private double FertACI;
            private double FertBCI;
            private double FertCCI;
            private double FertDCI;
            private double PH_S;
            private double PH_PH;
            private double PH_A;
            private double PH_B;
            private double PH_C;
            private double PH_D;
            private int ClearTime;
            private double ErrEC;
            private double ErrPH;
            private double HighECAlarm;
            private double LowPHAlarm;
            private int FertCtlMode;
            private double Proportion_A;
            private double Proportion_B;
            private double Proportion_C;
            private double Proportion_D;
            private double PertreatECvalue;

            public double getFertSCI() {
                return FertSCI;
            }

            public void setFertSCI(double FertSCI) {
                this.FertSCI = FertSCI;
            }

            public double getFertPHCI() {
                return FertPHCI;
            }

            public void setFertPHCI(double FertPHCI) {
                this.FertPHCI = FertPHCI;
            }

            public double getFertACI() {
                return FertACI;
            }

            public void setFertACI(double FertACI) {
                this.FertACI = FertACI;
            }

            public double getFertBCI() {
                return FertBCI;
            }

            public void setFertBCI(double FertBCI) {
                this.FertBCI = FertBCI;
            }

            public double getFertCCI() {
                return FertCCI;
            }

            public void setFertCCI(double FertCCI) {
                this.FertCCI = FertCCI;
            }

            public double getFertDCI() {
                return FertDCI;
            }

            public void setFertDCI(double FertDCI) {
                this.FertDCI = FertDCI;
            }

            public double getPH_S() {
                return PH_S;
            }

            public void setPH_S(double PH_S) {
                this.PH_S = PH_S;
            }

            public double getPH_PH() {
                return PH_PH;
            }

            public void setPH_PH(double PH_PH) {
                this.PH_PH = PH_PH;
            }

            public double getPH_A() {
                return PH_A;
            }

            public void setPH_A(double PH_A) {
                this.PH_A = PH_A;
            }

            public double getPH_B() {
                return PH_B;
            }

            public void setPH_B(double PH_B) {
                this.PH_B = PH_B;
            }

            public double getPH_C() {
                return PH_C;
            }

            public void setPH_C(double PH_C) {
                this.PH_C = PH_C;
            }

            public double getPH_D() {
                return PH_D;
            }

            public void setPH_D(double PH_D) {
                this.PH_D = PH_D;
            }

            public int getClearTime() {
                return ClearTime;
            }

            public void setClearTime(int ClearTime) {
                this.ClearTime = ClearTime;
            }

            public double getErrEC() {
                return ErrEC;
            }

            public void setErrEC(double ErrEC) {
                this.ErrEC = ErrEC;
            }

            public double getErrPH() {
                return ErrPH;
            }

            public void setErrPH(double ErrPH) {
                this.ErrPH = ErrPH;
            }

            public double getHighECAlarm() {
                return HighECAlarm;
            }

            public void setHighECAlarm(double HighECAlarm) {
                this.HighECAlarm = HighECAlarm;
            }

            public double getLowPHAlarm() {
                return LowPHAlarm;
            }

            public void setLowPHAlarm(double LowPHAlarm) {
                this.LowPHAlarm = LowPHAlarm;
            }

            public int getFertCtlMode() {
                return FertCtlMode;
            }

            public void setFertCtlMode(int FertCtlMode) {
                this.FertCtlMode = FertCtlMode;
            }

            public double getProportion_A() {
                return Proportion_A;
            }

            public void setProportion_A(double Proportion_A) {
                this.Proportion_A = Proportion_A;
            }

            public double getProportion_B() {
                return Proportion_B;
            }

            public void setProportion_B(double Proportion_B) {
                this.Proportion_B = Proportion_B;
            }

            public double getProportion_C() {
                return Proportion_C;
            }

            public void setProportion_C(double Proportion_C) {
                this.Proportion_C = Proportion_C;
            }

            public double getProportion_D() {
                return Proportion_D;
            }

            public void setProportion_D(double Proportion_D) {
                this.Proportion_D = Proportion_D;
            }

            public double getPertreatECvalue() {
                return PertreatECvalue;
            }

            public void setPertreatECvalue(double PertreatECvalue) {
                this.PertreatECvalue = PertreatECvalue;
            }
        }

        public static class BaseInfoBean {
            /**
             * TankNum : 4
             * TankOneValveL : 0101
             * TankOneValveH : 0
             * TankTwoValveL : 48
             * TankTwoValveH : 0
             * TankThreeValveL : 64
             * TankThreeValveH : 0
             * TankFourValveL : 32896
             * TankFourValveH : 0
             * ValveNum : 64
             * PulsePerLitre : 0.1
             * FertMaxFlow : 10
             * HasInPump : 1     ...
             * InPumpCh : 0
             * HasPertreat : 1
             * ValvePlan : 0
             * RunMode : 0
             * ValveType : 0
             */

            private int TankNum;
            private long TankOneValveL;
            private long TankOneValveH;
            private long TankTwoValveL;
            private long TankTwoValveH;
            private long TankThreeValveL;
            private long TankThreeValveH;
            private long TankFourValveL;
            private long TankFourValveH;
            private int ValveNum;
            private double PulsePerLitre;
            private double FertMaxFlow;
            private int HasInPump;
            private int InPumpCh;
            private int HasPertreat;
            private int ValvePlan;
            private int RunMode;
            private int ValveType;
            private int LevelRange; //液位计量程
            private int InPumpDelay; //进水泵启动延时

            public int getLevelRange() {
                return LevelRange;
            }

            public void setLevelRange(int LevelRange) {
                this.LevelRange = LevelRange;
            }

            public int getInPumpDelay() {
                return InPumpDelay;
            }

            public void setInPumpDelay(int InPumpDelay) {
                this.InPumpDelay = InPumpDelay;
            }

            public int getTankNum() {
                return TankNum;
            }

            public void setTankNum(int TankNum) {
                this.TankNum = TankNum;
            }

            public long getTankOneValveL() {
                return TankOneValveL;
            }

            public void setTankOneValveL(long TankOneValveL) {
                this.TankOneValveL = TankOneValveL;
            }

            public long getTankOneValveH() {
                return TankOneValveH;
            }

            public void setTankOneValveH(long TankOneValveH) {
                this.TankOneValveH = TankOneValveH;
            }

            public long getTankTwoValveL() {
                return TankTwoValveL;
            }

            public void setTankTwoValveL(long TankTwoValveL) {
                this.TankTwoValveL = TankTwoValveL;
            }

            public long getTankTwoValveH() {
                return TankTwoValveH;
            }

            public void setTankTwoValveH(long TankTwoValveH) {
                this.TankTwoValveH = TankTwoValveH;
            }

            public long getTankThreeValveL() {
                return TankThreeValveL;
            }

            public void setTankThreeValveL(long TankThreeValveL) {
                this.TankThreeValveL = TankThreeValveL;
            }

            public long getTankThreeValveH() {
                return TankThreeValveH;
            }

            public void setTankThreeValveH(long TankThreeValveH) {
                this.TankThreeValveH = TankThreeValveH;
            }

            public long getTankFourValveL() {
                return TankFourValveL;
            }

            public void setTankFourValveL(long TankFourValveL) {
                this.TankFourValveL = TankFourValveL;
            }

            public long getTankFourValveH() {
                return TankFourValveH;
            }

            public void setTankFourValveH(long TankFourValveH) {
                this.TankFourValveH = TankFourValveH;
            }

            public int getValveNum() {
                return ValveNum;
            }

            public void setValveNum(int ValveNum) {
                this.ValveNum = ValveNum;
            }

            public double getPulsePerLitre() {
                return PulsePerLitre;
            }

            public void setPulsePerLitre(double PulsePerLitre) {
                this.PulsePerLitre = PulsePerLitre;
            }

            public double getFertMaxFlow() {
                return FertMaxFlow;
            }

            public void setFertMaxFlow(double FertMaxFlow) {
                this.FertMaxFlow = FertMaxFlow;
            }

            public int getHasInPump() {
                return HasInPump;
            }

            public void setHasInPump(int HasInPump) {
                this.HasInPump = HasInPump;
            }

            public int getInPumpCh() {
                return InPumpCh;
            }

            public void setInPumpCh(int InPumpCh) {
                this.InPumpCh = InPumpCh;
            }

            public int getHasPertreat() {
                return HasPertreat;
            }

            public void setHasPertreat(int HasPertreat) {
                this.HasPertreat = HasPertreat;
            }

            public int getValvePlan() {
                return ValvePlan;
            }

            public void setValvePlan(int ValvePlan) {
                this.ValvePlan = ValvePlan;
            }

            public int getRunMode() {
                return RunMode;
            }

            public void setRunMode(int RunMode) {
                this.RunMode = RunMode;
            }

            public int getValveType() {
                return ValveType;
            }

            public void setValveType(int ValveType) {
                this.ValveType = ValveType;
            }
        }

        public static class WListBean {
            /**
             * HasTank : 1 (1:有回水罐)
             * HasLevel : 1 (1:显示) 1
             * LevelType : 0 (0：显示文字，HighLevelCh(第几位，然后去判断SwitchLevel的第几位，
             *              SwitchLevel值位1就是“液体已满”，LowLevelCh为1“液体不足”，如果都是0就显示“液体充足”)
             *              1:具体显示值-CurrentLevelVec)
             * CurrentCh : 2
             * HighLevelCh : 2
             * LowLevelCh : 3
             * HasValve : 1 (1:储液罐第一个有指示灯)
             * HasPump : 1  (1:有指示灯)
             * ValveCh : 0
             * PumpCh : 0 (第多少位-PumpStatus)
             */

            private int HasTank;
            private int HasLevel;
            private int LevelType;
            private int CurrentCh;
            private int HighLevelCh;
            private int LowLevelCh;
            private int HasValve;
            private int HasPump;
            private int ValveCh;
            private int PumpCh;

            public int getHasTank() {
                return HasTank;
            }

            public void setHasTank(int HasTank) {
                this.HasTank = HasTank;
            }

            public int getHasLevel() {
                return HasLevel;
            }

            public void setHasLevel(int HasLevel) {
                this.HasLevel = HasLevel;
            }

            public int getLevelType() {
                return LevelType;
            }

            public void setLevelType(int LevelType) {
                this.LevelType = LevelType;
            }

            public int getCurrentCh() {
                return CurrentCh;
            }

            public void setCurrentCh(int CurrentCh) {
                this.CurrentCh = CurrentCh;
            }

            public int getHighLevelCh() {
                return HighLevelCh;
            }

            public void setHighLevelCh(int HighLevelCh) {
                this.HighLevelCh = HighLevelCh;
            }

            public int getLowLevelCh() {
                return LowLevelCh;
            }

            public void setLowLevelCh(int LowLevelCh) {
                this.LowLevelCh = LowLevelCh;
            }

            public int getHasValve() {
                return HasValve;
            }

            public void setHasValve(int HasValve) {
                this.HasValve = HasValve;
            }

            public int getHasPump() {
                return HasPump;
            }

            public void setHasPump(int HasPump) {
                this.HasPump = HasPump;
            }

            public int getValveCh() {
                return ValveCh;
            }

            public void setValveCh(int ValveCh) {
                this.ValveCh = ValveCh;
            }

            public int getPumpCh() {
                return PumpCh;
            }

            public void setPumpCh(int PumpCh) {
                this.PumpCh = PumpCh;
            }
        }
    }
}
