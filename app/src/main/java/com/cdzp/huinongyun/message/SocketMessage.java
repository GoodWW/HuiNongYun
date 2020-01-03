package com.cdzp.huinongyun.message;

/**
 * socket消息
 */

public class SocketMessage {
    private int ZP_KEY_CLIENT = 987656789;//标识符
    private String ip = "192.168.4.1";//地址
    private int port = 50005;//端口
    private int requestType;//请求类型，int32
    private String mSender;//发送者
    private String mReceiver;//接受者
    private int commandType;//命令类型，int32
    private long coilStatus;
    private String playNo;//终端编号(日光温室用)
    private Object result;//其他数据
    private int fertType;//施肥机控制方式

    public int getZP_KEY_CLIENT() {
        return ZP_KEY_CLIENT;
    }

    public void setZP_KEY_CLIENT(int ZP_KEY_CLIENT) {
        this.ZP_KEY_CLIENT = ZP_KEY_CLIENT;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public String getmSender() {
        return mSender;
    }

    public void setmSender(String mSender) {
        this.mSender = mSender;
    }

    public String getmReceiver() {
        return mReceiver;
    }

    public void setmReceiver(String mReceiver) {
        this.mReceiver = mReceiver;
    }

    public int getCommandType() {
        return commandType;
    }

    public void setCommandType(int commandType) {
        this.commandType = commandType;
    }

    public long getCoilStatus() {
        return coilStatus;
    }

    public void setCoilStatus(long coilStatus) {
        this.coilStatus = coilStatus;
    }

    public String getPlayNo() {
        return playNo;
    }

    public void setPlayNo(String playNo) {
        this.playNo = playNo;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public int getFertType() {
        return fertType;
    }

    public void setFertType(int fertType) {
        this.fertType = fertType;
    }
}
