package com.xiaojia.daniujia.domain.resp;


import java.util.List;

/**
 * Created by Administrator on 2016/5/16.
 */
public class NovationByTopicVo {

    /**
     * pageNum : 1
     * perPageNum : 10
     * totalNum : 0
     * filterNovations : [{"name":"全部","code":"0000"}]
     * consultants : []
     * returncode : SUCCESS
     * returnmsg : 数据返回成功
     */

    private String pageNum;
    private int perPageNum;
    private String totalNum;
    private String returncode;
    private String returnmsg;
    /**
     * name : 全部
     * code : 0000
     */

    private List<FilterDirectionsEntity> filterDirections;
    private List<ConsultEntity> consultants;

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public void setPerPageNum(int perPageNum) {
        this.perPageNum = perPageNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public void setReturncode(String returncode) {
        this.returncode = returncode;
    }

    public void setReturnmsg(String returnmsg) {
        this.returnmsg = returnmsg;
    }

    public void setFilterDirections(List<FilterDirectionsEntity> filterDirections) {
        this.filterDirections = filterDirections;
    }

    @Override
    public String toString() {
        return "NovationByTopicVo{" +
                "pageNum='" + pageNum + '\'' +
                ", perPageNum=" + perPageNum +
                ", totalNum='" + totalNum + '\'' +
                ", returncode='" + returncode + '\'' +
                ", returnmsg='" + returnmsg + '\'' +
                ", filterNovations=" + filterDirections +
                ", consultants=" + consultants +
                '}';
    }

    public void setConsultants(List<ConsultEntity> consultants) {
        this.consultants = consultants;
    }

    public String getPageNum() {
        return pageNum;
    }

    public int getPerPageNum() {
        return perPageNum;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public String getReturncode() {
        return returncode;
    }

    public String getReturnmsg() {
        return returnmsg;
    }

    public List<FilterDirectionsEntity> getFilterDirections() {
        return filterDirections;
    }

    public List<ConsultEntity> getConsultants() {
        return consultants;
    }

    public static class FilterDirectionsEntity {
        private String name;
        private String code;

        public void setName(String name) {
            this.name = name;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
        }

        @Override
        public String toString() {
            return "FilterNovationsEntity{" +
                    "name='" + name + '\'' +
                    ", code='" + code + '\'' +
                    '}';
        }
    }
}
