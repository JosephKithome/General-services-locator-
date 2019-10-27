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
import com.example.sejjoh.gsls.models.CompModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class Compdetails extends AppCompatActivity {
    String  Computers;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compdetails);


            mdatabase=FirebaseDatabase.getInstance();
            Mreference=mdatabase.getReference().child("CompPosts");
            mImageView=(ImageView)findViewById(R.id.service_image);
            MAiportname=(TextView)findViewById(R.id.post_description);
            mCheckin=(TextView)findViewById(R.id.chkin);
            mAmenities=(TextView)findViewById(R.id.amenities);
            mcheckout=(TextView)findViewById(R.id.chkout);

            Mdesc=(TextView)findViewById(R.id.post_description);
            mlocation=(TextView)findViewById(R.id.location);
            mChat=(TextView)findViewById(R.id.chat);
            if(getIntent() !=null)
                Computers=getIntent().getStringExtra("compId");
            if(!Computers.isEmpty())
            {
                getDetailHotel(Computers);
            }
        }

        private void getDetailHotel(final String Computers) {

            Mreference.child(Computers).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    CompModel compModel=dataSnapshot.getValue(CompModel.class);

                    MAiportname.setText(compModel.getComputername());
                    Mdesc.setText(compModel.getCompDesc());
                    mcheckout.setText(compModel.getCheckout());
                    mCheckin.setText(compModel.getCheckin());
                    mAmenities.setText(compModel.getAmenities());


                    Glide.with(getBaseContext()).load(compModel.getCompImage()).into(mImageView);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            mChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent chatIntent=new Intent(Compdetails.this, StartChat.class);
                    startActivity(chatIntent);
                }
            });
            buttonBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent bookingIntent=new Intent(Compdetails.this,Booking.class);
                    bookingIntent.putExtra("HotelId",Computers);
                    startActivity(bookingIntent);
                }
            });
            hotelNear=(TextView)findViewById(R.id.nearby);
            hotelNear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent bookingIntent=new Intent(Compdetails.this,NearBySearch.class);
                    bookingIntent.putExtra("HotelId",Computers);
                    startActivity(bookingIntent);

                }
            });
            Findroute=(TextView)findViewById(R.id.directions);
            Findroute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent FindRouteIntent= new Intent(Compdetails.this,PolyLine.class);
                    startActivity(FindRouteIntent);
                }
            });
            hotelNear=(TextView)findViewById(R.id.nearby);
            hotelNear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent hotelNear=new Intent(Compdetails.this,MapHotels.class);
                    startActivity(hotelNear);
                }
            });
        }

    }