package com.example.sejjoh.gsls;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sejjoh.gsls.Adapters.MessageAdapter;
import com.example.sejjoh.gsls.Notifications.APIService;
import com.example.sejjoh.gsls.Notifications.Client;
import com.example.sejjoh.gsls.Notifications.Data;
import com.example.sejjoh.gsls.Notifications.MyResponse;
import com.example.sejjoh.gsls.Notifications.Sender;
import com.example.sejjoh.gsls.Notifications.Token;
import com.example.sejjoh.gsls.models.ChatModel;
import com.example.sejjoh.gsls.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Message extends AppCompatActivity {
    private TextView username;
    private CircleImageView profile_image;
    private  TextView email_user;

    private FirebaseUser fire_user;
    DatabaseReference reference;
    Intent intent;
    ValueEventListener seenListener;
    ImageButton btn_send;
    EditText text_send;
    private List<ChatModel> mChat;
    MessageAdapter messageAdapter;
    RecyclerView recyclerView;
    APIService apiService;
    boolean notify=false;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        recyclerView= findViewById(R.id.MessageRecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity(new Intent(Message.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        profile_image=findViewById(R.id.profile);
        username= findViewById(R.id.username);
        btn_send= findViewById(R.id.btn_send);
        text_send= findViewById(R.id.text_send);

        email_user= findViewById(R.id.email);
        intent=getIntent();
        userid=intent.getStringExtra("userid");
        fire_user= FirebaseAuth.getInstance().getCurrentUser();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify=true;
                String msg= text_send.getText().toString();
                if (!msg.equals("")){
                    sendMessage(fire_user.getUid(),userid,msg);
                }else{
                    Toast.makeText(Message.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });
        reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel=dataSnapshot.getValue(UserModel.class);
                username.setText(userModel.getUsername());
                if (userModel.getImageUrl().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);

                }else{
                    Glide.with(getApplicationContext()).load(userModel.getImageUrl()).into(profile_image);

                }
                readMessage(fire_user.getUid(),userid,userModel.getImageUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        seenMessage(userid);

    }
    private  void  seenMessage(final String userid){
        reference= FirebaseDatabase.getInstance().getReference("Chats");
        seenListener=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    ChatModel chat=snapshot.getValue(ChatModel.class);
                    if (chat.getReceiver().equals(fire_user.getUid())&& chat.getSender().equals(userid)){
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("isseen",true);
                        snapshot.getRef().updateChildren(hashMap);


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendMessage(String sender, final String receiver, String message){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen",false);
        reference.child("Chats").push().setValue(hashMap);

        //add users to chat Fragment

        final DatabaseReference chatRef= FirebaseDatabase.getInstance().getReference("ChatList")
                .child(fire_user.getUid())
                .child(userid);
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(userid);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final  String msg=message;
       reference=FirebaseDatabase.getInstance().getReference("Users").child(fire_user.getUid());
       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (notify) {
                   UserModel user = dataSnapshot.getValue(UserModel.class);

                   sendNotification(receiver, user.getUsername(), msg);
               }
               notify=false;

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });


    }
    private  void sendNotification(String receiver, final String username, final String message){
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Token token=snapshot.getValue(Token.class);
                    Data data=new Data(fire_user.getUid(),R.drawable.person,username +":"+ message,"New Message"
                            ,username);
                    Sender sender=new Sender(data,token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if ( response.code()==200){
                                        if (response.body().success !=1){
                                            Toast.makeText(Message.this, "failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private  void readMessage(final String myid, final String userid, final String imageurl) {
        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatModel chatModel = snapshot.getValue(ChatModel.class);
                    if (chatModel.getReceiver().equals(myid) && chatModel.getSender().equals(userid) || chatModel.getReceiver().equals(userid) && chatModel.getSender().equals(myid)) {
                        mChat.add(chatModel);

                    }
                    messageAdapter = new MessageAdapter(Message.this, mChat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private  void  status(String status){
        reference=FirebaseDatabase.getInstance().getReference("Users").child(fire_user.getUid());
        HashMap<String,Object> hashMap= new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);

    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
    }
}

