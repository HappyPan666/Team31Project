package edu.northeastern.team31project;

import static com.google.firebase.messaging.Constants.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TrainingRecommendation extends AppCompatActivity {

    public String muscleSelected;
    public String timeSelected;
    public String equipmentSelected;
    public RadioGroup muscleRadioGroup;
    public RadioGroup timeRadioGroup;
    public RadioGroup equipmentRadioGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_videos_filter);
        muscleRadioGroup=findViewById(R.id.muscle_radio_group);
        timeRadioGroup=findViewById(R.id.time_radio_group);
        equipmentRadioGroup=findViewById(R.id.equipment_radio_group);

        muscleRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // on below line we are getting radio button from our group.
                RadioButton radioButton = findViewById(checkedId);
                Toast.makeText(TrainingRecommendation.this, "Selected Radio Button is : " + radioButton.getText(), Toast.LENGTH_SHORT).show();
                muscleSelected=radioButton.getText().toString();
            }
        });

        timeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // on below line we are getting radio button from our group.
                RadioButton radioButton = findViewById(checkedId);
                timeSelected=radioButton.getText().toString();
            }
        });

        equipmentRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // on below line we are getting radio button from our group.
                RadioButton radioButton = findViewById(checkedId);
                equipmentSelected=radioButton.getText().toString();
            }
        });
    }


    public void onClick(View view) {
        if (view.getId() == R.id.Continue) {
            Intent intent = new Intent(TrainingRecommendation.this, TrainingRecommendationDisplay.class);
            intent.putExtra("muscleSelected",muscleSelected);
            intent.putExtra("timeSelected",timeSelected);
            intent.putExtra("equipmentSelected",equipmentSelected);
            startActivity(intent);
        }

    }
}
