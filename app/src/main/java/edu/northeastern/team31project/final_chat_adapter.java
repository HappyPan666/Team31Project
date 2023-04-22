package edu.northeastern.team31project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class final_chat_adapter extends ArrayAdapter<final_single_message> {
    final Context final_chat_activity;
    final List<final_single_message> messageList;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    int SENDER_VIEW, RECEIVER_VIEW;

    public class final_chat_holder {
        TextView tv_message;
        TextView tv_name;
        TextView tv_date;
        TextView tv_time;
    }

    public final_chat_adapter(@NonNull Context context, int RECEIVER_VIEW, int SENDER_VIEW, @NonNull ArrayList<final_single_message> objects) {
        super(context, RECEIVER_VIEW, SENDER_VIEW, objects);
        this.final_chat_activity = context;
        this.SENDER_VIEW = SENDER_VIEW;
        this.RECEIVER_VIEW = RECEIVER_VIEW;
        this.messageList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        view = null;
        int viewType = getItemViewType(position);

        if (view == null) {
            getItemViewType(position);
            if (viewType == 99) {
                view = LayoutInflater.from(final_chat_activity).inflate(R.layout.activity_final_item_send_message, parent, false);
                LayoutInflater inflater = LayoutInflater.from(final_chat_activity);
                view = inflater.inflate(SENDER_VIEW, parent, false);
            } else {
                view = LayoutInflater.from(final_chat_activity).inflate(R.layout.activity_final_item_receive_message, parent, false);
                LayoutInflater inflater = LayoutInflater.from(final_chat_activity);
                view = inflater.inflate(RECEIVER_VIEW, parent, false);
            }
            final_single_message final_single_message;
            final_chat_holder final_chat_holder = new final_chat_holder();
            final_chat_holder.tv_date = (TextView) view.findViewById(R.id.syx_Date);
            final_chat_holder.tv_message = (TextView) view.findViewById(R.id.syx_MesContent);
            final_chat_holder.tv_name = (TextView) view.findViewById(R.id.syx_Name);
            final_chat_holder.tv_time = (TextView) view.findViewById(R.id.syx_Time);
            final_single_message = messageList.get(position);
            final_chat_holder.tv_name.setText(final_single_message.getSenderName());
            final_chat_holder.tv_message.setText(final_single_message.getMessageContent());
            final_chat_holder.tv_time.setText(final_single_message.getTime());
            final_chat_holder.tv_date.setText(final_single_message.getDate());
            view.setTag(final_chat_holder);
        }
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getSenderUID() != null) {
            if (messageList.get(position).getSenderUID().equals(firebaseAuth.getUid())) {
                return 99;
            } else {
                return -99;
            }
        } else {
            return -99;
        }
    }
}
