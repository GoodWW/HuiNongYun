package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 获取终端接口数据
 */

public class GreenHouseSelResult {

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
        private List<PGhListBean> pGhList;
        private List<CGhListBean> cGhList;
        private List<OutGhListBean> outGhList;
        private List<LocListBean> locList;

        public List<PGhListBean> getPGhList() {
            return pGhList;
        }

        public void setPGhList(List<PGhListBean> pGhList) {
            this.pGhList = pGhList;
        }

        public List<CGhListBean> getCGhList() {
            return cGhList;
        }

        public void setCGhList(List<CGhListBean> cGhList) {
            this.cGhList = cGhList;
        }

        public List<OutGhListBean> getOutGhList() {
            return outGhList;
        }

        public void setOutGhList(List<OutGhListBean> outGhList) {
            this.outGhList = outGhList;
        }

        public List<LocListBean> getLocList() {
            return locList;
        }

        public void setLocList(List<LocListBean> locList) {
            this.locList = locList;
        }

        public static class PGhListBean {
            /**
             * GreenhouseID : 19
             * GreenhouseName : 日光温室
             * Area :
             * EnterpriseID : 1
             * DisplayNo : 08171013009
             * DisplayIP : 118.113.10.75
             * DisplayPort : 1044
             * Status : 1
             * Remark :
             * ZoneNumber :
             * OutputCoilStatus :
             * InputCoilStatus :
             * AuthCode : 982bfb80
             * ConnectType : 2
             * OrderNo : 4
             * GhType : 8
             * IrrigationState :
             * GhGroup : 1
             * GwID : -1
             * maxLocation :
             */

            private String GreenhouseID;
            private String GreenhouseName;
            private String Area;
            private String EnterpriseID;
            private String DisplayNo;
            private String DisplayIP;
            private String DisplayPort;
            private String Status;
            private String Remark;
            private String ZoneNumber;
            private String OutputCoilStatus;
            private String InputCoilStatus;
            private String AuthCode;
            private String ConnectType;
            private String OrderNo;
            private String GhType;
            private String IrrigationState;
            private String GhGroup;
            private String GwID;
            private String maxLocation;

            public String getGreenhouseID() {
                return GreenhouseID;
            }

            public void setGreenhouseID(String GreenhouseID) {
                this.GreenhouseID = GreenhouseID;
            }

            public String getGreenhouseName() {
                return GreenhouseName;
            }

            public void setGreenhouseName(String GreenhouseName) {
                this.GreenhouseName = GreenhouseName;
            }

            public String getArea() {
                return Area;
            }

            public void setArea(String Area) {
                this.Area = Area;
            }

            public String getEnterpriseID() {
                return EnterpriseID;
            }

            public void setEnterpriseID(String EnterpriseID) {
                this.EnterpriseID = EnterpriseID;
            }

            public String getDisplayNo() {
                return DisplayNo;
            }

            public void setDisplayNo(String DisplayNo) {
                this.DisplayNo = DisplayNo;
            }

            public String getDisplayIP() {
                return DisplayIP;
            }

            public void setDisplayIP(String DisplayIP) {
                this.DisplayIP = DisplayIP;
            }

            public String getDisplayPort() {
                return DisplayPort;
            }

            public void setDisplayPort(String DisplayPort) {
                this.DisplayPort = DisplayPort;
            }

            public String getStatus() {
                return Status;
            }

            public void setStatus(String Status) {
                this.Status = Status;
            }

            public String getRemark() {
                return Remark;
            }

            public void setRemark(String Remark) {
                this.Remark = Remark;
            }

            public String getZoneNumber() {
                return ZoneNumber;
            }

            public void setZoneNumber(String ZoneNumber) {
                this.ZoneNumber = ZoneNumber;
            }

            public String getOutputCoilStatus() {
                return OutputCoilStatus;
            }

            public void setOutputCoilStatus(String OutputCoilStatus) {
                this.OutputCoilStatus = OutputCoilStatus;
            }

