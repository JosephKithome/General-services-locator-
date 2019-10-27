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
import com.example.sejjoh.gsls.models.BankingModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class BankDetails extends AppCompatActivity {
    String  Bank;

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
        setContentView(R.layout.activity_bank_details);
            mdatabase=FirebaseDatabase.getInstance();
            Mreference=mdatabase.getReference().child("Banking");
            mImageView=(ImageView)findViewById(R.id.service_image);
            MAiportname=(TextView)findViewById(R.id.post_description);
            Mdesc=(TextView)findViewById(R.id.post_description);
            mlocation=(TextView)findViewById(R.id.location);
            hotelNear=(TextView)findViewById(R.id.nearby);
            if(getIntent() !=null)
                Bank=getIntent().getStringExtra("bankId");
            if(!Bank.isEmpty())
            {
                getDetailHotel(Bank);
            }
        }

        private void getDetailHotel(final String Bank) {

            Mreference.child(Bank).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    BankingModel bankingModel=dataSnapshot.getValue(BankingModel.class);

                    MAiportname.setText(bankingModel.getBankName());
                    Mdesc.setText(bankingModel.getDescription());

                    Glide.with(getBaseContext()).load(bankingModel.getImage()).into(mImageView);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            mChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent chatIntent=new Intent(BankDetails.this, StartChat.class);
                    startActivity(chatIntent);
                }
            });
            buttonBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent bookingIntent=new Intent(BankDetails.this,Booking.class);
                    bookingIntent.putExtra("HotelId",Bank);
                    startActivity(bookingIntent);
                }
            });
            hotelNear=(TextView)findViewById(R.id.nearby);
            hotelNear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent bookingIntent=new Intent(BankDetails.this,NearBySearch.class);
                    bookingIntent.putExtra("HotelId",Bank);
                    startActivity(bookingIntent);

                }
            });
            Findroute=(TextView)findViewById(R.id.directions);
            Findroute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent FindRouteIntent= new Intent(BankDetails.this,PolyLine.class);
                    startActivity(FindRouteIntent);
                }
            });
            hotelNear=(TextView)findViewById(R.id.nearby);
            hotelNear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent hotelNear=new Intent(BankDetails.this,MapHotels.class);
                    startActivity(hotelNear);
                }
            });

        }

    }