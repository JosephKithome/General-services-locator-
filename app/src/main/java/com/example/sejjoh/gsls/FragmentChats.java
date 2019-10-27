package com.example.sejjoh.gsls;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.sejjoh.gsls.Adapters.UserAdapter;
import com.example.sejjoh.gsls.Interface.ItemClickListener;
import com.example.sejjoh.gsls.Notifications.Token;
import com.example.sejjoh.gsls.models.AirportModel;
import com.example.sejjoh.gsls.models.ChatList;
import com.example.sejjoh.gsls.models.ChatModel;
import com.example.sejjoh.gsls.models.UserModel;
import com.example.sejjoh.gsls.viewHolder.ViewHolderService;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

@SuppressLint("ValidFragment")
public class FragmentChats extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<UserModel> mUsers;
    FirebaseUser fire_user;
    DatabaseReference reference;
    private List<ChatList>usersList;

    int color;

    @SuppressLint("ValidFragment")
    public FragmentChats(int color) {
        this.color = color;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_fragment_chats, container, false);
        recyclerView=view.findViewById(R.id.Messagerecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fire_user= FirebaseAuth.getInstance().getCurrentUser();
        usersList= new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("ChatList").child(fire_user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    ChatList chatList=snapshot.getValue(ChatList.class);
                    usersList.add(chatList);


                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

        return view;
    }
    private  void updateToken(String token){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(fire_user.getUid()).setValue(token1);
    }

    private void chatList() {
        mUsers= new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    UserModel user=snapshot.getValue(UserModel.class);
                    for (ChatList chatList: usersList){
                        if (user.getId().equals(chatList.getId())){
                            mUsers.add(user);
                        }
                    }
                }
                userAdapter=new UserAdapter(getContext(),mUsers,true);
                recyclerView.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void readChats(){

    }
    private  void  status(String status){
        reference=FirebaseDatabase.getInstance().getReference("Users").child(fire_user.getUid());
        HashMap<String,Object> hashMap= new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);

    }

    @Override
    public void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    public void onPause() {
        super.onPause();
        status("offline");
    }
}

