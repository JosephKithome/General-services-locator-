package com.example.sejjoh.gsls;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sejjoh.gsls.Model.MyPlaces;
import com.example.sejjoh.gsls.Model.Results;
import com.example.sejjoh.gsls.Remote.Common;
import com.example.sejjoh.gsls.Remote.IGoogleAPIService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeoLocation extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    LocationRequest mLocationRequest = new LocationRequest();

    private static final String TAG = "GeoLocation";

    public static final int REQUEST_CHECK_SETTINGS = 13;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private boolean mLocationPermissionGranted = false;

    private double latitude, longitude;

    IGoogleAPIService mService;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private static final float DEFAULT_ZOOM = 10f;


    private GoogleMap mMap;
    private int normal=1;



    private FusedLocationProviderClient mFusedLocationProviderClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);


        mService = Common.getGoogleAPIService();

        if (!isConnected(GeoLocation.this)) builder(GeoLocation.this).show();
        else {


        }



        ImageView locationback = findViewById(R.id.locationback);
        locationback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final EditText mSearchText = findViewById(R.id.input_search);

        final ImageView view = findViewById(R.id.ic_magnify);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClickedItem();

                mSearchText.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });
        ImageView mGps = findViewById(R.id.gpsimg);
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();

                mSearchText.onEditorAction(EditorInfo.IME_ACTION_DONE);

            }
        });

        getLocationPermission();

        BottomNavigationView.OnNavigationItemSelectedListener listener=new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.account:
                        nearByPlace("market");
                        break;
                    case R.id.home:
                        nearByPlace("restaurant");
                        break;
                    case R.id.enquire:
                        nearByPlace("school");
                        break;
                    default:
                        break;


                }
                return true;
            }


        };
    }
    private void nearByPlace(final String placeType) {
        mMap.clear();
        String url = getUrl(latitude,longitude,placeType);

        mService.getNearByPlaces(url)
                .enqueue(new Callback<MyPlaces>() {
                    @Override
                    public void onResponse(Call<MyPlaces> call, Response<MyPlaces> response) {

                        if (response.isSuccessful())
                        {
                            for (int i=0;i<response.body().getResults().length;i++)
                            {
                                MarkerOptions markerOptions = new MarkerOptions();
                                Results googlePlace = response.body().getResults()[i];
                                double lat = Double.parseDouble(googlePlace.getGeometry().getLocation().getLat());
                                double lng = Double.parseDouble(googlePlace.getGeometry().getLocation().getLng());
                                String placeName = googlePlace.getName();
                                String vicinity = googlePlace.getVicinity();
                                LatLng latLng = new LatLng(lat,lng);
                                markerOptions.position(latLng);
                                markerOptions.title(placeName);
                                if (placeType.equals("market"))
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                else
                                if (placeType.equals("restaurant"))
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                                else
                                if (placeType.equals("school"))
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                                else
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                                mMap.addMarker(markerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyPlaces> call, Throwable t) {

                    }
                });
    }

    private String getUrl(double latitude, double longitude, String placeType) {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+10000);
        googlePlaceUrl.append("&type="+placeType);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+getResources().getString(R.string.browser_key));
        Log.d("getUrl",googlePlaceUrl.toString());
        return googlePlaceUrl.toString();
    }


    @Override
    protected void onStart() {
        super.onStart();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {

            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {

                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(GeoLocation.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    public  boolean isConnected (Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        }
        else
            return false;
    }

    public AlertDialog.Builder builder (Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("No Internet Connection...");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                finish();

            }
        });
        return builder;
    }


    public void getClickedItem(){
        EditText mSearchText = findViewById(R.id.input_search);

        String address = mSearchText.getText().toString();

        List<Address> addresslist = null;

        MarkerOptions usermarkerOptions = new MarkerOptions();

        if (!TextUtils.isEmpty(address)){
            Log.d(TAG, "geoLocate: geolocating");

            Geocoder geocoder = new Geocoder(GeoLocation.this);

            try {
                addresslist = geocoder.getFromLocationName(address, 6);

                if (addresslist != null){
                    mMap.clear();
                    for (int i=0; i<addresslist.size(); i++){
                        Address userAddress = addresslist.get(i);

                        LatLng latLng = new LatLng(userAddress.getLatitude(), userAddress.getLongitude());

                        usermarkerOptions.position(latLng);
                        usermarkerOptions.title(address);
                        usermarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                        mMap.addMarker(usermarkerOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18),  10000, null);
                        mMap.getUiSettings().setMapToolbarEnabled(false);

                    }
                }
                else {
                    Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
                }
            }
            catch (IOException e){
                Log.e(TAG, "geoLocate: IOException:" + e.getMessage());
                e.printStackTrace();
            }


        }
        else {
            Toast.makeText(this, "Please input location name", Toast.LENGTH_SHORT).show();
        }
    }


    public void onClick(View view){
        ImageView imageView = findViewById(R.id.satelite);
        ImageView image2 = findViewById(R.id.gpsimg);
        switch (view.getId()){
            case R.id.satelite:
                if(normal==1){
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    imageView.setImageResource(R.drawable.ic_settings_black_24dp);
                    image2.setImageResource(R.drawable.ic_gps_fixed_black_24dp);

                    normal++;
                }
                else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    normal=1;
                    imageView.setImageResource(R.drawable.ic_satellite_black_24dp);
                    image2.setImageResource(R.drawable.ic_gps_fixed_black_24dp);
                }

                break;
        }

    }






    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the current device location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);



        try {
            if (mLocationPermissionGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            if (currentLocation != null) {

                                moveCamera(new LatLng( currentLocation.getLatitude(), currentLocation.getLongitude()),
                                        DEFAULT_ZOOM, "My Location");
                            }

                        } else {
                            Log.d(TAG, "onComplete: current location is null!");
                            Toast.makeText(GeoLocation.this, "Unable to get current location", Toast.LENGTH_SHORT).show();

                        }                    }
                });
            }
        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: securityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latlng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latlng.latitude + ", lng: " + latlng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

        if (!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions()
                    .position(latlng)
                    .title(title);
            mMap.addMarker(options);
        }



    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "onMapReady: map is ready");
                mMap = googleMap;

                if (mLocationPermissionGranted) {
                    getDeviceLocation();

                    if (ActivityCompat.checkSelfPermission(GeoLocation.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GeoLocation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);

                }
            }
        });
    }



    public void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String [] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionGranted = false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if (grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed.");
                            return;

                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted.");
                    mLocationPermissionGranted = true;
                    initMap();

                }
            }
        }
    }

    public void onBackPressed() {


        super.onBackPressed();
    }


}
