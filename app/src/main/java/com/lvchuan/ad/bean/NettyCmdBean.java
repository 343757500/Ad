package com.lvchuan.ad.bean;

public class NettyCmdBean {

    /**
     * createTime : 1596589793000
     * devId : 123
     * latitude : 123.123
     * longitude : 123.123
     * mode : 3
     * onlineState : 0
     * path : http://120.79.10.126:8888/group1/M00/00/03/rBIWZV8tBTKASUAEAAB-Xo0I5WE841.jpg,http://120.79.10.126:8888/group1/M00/00/03/rBIWZV8tJtWAJPIUALLuCQug2js518.mp4,http://120.79.10.126:8888/group1/M00/00/03/rBIWZV8tNJeALJh_AA6vxWgRTbA947.jpg,http://120.79.10.126:8888/group1/M00/00/03/rBIWZV8tN5yAOg5xAAA1AfcCf1M268.jpg,http://120.79.10.126:8888/group1/M00/00/03/rBIWZV8wrBqAF6azAAA6RZqHbbI944.jpg,http://120.79.10.126:8888/group1/M00/00/03/rBIWZV8wsPaARMTEAAA6RZqHbbI504.jpg,2222
     */

    private long createTime;
    private String devId;
    private String latitude;
    private String longitude;
    private String mode;
    private String onlineState;
    private String path;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getOnlineState() {
        return onlineState;
    }

    public void setOnlineState(String onlineState) {
        this.onlineState = onlineState;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
