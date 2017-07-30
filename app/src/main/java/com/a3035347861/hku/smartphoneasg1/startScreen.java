package com.a3035347861.hku.smartphoneasg1;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * Created by SHI Zhongqi on 2016-10-08.
 * For Smart Phone APP Assignment 1`
 */

public class startScreen extends AppCompatActivity {

    TextView textView_startGame;
    ImageView imageView_startGame;
    Button btn_next;
    gameActivityManager actManager;
    MediaPlayer mp_drop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        actManager= gameActivityManager.getInstance();
        mp_drop = MediaPlayer.create(this, R.raw.up);
        setContentView(R.layout.activity_start_screen);
        textView_startGame=(TextView)findViewById(R.id.startscreen_startgame);
        imageView_startGame=(ImageView) findViewById(R.id.startscreen_imageView);
        btn_next=(Button) findViewById(R.id.startscreen_button);
        Animation anim_text = new AlphaAnimation(0.0f, 1.0f);
        anim_text.setDuration(1000); //You can manage the blinking time with this parameter
        anim_text.setStartOffset(20);
        anim_text.setRepeatMode(Animation.REVERSE);
        anim_text.setRepeatCount(Animation.INFINITE);
        textView_startGame.startAnimation(anim_text);
        btn_next.setAlpha(0.0f);
        btn_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                mp_drop.start();
                Intent intent=new Intent(startScreen.this,mainGame.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        actManager.addActivity(this);

        new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY(); // get Touch Coordinate
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        };
    }
}


