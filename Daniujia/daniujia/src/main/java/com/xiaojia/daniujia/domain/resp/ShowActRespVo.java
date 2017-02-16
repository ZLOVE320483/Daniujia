package com.xiaojia.daniujia.domain.resp;

/**
 * Created by ZLOVE on 2016/6/8.
 */
public class ShowActRespVo {

    private String returncode;
    private String returnmsg;
    private int errcode;
    private String errmsg;
    private boolean showActivity;
    private String title;
    private String imgurl;
    private String url;

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

    public boolean isShowActivity() {
        return showActivity;
    }

    public void setShowActivity(boolean showActivity) {
        this.showActivity = showActivity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ShowActRespVo{" +
                "returncode='" + returncode + '\'' +
                ", returnmsg='" + returnmsg + '\'' +
                ", errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", showActivity=" + showActivity +
                ", title='" + title + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
