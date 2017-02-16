package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public class StepFirstVo {

    /**
     * userId : 72
     * imgurl : https://dn-daniujia.qbox.me/avatar-72-1463709437219.jpg
     * name : ahashshs
     * workage : 55
     * novationName : 服务业
     * functionName : 其他
     * functionId : 137
     * company : fff个很好睡觉睡觉解答
     * position : ttf佘佘
     * gender : 1
     */

    private UserEntity user;
    /**
     * user : {"userId":72,"imgurl":"https://dn-daniujia.qbox.me/avatar-72-1463709437219.jpg","name":"ahashshs","workage":55,"novationName":"服务业","functionName":"其他","functionId":137,"company":"fff个很好睡觉睡觉解答","position":"ttf佘佘","gender":1}
     * tags : []
     * profile :
     * returncode : SUCCESS
     * returnmsg : 数据返回成功
     */

    private String profile;
    private List<BaseInfoVo.Tag> tags;
    private int applyStepValue;

    public int getApplyStepValue() {
        return applyStepValue;
    }

    public void setApplyStepValue(int applyStepValue) {
        this.applyStepValue = applyStepValue;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setTags(List<BaseInfoVo.Tag> tags) {
        this.tags = tags;
    }

    public UserEntity getUser() {
        return user;
    }

    public String getProfile() {
        return profile;
    }

    public List<BaseInfoVo.Tag> getTags() {
        return tags;
    }

    public static class UserEntity {
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


        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
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

        public void setFunctionId(int functionId) {
            this.functionId = functionId;
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

        public int getUserId() {
            return userId;
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

        public int getFunctionId() {
            return functionId;
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
