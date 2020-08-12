package com.lvchuan.ad.bean;

import java.util.List;

public class StatisticsBean {


    /**
     * common_return : true
     * return_info : [{"date":"2020-08-12 星期三","boxName":"LZ02-316563","data":[{"recycleName":"废纺","unit":"KG","iocPath":"http://120.79.10.126:8888/group1/M00/00/01/rBIWZV8Wpg6AezCyAABNKfQ5ZsY594.jpg","recycleWeight":49.97,"recycleType":"w_spinning"}]}]
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
         * date : 2020-08-12 星期三
         * boxName : LZ02-316563
         * data : [{"recycleName":"废纺","unit":"KG","iocPath":"http://120.79.10.126:8888/group1/M00/00/01/rBIWZV8Wpg6AezCyAABNKfQ5ZsY594.jpg","recycleWeight":49.97,"recycleType":"w_spinning"}]
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
             * recycleName : 废纺
             * unit : KG
             * iocPath : http://120.79.10.126:8888/group1/M00/00/01/rBIWZV8Wpg6AezCyAABNKfQ5ZsY594.jpg
             * recycleWeight : 49.97
             * recycleType : w_spinning
             */

            private String recycleName;
            private String unit;
            private String iocPath;
            private double recycleWeight;
            private String recycleType;

            public String getRecycleName() {
                return recycleName;
            }

            public void setRecycleName(String recycleName) {
                this.recycleName = recycleName;
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

            public double getRecycleWeight() {
                return recycleWeight;
            }

            public void setRecycleWeight(double recycleWeight) {
                this.recycleWeight = recycleWeight;
            }

            public String getRecycleType() {
                return recycleType;
            }

            public void setRecycleType(String recycleType) {
                this.recycleType = recycleType;
            }
        }
    }
}
