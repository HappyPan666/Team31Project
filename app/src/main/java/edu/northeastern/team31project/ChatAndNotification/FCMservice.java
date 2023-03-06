package edu.northeastern.team31project.ChatAndNotification;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import edu.northeastern.team31project.Contact.Contact;
import edu.northeastern.team31project.R;
import edu.northeastern.team31project.Utils;


public class FCMservice extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "edu.northeastern.team31project.A8_stick";
    private static final String CHANNEL_NAME = "A8_stick";

    // FCM令牌更新时，该方法将被调用
    // 在 Android 客户端的 FirebaseMessagingService 中执行的，用于获取新的设备令牌并更新到服务器
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Utils.updateDeviceToken(this, token);
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    @Override
    // 是在客户端的Firebase Cloud Messaging (FCM) 中实现的。
    // 当设备收到新的 FCM 消息时，FirebaseMessagingService 服务将自动调用该方法
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("body");
        String image = remoteMessage.getData().get("img");
        String senderUid = remoteMessage.getData().get("sender");
        String senderName = remoteMessage.getData().get("senderName");
        String receiverUid = remoteMessage.getData().get("receiver");
        String receiverName = remoteMessage.getData().get("receiverName");

        Intent intentChat = new Intent(this, Contact.class);

        // 在未来某个时候代表应用程序执行的操作的对象
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, intentChat, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intentChat, PendingIntent.FLAG_ONE_SHOT);
        }

//        是用于管理应用程序的通知的系统服务，它可以在状态栏中创建通知图标、声音、震动等
        final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        在使用 NotificationManager 创建通知之前，必须使用 NotificationCompat.Builder
//        创建一个通知构建器实例，该实例用于设置通知的各种属性
        final NotificationCompat.Builder notificationBuilder;

        // 定义通知渠道。通知渠道是一种将通知分组的方式，允许用户控制如何显示来自应用程序的通知
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            // 创建一个名为CHANNEL_ID、显示名称为CHANNEL_NAME的通知渠道，并设置其重要性为IMPORTANCE_HIGH
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            // 为该通知渠道添加描述信息
            channel.setDescription("Chat App notifications");
            // 通过createNotificationChannel()方法创建该通知渠道
            notificationManager.createNotificationChannel(channel);
            // 创建一个通知建造者，将其与上面创建的通知渠道关联
            notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this);
        }

        // 创建通知， 并设置通知的各种属性
        // 获取默认的通知铃声
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // 获取应用资源中的图片，将其转化为 Bitmap 类型，以便于设置为通知的大图标
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), Utils.getImgId(image));
        notificationBuilder
                .setContentTitle(title)
                .setContentText(message)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.icon_user)        // 小图标
                .setLargeIcon(bitmap)                       // 大图标
                .setAutoCancel(true);                       // 设置点击通知后自动取消通知

        // 使用NotificationManagerCompat发布通知
        // 用于发送通知的方法。在这里，通知的 ID 被设置为 999，
        // 而通知的内容由 notificationBuilder.build() 构建而成。
        // 此方法会显示通知在通知栏中，如果用户点击通知，会触发 pendingIntent 对应的操作。
        notificationManager.notify(999, notificationBuilder.build());
    }
}

