package com.project.MobileProjet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ChoixCoursActivity extends AppCompatActivity {

    String cours;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cours);
    }

    public void goAddition(View view) {
        Intent intent = new Intent(this, CoursHTML.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("cours","addition");

        startActivity(intent);
    }

    public void goMultiplication(View view) {
        Intent intent = new Intent(this, CoursHTML.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("cours","multiplication");

        startActivity(intent);
    }

    public void goOrthographe(View view) {
        Intent intent = new Intent(this, CoursPDF.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("cours","orthographe");

        startActivity(intent);
    }

    public void goConjugaison(View view) {
        Intent intent = new Intent(this, CoursPDF.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("cours","conjugaison");

        startActivity(intent);
    }

    public void goPays(View view) {
        Intent intent = new Intent(this, CoursPDF.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("cours", "pays");

        startActivity(intent);
    }
}
