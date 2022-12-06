package com.dunvant.application.beans;

import java.io.Serializable;

public class CovidViewReportBean implements Serializable {
    String question;
    String answer;


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
