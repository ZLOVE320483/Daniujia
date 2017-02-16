package com.xiaojia.daniujia.domain.resp;

/**
 * Created by Administrator on 2016/8/9.
 */
public class QRCodeVo {
    private String returncode;//	是	string	SUCCESS/FAIL
    private String returnmsg;//	是	string	接口返回信息
    private String qrCode;//	是	string	base64图片

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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
