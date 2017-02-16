package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
public class CollectionExpertVo {

    /**
     * pageNum : 1
     * perPageNum : 10
     * totalNum : 0
     * filterNovations : [{"name":"全部","code":"0000"}]
     * consultants : []
     * returncode : SUCCESS
     * returnmsg : 数据返回成功
     */

    private int pageNum;
    private int perPageNum;
    private String totalNum;
    private String returncode;
    private String returnmsg;
    /**
     * name : 全部
     * code : 0000
     */

    private List<NovationByTopicVo.FilterDirectionsEntity> filterDirections;
    private List<ConsultEntity> consultants;

    public void setPageNum(int pageNum) {
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


    public void setConsultants(List<ConsultEntity> consultants) {
        this.consultants = consultants;
    }

    public int getPageNum() {
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

    public List<ConsultEntity> getConsultants() {
        return consultants;
    }

    public List<NovationByTopicVo.FilterDirectionsEntity> getFilterDirections() {
        return filterDirections;
    }

    public void setFilterDirections(List<NovationByTopicVo.FilterDirectionsEntity> filterDirections) {
        this.filterDirections = filterDirections;
    }
}
