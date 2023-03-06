package edu.northeastern.team31project.ChatAndNotification;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.team31project.R;

public class ChatReceiverHolder extends RecyclerView.ViewHolder  {
    ImageView iv_imageView;

    public ChatReceiverHolder(@NonNull View itemView) {
        super(itemView);
        iv_imageView = itemView.findViewById(R.id.receiver_img);
    }
}