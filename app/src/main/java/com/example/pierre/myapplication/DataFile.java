package com.example.pierre.myapplication;

import android.content.Context;
import java.io.*;


public class DataFile {

    //Attributs
    private String link;
    private Context context;

    //Constructeur
    public DataFile(String link, Context context) {
        this.link = link;
        this.context = context;
    }

    //Getteurs
    public String getLink() {
        return this.link;
    }

    public Context getContext() {
        return this.context;
    }

    //Setteurs
    public void setLink(String link) {
        this.link = link;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    //Methods
    public void write(String s) {
        try {
            FileOutputStream io = this.getContext().openFileOutput(this.getLink(), Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(io);
            writer.write(s);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String read()  {
        StringBuilder builder = new StringBuilder();
        try{
            FileInputStream in  = this.getContext().openFileInput(this.getLink());
            if(in != null){
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String temp;
                while((temp = br.readLine()) != null ){
                    builder.append(temp);
                }
                in.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return builder.toString();
    }
}
