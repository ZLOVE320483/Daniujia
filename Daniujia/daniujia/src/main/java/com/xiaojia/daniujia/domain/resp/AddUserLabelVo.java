package com.xiaojia.daniujia.domain.resp;

/**
 * Created by ZLOVE on 2016/5/10.
 */
public class AddUserLabelVo {

    private String returncode;
    private String returnmsg;
    private int errcode;
    private String errmsg;
    private TagInfo tag;

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

    public TagInfo getTag() {
        return tag;
    }

    public void setTag(TagInfo tag) {
        this.tag = tag;
    }

    public static class TagInfo {

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
