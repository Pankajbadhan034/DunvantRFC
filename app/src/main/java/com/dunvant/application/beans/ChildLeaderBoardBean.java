package com.dunvant.application.beans;

import java.io.Serializable;

/**
 * Created by DEVLABS\pbadhan on 24/8/17.
 */
public class ChildLeaderBoardBean implements Serializable {
    String challengeId;
    String child_id;
    String child_name;
    String child_dp_url;
    String scores;
    String time_taken;

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


    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public String getChild_id() {
        return child_id;
    }

    public void setChild_id(String child_id) {
        this.child_id = child_id;
    }

    public String getChild_name() {
        return child_name;
    }

    public void setChild_name(String child_name) {
        this.child_name = child_name;
    }

    public String getChild_dp_url() {
        return child_dp_url;
    }

    public void setChild_dp_url(String child_dp_url) {
        this.child_dp_url = child_dp_url;
    }

    public String getScores() {
        return scores;
    }

    public void setScores(String scores) {
        this.scores = scores;
    }

    public String getTime_taken() {
        return time_taken;
    }

    public void setTime_taken(String time_taken) {
        this.time_taken = time_taken;
    }
}
