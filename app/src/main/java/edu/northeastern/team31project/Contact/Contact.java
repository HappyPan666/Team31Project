package edu.northeastern.team31project.Contact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.northeastern.team31project.R;
import edu.northeastern.team31project.User_Auth.Login;
import edu.northeastern.team31project.User_Auth.User;

public class Contact extends AppCompatActivity {
    TextView tv_userName;
    List<User> userList;
    ImageView iv_signOutIcon;
    ContactAdapter contactAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        userList = new ArrayList<>();
        tv_userName = findViewById(R.id.Contacts);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        iv_signOutIcon = findViewById(R.id.img_logout);
        iv_signOutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.signOut();
                Intent intent = new Intent(Contact.this, Login.class);
                startActivity(intent);
                Contact.this.finish();
            }
        });

        recyclerView = findViewById(R.id.user_recycler_view);

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (!Objects.equals(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail(), Objects.requireNonNull(user).getEmail())) {
                        userList.add(user);
                    } else {
                        tv_userName.setText(user.getUsername());
                    }
                }
                contactAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        contactAdapter = new ContactAdapter(userList, Contact.this);            // ???
        recyclerView.setAdapter(contactAdapter);
    }
}