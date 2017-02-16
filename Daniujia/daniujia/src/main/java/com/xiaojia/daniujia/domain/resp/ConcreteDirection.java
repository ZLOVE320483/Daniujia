package com.xiaojia.daniujia.domain.resp;

import java.io.Serializable;

/**
 * Created by ZLOVE on 2016/7/4.
 */
public class ConcreteDirection implements Serializable {
    private int directionId;
    private String directionName;

    public int getDirectionId() {
        return directionId;
    }

    public void setDirectionId(int directionId) {
        this.directionId = directionId;
    }

    public String getDirectionName() {
        return directionName;
    }

    public void setDirectionName(String directionName) {
        this.directionName = directionName;
    }
}
