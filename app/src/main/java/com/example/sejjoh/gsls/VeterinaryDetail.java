package com.example.sejjoh.gsls;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sejjoh.gsls.models.VeterinaryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class VeterinaryDetail extends AppCompatActivity {
    FirebaseDatabase mdatabase;
    private ImageView mImageView;
    private TextView MAiportname;
    DatabaseReference Mreference;
    private TextView Mdesc;
    private TextView mlocation;
    private TextView mChat;
    String veterinary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veterinary);

            mdatabase= FirebaseDatabase.getInstance();
            Mreference=mdatabase.getReference().child("Veterinary");
            mImageView=(ImageView)findViewById(R.id.service_image);
            MAiportname=(TextView)findViewById(R.id.post_description);
            Mdesc=(TextView)findViewById(R.id.post_description);
            mlocation=(TextView)findViewById(R.id.location);
            mChat=(TextView)findViewById(R.id.chat);
            if(getIntent() !=null)
                veterinary=getIntent().getStringExtra("vetId");
            if(!veterinary.isEmpty())
            {
                getDetailHotel(veterinary);
            }
        }

        private void getDetailHotel(String veterinary) {

            Mreference.child(veterinary).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    VeterinaryModel VeterinaryModel=dataSnapshot.getValue(VeterinaryModel.class);

                    MAiportname.setText(VeterinaryModel.getVetName());
                    Mdesc.setText(VeterinaryModel.getVetDesc());

                    Glide.with(getBaseContext()).load(VeterinaryModel.getVetImage()).into(mImageView);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            mlocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent locationIntent=new Intent(VeterinaryDetail.this, GeoLocation.class);
                    startActivity(locationIntent);
                }
            });
            mChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent chatIntent=new Intent(VeterinaryDetail.this,  GeoLocation.class);
                    startActivity(chatIntent);
                }
            });

        }

    }