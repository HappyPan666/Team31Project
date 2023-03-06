package edu.northeastern.team31project.User_Auth;

import com.google.firebase.auth.FirebaseAuth;

public class User {
    private String uid;
    private String username;
    private String email;

    public User() {
    }

    public User(String uid, String username, String email) {
        this.uid = uid;
        this.username = username;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static void signOut() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getUid() != null) {
            Token.updateToken(firebaseAuth.getUid(), "");
            firebaseAuth.signOut();
        }
    }
}

