package com.xiaojia.daniujia.domain.resp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZLOVE on 2016/5/31.
 */
public class PayBalanceCouponVo {

    private String returncode;
    private String returnmsg;
    private int errcode;
    private String errmsg;
    private String balance;
    private List<CouponEntity> coupons;

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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<CouponEntity> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponEntity> coupons) {
        this.coupons = coupons;
    }

    public static class CouponEntity implements Serializable {
        private int couponId;
        private int ctype;
        private String title;
        private int value;
        private long deadtime;

        public int getCouponId() {
            return couponId;
        }

        public void setCouponId(int couponId) {
            this.couponId = couponId;
        }

        public int getCtype() {
            return ctype;
        }

        public void setCtype(int ctype) {
            this.ctype = ctype;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public long getDeadtime() {
            return deadtime;
        }

        public void setDeadtime(long deadtime) {
            this.deadtime = deadtime;
        }
    }

}
