package com.example.sejjoh.gsls;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sejjoh.gsls.models.MechanicModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MechanicDetails extends AppCompatActivity {

    FirebaseDatabase mdatabase;
    private ImageView mImageView;
    private TextView MAiportname;
    DatabaseReference Mreference;
    private TextView Mdesc;
    private TextView mlocation;
    private TextView mChat;
    String Mechanic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_details);

                mdatabase= FirebaseDatabase.getInstance();
                Mreference=mdatabase.getReference().child("Mechanic");
                mImageView=(ImageView)findViewById(R.id.service_image);
                MAiportname=(TextView)findViewById(R.id.post_description);
                Mdesc=(TextView)findViewById(R.id.post_description);
                mlocation=(TextView)findViewById(R.id.location);
                mChat=(TextView)findViewById(R.id.chat);
                if(getIntent() !=null)
                    Mechanic=getIntent().getStringExtra("mechanicId");
                if(!Mechanic.isEmpty())
                {
                    getDetailHotel(Mechanic);
                }
            }

            private void getDetailHotel(String Mechanic) {

                Mreference.child(Mechanic).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        MechanicModel GymModel=dataSnapshot.getValue(MechanicModel.class);

                        MAiportname.setText(GymModel.getMechanicTitle());
                        Mdesc.setText(GymModel.getDescription());

                        Glide.with(getBaseContext()).load(GymModel.getImage()).into(mImageView);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                mlocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent locationIntent=new Intent(MechanicDetails.this,  GeoLocation.class);
                        startActivity(locationIntent);
                    }
                });
                mChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent chatIntent=new Intent(MechanicDetails.this,  GeoLocation.class);
                        startActivity(chatIntent);
                    }
                });

            }

        }