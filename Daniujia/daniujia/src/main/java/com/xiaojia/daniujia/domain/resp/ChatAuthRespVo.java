package com.xiaojia.daniujia.domain.resp;

/**
 * Created by ZLOVE on 2016/12/19.
 */
public class ChatAuthRespVo {
    private String userData;
    private String signature;
    private String returncode;
    private String returnmsg;

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
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
