package edu.northeastern.team31project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class final_front_page extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_front_page);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        backToProfile(firebaseUser);
    }

    private void backToProfile(FirebaseUser firebaseUser) {
        if(firebaseUser!= null) {
            startActivity(new Intent(getApplicationContext(), final_profile.class));
        }
    }

    public void Login(View view) {
        startActivity(new Intent(getApplicationContext(), final_login.class));
    }

    public void Register(View view) {
        startActivity(new Intent(getApplicationContext(), final_register.class));
    }
}