package com.cdzp.huinongyun.bean;

import java.util.List;

public class VideoSel {
    private String success;
    private int CountNum;
    private List<datas> data;

    public static class datas {
        private String ModeName;
        private String Tagmark;
        private String User;
        private String Password;
        private String ServerIP2;
        private String Tagmark2;
        private int DevicePort2;

        public String getTagmark2() {
            return Tagmark2;
        }

        public void setTagmark2(String tagmark2) {
            Tagmark2 = tagmark2;
        }

        public String getModeName() {
            return ModeName;
        }

        public void setModeName(String modeName) {
            ModeName = modeName;
        }

        public String getTagmark() {
            return Tagmark;
        }

        public void setTagmark(String tagmark) {
            Tagmark = tagmark;
        }

        public String getUser() {
            return User;
        }

        public void setUser(String user) {
            User = user;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String password) {
            Password = password;
        }

        public String getServerIP2() {
            return ServerIP2;
        }

        public void setServerIP2(String serverIP2) {
            ServerIP2 = serverIP2;
        }

        public int getDevicePort2() {
            return DevicePort2;
        }

        public void setDevicePort2(int devicePort2) {
            DevicePort2 = devicePort2;
        }

    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public int getCountNum() {
        return CountNum;
    }

    public void setCountNum(int countNum) {
        CountNum = countNum;
    }

    public List<datas> getData() {
        return data;
    }

    public void setData(List<datas> data) {
        this.data = data;
    }

}
