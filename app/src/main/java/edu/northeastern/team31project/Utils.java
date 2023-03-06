package edu.northeastern.team31project;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Field;
import java.util.HashMap;


public class Utils {
    public static int getImgId(String imgName) {
        int resId = -1;
        try {
            // 在R.drawable类中查找名为imgName的字段并返回其Field对象。
            Field idField = R.drawable.class.getDeclaredField(imgName);
            resId = idField.getInt(idField);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return resId;
    }

    // update token when login
    public static void updateDeviceToken(final Context context, String token)
    {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser!=null) {

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference databaseReference = rootRef.child("tokens").child(currentUser.getUid());
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("tokens", token);

            databaseReference.setValue(hashMap).addOnCompleteListener(task -> {
                if(!task.isSuccessful()) {
                    Toast.makeText(context, "failed_to_save_device_token", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

