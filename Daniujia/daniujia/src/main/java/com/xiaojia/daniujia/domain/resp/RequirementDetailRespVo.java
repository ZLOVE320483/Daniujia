package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by ZLOVE on 2017/1/4.
 */
public class RequirementDetailRespVo {
    private int demandId;
    private String consultantName;
    private int consultantCount;
    private String writeName;
    private boolean certified;
    private String cityCode;
    private String cityName;
    private String budget;
    private String desc;
    private String serviceDay;
    private String serviceCycle;
    private float fee;
    private String feeStatus;
    private long deadline;
    private String status;
    private int viewCount;
    private int enterCount;
    private String currentUser;
    private List<EnterExpert> enterExperts;
    private List<ConfirmedExpert> confirmedExperts;
    private EnterExpert demandUser;
    private EnterExpert applicantUser;

    public int getDemandId() {
        return demandId;
    }

    public void setDemandId(int demandId) {
        this.demandId = demandId;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public int getConsultantCount() {
        return consultantCount;
    }

    public void setConsultantCount(int consultantCount) {
        this.consultantCount = consultantCount;
    }

    public String getWriteName() {
        return writeName;
    }

    public void setWriteName(String writeName) {
        this.writeName = writeName;
    }

    public boolean isCertified() {
        return certified;
    }

    public void setCertified(boolean certified) {
        this.certified = certified;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String getServiceDay() {
        return serviceDay;
    }

    public void setServiceDay(String serviceDay) {
        this.serviceDay = serviceDay;
    }

    public String getServiceCycle() {
        return serviceCycle;
    }

    public void setServiceCycle(String serviceCycle) {
        this.serviceCycle = serviceCycle;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public String getFeeStatus() {
        return feeStatus;
    }

    public void setFeeStatus(String feeStatus) {
        this.feeStatus = feeStatus;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getEnterCount() {
        return enterCount;
    }

    public void setEnterCount(int enterCount) {
        this.enterCount = enterCount;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public List<EnterExpert> getEnterExperts() {
        return enterExperts;
    }

    public void setEnterExperts(List<EnterExpert> enterExperts) {
        this.enterExperts = enterExperts;
    }

    public List<ConfirmedExpert> getConfirmedExperts() {
        return confirmedExperts;
    }

    public void setConfirmedExperts(List<ConfirmedExpert> confirmedExperts) {
        this.confirmedExperts = confirmedExperts;
    }

    public EnterExpert getDemandUser() {
        return demandUser;
    }

    public void setDemandUser(EnterExpert demandUser) {
        this.demandUser = demandUser;
    }

    public EnterExpert getApplicantUser() {
        return applicantUser;
    }

    public void setApplicantUser(EnterExpert applicantUser) {
        this.applicantUser = applicantUser;
    }

    public static class EnterExpert {
        private int id;
        private String name;
        private String username;
        private String imgUrl;
        private int viewCount;
        private String identity;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getViewCount() {
            return viewCount;
        }

        public void setViewCount(int viewCount) {
            this.viewCount = viewCount;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }
    }

    public static class ConfirmedExpert {
        private int id;
        private String name;
        private String username;
        private String imgUrl;
        private List<Evaluation> evaluations;
        private String identity;

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public List<Evaluation> getEvaluations() {
            return evaluations;
        }

        public void setEvaluations(List<Evaluation> evaluations) {
            this.evaluations = evaluations;
        }

        public static class Evaluation {
            private String content;
            private long datetime;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public long getDatetime() {
                return datetime;
            }

            public void setDatetime(long datetime) {
                this.datetime = datetime;
            }
        }
    }
}
