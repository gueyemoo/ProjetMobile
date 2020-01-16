package com.project.MobileProjet;;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;


import com.joanzapata.pdfview.PDFView;

import java.io.IOException;
import java.io.InputStream;

public class CoursHTML extends AppCompatActivity {

    String coursChoisi = "addition"; //default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courshtml);
        InputStream html;
        byte[] buffer = new byte[0];
        PDFView pdfView = findViewById(R.id.pdfView);




        coursChoisi = getIntent().getExtras().getString("cours");
        switch (coursChoisi) {
            case "addition" :

                try {
                    html = getAssets().open("coursAdd.html");
                    int size = html.available();
                    buffer = new byte[size];
                    html.read(buffer);
                    html.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "multiplication" :
                try {
                    html = getAssets().open("coursMult.html");
                    int size = html.available();
                    buffer = new byte[size];
                    html.read(buffer);
                    html.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default :
                try {
                    html = getAssets().open("coursAdd.html");
                    int size = html.available();
                    buffer = new byte[size];
                    html.read(buffer);
                    html.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        String text = new String(buffer);
        TextView textCours = findViewById(R.id.text_cours);
        textCours.setText(Html.fromHtml(text));
    }


}
