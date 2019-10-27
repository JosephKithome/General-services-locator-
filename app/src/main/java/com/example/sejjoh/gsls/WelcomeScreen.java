package com.example.sejjoh.gsls;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);




        ImageView imageView=(ImageView)findViewById(R.id.rotate);
                TextView textView=(TextView)findViewById(R.id.gsls);
                TextView textView1=(TextView)findViewById(R.id.start);
                Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
                imageView.startAnimation(animation);
                Animation animation1= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.downanime);
                textView.startAnimation(animation1);

                Thread timer=new Thread(){
                    public void run(){
                        try {
                            sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finally {
                            startHomepage();
                        }

                    }
                };
                timer.start();
            }

    private void startHomepage() {
        Intent welcome= new Intent(WelcomeScreen.this,StartActivity.class);
        startActivity(welcome);
    }
}
