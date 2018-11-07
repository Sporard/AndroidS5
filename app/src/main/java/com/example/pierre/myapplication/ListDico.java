package com.example.pierre.myapplication;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ListDico {
    public final static String LINK = "data.json";
    //attributs
    //La liste des dicos
    private ArrayList<Dico> liste ;
    //le liens du fichier de lecture

    //Le contexte acutel ou est l'instance de la classe
    private Context context;

    //Constructeur

    public ListDico(Context c ){
        setListe(new ArrayList<Dico>());
        this.setContext(c);
    }

    //getteurs

    //Pour avoir la liste des dicos
    public ArrayList<Dico> getListe(){
        return this.liste;
    }


    //Pour avoir un dico en particulier
    public Dico searchDico(String name){
        ArrayList<Dico> listD = this.getListe();

        for(int i=0; i < listD.size(); i++) {
            if (listD.get(i).getName().equals(name) ) {
                return listD.get(i);
            }
        }
        return null ;
    }

    //Pour avoir la liste des noms des dictionnaires
    public ArrayList<String> getDicoName(){
        ArrayList<String> temp = new ArrayList<>();
        for(int i = 0 ; i < this.getListe().size();i++){
            temp.add(this.getListe().get(i).getName());
        }
        return temp;
    }

    //Renvoit le context associé
    public Context getContext(){
        return this.context;
    }

    //Pour avoir le lien du fichier


    //setteurs

    //Mets a jour la liste des dictionnaires
    public void setListe( ArrayList<Dico> liste ){
        this.liste = liste ;
    }


    public void setContext(Context c){
        this.context = c;
    }

    //methode


    //Ajoute un dico a la liste
    public void add(Dico d){
        this.liste.add(d);
    }

    //Remplace les mots d'un dictionnaire dans la liste
    public void remplaceDico(Dico d, String name ){
        this.searchDico(name).setWords(d.getWords());
    }

    public void read()  {
        StringBuilder builder = new StringBuilder();
        try{
            FileInputStream in  = this.getContext().openFileInput(LINK);
            if(in != null){
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String temp;
                while((temp = br.readLine()) != null ){
                    builder.append(temp);
                }
                in.close();
                Gson gson = new Gson();
                ListDico tempTrue = gson.fromJson(builder.toString(),ListDico.class);
                this.setListe(tempTrue.getListe());
            }else{
                Gson gson = new Gson();
                ListDico temp = new ListDico(this.getContext());
                Dico d = new Dico("Fruit");
                temp.add(d);
                write(gson.toJson(temp.getListe().toArray()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void write() {
        Gson gson = new Gson();
        String s = gson.toJson(this.getListe().toArray());
        try {
            FileOutputStream io = this.getContext().openFileOutput(LINK, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(io);
            writer.write(s);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void write(String s){
        try {
            FileOutputStream io = this.getContext().openFileOutput(LINK, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(io);
            writer.write(s);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
