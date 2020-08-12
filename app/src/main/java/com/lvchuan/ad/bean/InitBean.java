package com.lvchuan.ad.bean;

import java.util.List;

public class InitBean {
    /**
     * common_return : true
     * return_info : [{"address":"string11","advertisement":[{"advertisementMode":"","available":"","createTime":null,"delFlag":"","global":"","id":0,"mode":"3","path":"https://www.baidu.com/","updateTime":null}],"boxId":"1","createTime":null,"delFlag":"","devId":"123","id":1,"latitude":"123.123","longitude":"123.123","mode":"3","onlineState":"0","path":"https://www.baidu.com/","updateTime":null}]
     */

    private boolean common_return;
    private List<ReturnInfoBean> return_info;

    public boolean isCommon_return() {
        return common_return;
    }

    public void setCommon_return(boolean common_return) {
        this.common_return = common_return;
    }

    public List<ReturnInfoBean> getReturn_info() {
        return return_info;
    }

    public void setReturn_info(List<ReturnInfoBean> return_info) {
        this.return_info = return_info;
    }

    public static class ReturnInfoBean {
        /**
         * address : string11
         * advertisement : [{"advertisementMode":"","available":"","createTime":null,"delFlag":"","global":"","id":0,"mode":"3","path":"https://www.baidu.com/","updateTime":null}]
         * boxId : 1
         * createTime : null
         * delFlag :
         * devId : 123
         * id : 1
         * latitude : 123.123
         * longitude : 123.123
         * mode : 3
         * onlineState : 0
         * path : https://www.baidu.com/
         * updateTime : null
         */

        private String address;
        private String boxId;
        private Object createTime;
        private String delFlag;
        private String devId;
        private int id;
        private String latitude;
        private String longitude;
        private String mode;
        private String onlineState;
        private String path;
        private Object updateTime;
        private List<AdvertisementBean> advertisement;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBoxId() {
            return boxId;
        }

        public void setBoxId(String boxId) {
            this.boxId = boxId;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
        }

        public String getDevId() {
            return devId;
        }

        public void setDevId(String devId) {
            this.devId = devId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public List<AdvertisementBean> getAdvertisement() {
            return advertisement;
        }

        public void setAdvertisement(List<AdvertisementBean> advertisement) {
            this.advertisement = advertisement;
        }

        public static class AdvertisementBean {
            /**
             * advertisementMode :
             * available :
             * createTime : null
             * delFlag :
             * global :
             * id : 0
             * mode : 3
             * path : https://www.baidu.com/
             * updateTime : null
             */

            private String advertisementMode;
            private String available;
            private Object createTime;
            private String delFlag;
            private String global;
            private int id;
            private String mode;
            private String path;
            private Object updateTime;

            public String getAdvertisementMode() {
                return advertisementMode;
            }

            public void setAdvertisementMode(String advertisementMode) {
                this.advertisementMode = advertisementMode;
            }

            public String getAvailable() {
                return available;
            }

            public void setAvailable(String available) {
                this.available = available;
            }

            public Object getCreateTime() {
                return createTime;
            }

            public void setCreateTime(Object createTime) {
                this.createTime = createTime;
            }

            public String getDelFlag() {
                return delFlag;
            }

            public void setDelFlag(String delFlag) {
                this.delFlag = delFlag;
            }

            public String getGlobal() {
                return global;
            }

            public void setGlobal(String global) {
                this.global = global;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getMode() {
                return mode;
            }

            public void setMode(String mode) {
                this.mode = mode;
            }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public Object getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(Object updateTime) {
                this.updateTime = updateTime;
            }
        }
    }
}
