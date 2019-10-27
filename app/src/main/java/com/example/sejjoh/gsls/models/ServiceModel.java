package com.example.sejjoh.gsls.models;

public class ServiceModel {
    private String aiport;
    private String image;

    public ServiceModel() {
    }

    public ServiceModel(String aiport, String image) {
        this.aiport = aiport;
        this.image = image;
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

}