            public String getInputCoilStatus() {
                return InputCoilStatus;
            }

            public void setInputCoilStatus(String InputCoilStatus) {
                this.InputCoilStatus = InputCoilStatus;
            }

            public String getAuthCode() {
                return AuthCode;
            }

            public void setAuthCode(String AuthCode) {
                this.AuthCode = AuthCode;
            }

            public String getConnectType() {
                return ConnectType;
            }

            public void setConnectType(String ConnectType) {
                this.ConnectType = ConnectType;
            }

            public String getOrderNo() {
                return OrderNo;
            }

            public void setOrderNo(String OrderNo) {
                this.OrderNo = OrderNo;
            }

            public String getGhType() {
                return GhType;
            }

            public void setGhType(String GhType) {
                this.GhType = GhType;
            }

            public String getIrrigationState() {
                return IrrigationState;
            }

            public void setIrrigationState(String IrrigationState) {
                this.IrrigationState = IrrigationState;
            }

            public String getGhGroup() {
                return GhGroup;
            }

            public void setGhGroup(String GhGroup) {
                this.GhGroup = GhGroup;
            }

            public String getGwID() {
                return GwID;
            }

            public void setGwID(String GwID) {
                this.GwID = GwID;
            }

            public String getMaxLocation() {
                return maxLocation;
            }

            public void setMaxLocation(String maxLocation) {
                this.maxLocation = maxLocation;
            }
        }

        public static class CGhListBean {
            /**
             * GreenhouseID : 20
             * GreenhouseName : 1号
             * Area :
             * EnterpriseID : 1
             * DisplayNo : 170425001
             * DisplayIP :
             * DisplayPort :
             * Status : 0
             * Remark :
             * ZoneNumber : 1
             * OutputCoilStatus : 0
             * InputCoilStatus : 0
             * AuthCode :
             * ConnectType :
             * OrderNo :
             * GhType :
             * IrrigationState :
             * GhGroup : 1
             * GwID : 19
             * maxLocation :
             */

            private String GreenhouseID;
            private String GreenhouseName;
            private String Area;
            private String EnterpriseID;
            private String DisplayNo;
            private String DisplayIP;
            private String DisplayPort;
            private String Status;
            private String Remark;
            private String ZoneNumber;
            private String OutputCoilStatus;
            private String InputCoilStatus;
            private String AuthCode;
            private String ConnectType;
            private String OrderNo;
            private String GhType;
            private String IrrigationState;
            private String GhGroup;
            private String GwID;
            private String maxLocation;

            public String getGreenhouseID() {
                return GreenhouseID;
            }

            public void setGreenhouseID(String GreenhouseID) {
                this.GreenhouseID = GreenhouseID;
            }

            public String getGreenhouseName() {
                return GreenhouseName;
            }

            public void setGreenhouseName(String GreenhouseName) {
                this.GreenhouseName = GreenhouseName;
            }

            public String getArea() {
                return Area;
            }

            public void setArea(String Area) {
                this.Area = Area;
            }

            public String getEnterpriseID() {
                return EnterpriseID;
            }

            public void setEnterpriseID(String EnterpriseID) {
                this.EnterpriseID = EnterpriseID;
            }

            public String getDisplayNo() {
                return DisplayNo;
            }

            public void setDisplayNo(String DisplayNo) {
                this.DisplayNo = DisplayNo;
            }

            public String getDisplayIP() {
                return DisplayIP;
            }

            public void setDisplayIP(String DisplayIP) {
                this.DisplayIP = DisplayIP;
            }

            public String getDisplayPort() {
                return DisplayPort;
            }

            public void setDisplayPort(String DisplayPort) {
                this.DisplayPort = DisplayPort;
            }

            public String getStatus() {
                return Status;
            }

            public void setStatus(String Status) {
                this.Status = Status;
            }

            public String getRemark() {
                return Remark;
            }

