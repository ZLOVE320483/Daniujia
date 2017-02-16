package com.xiaojia.daniujia.domain.resp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZLOVE on 2016/7/4.
 */
public class Speciality implements Serializable {

    private int specId;
    private String mainIconUrl;
    private String mainEditIconUrl;
    private String mainName;
    private int subId;
    private String subName;
    private String writeName;
    private int workage;
    private List<RelatedIndustry> relatedIndustrys;
    private List<ConcreteDirection> concreteDirections;

    public int getSpecId() {
        return specId;
    }

    public void setSpecId(int specId) {
        this.specId = specId;
    }

    public String getMainIconUrl() {
        return mainIconUrl;
    }

    public void setMainIconUrl(String mainIconUrl) {
        this.mainIconUrl = mainIconUrl;
    }

    public String getMainName() {
        return mainName;
    }

    public void setMainName(String mainName) {
        this.mainName = mainName;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getWriteName() {
        return writeName;
    }

    public void setWriteName(String writeName) {
        this.writeName = writeName;
    }

    public int getWorkage() {
        return workage;
    }

    public void setWorkage(int workage) {
        this.workage = workage;
    }

    public List<RelatedIndustry> getRelatedIndustrys() {
        return relatedIndustrys;
    }

    public void setRelatedIndustrys(List<RelatedIndustry> relatedIndustrys) {
        this.relatedIndustrys = relatedIndustrys;
    }

    public List<ConcreteDirection> getConcreteDirections() {
        return concreteDirections;
    }

    public void setConcreteDirections(List<ConcreteDirection> concreteDirections) {
        this.concreteDirections = concreteDirections;
    }

    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId = subId;
    }

    public String getMainEditIconUrl() {
        return mainEditIconUrl;
    }

    public void setMainEditIconUrl(String mainEditIconUrl) {
        this.mainEditIconUrl = mainEditIconUrl;
    }
}