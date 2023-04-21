package edu.northeastern.team31project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class final_login extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    //view
    EditText password;
    EditText email;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_login);

        firebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.final_login_email_et);
        password = findViewById(R.id.final_login_password_et);
        login = findViewById(R.id.final_login_loginButton);
        login.setOnClickListener(v -> {
            String emails = email.getText().toString();
            String pssswords = password.getText().toString();
            if(validateForm(emails,pssswords)) {
                signIn(emails,pssswords);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        backToProfile(firebaseAuth.getCurrentUser());
    }

    private  Boolean validateForm(String email, String password) {
        Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");
        if(email !=null && password!= null) {
            if(!email.isEmpty()&& !password.isEmpty()) {
                Matcher matcher = pattern.matcher(email);
                if(matcher.find() && password.length() > 5) {
                    return true;
                }
                else {
                    if(!matcher.find()) {
                        this.email.setError("Invalid mail");
                    }
                    if(password.length() < 5) {
                        this.password.setError("Password must be longer than 5 characters");
                    }
                }
            }
        }
        return false;

    }
    private void signIn(String email,String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        backToProfile(firebaseUser);
                    } else {
                        new MaterialAlertDialogBuilder(this)
                                .setTitle("Oops!")
                                .setMessage("Authentication failed!")
                                .setPositiveButton("OK",null)
                                .show();
                        backToProfile(null);
                    }
                });
    }

    private void backToProfile(FirebaseUser currentUser) {
        if(currentUser!=null) {
            startActivity(new Intent(getApplicationContext(), final_profile.class));
        }
        else {
            email.setText("");
            password.setText("");
        }
    }
}