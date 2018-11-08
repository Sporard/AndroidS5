package com.example.pierre.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //Atributs
    private Button buttonRNG;
    private ImageButton buttonGoToEdit;
    private TextView editText;
    private ListDico lesDicos;
    private int random;
    private Spinner spinner;
    private ImageButton bGoogle;
    private Boolean start = false ;
    private ImageButton addDico;

    public void goToAjout_Activity(String name){
        Intent ajout_intent = new Intent(MainActivity.this, AjoutActivity.class);
        ajout_intent.putExtra("name",name);
        startActivity(ajout_intent);
    }

    public void initView(){
        buttonRNG =  findViewById(R.id.buttonRNG);
        editText =  findViewById(R.id.centralText);
        buttonGoToEdit =  findViewById(R.id.buttonGoToEdit);
        spinner = findViewById(R.id.Spinner);
        bGoogle = findViewById(R.id.buttonWeb);
        addDico =findViewById(R.id.addDico);
    }

    public void newDico(){
        Dico d = new Dico("Nouveau");
        lesDicos.add(d);
        Gson gson = new Gson();
        lesDicos.sauvegarde(gson.toJson(lesDicos.getListe().toArray()),ListDico.LINK);
        goToAjout_Activity(d.getName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            //Initialise les objets de l'activité
            initView();
            //On genere la liste de dico
            lesDicos = new ListDico(this);
            lesDicos.read(ListDico.LINK);
            boolean bool = lesDicos.getListe().size() == 0;
            if(lesDicos.getListe().size() == 0 ){
                Dico d = new Dico();
                d.dice();
                lesDicos.add(d);
                Gson gson = new Gson();
                lesDicos.sauvegarde(gson.toJson(lesDicos.getListe().toArray()),ListDico.LINK);
            }

            //Menu spinner car les menus hamburger ne sont pas supporté par Android 4.0
            ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lesDicos.getDicoName());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Passage a l'ajout d'un nouveau dictionnaire
        addDico.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           newDico();
                                       }
                                   });

        //Passage a l'édition du dictionnaire choisit
        buttonGoToEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    goToAjout_Activity((String) spinner.getAdapter().getItem(spinner.getSelectedItemPosition()));
                }
                });


            //Bouton qui lance la RNG
            buttonRNG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String nameTemp = (String) spinner.getAdapter().getItem(spinner.getSelectedItemPosition());
                    final Dico d = lesDicos.searchDico(nameTemp);
                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.sample_anim);
                    if (!(d.estVide())) {
                        if(!start){
                            start = true;
                            buttonRNG.setText(getString(R.string.stop));
                            editText.startAnimation(animation);
                            //Listener sur l'animation pour la faire boucler
                            animation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    editText.setText(d.getWords().get(random));

                                }

                                @Override
                                public void onAnimationEnd(Animation animation){
                                    if(start) {
                                        random = (int) (Math.random() * d.getWords().size());
                                        editText.startAnimation(animation);

                                    }else{
                                        //On ralenti l'animation quand on veut la stopper
                                        random = (int) (Math.random() * d.getWords().size());
                                        for(int i = (int)animation.getDuration(); i < 10000 ; i+=100 ){
                                            editText.startAnimation(animation);
                                        }
                                        //Pour eviter toutes triches on genère un nouveau mouvement aléatoire
                                        random = (int) (Math.random() * d.getWords().size());
                                        editText.setText(d.getWords().get(random));
                                        editText.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.lefttoright));


                                   }
                                }
                                @Override
                                public void onAnimationRepeat(Animation animation){

                                }
                            });
                        }else{
                            //Arret de la boucle de l'animation
                            buttonRNG.setText(R.string.start);
                            start = false;
                        }

                    } else {
                        //On lance pas d'animation sur une liste vide !
                        Toast toast = Toast.makeText(MainActivity.this, "La liste est vide", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

            });
            //Listeners du bouton de recherche sur le web
            bGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String search = "http://www.google.fr/search?q=" + (String) editText.getText();
                    Intent web = new Intent(Intent.ACTION_VIEW);
                    web.setData(Uri.parse(search));
                    startActivity(web);
                }
            });




    }


}