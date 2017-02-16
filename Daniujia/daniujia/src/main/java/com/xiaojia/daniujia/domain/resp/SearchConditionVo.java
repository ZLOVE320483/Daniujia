package com.xiaojia.daniujia.domain.resp;

/**
 * Created by Administrator on 2016/12/15.
 */
public class SearchConditionVo {
    private String returncode;//: "SUCCESS",
    private String returnmsg;// : "数据返回成功"
    private SearchDataEntity item;

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

    public SearchDataEntity getItem() {
        return item;
    }

    public void setItem(SearchDataEntity item) {
        this.item = item;
    }



}
