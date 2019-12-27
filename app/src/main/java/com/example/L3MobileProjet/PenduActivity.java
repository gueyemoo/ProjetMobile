package com.example.L3MobileProjet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.L3MobileProjet.DB.DBClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PenduActivity extends AppCompatActivity implements View.OnClickListener {

    //les DATA
    private LinearLayout container;
    private Button btn_send;
    private TextView lettres_tapees;
    private EditText letter;
    private ImageView image;
    private String word;
    private int found;
    private int error;
    private List<Character> listOfLetters = new ArrayList<>();
    private boolean win;
    private List<String> listeDeMot = new ArrayList<>();
    private long backPressedTime;
    private DBClient mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendu);

        mDb = DBClient.getInstance(getApplicationContext());

        instruction();//charge l'instruction de l'exercice

        //Je recupere mes vues
        container = (LinearLayout) findViewById(R.id.word_container);
        btn_send = (Button) findViewById(R.id.btn_send);
        letter = (EditText) findViewById(R.id.et_letter);
        image = (ImageView) findViewById(R.id.iv_pendu);
        lettres_tapees = (TextView) findViewById(R.id.tv_lettres_tapees);

        initGame();

        btn_send.setOnClickListener(this);
    }

    public void initGame(){ //Initialise la partie avec les informations de base

        word = genereMot();
        win = false;
        error = 0;
        found = 0;
        lettres_tapees.setText("");
        image.setBackgroundResource(R.drawable.first);
        listOfLetters = new ArrayList<>();

        container.removeAllViews();

        for(int i = 0; i < word.length(); i++){//on ajoute le nombre de case necessaire pour mettre toute les lettres du mot à deviner
            TextView oneLetter = (TextView) getLayoutInflater().inflate(R.layout.textview, null);
            container.addView(oneLetter);
        }
    }


    @Override
    public void onClick(View v) {

        String lettreFromInput = letter.getText().toString().toUpperCase();
        letter.setText(""); // on vide apres chaque envoie l'input

        if(lettreFromInput.length() > 0){ //on verifie que ce n'est pas vide
            if(!letterDejaUtilise(lettreFromInput.charAt(0), listOfLetters)){ //Si la lettre n'est pas utilisé encore
                listOfLetters.add(lettreFromInput.charAt(0)); // on l'ajoute
                checkIfLetterIsInWord(lettreFromInput, word);
            }

            if(found == word.length()){ // La partie est gagné
                win = true;
                createDialog(win); // affiche un message pour rejouer ou retourner au menu
            }

            if(!word.contains(lettreFromInput)){//La lettre n'est pas dans le mot
                error++;
            }

            setImage(error); //pour mettre la bonne image sur le pendu

            if(error == 6){
                win = false;
                createDialog(win); // affiche un message pour rejouer ou retourner au menu
            }

            showAllLetters(listOfLetters); //on affiche les lettres entrées
        }
    }

    public boolean letterDejaUtilise(char a, List<Character> listOfLetters){

        for (int i = 0; i < listOfLetters.size(); i++){
            if(listOfLetters.get(i) == a){ //On verifie si la lettre existe deja
                Toast.makeText(this, "Vous avez déja entré cette lettre", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    public void checkIfLetterIsInWord(String letter, String word){

        for (int i = 0; i < word.length(); i++){
            if(letter.equals(String.valueOf(word.charAt(i)))){//Si vrai : lettre trouver
                TextView tv = (TextView) container.getChildAt(i);
                tv.setText(String.valueOf(word.charAt(i)));
                found++; //car lettre trouver
            }
        }

    }

    public void showAllLetters(List<Character> listOfLetters){
        String chaine = "";

        for (int i = 0; i < listOfLetters.size(); i++){
            chaine += listOfLetters.get(i) + "\n"; //afficher les lettres saisi les une au dessous des autres
        }
        if(!chaine.equals("")){
            lettres_tapees.setText(chaine);
        }
    }

    public void setImage(int error){ // On mets en place l'image du pendu en fonction du nombre d'erreur
        switch (error){
            case 1:
                image.setBackgroundResource(R.drawable.second);
                break;

            case 2:
                image.setBackgroundResource(R.drawable.third);
                break;

            case 3:
                image.setBackgroundResource(R.drawable.fourth);
                break;

            case 4:
                image.setBackgroundResource(R.drawable.fifth);
                break;

            case 5:
                image.setBackgroundResource(R.drawable.sixth);
                break;

            case 6:
                image.setBackgroundResource(R.drawable.seventh);
                break;
        }
    }

    public void createDialog(boolean win){ //Message de fin de partie à afficher
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Vous avez gagné");

        if(!win){
            builder.setTitle("Vous avez perdu !");
            builder.setMessage("Le bon mot était: " + word);
        }else{
            saveScore(10);
        }

        builder.setPositiveButton("Rejouer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initGame(); //on réinitialise la partie
            }
        }).setNegativeButton("Retour Choix Catégorie",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        builder.create().show();
    }

    public List<String> getListeDeMot(){ //Cette fonction recupere un mot présent dans le fichier texte "pendu_liste.txt" pour le faire deviner à l'utilisateur
        try {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(getAssets().open(("pendu_liste.txt"))));
            String line;
            while((line = buffer.readLine()) != null) { //Si il trouve un mot dans le fichier il l'ajoute
                listeDeMot.add(line);
            }
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listeDeMot;
    }

    public String genereMot(){//Recupere un mot aléatoire de la liste de mot
        listeDeMot = getListeDeMot();
        int random = (int) (Math.floor(Math.random() * listeDeMot.size()));
        String mot = listeDeMot.get(random).trim();
        return mot;

    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finish();
        } else {
            Toast.makeText(this, "Appuyer de nouveau pour quitter", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();

    }

    private void saveScore(final int score) {

        final int userId;
        userId = ((MyApp) this.getApplication()).getId();


        class SaveScore extends AsyncTask<Void, Void, Boolean> {

            @Override
            protected Boolean doInBackground(Void... voids) {

                // adding to database
                mDb.getAppDatabase()
                        .mydao()
                        .addScore(score, userId);

                return true;
            }

        }

        SaveScore st = new SaveScore();
        st.execute();
    }


    private void instruction(){ //Cette fonction affiche les instructions de l'exercice
        String htmlString ="<u>INSTRUCTION:</u> Dans cet exercice il faut deviner le bon mot <font color=\"red\">ATTENTION</font> à chaque erreur un membre du pendu se mets en place. Chaque mot deviner rapporte 10 points.";
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PenduActivity.this);
        alertDialogBuilder.setMessage(Html.fromHtml(htmlString)).setCancelable(false).setPositiveButton("Compris",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
