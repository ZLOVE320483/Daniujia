package com.xiaojia.daniujia.domain.resp;

/**
 * Created by Administrator on 2017/1/5.
 */
public class PublishRequireVo {
    private String returncode;
    private String returnmsg;
    private int demandId;

    public int getDemandId() {
        return demandId;
    }

    public void setDemandId(int demandId) {
        this.demandId = demandId;
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
