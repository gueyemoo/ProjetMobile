package com.project.MobileProjet;

import com.project.MobileProjet.DB.DBClient;
import com.project.MobileProjet.DB.ScoreDuel;
import com.project.MobileProjet.model.QuestionQuiz;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    private DBClient mDb;
    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTDOWN_IN_MILLIS = 20000;

    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonConfirmNext;

    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private ArrayList<QuestionQuiz> questionQuizList;
    private int questionCounter;
    private int questionCountTotal;
    private QuestionQuiz currentQuestionQuiz;

    private int score;
    private boolean answered;

    private long backPressedTime;

    private int joueurActuel = 0;
    private boolean duel;
    private String pseudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        duel = getIntent().getBooleanExtra("duel",false);
        if(duel)
        {
            joueurActuel = getIntent().getIntExtra("joueurActuel",1);
            pseudo = getIntent().getStringExtra("nickname");
        }

        mDb = DBClient.getInstance(getApplicationContext());

        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        buttonConfirmNext = findViewById(R.id.button_confirm_next);

        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = textViewCountDown.getTextColors();

        if (savedInstanceState == null) {
            QuizDbHelper dbHelper = new QuizDbHelper(this);
            questionQuizList = dbHelper.getAllQuestions();
            questionCountTotal = questionQuizList.size();
            Collections.shuffle(questionQuizList);

            showNextQuestion();
        } else {
            questionQuizList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionQuizList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestionQuiz = questionQuizList.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);

            if (!answered) {
                startCountDown();
            } else {
                updateCountDownText();
                showSolution();
            }
        }

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuizActivity.this, "Sélectionnes une réponse", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });
    }

    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();

        if (questionCounter < questionCountTotal) {
            currentQuestionQuiz = questionQuizList.get(questionCounter);
            if(duel)
            {
                textViewQuestion.setText("Question pour " + pseudo + " ! " + currentQuestionQuiz.getQuestion());
            }
            else
            {
                textViewQuestion.setText(currentQuestionQuiz.getQuestion());
            }

            rb1.setText(currentQuestionQuiz.getOption1());
            rb2.setText(currentQuestionQuiz.getOption2());
            rb3.setText(currentQuestionQuiz.getOption3());

            questionCounter++;
            textViewQuestionCount.setText("QuestionQuiz: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            buttonConfirmNext.setText("VALIDER");

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else {
            finishQuiz();
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        textViewCountDown.setText(timeFormatted);

        if (timeLeftInMillis < 10000) {
            textViewCountDown.setTextColor(Color.RED);
        } else {
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }

    private void checkAnswer() {
        answered = true;

        countDownTimer.cancel();

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;

        if (answerNr == currentQuestionQuiz.getAnswerNr()) {
            score++;
            textViewScore.setText("Score: " + score);
        }

        showSolution();
    }

    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);

        switch (currentQuestionQuiz.getAnswerNr()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Réponse 1 correcte");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Réponse 2 correcte");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Réponse 3 correcte");
                break;
        }

        if (questionCounter < questionCountTotal) {
            buttonConfirmNext.setText("Suivante");
        } else {
            if(!duel || joueurActuel == 2 )
            {
                buttonConfirmNext.setText("RETOUR AUX JEUX");
            }

        }
    }

    private void finishQuiz() {
        if(duel)
        {
            if(joueurActuel == 2)
            {
                saveScoreDuel("quiz",((MyApp) this.getApplication()).getScore(),score);
                Intent intent = new Intent(this,ChoixCategorieExerciceActivity.class);
                intent.putExtra("duel",true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
            else
            {
                ((MyApp) QuizActivity.this.getApplication()).setScore(score);
                finish();
            }
        }
        else
        {
            saveScore(score);
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_SCORE, score);
            setResult(RESULT_OK, resultIntent);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Appuyer de nouveau pour quitter", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
        
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionQuizList);
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