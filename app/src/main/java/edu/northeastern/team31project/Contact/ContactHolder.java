package edu.northeastern.team31project.Contact;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.team31project.R;

public class ContactHolder extends RecyclerView.ViewHolder {
    TextView tv_userName;

    public ContactHolder(@NonNull View itemView) {
        super(itemView);
        tv_userName = itemView.findViewById(R.id.friend);
    }
}
