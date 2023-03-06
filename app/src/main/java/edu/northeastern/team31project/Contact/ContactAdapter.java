package edu.northeastern.team31project.Contact;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.team31project.ChatAndNotification.Chat;
import edu.northeastern.team31project.R;
import edu.northeastern.team31project.User_Auth.User;

public class ContactAdapter extends RecyclerView.Adapter<ContactHolder> {
    final List<User> userList;
    final Context ContactActivity;

    public ContactAdapter(List<User> userList, Context ContactActivity) {
        this.userList = userList;
        this.ContactActivity = ContactActivity;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_contact, parent, false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        User user = userList.get(position);
        holder.tv_userName.setText(user.getUsername());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactActivity, Chat.class);
                intent.putExtra("username", user.getUsername());
                intent.putExtra("uid", user.getUid());
                ContactActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
