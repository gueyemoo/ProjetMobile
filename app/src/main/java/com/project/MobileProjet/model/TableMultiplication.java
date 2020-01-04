package com.project.MobileProjet.model;

import java.util.ArrayList;

public class TableMultiplication {


    private int nombre;
    private ArrayList<Multiplication> multiplications;

    public TableMultiplication(int nombre, int nombreOperations) {
        this.nombre = nombre;

        // Initialisation
        multiplications = new ArrayList<Multiplication>(nombreOperations);
        for (int i=1; i <= nombreOperations; i++) {
            Multiplication mult = new Multiplication(i, nombre);
            multiplications.add(mult);
        }
    }

    public int getNombre() {
        return nombre;
    }

    public int getNombreOperations() {
        return multiplications.size();
    }

    public Multiplication getMultiplication(int i) {
        return multiplications.get(i);
    }

    public ArrayList<Multiplication> getMultiplications() {
        return multiplications;
    }
}
