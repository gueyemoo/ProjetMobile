package com.project.MobileProjet;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.project.MobileProjet.DB.DBClient;
import com.project.MobileProjet.DB.User;

import java.util.ArrayList;
import java.util.List;

public class SelectionProfilDuelActivity extends AppCompatActivity {


    private List<User> itemList = new ArrayList<User>();
    private TaskAdapter adapter;
    private ListView listUsers;
    private DBClient mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_profil_duel);


        mDb = DBClient.getInstance(getApplicationContext());

        listUsers = findViewById(R.id.listUsers);

        adapter = new TaskAdapter(this, itemList);
        listUsers.setAdapter(adapter);



        listUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Récupération de l'utilisateur cliqué à l'aide de l'adapter
                User user = adapter.getItem(position);

                Toast.makeText(SelectionProfilDuelActivity.this, "Tu vas défier" + " " + user.getPseudo() + " que le meilleur gagne !!", Toast.LENGTH_SHORT).show();

                jeu(view,user.getId());
            }
        });

        getItem();

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh,R.color.refresh1,R.color.refresh2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);

                        getItem();


                    }
                },3000);
            }
        });


        // Mise à jour des taches
        getUsers();
    }

    private void getUsers() {

        final int idJ1 = ((MyApp) this.getApplication()).getId();
        ///////////////////////
        // Classe asynchrone permettant de récupérer des taches et de mettre à jour le listView de l'activité
        class GetUsers extends AsyncTask<Void, Void, List<User>> {

            @Override
            protected List<User> doInBackground(Void... voids) {
                List<User> userList = mDb.getAppDatabase()
                        .mydao()
                        .getAllExcept(idJ1);
                return userList;
            }

            @Override
            protected void onPostExecute(List<User> users) {
                super.onPostExecute(users);

                // Mettre à jour l'adapter avec la liste de taches
                adapter.clear();
                adapter.addAll(users);

                // Now, notify the adapter of the change in source
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void getItem() {
        final int idJ1 = ((MyApp) this.getApplication()).getId();
        class GetItem extends AsyncTask<Void, Void, List<User>> {

            @Override
            protected List<User> doInBackground(Void... voids) {
                List<User> userList = mDb.getAppDatabase()
                        .mydao()
                        .getAllExcept(idJ1);
                return userList;
            }

            protected void onPostExecute(List<User> users) {
                super.onPostExecute(users);

                // Mettre à jour l'adapter avec la liste de users
                adapter.clear();
                adapter.addAll(users);

                // Notifier l'adapter des changements
                adapter.notifyDataSetChanged();
            }

        }

        GetItem gt = new GetItem();
        gt.execute();

    }

    public void jeu(View view, int id) {

        ((MyApp) this.getApplication()).setIdJ2(id);

        Intent intent = new Intent(this, ChoixCategorieExerciceActivity.class);
        intent.putExtra("duel",true);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);

    }


}
