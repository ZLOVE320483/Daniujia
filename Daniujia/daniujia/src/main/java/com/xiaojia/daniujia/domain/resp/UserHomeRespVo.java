package com.xiaojia.daniujia.domain.resp;

/**
 * Created by ZLOVE on 2016/7/10.
 */
public class UserHomeRespVo {
    private String returncode;
    private String returnmsg;
    private int errcode;
    private String errmsg;
    private BasicInfo basic;

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

    public BasicInfo getBasic() {
        return basic;
    }

    public void setBasic(BasicInfo basic) {
        this.basic = basic;
    }

    public static class BasicInfo {
        private String imgurl;
        private String adimgurl;
        private String username;
        private String name;
        private int workage;
        private String company;
        private String position;
        private int gender;
        private String cityName;

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getAdimgurl() {
            return adimgurl;
        }

        public void setAdimgurl(String adimgurl) {
            this.adimgurl = adimgurl;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
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

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }
    }
}
