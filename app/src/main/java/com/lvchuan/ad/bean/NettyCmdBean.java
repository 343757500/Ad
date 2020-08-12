package com.lvchuan.ad.bean;

import java.util.List;

public class NettyCmdBean {


    /**
     * devs : ["1"]
     * flag : init
     * data : 3
     */

    private String flag;
    private String data;
    private List<String> devs;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<String> getDevs() {
        return devs;
    }

    public void setDevs(List<String> devs) {
        this.devs = devs;
    }
}
