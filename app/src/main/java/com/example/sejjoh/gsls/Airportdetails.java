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
import com.example.sejjoh.gsls.models.AirportModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class Airportdetails extends AppCompatActivity {

    String  Airport;

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
    private  TextView mNearby;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airportdetails);
        mdatabase=FirebaseDatabase.getInstance();
        Mreference=mdatabase.getReference().child("Aiport");
        mImageView=(ImageView)findViewById(R.id.service_image);
        MAiportname=(TextView)findViewById(R.id.post_description);
        Mdesc=(TextView)findViewById(R.id.post_description);
        mlocation=(TextView)findViewById(R.id.location);
        buttonBook=(Button)findViewById(R.id.booking);

        mChat=(TextView)findViewById(R.id.chat);
        if(getIntent() !=null)
            Airport=getIntent().getStringExtra("airportId");
        if(!Airport.isEmpty())
        {
            getDetailHotel(Airport);
        }
    }

    private void getDetailHotel(String airport) {

        Mreference.child(Airport).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AirportModel airportModel=dataSnapshot.getValue(AirportModel.class);

                MAiportname.setText(airportModel.getAiport());
                Mdesc.setText(airportModel.getDescription());

                Glide.with(getBaseContext()).load(airportModel.getImage()).into(mImageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent locationIntent=new Intent(Airportdetails.this, GeoLocation.class);
                startActivity(locationIntent);
            }
        });
        mChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent=new Intent(Airportdetails.this, StartChat.class);
                startActivity(chatIntent);
            }
        });
        buttonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookingIntent=new Intent(Airportdetails.this,Booking.class);
                bookingIntent.putExtra("HotelId",Airport);
                startActivity(bookingIntent);
            }
        });
        hotelNear=(TextView)findViewById(R.id.nearby);
        hotelNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookingIntent=new Intent(Airportdetails.this,NearBySearch.class);
                bookingIntent.putExtra("HotelId",Airport);
                startActivity(bookingIntent);

            }
        });
        Findroute=(TextView)findViewById(R.id.directions);
        Findroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent FindRouteIntent= new Intent(Airportdetails.this,PolyLine.class);
                startActivity(FindRouteIntent);
            }
        });
        mNearby=(TextView)findViewById(R.id.nearby);
        mNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hotelNear=new Intent(Airportdetails.this,MapHotels.class);
                startActivity(hotelNear);
            }
        });

    }

}