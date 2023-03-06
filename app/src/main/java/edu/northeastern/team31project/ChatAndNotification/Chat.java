package edu.northeastern.team31project.ChatAndNotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import java.util.List;
import java.util.Objects;

import edu.northeastern.team31project.R;
import edu.northeastern.team31project.User_Auth.Token;
import edu.northeastern.team31project.User_Auth.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Chat extends AppCompatActivity {
    TextView tv_friendName;
    List<Sticker> stickerList;
    ImageView iv_goBackIcon, sticker1, sticker2, sticker3, sticker4, sticker5, sticker6, sticker7, sticker8, sticker9;
    String senderUID, receiverUID, senderName, receiverName;
    ChatAdapter chatAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    String roomBySender, roomByReceiver;

    private static Retrofit retrofit = null;
    WebAPI webAPI = ConnectClient().create(WebAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        stickerList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        iv_goBackIcon = findViewById(R.id.arrow_back);
        iv_goBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        sticker1 = findViewById(R.id.sticker1);
        sticker2 = findViewById(R.id.sticker2);
        sticker3 = findViewById(R.id.sticker3);
        sticker4 = findViewById(R.id.sticker4);
        sticker5 = findViewById(R.id.sticker5);
        sticker6 = findViewById(R.id.sticker6);
        sticker7 = findViewById(R.id.sticker7);
        sticker8 = findViewById(R.id.sticker8);
        sticker9 = findViewById(R.id.sticker9);

        sticker1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("sticker1");
            }
        });

        sticker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("sticker2");
            }
        });

        sticker3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("sticker3");
            }
        });

        sticker4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("sticker4");
            }
        });

        sticker5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("sticker5");
            }
        });

        sticker6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("sticker6");
            }
        });

        sticker7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("sticker7");
            }
        });

        sticker8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("sticker8");
            }
        });

        sticker9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("sticker9");
            }
        });

        receiverName = getIntent().getStringExtra("username");
        tv_friendName = findViewById(R.id.friendName);
        tv_friendName.setText(receiverName);

        recyclerView = findViewById(R.id.message_list);

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (Objects.equals(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail(), Objects.requireNonNull(user).getEmail())) {
                        senderName = user.getUsername();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        receiverUID = getIntent().getStringExtra("uid");
        senderUID = firebaseAuth.getUid();

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(Chat.this, stickerList);
        recyclerView.setAdapter(chatAdapter);

        roomBySender = senderUID + receiverUID;
        roomByReceiver = receiverUID + senderUID;

        DatabaseReference databaseReference1 = firebaseDatabase.getReference().child("chats").child(roomBySender).child("messages");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stickerList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Sticker sticker = dataSnapshot.getValue(Sticker.class);
                    stickerList.add(sticker);
                }
                chatAdapter.notifyDataSetChanged();
                System.out.println(stickerList.size());     // ??????????????????????????????
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String stickerName) {
        Sticker sticker = new Sticker(stickerName, senderUID, receiverUID);
        DatabaseReference refSender = firebaseDatabase.getReference().child("chats").child(roomBySender).child("messages");
        DatabaseReference refReceiver = firebaseDatabase.getReference().child("chats").child(roomByReceiver).child("messages");

        refSender.push().setValue(sticker).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                sendAndNotify(senderUID, senderName, receiverUID, receiverName, stickerName);
                refReceiver.push().setValue(sticker);
            }
        });
    }

    private void sendAndNotify(String senderUID, String senderName, String receiverUID, String receiverName, String stickerName) {
        DatabaseReference databaseReference = firebaseDatabase.getReference("tokens");
        Query query = databaseReference.orderByKey().equalTo(receiverUID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Token token = dataSnapshot.getValue(Token.class);

                    Data data = new Data(senderUID, senderName, receiverUID, receiverName, "New Notification", senderName + " sent a message to you", stickerName);
                    Send send = new Send(data, Objects.requireNonNull(token).getToken());
                    webAPI.sendAndNotify(send).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                            if (response.isSuccessful()) {
                                System.out.println("send");
                            } else {
                                System.out.println("send");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {
                            System.out.println("Failed");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private static Retrofit ConnectClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl("https://fcm.googleapis.com/").addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
