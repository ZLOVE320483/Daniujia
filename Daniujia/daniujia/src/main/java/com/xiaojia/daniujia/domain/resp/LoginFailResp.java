package com.xiaojia.daniujia.domain.resp;

/**
 * Created by ZLOVE on 2016/11/3.
 */
public class LoginFailResp {

    private int errcode;
    private String errmsg;
    private String returncode;
    private String returnmsg;
    private Extra extra;

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

    public Extra getExtra() {
        return extra;
    }

    public void setExtra(Extra extra) {
        this.extra = extra;
    }

    public static class Extra {

        private int failLoginTimes;
        private String imageData;
        private boolean lockedToday;
        private boolean needCaptcha = false;

        public int getFailLoginTimes() {
            return failLoginTimes;
        }

        public void setFailLoginTimes(int failLoginTimes) {
            this.failLoginTimes = failLoginTimes;
        }

        public String getImageData() {
            return imageData;
        }

        public void setImageData(String imageData) {
            this.imageData = imageData;
        }

        public boolean isLockedToday() {
            return lockedToday;
        }

        public void setLockedToday(boolean lockedToday) {
            this.lockedToday = lockedToday;
        }

        public boolean isNeedCaptcha() {
            return needCaptcha;
        }

        public void setNeedCaptcha(boolean needCaptcha) {
            this.needCaptcha = needCaptcha;
        }
    }

}
