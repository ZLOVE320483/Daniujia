package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */
public class HomeEntity {
    // 首页轮播图
    private List<BannersEntity> banners;
    private List<ActivityEntity> activities;
    // 推荐专家
    private List<ConsultEntity> experts;
    private List<TopicEntity> topics;
    // 悬赏问答
    private List<AskRewardListRespVo.AskRewardListItem> rewardQuestions;
    private boolean questionFeedback;

    public List<AskRewardListRespVo.AskRewardListItem> getRewardQuestions() {
        return rewardQuestions;
    }

    public void setRewardQuestions(List<AskRewardListRespVo.AskRewardListItem> rewardQuestions) {
        this.rewardQuestions = rewardQuestions;
    }

    public List<BannersEntity> getBanners() {
        return banners;
    }

    public void setBanners(List<BannersEntity> banners) {
        this.banners = banners;
    }

    public List<ActivityEntity> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityEntity> activities) {
        this.activities = activities;
    }

    public List<ConsultEntity> getExperts() {
        return experts;
    }

    public void setExperts(List<ConsultEntity> experts) {
        this.experts = experts;
    }

    public boolean isQuestionFeedback() {
        return questionFeedback;
    }

    public void setQuestionFeedback(boolean questionFeedback) {
        this.questionFeedback = questionFeedback;
    }

    public List<TopicEntity> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicEntity> topics) {
        this.topics = topics;
    }

    public static class ActivityEntity{
        private String id;// "id": 1,
        private String title;// "title": "成为专家",
        private String url;//"url": "https://daniujia.com/m/grant",
        private String imgUrl;//"imgUrl": "https://dn-daniujia.qbox.me/knowledge_sharing@3x.jpg"

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }

    public static class BannersEntity {
        private String image;
        private String description;
        private String name;
        private int carouselId;
        private String position;
        private int workage;
        private String company;

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getCarouselId() {
            return carouselId;
        }
        public void setCarouselId(int carouselId) {
            this.carouselId = carouselId;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public void setWorkage(int workage) {
            this.workage = workage;
        }

        public String getImage() {
            return image;
        }

        public String getDescription() {
            return description;
        }

        public String getName() {
            return name;
        }

        public String getPosition() {
            return position;
        }

        public int getWorkage() {
            return workage;
        }
    }

    public static class TopicEntity{
        private String searchKey;//	string	调用搜索接口所需参数各
        private String searchValue;//	string	搜索参数值
        private String desc;//	string	描述文字
        private String iconUrl;//	string	图标

        public String getSearchKey() {
            return searchKey;
        }

        public void setSearchKey(String searchKey) {
            this.searchKey = searchKey;
        }

        public String getSearchValue() {
            return searchValue;
        }

        public void setSearchValue(String searchValue) {
            this.searchValue = searchValue;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }
    }

}
