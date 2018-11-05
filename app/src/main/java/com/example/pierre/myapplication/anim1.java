package com.example.pierre.myapplication;

public class anim1 extends Thread {

    private boolean sorti;
    private Dico liste;

    public anim1(boolean sorti,Dico liste){
        this.sorti = sorti;
        this.liste =liste;
    }

    public void run(){
        while(this.sorti != true){

        }
    }
}
