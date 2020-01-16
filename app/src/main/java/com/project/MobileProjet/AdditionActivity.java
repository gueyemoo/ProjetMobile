package com.project.MobileProjet;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.project.MobileProjet.DB.DBClient;
import com.project.MobileProjet.DB.ScoreDuel;
import com.project.MobileProjet.DB.User;

import java.util.Random;

public class AdditionActivity extends AppCompatActivity {

    int nombre1;
    int nombre2;

    int nbOpeJ1;
    int scoreJ1;

    int nbOpeJ2;
    int scoreJ2;

    int joueurActuel;

    private DBClient mDb;

    final int min = 4;
    final int max = 20;

    private boolean duel;

    //GUI Elements

    EditText resultat;
    TextView correction;
    TextView question;
    TextView calcul;
    Button questionSuivante;

    //
    private  String nicknameP1;
    private  String nicknameP2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent currentIntent = getIntent();
        duel = currentIntent.getBooleanExtra("duel", false);

        //Par défaut si l'activité n'est pas lancé en tant que duel, c'est toujours au j1
        joueurActuel = currentIntent.getIntExtra("joueurActuel", 1);

        setContentView(R.layout.activity_addition);

        mDb = DBClient.getInstance(getApplicationContext());

        resultat = findViewById(R.id.resultat);
        questionSuivante = findViewById(R.id.valider);
        question = findViewById(R.id.question);
        calcul = findViewById(R.id.calcul);

        nbOpeJ1 = ((MyApp) this.getApplication()).getNbOpe();
        scoreJ1 = ((MyApp) this.getApplication()).getScore();
        if(duel)
        {
            nbOpeJ2 = ((MyApp) this.getApplication()).getNbOpeJ2();
            scoreJ2 = ((MyApp) this.getApplication()).getScoreJ2();
            nicknameP1 = getIntent().getStringExtra("nicknameP1");
            nicknameP2 = getIntent().getStringExtra("nicknameP2");

        }

        // génération de 2 nombres aléatoires pour les opérations

        nombre1 = new Random().nextInt((max - min) + 1) + min;
        nombre2 = new Random().nextInt((max - min) + 1) + min;

        resultat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (resultat.getText().toString().equals(Integer.toString(nombre1 + nombre2))) {
                    validerEntreeJoueur();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (FinDeJeuAtteinte()) {

            if(duel)
            {
                saveScoreDuel("additions",scoreJ1,scoreJ2);
            }
            questionSuivante.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ChoixCategorieExerciceActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("duel",true);
                    intent.putExtra("nicknameP1",nicknameP1);
                    intent.putExtra("nicknameP2",nicknameP2);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            if (duel) {
                questionSuivante.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if(joueurActuel == 1)
                        {
                            ((MyApp) AdditionActivity.this.getApplication()).setNbOpe(++nbOpeJ1);
                        }
                        else
                        {
                            ((MyApp) AdditionActivity.this.getApplication()).setNbOpeJ2(++nbOpeJ2);
                        }
                        Intent intent = new Intent(v.getContext(), AdditionActivity.class);
                        intent.putExtra("duel", true);
                        if (nbOpeJ1 == 9) {
                            intent.putExtra("joueurActuel", 2);
                        } else {
                            intent.putExtra("joueurActuel", 1);
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("nicknameP1",nicknameP1);
                        intent.putExtra("nicknameP2",nicknameP2);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                questionSuivante.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), AdditionActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ((MyApp) AdditionActivity.this.getApplication()).setNbOpe(++nbOpeJ1);
                        intent.putExtra("nicknameP1",nicknameP1);
                        intent.putExtra("nicknameP2",nicknameP2);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }
        createCalcul();
    }


    private boolean FinDeJeuAtteinte() {
        if (duel) {
            if ( nbOpeJ1 == 9 &&  nbOpeJ2 == 9) {
                return true;
            }
            return false;
        } else {
            if ( nbOpeJ1 == 9) {
                return true;
            }
            return false;
        }
    }


    private void createCalcul() {
        TextView joueur = findViewById(R.id.joueur);

        if (duel) {

            if (joueurActuel == 1) {
                joueur.setText("Au tour de " + nicknameP1);

            } else {
                joueur.setText("Au tour de " + nicknameP2);

            }

        }
        question.setText("Combien font " + nombre1 + " et " + nombre2 + " ? ");
        calcul.setText(nombre1 + " + " + nombre2 + " = ");
    }




    private void validerEntreeJoueur() {
        correction = findViewById(R.id.correction);

        resultat.setFocusable(false);

        // si pas de réponse par l'utilisateur on met "0" pour pouvoir faire la comparaison (toujours faux)

        if (resultat.getText().toString().equals("")) {
            resultat.setText("0");
        }

        // comparaison entre résultat attendu et celui de l'utilisateur

        if (Integer.parseInt(resultat.getText().toString()) != (nombre1 + nombre2)) {
            resultat.setTextColor(Color.parseColor("#FF0000"));
            correction.setText("Réponse : " + (nombre1 + nombre2));
            correction.setTextColor(Color.parseColor("#008000"));


        } else {
            resultat.setTextColor(Color.parseColor("#008000"));
            correction.setText("Félicitations !!");
            correction.setTextColor(Color.parseColor("#008000"));

            //Cas classique, si il s'agit du joueur 1
            if (joueurActuel == 1) {
                scoreJ1 = ((MyApp) this.getApplication()).getScore(); // récupérer le score contenu dans la variable globale
                scoreJ1++; // on incrémente
                ((MyApp) this.getApplication()).setScore(scoreJ1); // et on la sauve de nouveau
            } else //Sinon, si jamais il y a un second joueur
            {
                scoreJ2 = ((MyApp) this.getApplication()).getScoreJ2(); // récupérer le score contenu dans la variable globale
                scoreJ2++; // on incrémente

                ((MyApp) this.getApplication()).setScoreJ2(scoreJ2); // et on la sauve de nouveau
            }

        }
    }


    private void saveScoreDuel(final String exercice, final int scoreJ1, final int scoreJ2) {
        final int userIdJ1 = ((MyApp) this.getApplication()).getId();
        final int userIdJ2 = ((MyApp) this.getApplication()).getIdJ2();

        class SaveScoreDuel extends AsyncTask < Void, Void, Boolean > {

            @Override
            protected Boolean doInBackground(Void...voids) {

                // adding to database
                mDb.getAppDatabase()
                        .mydao()
                        .addScoreDuel(new ScoreDuel(userIdJ1, userIdJ2, scoreJ1, scoreJ2));

                return true;
            }

        }
        SaveScoreDuel sSD = new SaveScoreDuel();
        sSD.execute();

    }


    private void saveScore(final int score, int joueurActuel) {

        // Récupérer les informations contenues dans les vues
        final int userId;
        userId = ((MyApp) this.getApplication()).getId();

        class SaveScore extends AsyncTask < Void, Void, Boolean > {

            @Override
            protected Boolean doInBackground(Void...voids) {

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
        if (joueurActuel != 1 && duel) {
            scoreJ2 = ((MyApp) this.getApplication()).getScoreJ2();
            saveScore(scoreJ2, 2);
            ((MyApp) this.getApplication()).setNbOpeJ2(0);
            ((MyApp) this.getApplication()).setScoreJ2(0);
        }
        saveScore(scoreJ1, 2);
        ((MyApp) this.getApplication()).setNbOpe(0);
        ((MyApp) this.getApplication()).setScore(0);
        finish();
    }


}
