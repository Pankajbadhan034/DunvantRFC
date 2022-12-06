package com.dunvant.application.child.smartBand.bean;

import java.io.Serializable;

/**
 * Created by DEVLABS\pbadhan on 29/6/17.
 */
public class ChildSmartBandLevelsBean implements Serializable {
    String id;
    String name;
    int startVal;
    int endVal;
    int levelType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartVal() {
        return startVal;
    }

    public void setStartVal(int startVal) {
        this.startVal = startVal;
    }

    public int getEndVal() {
        return endVal;
    }

    public void setEndVal(int endVal) {
        this.endVal = endVal;
    }

    public int getLevelType() {
        return levelType;
    }

    public void setLevelType(int levelType) {
        this.levelType = levelType;
    }
}
