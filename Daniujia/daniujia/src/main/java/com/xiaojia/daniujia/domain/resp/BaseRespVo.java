package com.xiaojia.daniujia.domain.resp;

/**
 * Created by ZLOVE on 2016/5/11.
 */
public class BaseRespVo {

    private String returncode;
    private String returnmsg;
    private int serviceProductStatus;

    public int getServiceProductStatus() {
        return serviceProductStatus;
    }

    public void setServiceProductStatus(int serviceProductStatus) {
        this.serviceProductStatus = serviceProductStatus;
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

    private int anonymous;
    private int profile_status;

    public int getProfile_status() {
        return profile_status;
    }

    public void setProfile_status(int profile_status) {
        this.profile_status = profile_status;
    }

    public int getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(int anonymous) {
        this.anonymous = anonymous;
    }
}
