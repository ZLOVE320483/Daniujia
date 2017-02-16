package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by Administrator on 2016/5/5.
 */
public class HomeBeanVo {
    /**
     * carousels : [{"image":"http://7xotcn.com2.z0.glb.qiniucdn.com/ceshi3.jpg","description":"我能为您做什么","name":"韦利东","position":"总经理","workage":0},{"image":"http://7xotcn.com2.z0.glb.qiniucdn.com/ceshi2.jpg","description":"我能为您做什么","name":"盛进","position":"集成计划与交付保证经理","workage":0},{"image":"http://7xotcn.com2.z0.glb.qiniucdn.com/ceshi4.jpg","description":"我能为您做什么","name":"卢文融","position":"运营总监","workage":0},{"image":"http://7xotcn.com2.z0.glb.qiniucdn.com/ceshi5.jpg","description":"我能为您做什么","name":"吕冬芳","position":"中国区财务分析","workage":0},{"image":"http://7xotcn.com2.z0.glb.qiniucdn.com/ceshi6.jpg","description":"我能为您做什么","name":"贺加贝","position":"总经理","workage":0}]
     * topics : [{"id":1,"bgimage":"http://7xotcn.com2.z0.glb.qiniucdn.com/topic_test1.png"},{"id":2,"bgimage":"http://7xotcn.com2.z0.glb.qiniucdn.com/topic_test2.png"},{"id":3,"bgimage":"http://7xotcn.com2.z0.glb.qiniucdn.com/topic_test3.png"},{"id":4,"bgimage":"http://7xotcn.com2.z0.glb.qiniucdn.com/topic_test4.png"}]
     * consultants : [{"id":29,"name":"卢文融","imgurl":"https://dn-daniujia.qbox.me/Fn2TmXkCuDt2lNKWhfAndS_2bKCV","workage":0,"position":"运营总监","company":"上海晓家网络科技有限公司","followerCnt":"8"},{"id":84,"name":"朱燕青","imgurl":"https://dn-daniujia.qbox.me/FqA_-5-rQ6ZI6mUlLb6tj2CUFzJw","workage":0,"position":"咨询总监","company":"上海济浩投资管理有限公司","followerCnt":"4"},{"id":157,"name":"谢晓丽","imgurl":"https://dn-daniujia.qbox.me/FqOmFLpQbH5uvSBqK8aYJEBOKH5d","workage":0,"position":"资深财税咨询顾问","company":"上海理鎔财务咨询有限公司","followerCnt":"3"},{"id":28,"name":"韦利东","imgurl":"http://7xlqoz.com1.z0.glb.clouddn.com/FlZE2ZtJHCwuZggPEHJ1OSthNENB","workage":0,"position":"总经理","company":"上海泛微网络科技股份有限公司","followerCnt":"3"},{"id":48,"name":"金戈","imgurl":"https://dn-daniujia.qbox.me/moren.png","workage":0,"position":"副总裁","company":"上海泛微网络科技股份有限公司","followerCnt":"3"},{"id":141,"name":"张一帆","imgurl":"https://dn-daniujia.qbox.me/FtelfA2o0K-RllhRDJtgYuC8iF6W","workage":0,"position":"投资经理","company":"上海经邦股权投资基金管理有限公司","followerCnt":"3"},{"id":73,"name":"张涛","imgurl":"http://7xlqoz.com1.z0.glb.clouddn.com/Fuzh0osaK2pRuHT6_buUJAnGGtBu","workage":0,"position":"律师","company":"上海君澜律师事务所","followerCnt":"3"},{"id":27,"name":"盛进","imgurl":"https://dn-daniujia.qbox.me/moren.png","workage":0,"position":"集成计划与交付保证经理","company":"远景能源","followerCnt":"2"},{"id":36,"name":"董懿为","imgurl":"https://dn-daniujia.qbox.me/moren.png","workage":0,"position":"董事会秘书","company":"山东布莱凯特黑牛科技股份有限公司","followerCnt":"2"},{"id":40,"name":"徐博","imgurl":"https://dn-daniujia.qbox.me/moren.png","workage":0,"position":"人才测评顾问","company":"中智人力资源管理咨询公司","followerCnt":"2"}]
     * returncode : SUCCESS
     * returnmsg : 数据返回成功
     */

    private String returncode;
    private String returnmsg;
    /**
     * image : http://7xotcn.com2.z0.glb.qiniucdn.com/ceshi3.jpg
     * description : 我能为您做什么
     * name : 韦利东
     * position : 总经理
     * workage : 0
     */

    private List<CarouselsEntity> carousels;
    /**
     * id : 1
     * bgimage : http://7xotcn.com2.z0.glb.qiniucdn.com/topic_test1.png
     */

    private List<TopicsEntity> topics;
    /**
     * id : 29
     * name : 卢文融
     * imgurl : https://dn-daniujia.qbox.me/Fn2TmXkCuDt2lNKWhfAndS_2bKCV
     * workage : 0
     * position : 运营总监
     * company : 上海晓家网络科技有限公司
     * followerCnt : 8
     */

    private List<ConsultEntity> consultants;

    public void setReturncode(String returncode) {
        this.returncode = returncode;
    }

    public void setReturnmsg(String returnmsg) {
        this.returnmsg = returnmsg;
    }

    public void setCarousels(List<CarouselsEntity> carousels) {
        this.carousels = carousels;
    }

    public void setTopics(List<TopicsEntity> topics) {
        this.topics = topics;
    }

    public void setConsultants(List<ConsultEntity> consultants) {
        this.consultants = consultants;
    }

    public String getReturncode() {
        return returncode;
    }

    public String getReturnmsg() {
        return returnmsg;
    }

    public List<CarouselsEntity> getCarousels() {
        return carousels;
    }

    public List<TopicsEntity> getTopics() {
        return topics;
    }

    public List<ConsultEntity> getConsultants() {
        return consultants;
    }

    public static class CarouselsEntity {
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

    public static class TopicsEntity {
        private int topicId;
        private String bgimage;
        private String name;
        private int mainDirectionId;
        private String subDirectionCode;

        public int getMainDirectionId() {
            return mainDirectionId;
        }

        public void setMainDirectionId(int mainDirectionId) {
            this.mainDirectionId = mainDirectionId;
        }

        public String getSubDirectionCode() {
            return subDirectionCode;
        }

        public void setSubDirectionCode(String subDirectionCode) {
            this.subDirectionCode = subDirectionCode;
        }

        public int getTopicId() {
            return topicId;
        }

        public void setTopicId(int topicId) {
            this.topicId = topicId;
        }

        public void setBgimage(String bgimage) {
            this.bgimage = bgimage;
        }

        public String getBgimage() {
            return bgimage;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
