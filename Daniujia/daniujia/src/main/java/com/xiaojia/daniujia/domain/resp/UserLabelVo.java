package com.xiaojia.daniujia.domain.resp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZLOVE on 2016/5/10.
 */
public class UserLabelVo {
    private String returncode;
    private String returnmsg;
    private int errcode;
    private String errmsg;
    private List<MyTagInfo> myTags;
    private List<CommonTagInfo> commonTags;

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

    public List<MyTagInfo> getMyTags() {
        return myTags;
    }

    public void setMyTags(List<MyTagInfo> myTags) {
        this.myTags = myTags;
    }

    public List<CommonTagInfo> getCommonTags() {
        return commonTags;
    }

    public void setCommonTags(List<CommonTagInfo> commonTags) {
        this.commonTags = commonTags;
    }

    public static class MyTagInfo implements Serializable {

        private int tagId;
        private String name;

        public int getTagId() {
            return tagId;
        }

        public void setTagId(int tagId) {
            this.tagId = tagId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class CommonTagInfo {

        private int tagId;
        private String name;

        public int getTagId() {
            return tagId;
        }

        public void setTagId(int tagId) {
            this.tagId = tagId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
