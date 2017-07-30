package com.a3035347861.hku.smartphoneasg1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Shi Zhongqi on 2016-10-08.
 * For Smart Phone APP Assignment 1
 */

public class mainGame extends AppCompatActivity {
    private gameActivityManager actManager;
    private gameLogic gLogic;//game logic inside
    private ImageView game_imageview_player1_win;
    private ImageView game_imageview_player2_win;
    private ImageView game_imageview_draw;
    private ImageButton[] game_img_btn_column=new ImageButton[7];
    private TextView game_tv_player1,game_tv_player2;
    private ImageView game_iv_player1,game_iv_player2;
    private TextView game_tv_player1_win,game_tv_player2_win;
    Button btn_retract, btn_restart;
    private RelativeLayout[] game_relativelayout_column=new RelativeLayout[7];
    private SparseArray<ImageView> map_keys;
    private boolean gameFinished=false;
    private boolean animationFinished=true;
    private int keyid=1;
    private int turn;
    private int winner=0;
    private int[][] int_corridnate=new int[7][6];
    private int[] lastCoordinate=new int[2];
    private int player1_win,player2_win;
    private SharedPreferences game_sp;
    private generalAnimationListener gameAnimationListener;
    private playKeyAnimationListener KeyAnimationListner;
    private SoundPool gameSoundEffect;
    private AudioManager audioManager;
    private int priority;
    private int no_loop;
    private float normal_playback_rate;
    private HashMap<String,Integer> soundEffectMap;
    MediaPlayer mp_bgm;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actManager= gameActivityManager.getInstance();
        gLogic=gameLogic.getInstance();
        actManager.addActivity(this);
        setContentView(R.layout.activity_game);
        map_keys=new SparseArray<>();
        //Integer
        no_loop = 0;
        priority = 1;
        normal_playback_rate = 1f;
        //sound effect
        audioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        gameSoundEffect = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
        soundEffectMap = new HashMap<>();
        soundEffectMap.put("drop",gameSoundEffect.load(this,R.raw.drop,1));
        soundEffectMap.put("button",gameSoundEffect.load(this,R.raw.button,1));
        soundEffectMap.put("win",gameSoundEffect.load(this,R.raw.win,1));

