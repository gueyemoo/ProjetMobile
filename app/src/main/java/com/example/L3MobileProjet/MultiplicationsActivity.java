package com.example.L3MobileProjet;

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

import com.example.L3MobileProjet.DB.DBClient;
import com.example.L3MobileProjet.model.Multiplication;
import com.example.L3MobileProjet.model.TableMultiplication;

public class MultiplicationsActivity extends AppCompatActivity {

    public static final String TABLE_KEY = "table_key";
    int table = 0;
    private DBClient mDb;
    int nbErreurs =0;

    // DATA
    TableMultiplication tableMultiplication;

    // VIEW
    LinearLayout linear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplications);
        table = getIntent().getIntExtra(TABLE_KEY,0);

        mDb = DBClient.getInstance(getApplicationContext());


        // 1. Initialiser les données
        tableMultiplication = new TableMultiplication(table, 10);

        // 2. Récupérer les éléments graphiques
        linear = (LinearLayout) findViewById(R.id.inflate_linear);

        // 3. Mettre à jour l'interface utilisateur
        miseAJourUI();
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

    public void valider(View view)
    {
        Button choixJeu = (Button) findViewById(R.id.valider);

        TextView correction = (TextView) findViewById(R.id.correction);

        for (Multiplication multiplication : tableMultiplication.getMultiplications()) {

            EditText resultat = (EditText) findViewById(R.id.template_resultat);
            TextView reponse = (TextView) findViewById(R.id.reponse);

            resultat.setFocusable(false);

            if(resultat.getText().toString().equals(""))
            {
                resultat.setText("0");
            }

            if(Integer.parseInt(resultat.getText().toString()) != (multiplication.getOperande1()*multiplication.getOperande2())) {
                resultat.setTextColor(Color.parseColor("#FF0000"));
                reponse.setText("Réponse : " + multiplication.getOperande1()*multiplication.getOperande2());
                reponse.setTextColor(Color.parseColor("#008000"));
                nbErreurs++;
            }
            else {
                resultat.setTextColor(Color.parseColor("#008000"));
            }

            resultat.setId(0);
            reponse.setId(0);

        }

            correction.setText("Score: " + (10-nbErreurs) + " " + "/ 10");
             correction.setTextColor(Color.parseColor("#008000"));

        saveScore(10-nbErreurs);

        choixJeu.setText("RETOUR AUX JEUX");

        choixJeu.setOnClickListener( new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChoixCategorieExerciceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

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


}


