
package com.example.L3MobileProjet;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class ChoixCategorieExerciceActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_QUIZ = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu);

        ((MyApp) this.getApplication()).setNbOpe(0);


    }

    public void goQuizz(View view) {

        Intent intent = new Intent(this, QuizActivity.class);
        startActivityForResult(intent, REQUEST_CODE_QUIZ);

    }

    public void goMaths(View view) {

        Intent intent = new Intent(this, MathsActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);

    }

    public void langues_click(View view) {

        Intent intent = new Intent(this, FrancaisActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }

    public void pendu_click(View view) {

        Intent intent = new Intent(this, PenduActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }
}
