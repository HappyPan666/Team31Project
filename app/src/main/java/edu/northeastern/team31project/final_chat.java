package edu.northeastern.team31project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class final_chat extends AppCompatActivity {
    TextView receiverName;
    EditText editText;
    ImageButton sendClickBtn;
    ListView messageListView;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference senderDatabaseReference;
    DatabaseReference receiverDatabaseReference;
    DatabaseReference finalUserDatabaseReference;
    final_user final_user;
    ArrayList<final_single_message> final_single_messageArrayList = new ArrayList<>();
    final_chat_adapter final_chat_adapter;

    String roomBySender, roomByReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_user_chat);

        receiverName = findViewById(R.id.syx_touxiang_userName);
        sendClickBtn = findViewById(R.id.syx_chat_page_send_button);
        editText = findViewById(R.id.syx_xiaoxineirong);
        messageListView = findViewById(android.R.id.list);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        final_single_messageArrayList = new ArrayList<final_single_message>();
        final_chat_adapter = new final_chat_adapter(getApplicationContext(), R.layout.activity_final_item_receive_message, R.layout.activity_final_item_send_message, final_single_messageArrayList);
        messageListView.setAdapter(final_chat_adapter);

        String receiverUID, firstName, lastName;
        firstName = getIntent().getExtras().getString("firstName");
        lastName = getIntent().getExtras().getString("lastName");
        receiverUID = getIntent().getExtras().getString("UID");
        receiverName.setText(firstName + " " + lastName);

        roomBySender = firebaseAuth.getUid() + receiverUID;
        roomByReceiver = receiverUID + firebaseAuth.getUid();

        senderDatabaseReference = firebaseDatabase.getReference().child("chat").child(roomBySender).child("messages");
        receiverDatabaseReference = firebaseDatabase.getReference().child("chat").child(roomByReceiver).child("messages");

        // 得到发送人的信息
        finalUserDatabaseReference = FirebaseDatabase.getInstance().getReference();
        finalUserDatabaseReference.child("user/").child(firebaseAuth.getUid()).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
            } else {
                final_user = task.getResult().getValue(final_user.class);
            }
        });

        sendClickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().length() > 0) {
                    final_single_messageArrayList.clear();
                    final_chat_adapter.clear();
                    final_chat_adapter.notifyDataSetChanged();
                    String date = new SimpleDateFormat("yyyy/MM/dd ").format(Calendar.getInstance().getTime());
                    String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                    String messageText = editText.getText().toString();
                    final_single_message final_single_message = new final_single_message();
                    final_single_message.setMessageContent(messageText);
                    final_single_message.setSenderUID(firebaseAuth.getUid());
                    final_single_message.setDate(date);
                    final_single_message.setTime(time);
                    final_single_message.setSenderName(final_user.getFirstName() + " " + final_user.getLastName());
                    senderDatabaseReference.push().setValue(final_single_message);
                    receiverDatabaseReference.push().setValue(final_single_message);
                    editText.setText("");
                }
            }
        });
        subscribeToChanges();
    }

    private void subscribeToChanges() {
        senderDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final_single_messageArrayList.clear();
                final_chat_adapter.clear();
                final_chat_adapter.notifyDataSetChanged();

                for (DataSnapshot single : snapshot.getChildren()) {
                    final_single_message final_single_message = single.getValue(final_single_message.class);
                    final_single_messageArrayList.add(final_single_message);
                    final_chat_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
