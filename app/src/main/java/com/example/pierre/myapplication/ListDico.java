package com.example.pierre.myapplication;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public void del(Dico d){
        this.liste.remove(d);
    }

    //Remplace les mots d'un dictionnaire dans la liste
    public void remplaceDico(Dico d, String name ){
        this.searchDico(name).setWords(d.getWords());
    }

    //Lis le fichier et parse le Json en ListDico
    public void read(String fichier) {
        File file = null;

        StringBuilder builder = null;
        FileInputStream fis = null;
        try {
            fis = this.getContext().openFileInput(fichier);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fis == null){
            //Si le fichier n'est pas existant alors on créer un nouveau fichier avec un dico a l'intérieur
            Dico d = new Dico();
            d.dice();
            this.add(d);
            Gson gson = new Gson();
            this.sauvegarde(gson.toJson(this.getListe().toArray()),ListDico.LINK);
            try {
                fis = this.getContext().openFileInput(fichier);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        builder = lireFileInpuStream(fis);
        try{
            fis.close();
        }catch (IOException e ){
            e.printStackTrace();
        }
        //Deserialisation de l'objet json
        Gson gson = new Gson();
        TypeToken<ArrayList<Dico>> token = new TypeToken<ArrayList<Dico>>(){};
        ArrayList<Dico> temp = gson.fromJson(builder.toString(),token.getType());
        this.setListe(temp);
    }

    //Methode qui genère la chaine de caractère du fichier
    private StringBuilder lireFileInpuStream(FileInputStream fis) {
        StringBuilder sb = null ;
        InputStreamReader rstream = null;
        try{
            rstream = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(rstream);
            sb = new StringBuilder();
            String temp;
            while((temp = br.readLine()) != null){
                sb.append(temp);
            }
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return sb;
    }



    //Ecrit dans un fichier
    public boolean sauvegarde(String text,String fichier) {
        FileOutputStream fos = null;
        try{
            fos = this.getContext().openFileOutput(fichier,Context.MODE_PRIVATE);
        }catch (FileNotFoundException e){
            e.printStackTrace();
            return false;
        }
        try{
            OutputStreamWriter ostream = new OutputStreamWriter(fos);
            ostream.write(text);
            ostream.close();
        }catch (IOException e ){
            e.printStackTrace();
            return false ;
        }
        return true;
    }



}
