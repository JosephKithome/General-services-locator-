package com.example.sejjoh.gsls;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

public class  StartActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser !=null) {
            Intent mainIntent= new Intent(StartActivity.this,MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_start);
        MaterialButton button=findViewById(R.id.btnLogin);
        MaterialButton button1=findViewById(R.id.register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent=new Intent(StartActivity.this,LoginActivity.class);
                startActivity(registerIntent);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent=new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(regIntent);
            }
        });

    }
}
