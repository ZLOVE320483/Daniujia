package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public class WorkExperienceVo {

    /**
     * careers : [{"careerId":40,"entryTime":544204800,"quitTime":1304179200,"position":"经理","company":"泛微","positionDesc":"洗洁精点解点解点解点解"}]
     * returncode : SUCCESS
     * returnmsg : 数据返回成功
     */

    private String returncode;
    private String returnmsg;
    /**
     * careerId : 40
     * entryTime : 544204800
     * quitTime : 1304179200
     * position : 经理
     * company : 泛微
     * positionDesc : 洗洁精点解点解点解点解
     */

    private List<CareersEntity> careers;

    public void setReturncode(String returncode) {
        this.returncode = returncode;
    }

    public void setReturnmsg(String returnmsg) {
        this.returnmsg = returnmsg;
    }

    public void setCareers(List<CareersEntity> careers) {
        this.careers = careers;
    }

    public String getReturncode() {
        return returncode;
    }

    public String getReturnmsg() {
        return returnmsg;
    }

    public List<CareersEntity> getCareers() {
        return careers;
    }

    public static class CareersEntity{
        private int careerId;
        private long entryTime;
        private long quitTime;
        private String position;
        private String company;
        private String positionDesc;

        public void setCareerId(int careerId) {
            this.careerId = careerId;
        }

        public void setEntryTime(int entryTime) {
            this.entryTime = entryTime;
        }

        public void setQuitTime(int quitTime) {
            this.quitTime = quitTime;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public void setPositionDesc(String positionDesc) {
            this.positionDesc = positionDesc;
        }

        public int getCareerId() {
            return careerId;
        }

        public long getEntryTime() {
            return entryTime;
        }

        public void setEntryTime(long entryTime) {
            this.entryTime = entryTime;
        }

        public long getQuitTime() {
            return quitTime;
        }

        public void setQuitTime(long quitTime) {
            this.quitTime = quitTime;
        }

        public String getPosition() {
            return position;
        }

        public String getCompany() {
            return company;
        }

        public String getPositionDesc() {
            return positionDesc;
        }
    }
}
