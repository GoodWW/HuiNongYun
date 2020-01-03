package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 设备和通道
 */
public class DeviceListResults {

    /**
     * DevID : 1
     * deviceName : 四川农大
     * deviceSerial : 106573611
     * channels :
     */

    private int DevID;
    private String deviceName;
    private String deviceSerial;
    private List<ChannelsBean> channels;

    public int getDevID() {
        return DevID;
    }

    public void setDevID(int DevID) {
        this.DevID = DevID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public List<ChannelsBean> getChannels() {
        return channels;
    }

    public void setChannels(List<ChannelsBean> channels) {
        this.channels = channels;
    }

    public static class ChannelsBean {
        /**
         * CIID : 1
         * MaID : 1
         * DevID : 1
         * CameName : 前球机
         * IpcSerial : 106573611
         * ValidateCode :
         * ChannelNo : 1
         * AddTime :
         * LiveStatus : 0
         * LiveException : 0
         * Remark :
         * status : 1
         * isShared : 0
         * picUrl : https://i.ys7.com/assets/imgs/public/homeDevice.jpeg
         * isEncrypt : 0
         * videoLevel : 0
         * relatedIpc : -1
         * liveAddress :
         * hdAddress :
         * rtmp :
         * rtmpHd :
         * DevName : 四川农大
         * DeviceSerial : 106573611
         * HasPower : 1
         */

        private String CIID;
        private String MaID;
        private String DevID;
        private String CameName;
        private String IpcSerial;
        private String ValidateCode;
        private String ChannelNo;
        private String AddTime;
        private String LiveStatus;
        private String LiveException;
        private String Remark;
        private String status;
        private String isShared;
        private String picUrl;
        private String isEncrypt;
        private String videoLevel;
        private String relatedIpc;
        private String liveAddress;
        private String hdAddress;
        private String rtmp;
        private String rtmpHd;
        private String DevName;
        private String DeviceSerial;
        private String HasPower;

        public String getCIID() {
            return CIID;
        }

        public void setCIID(String CIID) {
            this.CIID = CIID;
        }

        public String getMaID() {
            return MaID;
        }

        public void setMaID(String MaID) {
            this.MaID = MaID;
        }

        public String getDevID() {
            return DevID;
        }

        public void setDevID(String DevID) {
            this.DevID = DevID;
        }

        public String getCameName() {
            return CameName;
        }

        public void setCameName(String CameName) {
            this.CameName = CameName;
        }

        public String getIpcSerial() {
            return IpcSerial;
        }

        public void setIpcSerial(String IpcSerial) {
            this.IpcSerial = IpcSerial;
        }

        public String getValidateCode() {
            return ValidateCode;
        }

        public void setValidateCode(String ValidateCode) {
            this.ValidateCode = ValidateCode;
        }

        public String getChannelNo() {
            return ChannelNo;
        }

        public void setChannelNo(String ChannelNo) {
            this.ChannelNo = ChannelNo;
        }

        public String getAddTime() {
            return AddTime;
        }

        public void setAddTime(String AddTime) {
            this.AddTime = AddTime;
        }

        public String getLiveStatus() {
            return LiveStatus;
        }

        public void setLiveStatus(String LiveStatus) {
            this.LiveStatus = LiveStatus;
        }

        public String getLiveException() {
            return LiveException;
        }

        public void setLiveException(String LiveException) {
            this.LiveException = LiveException;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getIsShared() {
            return isShared;
        }

        public void setIsShared(String isShared) {
            this.isShared = isShared;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getIsEncrypt() {
            return isEncrypt;
        }

        public void setIsEncrypt(String isEncrypt) {
            this.isEncrypt = isEncrypt;
        }

        public String getVideoLevel() {
            return videoLevel;
        }

        public void setVideoLevel(String videoLevel) {
            this.videoLevel = videoLevel;
        }

        public String getRelatedIpc() {
            return relatedIpc;
        }

        public void setRelatedIpc(String relatedIpc) {
            this.relatedIpc = relatedIpc;
        }

        public String getLiveAddress() {
            return liveAddress;
        }

        public void setLiveAddress(String liveAddress) {
            this.liveAddress = liveAddress;
        }

        public String getHdAddress() {
            return hdAddress;
        }

        public void setHdAddress(String hdAddress) {
            this.hdAddress = hdAddress;
        }

        public String getRtmp() {
            return rtmp;
        }

        public void setRtmp(String rtmp) {
            this.rtmp = rtmp;
        }

        public String getRtmpHd() {
            return rtmpHd;
        }

        public void setRtmpHd(String rtmpHd) {
            this.rtmpHd = rtmpHd;
        }

        public String getDevName() {
            return DevName;
        }

        public void setDevName(String DevName) {
            this.DevName = DevName;
        }

        public String getDeviceSerial() {
            return DeviceSerial;
        }

        public void setDeviceSerial(String DeviceSerial) {
            this.DeviceSerial = DeviceSerial;
        }

        public String getHasPower() {
            return HasPower;
        }

        public void setHasPower(String HasPower) {
            this.HasPower = HasPower;
        }
    }
}
