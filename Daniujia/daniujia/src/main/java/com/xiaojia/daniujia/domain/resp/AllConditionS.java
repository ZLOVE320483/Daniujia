package com.xiaojia.daniujia.domain.resp;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/16.
 */
public class AllConditionS {
    private int superId;
    // 父类和子类字段其实是一样的
    private ArrayList<AllConditionF> conditionSon = new ArrayList<>();

    public int getSuperId() {
        return superId;
    }

    public void setSuperId(int superId) {
        this.superId = superId;
    }

    public ArrayList<AllConditionF> getConditionSon() {
        return conditionSon;
    }

    public void setConditionSon(ArrayList<AllConditionF> conditionSon) {
        this.conditionSon = conditionSon;
    }

}
