package com.example.sejjoh.gsls;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.example.sejjoh.gsls.models.AirportModel;
import com.example.sejjoh.gsls.models.HotelModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookFlights extends AppCompatActivity {
    WebView booksite;
    FirebaseDatabase db;
    DatabaseReference databaseReference;

    String AirportId = "",url;
    AirportModel Airport;

    LinearLayout webvieLayout;
    LinearLayout loadingProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_flights);

        webvieLayout = (LinearLayout) findViewById(R.id.booksite);
        loadingProgress = (LinearLayout) findViewById(R.id.redirect);
        loadingProgress.setVisibility(View.INVISIBLE);

        //Init firebase
        db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("Aiport");
        booksite = (WebView) findViewById(R.id.webview);

        if (getIntent() != null)AirportId = getIntent().getStringExtra("airportId");
        if(!AirportId.isEmpty())
            loadwebsite(AirportId);

        booksite.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageFinished(WebView view, String url) {


                // Page loading finished
                showView(webvieLayout);//hide web
                hideView(loadingProgress);//show progress loading layout
            }
        });



    }

    private void loadwebsite(String airportId) {
        WebSettings webSettings = booksite.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        booksite.getSettings().getLoadsImagesAutomatically();
        databaseReference.child(AirportId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Airport = dataSnapshot.getValue(AirportModel.class);
                url=Airport.getWebsite();
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

