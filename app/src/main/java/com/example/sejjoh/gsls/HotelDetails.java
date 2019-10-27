package com.example.sejjoh.gsls;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sejjoh.gsls.Chat.StartChat;
import com.example.sejjoh.gsls.models.HotelModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class HotelDetails extends AppCompatActivity implements OnMapReadyCallback {
    String  hotel;
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

    GoogleMap mMap;
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_details);

                mdatabase=FirebaseDatabase.getInstance();
                Mreference=mdatabase.getReference().child("Hotels");
                mImageView=(ImageView)findViewById(R.id.service_image);
                MAiportname=(TextView)findViewById(R.id.post_description);
                mCheckin=(TextView)findViewById(R.id.chkin);
                mAmenities=(TextView)findViewById(R.id.amenities);
                mcheckout=(TextView)findViewById(R.id.chkout);
                Mdesc=(TextView)findViewById(R.id.post_description);
                buttonBook=(Button)findViewById(R.id.booking);
                mlocation=(TextView)findViewById(R.id.location);
                locationAddress=(TextView)findViewById(R.id.location_addres);
                mChat=(TextView)findViewById(R.id.chat);
                mWebView=(WebView)findViewById(R.id.webview);
                if(getIntent() !=null)
                    hotel=getIntent().getStringExtra("HotelId");
                if(!hotel.isEmpty())
                {
                    getDetailHotel(hotel);
                }
            }

            private void getDetailHotel(final String hotel) {

                Mreference.child(hotel).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HotelModel hotelModel=dataSnapshot.getValue(HotelModel.class);

                        MAiportname.setText(hotelModel.getHotelName());
                        Mdesc.setText(hotelModel.getDescription());
                        locationAddress.setText(hotelModel.getLocation());
                        mcheckout.setText(hotelModel.getCheckout());
                        mCheckin.setText(hotelModel.getCheckin());
                        mAmenities.setText(hotelModel.getAmenities());

                        Glide.with(getBaseContext()).load(hotelModel.getImage()).into(mImageView);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                mlocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent locationIntent=new Intent(HotelDetails.this, GeoLocation.class);
                        startActivity(locationIntent);
                    }
                });
                mChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent chatIntent=new Intent(HotelDetails.this, StartChat.class);
                        startActivity(chatIntent);
                    }
                });
                buttonBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent bookingIntent=new Intent(HotelDetails.this,Booking.class);
                        bookingIntent.putExtra("HotelId",hotel);
                        startActivity(bookingIntent);
                    }
                });
                hotelNear=(TextView)findViewById(R.id.nearby);
                hotelNear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent bookingIntent=new Intent(HotelDetails.this,NearBySearch.class);
                        bookingIntent.putExtra("HotelId",hotel);
                        startActivity(bookingIntent);

                    }
                });
                Findroute=(TextView)findViewById(R.id.directions);
                Findroute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent FindRouteIntent= new Intent(HotelDetails.this,PolyLine.class);
                        startActivity(FindRouteIntent);
                    }
                });
                mNearby=(TextView)findViewById(R.id.nearby);
                mNearby.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent hotelNear=new Intent(HotelDetails.this,MapHotels.class);
                        startActivity(hotelNear);
                    }
                });

            }
            @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap=googleMap;

        LatLng latLng=new LatLng(1.455455,36.45545);

                if (isServicesOK()){
                    init();
                }
            }
    private void init(){

    }
    public boolean isServicesOK(){
        Log.d(TAG,"isServicesOK:checking google services version");
        int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(HotelDetails.this);
        if (available== ConnectionResult.SUCCESS){
            //everything is okay and the user can make map requests
            Log.d(TAG,"isServicesOK:Google play services is working");
            return true;

        }else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occurred but we can fix it
            Log.d(TAG,"isServicesOK:an error occurred but we can fix it");
            Dialog dialog=GoogleApiAvailability.getInstance().getErrorDialog(HotelDetails.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();

        }else {
            Toast.makeText(this, "You cant make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


}
