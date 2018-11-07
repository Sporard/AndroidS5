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
    private FloatingActionButton buttonAdd;
    private ListView listView;
    private EditText inputAddWord;
    private ListDico lesDicos;
    private EditText dicoName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout);

        dico = new Dico();
        lesDicos = new ListDico(this);
        try{
            lesDicos.generateFromJson();
        }catch (IOException e) {
            e.printStackTrace();
        }
        dicoName = findViewById(R.id.TitleDico);
        buttonComeBack =  findViewById(R.id.buttonComeBack);
        buttonAdd =  findViewById(R.id.buttonAdd);
        listView = findViewById(R.id.listView);
        inputAddWord =  findViewById(R.id.inputAddWord);

        //Récupération des extras
        Intent intent_main = getIntent();
        if(intent_main != null){
            Bundle extras = intent_main.getExtras();
            if (extras != null){
                String name = extras.getString("name");
                if(lesDicos.searchDico(name) != null ) {
                    dico = lesDicos.searchDico(name);
                    dicoName.setHint(name);
                }else if(name.equals("+")){
                    dico.setName(name);
                    lesDicos.add(dico);

                }
            }

        }
        dicoName.setText(dico.getName());
        DataFile dfTest = new DataFile("dico.json",AjoutActivity.this);
       listView.setAdapter(new MyAdapter(this, dico.getWords()));
        //Renvoit des extras
        buttonComeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_back_main = new Intent(AjoutActivity.this, MainActivity.class) ;
                intent_back_main.putExtra("liste",dico.getWords());
                intent_back_main.putExtra("name",dico.getName());
                startActivity(intent_back_main);
            }

        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = inputAddWord.getText().toString();
                if( text != null ) {
                    dico.addWord(text);
                    lesDicos.searchDico(dico.getName()).setWords(dico.getWords());
//                    try{
//                        lesDicos.write();
//                    }catch(IOException e){
//                        e.printStackTrace();
//                    }
                    Gson gson = new Gson();

                    DataFile df = new DataFile("dico.json",AjoutActivity.this);
                    String s;
                    df.write(dico.toJSON());
                    listView.setAdapter(new MyAdapter(AjoutActivity.this, dico.getWords()));
                    //s = df.read();
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context,dico.toJSON(),duration);
                    toast.show();
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

                listView.setAdapter(new MyAdapter(AjoutActivity.this, dico.getWords()));
                Toast toast = Toast.makeText(AjoutActivity.this,choice,Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        dicoName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    dico.setName(dicoName.getText().toString());
                    lesDicos.searchDico(dico.getName()).setName(dico.getName());
                    dicoName.setHint(dico.getName());
//                    try{
//                        lesDicos.write();
//                    }catch(IOException e){
//                             e.printStackTrace();
//                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
