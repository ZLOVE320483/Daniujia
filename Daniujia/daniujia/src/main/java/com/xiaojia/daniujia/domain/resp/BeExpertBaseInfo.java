package com.xiaojia.daniujia.domain.resp;

/**
 * Created by Administrator on 2016/6/29.
 */
public class BeExpertBaseInfo {

    private String returncode;
    private String returnmsg;
    private int errcode;
    private String errmsg;
    private String imgurl;
    private String name;
    private String company;
    private int workage;
    private String position;
    private String cityName;
    private String cityCode;
    private int gender;
    private int anonymous;

    @Override
    public String toString() {
        return "BeExpertBaseInfo{" +
                "returncode='" + returncode + '\'' +
                ", returnmsg='" + returnmsg + '\'' +
                ", errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", name='" + name + '\'' +
                ", company='" + company + '\'' +
                ", workage=" + workage +
                ", position='" + position + '\'' +
                ", cityName='" + cityName + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", gender=" + gender +
                '}';
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getWorkage() {
        return workage;
    }

    public void setWorkage(int workage) {
        this.workage = workage;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(int anonymous) {
        this.anonymous = anonymous;
    }
}
