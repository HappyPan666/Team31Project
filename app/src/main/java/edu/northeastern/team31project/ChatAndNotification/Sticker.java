package edu.northeastern.team31project.ChatAndNotification;

public class Sticker {

    private String imageName;
    private String senderId;
    private String receiverId;

    public Sticker() {

    }

    public Sticker(String imageName, String senderId, String receiverId) {
        this.imageName = imageName;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
}
