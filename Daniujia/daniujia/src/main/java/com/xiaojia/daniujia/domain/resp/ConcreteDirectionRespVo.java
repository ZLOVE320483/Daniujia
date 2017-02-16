package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by ZLOVE on 2016/7/1.
 */
public class ConcreteDirectionRespVo {

    private String returncode;
    private String returnmsg;
    private int errcode;
    private String errmsg;
    private List<ConcreteDirection> concreteDirections;


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

    public List<ConcreteDirection> getConcreteDirections() {
        return concreteDirections;
    }

    public void setConcreteDirections(List<ConcreteDirection> concreteDirections) {
        this.concreteDirections = concreteDirections;
    }

}
