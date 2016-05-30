package com.example.sandeep_pc.alphabite;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by SANDEEP_SHARMA on 05-May-16.
 */

public class MainActivity extends AppCompatActivity {


    TextView score_view, del, random_word, type_word, timer, l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12, l13, l14, l15, l16, l17, l18, l19, l20, l21, l22, l23, l24, l25, l26;
    TextView[] keys;
    RelativeLayout background;
    TableLayout table;
    String[] alphabets = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    String[] words = new String[200];
    String[] color = new String[]{"#0097b7","#fa7703","#ff00d8","#0181bd","#5ea001","#5a5a5a","#ff3600","#ffffff","#0010e6"};
    Scanner inputFile;
    Button startButton, play_again, music;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    MediaPlayer player;
    Typeface fontStyle;
    int points = 0, i = 0, position;
    String stringUpdate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fontStyle= Typeface.createFromAsset(getAssets(),"ALIEN5.TTF");
        background = (RelativeLayout)findViewById(R.id.background_color);
        music = (Button)findViewById(R.id.music);
        random_word = (TextView)findViewById(R.id.random_word);
        score_view = (TextView)findViewById(R.id.score_view);
        type_word = (TextView)findViewById(R.id.type_word);
        timer = (TextView)findViewById(R.id.timer);
        table =(TableLayout)findViewById(R.id.table);
        startButton =(Button)findViewById(R.id.startButton);
        play_again = (Button)findViewById(R.id.play_again);
        del = (TextView)findViewById(R.id.del);


        //Adview Layout Size
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //setting preferences
        prefs = this.getSharedPreferences("HighScore", Context.MODE_PRIVATE);
        editor = prefs.edit();

        //Reading words from file
        inputFile = new Scanner(getResources().openRawResource(R.raw.words));
        int j = 0;
        while(inputFile.hasNext())
        {
            words[j++] = inputFile.next();
        }

        //Setting the background music
        player = MediaPlayer.create(MainActivity.this, R.raw.bg_music);
        player.setLooping(true); // Set looping
        player.setVolume(0.5f, 0.5f);


        //play Music if set
        if(prefs.getInt("music",1)!=0)
        {
            player.start();
            music.setText("Music : OFF");
        }
        else
        {
            music.setText("Music : ON");
        }


