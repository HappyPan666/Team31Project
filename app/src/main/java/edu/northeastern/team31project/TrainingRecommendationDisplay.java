package edu.northeastern.team31project;

import static com.google.firebase.messaging.Constants.TAG;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class TrainingRecommendationDisplay extends AppCompatActivity {
    private ListView listView;
    private Button addPicture;

    private Uri imageUri;

    private static final int IMAGE_REQUEST=2;
    //    private Button add_button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_videos_display);
        listView=findViewById(R.id.training_list);
        addPicture=findViewById(R.id.add_picture);
//        add_button=findViewById(R.id.add);
        ArrayList<String> list=new ArrayList<>();
        ArrayAdapter adapter=new ArrayAdapter<String>(this,R.layout.list_item,list);
        listView.setAdapter(adapter);

        //DISPLAY Training list
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference trainingdata = database.getReference("trainingdata");

        //retrieve the muscle to be selected
        String selectedMuscle = getIntent().getStringExtra("muscleSelected");
        String selectedTime = getIntent().getStringExtra("timeSelected");
        String selectedEquipment = getIntent().getStringExtra("equipmentSelected");

        trainingdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        list.add(snapshot.child("training_name").getValue(String.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

    }

    private void openImage() {
        Intent intent=new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==IMAGE_REQUEST && resultCode==RESULT_OK){
            imageUri=data.getData();
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri!=null){
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploads").child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url=uri.toString();
                            Log.d("DownloadUrl",url);
                            pd.dismiss();
                            Toast.makeText(TrainingRecommendationDisplay.this,"Image upload successful",Toast.LENGTH_SHORT).show();
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


    public void add() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference trainingdata = database.getReference("trainingdata");
//        trainingdata.child("4").setValue("push up");

//        trainingdata.child("4").setValue(56);
        HashMap<String,Object> map=new HashMap<>();
        map.put("training_name","PH");
        map.put("equipment","PH");
        map.put("muscle","PH");
        trainingdata.child("4").updateChildren(map);
    }

//    public void onClick(View view) {
//        if (view.getId() == R.id.add_picture) {
//            Intent intent = new Intent(TrainingRecommendationDisplay.this, AddNewTrainingFromDatabase.class);
//            startActivity(intent);
//        }
//
//    }
}
