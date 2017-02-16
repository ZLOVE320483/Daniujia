package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by Administrator on 2016/5/27.
 */
public class MyAppointExpert {

    private String returncode;
    private String returnmsg;
    private int errcode;
    private String errmsg;
    private int pageNum;
    private int perPageNum;
    private int totalNum;
    private boolean flagOneAllRead;
    private boolean flagTwoAllRead;
    private boolean flagThreeAllRead;
    private List<OrdersEntity> orders;

    public boolean isFlagOneAllRead() {
        return flagOneAllRead;
    }

    public void setFlagOneAllRead(boolean flagOneAllRead) {
        this.flagOneAllRead = flagOneAllRead;
    }

    public boolean isFlagTwoAllRead() {
        return flagTwoAllRead;
    }

    public void setFlagTwoAllRead(boolean flagTwoAllRead) {
        this.flagTwoAllRead = flagTwoAllRead;
    }

    public boolean isFlagThreeAllRead() {
        return flagThreeAllRead;
    }

    public void setFlagThreeAllRead(boolean flagThreeAllRead) {
        this.flagThreeAllRead = flagThreeAllRead;
    }

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

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPerPageNum() {
        return perPageNum;
    }

    public void setPerPageNum(int perPageNum) {
        this.perPageNum = perPageNum;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<OrdersEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersEntity> orders) {
        this.orders = orders;
    }

    public static class OrdersEntity {
        private int orderId;
        private int consultantId;
        private int userId;
        private String imgurl;
        private String name;
        private String username;
        private String position;
        private String company;
        private int serviceType;
        private String servicePrice;
        private String totalPrice;
        private int serviceCnt;
        private int status;
        private int duration;
        private boolean isRead;

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getConsultantId() {
            return consultantId;
        }

        public void setConsultantId(int consultantId) {
            this.consultantId = consultantId;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public int getServiceType() {
            return serviceType;
        }

        public void setServiceType(int serviceType) {
            this.serviceType = serviceType;
        }

        public String getServicePrice() {
            return servicePrice;
        }

        public void setServicePrice(String servicePrice) {
            this.servicePrice = servicePrice;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public int getServiceCnt() {
            return serviceCnt;
        }

        public void setServiceCnt(int serviceCnt) {
            this.serviceCnt = serviceCnt;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

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

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getDuration() {
            return duration;
        }

        public boolean isRead() {
            return isRead;
        }

        public void setIsRead(boolean isRead) {
            this.isRead = isRead;
        }
    }

    @Override
    public String toString() {
        return "MyAppointExpert{" +
                "returncode='" + returncode + '\'' +
                ", returnmsg='" + returnmsg + '\'' +
                ", errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", pageNum=" + pageNum +
                ", perPageNum=" + perPageNum +
                ", totalNum=" + totalNum +
                ", orders=" + orders +
                '}';
    }
}
