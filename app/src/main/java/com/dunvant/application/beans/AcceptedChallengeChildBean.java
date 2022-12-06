package com.dunvant.application.beans;

import java.io.Serializable;

public class AcceptedChallengeChildBean implements Serializable{

    private String challengersId;
    private String childId;
    private String childName;
    private String showCreatedAt;
    private String showAcceptedDate;
    private String showAcceptedTime;
    private String childDpUrl;
    private String scores;
    private String timeTaken;
    private String approvalStatus;
    private String achievedScore;
    private String achievedTime;
    private String challengeMedia;
    private String videoThumbnail;

    public String getVideoThumbnail() {
        return videoThumbnail;
    }
    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }



    public String getChallengeMedia() {
        return challengeMedia;
    }
    public void setChallengersMedia(String challengeMedia) {
        this.challengeMedia = challengeMedia;
    }


    public String getChallengersId() {
        return challengersId;
    }

    public void setChallengersId(String challengersId) {
        this.challengersId = challengersId;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getShowCreatedAt() {
        return showCreatedAt;
    }

    public void setShowCreatedAt(String showCreatedAt) {
        this.showCreatedAt = showCreatedAt;
    }

    public String getShowAcceptedDate() {
        return showAcceptedDate;
    }

    public void setShowAcceptedDate(String showAcceptedDate) {
        this.showAcceptedDate = showAcceptedDate;
    }

    public String getShowAcceptedTime() {
        return showAcceptedTime;
    }

    public void setShowAcceptedTime(String showAcceptedTime) {
        this.showAcceptedTime = showAcceptedTime;
    }

    public String getChildDpUrl() {
        return childDpUrl;
    }

    public void setChildDpUrl(String childDpUrl) {
        this.childDpUrl = childDpUrl;
    }

    public String getScores() {
        return scores;
    }

    public void setScores(String scores) {
        this.scores = scores;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getAchievedScore() {
        return achievedScore;
    }

    public void setAchievedScore(String achievedScore) {
        this.achievedScore = achievedScore;
    }

    public String getAchievedTime() {
        return achievedTime;
    }

    public void setAchievedTime(String achievedTime) {
        this.achievedTime = achievedTime;
    }
}