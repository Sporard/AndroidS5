package com.example.pierre.myapplication;

import android.content.*;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<String> {
    //Attribut

    //constructeur

    public MyAdapter(Context context, ArrayList<String> choix ){
        super(context,0,choix);

    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        if(convertView == null ){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item,parent,false);

        }
        TextView caseChoix = (TextView)convertView.findViewById(R.id.choix_name);
        caseChoix.setText(getItem(position));
        return convertView;
    }

}
