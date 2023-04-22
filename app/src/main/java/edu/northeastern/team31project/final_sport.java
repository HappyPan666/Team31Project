package edu.northeastern.team31project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class final_sport extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_sport);

        Button training_video = (Button) findViewById(R.id.trainingVideos);
        training_video.setOnClickListener(this);

        Button fitness_reminder = (Button) findViewById(R.id.locationSensor);
        fitness_reminder.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.trainingVideos) {
            Intent intent = new Intent(final_sport.this, TrainingRecommendation.class);
            startActivity(intent);
        } else if (view.getId() == R.id.locationSensor) {
            Intent intent = new Intent(final_sport.this, LocationSensor.class);
            startActivity(intent);
        }
    }
}