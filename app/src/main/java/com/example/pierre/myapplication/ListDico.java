package com.example.pierre.myapplication;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListDico {

    //attributs
    //La liste des dicos
    private ArrayList<Dico> liste ;
    //le liens du fichier de lecture
    private String link ;
    //Le contexte acutel ou est l'instance de la classe
    private Context context;

    //Constructeur

    public ListDico(Context c ){
        setListe(new ArrayList<Dico>());
        this.setLink("dico.json");
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

    public String getLink(){ return this.link;}

    //setteurs

    //Mets a jour la liste des dictionnaires
    public void setListe( ArrayList<Dico> liste ){
        this.liste = liste ;
    }

    public void setLink(String link){
        this.link=link;
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

    //Genere la liste des dictionnaire en lisant un fichier JSON
    //Par choix personnel on a fixé le fichier que nous lisons pour générer la liste
    public void generateFromJson() throws IOException {
        String json;
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(this.getContext().getAssets().open(this.getLink())));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null){
            result += line;
        }
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("liste");

            Dico d = new Dico();
            ArrayList<String> temp = new ArrayList<>();
            for(int i = 0; i < jsonArray.length() ; i++ ){
                d.setName(jsonArray.getJSONObject(i).getString("name"));
                for(int k =0; k<jsonArray.getJSONObject(i).getJSONArray("mots").length(); k++){
                    temp.add(jsonArray.getJSONObject(i).getJSONArray("mots").getString(k));
                }
                d.setWords(temp);
                this.add(d);
                temp = new ArrayList<>();
                d = new Dico();
            }

    }catch(JSONException e ) {
            e.printStackTrace();
        }
    }

//    public void write() throws IOException {
//        FileOutputStream fos = openFileOutpu
//        bw.write(this.toJSON());
//        bw.close();
//    }

    public String toJSON(){
        String json ="{ \"liste\":[";
        for(int i = 0 ; i < this.getListe().size();i++){
            json  = json + this.getListe().get(i).toJSON();
        }
        json = json + "]}";

        return json;
    }


}
