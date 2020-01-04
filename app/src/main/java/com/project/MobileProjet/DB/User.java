package com.project.MobileProjet.DB;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "users", indices = {@Index(value = {"pseudo"}, unique = true)})
public class User implements Serializable  {


        private String pseudo;

        @PrimaryKey (autoGenerate = true)
        @NonNull
        private int id;

        private int age;

        private String sexe;

        private int photo;

        private int score;

    public String getSexe() {
        return sexe;
    }

    public int getAge() {
        return age;
    }

    public String getPseudo() {
        return pseudo;
    }

    public int getPhoto() {
        return photo;
    }

    public int getId() {return id;}

    public void setAge(int age) {
        this.age = age;
    }

    public void setPseudo(String pseudo) {

        this.pseudo = pseudo;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public void setId(int id) { this.id = id;}

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
