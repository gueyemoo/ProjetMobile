package com.example.L3MobileProjet.model;

public class Multiplication {

    private int operande1;
    private int operande2;
    private Integer resultat;

    public Multiplication(int operande1, int operande2) {
        this.operande1 = operande1;
        this.operande2 = operande2;
        resultat = null;
    }

    public int getOperande1() {
        return operande1;
    }

    public int getOperande2() {
        return operande2;
    }

    public int getResultat() {
        return resultat;
    }

    public void setResultat(int resultat) {
        this.resultat = resultat;
    }
}
