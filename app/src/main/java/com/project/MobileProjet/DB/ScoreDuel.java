package com.project.MobileProjet.DB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "scoreDuels")
public class ScoreDuel implements Serializable  {


    public ScoreDuel(int idJ1, int idJ2, int scoreJ1 , int scoreJ2)
    {
        this.idJ1 = idJ1;
        this.idJ2 = idJ2;
        this.scoreJ1 = scoreJ1;
        this.scoreJ2 = scoreJ2;
    }

    @PrimaryKey (autoGenerate = true)
    @NonNull
    private int id;

    private int idJ1;

    private int idJ2;

    private String exercice;

    private int scoreJ1;

    private int scoreJ2;

    public int getId() {return id;}

    public void setId(int id) { this.id = id;}


    public int getScoreJ2() {
        return scoreJ2;
    }

    public void setScoreJ2(int scoreJ2) {
        this.scoreJ2 = scoreJ2;
    }

    public int getScoreJ1() {
        return scoreJ1;
    }

    public void setScoreJ1(int scoreJ1) {
        this.scoreJ1 = scoreJ1;
    }

    public String getExercice() {
        return exercice;
    }

    public void setExercice(String exercice) {
        this.exercice = exercice;
    }



    public int getIdJ2() {
        return idJ2;
    }

    public void setIdJ2(int idJ2) {
        this.idJ2 = idJ2;
    }

    public int getIdJ1() {
        return idJ1;
    }

    public void setIdJ1(int idJ1) {
        this.idJ1 = idJ1;
    }
}
