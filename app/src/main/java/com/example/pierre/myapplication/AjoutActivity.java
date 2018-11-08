package com.example.pierre.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.*;

import org.w3c.dom.Text;

import java.io.IOException;

public class AjoutActivity extends AppCompatActivity {

    private Dico dico;

    private FloatingActionButton buttonComeBack;
    private ImageButton buttonAdd;
    private ListView listView;
    private EditText inputAddWord;
    private ListDico lesDicos;
    private EditText dicoName;
    private ImageButton delDico;

    public void initView(){
        dicoName = findViewById(R.id.TitleDico);
        buttonComeBack =  findViewById(R.id.buttonComeBack);
        buttonAdd =  findViewById(R.id.buttonAdd);
        listView = findViewById(R.id.listView);
        inputAddWord =  findViewById(R.id.inputAddWord);
        delDico = findViewById(R.id.delDico);
    }
public void gotoMain(){
    Intent intent_back_main = new Intent(AjoutActivity.this, MainActivity.class) ;
    startActivity(intent_back_main);
}
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout);
        dico = new Dico();
        initView();
        lesDicos = new ListDico(this);
        lesDicos.read(ListDico.LINK);
        //Récupération des extras
        Intent intent_main = getIntent();
        if(intent_main != null){
            Bundle extras = intent_main.getExtras();
            if (extras != null) {
                String name = extras.getString("name");
                dico = lesDicos.searchDico(name);
                dicoName.setHint(name);
            }
        }
        dicoName.setText(dico.getName());
       listView.setAdapter(new MyAdapter(this, dico.getWords()));
        //Renvoit des extras
        buttonComeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMain();
            }

        });

        delDico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lesDicos.del(dico);
                Gson gson = new Gson();
                lesDicos.sauvegarde(gson.toJson(lesDicos.getListe().toArray()),ListDico.LINK);
                gotoMain();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = inputAddWord.getText().toString();
                if( text != null ) {
                    dico.addWord(text);
                    lesDicos.remplaceDico(dico,dico.getName());
                    Gson gson = new Gson();
                    lesDicos.sauvegarde(gson.toJson(lesDicos.getListe().toArray()),ListDico.LINK);
                    listView.setAdapter(new MyAdapter(AjoutActivity.this, dico.getWords()));
                }
                else{
                    Toast toast = Toast.makeText(AjoutActivity.this,"Veuillez entrer quelque chose ! ",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                String choice =(String) o;
                dico.removeWord(choice);
                lesDicos.remplaceDico(dico,dico.getName());
                listView.setAdapter(new MyAdapter(AjoutActivity.this, dico.getWords()));
                Gson gson = new Gson();
                lesDicos.sauvegarde(gson.toJson(lesDicos.getListe().toArray()),ListDico.LINK);

            }
        });

        dicoName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dico.setName(dicoName.getText().toString());
                lesDicos.searchDico(dico.getName()).setName(dico.getName());
                dicoName.setHint(dico.getName());
                Gson gson = new Gson();
                lesDicos.sauvegarde(gson.toJson(lesDicos.getListe().toArray()),ListDico.LINK);


            }
        });

    }
}
