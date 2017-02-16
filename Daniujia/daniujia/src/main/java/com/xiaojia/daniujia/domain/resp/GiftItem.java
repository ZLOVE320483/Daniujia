package com.xiaojia.daniujia.domain.resp;

/**
 * Created by ZLOVE on 2016/6/15.
 */
public class GiftItem {
    public int couponId;
    public int ctype;
    public String title;
    public int value;
    public int status;
    public long deadtime;
    public int usetime;
    public int createtime;

    @Override
    public String toString() {
        return "GiftItem{" +
                "couponId=" + couponId +
                ", ctype=" + ctype +
                ", title='" + title + '\'' +
                ", value=" + value +
                ", status=" + status +
                ", deadtime=" + deadtime +
                ", usetime=" + usetime +
                ", createtime=" + createtime +
                '}';
    }
}
