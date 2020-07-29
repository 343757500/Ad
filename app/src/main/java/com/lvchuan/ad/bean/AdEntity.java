package com.lvchuan.ad.bean;

public class AdEntity {


    /**
     * advertName : null
     * agentId : 1
     * imgHref : //static.leesche.cn/retail/pic_1541585712258.jpg
     * createTime : 2019-01-08 09:32:24
     * module : app-banner
     * id : 1
     * advertType : img
     * jumpLink : null
     * advertId : 3
     */

    private Object advertName;
    private int agentId;
    private String imgHref;
    private String createTime;
    private String module;
    private int id;
    private String advertType;
    private Object jumpLink;
    private int advertId;
    private String localCache;
    private int status = 0; //0未下载1已下载

    public Object getAdvertName() {
        return advertName;
    }

    public void setAdvertName(Object advertName) {
        this.advertName = advertName;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public String getImgHref() {
        return imgHref;
    }

    public void setImgHref(String imgHref) {
        this.imgHref = imgHref;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdvertType() {
        return advertType;
    }

    public void setAdvertType(String advertType) {
        this.advertType = advertType;
    }

    public Object getJumpLink() {
        return jumpLink;
    }

    public void setJumpLink(Object jumpLink) {
        this.jumpLink = jumpLink;
    }

    public int getAdvertId() {
        return advertId;
    }

    public void setAdvertId(int advertId) {
        this.advertId = advertId;
    }

    public String getLocalCache() {
        return localCache;
    }

    public void setLocalCache(String localCache) {
        this.localCache = localCache;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
