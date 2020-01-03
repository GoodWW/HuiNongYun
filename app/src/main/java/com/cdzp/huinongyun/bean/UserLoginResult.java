package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 登录返回数据
 */

public class UserLoginResult {

    /**
     * success : true
     * status : 0
     * data :
     * Msg : 登陆成功!
     */

    private boolean success;
    private int status;
    private String Msg;
    private List<DataBean> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
         * EntName : null
         * RoleName : null
         * SelEntId : -1
         * SystemValue : -1
         * SystemName :
         * City : 成都
         * UserID : 2
         * EnterpriseID : 1
         * UserName : zptest
         * ContactMethod : 18000575852
         * ApplyTime : 2018-04-10 16:38:37
         * NickName : 企业管理员
         * Password : E1-0A-DC-39-49-BA-59-AB-BE-56-E0-57-F2-0F-88-3E
         * PlainCode : 123456
         * Status : 0
         * RoleID : 3
         * AuthCode : ef307a6f
         * IsUsed : 1
         */

        private String EntName;
        private String RoleName;
        private int SelEntId;
        private int SystemValue;
        private String SystemName;
        private String City;
        private int UserID;
        private int EnterpriseID;
        private String UserName;
        private String ContactMethod;
        private String ApplyTime;
        private String NickName;
        private String Password;
        private String PlainCode;
        private int Status;
        private int RoleID;
        private String AuthCode;
        private int IsUsed;

        public String getEntName() {
            return EntName;
        }

        public void setEntName(String EntName) {
            this.EntName = EntName;
        }

        public String getRoleName() {
            return RoleName;
        }

        public void setRoleName(String RoleName) {
            this.RoleName = RoleName;
        }

        public int getSelEntId() {
            return SelEntId;
        }

        public void setSelEntId(int SelEntId) {
            this.SelEntId = SelEntId;
        }

        public int getSystemValue() {
            return SystemValue;
        }

        public void setSystemValue(int SystemValue) {
            this.SystemValue = SystemValue;
        }

        public String getSystemName() {
            return SystemName;
        }

        public void setSystemName(String SystemName) {
            this.SystemName = SystemName;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String City) {
            this.City = City;
        }

        public int getUserID() {
            return UserID;
        }

        public void setUserID(int UserID) {
            this.UserID = UserID;
        }

        public int getEnterpriseID() {
            return EnterpriseID;
        }

        public void setEnterpriseID(int EnterpriseID) {
            this.EnterpriseID = EnterpriseID;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getContactMethod() {
            return ContactMethod;
        }

        public void setContactMethod(String ContactMethod) {
            this.ContactMethod = ContactMethod;
        }

        public String getApplyTime() {
            return ApplyTime;
        }

        public void setApplyTime(String ApplyTime) {
            this.ApplyTime = ApplyTime;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String NickName) {
            this.NickName = NickName;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String Password) {
            this.Password = Password;
        }

        public String getPlainCode() {
            return PlainCode;
        }

        public void setPlainCode(String PlainCode) {
            this.PlainCode = PlainCode;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int Status) {
            this.Status = Status;
        }

        public int getRoleID() {
            return RoleID;
        }

        public void setRoleID(int RoleID) {
            this.RoleID = RoleID;
        }

        public String getAuthCode() {
            return AuthCode;
        }

        public void setAuthCode(String AuthCode) {
            this.AuthCode = AuthCode;
        }

        public int getIsUsed() {
            return IsUsed;
        }

        public void setIsUsed(int IsUsed) {
            this.IsUsed = IsUsed;
        }
    }
}
