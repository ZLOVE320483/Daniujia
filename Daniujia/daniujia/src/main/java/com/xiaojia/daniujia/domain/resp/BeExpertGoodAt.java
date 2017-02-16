package com.xiaojia.daniujia.domain.resp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/6/29.
 */
public class BeExpertGoodAt implements Serializable {

    private String returncode;
    private String returnmsg;
    private String errmsg;
    private int errcode;

    public List<Speciality> specialities;

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

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public List<Speciality> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(List<Speciality> specialities) {
        this.specialities = specialities;
    }

}
