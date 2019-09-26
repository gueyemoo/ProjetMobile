package com.example.L3MobileProjet;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.L3MobileProjet.DB.DBClient;

import java.util.Random;

public class AdditionActivity extends AppCompatActivity {
    public static final String NOMBRE1_KEY = "nombre1_key";
    public static final String NOMBRE2_KEY = "nombre2_key";

    int nombre1;
    int nombre2;
    int nbOpe;
    int score;
    private DBClient mDb;
    EditText resultat;
    TextView correction;

    final int min = 4;
    final int max = 60;
     int nb1;
     int nb2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addition);

        mDb = DBClient.getInstance(getApplicationContext());

        resultat = (EditText) findViewById(R.id.resultat);


            // génération de 2 nombres aléatoires pour les opérations
            nb1 = new Random().nextInt((max - min) + 1) + min;
            nb2 = new Random().nextInt((max - min) + 1) + min;

            // récupération des opérandes contenus dans les extra d'intent. Si pas de valeur : prendre les 2 nb générés juste avant
            nombre1 = getIntent().getIntExtra(NOMBRE1_KEY,nb1);
            nombre2 = getIntent().getIntExtra(NOMBRE2_KEY,nb2);

        createCalcul();
    }


    private void createCalcul() {


        TextView calcul = (TextView) findViewById(R.id.calcul);
        calcul.setText(nombre1 + " + " + nombre2 + " = ");

    }

    public void valider(View view)
    {

        correction = (TextView) findViewById(R.id.correction);

        resultat.setFocusable(false);

        // si pas de réponse par l'utilisateur on met "0" pour pouvoir faire la compaison (toujours faux)

        if(resultat.getText().toString().equals(""))
        {
            resultat.setText("0");
        }

        // comparaison entre résultat attendu et celui de l'utilisateur

        if(Integer.parseInt(resultat.getText().toString()) != (nombre1 + nombre2)) {
            resultat.setTextColor(Color.parseColor("#FF0000"));
            correction.setText("Réponse : " + (nombre1+nombre2));
            correction.setTextColor(Color.parseColor("#008000"));
        }
        else {
            resultat.setTextColor(Color.parseColor("#008000"));
            correction.setText("Félicitations !!");
            correction.setTextColor(Color.parseColor("#008000"));
            score = ((MyApp) this.getApplication()).getScore(); // récupérer le score contenu dans la variable globale
            score++; // on incrémente
            ((MyApp) this.getApplication()).setScore(score); // et on la sauve de nouveau
        }

        if(((MyApp) this.getApplication()).getNbOpe() == 9) { // si le nb d'opérations (dans la variable globale) effectuées est égale à 9

            Button choixJeu = (Button) findViewById(R.id.valider);

            choixJeu.setText("RETOUR AUX JEUX");

            score = ((MyApp) this.getApplication()).getScore();

            saveScore(score);

            correction.setText("Ton score : " + score + " /10");

            ((MyApp) this.getApplication()).setNbOpe(0);
            ((MyApp) this.getApplication()).setScore(0);

            choixJeu.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ChoixCategorieExerciceActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });

        }

        else {

            nbOpe = ((MyApp) this.getApplication()).getNbOpe();
            nbOpe++;
            ((MyApp) this.getApplication()).setNbOpe(nbOpe);

            Button suivant = (Button) findViewById(R.id.valider);

            suivant.setText("SUIVANT");

            final int nombre1 = new Random().nextInt((max - min) + 1) + min;
            final int nombre2 = new Random().nextInt((max - min) + 1) + min;

            suivant.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),AdditionActivity.class);
                    intent.putExtra(AdditionActivity.NOMBRE1_KEY,nombre1);
                    intent.putExtra(AdditionActivity.NOMBRE2_KEY,nombre2);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });

        }

    }


    private void saveScore(final int score) {

        // Récupérer les informations contenues dans les vues
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


    @Override
    public void onBackPressed() {
        score = ((MyApp) this.getApplication()).getScore();
        saveScore(score);
        ((MyApp) this.getApplication()).setNbOpe(0);
        ((MyApp) this.getApplication()).setScore(0);
        finish();
    }


}
