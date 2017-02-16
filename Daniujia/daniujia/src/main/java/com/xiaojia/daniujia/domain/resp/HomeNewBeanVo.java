package com.xiaojia.daniujia.domain.resp;

/**
 * Created by Administrator on 2016/10/24.
 */
public class HomeNewBeanVo {
    private String returncode;//"returncode": "SUCCESS",
    private String returnmsg;//      "returnmsg": "数据返回成功"

    private HomeEntity item;

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

    public HomeEntity getItem() {
        return item;
    }

    public void setItem(HomeEntity item) {
        this.item = item;
    }
}
