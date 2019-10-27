package com.example.sejjoh.gsls.models;

public class GalleryModel {
    String images;
    String  imagetwo;
    String imageThree;
    String imagefour;
    String imagefive;

    public GalleryModel(String images, String imagetwo, String imageThree, String imagefour, String imagefive) {
        this.images = images;
        this.imagetwo = imagetwo;
        this.imageThree = imageThree;
        this.imagefour = imagefour;
        this.imagefive = imagefive;
    }

    public GalleryModel() {
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getImagetwo() {
        return imagetwo;
    }

    public void setImagetwo(String imagetwo) {
        this.imagetwo = imagetwo;
    }

    public String getImageThree() {
        return imageThree;
    }

    public void setImageThree(String imageThree) {
        this.imageThree = imageThree;
    }

    public String getImagefour() {
        return imagefour;
    }

    public void setImagefour(String imagefour) {
        this.imagefour = imagefour;
    }

    public String getImagefive() {
        return imagefive;
    }

    public void setImagefive(String imagefive) {
        this.imagefive = imagefive;
    }
}
