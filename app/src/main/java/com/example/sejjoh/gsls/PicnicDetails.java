package com.example.sejjoh.gsls;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sejjoh.gsls.models.PicnicModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class PicnicDetails extends AppCompatActivity {
    String  Picnic;

    FirebaseDatabase mdatabase;
    private ImageView mImageView;
    private TextView MAiportname;
    DatabaseReference Mreference;
    private TextView Mdesc;
    private TextView mlocation;
    private TextView mNearby;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picnic_details);



            mdatabase=FirebaseDatabase.getInstance();
            Mreference=mdatabase.getReference().child("Picnic_sites");
            mImageView=(ImageView)findViewById(R.id.service_image);
            MAiportname=(TextView)findViewById(R.id.post_description);
            Mdesc=(TextView)findViewById(R.id.post_description);
            mlocation=(TextView)findViewById(R.id.location);
            mNearby=(TextView)findViewById(R.id.nearby);
            if(getIntent() !=null)
                Picnic=getIntent().getStringExtra("picnicId");
            if(!Picnic.isEmpty())
            {
                getDetailHotel(Picnic);
            }
        }

        private void getDetailHotel(String Picnic) {

            Mreference.child(Picnic).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    PicnicModel picnicModel=dataSnapshot.getValue(PicnicModel.class);

                    MAiportname.setText(picnicModel.getPicnicName());
                    Mdesc.setText(picnicModel.getDescription());

                    Glide.with(getBaseContext()).load(picnicModel.getImage()).into(mImageView);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            mlocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent locationIntent=new Intent(PicnicDetails.this,  GeoLocation.class);
                    startActivity(locationIntent);
                }
            });
            mNearby.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent chatIntent=new Intent(PicnicDetails.this, NearBySearch.class);
                    startActivity(chatIntent);
                }
            });

        }

    }