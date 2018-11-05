package com.example.pierre.myapplication;
import android.graphics.Color;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Dico {
    private String name;
    private ArrayList<String> words;


    public Dico() {
        this("default");
    }

    //Constructeur

    public Dico(String name) {
        setName(name);
        this.words = new ArrayList<String>();
    }


    //Setteurs

    public void setName(String name) {

        this.name = name;
    }

    public void setWords(ArrayList<String> liste){
        this.words = liste;
    }


    //Getteurs
    public String getName() {
        return this.name;
    }

    //Method

    public void addWord(String word) {
        this.words.add(word);
    }

    public void removeWord(String word) {
        this.words.remove(word);
    }

    public int getSize() {
        return this.words.size();
    }

    public ArrayList<String> getWords() {
        //return this.words.toArray(new String[0]);
        return this.words;
    }

    public String toString() {
        String res = "";
        for (String str : this.words) {
            res += "> " + str + "\n";
        }

        return res;
    }

    public boolean estVide(){
        return this.words.isEmpty();
    }

    public String toJSON(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}