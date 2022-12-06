package com.dunvant.application.beans;

import java.io.Serializable;

/**
 * Created by DEVLABS\pbadhan on 11/1/17.
 */
public class ChildViewYourProfileBean implements Serializable {
    String id;
    String roleId;
    String rolName;
    String roleCode;
    String fullName;
    String title;
    String profilePicture;
    String profilePicturePath;
    String dob;
    String dobFormatted;
    String gender;
    String favoritePlayer;
    String favoriteTeam;
    String favoritePosition;
    String height;
    String weight;
    String heightFormatted;
    String weightFormatted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRolName() {
        return rolName;
    }

    public void setRolName(String rolName) {
        this.rolName = rolName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDobFormatted() {
        return dobFormatted;
    }

    public void setDobFormatted(String dobFormatted) {
        this.dobFormatted = dobFormatted;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFavoritePlayer() {
        return favoritePlayer;
    }

    public void setFavoritePlayer(String favoritePlayer) {
        this.favoritePlayer = favoritePlayer;
    }

    public String getFavoriteTeam() {
        return favoriteTeam;
    }

    public void setFavoriteTeam(String favoriteTeam) {
        this.favoriteTeam = favoriteTeam;
    }

    public String getFavoritePosition() {
        return favoritePosition;
    }

    public void setFavoritePosition(String favoritePosition) {
        this.favoritePosition = favoritePosition;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeightFormatted() {
        return heightFormatted;
    }

    public void setHeightFormatted(String heightFormatted) {
        this.heightFormatted = heightFormatted;
    }

    public String getWeightFormatted() {
        return weightFormatted;
    }

    public void setWeightFormatted(String weightFormatted) {
        this.weightFormatted = weightFormatted;
    }
}
