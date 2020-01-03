package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 账号管理接口账户数据
 */

public class GetUserInfoResult {

    /**
     * success : true
     * CountNum : 4
     * data :
     * Msg : 获取成功
     */

    private boolean success;
    private int CountNum;
    private String Msg;
    private List<List<DataBean>> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCountNum() {
        return CountNum;
    }

    public void setCountNum(int CountNum) {
        this.CountNum = CountNum;
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
         * rn : 1
         * UserID : 14
         * UserName : csyk
         * NickName : csyk
         * RoleName : 游客
         * RoleEnglishName : Tourists
         * GhIDs :
         */

        private String rn;
        private String UserID;
        private String UserName;
        private String NickName;
        private String RoleName;
        private String RoleEnglishName;
        private String GhIDs;

        public String getRn() {
            return rn;
        }

        public void setRn(String rn) {
            this.rn = rn;
        }

        public String getUserID() {
            return UserID;
        }

        public void setUserID(String UserID) {
            this.UserID = UserID;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String NickName) {
            this.NickName = NickName;
        }

        public String getRoleName() {
            return RoleName;
        }

        public void setRoleName(String RoleName) {
            this.RoleName = RoleName;
        }

        public String getRoleEnglishName() {
            return RoleEnglishName;
        }

        public void setRoleEnglishName(String RoleEnglishName) {
            this.RoleEnglishName = RoleEnglishName;
        }

        public String getGhIDs() {
            return GhIDs;
        }

        public void setGhIDs(String GhIDs) {
            this.GhIDs = GhIDs;
        }
    }
}
