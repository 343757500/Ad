package com.lvchuan.ad.bean;

public class UpdateBean {
    /**
     * status : 200
     * data : {"id":4,"path":"http://120.79.10.126:8888/group1/M00/00/04/rBIWZV82cYWAXSsiAXkiSruSL08175.apk","version":"1.0","createTime":"2020-08-14 19:12:17","updateTime":"2020-08-14 19:12:21","type":"2"}
     * rel : true
     */

    private int status;
    private DataBean data;
    private boolean rel;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isRel() {
        return rel;
    }

    public void setRel(boolean rel) {
        this.rel = rel;
    }

    public static class DataBean {
        /**
         * id : 4
         * path : http://120.79.10.126:8888/group1/M00/00/04/rBIWZV82cYWAXSsiAXkiSruSL08175.apk
         * version : 1.0
         * createTime : 2020-08-14 19:12:17
         * updateTime : 2020-08-14 19:12:21
         * type : 2
         */

        private int id;
        private String path;
        private String version;
        private String createTime;
        private String updateTime;
        private String type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
