package com.project.MobileProjet;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.project.MobileProjet.DB.DBClient;
import com.project.MobileProjet.DB.User;

public class AddUserActivity extends AppCompatActivity {

    // DATA
    private DBClient mDb;

    // VIEW
    private EditText pseudo;
    private NumberPicker age;
    private RadioGroup sexe;
    private ImageView save;
    private RadioButton garçon;
    private RadioButton fille;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // Récupération du DatabaseClient
        mDb = DBClient.getInstance(getApplicationContext());

        // Récupérer les vues
        pseudo = findViewById(R.id.pseudo_id);
        age = findViewById(R.id.age);
        sexe = findViewById(R.id.sexe);
        garçon = findViewById(R.id.garcon);
        garçon.setId(R.id.garcon);
        fille = findViewById(R.id.fille);
        fille.setId(R.id.fille);
        save = findViewById(R.id.save);

        age.setMinValue(6);
        age.setMaxValue(11);

        // Associer un événement au bouton save
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUser();
            }
        });
    }



    private void saveUser() {

        // Récupérer les informations contenues dans les vues
        final String pseudoValue = pseudo.getText().toString().trim();
        final int ageValue = age.getValue();
        final int sexeValue = sexe.getCheckedRadioButtonId();

        // Vérifier les informations fournies par l'utilisateur
        if (pseudoValue.isEmpty()) {
            pseudo.setError("Obligatoire");
            pseudo.requestFocus();
            return;
        }


        class SaveUser extends AsyncTask<Void, Void, User> {

            @Override
            protected User doInBackground(Void... voids) {

                // creating a user
                User user = new User();
                user.setPseudo(pseudoValue);
                user.setAge(ageValue);


                if(sexeValue == R.id.garcon) {
                    user.setSexe("garçon");
                    user.setPhoto(R.drawable.homme);
                }
                else {
                    user.setSexe("fille");
                    user.setPhoto(R.drawable.femme);
                }

                // adding to database
                mDb.getAppDatabase()
                        .mydao()
                        .addUser(user);

                return user;
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);

                setResult(RESULT_OK);
                finish();
                Toast.makeText(getApplicationContext(), "Profil créé", Toast.LENGTH_LONG).show();
            }
        }

        SaveUser st = new SaveUser();
        st.execute();
    }

}


