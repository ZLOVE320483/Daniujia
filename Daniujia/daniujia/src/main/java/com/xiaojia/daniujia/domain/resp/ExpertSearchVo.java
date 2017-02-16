package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by ZLOVE on 2016/5/6.
 */
public class ExpertSearchVo {

    private List<ConsultEntity> consultants;
    private int pageNum;
    private int totalNum;
    private int perPageNum;
    private String returncode;
    private String returnmsg;

    public List<ConsultEntity> getConsultants() {
        return consultants;
    }

    public void setConsultants(List<ConsultEntity> consultants) {
        this.consultants = consultants;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getPerPageNum() {
        return perPageNum;
    }

    public void setPerPageNum(int perPageNum) {
        this.perPageNum = perPageNum;
    }

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

}
