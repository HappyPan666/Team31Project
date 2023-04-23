package edu.northeastern.team31project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.RestrictionsManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class final_share extends AppCompatActivity {
    final_session final_session;
    String sessionID;
    Bitmap bitmap;
//    private static final int CAMERA_PERMISSION_ID = 1;
//    private static final int IMAGE_PICKER_PERMISSION_ID = 2;
//    private static final String CAMERA_NAME = Manifest.permission.CAMERA;
//    private static final String IMAGE_PICKER_NAME = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int IMAGE_REQUEST = 2;
    private Uri imageUri;
    ImageView imageView;
    Button Btn_takePics;
    Button Btn_uploadPics;
    Button Btn_sharePics;
    Button Btn_overPost;
    TextView titleName;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    final_user final_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_share);

        Btn_takePics = (Button) findViewById(R.id.syx_open_camera);
        Btn_uploadPics = (Button) findViewById(R.id.syx_upload_pics);
//        Btn_sharePics = (Button) findViewById(R.id.syx_share_pics);
        Btn_overPost = (Button) findViewById(R.id.syx_stop_share);
        titleName = findViewById(R.id.syx_titleName);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        String UID = firebaseAuth.getUid();
        databaseReference = firebaseDatabase.getReference("user/" + UID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final_user = snapshot.getValue(final_user.class);
                titleName.setText("Hold on, " + final_user.getFirstName().toUpperCase() + "!");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void takePics(View view) {
        Intent takePictureWithCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureWithCamera, 0);
    }

    public void uploadPics(View view) {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
            imageUri = data.getData();
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri != null) {
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploads").child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url=uri.toString();
                            pd.dismiss();
                        }
                    });
                }
            });
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    public void overPost(View view) {
        startActivity(new Intent(getApplicationContext(), final_profile.class));
    }







//    private void takePicture() {
//        Intent take_pic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(take_pic, CAMERA_PERMISSION_ID);
//    }

//    private void choosePicture() {
//        Intent choose_pic = new Intent(Intent.ACTION_PICK);
//        choose_pic.setType("image/*");
//        startActivityForResult(choose_pic, IMAGE_PICKER_PERMISSION_ID);
//    }

//    @Override
//    protected void onActivityResult(int request_code, int result_code, Intent data) {
//        super.onActivityResult(request_code, result_code, data);
//        if (request_code == CAMERA_PERMISSION_ID && result_code == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            bitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(bitmap);
//        } else if (request_code == IMAGE_PICKER_PERMISSION_ID && result_code == RESULT_OK) {
//            try {
//                final Uri imageUri = data.getData();
//                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                bitmap = BitmapFactory.decodeStream(imageStream);
//                imageView.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == CAMERA_PERMISSION_ID) {
//            if (PermissionManager.checkPermission(this, CAMERA_NAME)) {
//                takePicture();
//            }
//        }
//        if (requestCode == IMAGE_PICKER_PERMISSION_ID) {
//            if (PermissionManager.checkPermission(this, IMAGE_PICKER_NAME)) {
//                choosePicture();
//            }
//        }
//    }

}