package com.xiaojia.daniujia.domain.resp;

/**
 * Created by ZLOVE on 2016/5/13.
 */
public class UserCenterRespVo {

    private UserDesc userDesc;
    private int pendingOrdersCnt;
    private int needingServiceOrdersCnt;
    private String returncode;
    private String returnmsg;
    private int beconsultanttip;
    private int consultantFirstLogin;
    private int rewardCount;
    private int demandCount;

    public int getDemandCount() {
        return demandCount;
    }

    public void setDemandCount(int demandCount) {
        this.demandCount = demandCount;
    }

    public int getRewardCount() {
        return rewardCount;
    }
    public void setRewardCount(int rewardCount) {
        this.rewardCount = rewardCount;
    }
    public int getBeconsultanttip() {
        return beconsultanttip;
    }

    public void setBeconsultanttip(int beconsultanttip) {
        this.beconsultanttip = beconsultanttip;
    }

    public int getConsultantFirstLogin() {
        return consultantFirstLogin;
    }

    public void setConsultantFirstLogin(int consultantFirstLogin) {
        this.consultantFirstLogin = consultantFirstLogin;
    }

    public UserDesc getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(UserDesc userDesc) {
        this.userDesc = userDesc;
    }

    public int getPendingOrdersCnt() {
        return pendingOrdersCnt;
    }

    public void setPendingOrdersCnt(int pendingOrdersCnt) {
        this.pendingOrdersCnt = pendingOrdersCnt;
    }

    public int getNeedingServiceOrdersCnt() {
        return needingServiceOrdersCnt;
    }

    public void setNeedingServiceOrdersCnt(int needingServiceOrdersCnt) {
        this.needingServiceOrdersCnt = needingServiceOrdersCnt;
    }

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

    public static class UserDesc {
        private int userId;
        private int identity;
        private String imgurl;
        private int workage;
        private String name;
        private String username;
        private String position;
        private String company;
        private int applyStepValue;

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public int getWorkage() {
            return workage;
        }

        public void setWorkage(int workage) {
            this.workage = workage;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getIdentity() {
            return identity;
        }

        public void setIdentity(int identity) {
            this.identity = identity;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public int getApplyStepValue() {
            return applyStepValue;
        }

        public void setApplyStepValue(int applyStepValue) {
            this.applyStepValue = applyStepValue;
        }

        @Override
        public String toString() {
            return "UserDesc{" +
                    "userId=" + userId +
                    ", identity=" + identity +
                    ", workage=" + workage +
                    ", name='" + name + '\'' +
                    ", username='" + username + '\'' +
                    ", position='" + position + '\'' +
                    ", company='" + company + '\'' +
                    ", applyStepValue=" + applyStepValue +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserCenterRespVo{" +
                "userDesc=" + userDesc +
                ", pendingOrdersCnt=" + pendingOrdersCnt +
                ", needingServiceOrdersCnt=" + needingServiceOrdersCnt +
                ", returncode='" + returncode + '\'' +
                ", returnmsg='" + returnmsg + '\'' +
                ", beconsultanttip=" + beconsultanttip +
                ", consultantFirstLogin=" + consultantFirstLogin +
                '}';
    }
}
