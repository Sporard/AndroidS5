package com.example.pierre.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //Atributs
    private ImageButton buttonRNG;
    private FloatingActionButton buttonGoToEdit;
    private TextView editText;
    private ListDico lesDicos;
    private ConstraintLayout cl;
    private Spinner spinner;
    private Button bGoogle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            lesDicos = new ListDico(this);
            buttonRNG =  findViewById(R.id.buttonRNG);
            editText =  findViewById(R.id.centralText);
            buttonGoToEdit =  findViewById(R.id.buttonGoToEdit);
            spinner = findViewById(R.id.Spinner);

            bGoogle = findViewById(R.id.buttonWeb);
            Dico fais_la_rng = new Dico("main_dico");
            Dico testtest = new Dico("lalaland");
            DataFile df = new DataFile("dico.json",MainActivity.this);


           //Génération de l'objet ListDico en lisant le fichier
           try{
               lesDicos.generateFromJson();
           }catch(IOException e){
               e.printStackTrace();
           }
            cl = (ConstraintLayout)findViewById(R.id.Mainlayout_id);
           lesDicos.add(new Dico("+"));
            ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lesDicos.getDicoName());
            editText.setText(lesDicos.toJSON());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
            //Envoie des extras a la vue 2
            buttonGoToEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ajout_intent = new Intent(MainActivity.this, AjoutActivity.class);
                    String nameTemp = (String) spinner.getAdapter().getItem(spinner.getSelectedItemPosition());
                    ajout_intent.putExtra("name",nameTemp);
                    startActivity(ajout_intent);
                }

            });


            //Récupération des extras potentiel
            Intent intent_main = getIntent();
            if(intent_main != null){
                Bundle extras = intent_main.getExtras();
                if (extras != null){
                    Dico d = new Dico();
                    d.setName(extras.getString("name"));
                    d.setWords(extras.getStringArrayList("liste"));
                    //lesDicos.remplaceDico(d,d.getName());
                    editText.setText(d.getName());
                    Toast toast = Toast.makeText(MainActivity.this,extras.getString("name") + "\n " + lesDicos.getListe().toString(),Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
            //Bouton qui lance la RNG
            buttonRNG.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    String nameTemp = (String) spinner.getAdapter().getItem(spinner.getSelectedItemPosition());
                    Dico d = lesDicos.searchDico(nameTemp);
                    if (!(d.estVide()) ) {
                            int random = (int) (Math.random() * d.getWords().size());
                            editText.setText(d.getWords().get(random));
                            Animation animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.lefttoright);
                            editText.startAnimation(animation);

                    }else{
                        Toast toast = Toast.makeText(MainActivity.this,"La liste est vide",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });

            //Listeners du bouton de recherche sur le web
            bGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String search = "http://www.google.fr/search?q="+ (String) editText.getText();
                    Intent web = new Intent(Intent.ACTION_VIEW);
                    web.setData(Uri.parse(search));
                    startActivity(web);
                }
            });



    }
}