package com.project.MobileProjet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.MobileProjet.DB.DBClient;
import com.project.MobileProjet.model.DonneePays;
import com.project.MobileProjet.model.ObjetPays;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PaysActivity extends AppCompatActivity {

    private DBClient mDb;

    EditText reponse;
    ImageView drapeau;
    TextView commentaire;
    int curQuestions = 0;
    int i = 0;
    Button bouton_continuer;
    TextView score;


    Random r;

    List<ObjetPays> list;
    private long backPressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pays);

        instruction(); //On charge les instructions de la partie

        mDb = DBClient.getInstance(getApplicationContext());

        score = (TextView) findViewById(R.id.score_pays);
        drapeau = (ImageView) findViewById(R.id.drapeau);
        reponse = (EditText) findViewById(R.id.reponse_pays);
        commentaire = (TextView) findViewById(R.id.commentaire_pays);

        bouton_continuer = (Button) findViewById(R.id.continuer);

        list = new ArrayList<>();

        int max = DonneePays.drapeaux.length;
        score.setText("Question: "+i+"/"+ max );

        //on ajoute tout les drapeaux à la liste
        for(int i = 0; i < new DonneePays().reponses.length; i++){
            list.add(new ObjetPays(new DonneePays().reponses[i], new DonneePays().drapeaux[i]));
        }

        //On mélange la list
        Collections.shuffle(list);

        //On recupere l'image actuel
        drapeau.setImageResource(list.get(curQuestions).getImages());

        reponse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(reponse.getText().toString().equalsIgnoreCase(list.get(curQuestions).getNom())){

                    bouton_continuer.setVisibility(View.VISIBLE);
                    commentaire.setVisibility(View.VISIBLE);
                    commentaire.setText("Bravo !");
                    commentaire.setTextColor(Color.GREEN);
                }else{
                    bouton_continuer.setVisibility(View.INVISIBLE);
                    commentaire.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!reponse.getText().toString().equalsIgnoreCase(list.get(curQuestions).getNom())){
                    if(reponse.getText().toString().isEmpty()){
                        commentaire.setVisibility(View.INVISIBLE);
                    }else{
                        commentaire.setVisibility(View.VISIBLE);
                        commentaire.setText("Ce n'est pas ce pays :/");
                        commentaire.setTextColor(Color.YELLOW);
                    }
                }
            }
        });

        bouton_continuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int max = DonneePays.drapeaux.length;
                i++;
                score.setText("Question: "+i+"/"+ max );

                //Question Suivante
                if (curQuestions < DonneePays.drapeaux.length - 1) {
                    curQuestions++;
                    drapeau.setImageResource(list.get(curQuestions).getImages());
                    bouton_continuer.setVisibility(View.INVISIBLE);
                    commentaire.setVisibility(View.INVISIBLE);
                    reponse.setText("");
                    commentaire.setText("");
                } else {
                    //Il n'y a plus de question fin de partie
                    commentaire.setTextColor(Color.GREEN);
                    bouton_continuer.setVisibility(View.VISIBLE);
                    commentaire.setVisibility(View.VISIBLE);
                    reponse.setEnabled(false);
                    saveScore(i);
                    commentaire.setText("BRAVO ! Tu as trouvé tout les pays !");
                    bouton_continuer.setText("Retour Choix Catégorie");
                    bouton_continuer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), ChoixCategorieExerciceActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                }
            }
        });

        }


    private void instruction(){ //Cette fonction affiche les instructions de l'exercice
        String htmlString ="<u>INSTRUCTION:</u> Dans cet exercice tu dois trouver le nom du pays correspondant au drapeau <font color=\"red\">ATTENTION</font> aux espaces à la fin des mots. Ce n'est pas grave si tu oublies les accents sur les 'e'.";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PaysActivity.this);
        alertDialogBuilder.setMessage(Html.fromHtml(htmlString)).setCancelable(false).setPositiveButton("Compris",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finish();
        } else {
            Toast.makeText(this, "Appuie de nouveau pour quitter", Toast.LENGTH_SHORT).show();
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

}
