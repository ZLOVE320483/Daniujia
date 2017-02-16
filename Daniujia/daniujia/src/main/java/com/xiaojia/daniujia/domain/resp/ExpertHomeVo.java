package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by ZLOVE on 2016/5/5.
 */
public class ExpertHomeVo {

    private BasicInfo basic;
    private StatisticInfo statistic;
    private String profile;
    private List<CareerInfo> career;
    private Demand demand;
    private List<EducationInfo> education;
    private List<TagsInfo> tags;
    private LastCommentInfo lastComment;
    private int totalCommentNum;
    private ServiceInfo services;
    private String returncode;
    private String returnmsg;
    private boolean isFavorited;
    private List<Speciality> specialities;

    public Demand getDemand() {
        return demand;
    }

    public void setDemand(Demand demand) {
        this.demand = demand;
    }

    public List<Speciality> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(List<Speciality> specialities) {
        this.specialities = specialities;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setIsFavorited(boolean isFavorited) {
        this.isFavorited = isFavorited;
    }

    public BasicInfo getBasic() {
        return basic;
    }

    public void setBasic(BasicInfo basic) {
        this.basic = basic;
    }

    public StatisticInfo getStatistic() {
        return statistic;
    }

    public void setStatistic(StatisticInfo statistic) {
        this.statistic = statistic;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public List<CareerInfo> getCareer() {
        return career;
    }

    public void setCareer(List<CareerInfo> career) {
        this.career = career;
    }

    public List<EducationInfo> getEducation() {
        return education;
    }

    public void setEducation(List<EducationInfo> education) {
        this.education = education;
    }

    public List<TagsInfo> getTags() {
        return tags;
    }

    public void setTags(List<TagsInfo> tags) {
        this.tags = tags;
    }

    public LastCommentInfo getLastComment() {
        return lastComment;
    }

    public void setLastComment(LastCommentInfo lastComment) {
        this.lastComment = lastComment;
    }

    public int getTotalCommentNum() {
        return totalCommentNum;
    }

    public void setTotalCommentNum(int totalCommentNum) {
        this.totalCommentNum = totalCommentNum;
    }

    public ServiceInfo getServices() {
        return services;
    }

    public void setServices(ServiceInfo services) {
        this.services = services;
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

    public static class BasicInfo {
        private String imgurl;
        private String adimgurl;
        private String name;
        private int workage;
        private String company;
        private String companyAbbr;
        private String position;
        private String cityName;

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getCompanyAbbr() {
            return companyAbbr;
        }

        public void setCompanyAbbr(String companyAbbr) {
            this.companyAbbr = companyAbbr;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getWorkage() {
            return workage;
        }

        public void setWorkage(int workage) {
            this.workage = workage;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getAdimgurl() {
            return adimgurl;
        }

        public void setAdimgurl(String adimgurl) {
            this.adimgurl = adimgurl;
        }
    }

    public static class StatisticInfo {
        private int serviceTimes;
        private int consultMins;
        private int followerCnt;

        public int getServiceTimes() {
            return serviceTimes;
        }

        public void setServiceTimes(int serviceTimes) {
            this.serviceTimes = serviceTimes;
        }

        public int getConsultMins() {
            return consultMins;
        }

        public void setConsultMins(int consultMins) {
            this.consultMins = consultMins;
        }

        public int getFollowerCnt() {
            return followerCnt;
        }

        public void setFollowerCnt(int followerCnt) {
            this.followerCnt = followerCnt;
        }
    }

    public static class Demand {
        private boolean demand;
        private String username;
        private String type;
        private int demandId;
        private int expertId;

        public boolean isDemand() {
            return demand;
        }

        public void setDemand(boolean demand) {
            this.demand = demand;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getDemandId() {
            return demandId;
        }

        public void setDemandId(int demandId) {
            this.demandId = demandId;
        }

        public int getExpertId() {
            return expertId;
        }

        public void setExpertId(int expertId) {
            this.expertId = expertId;
        }
    }

    public static class CareerInfo {
        private String company;
        private String position;
        private long begintime;
        private long endtime;
        private String positiondesc;

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public long getBegintime() {
            return begintime;
        }

        public void setBegintime(long begintime) {
            this.begintime = begintime;
        }

        public long getEndtime() {
            return endtime;
        }

        public void setEndtime(long endtime) {
            this.endtime = endtime;
        }

        public String getPositiondesc() {
            return positiondesc;
        }

        public void setPositiondesc(String positiondesc) {
            this.positiondesc = positiondesc;
        }
    }

    public static class EducationInfo {
        private long begintime;
        private long endtime;
        private String degree;
        private String school;

        public long getBegintime() {
            return begintime;
        }

        public void setBegintime(long begintime) {
            this.begintime = begintime;
        }

        public long getEndtime() {
            return endtime;
        }

        public void setEndtime(long endtime) {
            this.endtime = endtime;
        }

        public String getDegree() {
            return degree;
        }

        public void setDegree(String degree) {
            this.degree = degree;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }
    }

    public static class TagsInfo {
        private int tagId;
        private String name;

        public int getTagId() {
            return tagId;
        }

        public void setTagId(int tagId) {
            this.tagId = tagId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class LastCommentInfo {
        private String content;
        private int serviceType;
        private int userid;
        private String imgurl;
        private String name;
        private String username;
        private long commenttime;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getServiceType() {
            return serviceType;
        }

        public void setServiceType(int serviceType) {
            this.serviceType = serviceType;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
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

        public long getCommenttime() {
            return commenttime;
        }

        public void setCommenttime(long commenttime) {
            this.commenttime = commenttime;
        }
    }

    public static class ServiceInfo {
        private PhotoInfo photo;
        private PhoneInfo phone;
        private ChatInfo chat;

        public PhotoInfo getPhoto() {
            return photo;
        }

        public void setPhoto(PhotoInfo photo) {
            this.photo = photo;
        }

        public PhoneInfo getPhone() {
            return phone;
        }

        public void setPhone(PhoneInfo phone) {
            this.phone = phone;
        }

        public ChatInfo getChat() {
            return chat;
        }

        public void setChat(ChatInfo chat) {
            this.chat = chat;
        }

        public static class PhotoInfo {
            private int productId;
            private boolean enabled;
            private String name;
            private String price;

            public int getProductId() {
                return productId;
            }

            public void setProductId(int productId) {
                this.productId = productId;
            }

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }

        public static class PhoneInfo {
            private int productId;
            private boolean enabled;
            private String name;
            private String price;

            public int getProductId() {
                return productId;
            }

            public void setProductId(int productId) {
                this.productId = productId;
            }

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }

        public static class ChatInfo {
            private int productId;
            private boolean enabled;
            private String name;
            private String price;

            public int getProductId() {
                return productId;
            }

            public void setProductId(int productId) {
                this.productId = productId;
            }

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }
    }
}
