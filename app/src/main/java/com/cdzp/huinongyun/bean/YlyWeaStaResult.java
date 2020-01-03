package com.cdzp.huinongyun.bean;

/**
 * 原力元气象站数据
 */
public class YlyWeaStaResult {

    /**
     * code : 200
     * message : success
     * data :
     */

    private String code;
    private String message;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 8578
         * deviceEui : 4a770538000002
         * deviceType : 3
         * timeStam : 2018-06-09 12:48:13
         * voltage : 12.04
         * lightIntensity : 1040.00
         * dailyRainFall : 7.20
         * rainFall : 0.00
         * airTemperature : 22.17
         * lightDuration : 0.00
         * airHumidity : 78.40
         * ph : 7.40
         * windDirection : 1.00
         * windSpeed : 2.25
         */

        private int id;
        private String deviceEui;
        private int deviceType;
        private String timeStam;
        private String voltage;
        private String lightIntensity;
        private String dailyRainFall;
        private String rainFall;
        private String airTemperature;
        private String lightDuration;
        private String airHumidity;
        private String ph;
        private String windDirection;
        private String windSpeed;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDeviceEui() {
            return deviceEui;
        }

        public void setDeviceEui(String deviceEui) {
            this.deviceEui = deviceEui;
        }

        public int getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(int deviceType) {
            this.deviceType = deviceType;
        }

        public String getTimeStam() {
            return timeStam;
        }

        public void setTimeStam(String timeStam) {
            this.timeStam = timeStam;
        }

        public String getVoltage() {
            return voltage;
        }

        public void setVoltage(String voltage) {
            this.voltage = voltage;
        }

        public String getLightIntensity() {
            return lightIntensity;
        }

        public void setLightIntensity(String lightIntensity) {
            this.lightIntensity = lightIntensity;
        }

        public String getDailyRainFall() {
            return dailyRainFall;
        }

        public void setDailyRainFall(String dailyRainFall) {
            this.dailyRainFall = dailyRainFall;
        }

        public String getRainFall() {
            return rainFall;
        }

        public void setRainFall(String rainFall) {
            this.rainFall = rainFall;
        }

        public String getAirTemperature() {
            return airTemperature;
        }

        public void setAirTemperature(String airTemperature) {
            this.airTemperature = airTemperature;
        }

        public String getLightDuration() {
            return lightDuration;
        }

        public void setLightDuration(String lightDuration) {
            this.lightDuration = lightDuration;
        }

        public String getAirHumidity() {
            return airHumidity;
        }

        public void setAirHumidity(String airHumidity) {
            this.airHumidity = airHumidity;
        }

        public String getPh() {
            return ph;
        }

        public void setPh(String ph) {
            this.ph = ph;
        }

        public String getWindDirection() {
            return windDirection;
        }

        public void setWindDirection(String windDirection) {
            this.windDirection = windDirection;
        }

        public String getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(String windSpeed) {
            this.windSpeed = windSpeed;
        }
    }
}
