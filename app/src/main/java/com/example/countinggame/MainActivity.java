package com.example.countinggame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    TextView timerText;
    Button stopStartButton;
    Double time = 0.0;

    Integer maxRange = 10;
    Timer timer;
    TimerTask  timerTask;

    GridLayout gridView;

    ArrayList<Integer> intArr = new ArrayList<Integer>();


    Integer currentNumber = 1;
    Integer guessesLeft = 3;


    boolean timerStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerText = findViewById(R.id.timerText);
        stopStartButton = findViewById(R.id.startStopButton);
        gridView = findViewById(R.id.gridView);

        timer = new Timer();

        for (int i = 1; i<=maxRange;i++){
            intArr.add(i);
        }

        renderGameButton();

    }

    private void renderGameButton (){
        Collections.shuffle(intArr);

        for (Integer number: intArr
        ) {
            addButton(number);
        }
    }

    public void resetTapped(View view)
    {
        AlertDialog.Builder resetAlert = new AlertDialog.Builder(this);
        resetAlert.setTitle("Reset Timer");
        resetAlert.setMessage("Are you sure you want to reset the timer?");
        resetAlert.setPositiveButton("Reset", new DialogInterface.OnClickListener()
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

        resetAlert.setNeutralButton("Cancel", new DialogInterface.OnClickListener()
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
        } else if (timerStarted  && time > 0) {
            //onResume
            onStopTimer();
        } else
        {
            //onReset
           onResetTimer();
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

    private void addButton(Integer number) {
        final View view = getLayoutInflater().inflate(R.layout.add_button,null);
        Button gameButton = view.findViewById(R.id.gameButton);
        if(gameButton == null){
            System.out.println("abc");
           return;
        }
        gameButton.setText(number.toString());
        gameButton.setId(number);
        gridView.addView(view);
    }

    public void gameButtonTapped (View view){
        Button tappedButton = findViewById(view.getId());
        if(!timerStarted){
            AlertDialog.Builder startAlert = new AlertDialog.Builder(this);
            startAlert.setTitle("The game hasnt start yet ?");
            startAlert.setMessage("Start the game ?");
            startAlert.setPositiveButton("No", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    return;
                }
            });

            startAlert.setNeutralButton("Yes", new DialogInterface.OnClickListener()
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
                onRightAnswer(tappedButton);
            }
        }




    }

    private  void  onRightAnswer( Button tappedButton){
        currentNumber++;
        tappedButton.setEnabled(false);
        if(currentNumber == maxRange + 1){
            AlertDialog.Builder winningAlert = new AlertDialog.Builder(this);
            winningAlert.setTitle("You Won");
            winningAlert.setMessage("Great job");
            winningAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    onWon();
                }
            });
            winningAlert.show();
        }

    }
    private void onWrongAnswer(){
        guessesLeft--;

        AlertDialog.Builder wrongAnswerAlert = new AlertDialog.Builder(this);

            String message = guessesLeft == 0 ? "You Lost":"You have " +guessesLeft +" left";
            wrongAnswerAlert.setTitle("Wrong answer");
            wrongAnswerAlert.setMessage(message);
            wrongAnswerAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                }
            });

        if(guessesLeft == 0) onLost();

        wrongAnswerAlert.show();





    }

    private void onLost (){
        onResetTimer();
    }

    private  void onWon(){
        onResetTimer();
    }


    private void onStartTimer(){
        timerStarted = true;
        setButtonUI("STOP");
        startTimer();
    }

    private void onStopTimer(){
        timerStarted = false;
        setButtonUI("Resume");
        timerTask.cancel();
    }

    private void onResetTimer(){
        timerStarted = false;
        setButtonUI("START");
        timerTask.cancel();
        time = 0.0;
        timerText.setText(formatTime(0,0,0));
        currentNumber = 1;
        guessesLeft = 3;
        gridView.removeAllViews();
        renderGameButton();
    }





}