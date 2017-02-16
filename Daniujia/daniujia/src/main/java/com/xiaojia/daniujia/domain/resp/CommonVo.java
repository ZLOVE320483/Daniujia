package com.xiaojia.daniujia.domain.resp;

/**
 * Created by Administrator on 2017/2/13.
 */
public class CommonVo {
    /**
     * returncode : SUCCESS
     * returnmsg : 数据返回成功
     */

    private String returncode;
    private String returnmsg;

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
