package com.example.sejjoh.gsls;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sejjoh.gsls.Chat.StartChat;
import com.example.sejjoh.gsls.models.UserModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tablayout;
    Toolbar toolbar;
   private TextView usename;
    CircleImageView circleImageViewUser;
    BottomNavigationView bottomNavigationMenuView;
    private FirebaseAuth mAuth;
    private DatabaseReference mdatabaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    FirebaseUser firebaseUser;
    DatabaseReference mdatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
////        bottomNavigationMenuView=(BottomNavigationView)findViewById(R.id.bottom_nav);
//        bottomNavigationMenuView.setOnNavigationItemSelectedListener(listener);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        circleImageViewUser=(CircleImageView)findViewById(R.id.profile);
        usename=(TextView)findViewById(R.id.username);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        mdatabase= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel user=dataSnapshot.getValue(UserModel.class);
             usename.setText(user.getUsername());
                if (user.getImageUrl().equals("default")){
                    circleImageViewUser.setImageResource(R.drawable.person);
                }else
                {
                    Glide.with(getApplicationContext()).load(user.getImageUrl()).into(circleImageViewUser);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //create and set ViewPager adapter
        setupViewPager(viewPager);
        tablayout.setupWithViewPager(viewPager);

        //change selected tab when viewpager changed page
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));

        //change viewpager page when tab selected
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:

                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//    }
//
//    BottomNavigationView.OnNavigationItemSelectedListener listener=new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//
//            int id=menuItem.getItemId();
//            if (id==R.id.account){
//                startActivity(new Intent(MainActivity.this,FragmentProfile.class));
//
//            }else  if(id==R.id.home){
//                startActivity(new Intent(MainActivity.this,MainActivity.class));
//
//            }else if (id==R.id.enquire){
//                startActivity(new Intent(MainActivity.this, StartChat.class));
//
//            }else  if (id==R.id.location){
//                startActivity(new Intent(MainActivity.this,NearBySearch.class));
//            }
//
//            return true;
//        }
    };

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentGym(
                ContextCompat.getColor(this, R.color.white)), "Gym services");
        adapter.addFrag(new FragmentMechanics(
                ContextCompat.getColor(this, R.color.white)), "Mechanic Areas");
        adapter.addFrag(new FragmentLaundry(
                ContextCompat.getColor(this, R.color.white)), "Laundry services");
        adapter.addFrag(new FragmentPicnics(
                ContextCompat.getColor(this, R.color.white)), "Picnic sites");
        adapter.addFrag(new FragmentAirport(
                ContextCompat.getColor(this, R.color.white)), "Airport services");
        adapter.addFrag(new FragmentBank(
                ContextCompat.getColor(this, R.color.white)), "Bank services");
        adapter.addFrag(new FragmentVeterinaryServices(
                ContextCompat.getColor(this, R.color.white)), "Veterinary services");
        adapter.addFrag(new FragmentHotel(
                ContextCompat.getColor(this, R.color.white)), "Hotel services");
        adapter.addFrag(new FragmentHotel(
                ContextCompat.getColor(this, R.color.white)), "Computer Services");
        adapter.addFrag(new FragmentHotel(
                ContextCompat.getColor(this, R.color.white)), "HairDressing services");
        viewPager.setAdapter(adapter);
        if (isServicesOK()){
            init();
        }
    }
    private void init(){

    }
    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK:checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            //everything is okay and the user can make map requests
            Log.d(TAG, "isServicesOK:Google play services is working");
            return true;

        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occurred but we can fix it
            Log.d(TAG, "isServicesOK:an error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();

        } else {
            Toast.makeText(this, "You cant make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_Logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;

        }
        return false;
    }
    private  void  status(String status){
        mdatabase=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap= new HashMap<>();
        hashMap.put("status",status);
        mdatabase.updateChildren(hashMap);

    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}
