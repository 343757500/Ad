package com.lvchuan.ad.bean;

import java.util.List;

public class StatisticsBean {
    /**
     * common_return : true
     * return_info : [{"date":"2020-08-06 星期四","boxName":"市社大院","data":[{"recycleType":"30","recycleName":"饮料瓶","recycleWeight":"0","unit":"个","iocPath":"/userfiles/1/images/userfiles/integralType/2020/06/home_plastic.jpg"},{"recycleType":"28","recycleName":"纸类","recycleWeight":"0","unit":"KG","iocPath":"/userfiles/1/images/userfiles/integralType/2020/06/home_book.jpg"},{"recycleType":"32","recycleName":"织物类","recycleWeight":"0","unit":"KG","iocPath":"/userfiles/1/images/userfiles/integralType/2020/06/home_old_clothes.jpg"},{"recycleType":"31","recycleName":"金属/塑料","recycleWeight":"0","unit":"KG","iocPath":"/userfiles/1/images/userfiles/integralType/2020/06/%E9%A6%96%E9%A1%B5_%E5%A1%91%E6%96%99%E9%87%91%E5%B1%9E.jpg"}]}]
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
         * date : 2020-08-06 星期四
         * boxName : 市社大院
         * data : [{"recycleType":"30","recycleName":"饮料瓶","recycleWeight":"0","unit":"个","iocPath":"/userfiles/1/images/userfiles/integralType/2020/06/home_plastic.jpg"},{"recycleType":"28","recycleName":"纸类","recycleWeight":"0","unit":"KG","iocPath":"/userfiles/1/images/userfiles/integralType/2020/06/home_book.jpg"},{"recycleType":"32","recycleName":"织物类","recycleWeight":"0","unit":"KG","iocPath":"/userfiles/1/images/userfiles/integralType/2020/06/home_old_clothes.jpg"},{"recycleType":"31","recycleName":"金属/塑料","recycleWeight":"0","unit":"KG","iocPath":"/userfiles/1/images/userfiles/integralType/2020/06/%E9%A6%96%E9%A1%B5_%E5%A1%91%E6%96%99%E9%87%91%E5%B1%9E.jpg"}]
         */

        private String date;
        private String boxName;
        private List<DataBean> data;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getBoxName() {
            return boxName;
        }

        public void setBoxName(String boxName) {
            this.boxName = boxName;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * recycleType : 30
             * recycleName : 饮料瓶
             * recycleWeight : 0
             * unit : 个
             * iocPath : /userfiles/1/images/userfiles/integralType/2020/06/home_plastic.jpg
             */

            private String recycleType;
            private String recycleName;
            private String recycleWeight;
            private String unit;
            private String iocPath;

            public String getRecycleType() {
                return recycleType;
            }

            public void setRecycleType(String recycleType) {
                this.recycleType = recycleType;
            }

            public String getRecycleName() {
                return recycleName;
            }

            public void setRecycleName(String recycleName) {
                this.recycleName = recycleName;
            }

            public String getRecycleWeight() {
                return recycleWeight;
            }

            public void setRecycleWeight(String recycleWeight) {
                this.recycleWeight = recycleWeight;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public String getIocPath() {
                return iocPath;
            }

            public void setIocPath(String iocPath) {
                this.iocPath = iocPath;
            }
        }
    }
}
