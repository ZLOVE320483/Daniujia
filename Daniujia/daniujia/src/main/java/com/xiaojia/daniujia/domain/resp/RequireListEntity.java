package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */
public class RequireListEntity {
    public static final String UNAUDITED = "unaudited";	//待审核 创建需求后, 即为待审核
    public static final String SEARCHING = "searching";	//寻找中 用户支付成功后, 其他用户可见 (管理后台审核通过后即为寻找中)
    public static final String CONFIREMED = "confirmed";	//已确认 发布后确认全部专家后自动转换为已确认
    public static final String CLOSED = "closed";	//已关闭 关闭操作由管理后台操作, 关闭后用户不可该需求进行操作
    private int demandId;//	int	需求id
    private String consultantName;//	string	寻找的顾问名称
    private String cityName;//string	城市名称
    private String budget;//string	需求的价格范围
    private String desc;//string	需求描述
    private String serviceDay;//	string	现场服务天数
    private String serviceCycle;//	string	服务周期, 有 “1年” , “一个月”, “三个月”, “六个月”
    private String feeStatsu;//string	金额状态, 已支付, 已退回
    private float fee;//float	预支付金额 全部被确认则为已支付, 否则仍为已托管
    private String deadline;//string	有效日期
    private String status;//	string	需求状态
    private boolean statusChanged = false;//	bool	状态变化, 前端显示小红点, 默认为false
    private int viewCount;//	int	浏览数, 默认为0
    private int applicantCount;//	int	专家报名数, 默认为0
    private String writeName;//	string	发布者显示名称
    private boolean applicantChanged = false;//	bool	报名人数增加, 前端显示小红点, 默认为false
    private boolean certified;//	bool	发布者是否被认证

    public boolean isCertified() {
        return certified;
    }

    public void setCertified(boolean certified) {
        this.certified = certified;
    }

    public boolean isApplicantChanged() {
        return applicantChanged;
    }

    public void setApplicantChanged(boolean applicantChanged) {
        this.applicantChanged = applicantChanged;
    }

    public String getWriteName() {
        return writeName;
    }

    public void setWriteName(String writeName) {
        this.writeName = writeName;
    }

    private List<ConfirmExpert> confirmedExperts;    //array	已确认专家列表 [demand-list-expert]

    public int getDemandId() {
        return demandId;
    }

    public void setDemandId(int demandId) {
        this.demandId = demandId;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getServiceDay() {
        return serviceDay;
    }

    public void setServiceDay(String serviceDay) {
        this.serviceDay = serviceDay;
    }

    public String getServiceCycle() {
        return serviceCycle;
    }

    public void setServiceCycle(String serviceCycle) {
        this.serviceCycle = serviceCycle;
    }

    public String getFeeStatsu() {
        return feeStatsu;
    }

    public void setFeeStatsu(String feeStatsu) {
        this.feeStatsu = feeStatsu;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isStatusChanged() {
        return statusChanged;
    }

    public void setStatusChanged(boolean statusChanged) {
        this.statusChanged = statusChanged;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getApplicantCount() {
        return applicantCount;
    }

    public void setApplicantCount(int applicantCount) {
        this.applicantCount = applicantCount;
    }

    public List<ConfirmExpert> getConfirmedExperts() {
        return confirmedExperts;
    }

    public void setConfirmedExperts(List<ConfirmExpert> confirmedExperts) {
        this.confirmedExperts = confirmedExperts;
    }

    public static class ConfirmExpert {
        private int id;//int	专家用户id
        private String imgUrl;//	string	专家头像

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }

}
