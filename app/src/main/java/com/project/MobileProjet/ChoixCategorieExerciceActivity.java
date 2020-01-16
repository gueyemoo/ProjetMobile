
package com.project.MobileProjet;

import android.content.Intent;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.project.MobileProjet.DB.DBClient;
import com.project.MobileProjet.DB.User;
import com.project.MobileProjet.QuizActivity;
import com.project.MobileProjet.R;


public class ChoixCategorieExerciceActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_QUIZ = 1;

    private DBClient mDb;

    private boolean duel;

    private String nicknameP1;
    private String nicknameP2;
    private boolean nicknameLoaded;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu);
        mDb = DBClient.getInstance(getApplicationContext());
        nicknameLoaded = false;
        Intent myIntent = getIntent();
        duel = myIntent.getBooleanExtra("duel",false);
        if(duel)
        {
            ((MyApp) this.getApplication()).setNbOpeJ2(0);
            loadNicknames();
        }
        ((MyApp) this.getApplication()).setNbOpe(0);
    }

    public void goQuizz(View view) {

        Intent intent = new Intent(this, QuizActivity.class);
        startActivityForResult(intent, REQUEST_CODE_QUIZ);

    }

    public void goMaths(View view) {

        Intent intent = new Intent(this, MathsActivity.class);
        if(duel && nicknameLoaded)
        {
            intent.putExtra("duel",true);
            intent.putExtra("nicknameP1",nicknameP1);
            intent.putExtra("nicknameP2",nicknameP2);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);

    }

    public void langues_click(View view) {

        Intent intent = new Intent(this, FrancaisActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }

    public void pendu_click(View view) {

        Intent intent = new Intent(this, PenduActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }

    public void pays_click(View view) {

        Intent intent = new Intent(this, PaysActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }

    private void loadNicknames()
    {

        final int userIdP1 = ((MyApp) this.getApplication()).getId();
        final int userIdP2 = ((MyApp) this.getApplication()).getIdJ2();

        class LoadCalcul extends AsyncTask< Void, Void, Void > {

            private User utilisateurActuel1 = null;
            private User utilisateurActuel2 = null;
            @Override
            protected Void doInBackground(Void...voids) {

                utilisateurActuel1 = mDb.getAppDatabase()
                        .mydao()
                        .getUser(userIdP1);

                utilisateurActuel2 = mDb.getAppDatabase()
                        .mydao()
                        .getUser(userIdP2);

                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                nicknameP1 = utilisateurActuel1.getPseudo();
                nicknameP2 = utilisateurActuel2.getPseudo();
                nicknameLoaded = true;
            }

        }
        LoadCalcul lc = new LoadCalcul();
        lc.execute();
    }
}
