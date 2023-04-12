package edu.northeastern.team31project;

import static com.google.firebase.messaging.Constants.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrainingVideosDisplay extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_videos_display);
        add();
    }

    public void add() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        Log.d("TAG_123", "query: "+database.getReference());
        DatabaseReference trainingdata = database.getReference("trainingdata");
//        trainingdata.setValue("push up");56

        trainingdata.child("1").setValue(56);

    }

    public void display(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        Log.d("TAG_123", "query: "+database.getReference());
        DatabaseReference trainingdata = database.getReference("trainingdata");
        trainingdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}
