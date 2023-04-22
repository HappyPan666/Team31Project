package edu.northeastern.team31project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class final_contact_adapter extends RecyclerView.Adapter<final_contact_holder> {
    final List<final_user> userList;
    final Context final_contact_activity;

    public final_contact_adapter(List<final_user> userList, Context contactActivity) {
        this.userList = userList;
        final_contact_activity = contactActivity;
    }

    @NonNull
    @Override
    public final_contact_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_final_item_contact, parent, false);
        return new final_contact_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final_contact_holder holder, int position) {
        final_user final_user = userList.get(position);
        holder.tv_userName.setText(final_user.getFirstName() + " " + final_user.getLastName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(final_contact_activity, final_chat.class);
                intent.putExtra("firstName", final_user.getFirstName());
                intent.putExtra("lastName", final_user.getLastName());
                intent.putExtra("userName", final_user.getUserName());
                intent.putExtra("UID", final_user.getUid());
                final_contact_activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
