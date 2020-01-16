package com.project.MobileProjet;

import android.app.Application;

public class MyApp extends Application
{
    private int id;
    private int nbOpe;
    private int score;


    private int idJ2;
    private int nbOpeJ2;
    private int scoreJ2;



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

    public int getScoreJ2() {
        return scoreJ2;
    }

    public void setScoreJ2(int scoreJ2) {
        this.scoreJ2 = scoreJ2;
    }

    public int getNbOpeJ2() {
        return nbOpeJ2;
    }

    public void setNbOpeJ2(int nbOpeJ2) {
        this.nbOpeJ2 = nbOpeJ2;
    }

    public int getIdJ2() {
        return idJ2;
    }

    public void setIdJ2(int idJ2) {
        this.idJ2 = idJ2;
    }
}

