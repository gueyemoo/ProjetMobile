package com.project.MobileProjet;;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.widget.TextView;

import com.joanzapata.pdfview.PDFView;


public class CoursPDF extends AppCompatActivity {

    String coursChoisi = "addition"; //default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courspdf);
        PDFView pdfView = findViewById(R.id.pdfView);
        TextView titre = findViewById(R.id.titre_cours);



        coursChoisi = getIntent().getExtras().getString("cours");
        switch (coursChoisi) {
            case "orthographe" :
                titre.setText("Leçon d'Orthoographe");
                pdfView.fromAsset("coursOrth.pdf").defaultPage(1).showMinimap(false).enableSwipe(true).load();
                break;
            case "conjugaison" :
                titre.setText("Leçon de Conjugaison");
                pdfView.fromAsset("coursConju.pdf").defaultPage(1).showMinimap(false).enableSwipe(true).load();
                break;
            case "pays" :
                titre.setText("Tableau des pays");
                pdfView.fromAsset("pays.pdf").defaultPage(1).showMinimap(false).enableSwipe(true).load();
                break;
            default :
                titre.setText("Leçon de Conjugaison");
                pdfView.fromAsset("coursConju.pdf").defaultPage(1).showMinimap(false).enableSwipe(true).load();
        }


    }
}
