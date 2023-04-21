package edu.northeastern.team31project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.team31project.User_Auth.Login;

public class final_profile extends AppCompatActivity {

    //Google
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    final_user user;
    private String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_profile);
        //firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        uId = firebaseAuth.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("user/").child(uId).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                user = task.getResult().getValue(final_user.class);
            }
        });
    }

    public void UpdateProfile(View view){
        startActivity(new Intent(getApplicationContext(), final_edit_profile.class));
        overridePendingTransition(0, 0);  //overridePendingTransition(0, 0); 设置 Activity 转换效果，通过调用 overridePendingTransition() 方法并传递两个整数值 0，表示无动画效果，来禁用默认的 Activity 转换动画效果。这意味着，当新的 Activity 被启动时，它将立即出现，而不是使用默认的淡入淡出效果。
    }

    public void Sport(View view){
        startActivity(new Intent(getApplicationContext(), final_sport.class));
        overridePendingTransition(0, 0);
    }

    public void Chat(View view){
        startActivity(new Intent(getApplicationContext(), final_chat.class));
        overridePendingTransition(0, 0);
    }

    public void Post(View view){
        startActivity(new Intent(getApplicationContext(), final_post.class));
        overridePendingTransition(0, 0);
    }

    public void logOut(View view){
        firebaseAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), final_front_page.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
