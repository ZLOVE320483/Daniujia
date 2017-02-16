package com.xiaojia.daniujia.domain.resp;

/**
 * Created by Administrator on 2016/8/23.
 */
public class Charity {
    private String charityExpert;//	是	string	参加慈善公益活动
    private String share;	//是	string	专家捐助比较

    public String getCharityExpert() {
        return charityExpert;
    }

    public void setCharityExpert(String charityExpert) {
        this.charityExpert = charityExpert;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }
}
