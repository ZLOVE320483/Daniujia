package com.xiaojia.daniujia.domain.resp;

/**
 * Created by ZLOVE on 2016/6/29.
 */
public class SelectCityBean implements IAlphabetSection {

    private int sectionType;
    private String sectionText;
    private String cityCode;

    @Override
    public int getSectionType() {
        return sectionType;
    }

    @Override
    public void setSectionType(int type) {
        this.sectionType = type;
    }

    @Override
    public String getSectionText() {
        return sectionText;
    }

    @Override
    public void setSectionText(String sectionText) {
        this.sectionText = sectionText;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
}
