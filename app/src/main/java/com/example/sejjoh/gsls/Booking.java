package com.example.sejjoh.gsls;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.example.sejjoh.gsls.models.HotelModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Booking extends AppCompatActivity {
    WebView booksite;
    FirebaseDatabase db;
    DatabaseReference databaseReference;

    String HotelId = "",url;
    HotelModel hotels;

    LinearLayout webvieLayout;
    LinearLayout loadingProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        webvieLayout = (LinearLayout) findViewById(R.id.booksite);
        loadingProgress = (LinearLayout) findViewById(R.id.redirect);
        loadingProgress.setVisibility(View.INVISIBLE);

        //Init firebase
        db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("Hotels");
        booksite = (WebView) findViewById(R.id.webview);

        if (getIntent() != null)HotelId = getIntent().getStringExtra("HotelId");
        if(!HotelId.isEmpty())
            loadwebsite(HotelId);

        booksite.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageFinished(WebView view, String url) {


                // Page loading finished
                showView(webvieLayout);//hide web
                hideView(loadingProgress);//show progress loading layout
            }
        });


    }

    private void loadwebsite(String hotelId) {
        WebSettings webSettings = booksite.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        booksite.getSettings().getLoadsImagesAutomatically();
        databaseReference.child(HotelId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                hotels = dataSnapshot.getValue(HotelModel.class);
                url=hotels.getWebsite();
                booksite.loadUrl(url);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void showView (View...views){
            for (View v : views) {
                v.setVisibility(View.VISIBLE);
            }
        }
        private void hideView (View...views){
            for (View v : views) {
                v.setVisibility(View.INVISIBLE);
            }
        }
    }

