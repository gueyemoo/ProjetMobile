package com.project.MobileProjet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MathsActivity extends AppCompatActivity {

    private boolean duel;
    private String nicknameP1;
    private String nicknameP2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths);
        Intent currentIntent = getIntent();
        duel = currentIntent.getBooleanExtra("duel",false);
        if(duel)
        {
            nicknameP1 = getIntent().getStringExtra("nicknameP1");
            nicknameP2 = getIntent().getStringExtra("nicknameP2");

        }


    }

    public void mul(View view) {

        Intent intent = new Intent(this,ChoixTables.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);

    }

    public void add(View view) {

        Intent intent = new Intent(this,AdditionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(duel)
        {
            intent.putExtra("duel",true);
            intent.putExtra("nicknameP1",nicknameP1);
            intent.putExtra("nicknameP2",nicknameP2);
        }
        startActivity(intent);

    }

}
