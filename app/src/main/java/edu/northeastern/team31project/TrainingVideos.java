package edu.northeastern.team31project;

import static com.google.firebase.messaging.Constants.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrainingVideos extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_videos_filter);

        CheckBox chestCheckBox=findViewById(R.id.chest);
        boolean chestSelected=chestCheckBox.isChecked();
        CheckBox bicepCheckBox=findViewById(R.id.bicep);
        boolean bicepSelected=bicepCheckBox.isChecked();
        CheckBox legCheckBox=findViewById(R.id.leg);
        boolean legSelected=legCheckBox.isChecked();
    }


    public void onClick(View view) {
        if (view.getId() == R.id.Continue) {
            Intent intent = new Intent(TrainingVideos.this, TrainingVideosDisplay.class);
            startActivity(intent);
        }

//    query the database - SQLite?


//    String[] projection = {
//            ExerciseContract.ExerciseEntry._ID,
//            ExerciseContract.ExerciseEntry.COLUMN_NAME,
//            ExerciseContract.ExerciseEntry.COLUMN_MUSCLE_GROUP,
//            ExerciseContract.ExerciseEntry.COLUMN_EQUIPMENT
//    };
//
//    String selection = ExerciseContract.ExerciseEntry.COLUMN_MUSCLE_GROUP + " = ?" +
//            " AND " + ExerciseContract.ExerciseEntry.COLUMN_EQUIPMENT + " = ?";
//
//    String[] selectionArgs = {muscleGroup, equipment};
//
//    String sortOrder =
//            ExerciseContract.ExerciseEntry.COLUMN_NAME + " ASC";
//
//    Cursor cursor = db.query(
//            ExerciseContract.ExerciseEntry.TABLE_NAME,
//            projection,
//            selection,
//            selectionArgs,
//            null,
//            null,
//            sortOrder
//    );

        //use firebase
        // Write a message to the database

        //my data structure should look like:
        //- id
        //- training_name
        //- training_muscle
        //- equipment
    }
}
