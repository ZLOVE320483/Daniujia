package com.xiaojia.daniujia.domain.resp;

/**
 * Created by ZLOVE on 2016/6/6.
 */
public class OrderDetailRespVo {

    private String returncode;
    private String returnmsg;
    private int errcode;
    private String errmsg;
    private int orderId;
    private String orderNo;
    private long createTime;
    private int status;
    private int userId;
    private String imgurl;
    private String name;
    private String username;
    private int serviceType;
    private String servicePrice;
    private int serviceCnt;
    private String totalPrice;
    private String quesDesc;
    private String profile;
    private String comment;
    private String reply;
    private String rejectReason;
    private String company;
    private String position;
    private int score;
    private int consultantId;
    private String orderUser;
    private int duration;
    private Charity charity;

    public Charity getCharity() {
        return charity;
    }

    public void setCharity(Charity charity) {
        this.charity = charity;
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getServiceCnt() {
        return serviceCnt;
    }

    public void setServiceCnt(int serviceCnt) {
        this.serviceCnt = serviceCnt;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getQuesDesc() {
        return quesDesc;
    }

    public void setQuesDesc(String quesDesc) {
        this.quesDesc = quesDesc;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public int getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(int consultantId) {
        this.consultantId = consultantId;
    }

    public String getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(String orderUser) {
        this.orderUser = orderUser;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "OrderDetailRespVo{" +
                "returncode='" + returncode + '\'' +
                ", returnmsg='" + returnmsg + '\'' +
                ", errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", orderId=" + orderId +
                ", orderNo='" + orderNo + '\'' +
                ", createTime=" + createTime +
                ", status=" + status +
                ", userId=" + userId +
                ", imgurl='" + imgurl + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", serviceType=" + serviceType +
                ", servicePrice='" + servicePrice + '\'' +
                ", serviceCnt=" + serviceCnt +
                ", totalPrice='" + totalPrice + '\'' +
                ", quesDesc='" + quesDesc + '\'' +
                ", profile='" + profile + '\'' +
                ", comment='" + comment + '\'' +
                ", reply='" + reply + '\'' +
                ", rejectReason='" + rejectReason + '\'' +
                ", company='" + company + '\'' +
                ", position='" + position + '\'' +
                ", score=" + score +
                ", consultantId=" + consultantId +
                ", orderUser='" + orderUser + '\'' +
                '}';
    }
}
