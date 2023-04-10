package edu.northeastern.team31project;

import android.os.Bundle;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TrainingVideos extends AppCompatActivity {

    CheckBox chestCheckBox=findViewById(R.id.chest);
    boolean chestSelected=chestCheckBox.isChecked();
    CheckBox bicepCheckBox=findViewById(R.id.bicep);
    boolean bicepSelected=bicepCheckBox.isChecked();
    CheckBox legCheckBox=findViewById(R.id.leg);
    boolean legSelected=legCheckBox.isChecked();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_videos_filter);
    }

//    query the database - SQLite?
String[] projection = {
        ExerciseContract.ExerciseEntry._ID,
        ExerciseContract.ExerciseEntry.COLUMN_NAME,
        ExerciseContract.ExerciseEntry.COLUMN_MUSCLE_GROUP,
        ExerciseContract.ExerciseEntry.COLUMN_EQUIPMENT
};

    String selection = ExerciseContract.ExerciseEntry.COLUMN_MUSCLE_GROUP + " = ?" +
            " AND " + ExerciseContract.ExerciseEntry.COLUMN_EQUIPMENT + " = ?";

    String[] selectionArgs = {muscleGroup, equipment};

    String sortOrder =
            ExerciseContract.ExerciseEntry.COLUMN_NAME + " ASC";

    Cursor cursor = db.query(
            ExerciseContract.ExerciseEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
    );

}
