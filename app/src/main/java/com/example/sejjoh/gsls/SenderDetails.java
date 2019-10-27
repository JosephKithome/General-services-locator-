package com.example.sejjoh.gsls;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class SenderDetails extends AppCompatActivity {
    ImageView senderimage;
    TextView sendername;
    TextView senderemail;
    TextView senderphone;
    DatabaseReference senderreference;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender_details);

        senderimage = findViewById(R.id.SenderImage);
        sendername = findViewById(R.id.SenderName);
        senderemail = findViewById(R.id.SenderEmail);
        senderphone = findViewById(R.id.SenderPhone);



        intent = getIntent();
        final String userid = intent.getStringExtra("Upuserid");
        senderreference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        senderreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final saveData addData = dataSnapshot.getValue(saveData.class);

                assert addData != null;
                sendername.setText("Name:" + " " +addData.getUpname());
                senderemail.setText("Email:" + " " +addData.getUpcurrentuser());
                senderphone.setText("Phone:" + " " +addData.getUpphone());

                Picasso.with(getApplicationContext())
                        .load(addData.getImageURL())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(senderimage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(getApplicationContext()).load(addData.getImageURL())
                                        .into(senderimage);
                            }
                        });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
