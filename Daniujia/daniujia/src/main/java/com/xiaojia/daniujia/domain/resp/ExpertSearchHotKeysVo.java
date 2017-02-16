package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by ZLOVE on 2016/5/6.
 */
public class ExpertSearchHotKeysVo {

    private List<HotKeyInfo> keywords;
    private String returncode;
    private String returnmsg;

    public void setKeywords(List<HotKeyInfo> keywords) {
        this.keywords = keywords;
    }

    public void setReturncode(String returncode) {
        this.returncode = returncode;
    }

    public void setReturnmsg(String returnmsg) {
        this.returnmsg = returnmsg;
    }

    public List<HotKeyInfo> getKeywords() {
        return keywords;
    }

    public String getReturncode() {
        return returncode;
    }

    public String getReturnmsg() {
        return returnmsg;
    }

    public static class HotKeyInfo {

        private String keyword;

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getKeyword() {
            return keyword;
        }
    }
}
