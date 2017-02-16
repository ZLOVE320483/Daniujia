package com.xiaojia.daniujia.domain.resp;

/**
 * Created by Administrator on 2016/5/16.
 */
public class ConsultEntity {
    private int consultantId;
    private String name;
    private String imgurl;
    private int workage;
    private String position;
    private String company;
    private String followerCnt;
    private int sectionType = 0;
    private String[] concretes;
    private String visitCount;

    @Override
    public String toString() {
        return "ConsultEntity{" +
                "consultantId=" + consultantId +
                ", name='" + name + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", workage=" + workage +
                ", position='" + position + '\'' +
                ", company='" + company + '\'' +
                ", followerCnt='" + followerCnt + '\'' +
                '}';
    }

    public String[] getConcretes() {
        return concretes;
    }

    public void setConcretes(String[] concretes) {
        this.concretes = concretes;
    }

    public int getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(int consultantId) {
        this.consultantId = consultantId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setWorkage(int workage) {
        this.workage = workage;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setFollowerCnt(String followerCnt) {
        this.followerCnt = followerCnt;
    }

    public String getName() {
        return name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public int getWorkage() {
        return workage;
    }

    public String getPosition() {
        return position;
    }

    public String getCompany() {
        return company;
    }

    public String getFollowerCnt() {
        return followerCnt;
    }

    public int getSectionType() {
        return sectionType;
    }

    public void setSectionType(int sectionType) {
        this.sectionType = sectionType;
    }

    public String getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(String visitCount) {
        this.visitCount = visitCount;
    }
}
