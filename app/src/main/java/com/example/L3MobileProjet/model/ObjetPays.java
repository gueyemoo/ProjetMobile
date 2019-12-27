package com.example.L3MobileProjet.model;

public class ObjetPays {
    String nom;
    int images;

    public ObjetPays(String nom, int images) {
        this.nom = nom;
        this.images = images;
    }

    public String getNom() {
        return nom;
    }

    public int getImages() {
        return images;
    }
}
