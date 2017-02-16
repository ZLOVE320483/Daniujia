package com.xiaojia.daniujia.domain.resp;

/**
 * Created by ZLOVE on 2016/12/20.
 */
public class DispatchCustomerResponseVo {

    private int eventId;
    private boolean ok;
    private ResponseData data;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public ResponseData getData() {
        return data;
    }

    public void setData(ResponseData data) {
        this.data = data;
    }

    public static class ResponseData {
        private int userId;
        private String username;
        private String realname;
        private int servicingCount;
        private String avatarUrl;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public int getServicingCount() {
            return servicingCount;
        }

        public void setServicingCount(int servicingCount) {
            this.servicingCount = servicingCount;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }
    }
}
