package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 账号管理角色接口数据
 */

public class GetRoleListResult {

    /**
     * success : true
     * Msg : 获取成功
     */

    private boolean success;
    private String Msg;
    private List<List<DataBean>> data;
    private List<List<GhListBean>> GhList;

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

    public List<List<GhListBean>> getGhList() {
        return GhList;
    }

    public void setGhList(List<List<GhListBean>> GhList) {
        this.GhList = GhList;
    }

    public static class DataBean {
        /**
         * BaseID : 155
         * ClassID : 13
         * EnterpriseID : 0
         * BaseName : 企业用户
         * BaseValue : 4
         * MaximizeValue :
         * MinValue :
         * Remark : 企业用户
         * Status : 1
         * OrderNo : 4
         * ParentID : 0
         * ExtraCol1 :
         * ExtraCol2 :
         * NewBaseID : 0
         * ClassName : null
         */

        private int BaseID;
        private int ClassID;
        private int EnterpriseID;
        private String BaseName;
        private String BaseValue;
        private String MaximizeValue;
        private String MinValue;
        private String Remark;
        private int Status;
        private int OrderNo;
        private int ParentID;
        private String ExtraCol1;
        private String ExtraCol2;
        private int NewBaseID;
        private Object ClassName;

        public int getBaseID() {
            return BaseID;
        }

        public void setBaseID(int BaseID) {
            this.BaseID = BaseID;
        }

        public int getClassID() {
            return ClassID;
        }

        public void setClassID(int ClassID) {
            this.ClassID = ClassID;
        }

        public int getEnterpriseID() {
            return EnterpriseID;
        }

        public void setEnterpriseID(int EnterpriseID) {
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

        public int getStatus() {
            return Status;
        }

        public void setStatus(int Status) {
            this.Status = Status;
        }

        public int getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(int OrderNo) {
            this.OrderNo = OrderNo;
        }

        public int getParentID() {
            return ParentID;
        }

        public void setParentID(int ParentID) {
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

        public int getNewBaseID() {
            return NewBaseID;
        }

        public void setNewBaseID(int NewBaseID) {
            this.NewBaseID = NewBaseID;
        }

        public Object getClassName() {
            return ClassName;
        }

        public void setClassName(Object ClassName) {
            this.ClassName = ClassName;
        }
    }

    public static class GhListBean {
        /**
         * GreenhouseID : 82
         * GreenhouseName : 大田1#
         * Area : null
         * EnterpriseID : 9
         * DisplayNo : 026498465
         * DisplayIP : null
         * DisplayPort : null
         * Status : 0
         * Remark :
         * ZoneNumber : null
         * OutputCoilStatus : null
         * InputCoilStatus : null
         * AuthCode : cd0591f5
         * ConnectType : 2
         * OrderNo : 30
         * GhType : 2
         * IrrigationState : null
         * GhGroup : 1
         * GwID : null
         * IsUsed : null
         */

        private int GreenhouseID;
        private String GreenhouseName;
        private Object Area;
        private int EnterpriseID;
        private String DisplayNo;
        private Object DisplayIP;
        private Object DisplayPort;
        private int Status;
        private String Remark;
        private Object ZoneNumber;
        private Object OutputCoilStatus;
        private Object InputCoilStatus;
        private String AuthCode;
        private int ConnectType;
        private int OrderNo;
        private int GhType;
        private Object IrrigationState;
        private int GhGroup;
        private int GwID;
        private Object IsUsed;

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

        public Object getArea() {
            return Area;
        }

        public void setArea(Object Area) {
            this.Area = Area;
        }

        public int getEnterpriseID() {
            return EnterpriseID;
        }

        public void setEnterpriseID(int EnterpriseID) {
            this.EnterpriseID = EnterpriseID;
        }

        public String getDisplayNo() {
            return DisplayNo;
        }

        public void setDisplayNo(String DisplayNo) {
            this.DisplayNo = DisplayNo;
        }

        public Object getDisplayIP() {
            return DisplayIP;
        }

        public void setDisplayIP(Object DisplayIP) {
            this.DisplayIP = DisplayIP;
        }

        public Object getDisplayPort() {
            return DisplayPort;
        }

        public void setDisplayPort(Object DisplayPort) {
            this.DisplayPort = DisplayPort;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int Status) {
            this.Status = Status;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }

        public Object getZoneNumber() {
            return ZoneNumber;
        }

        public void setZoneNumber(Object ZoneNumber) {
            this.ZoneNumber = ZoneNumber;
        }

        public Object getOutputCoilStatus() {
            return OutputCoilStatus;
        }

        public void setOutputCoilStatus(Object OutputCoilStatus) {
            this.OutputCoilStatus = OutputCoilStatus;
        }

        public Object getInputCoilStatus() {
            return InputCoilStatus;
        }

        public void setInputCoilStatus(Object InputCoilStatus) {
            this.InputCoilStatus = InputCoilStatus;
        }

        public String getAuthCode() {
            return AuthCode;
        }

        public void setAuthCode(String AuthCode) {
            this.AuthCode = AuthCode;
        }

        public int getConnectType() {
            return ConnectType;
        }

        public void setConnectType(int ConnectType) {
            this.ConnectType = ConnectType;
        }

        public int getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(int OrderNo) {
            this.OrderNo = OrderNo;
        }

        public int getGhType() {
            return GhType;
        }

        public void setGhType(int GhType) {
            this.GhType = GhType;
        }

        public Object getIrrigationState() {
            return IrrigationState;
        }

        public void setIrrigationState(Object IrrigationState) {
            this.IrrigationState = IrrigationState;
        }

        public int getGhGroup() {
            return GhGroup;
        }

        public void setGhGroup(int GhGroup) {
            this.GhGroup = GhGroup;
        }

        public int getGwID() {
            return GwID;
        }

        public void setGwID(int GwID) {
            this.GwID = GwID;
        }

        public Object getIsUsed() {
            return IsUsed;
        }

        public void setIsUsed(Object IsUsed) {
            this.IsUsed = IsUsed;
        }
    }
}
