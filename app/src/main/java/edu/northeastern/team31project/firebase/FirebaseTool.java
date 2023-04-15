package edu.northeastern.team31project.firebase;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.util.Listener;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.team31project.firebase.model.TrainingData;

public class FirebaseTool {

    private static final String TRAINING_DATA = "trainingdata";

    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();


    public static void getTrainingData(String selectedMuscle,
                                       String selectedTime,
                                       String selectedEquipment,
                                       Listener<List<TrainingData>> listener) {

        DatabaseReference ref = database.getReference(TRAINING_DATA);

        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    List<TrainingData> trainingDataList = new ArrayList<>();

                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        TrainingData data = snapshot.getValue(TrainingData.class);

                        if (data.matched(selectedMuscle, selectedTime, selectedEquipment)) {
                            trainingDataList.add(data);
                        }
                    }
                    listener.onValue(trainingDataList);
                } else {
                    // pass null if failed
                    listener.onValue(null);
                }
            }
        });
    }
}
