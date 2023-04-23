package edu.northeastern.team31project;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class final_upload {

    public static void upload(final_single_post final_single_post, byte[] imgData, FirebaseDatabase firebaseDatabase, FirebaseStorage firebaseStorage, Intent targetIntent, Context context) {
        if (final_single_post != null) {
            firebaseDatabase.getReference("posts/" + final_single_post.getPosterUID()).push().setValue(final_single_post).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    if (!final_single_post.getPostImage().equals("")) {
                        firebaseStorage.getReference(final_single_post.getPostImage()).putBytes(imgData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                context.startActivity(targetIntent);
                            }
                        });
                    } else {
                        context.startActivity(targetIntent);
                    }
                }
            });
        }
    }

    public static final_single_post createPostFromSession(final_session session, String userUID, String imagePath){
        final_single_post final_single_post = new final_single_post();
        final_single_post.setPostType(0);
        final_single_post.setPostdate(new Date());
        final_single_post.setPosterUID(userUID);
        final_single_post.setPostImage(imagePath);
        return final_single_post;
    }

}
