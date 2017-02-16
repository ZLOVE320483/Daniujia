package com.xiaojia.daniujia.domain.resp;

/**
 * Created by ZLOVE on 2016/8/14.
 */
public class ChangePwdResp {
    private String token;
    private String returncode;
    private String returnmsg;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setReturnmsg(String returnmsg) {
        this.returnmsg = returnmsg;
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
}
