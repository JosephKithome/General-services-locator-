package com.example.sejjoh.gsls;

public class saveData {
    String upname;
    String upphone;
    String upcurrentuser;
    String upuserid;
    String imageURL;
    String status;


    public saveData(String upname, String upphone, String upuserid, String upcurrentuser) {
        this.upname = upname;
        this.upphone = upphone;
        this.upuserid = upuserid;
        this.upcurrentuser = upcurrentuser;
    }

    public saveData() {
    }

    public String getUpname() {
        return upname;
    }

    public void setUpname(String upname) {
        this.upname = upname;
    }

    public String getUpphone() {
        return upphone;
    }

    public void setUpphone(String upphone) {
        this.upphone = upphone;
    }

    public String getUpcurrentuser() {
        return upcurrentuser;
    }

    public void setUpcurrentuser(String upcurrentuser) {
        this.upcurrentuser = upcurrentuser;
    }

    public String getUpuserid() {
        return upuserid;
    }

    public void setUpuserid(String upuserid) {
        this.upuserid = upuserid;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
