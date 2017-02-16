package com.xiaojia.daniujia.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/5.
 */
public class PublishRequireData implements Serializable {
    private String consultantName;//	string	true	顾问名称
    private String budget;//	string	true	需求的价格范围, 如: 5万, 5万左右
    private String desc	;//string	true	需求描述
    private String serviceDay;//	string	true	到场服务天数, 1天
    private String serviceCycle;//	string	true	服务周期, 有 “1年” , “一个月”, “三个月”, “六个月”
    private String writeName;//	string	true	发布者显示名称
    private String cityCode;//	string	true	城市code
    /**
     * 以下两个用于显示
     * 用于显示，不传入接口
     */
    private String cityName;
    private String deadTimeString;

    private long deadline;//	int	true	截至日期时间戳
    // 需求id，需要传递给后台
    private int requireId;

    public int getRequireId() {
        return requireId;
    }

    public void setRequireId(int requireId) {
        this.requireId = requireId;
    }

    public String getDeadTimeString() {
        return deadTimeString;
    }

    public void setDeadTimeString(String deadTimeString) {
        this.deadTimeString = deadTimeString;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
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

    public String getWriteName() {
        return writeName;
    }

    public void setWriteName(String writeName) {
        this.writeName = writeName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }
}
