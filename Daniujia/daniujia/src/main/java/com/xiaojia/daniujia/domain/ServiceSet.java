package com.xiaojia.daniujia.domain;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ServiceSet {
    private String  msgserviceprice;
    private String    msgservicestatus;
    private String  telserviceprice;
    private String    telservicestatus;
    private String    offlineservicestatus;
    private String  offlineserviceprice;

    public String getMsgserviceprice() {
        return msgserviceprice;
    }

    public void setMsgserviceprice(String msgserviceprice) {
        this.msgserviceprice = msgserviceprice;
    }

    public String getTelserviceprice() {
        return telserviceprice;
    }

    public void setTelserviceprice(String telserviceprice) {
        this.telserviceprice = telserviceprice;
    }

    public String getOfflineserviceprice() {
        return offlineserviceprice;
    }

    public void setOfflineserviceprice(String offlineserviceprice) {
        this.offlineserviceprice = offlineserviceprice;
    }

    public String getMsgservicestatus() {
        return msgservicestatus;
    }

    public void setMsgservicestatus(String msgservicestatus) {
        this.msgservicestatus = msgservicestatus;
    }

    public String getTelservicestatus() {
        return telservicestatus;
    }

    public void setTelservicestatus(String telservicestatus) {
        this.telservicestatus = telservicestatus;
    }

    public String getOfflineservicestatus() {
        return offlineservicestatus;
    }

    public void setOfflineservicestatus(String offlineservicestatus) {
        this.offlineservicestatus = offlineservicestatus;
    }
}
