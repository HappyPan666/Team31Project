package edu.northeastern.team31project.ChatAndNotification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

import edu.northeastern.team31project.R;

public class ChatAdapter extends RecyclerView.Adapter {
    final Context ChatActivity;
    final List<Sticker> stickers;

    int SENDER_VIEW = 1, RECEIVER_VIEW = 2;

    public ChatAdapter(Context chatActivity, List<Sticker> stickers) {
        this.ChatActivity = chatActivity;
        this.stickers = stickers;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == RECEIVER_VIEW) {
            View view = LayoutInflater.from(ChatActivity).inflate(R.layout.activity_item_receive_sticker, parent, false);
            return new ChatReceiverHolder(view);
        } else {
            View view = LayoutInflater.from(ChatActivity).inflate(R.layout.activity_item_send_sticker, parent, false);
            return new ChatSenderHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Sticker sticker = stickers.get(position);
        if (holder.getClass() == ChatReceiverHolder.class) {
            ChatReceiverHolder chatReceiverHolder = (ChatReceiverHolder) holder;
            chatReceiverHolder.iv_imageView.setImageResource(getStickerId(sticker.getImageName()));
        } else {
            ChatSenderHolder chatSenderHolder = (ChatSenderHolder) holder;
            chatSenderHolder.iv_imageView.setImageResource(getStickerId(sticker.getImageName()));
        }
    }

    @Override
    public int getItemCount() {
        return stickers.size();
    }

    @Override
    public int getItemViewType(int position) {
        Sticker sticker = stickers.get(position);
        if (Objects.equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), sticker.getReceiverId())) {           // 可能要改
            return RECEIVER_VIEW;
        } else {
            return SENDER_VIEW;
        }
    }

    public static int getStickerId(String stickerName) {
        int id = -999;
        try
        {
            Field field = R.drawable.class.getDeclaredField(stickerName);
            id = field.getInt(field);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return id;
    }
}