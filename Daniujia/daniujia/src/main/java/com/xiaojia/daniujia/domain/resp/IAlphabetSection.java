package com.xiaojia.daniujia.domain.resp;

/**
 * Created by ZLOVE on 2016/6/29.
 */
public interface IAlphabetSection {

    public static final int ITEM = 0;
    public static final int SECTION = 1;

    public int getSectionType();
    public void setSectionType(int type);

    public String getSectionText();
    public void setSectionText(String sectionText);
}
