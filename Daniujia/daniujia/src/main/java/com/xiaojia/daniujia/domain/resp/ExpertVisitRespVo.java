package com.xiaojia.daniujia.domain.resp;

/**
 * Created by ZLOVE on 2016/6/3.
 */
public class ExpertVisitRespVo {

    private String returncode;
    private String returnmsg;
    private int errcode;
    private String errmsg;
    private int dailyPageVisitCnt;
    private int monthPageVisitCnt;
    private int totalPageVisitCnt;

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

    public int getDailyPageVisitCnt() {
        return dailyPageVisitCnt;
    }

    public void setDailyPageVisitCnt(int dailyPageVisitCnt) {
        this.dailyPageVisitCnt = dailyPageVisitCnt;
    }

    public int getMonthPageVisitCnt() {
        return monthPageVisitCnt;
    }

    public void setMonthPageVisitCnt(int monthPageVisitCnt) {
        this.monthPageVisitCnt = monthPageVisitCnt;
    }

    public int getTotalPageVisitCnt() {
        return totalPageVisitCnt;
    }

    public void setTotalPageVisitCnt(int totalPageVisitCnt) {
        this.totalPageVisitCnt = totalPageVisitCnt;
    }
}
