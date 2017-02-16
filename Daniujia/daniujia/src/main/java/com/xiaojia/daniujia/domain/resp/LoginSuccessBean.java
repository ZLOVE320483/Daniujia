package com.xiaojia.daniujia.domain.resp;

import com.xiaojia.daniujia.domain.UserInfo;

/**
 * Created by ZLOVE on 2016/11/4.
 */
public class LoginSuccessBean {

    private UserInfo item;
    private String returncode;
    private String returnmsg;

    public UserInfo getItem() {
        return item;
    }

    public void setItem(UserInfo item) {
        this.item = item;
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
