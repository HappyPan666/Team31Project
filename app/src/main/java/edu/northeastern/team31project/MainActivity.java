package edu.northeastern.team31project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button weather = (Button) findViewById(R.id.buttonWeather);
        weather.setOnClickListener(this);
    }

    public void onClick(View view){
        if(view.getId() == R.id.buttonWeather) {
            Intent intent = new Intent(MainActivity.this, weather.class);
            startActivity(intent);
        }
    }


}