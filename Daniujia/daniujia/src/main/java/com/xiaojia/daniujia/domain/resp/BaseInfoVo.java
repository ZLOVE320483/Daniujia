package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
public class BaseInfoVo {
    @Override
    public String toString() {
        return "BaseInfoVo{" +
                "user=" + user +
                ", returncode='" + returncode + '\'' +
                ", returnmsg='" + returnmsg + '\'' +
                ", tags=" + tags +
                '}';
    }

    /**
     * imgurl : https://dn-daniujia.qbox.me/avatar-72-1459503668217.jpg
     * name :
     * workage : 0
     * novationName :
     * functionName :
     * company :
     * position :
     * gender : 0
     */

    private UserEntity user;
    /**
     * user : {"imgurl":"https://dn-daniujia.qbox.me/avatar-72-1459503668217.jpg","name":"","workage":0,"novationName":"","functionName":"","company":"","position":"","gender":0}
     * tags : []
     * returncode : SUCCESS
     * returnmsg : 数据返回成功
     */

    private String returncode;
    private String returnmsg;
    private List<Tag> tags;

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setReturncode(String returncode) {
        this.returncode = returncode;
    }

    public void setReturnmsg(String returnmsg) {
        this.returnmsg = returnmsg;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public UserEntity getUser() {
        return user;
    }

    public String getReturncode() {
        return returncode;
    }

    public String getReturnmsg() {
        return returnmsg;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public static class Tag{
        public Tag(int tagId, String name) {
            this.tagId = tagId;
            this.name = name;
        }

        public Tag(){

        }
        public int tagId;
        public String name;

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

    public static class UserEntity {

        @Override
        public String toString() {
            return "UserEntity{" +
                    "imgurl='" + imgurl + '\'' +
                    ", name='" + name + '\'' +
                    ", workage=" + workage +
                    ", novationName='" + novationName + '\'' +
                    ", functionName='" + functionName + '\'' +
                    ", company='" + company + '\'' +
                    ", position='" + position + '\'' +
                    ", gender=" + gender +
                    '}';
        }

        private int userId;
        private String imgurl;
        private String name;
        private int workage;
        private String novationName;
        private String functionName;
        private int functionId;
        private String company;
        private String position;
        private int gender;

        public int getFunctionId() {
            return functionId;
        }
        public void setFunctionId(int functionId) {
            this.functionId = functionId;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setWorkage(int workage) {
            this.workage = workage;
        }

        public void setNovationName(String novationName) {
            this.novationName = novationName;
        }

        public void setFunctionName(String functionName) {
            this.functionName = functionName;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getImgurl() {
            return imgurl;
        }

        public String getName() {
            return name;
        }

        public int getWorkage() {
            return workage;
        }

        public String getNovationName() {
            return novationName;
        }

        public String getFunctionName() {
            return functionName;
        }

        public String getCompany() {
            return company;
        }

        public String getPosition() {
            return position;
        }

        public int getGender() {
            return gender;
        }
    }
}