        //Colour change feature for the Random Word on LongPress
        random_word.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Random ran = new Random();
                int i = ran.nextInt(6);
                Log.d("Color","value: "+i);
                random_word.setTextColor(Color.parseColor(color[i]));
                return false;
            }
        });

        //Initializing the KeyPad Buttons
        keypadInitializer();

        keys = new TextView[]{l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12, l13, l14, l15, l16, l17, l18, l19, l20, l21, l22, l23, l24, l25, l26};

        //Setting the keypad buttons.
        setKeypad();

        //On long pressing the delete button clear the complete word
        del.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                stringUpdate = "";
                type_word.setText(stringUpdate);
                return false;
            }
        });

        //setting the font style except for keyPad.
        settingFontStyle();

        //Hiding the unwanted views during the start of the game.
        score_view.setVisibility(View.INVISIBLE);
        table.setVisibility(View.INVISIBLE);
        type_word.setVisibility(View.INVISIBLE);
        timer.setVisibility(View.INVISIBLE);
        play_again.setVisibility(View.INVISIBLE);

    }


    public void onPause() {
        //pause the background music
        player.pause();
        position = player.getCurrentPosition();
        super.onPause();
    }

    public void onResume() {
        //Playing the background music from the pause state
        player.seekTo(position);
        if(prefs.getInt("music",0)!=0)
        {
            player.start();
        }
        super.onResume();
    }

    public void onDestroy() {
        //stop and release the background music resource
        player.stop();
        player.release();
        finish();
        super.onDestroy();
    }

    //Method to control music by button press.
    public void musicOnAndOff(View v)
    {
        if(prefs.getInt("music",1) == 1)
        {
            editor.putInt("music",0);
            editor.commit();
            player.pause();
            music.setText("Music : ON");
        }
        else if(prefs.getInt("music",0) == 0)
        {
            editor.putInt("music",1);
            editor.commit();
            player.start();
            music.setText("Music : OFF");
        }
    }

    //Initializing all the Keys of Keyboard
    //Textview is used here
    public void keypadInitializer()
    {
        l1 = (TextView)findViewById(R.id.l1);
        l2 = (TextView)findViewById(R.id.l2);
        l3 = (TextView)findViewById(R.id.l3);
        l4 = (TextView)findViewById(R.id.l4);
        l5 = (TextView)findViewById(R.id.l5);
        l6 = (TextView)findViewById(R.id.l6);
        l7 = (TextView)findViewById(R.id.l7);
        l8 = (TextView)findViewById(R.id.l8);
        l9 = (TextView)findViewById(R.id.l9);
        l10 = (TextView)findViewById(R.id.l10);
        l11 = (TextView)findViewById(R.id.l11);
        l12 = (TextView)findViewById(R.id.l12);
        l13 = (TextView)findViewById(R.id.l13);
        l14 = (TextView)findViewById(R.id.l14);
        l15 = (TextView)findViewById(R.id.l15);
        l16 = (TextView)findViewById(R.id.l16);
        l17 = (TextView)findViewById(R.id.l17);
        l18 = (TextView)findViewById(R.id.l18);
        l19 = (TextView)findViewById(R.id.l19);
        l20 = (TextView)findViewById(R.id.l20);
        l21 = (TextView)findViewById(R.id.l21);
        l22 = (TextView)findViewById(R.id.l22);
        l23 = (TextView)findViewById(R.id.l23);
        l24 = (TextView)findViewById(R.id.l24);
        l25 = (TextView)findViewById(R.id.l25);
        l26 = (TextView)findViewById(R.id.l26);

    }

    //Shuffle the letters of the keypad
    public void setKeypad()
    {
        Collections.shuffle(Arrays.asList(words));
        Collections.shuffle(Arrays.asList(alphabets));
        for(int i=0;i<alphabets.length;i++)
        {
            keys[i].setText(alphabets[i]);
        }

    }


    //Setting font style for the views.
    public void settingFontStyle()
    {
        random_word.setTypeface(fontStyle);
        type_word.setTypeface(fontStyle);
        score_view.setTypeface(fontStyle);
        timer.setTypeface(fontStyle);
        music.setTypeface(fontStyle);
        startButton.setTypeface(fontStyle);
        play_again.setTypeface(fontStyle);
    }

    //Generating a prompt when user presses back button.
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Exit or Not");
        builder.setMessage("Do you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
                MainActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //MainActivity.super.onBackPressed();
            }
        });
        builder.show();
    }

    //Set the views to visibility and start the game on pressing the PLAY button.
    public void startButtonPress(View v)
    {
        table.setVisibility(View.VISIBLE);
        type_word.setVisibility(View.VISIBLE);
        timer.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.INVISIBLE);
        music.setVisibility(View.INVISIBLE);
        timerStop();
        updateWords();
    }

    //Del charachter of typed word on press '<'(del) from the keypad.
    public void delete_letter(View v)
    {
        int len = stringUpdate.length();
        if(len > 20)
        {
            stringUpdate = stringUpdate.substring(0,19);
        }
        else if (len > 0)
        {
            stringUpdate = stringUpdate.substring(0,len-1);
        }
        type_word.setText(stringUpdate);
    }

    //Method to start the game again.
    public void playAgain(View v)
    {
        Intent intent = new Intent(MainActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    //method to move to next word if the current typed word matches the random word.
    public void enter_word(View v)
    {
        if(stringUpdate.toLowerCase().equals(words[i]))
        {
            i++;
            points++;
            stringUpdate = "";
            type_word.setText(stringUpdate);
            updateWords();
        }
    }

    //Update the random word once the typed word matches.
    public void updateWords()
    {
        random_word.setText(words[i].toUpperCase());
    }

    //Setting the high score
    public void settingHighScore()
    {
        if(prefs.getInt("score",0) < points)
        {
            editor.putInt("score",points);
            editor.commit();
        }
    }

    //Set the timer
    public void timerStop()
    {
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {

                //Countdown
                timer.setText(""+millisUntilFinished / 1000);

                //Forward to next random word if the current one matches.
                //Here we are just checking the length if it matches then call enter_word() to check for the word match.
                if(stringUpdate.length() == words[i].length())
                {
                    enter_word(null);
                }
            }

            //When timer finishes we set the visibility OFF for keypad,random_word,type_word and timer, and set
            //the visibilty ON for play_again, score_view.
            public void onFinish() {
                timer.setText("Done!");
                table.setVisibility(View.INVISIBLE);
                type_word.setVisibility(View.INVISIBLE);
                timer.setVisibility(View.INVISIBLE);
                random_word.setVisibility(View.INVISIBLE);
                settingHighScore();
                score_view.setText("Your Score is: "+points+"\n\n High Score: "+prefs.getInt("score",0));
                play_again.setVisibility(View.VISIBLE);
                score_view.setVisibility(View.VISIBLE);

            }

        }.start();
    }

    //Sice Textview is used all the letters are indivisually clickable and below are the methods to see the action on click.
    //Methods to get Text from keypad to the type_word.

    public void onPressL1(View v){
        stringUpdate = stringUpdate + l1.getText().toString();
        type_word.setText(stringUpdate);

    }
    public void onPressL2(View v){
        stringUpdate = stringUpdate + l2.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL3(View v){
        stringUpdate = stringUpdate + l3.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL4(View v){
        stringUpdate = stringUpdate + l4.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL5(View v){
        stringUpdate = stringUpdate + l5.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL6(View v){
        stringUpdate = stringUpdate + l6.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL7(View v){
        stringUpdate = stringUpdate + l7.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL8(View v){
        stringUpdate = stringUpdate + l8.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL9(View v){
        stringUpdate = stringUpdate + l9.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL10(View v){
        stringUpdate = stringUpdate + l10.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL11(View v){
        stringUpdate = stringUpdate + l11.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL12(View v){
        stringUpdate = stringUpdate + l12.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL13(View v){
        stringUpdate = stringUpdate + l13.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL14(View v){
        stringUpdate = stringUpdate + l14.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL15(View v){
        stringUpdate = stringUpdate + l15.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL16(View v){
        stringUpdate = stringUpdate + l16.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL17(View v){
        stringUpdate = stringUpdate + l17.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL18(View v){
        stringUpdate = stringUpdate + l18.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL19(View v){
        stringUpdate = stringUpdate + l19.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL20(View v){
        stringUpdate = stringUpdate + l20.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL21(View v){
        stringUpdate = stringUpdate + l21.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL22(View v){
        stringUpdate = stringUpdate + l22.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL23(View v){
        stringUpdate = stringUpdate + l23.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL24(View v){
        stringUpdate = stringUpdate + l24.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL25(View v){
        stringUpdate = stringUpdate + l25.getText().toString();
        type_word.setText(stringUpdate);
    }
    public void onPressL26(View v){
        stringUpdate = stringUpdate + l26.getText().toString();
        type_word.setText(stringUpdate);
    }
}
