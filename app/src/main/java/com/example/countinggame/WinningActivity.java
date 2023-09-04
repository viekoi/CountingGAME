package com.example.countinggame;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;


public class WinningActivity extends AppCompat {
    TextView currentHighScore;
    Integer lastScore;

    Integer best1,best2,best3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning);
        currentHighScore = findViewById(R.id.currentHighScore);
        SharedPreferences sharedPreferences = getSharedPreferences("PREFS",0);
        lastScore= sharedPreferences.getInt("lastScore",0);
        best1 = sharedPreferences.getInt("best1",0);
        best2 = sharedPreferences.getInt("best2",0);
        best3 = sharedPreferences.getInt("best3",0);

        if(lastScore > best2 && lastScore < best3) {
            best3 = lastScore;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("best3",best3);
            editor.apply();
        }

        if(lastScore > best1 && lastScore < best2){
            Integer temp = best2;
            best2 = lastScore;
            best3 = temp;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("best3",best3);
            editor.putInt("best2",best2);
            editor.apply();
        }

        if(lastScore < best1){
            Integer temp = best1;
            best1 = lastScore;
            best2 = temp;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("best1",best1);
            editor.putInt("best2",best2);
            editor.apply();
        }

        currentHighScore.setText(getTimerText(lastScore) +"\n" + "Best 1: " + getTimerText(best1) +"\n" + "Best 2: " + getTimerText(best2) +"\n"+ "Best 3: " + getTimerText(best3));




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    private String getTimerText(Integer time)
    {
        int rounded = (int) Math.round(time);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }

    private String formatTime(int seconds, int minutes, int hours)
    {
        return String.format("%02d",hours) + " : " + String.format("%02d",minutes) + " : " + String.format("%02d",seconds);
    }

    public void playAgainButtonTapped( View view){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }
}