            public void setRemark(String Remark) {
                this.Remark = Remark;
            }

            public String getZoneNumber() {
                return ZoneNumber;
            }

            public void setZoneNumber(String ZoneNumber) {
                this.ZoneNumber = ZoneNumber;
            }

            public String getOutputCoilStatus() {
                return OutputCoilStatus;
            }

            public void setOutputCoilStatus(String OutputCoilStatus) {
                this.OutputCoilStatus = OutputCoilStatus;
            }

            public String getInputCoilStatus() {
                return InputCoilStatus;
            }

            public void setInputCoilStatus(String InputCoilStatus) {
                this.InputCoilStatus = InputCoilStatus;
            }

            public String getAuthCode() {
                return AuthCode;
            }

            public void setAuthCode(String AuthCode) {
                this.AuthCode = AuthCode;
            }

            public String getConnectType() {
                return ConnectType;
            }

            public void setConnectType(String ConnectType) {
                this.ConnectType = ConnectType;
            }

            public String getOrderNo() {
                return OrderNo;
            }

            public void setOrderNo(String OrderNo) {
                this.OrderNo = OrderNo;
            }

            public String getGhType() {
                return GhType;
            }

            public void setGhType(String GhType) {
                this.GhType = GhType;
            }

            public String getIrrigationState() {
                return IrrigationState;
            }

            public void setIrrigationState(String IrrigationState) {
                this.IrrigationState = IrrigationState;
            }

            public String getGhGroup() {
                return GhGroup;
            }

            public void setGhGroup(String GhGroup) {
                this.GhGroup = GhGroup;
            }

            public String getGwID() {
                return GwID;
            }

            public void setGwID(String GwID) {
                this.GwID = GwID;
            }

            public String getMaxLocation() {
                return maxLocation;
            }

            public void setMaxLocation(String maxLocation) {
                this.maxLocation = maxLocation;
            }
        }

        public static class OutGhListBean {
            /**
             * GreenhouseID : 0
             * GreenhouseName : 室外
             * Area :
             * EnterpriseID :
             * DisplayNo :
             * DisplayIP :
             * DisplayPort :
             * Status :
             * Remark :
             * ZoneNumber :
             * OutputCoilStatus :
             * InputCoilStatus :
             * AuthCode :
             * ConnectType :
             * OrderNo :
             * GhType :
             * IrrigationState :
             * GhGroup :
             * GwID : -1
             * maxLocation : 0
             */

            private String GreenhouseID;
            private String GreenhouseName;
            private String Area;
            private String EnterpriseID;
            private String DisplayNo;
            private String DisplayIP;
            private String DisplayPort;
            private String Status;
            private String Remark;
            private String ZoneNumber;
            private String OutputCoilStatus;
            private String InputCoilStatus;
            private String AuthCode;
            private String ConnectType;
            private String OrderNo;
            private String GhType;
            private String IrrigationState;
            private String GhGroup;
            private String GwID;
            private String maxLocation;

            public String getGreenhouseID() {
                return GreenhouseID;
            }

            public void setGreenhouseID(String GreenhouseID) {
                this.GreenhouseID = GreenhouseID;
            }

            public String getGreenhouseName() {
                return GreenhouseName;
            }

            public void setGreenhouseName(String GreenhouseName) {
                this.GreenhouseName = GreenhouseName;
            }

            public String getArea() {
                return Area;
            }

            public void setArea(String Area) {
                this.Area = Area;
            }

            public String getEnterpriseID() {
                return EnterpriseID;
            }

            public void setEnterpriseID(String EnterpriseID) {
                this.EnterpriseID = EnterpriseID;
            }

            public String getDisplayNo() {
                return DisplayNo;
            }

            public void setDisplayNo(String DisplayNo) {
                this.DisplayNo = DisplayNo;
            }

            public String getDisplayIP() {
                return DisplayIP;
            }

            public void setDisplayIP(String DisplayIP) {
                this.DisplayIP = DisplayIP;
            }

