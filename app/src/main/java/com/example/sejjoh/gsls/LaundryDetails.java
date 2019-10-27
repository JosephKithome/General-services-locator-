package com.example.sejjoh.gsls;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sejjoh.gsls.Chat.StartChat;
import com.example.sejjoh.gsls.models.LaundryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LaundryDetails extends AppCompatActivity {
    FirebaseDatabase mdatabase;
    private ImageView mImageView;
    private TextView MAiportname;
    DatabaseReference Mreference;
    private TextView Mdesc;
    private TextView mlocation;
    private TextView mChat;
    private TextView nearbyLaudnry;
    String laundry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_details);

            mdatabase= FirebaseDatabase.getInstance();
            Mreference=mdatabase.getReference().child("Laundry");
            mImageView=(ImageView)findViewById(R.id.service_image);
            MAiportname=(TextView)findViewById(R.id.post_description);
            Mdesc=(TextView)findViewById(R.id.post_description);
            mlocation=(TextView)findViewById(R.id.location);
            mChat=(TextView)findViewById(R.id.chat);
            if(getIntent() !=null)
                laundry=getIntent().getStringExtra("LaundryId");
            if(!laundry.isEmpty())
            {
                getDetailHotel(laundry);
            }
        }

        private void getDetailHotel(String laundry) {

            Mreference.child(laundry).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    LaundryModel LaundryModel=dataSnapshot.getValue(LaundryModel.class);

                    MAiportname.setText(LaundryModel.getLoundryName());
                    Mdesc.setText(LaundryModel.getLoudrydesc());

                    Glide.with(getBaseContext()).load(LaundryModel.getLoudryImage()).into(mImageView);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            mlocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent locationIntent=new Intent(LaundryDetails.this,  GeoLocation.class);
                    startActivity(locationIntent);
                }
            });
            mChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent chatIntent=new Intent(LaundryDetails.this, StartChat.class);
                    startActivity(chatIntent);
                }
            });
            nearbyLaudnry=(TextView)findViewById(R.id.nearby);
            nearbyLaudnry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent nearIntent=new Intent(LaundryDetails.this,NearBySearch.class);
                    startActivity(nearIntent);
                }
            });

        }
}

