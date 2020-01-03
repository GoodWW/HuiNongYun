package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 视频播放类型接口数据
 */
public class VideoTypeResponse {

    /**
     * success : true
     * data :
     * Msg : 成功!
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
        /**
         * palyType : 3
         * hikModel :
         * Ys7MaID : 1
         * Ys7SmID : 1
         */

        private int palyType;
        private HikModelBean hikModel;
        private int Ys7MaID;
        private int Ys7SmID;

        public int getPalyType() {
            return palyType;
        }

        public void setPalyType(int palyType) {
            this.palyType = palyType;
        }

        public HikModelBean getHikModel() {
            return hikModel;
        }

        public void setHikModel(HikModelBean hikModel) {
            this.hikModel = hikModel;
        }

        public int getYs7MaID() {
            return Ys7MaID;
        }

        public void setYs7MaID(int Ys7MaID) {
            this.Ys7MaID = Ys7MaID;
        }

        public int getYs7SmID() {
            return Ys7SmID;
        }

        public void setYs7SmID(int Ys7SmID) {
            this.Ys7SmID = Ys7SmID;
        }

        public static class HikModelBean {
            /**
             * VideoDeviceID : 1
             * EnterpriseID : 1
             * DeviceName : 天喜
             * DefaultMode : 2
             * ServerIP : 183.136.184.61
             * DevicePort : 80
             * Tagmark : xctxnvr
             * ServerIP2 : null
             * DevicePort2 : 85
             * Tagmark2 : null
             * User : admin
             * Password : txly888888
             * Status : 1
             * Remark : null
             */

            private int VideoDeviceID;
            private int EnterpriseID;
            private String DeviceName;
            private int DefaultMode;//1:IPServer,2:HiDDNS
            private String ServerIP;
            private int DevicePort;
            private String Tagmark;
            private String ServerIP2;
            private int DevicePort2;
            private String Tagmark2;
            private String User;
            private String Password;
            private int Status;
            private String Remark;

            public int getVideoDeviceID() {
                return VideoDeviceID;
            }

            public void setVideoDeviceID(int VideoDeviceID) {
                this.VideoDeviceID = VideoDeviceID;
            }

            public int getEnterpriseID() {
                return EnterpriseID;
            }

            public void setEnterpriseID(int EnterpriseID) {
                this.EnterpriseID = EnterpriseID;
            }

            public String getDeviceName() {
                return DeviceName;
            }

            public void setDeviceName(String DeviceName) {
                this.DeviceName = DeviceName;
            }

            public int getDefaultMode() {
                return DefaultMode;
            }

            public void setDefaultMode(int DefaultMode) {
                this.DefaultMode = DefaultMode;
            }

            public String getServerIP() {
                return ServerIP;
            }

            public void setServerIP(String ServerIP) {
                this.ServerIP = ServerIP;
            }

            public int getDevicePort() {
                return DevicePort;
            }

            public void setDevicePort(int DevicePort) {
                this.DevicePort = DevicePort;
            }

            public String getTagmark() {
                return Tagmark;
            }

            public void setTagmark(String Tagmark) {
                this.Tagmark = Tagmark;
            }

            public String getServerIP2() {
                return ServerIP2;
            }

            public void setServerIP2(String ServerIP2) {
                this.ServerIP2 = ServerIP2;
            }

            public int getDevicePort2() {
                return DevicePort2;
            }

            public void setDevicePort2(int DevicePort2) {
                this.DevicePort2 = DevicePort2;
            }

            public String getTagmark2() {
                return Tagmark2;
            }

            public void setTagmark2(String Tagmark2) {
                this.Tagmark2 = Tagmark2;
            }

            public String getUser() {
                return User;
            }

            public void setUser(String User) {
                this.User = User;
            }

            public String getPassword() {
                return Password;
            }

            public void setPassword(String Password) {
                this.Password = Password;
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
        }
    }
}
