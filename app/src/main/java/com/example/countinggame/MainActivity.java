package com.example.countinggame;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompat {
    GridView gridView;
    TextView timerText;
    Button stopStartButton;
    Double time = 0.0;

    Integer maxRange = 10;
    Timer timer;
    TimerTask  timerTask;

    Spinner langSpinner;


    ArrayList<GameButton> gameButtonArrayList;

    Integer currentNumber = 1;
    Integer guessesLeft = 3;

    TextView nextNumber;
    boolean timerStarted = false;

    MyCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //variables

        timerText = findViewById(R.id.timerText);
        stopStartButton = findViewById(R.id.startStopButton);
        gridView = findViewById(R.id.gridView);

        nextNumber = findViewById(R.id.nextNumber);
        timer = new Timer();
        LanguageManager languageManager = new LanguageManager(MainActivity.this);
        //spinner
        langSpinner = findViewById(R.id.langSpinner);
        String[] langOptions = {getResources().getString(R.string.language),"EN","VI"};
        ArrayAdapter<String> langAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, langOptions);
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langSpinner.setAdapter(langAdapter);


       langSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
           public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
               Object item = parent.getItemAtPosition(pos);
               if(pos == 0){
                   System.out.println(pos);
                  return;
               }

               languageManager.updateResource(item.toString().toLowerCase());
               finish();
               startActivity(getIntent());

           }
           public void onNothingSelected(AdapterView<?> parent) {
           }
       });


        //render buttons
        gameButtonArrayList = new ArrayList<>();
        for (int i = 1; i<=maxRange;i++){
            GameButton newGameButton = new GameButton(i);
            gameButtonArrayList.add(newGameButton);
        }
        renderItems();


    }

    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }



    private void renderItems (){
        Collections.shuffle(gameButtonArrayList);
        adapter = new MyCustomAdapter(getApplicationContext(),gameButtonArrayList);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Button tappedButton = findViewById(view.getId());
                gameButtonTapped(tappedButton);
            }
        });
    }



    public void resetTapped()
    {
        AlertDialog.Builder resetAlert = new AlertDialog.Builder(this);
        resetAlert.setTitle(getResources().getString(R.string.resetTimer_modal_title));
        resetAlert.setMessage(getResources().getString(R.string.resetTimer_modal_message));
        resetAlert.setPositiveButton(getResources().getString(R.string.agree), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(timerTask != null)
                {
                  onResetTimer();

                }
            }
        });

        resetAlert.setNeutralButton(getResources().getString(R.string.disagree), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                //do nothing
            }
        });

        resetAlert.show();

    }

    public void startStopTapped(View view)
    {
        if(timerStarted == false )
        {
            //onStop
            onStartTimer();
        }else
        {
            //onReset
           resetTapped();
        }
    }

    private void setButtonUI(String start)
    {
        stopStartButton.setText(start);
//        stopStartButton.setTextColor(ContextCompat.getColor(this, color));
    }

    private void startTimer()
    {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        time++;
                        timerText.setText(getTimerText());
                    }
                });
            }

        };
        timer.scheduleAtFixedRate(timerTask, 0 ,1000);
    }


    private String getTimerText()
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



    public void gameButtonTapped (Button view){

        if(!timerStarted){
            AlertDialog.Builder startAlert = new AlertDialog.Builder(this);
            startAlert.setTitle(getResources().getString(R.string.startGame_modal_title));
            startAlert.setMessage(getResources().getString(R.string.startGame_modal_message));
            startAlert.setPositiveButton(getResources().getString(R.string.disagree), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    return;
                }
            });

            startAlert.setNeutralButton(getResources().getString(R.string.agree), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    onStartTimer();
                }
            });

            startAlert.show();
        }else {
            if(view.getId() != currentNumber){
                onWrongAnswer();

            }else {
                onRightAnswer(view);
            }
        }




    }

    private  void  onRightAnswer( Button tappedButton){
        currentNumber++;
        if(currentNumber == maxRange + 1){
            onWon();
        }
        Integer currentDisplayNumber = currentNumber - 1;
        nextNumber.setText(currentDisplayNumber.toString());
        tappedButton.setEnabled(false);
        tappedButton.setVisibility(View.GONE);


    }
    private void onWrongAnswer(){
        guessesLeft--;
        if(guessesLeft > 0){
            AlertDialog.Builder wrongAnswerAlert = new AlertDialog.Builder(this);
            String message = String.format(getResources().getString(R.string.guessesLeft),guessesLeft);
            wrongAnswerAlert.setTitle(getResources().getString(R.string.wrongAnswer_modal_title));
            wrongAnswerAlert.setMessage(message);
            wrongAnswerAlert.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                }
            });
            wrongAnswerAlert.show();
        }


        if(guessesLeft == 0) onLost();







    }

    private void onLost (){
        Intent i = new Intent(getApplicationContext(), LosingActivity.class);
        startActivity(i);
    }

    private  void onWon(){

        Intent i = new Intent(getApplicationContext(), WinningActivity.class);
        startActivity(i);

    }


    private void onStartTimer(){
        timerStarted = true;
        setButtonUI(getResources().getString(R.string.restart_game));
        startTimer();
    }



    private void onResetTimer(){
        timerStarted = false;
        setButtonUI(getResources().getString(R.string.start_game));
        timerTask.cancel();
        time = 0.0;
        timerText.setText(formatTime(0,0,0));
        currentNumber = 1;
        guessesLeft = 3;
        renderItems();
        nextNumber.setText("");
    }






}