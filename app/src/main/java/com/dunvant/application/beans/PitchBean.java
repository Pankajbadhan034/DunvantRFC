package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class PitchBean implements Serializable{
    private String parentId;

    private String pitchId;
    private String pitchName;
//    private String pricePerHour;
//    private ArrayList<Double> pricePerHourDailyBasis = new ArrayList<>();

    private String academiesId;
    private String fromDateFormatted;
    private String toDateFormatted;
    private String fromDate;
    private String toDate;
    private String isHalfHour;
    private String leagueState;
    private String locationsId;
    private String locationName;
    private String locationDescription;
    private String fileTitle;
    private String filePath;
    private String noPitch;
    private String pitchDurationLabel;

    private ArrayList<PitchAvailabilityBean> pitchAvailabilityListing;
    private ArrayList<PitchExcludedDatesBean> pitchExcludedDatesBeanListing;
    private ArrayList<PitchBookedDataBean> pitchBookedDataBeanListing;

    private ArrayList<PitchTypeBean> simplePitchList;
    private ArrayList<PitchTypeBean> fullPitchList;
    private ArrayList<PitchTypeBean> specialPriceList;

    /*private PitchTypeBean simplePitch;
    private PitchTypeBean fullPitch;*/

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPitchId() {
        return pitchId;
    }

    public void setPitchId(String pitchId) {
        this.pitchId = pitchId;
    }

    public String getPitchName() {
        return pitchName;
    }

    public void setPitchName(String pitchName) {
        this.pitchName = pitchName;
    }

    public String getAcademiesId() {
        return academiesId;
    }

    public void setAcademiesId(String academiesId) {
        this.academiesId = academiesId;
    }

    public String getFromDateFormatted() {
        return fromDateFormatted;
    }

    public void setFromDateFormatted(String fromDateFormatted) {
        this.fromDateFormatted = fromDateFormatted;
    }

    public String getToDateFormatted() {
        return toDateFormatted;
    }

    public void setToDateFormatted(String toDateFormatted) {
        this.toDateFormatted = toDateFormatted;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getIsHalfHour() {
        return isHalfHour;
    }

    public void setIsHalfHour(String isHalfHour) {
        this.isHalfHour = isHalfHour;
    }

    public String getLeagueState() {
        return leagueState;
    }

    public void setLeagueState(String leagueState) {
        this.leagueState = leagueState;
    }

    public String getLocationsId() {
        return locationsId;
    }

    public void setLocationsId(String locationsId) {
        this.locationsId = locationsId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getNoPitch() {
        return noPitch;
    }

    public void setNoPitch(String noPitch) {
        this.noPitch = noPitch;
    }

    public ArrayList<PitchAvailabilityBean> getPitchAvailabilityListing() {
        return pitchAvailabilityListing;
    }

    public void setPitchAvailabilityListing(ArrayList<PitchAvailabilityBean> pitchAvailabilityListing) {
        this.pitchAvailabilityListing = pitchAvailabilityListing;
    }

    public ArrayList<PitchExcludedDatesBean> getPitchExcludedDatesBeanListing() {
        return pitchExcludedDatesBeanListing;
    }

    public void setPitchExcludedDatesBeanListing(ArrayList<PitchExcludedDatesBean> pitchExcludedDatesBeanListing) {
        this.pitchExcludedDatesBeanListing = pitchExcludedDatesBeanListing;
    }

    public ArrayList<PitchBookedDataBean> getPitchBookedDataBeanListing() {
        return pitchBookedDataBeanListing;
    }

    public void setPitchBookedDataBeanListing(ArrayList<PitchBookedDataBean> pitchBookedDataBeanListing) {
        this.pitchBookedDataBeanListing = pitchBookedDataBeanListing;
    }

    public ArrayList<PitchTypeBean> getSimplePitchList() {
        return simplePitchList;
    }

    public void setSimplePitchList(ArrayList<PitchTypeBean> simplePitchList) {
        this.simplePitchList = simplePitchList;
    }

    public ArrayList<PitchTypeBean> getFullPitchList() {
        return fullPitchList;
    }

    public void setFullPitchList(ArrayList<PitchTypeBean> fullPitchList) {
        this.fullPitchList = fullPitchList;
    }

    public ArrayList<PitchTypeBean> getSpecialPriceList() {
        return specialPriceList;
    }

    public void setSpecialPriceList(ArrayList<PitchTypeBean> specialPriceList) {
        this.specialPriceList = specialPriceList;
    }

    public String getPitchDurationLabel() {
        return pitchDurationLabel;
    }

    public void setPitchDurationLabel(String pitchDurationLabel) {
        this.pitchDurationLabel = pitchDurationLabel;
    }

    /*public PitchTypeBean getSimplePitch() {
        return simplePitch;
    }

    public void setSimplePitch(PitchTypeBean simplePitch) {
        this.simplePitch = simplePitch;
    }

    public PitchTypeBean getFullPitch() {
        return fullPitch;
    }

    public void setFullPitch(PitchTypeBean fullPitch) {
        this.fullPitch = fullPitch;
    }*/

    /*public String getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(String pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public ArrayList<Double> getPricePerHourDailyBasis() {
        return pricePerHourDailyBasis;
    }

    public void setPricePerHourDailyBasis(ArrayList<Double> pricePerHourDailyBasis) {
        this.pricePerHourDailyBasis = pricePerHourDailyBasis;
    }*/
}