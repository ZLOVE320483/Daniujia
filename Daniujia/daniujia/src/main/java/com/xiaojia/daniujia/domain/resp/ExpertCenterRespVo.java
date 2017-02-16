package com.xiaojia.daniujia.domain.resp;

/**
 * Created by ZLOVE on 2016/5/13.
 */
public class ExpertCenterRespVo {
    private String returncode;
    private String returnmsg;
    private int errcode;
    private String errmsg;
    private int consultantId;
    private int favoriteMeCnt;
    private float actBalance;
    private int dailyPageVisitCnt;

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

    public int getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(int consultantId) {
        this.consultantId = consultantId;
    }

    public int getFavoriteMeCnt() {
        return favoriteMeCnt;
    }

    public void setFavoriteMeCnt(int favoriteMeCnt) {
        this.favoriteMeCnt = favoriteMeCnt;
    }

    public float getActBalance() {
        return actBalance;
    }

    public void setActBalance(float actBalance) {
        this.actBalance = actBalance;
    }

    public int getDailyPageVisitCnt() {
        return dailyPageVisitCnt;
    }

    public void setDailyPageVisitCnt(int dailyPageVisitCnt) {
        this.dailyPageVisitCnt = dailyPageVisitCnt;
    }
}
