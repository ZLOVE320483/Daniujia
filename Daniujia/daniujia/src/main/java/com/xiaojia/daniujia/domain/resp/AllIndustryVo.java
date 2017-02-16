package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by Administrator on 2016/5/5.
 */
public class AllIndustryVo {

    private String returncode;
    private String returnmsg;
    private int errcode;
    private String errmsg;
    private List<ClassificationEntity> classifications;

    public String getReturncode() {
        return returncode;
    }

    public void setReturncode(String returncode) {
        this.returncode = returncode;
    }

    public String getReturnmsg() {
        return returnmsg;
    }

    public void setReturnmsg(String returnmsg) {
        this.returnmsg = returnmsg;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public List<ClassificationEntity> getClassifications() {
        return classifications;
    }

    public void setClassifications(List<ClassificationEntity> classifications) {
        this.classifications = classifications;
    }

    public static class ClassificationEntity {
        private int mainDirectionId;
        private String mainDirectionIcon;
        private String mainDirectionName;
        private String mainDirectionCode;
        private List<SubDirectionEntity> subDirections;

        public int getMainDirectionId() {
            return mainDirectionId;
        }

        public void setMainDirectionId(int mainDirectionId) {
            this.mainDirectionId = mainDirectionId;
        }

        public String getMainDirectionIcon() {
            return mainDirectionIcon;
        }

        public void setMainDirectionIcon(String mainDirectionIcon) {
            this.mainDirectionIcon = mainDirectionIcon;
        }

        public String getMainDirectionName() {
            return mainDirectionName;
        }

        public void setMainDirectionName(String mainDirectionName) {
            this.mainDirectionName = mainDirectionName;
        }

        public String getMainDirectionCode() {
            return mainDirectionCode;
        }

        public void setMainDirectionCode(String mainDirectionCode) {
            this.mainDirectionCode = mainDirectionCode;
        }

        public List<SubDirectionEntity> getSubDirections() {
            return subDirections;
        }

        public void setSubDirections(List<SubDirectionEntity> subDirections) {
            this.subDirections = subDirections;
        }

        public static class SubDirectionEntity {
            private int subDirectionId;
            private String subDirectionName;
            private String subDirectionCode;

            public int getSubDirectionId() {
                return subDirectionId;
            }

            public void setSubDirectionId(int subDirectionId) {
                this.subDirectionId = subDirectionId;
            }

            public String getSubDirectionName() {
                return subDirectionName;
            }

            public void setSubDirectionName(String subDirectionName) {
                this.subDirectionName = subDirectionName;
            }

            public String getSubDirectionCode() {
                return subDirectionCode;
            }

            public void setSubDirectionCode(String subDirectionCode) {
                this.subDirectionCode = subDirectionCode;
            }
        }
    }

}
