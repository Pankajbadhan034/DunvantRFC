package com.dunvant.application.beans;

import java.io.Serializable;

public class CountryBean implements Serializable{

    private String id;
    private String country;
//    private String countryCode;
    private String dialingCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /*public String getCountryCode() {
        return countryCode;
    }*/

    /*public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }*/

    public String getDialingCode() {
        return dialingCode;
    }

    public void setDialingCode(String dialingCode) {
        this.dialingCode = dialingCode;
    }
}