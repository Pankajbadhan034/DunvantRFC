package com.dunvant.application.beans;

import com.dunvant.application.Chapter;

import java.io.Serializable;
import java.util.ArrayList;

public class RecycleExpBean implements Serializable {
    String childName;
    String used_credit;
    String total_credit;
    String attendanceDate;
    ArrayList<Chapter> chapters;
   int id;

    public String getUsed_credit() {
        return used_credit;
    }

    public void setUsed_credit(String used_credit) {
        this.used_credit = used_credit;
    }

    public String getTotal_credit() {
        return total_credit;
    }

    public void setTotal_credit(String total_credit) {
        this.total_credit = total_credit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }
}
