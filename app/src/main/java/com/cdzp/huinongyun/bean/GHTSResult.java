package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 获取主页终端模块
 */

public class GHTSResult {

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
         * GhType : 1 类型
         * alarm_cout : 8 报警数量
         * gh_cout : 3 终端总数
         * online : 0 在线数量
         * notline : 3 不在线数量
         * TypeName : 温室环控
         */

        private String GhType;
        private String alarm_cout;
        private String gh_cout;
        private String online;
        private String notline;
        private String TypeName;

        public String getGhType() {
            return GhType;
        }

        public void setGhType(String GhType) {
            this.GhType = GhType;
        }

        public String getAlarm_cout() {
            return alarm_cout;
        }

        public void setAlarm_cout(String alarm_cout) {
            this.alarm_cout = alarm_cout;
        }

        public String getGh_cout() {
            return gh_cout;
        }

        public void setGh_cout(String gh_cout) {
            this.gh_cout = gh_cout;
        }

        public String getOnline() {
            return online;
        }

        public void setOnline(String online) {
            this.online = online;
        }

        public String getNotline() {
            return notline;
        }

        public void setNotline(String notline) {
            this.notline = notline;
        }

        public String getTypeName() {
            return TypeName;
        }

        public void setTypeName(String TypeName) {
            this.TypeName = TypeName;
        }
    }
}
