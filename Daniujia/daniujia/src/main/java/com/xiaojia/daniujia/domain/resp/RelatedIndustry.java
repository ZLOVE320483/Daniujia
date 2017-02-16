package com.xiaojia.daniujia.domain.resp;

import java.io.Serializable;

/**
 * Created by ZLOVE on 2016/7/4.
 */
public class RelatedIndustry implements Serializable {
    private int industryId;
    private String industryName;

    public int getIndustryId() {
        return industryId;
    }

    public void setIndustryId(int industryId) {
        this.industryId = industryId;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getIndustryName() {
        return industryName;
    }
}