package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by ZLOVE on 2016/6/30.
 */
public class AllDirectionRespVo {
    private String returncode;
    private String returnmsg;
    private int errcode;
    private String errmsg;
    private List<MainDirection> mainDirections;

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

    public List<MainDirection> getMainDirections() {
        return mainDirections;
    }

    public void setMainDirections(List<MainDirection> mainDirections) {
        this.mainDirections = mainDirections;
    }

    public static class MainDirection {

        private int mainId;
        private String mainName;
        private String iconUrl;
        private String editIconUrl;
        private List<SubDirection> subDirections;

        public int getMainId() {
            return mainId;
        }

        public void setMainId(int mainId) {
            this.mainId = mainId;
        }

        public String getMainName() {
            return mainName;
        }

        public void setMainName(String mainName) {
            this.mainName = mainName;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getEditIconUrl() {
            return editIconUrl;
        }

        public void setEditIconUrl(String editIconUrl) {
            this.editIconUrl = editIconUrl;
        }

        public List<SubDirection> getSubDirections() {
            return subDirections;
        }

        public void setSubDirections(List<SubDirection> subDirections) {
            this.subDirections = subDirections;
        }

        public static class SubDirection {
            private String subName;
            private int subId;
            private int mainId;

            public String getSubName() {
                return subName;
            }

            public void setSubName(String subName) {
                this.subName = subName;
            }

            public int getSubId() {
                return subId;
            }

            public void setSubId(int subId) {
                this.subId = subId;
            }

            public int getMainId() {
                return mainId;
            }

            public void setMainId(int mainId) {
                this.mainId = mainId;
            }
        }
    }
}