        mp_bgm = MediaPlayer.create(this, R.raw.bgm);
        //player vs player part
        game_tv_player1=(TextView) findViewById(R.id.game_textview_player1);
        game_tv_player2=(TextView) findViewById(R.id.game_textview_player2);
        game_iv_player1=(ImageView)findViewById(R.id.game_imageview_player1);
        game_iv_player2=(ImageView)findViewById(R.id.game_imageview_player2);
        //result imageview part
        game_imageview_player1_win= (ImageView) findViewById(R.id.game_imageview_player1_win);
        game_imageview_player2_win= (ImageView) findViewById(R.id.game_imageview_player2_win);
        game_imageview_draw=(ImageView)findViewById(R.id.game_imageview_draw);
        game_imageview_player1_win.setVisibility(View.GONE);
        game_imageview_player2_win.setVisibility(View.GONE);
        game_imageview_draw.setVisibility(View.GONE);
        //result imageview part finish
        //gameboard imagebutton part
        game_img_btn_column[0]=(ImageButton)findViewById(R.id.game_imgbutton_list1);
        game_img_btn_column[1]=(ImageButton)findViewById(R.id.game_imgbutton_list2);
        game_img_btn_column[2]=(ImageButton)findViewById(R.id.game_imgbutton_list3);
        game_img_btn_column[3]=(ImageButton)findViewById(R.id.game_imgbutton_list4);
        game_img_btn_column[4]=(ImageButton)findViewById(R.id.game_imgbutton_list5);
        game_img_btn_column[5]=(ImageButton)findViewById(R.id.game_imgbutton_list6);
        game_img_btn_column[6]=(ImageButton)findViewById(R.id.game_imgbutton_list7);
        for(int i=0;i<7;i++){
            game_img_btn_column[i].setOnClickListener(new btn_listener_column(i));
        }
        //gameboard imagebutton part finish
        //gameboard relative layout for animation part
        game_relativelayout_column[0]=(RelativeLayout)findViewById(R.id.game_relativelayout_list1);
        game_relativelayout_column[1]=(RelativeLayout)findViewById(R.id.game_relativelayout_list2);
        game_relativelayout_column[2]=(RelativeLayout)findViewById(R.id.game_relativelayout_list3);
        game_relativelayout_column[3]=(RelativeLayout)findViewById(R.id.game_relativelayout_list4);
        game_relativelayout_column[4]=(RelativeLayout)findViewById(R.id.game_relativelayout_list5);
        game_relativelayout_column[5]=(RelativeLayout)findViewById(R.id.game_relativelayout_list6);
        game_relativelayout_column[6]=(RelativeLayout)findViewById(R.id.game_relativelayout_list7);
        //gameboard relative layout for animation part finish
        //Bottom Button part
        btn_restart=(Button)findViewById(R.id.game_button_restart);
        btn_retract=(Button)findViewById(R.id.game_button_retract);
        btn_restart.setOnClickListener(new btn_listener_restart());
        btn_retract.setOnClickListener(new btn_listener_retract());
        //Bottom Button part finish
        //Player VS part
        game_sp= mainGame.this.getSharedPreferences("SP_Player", MODE_PRIVATE);
        player1_win=game_sp.getInt("Player1",0);
        player2_win=game_sp.getInt("Player2",0);
        game_tv_player1_win=(TextView)findViewById(R.id.game_textview_player1_win);
        game_tv_player2_win=(TextView)findViewById(R.id.game_textview_player2_win);
        String temp = Integer.toString(player1_win);
        game_tv_player1_win.setText(temp);
        temp = Integer.toString(player2_win);
        game_tv_player2_win.setText(temp);
        //Player VS part finish
        mp_bgm.setVolume(0.3f,0.3f);
        mp_bgm.setLooping(true);
        mp_bgm.start();
        display_turn();
        gameAnimationListener=new generalAnimationListener();
        KeyAnimationListner = new playKeyAnimationListener();


    }

    @Override
    public void onPause() {
        super.onPause();
        mp_bgm.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mp_bgm.start();
    }

    private void display_turn(){
        float player1,player2;
        if(gLogic.getTurn()==1){
            player1=1.0f;
            player2=0.5f;
        }else{
            player1=0.5f;
            player2=1.0f;
        }

        game_tv_player1.setAlpha(player1);
        game_tv_player2.setAlpha(player2);
        game_iv_player1.setAlpha(player1);
        game_iv_player2.setAlpha(player2);
    }

    private void display_react(){
        int react_keyid=int_corridnate[lastCoordinate[0]][lastCoordinate[1]];
        int_corridnate[lastCoordinate[0]][lastCoordinate[1]]=0;
        game_relativelayout_column[lastCoordinate[0]].removeView(map_keys.get(react_keyid));
        map_keys.remove(react_keyid);
        keyid--;
    }

    private void display_playKey(int x, int y){
        float to_y=(5-y);
        int key_dp = (int) (getResources().getDimension(R.dimen.game_key) / getResources().getDisplayMetrics().density);
        to_y = to_y*key_dp/49;
        ImageView key = new ImageView(this);
        if(turn==1)
            key.setImageResource(R.drawable.key1);
        else
            key.setImageResource(R.drawable.key2);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        key.setLayoutParams(lp);
        int_corridnate[x][y]=keyid;
        map_keys.put(keyid,key);
        keyid++;
        game_relativelayout_column[x].addView(key);
        if(y!=5) {
            Animation translateAnimation = new TranslateAnimation(
                    TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                    TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                    TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                    TranslateAnimation.RELATIVE_TO_SELF, to_y);
            translateAnimation.setDuration(500);
            translateAnimation.setFillAfter(true);
            translateAnimation.setAnimationListener(KeyAnimationListner);
            key.startAnimation(translateAnimation);
        } else {
            if(gLogic.getSteps()==42&&winner==0)
                display_draw();
            else if(winner!=0)
                display_win(winner);
        }


    }
    //Draw the draw result
    private void display_draw(){
        playSoundEffect("win");
        gameFinished=true;
        game_imageview_draw.setVisibility(View.VISIBLE);
        Animation animation = new AlphaAnimation(0,1.0f);
        animation.setAnimationListener(gameAnimationListener);
        animation.setDuration(500);
        game_imageview_draw.startAnimation(animation);
    }
    //Draw the Wining Result
    private void display_win(int player){
        ArrayList<Integer> gameWinKeys=gLogic.getWinKeys();
        int cal_x_temp,cal_y_temp;
        playSoundEffect("win");
        gameFinished=true;
        Animation animation = new AlphaAnimation(0,1.0f);
        animation.setDuration(500);
        animation.setAnimationListener(gameAnimationListener);

        SharedPreferences.Editor editor = game_sp.edit();
        if(player==1) {
            game_imageview_player1_win.setVisibility(View.VISIBLE);
            game_imageview_player1_win.startAnimation(animation);
            for(int i=0;i<gameWinKeys.size();i++) {
                cal_x_temp=gameWinKeys.get(i)/10;
                cal_y_temp=gameWinKeys.get(i)%10;
                map_keys.get(int_corridnate[cal_x_temp][cal_y_temp]).setImageResource(R.drawable.key1win);
            }
            player1_win++;
            editor.putInt("Player1",player1_win);
            String temp = Integer.toString(player1_win);
            game_tv_player1_win.setText(temp);
        }
        else {
            game_imageview_player2_win.setVisibility(View.VISIBLE);
            game_imageview_player2_win.startAnimation(animation);
            for(int i=0;i<gameWinKeys.size();i++) {
                cal_x_temp=gameWinKeys.get(i)/10;
                cal_y_temp=gameWinKeys.get(i)%10;
                map_keys.get(int_corridnate[cal_x_temp][cal_y_temp]).setImageResource(R.drawable.key2win);
            }
            player2_win++;
            editor.putInt("Player2",player2_win);
            String temp = Integer.toString(player2_win);
            game_tv_player2_win.setText(temp);
        }
        editor.apply();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialog_exit();
            return true;
        }
        return false;
    }

    private void dialog_exit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainGame.this);
        builder.setMessage(R.string.confirm_to_exit);
        builder.setTitle(R.string.NOTICE);
        builder.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                playSoundEffect("button");
                dialog.dismiss();
                actManager.finshAllActivities();

            }
        });
        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                playSoundEffect("button");
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void playSoundEffect(String soundName){
        float curVolume,maxVolume,leftVolume,rightVolume;
        curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        leftVolume = curVolume / maxVolume;
        rightVolume = curVolume / maxVolume;
        gameSoundEffect.play(soundEffectMap.get(soundName), leftVolume, rightVolume, priority, no_loop, normal_playback_rate);

    }

    private class btn_listener_column implements View.OnClickListener {
        int int_x=0;
        int int_y=0;

        btn_listener_column(int x){
            int_x=x;
        }
        public void onClick(View v) {
            if(!gameFinished&&animationFinished&&int_corridnate[int_x][5]==0) {
                turn = gLogic.getTurn();
                int_y = gLogic.play(int_x);
                winner = gLogic.getWinner();
                display_playKey(int_x, int_y);
            }
        }
    }

    private class btn_listener_retract implements View.OnClickListener {

        public void onClick(View v) {
            playSoundEffect("button");
            if(!gameFinished&&animationFinished) {
                lastCoordinate = gLogic.retract();
                if(lastCoordinate!=null) {
                    display_react();
                    display_turn();
                }
            }
        }
    }

    private class btn_listener_restart implements View.OnClickListener {

        public void onClick(View v) {
            playSoundEffect("button");
            if(gameFinished&&animationFinished) {

                game_imageview_player1_win.setVisibility(View.GONE);
                game_imageview_player2_win.setVisibility(View.GONE);
                game_imageview_draw.setVisibility(View.GONE);
                for (int i = 0; i < 7; i++) {
                    game_relativelayout_column[i].removeAllViews();
                    for(int m=0;m<6;m++){
                        int_corridnate[i][m]=0;
                    }
                }

                map_keys.clear();
                keyid = 1;
                winner = 0;
                gameFinished=!gLogic.clearGameboard();
                display_turn();
            }
        }
    }

    private class generalAnimationListener implements Animation.AnimationListener{
        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        @Override
        public void onAnimationStart(Animation animation) {
            animationFinished=false;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            animationFinished=true;
        }
    }

    private class playKeyAnimationListener implements Animation.AnimationListener{

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        @Override
        public void onAnimationStart(Animation animation) {
            animationFinished=false;
            playSoundEffect("drop");
            display_turn();
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            animationFinished=true;
            if(gLogic.getSteps()==42&&winner==0)
                display_draw();
            else if(winner!=0)
                display_win(winner);

        }
    }
}
