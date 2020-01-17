package com.project.MobileProjet;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.MobileProjet.DB.DBClient;
import com.project.MobileProjet.DB.ScoreDuel;
import com.project.MobileProjet.R;
import com.project.MobileProjet.model.Multiplication;
import com.project.MobileProjet.model.TableMultiplication;

public class MultiplicationsActivity extends AppCompatActivity {

    public static final String TABLE_KEY = "table_key";
    int table = 0;
    private DBClient mDb;
    int nbErreurs =0;

    // DATA
    TableMultiplication tableMultiplication;

    // VIEW
    LinearLayout linear;

    private int joueurActuel = 0;
    private boolean duel;
    private String pseudo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplications);
        table = getIntent().getIntExtra(TABLE_KEY,0);
        duel = getIntent().getBooleanExtra("duel",false);
        if(duel)
        {
            joueurActuel = getIntent().getIntExtra("joueurActuel",1);
            pseudo = getIntent().getStringExtra("nickname");
        }

        mDb = DBClient.getInstance(getApplicationContext());


        // 1. Initialiser les données
        tableMultiplication = new TableMultiplication(table, 10);

        // 2. Récupérer les éléments graphiques
        linear = (LinearLayout) findViewById(R.id.inflate_linear);

        // 3. Mettre à jour l'interface utilisateur
        miseAJourUI();

        //4.Charger la question modifié en cas de duel
        if(duel)
        {
            chargerQuestion();
        }
    }

    private void chargerQuestion()
    {
        final int userId;
        final TextView question = findViewById(R.id.question);
            question.setText("Au tour de " + pseudo + " ! ");
    }


    //
    private void miseAJourUI() {

        // 1. Boucler pour générer la table
        for (Multiplication multiplication : tableMultiplication.getMultiplications()) {

            // 2. Création de la ligne temporaire
            LinearLayout linearTMP = (LinearLayout) getLayoutInflater().inflate(R.layout.template_calc_mul, linear,false);

            // 3. Création du texte décrivant l'opération
            TextView calcul = (TextView) linearTMP.findViewById(R.id.template_calcul);
            calcul.setText(multiplication.getOperande1() + " x " + multiplication.getOperande2() + " = ");

            // 4. Récupération de l'editText permettant d'interagir avec l'utilisateur
            EditText resultat = (EditText) linearTMP.findViewById(R.id.template_resultat);


            // 5. Ajout au linear principal
            linear.addView(linearTMP);
        }

    }

    public void valider(View view) {
        Button choixJeu = (Button) findViewById(R.id.valider);

        TextView correction = (TextView) findViewById(R.id.correction);

        for (Multiplication multiplication : tableMultiplication.getMultiplications()) {

            EditText resultat = (EditText) findViewById(R.id.template_resultat);
            TextView reponse = (TextView) findViewById(R.id.reponse);

            resultat.setFocusable(false);

            if (resultat.getText().toString().equals("")) {
                resultat.setText("0");
            }

            if (Integer.parseInt(resultat.getText().toString()) != (multiplication.getOperande1() * multiplication.getOperande2())) {
                resultat.setTextColor(Color.parseColor("#FF0000"));
                reponse.setText("Réponse : " + multiplication.getOperande1() * multiplication.getOperande2());
                reponse.setTextColor(Color.parseColor("#008000"));
                nbErreurs++;
            } else {
                resultat.setTextColor(Color.parseColor("#008000"));
            }

            resultat.setId(0);
            reponse.setId(0);

        }

        if(duel && joueurActuel == 1) {
            ((MyApp) this.getApplication()).setScore(10 - nbErreurs);
        }
        validerSaisiesJoueur();
    }

    private void validerSaisiesJoueur()
    {
        Button choixJeu = (Button) findViewById(R.id.valider);

        TextView correction = (TextView) findViewById(R.id.correction);

        if(duel)
        {

            if(joueurActuel == 2)
            {
                choixJeu.setText("TETS");
                saveScoreDuel("multiplication",((MyApp) this.getApplication()).getScore(),10-nbErreurs);
                ((MyApp) MultiplicationsActivity.this.getApplication()).setScore(10-nbErreurs);
                choixJeu.setOnClickListener( new OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ChoixCategorieExerciceActivity.class);
                        intent.putExtra("duel",true);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }else{
                ((MyApp) this.getApplication()).setScore(10-nbErreurs);
                choixJeu.setText("Au tour de " + pseudo);
                choixJeu.setOnClickListener( new OnClickListener() {
                    public void onClick(View v) {
                        finish();
                    }
                });

            }
        }
        else
        {
            saveScore(10-nbErreurs);

            choixJeu.setText("RETOUR AUX JEUX");

            choixJeu.setOnClickListener( new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ChoixCategorieExerciceActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
            correction.setText("Score: " + (10-nbErreurs) + " " + "/ 10");
            correction.setTextColor(Color.parseColor("#008000"));
        }

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
                        .addScore(score,userId);

                return true;
            }

        }

        SaveScore st = new SaveScore();
        st.execute();
    }

    private void saveScoreDuel(final String exercice, final int scoreJ1, final int scoreJ2)
    {
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


}


