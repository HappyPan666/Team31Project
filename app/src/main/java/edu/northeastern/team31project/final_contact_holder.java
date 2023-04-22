package edu.northeastern.team31project;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class final_contact_holder extends RecyclerView.ViewHolder {
    TextView tv_userName;

    public final_contact_holder(@NonNull View itemView) {
        super(itemView);
        tv_userName = itemView.findViewById(R.id.syx_contact_name);
    }
}
