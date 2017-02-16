package com.xiaojia.daniujia.domain.resp;

/**
 * Created by Administrator on 2016/12/15.
 */
public class ExpertInformationVo {
    private String returncode;
    private String returnmsg;

    private ExpertInfoEntity item;

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

    public ExpertInfoEntity getItem() {
        return item;
    }

    public void setItem(ExpertInfoEntity item) {
        this.item = item;
    }
}
