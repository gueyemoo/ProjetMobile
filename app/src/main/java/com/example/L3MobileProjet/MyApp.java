package com.example.L3MobileProjet;

import android.app.Application;

public class MyApp extends Application
{
    private int id;
    private int nbOpe;
    private int score;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNbOpe() {
        return nbOpe;
    }

    public void setNbOpe(int nbOpe) {
        this.nbOpe = nbOpe;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
