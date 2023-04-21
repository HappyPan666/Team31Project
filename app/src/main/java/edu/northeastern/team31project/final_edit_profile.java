package edu.northeastern.team31project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class final_edit_profile extends AppCompatActivity {

    private final_user user;
    private String emailString;
    private String userID;
    boolean changePassword;
    boolean verifyPassword;


    //firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    //components
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText userName;
    EditText phoneNumber;
    EditText password;
    EditText newPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_final_edit_profile);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        emailString = firebaseAuth.getCurrentUser().getEmail();
        firebaseDatabase = FirebaseDatabase.getInstance();

//        get the input information
        firstName = findViewById(R.id.final_edit_profile_first_name_et);
        lastName = findViewById(R.id.final_edit_profile_last_name_et);
        email = findViewById(R.id.final_edit_profile_email_et);
        userName = findViewById(R.id.final_edit_profile_username_et);
        phoneNumber = findViewById(R.id.final_edit_profile_phone_number_et);
        password = findViewById(R.id.final_edit_profile_current_password_et);
        newPassword = findViewById(R.id.final_edit_profile_new_password_et);

        //authentication
        userID = firebaseAuth.getUid();
        databaseReference = firebaseDatabase.getReference("user/" + userID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(final_user.class);
                firstName.setText(user.getFirstName());
                lastName.setText(user.getLastName());
                email.setText(firebaseAuth.getCurrentUser().getEmail());
                userName.setText(user.getUserName());
                phoneNumber.setText(user.getPhoneNumber());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private Boolean validateForm(String emails, String passwords, String firstNames, String lastNames,
                                 String userNames, String phoneNumbers, String password2) {
        Boolean flag = true;
        Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");
        Matcher matched = pattern.matcher(emails);

        //data validation
        if (firstNames != null && lastNames != null && emails != null && userNames != null
                && phoneNumbers != null && passwords != null) {
            if (firstNames.isEmpty()) {
                firstName.setError("Information required");
                flag = false;
            }
            if (lastNames.isEmpty()) {
                lastName.setError("Information required");
                flag = false;
            }
            if (emails.isEmpty()) {
                email.setError("Information required");
                flag = false;
            }
            if (userNames.isEmpty()) {
                userName.setError("Information required");
                flag = false;
            }
            if (phoneNumbers.isEmpty()) {
                phoneNumber.setError("Information required");
                flag = false;
            }

            if (passwords.isEmpty() && !password2.isEmpty()) {
                password.setError("Information required");
                flag = false;
            }

            if (!matched.find()) {
                this.email.setError("Invalid mail");
                flag = false;
            }

        }
        return flag;
    }

    public boolean passwordAuthenication(String oldPassword, String newPassword) {
        boolean passwordUpdated = false;
        if (oldPassword.isEmpty() && newPassword.isEmpty()) {
            passwordUpdated = false;
        } else {
            if (newPassword.length() < 5) {
                this.newPassword.setError("The password must be longer than 5 characters.");
            }
            if (oldPassword.length() < 5) {
                this.password.setError("The password must be longer than 5 characters.");
            }
            if (oldPassword.length() > 5 && newPassword.length() > 5) {
                passwordUpdated = true;
            }
        }
        return passwordUpdated;
    }

    public void editProfile(View view) {

        String firstNames = firstName.getText().toString();
        String lastNames = lastName.getText().toString();
        String emails = email.getText().toString();
        String userNames = userName.getText().toString();
        String phones = phoneNumber.getText().toString();
        String oldPasswords = password.getText().toString();
        String newPasswords = this.newPassword.getText().toString();
        verifyPassword = true;
        changePassword =true;

        if (validateForm(emails, oldPasswords, firstNames, lastNames, userNames, phones, newPasswords))
        {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            String email = firebaseUser.getEmail();
            if (user != null) {
                user.setFirstName(firstNames);
                user.setLastName(lastNames);
                user.setPhoneNumber(phones);
                user.setUserName(userNames);

                //change password to new one
                if (!newPasswords.isEmpty()) {
                    changePassword = false;
                    if (passwordAuthenication(oldPasswords, newPasswords)) {
                        AuthCredential credential = EmailAuthProvider.getCredential(email, oldPasswords);
                        firebaseUser.reauthenticate(credential).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                firebaseUser.updatePassword(newPasswords).addOnCompleteListener(taskT -> {
                                    if (!taskT.isSuccessful()) {
                                        new MaterialAlertDialogBuilder(this)
                                                .setTitle("OOPS!")
                                                .setMessage("Authentication failed!")
                                                .setPositiveButton("OK", null)
                                                .show();
                                        password.setError("Invalid password");
                                    } else {
                                        changePassword = true;
                                        new MaterialAlertDialogBuilder(this)
                                                .setTitle("Correct!")
                                                .setMessage("Password updated!")
                                                .setPositiveButton("OK", null)
                                                .show();
                                    }
                                });
                            } else {
                                new MaterialAlertDialogBuilder(this)
                                        .setTitle("OOPS!")
                                        .setMessage("Authentication failed!")
                                        .setPositiveButton("OK", null)
                                        .show();
                                password.setError("Invalid password");
                            }
                        });
                    }
                }

                //mail change
                if (!email.equals(emails)) {
                    verifyPassword = false;
                    if (passwordNotEmpty(oldPasswords)) {
                        AuthCredential credential = EmailAuthProvider.getCredential(email, oldPasswords);
                        if (!firebaseUser.reauthenticate(credential).isSuccessful()) {
                            firebaseUser.reauthenticate(credential).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    firebaseUser.updateEmail(emails).addOnCompleteListener(taskEmail -> {
                                        verifyPassword = true;
                                        if (!taskEmail.isSuccessful()) {
                                            Toast.makeText(final_edit_profile.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                        } else {
                                            verifyPassword = true;
                                            Toast.makeText(final_edit_profile.this, "Updated mail", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                } else {
                                    Toast.makeText(final_edit_profile.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            this.password.setError("Incorrect password");
                            this.email.setError("Please re-enter the new email address");
                            Toast.makeText(final_edit_profile.this, "Incorrect data", Toast.LENGTH_SHORT).show();
                            this.email.setText(emailString);
                        }

                    }

                }
                //Change of other user data
                databaseReference = firebaseDatabase.getReference("user/" + userID);
                databaseReference.setValue(user);
            }
        }

        if (changePassword) {
            startActivity(new Intent(getApplicationContext(), final_profile.class));
        }
        overridePendingTransition(0, 0);
    }

    private boolean passwordNotEmpty(String password) {
        if (password.isEmpty()) {
            email.setError("You must fill in the CURRENT PASSWORD field to change the email address.");
            return false;
        }
        return true;
    }

}