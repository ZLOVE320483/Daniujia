package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */
public class ExpertInfoEntity {
    private List<City> cities;//	array| [cities]	筛选条件城市数据, 无数据则为空数组
    private List<Direction> directions;//	array| [directions]	筛选条件方向数据, 无数据则为空数组
    private List<Industry> industries; //	array| [industries]	筛选条件行业数据, 无数据则为空数组
    private int itemCount;//	int	当前获取到的专家数量
    private List<Expert> items;//	array	专家列表, 元素为 专家对象
    private int totalCount;//	int	所有符合搜索的专家数量
    private boolean showConditionBar;//	bool	是否显示筛选条件, false 则去除
    private boolean refreshCondition;//	bool	是否刷新条件数据, true 则刷新
    private String shareUrl;

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public List<Direction> getDirections() {
        return directions;
    }

    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    public List<Industry> getIndustries() {
        return industries;
    }

    public void setIndustries(List<Industry> industries) {
        this.industries = industries;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public List<Expert> getItems() {
        return items;
    }

    public void setItems(List<Expert> items) {
        this.items = items;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isShowConditionBar() {
        return showConditionBar;
    }

    public void setShowConditionBar(boolean showConditionBar) {
        this.showConditionBar = showConditionBar;
    }

    public boolean isRefreshCondition() {
        return refreshCondition;
    }

    public void setRefreshCondition(boolean refreshCondition) {
        this.refreshCondition = refreshCondition;
    }

    public class City{
        private int provinceId;//	int	省id (该值以 provinceId 搜索)
        private String provinceName;//	string	省名称
        private List<Cities> cities;//	array|object	省下城市数组

        public class Cities{
            private int cityId;
            private String cityName;

            public int getCityId() {
                return cityId;
            }

            public void setCityId(int cityId) {
                this.cityId = cityId;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }
        }

        public int getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(int provinceId) {
            this.provinceId = provinceId;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public List<Cities> getCities() {
            return cities;
        }

        public void setCities(List<Cities> cities) {
            this.cities = cities;
        }
    }



    public static class Industry{
        private int industryParentId;	//int	主方向id (该值以 mainDirectionId 搜索)
        private String name	;//string	主方向名称
        private List<SubIndustry> subIndustries;//	array|object	主方向名称子方向数组

        public class SubIndustry{
            private int industryId;//	int	子方向id (该值以 subDirectionId 搜索)
            private String name;//	string	子方向名称

            public int getIndustryId() {
                return industryId;
            }

            public void setIndustryId(int industryId) {
                this.industryId = industryId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public int getIndustryParentId() {
            return industryParentId;
        }

        public void setIndustryParentId(int industryParentId) {
            this.industryParentId = industryParentId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<SubIndustry> getSubIndustries() {
            return subIndustries;
        }

        public void setSubIndustries(List<SubIndustry> subIndustries) {
            this.subIndustries = subIndustries;
        }
    }

    public static class Direction{
        private int mainDirectionId;//	int	主方向id (该值以 mainDirectionId 搜索)
        private String name;//	string	主方向名称
        private List<SubDirection> subDirections;//	array|object	主方向名称子方向数组

        public static class SubDirection{
            private int subDirectionId;
            private String name;

            public int getSubDirectionId() {
                return subDirectionId;
            }

            public void setSubDirectionId(int subDirectionId) {
                this.subDirectionId = subDirectionId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public int getMainDirectionId() {
            return mainDirectionId;
        }

        public void setMainDirectionId(int mainDirectionId) {
            this.mainDirectionId = mainDirectionId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<SubDirection> getSubDirections() {
            return subDirections;
        }

        public void setSubDirections(List<SubDirection> subDirections) {
            this.subDirections = subDirections;
        }
    }

    public class Expert {
        private int consultantId;//	int	专家用户id
        private String name;//	string	专家姓名
        private String position;//	string	专家职业
        private String company;//	string	专家公司
        private int followerCnt;//	int	被关注数
        private float workage;//	float	工作年限
        private int visitCount;//	int	浏览数
        private int anonymous;//	int	是否为匿名专家 1:匿名 0:否
        private String[] concretes;//	array	具体方向
        private String imgurl;

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public int getConsultantId() {
            return consultantId;
        }

        public void setConsultantId(int consultantId) {
            this.consultantId = consultantId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public int getFollowerCnt() {
            return followerCnt;
        }

        public void setFollowerCnt(int followerCnt) {
            this.followerCnt = followerCnt;
        }

        public float getWorkage() {
            return workage;
        }

        public void setWorkage(float workage) {
            this.workage = workage;
        }

        public int getVisitCount() {
            return visitCount;
        }

        public void setVisitCount(int visitCount) {
            this.visitCount = visitCount;
        }

        public int getAnonymous() {
            return anonymous;
        }

        public void setAnonymous(int anonymous) {
            this.anonymous = anonymous;
        }

        public String[] getConcretes() {
            return concretes;
        }

        public void setConcretes(String[] concretes) {
            this.concretes = concretes;
        }
    }
}
