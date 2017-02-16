package com.xiaojia.daniujia.domain.resp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */
public class SearchDataEntity {
    private List<Directions> directions;//	array	方向数据 [condition-directions]
    private List<Industries> industries;//	array	行业数据 [condition-industries]

    public List<Directions> getDirections() {
        return directions;
    }

    public void setDirections(ArrayList<Directions> directions) {
        this.directions = directions;
    }

    public List<Industries> getIndustries() {
        return industries;
    }

    public void setIndustries(ArrayList<Industries> industries) {
        this.industries = industries;
    }

    // 行业
    public static class Industries{
        private String desc;//	string	描述(用于显示)
        private String searchKey;//	string	用于搜索key
        private String searchValue;//	array|string	用于搜索value
        private String iconUrl;//	string	图标地址

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

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

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }
    }

    public static class Directions{
        private String desc;//	string	描述(用于显示)
        private String searchKey;//	string	用于搜索key
        private String searchValue;//	array|string	用于搜索value
        private String iconUrl;//	string	图标地址

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

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

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }
    }
}
