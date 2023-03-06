package edu.northeastern.team31project.ChatAndNotification;

public class Send {
    public Data data;   // 通知
    public String to;   // 接收者的设备令牌

    public Send(Data data, String to) {
        this.data = data;
        this.to = to;
    }
}