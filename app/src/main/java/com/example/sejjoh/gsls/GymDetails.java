package com.example.sejjoh.gsls;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sejjoh.gsls.Chat.StartChat;
import com.example.sejjoh.gsls.Maps.MapGym;
import com.example.sejjoh.gsls.models.GymModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class GymDetails extends AppCompatActivity {

    FirebaseDatabase mdatabase;
    private ImageView mImageView;
    private TextView MAiportname;
    DatabaseReference Mreference;
    private TextView Mdesc;
    private TextView mlocation;
    private TextView mChat;
    private Fragment fragmentMap;
    private Button buttonBook;
    private WebView mWebView;
    private TextView locationAddress;
    private TextView mCheckin;
    private TextView mcheckout;
    private TextView mAmenities;
    private TextView hotelNear;
    private  TextView Findroute;
    String gym;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_details);
        mdatabase= FirebaseDatabase.getInstance();
        Mreference=mdatabase.getReference().child("Gym");
        mImageView=(ImageView)findViewById(R.id.service_image);
        Mdesc=(TextView)findViewById(R.id.post_description);
        mlocation=(TextView)findViewById(R.id.location);
        hotelNear=(TextView)findViewById(R.id.nearby) ;
        mChat=(TextView)findViewById(R.id.chat);
        toolbar=findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);

        toolbar.setTitle("service");
        if(getIntent() !=null)
            gym=getIntent().getStringExtra("GymId");
        if(!gym.isEmpty())
        {
            getDetailHotel(gym);
        }
    }

    private void getDetailHotel(final String gym) {

        Mreference.child(gym).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GymModel GymModel=dataSnapshot.getValue(GymModel.class);
                Mdesc.setText(GymModel.getGymdescription());
               //toolbar.setTitle(GymModel.getGymName());

                Glide.with(getBaseContext()).load(GymModel.getGymimage()).into(mImageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent=new Intent(GymDetails.this, StartChat.class);
                startActivity(chatIntent);
            }
        });
        buttonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookingIntent=new Intent(GymDetails.this,Booking.class);
                bookingIntent.putExtra("HotelId",gym);
                startActivity(bookingIntent);
            }
        });
        hotelNear=(TextView)findViewById(R.id.nearby);
        hotelNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookingIntent=new Intent(GymDetails.this,NearBySearch.class);
                bookingIntent.putExtra("HotelId",gym);
                startActivity(bookingIntent);

            }
        });
        Findroute=(TextView)findViewById(R.id.directions);
        Findroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent FindRouteIntent= new Intent(GymDetails.this,PolyLine.class);
                startActivity(FindRouteIntent);
            }
        });
        hotelNear=(TextView)findViewById(R.id.nearby);
        hotelNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hotelNear=new Intent(GymDetails.this,MapHotels.class);
                startActivity(hotelNear);
            }
        });
    }

}