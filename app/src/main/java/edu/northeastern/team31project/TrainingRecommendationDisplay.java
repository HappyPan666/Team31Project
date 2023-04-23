package edu.northeastern.team31project;

import static com.google.firebase.messaging.Constants.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.firestore.util.Listener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

import edu.northeastern.team31project.firebase.FirebaseTool;
import edu.northeastern.team31project.firebase.model.TrainingData;

public class TrainingRecommendationDisplay extends AppCompatActivity {
    private ListView listView;
    private Button addPicture;
    private Button takePicture;

    private Uri imageUri;

    private static final int IMAGE_REQUEST=2;

    //    private Button add_button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_videos_display);
        listView=findViewById(R.id.training_list);
        addPicture=findViewById(R.id.add_picture);
        takePicture=findViewById(R.id.take_picture);
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

        Listener<List<TrainingData>> trainingDataListener = new Listener<List<TrainingData>>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onValue(List<TrainingData> trainingData) {
                list.clear();

                if (trainingData.size()==0) {
                    list.add("Sorry we couldn't find any matched training");
                    adapter.notifyDataSetChanged();
                } else {
                    int i=0;
                    for (TrainingData data: trainingData) {
                        list.add("No."+i+": "+data.training_name+" "+data.muscle+" "+data.time+" "+data.equipment);
                        list.add("See the instruction here: "+data.instruction);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        };

        FirebaseTool.getTrainingData(
                selectedMuscle,
                selectedTime,
                selectedEquipment,
                trainingDataListener
        );

        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureWithCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureWithCamera, 0);
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

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
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