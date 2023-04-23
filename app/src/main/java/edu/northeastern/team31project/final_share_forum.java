package edu.northeastern.team31project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class final_share_forum extends AppCompatActivity {
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ListView listView;
    List<String> postersUID;
    List<final_single_post> postList;
    Map<String, Boolean> postNumber;
    Map<String, final_user> postersMap;
    final_post_adapter final_post_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_share_forum);

        postersUID = new ArrayList<>();
        postList = new ArrayList<>();
        postNumber = new HashMap<>();
        postersMap = new HashMap<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        listView = findViewById(R.id.feed_list_view);
        final_post_adapter = new final_post_adapter(this, postList, postersMap, firebaseUser.getUid());

        listView.setAdapter(final_post_adapter);
        this.getPost();
    }

    private void getPost() {
        databaseReference = FirebaseDatabase.getInstance().getReference("user/" + firebaseUser.getUid());
        databaseReference.get().addOnSuccessListener(v -> {
            for (DataSnapshot dataSnapshot : v.getChildren()) {
                postersUID.add(dataSnapshot.getKey());
            }
            postersUID.add(firebaseUser.getUid());

            for (String user : postersUID) {
                DatabaseReference posts = FirebaseDatabase.getInstance().getReference("posts/" + user);
                posts.get().addOnSuccessListener(Task -> {
                    if (Task.getValue() != null) {
                        FirebaseDatabase.getInstance().getReference("user/" + Task.getKey())
                                .get()
                                .addOnSuccessListener(Task2 -> {
                                    postersMap.put(Task.getKey(), Task2.getValue(final_user.class));
                                    for (DataSnapshot dataSnapshot : Task.getChildren()) {
                                        if (postNumber.get(dataSnapshot.getKey()) == null) {
                                            postNumber.put(dataSnapshot.getKey(), true);
                                            final_post_adapter.add(dataSnapshot.getValue(final_single_post.class));
                                            final_post_adapter.sort((o1, o2) -> o2.getPostdate().compareTo(o1.getPostdate()));
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}