            public String getDisplayPort() {
                return DisplayPort;
            }

            public void setDisplayPort(String DisplayPort) {
                this.DisplayPort = DisplayPort;
            }

            public String getStatus() {
                return Status;
            }

            public void setStatus(String Status) {
                this.Status = Status;
            }

            public String getRemark() {
                return Remark;
            }

            public void setRemark(String Remark) {
                this.Remark = Remark;
            }

            public String getZoneNumber() {
                return ZoneNumber;
            }

            public void setZoneNumber(String ZoneNumber) {
                this.ZoneNumber = ZoneNumber;
            }

            public String getOutputCoilStatus() {
                return OutputCoilStatus;
            }

            public void setOutputCoilStatus(String OutputCoilStatus) {
                this.OutputCoilStatus = OutputCoilStatus;
            }

            public String getInputCoilStatus() {
                return InputCoilStatus;
            }

            public void setInputCoilStatus(String InputCoilStatus) {
                this.InputCoilStatus = InputCoilStatus;
            }

            public String getAuthCode() {
                return AuthCode;
            }

            public void setAuthCode(String AuthCode) {
                this.AuthCode = AuthCode;
            }

            public String getConnectType() {
                return ConnectType;
            }

            public void setConnectType(String ConnectType) {
                this.ConnectType = ConnectType;
            }

            public String getOrderNo() {
                return OrderNo;
            }

            public void setOrderNo(String OrderNo) {
                this.OrderNo = OrderNo;
            }

            public String getGhType() {
                return GhType;
            }

            public void setGhType(String GhType) {
                this.GhType = GhType;
            }

            public String getIrrigationState() {
                return IrrigationState;
            }

            public void setIrrigationState(String IrrigationState) {
                this.IrrigationState = IrrigationState;
            }

            public String getGhGroup() {
                return GhGroup;
            }

            public void setGhGroup(String GhGroup) {
                this.GhGroup = GhGroup;
            }

            public String getGwID() {
                return GwID;
            }

            public void setGwID(String GwID) {
                this.GwID = GwID;
            }

            public String getMaxLocation() {
                return maxLocation;
            }

            public void setMaxLocation(String maxLocation) {
                this.maxLocation = maxLocation;
            }
        }

        public static class LocListBean {
            /**
             * BaseID : 19
             * ClassID : 11
             * EnterpriseID : 0
             * BaseName : 一区
             * BaseValue : 1
             * MaximizeValue : null
             * MinValue : null
             * Remark : null
             * Status : 1
             * OrderNo : 2
             * ParentID : null
             * ExtraCol1 : null
             * ExtraCol2 : null
             * NewBaseID : 0
             * ClassName : null
             */

            private int BaseID;
            private int ClassID;
            private int EnterpriseID;
            private String BaseName;
            private String BaseValue;
            private Object MaximizeValue;
            private Object MinValue;
            private Object Remark;
            private int Status;
            private int OrderNo;
            private Object ParentID;
            private Object ExtraCol1;
            private Object ExtraCol2;
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

            public Object getMaximizeValue() {
                return MaximizeValue;
            }

            public void setMaximizeValue(Object MaximizeValue) {
                this.MaximizeValue = MaximizeValue;
            }

            public Object getMinValue() {
                return MinValue;
            }

            public void setMinValue(Object MinValue) {
                this.MinValue = MinValue;
            }

            public Object getRemark() {
                return Remark;
            }

            public void setRemark(Object Remark) {
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

            public Object getParentID() {
                return ParentID;
            }

            public void setParentID(Object ParentID) {
                this.ParentID = ParentID;
            }

            public Object getExtraCol1() {
                return ExtraCol1;
            }

            public void setExtraCol1(Object ExtraCol1) {
                this.ExtraCol1 = ExtraCol1;
            }

            public Object getExtraCol2() {
                return ExtraCol2;
            }

            public void setExtraCol2(Object ExtraCol2) {
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
    }
}
