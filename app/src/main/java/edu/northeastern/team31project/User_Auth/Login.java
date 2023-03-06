package edu.northeastern.team31project.User_Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import edu.northeastern.team31project.Contact.Contact;
import edu.northeastern.team31project.R;

public class Login extends AppCompatActivity {
    EditText signInUserEmail, signInUserPassword;
    TextView signUpText;
    Button signinButton;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInUserEmail = findViewById(R.id.signIn_email);
        signInUserPassword = findViewById(R.id.signIn_password);
        signUpText = findViewById(R.id.signup_text);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getUid() != null) {
            User.signOut();
        }

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
            }
        });

        signinButton = findViewById(R.id.loginButton);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserEmail = signInUserEmail.getText().toString();
                String UserPassword = signInUserPassword.getText().toString();

                logIn(UserEmail, UserPassword);
            }
        });
    }

    private void logIn(String userEmail, String userPassword) {
        User.signOut();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(Login.this,"Empty line is invalid", Toast.LENGTH_SHORT).show();
        } else if (!userEmail.matches(emailPattern)) {
            signInUserEmail.setError("Invalid Email");
            Toast.makeText(Login.this, "Email is invalid", Toast.LENGTH_SHORT).show();
        } else if (userPassword.length() < 6) {
            signInUserPassword.setError("Invalid Password");
            Toast.makeText(Login.this, "6 characters at least", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Token.updateToken(firebaseAuth.getUid());
                    startActivity(new Intent(Login.this, Contact.class));
                } else {
                    Toast.makeText(Login.this, "Please sign up first", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
