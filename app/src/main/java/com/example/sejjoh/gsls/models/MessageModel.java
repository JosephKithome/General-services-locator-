package com.example.sejjoh.gsls.models;

public class MessageModel {

    private String sender;
    private String receiver;
    private String message;
    private String imageURL;
    private boolean seen;

    public MessageModel(String sender, String receiver, String message, String imageURL, boolean seen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.imageURL = imageURL;
        this.seen = seen;
    }

    public MessageModel() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
