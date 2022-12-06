package com.dunvant.application.beans;

import java.io.Serializable;

/**
 * Created by DEVLABS\grathour on 19/1/18.
 */
public class ScoreDatesCoachBean implements Serializable {
    String sessiondate;

    public String getSessiondate() {
        return sessiondate;
    }

    public void setSessiondate(String sessiondate) {
        this.sessiondate = sessiondate;
    }
}
