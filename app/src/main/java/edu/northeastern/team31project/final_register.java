package edu.northeastern.team31project;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.northeastern.team31project.User_Auth.User;

public class final_register extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    boolean hasRegistered = false;
    DatabaseReference databaseReference;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText userName;
    EditText phoneNumber;
    EditText password;
    EditText height;
    EditText weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_register);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("user/");

        firstName = findViewById(R.id.final_regis_first_name_et);
        lastName = findViewById(R.id.final_regis_last_name_et);
        email = findViewById(R.id.final_regis_email_et);
        userName = findViewById(R.id.final_regis_username_et);
        phoneNumber = findViewById(R.id.final_regis_phone_et);
        password = findViewById(R.id.final_regis_password_et);
        height = findViewById(R.id.final_regis_height_et);
        weight = findViewById(R.id.final_regis_weight_et);
    }

    private Boolean validateForm (String emails, String passwords, String firstNames, String lastNames, String userNames, String phoneNumbers, String weights, String heights) {
        Boolean flag = true;
        Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");
        Matcher matcher = pattern.matcher(emails);

        if (firstNames != null && lastNames != null && emails != null && userNames != null && phoneNumbers != null && passwords != null && weights != null && heights != null) {
            if (firstNames.isEmpty()) {
                firstName.setError("Information is required.");
                flag = false;
            }
            if (lastNames.isEmpty()) {
                lastName.setError("Information is required.");
                flag = false;
            }
            if (emails.isEmpty()) {
                email.setError("Information is required.");
                flag = false;
            }
            if (userNames.isEmpty()) {
                userName.setError("Information is required.");
                flag = false;
            }
            if (phoneNumbers.isEmpty()) {
                phoneNumber.setError("Information is required.");
                flag = false;
            }
            if (passwords.isEmpty()) {
                password.setError("Information is required.");
                flag = false;
            }

            if (weights.isEmpty()) {
                height.setError("Information is required.");
                flag = false;

            }
            if (weights.isEmpty()) {
                weight.setError("Information is required.");
                flag = false;
            }

            if (flag) {
                if (matcher.find() && passwords.length() > 5) {
                    return true;
                } else {
                    if (!matcher.find()) {
                        this.email.setError("Invalid email.");
                    }
                    if (passwords.length() < 5) {
                        this.password.setError("The password should be at least 5 characters.");
                    }
                    flag = false;
                }

            }
        } else {
            flag = false;
        }

        return flag;
    }

    public void register(View view) {
        hasRegistered = false;
        String firstNames = firstName.getText().toString();
        String lastNames = lastName.getText().toString();
        String emails = email.getText().toString();
        String userNames = userName.getText().toString();
        String phoneNumbers = phoneNumber.getText().toString();
        String passwords = password.getText().toString();
        String heights = height.getText().toString();
        String weights = weight.getText().toString();

        if (validateForm(emails, passwords, firstNames, lastNames, userNames, phoneNumbers, heights, weights)) {

            hasRegistered = false;
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot single: snapshot.getChildren())
                    {
                        final_user user = single.getValue(final_user.class);
                        if(user.getUserName().equals(userNames)) {
                            hasRegistered = true;
                            userName.setError("User Existed");
                        }
                    }
                    if (!hasRegistered) {
                        firebaseAuth.createUserWithEmailAndPassword(emails, passwords).addOnCompleteListener(task -> {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                final_user user2 = new final_user();
                                user2.setFirstName(firstNames);
                                user2.setLastName(lastNames);
                                user2.setPhoneNumber(phoneNumbers);
                                user2.setUserName(userNames);
                                user2.setHeight(heights);
                                user2.setWeight(weights);
                                user2.setEmail(emails);
                                user2.setUid(firebaseAuth.getUid());

                                String key = firebaseUser.getUid();
                                databaseReference = firebaseDatabase.getReference("user/" + key);
                                databaseReference.setValue(user2);
                                startActivity(new Intent(getApplicationContext(), final_profile.class));
                            }
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}