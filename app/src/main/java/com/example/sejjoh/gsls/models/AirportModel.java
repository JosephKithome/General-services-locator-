package com.example.sejjoh.gsls.models;

public class AirportModel {
    String aiport;
    String image;
    String description;
    String checkin;
    String checkout;
    String website;

    public AirportModel() {
    }

    public AirportModel(String aiport, String image, String description, String checkin, String checkout, String website) {
        this.aiport = aiport;
        this.image = image;
        this.description = description;
        this.checkin = checkin;
        this.checkout = checkout;
        this.website = website;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAiport() {
        return aiport;
    }

    public void setAiport(String aiport) {
        this.aiport = aiport;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
