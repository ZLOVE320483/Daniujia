package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by ZLOVE on 2016/6/29.
 */
public class AllCityRespVo {

    private String returncode;
    private String returnmsg;
    private int errcode;
    private String errmsg;
    private List<RecommendCity> recommendCitys;
    private List<CommonCity> citys;

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

    public List<RecommendCity> getRecommendCitys() {
        return recommendCitys;
    }

    public void setRecommendCitys(List<RecommendCity> recommendCitys) {
        this.recommendCitys = recommendCitys;
    }

    public List<CommonCity> getCitys() {
        return citys;
    }

    public void setCitys(List<CommonCity> citys) {
        this.citys = citys;
    }

    public static class RecommendCity {
        private String name;
        private String cityCode;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }
    }

    public static class CommonCity {
        private String name;
        private String cityCode;
        private String phonetic;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getPhonetic() {
            return phonetic;
        }

        public void setPhonetic(String phonetic) {
            this.phonetic = phonetic;
        }
    }
}
