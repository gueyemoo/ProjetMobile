package com.example.L3MobileProjet.model;

public class Objet {
    private String question, reponse;

    public Objet(String question, String reponse) {
        this.question = question;
        this.reponse = reponse;
    }

    public String getQuestion() {
        return question;
    }

    public String getReponse() {
        return reponse;
    }

}
