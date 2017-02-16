package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by ZLOVE on 2016/6/30.
 */
public class DirectionRelativeIndustryRespVo {

    private String returncode;
    private String returnmsg;
    private int errcode;
    private String errmsg;
    private List<RelatedIndustry> resultIndustrys;

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

    public List<RelatedIndustry> getResultIndustrys() {
        return resultIndustrys;
    }

    public void setResultIndustrys(List<RelatedIndustry> resultIndustrys) {
        this.resultIndustrys = resultIndustrys;
    }

}
