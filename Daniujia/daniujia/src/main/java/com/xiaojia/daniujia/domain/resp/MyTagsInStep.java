package com.xiaojia.daniujia.domain.resp;
import java.util.List;

/**
 * Created by Administrator on 2016/6/16.
 */
public class MyTagsInStep {
    private List<UserLabelVo.MyTagInfo> myTags;

    public List<UserLabelVo.MyTagInfo> getMyTags() {
        return myTags;
    }

    public void setMyTags(List<UserLabelVo.MyTagInfo> myTags) {
        this.myTags = myTags;
    }
}
