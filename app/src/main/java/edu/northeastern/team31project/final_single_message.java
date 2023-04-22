package edu.northeastern.team31project;

public class final_single_message {
    String messageContent;
    String date;
    String time;
    String senderUID;
    String senderName;

    public final_single_message() {
    }

    public final_single_message(String messageContent, String date, String time, String senderUID, String senderName) {
        this.messageContent = messageContent;
        this.date = date;
        this.time = time;
        this.senderUID = senderUID;
        this.senderName = senderName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSenderUID() {
        return senderUID;
    }

    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
