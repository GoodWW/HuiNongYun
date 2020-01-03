package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 根据地块Id获取地块下的设备列表数据(原力原特殊定制)
 */
public class YlyByFkFieldUuids {

    /**
     * total : true
     * rows :
     * successCode : true
     */

    private boolean total;
    private boolean successCode;
    private List<RowsBean> rows;

    public boolean isTotal() {
        return total;
    }

    public void setTotal(boolean total) {
        this.total = total;
    }

    public boolean isSuccessCode() {
        return successCode;
    }

    public void setSuccessCode(boolean successCode) {
        this.successCode = successCode;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * cameraUuid : 4A770538000203
         * type : 2
         * equUuid : 3
         * cameraName : 传感器3
         * updateTime : 2018-06-29 14:39:56
         * installTime : 2018-06-21
         * warrantyTime : 2018-06-21
         * manufacturer : 未知
         * onlineStatus : 1
         * cameraType : null
         * resAuths : null
         * fkFieldUuid : 5db24198-7458-11e8-b00a-0242ac110003
         * fkEncoderUuid : null
         * x : 0.434
         * y : 0.818
         */

        private String cameraUuid;
        private int type;
        private String equUuid;
        private String cameraName;
        private String updateTime;
        private String installTime;
        private String warrantyTime;
        private String manufacturer;
        private int onlineStatus;
        private Object cameraType;
        private Object resAuths;
        private String fkFieldUuid;
        private Object fkEncoderUuid;
        private double x;
        private double y;

        public String getCameraUuid() {
            return cameraUuid;
        }

        public void setCameraUuid(String cameraUuid) {
            this.cameraUuid = cameraUuid;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getEquUuid() {
            return equUuid;
        }

        public void setEquUuid(String equUuid) {
            this.equUuid = equUuid;
        }

        public String getCameraName() {
            return cameraName;
        }

        public void setCameraName(String cameraName) {
            this.cameraName = cameraName;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getInstallTime() {
            return installTime;
        }

        public void setInstallTime(String installTime) {
            this.installTime = installTime;
        }

        public String getWarrantyTime() {
            return warrantyTime;
        }

        public void setWarrantyTime(String warrantyTime) {
            this.warrantyTime = warrantyTime;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }

        public int getOnlineStatus() {
            return onlineStatus;
        }

        public void setOnlineStatus(int onlineStatus) {
            this.onlineStatus = onlineStatus;
        }

        public Object getCameraType() {
            return cameraType;
        }

        public void setCameraType(Object cameraType) {
            this.cameraType = cameraType;
        }

        public Object getResAuths() {
            return resAuths;
        }

        public void setResAuths(Object resAuths) {
            this.resAuths = resAuths;
        }

        public String getFkFieldUuid() {
            return fkFieldUuid;
        }

        public void setFkFieldUuid(String fkFieldUuid) {
            this.fkFieldUuid = fkFieldUuid;
        }

        public Object getFkEncoderUuid() {
            return fkEncoderUuid;
        }

        public void setFkEncoderUuid(Object fkEncoderUuid) {
            this.fkEncoderUuid = fkEncoderUuid;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }
}
