package edu.northeastern.team31project;

import static com.google.firebase.messaging.Constants.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class TrainingRecommendationDisplay extends AppCompatActivity {
    private ListView listView;
    //    private Button add_button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_videos_display);
        listView=findViewById(R.id.training_list);
//        add_button=findViewById(R.id.add);
        ArrayList<String> list=new ArrayList<>();
        ArrayAdapter adapter=new ArrayAdapter<String>(this,R.layout.list_item,list);
        listView.setAdapter(adapter);

        //DISPLAY Training list
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference trainingdata = database.getReference("trainingdata");

        //retrieve the muscle to be selected
        String selectedMuscle = getIntent().getStringExtra("selectedMuscle");

        trainingdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){ //how to implement the filter
                    if (selectedMuscle.contains(snapshot.child("muscle").getValue(String.class))){
                        list.add(snapshot.child("training_name").getValue(String.class));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void add() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference trainingdata = database.getReference("trainingdata");
//        trainingdata.child("4").setValue("push up");

//        trainingdata.child("4").setValue(56);
        HashMap<String,Object> map=new HashMap<>();
        map.put("training_name","PH");
        map.put("equipment","PH");
        map.put("muscle","PH");
        trainingdata.child("4").updateChildren(map);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.add) {
            Intent intent = new Intent(TrainingRecommendationDisplay.this, AddNewTrainingFromDatabase.class);
            startActivity(intent);
        }

    }
}
