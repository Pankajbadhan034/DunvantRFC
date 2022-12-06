package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderSessionBeanNew implements Serializable{

    private ChildrenInfoBean childrenInfoBean;
    private SessionInfoBean sessionInfoBean;
    private ArrayList<BookingDateBean> bookingDatesList;

    public ChildrenInfoBean getChildrenInfoBean() {
        return childrenInfoBean;
    }

    public void setChildrenInfoBean(ChildrenInfoBean childrenInfoBean) {
        this.childrenInfoBean = childrenInfoBean;
    }

    public SessionInfoBean getSessionInfoBean() {
        return sessionInfoBean;
    }

    public void setSessionInfoBean(SessionInfoBean sessionInfoBean) {
        this.sessionInfoBean = sessionInfoBean;
    }

    public ArrayList<BookingDateBean> getBookingDatesList() {
        return bookingDatesList;
    }

    public void setBookingDatesList(ArrayList<BookingDateBean> bookingDatesList) {
        this.bookingDatesList = bookingDatesList;
    }
}