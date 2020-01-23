package com.project.MobileProjet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.project.MobileProjet.DB.DBClient;
import com.project.MobileProjet.model.Objet;
import com.project.MobileProjet.model.QuestionFrancais;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FrancaisActivity extends AppCompatActivity {

    TextView question;
    EditText reponse;
    TextView commentaire;
    Button bouton_continuer;
    TextView scoreView;
    TextView statutQuestion;
    private int score;
    private DBClient mDb;
    public static final String EXTRA_SCORE = "extraScore";


    List<Objet> questions;
    int curQuestions = 0;
    int i = 0;
    Random r;

    private long backPressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_francais);

        mDb = DBClient.getInstance(getApplicationContext());


        instruction(); //on charge les instructions de l'exercice

        //Je récuper les vues
        scoreView = (TextView) findViewById(R.id.score_en);
        statutQuestion = (TextView) findViewById(R.id.statutQuestion_en);
        question = (TextView) findViewById(R.id.question_en);
        reponse = (EditText) findViewById(R.id.reponse_en);
        commentaire = (TextView) findViewById(R.id.commentaire);
        bouton_continuer = (Button) findViewById(R.id.continue_button);

        bouton_continuer.setVisibility(View.INVISIBLE);
        commentaire.setVisibility(View.INVISIBLE);

        final int max = 10;
        statutQuestion.setText("Question: " + i + "/" + max);
        scoreView.setText("Score : " + score);


        r = new Random();

        questions = new ArrayList<>(); // on crée un arraylist de question
        for (int i = 0; i < QuestionFrancais.questions.length; i++) {
            // ajoute toute les questions et les reponses au jeu
            questions.add(new Objet(QuestionFrancais.questions[i], QuestionFrancais.reponses[i]));
        }

        // melange les questions
        Collections.shuffle(questions);
        // On recupere la question actuel
        question.setText(questions.get(curQuestions).getQuestion());

        reponse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { // si le text est entrain d'être changer
                if (reponse.getText().toString().equalsIgnoreCase(questions.get(curQuestions).getReponse())) {
                    bouton_continuer.setVisibility(View.VISIBLE);
                    commentaire.setVisibility(View.VISIBLE);
                    commentaire.setText("Bravo !");
                    commentaire.setTextColor(Color.GREEN);

                } else {
                    bouton_continuer.setVisibility(View.VISIBLE);
                    commentaire.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {// si les modifications sur le texte sont terminé
                if (!reponse.getText().toString().equalsIgnoreCase(questions.get(curQuestions).getReponse())) {
                    if (reponse.getText().toString().isEmpty()) {
                        commentaire.setVisibility(View.INVISIBLE);
                    } else {
                        commentaire.setVisibility(View.VISIBLE);
                        commentaire.setText("Ce n'est pas ce mot :/");
                        commentaire.setTextColor(Color.YELLOW);
                    }
                }
            }
        });

        bouton_continuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                // score.setText("Question: "+i+"/"+QuestionFrancais.questions.length);
                //Question Suivante
                if (curQuestions < QuestionFrancais.questions.length - 1) {
                    curQuestions++;
                    score++; //on incrémente le score
                    scoreView.setText("Score : " + score); // on remets a jour le score du quiz
                    question.setText(questions.get(curQuestions).getQuestion()); //on passe a la question suivante
                    statutQuestion.setText("Question: " + i + "/" + max); // mise a jour de la sitaution du quiz
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
                    //commentaire.setText("");
                    saveScore(score);
                    bouton_continuer.setText("Retour aux exercices");
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

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finish();
        } else {
            Toast.makeText(this, "Appuie de nouveau pour quitter", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();

    }


    private void instruction() { //Cette fonction affiche les instructions de l'exercice
        String htmlString = "<u>INSTRUCTION:</u> Dans cet exercice tu dois  trouver le <font color=\"red\">VERBE</font> utilisé et l'écrire à l'infinitif.";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FrancaisActivity.this);
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

    private void finishQuiz() {
        saveScore(score);
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();

    }
}

