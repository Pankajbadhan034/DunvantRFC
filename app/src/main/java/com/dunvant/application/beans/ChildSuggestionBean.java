package com.dunvant.application.beans;

import java.io.Serializable;

/**
 * Created by DEVLABS\pbadhan on 10/5/17.
 */
public class ChildSuggestionBean implements Serializable {
    String userId;
    String fullName;
    String profilePicUrl;
    String ageValue;
    String favouritePlayer;
    String favouritePosition;
    String favouriteTeam;
    String canSendRequest;
    String isPrivate;

    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getAgeValue() {
        return ageValue;
    }

    public void setAgeValue(String ageValue) {
        this.ageValue = ageValue;
    }

    public String getFavouritePlayer() {
        return favouritePlayer;
    }

    public void setFavouritePlayer(String favouritePlayer) {
        this.favouritePlayer = favouritePlayer;
    }

    public String getFavouritePosition() {
        return favouritePosition;
    }

    public void setFavouritePosition(String favouritePosition) {
        this.favouritePosition = favouritePosition;
    }

    public String getFavouriteTeam() {
        return favouriteTeam;
    }

    public void setFavouriteTeam(String favouriteTeam) {
        this.favouriteTeam = favouriteTeam;
    }

    public String getCanSendRequest() {
        return canSendRequest;
    }

    public void setCanSendRequest(String canSendRequest) {
        this.canSendRequest = canSendRequest;
    }
}
