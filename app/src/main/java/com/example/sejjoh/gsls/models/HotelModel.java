package com.example.sejjoh.gsls.models;

public class HotelModel {
    String hotelName;
    String description;
     String image;
    String location;
     String website;
     String checkin;
     String checkout;
    String amenities;



    public HotelModel() {
    }

    public HotelModel(String hotelName, String description, String image, String location, String website, String checkin, String checkout, String amenities) {
        this.hotelName = hotelName;
        this.description = description;
        this.image = image;
        this.location = location;
        this.website = website;
        this.checkin = checkin;
        this.checkout = checkout;
        this.amenities = amenities;
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

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
