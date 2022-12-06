package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class ChildBean implements Serializable{

    private String id;
    private String academiesId;
    private String username;
    private String email;
    private String gender;
    private String createdAt;
    private String state;
    private String firstName;
    private String lastName;
    private String fullName;
    private String age;
    private int ageValue;
    private String dateOfBirth;
    private String medicalCondition;
    private String school;
    private String favPlayer;
    private String favTeam;
    private String favPosition;
    private String favFootballBoot;
    private String favFood;
    private String nationality;
    private String height;
    private String weight;
    private boolean selected;
    private String registrationFee;
    private String tournamentFee;
    private String isPrivate;
    private String genderValue;

    private String profilePicture;
    private String profilePicUrl;
    private String favPlayerPicture;
    private String favTeamPicture;
    private String genderValueNumber;
    private String dateOfBirthFormatted;

    private String badgeCount;
    private String childClass;
    private String fieldClub;

    private String field1;
    private String field2;
    private String field3;
    private String field4;
    private String field5;

    private String childEmail;

    public String getChildEmail() {
        return childEmail;
    }

    public void setChildEmail(String childEmail) {
        this.childEmail = childEmail;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public String getField4() {
        return field4;
    }

    public void setField4(String field4) {
        this.field4 = field4;
    }

    public String getField5() {
        return field5;
    }

    public void setField5(String field5) {
        this.field5 = field5;
    }

    public String getFieldClub() {
        return fieldClub;
    }

    public void setFieldClub(String fieldClub) {
        this.fieldClub = fieldClub;
    }

    public String getChildClass() {
        return childClass;
    }

    public void setChildClass(String childClass) {
        this.childClass = childClass;
    }

    public String getBadgeCount() {
        return badgeCount;
    }

    public void setBadgeCount(String badgeCount) {
        this.badgeCount = badgeCount;
    }

    public String getGenderValueNumber() {
        return genderValueNumber;
    }

    public void setGenderValueNumber(String genderValueNumber) {
        this.genderValueNumber = genderValueNumber;
    }

    public String getDateOfBirthFormatted() {
        return dateOfBirthFormatted;
    }

    public void setDateOfBirthFormatted(String dateOfBirthFormatted) {
        this.dateOfBirthFormatted = dateOfBirthFormatted;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getFavPlayerPicture() {
        return favPlayerPicture;
    }

    public void setFavPlayerPicture(String favPlayerPicture) {
        this.favPlayerPicture = favPlayerPicture;
    }

    public String getFavTeamPicture() {
        return favTeamPicture;
    }

    public void setFavTeamPicture(String favTeamPicture) {
        this.favTeamPicture = favTeamPicture;
    }

    private ArrayList<ExtraFeesBean> extraFeesList = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAcademiesId() {
        return academiesId;
    }

    public void setAcademiesId(String academiesId) {
        this.academiesId = academiesId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMedicalCondition() {
        return medicalCondition;
    }

    public void setMedicalCondition(String medicalCondition) {
        this.medicalCondition = medicalCondition;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(String registrationFee) {
        this.registrationFee = registrationFee;
    }

    public String getTournamentFee() {
        return tournamentFee;
    }

    public void setTournamentFee(String tournamentFee) {
        this.tournamentFee = tournamentFee;
    }

    public int getAgeValue() {
        return ageValue;
    }

    public void setAgeValue(int ageValue) {
        this.ageValue = ageValue;
    }

    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getFavPlayer() {
        return favPlayer;
    }

    public void setFavPlayer(String favPlayer) {
        this.favPlayer = favPlayer;
    }

    public String getFavTeam() {
        return favTeam;
    }

    public void setFavTeam(String favTeam) {
        this.favTeam = favTeam;
    }

    public String getFavPosition() {
        return favPosition;
    }

    public void setFavPosition(String favPosition) {
        this.favPosition = favPosition;
    }

    public String getFavFootballBoot() {
        return favFootballBoot;
    }

    public void setFavFootballBoot(String favFootballBoot) {
        this.favFootballBoot = favFootballBoot;
    }

    public String getFavFood() {
        return favFood;
    }

    public void setFavFood(String favFood) {
        this.favFood = favFood;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
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

    public ArrayList<ExtraFeesBean> getExtraFeesList() {
        return extraFeesList;
    }

    public void setExtraFeesList(ArrayList<ExtraFeesBean> extraFeesList) {
        this.extraFeesList = extraFeesList;
    }

    public String getGenderValue() {
        return genderValue;
    }

    public void setGenderValue(String genderValue) {
        this.genderValue = genderValue;
    }
}