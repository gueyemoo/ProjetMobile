package com.project.MobileProjet.DB;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

public class DBClient
{
    // Instance unique permettant de faire le lien avec la base de données
    private static DBClient instance;

    // Objet représentant la base de données de votre application
    private MyDB DBClient;

    // Constructeur
    private DBClient(final Context context) {

        // Créer l'objet représentant la base de données de votre application
        // à l'aide du "Room database builder"
        // MyToDos est le nom de la base de données
        //appDatabase = Room.databaseBuilder(context, AppDatabase.class, "MyToDos").build();

        // Ajout de la méthode addCallback permettant de populate (remplir) la base de données à sa création
        DBClient = Room.databaseBuilder(context, MyDB.class, "ILearn").addCallback(roomDatabaseCallback).build();
    }

    // Méthode statique
    // Retourne l'instance de l'objet DatabaseClient
    public static synchronized DBClient getInstance(Context context) {
        if (instance == null) {
            instance = new DBClient(context);
        }
        return instance;
    }

    // Retourne l'objet représentant la base de données de votre application
    public MyDB getAppDatabase() {
        return DBClient;
    }

    // Objet permettant de populate (remplir) la base de données à sa création
    RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {

        // Called when the database is created for the first time.
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            //


          // db.execSQL("INSERT INTO users (pseudo, age,sexe,photo) VALUES(\"Tom\", 5, \"homme\",R.drawable.homme);");


        }
    };
}
