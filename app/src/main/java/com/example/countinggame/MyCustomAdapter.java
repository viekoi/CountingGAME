package com.example.countinggame;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyCustomAdapter extends ArrayAdapter<GameButton> {
    private ArrayList<GameButton> gameButtonArrayList;

    Context context;


    public MyCustomAdapter( Context context,ArrayList<GameButton> gameButtonArrayList) {
        super(context, R.layout.add_button, gameButtonArrayList);
        this.gameButtonArrayList = gameButtonArrayList;
        this.context = context;
    }

    private static class MyViewHolder{
       Button gameButton;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String[] colorcells ={
                "#8b008b",
                "#ffa500",
                "#2d2d2d",
                "#ff0000",
                "#00ff00",
                "#0000ff",
                "#ffff00",
                "#00ffff",
                "#ff00ff"
        };

        GameButton gameButtons = getItem(position);
        MyViewHolder myViewHolder;

        if(convertView == null){
            myViewHolder = new MyViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(
                    R.layout.add_button,
                    parent,
                    false
            );

            myViewHolder.gameButton = (Button) convertView.findViewById(R.id.gameButton);

            convertView.setTag(myViewHolder);

        }else{
            myViewHolder = (MyViewHolder) convertView.getTag();

        }

        myViewHolder.gameButton.setText(gameButtons.getButtonValue().toString());
        myViewHolder.gameButton.setId(gameButtons.getButtonValue());
        int colorIndex = position % colorcells.length;
        String backgroundColor = colorcells[colorIndex];
        myViewHolder.gameButton.setBackgroundColor(Color.parseColor(backgroundColor));

        return  convertView;
    }
}
