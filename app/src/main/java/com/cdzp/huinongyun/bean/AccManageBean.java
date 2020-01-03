package com.cdzp.huinongyun.bean;

/**
 * 账号管理列表数据类
 */

public class AccManageBean {

    private String userID;
    private String userName;
    private String nickName;
    private String roleName;
    private boolean[] isCheck;
    private String[] greenhouseID;
    private String[] greenhouseName;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean[] getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean[] isCheck) {
        this.isCheck = isCheck;
    }

    public String[] getGreenhouseID() {
        return greenhouseID;
    }

    public void setGreenhouseID(String[] greenhouseID) {
        this.greenhouseID = greenhouseID;
    }

    public String[] getGreenhouseName() {
        return greenhouseName;
    }

    public void setGreenhouseName(String[] greenhouseName) {
        this.greenhouseName = greenhouseName;
    }
}
