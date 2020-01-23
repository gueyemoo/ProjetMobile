package com.project.MobileProjet;

import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

public class ChoixTables extends AppCompatActivity {

    private boolean duel;
    private String nicknameP1;
    private String nicknameP2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_tables);

        setContentView(R.layout.activity_choix_tables);


        NumberPicker picker = (NumberPicker) findViewById(R.id.np);
        picker.setMinValue(1);
        picker.setMaxValue(10);

        duel = getIntent().getBooleanExtra("duel",false);
        if(duel)
        {
            nicknameP1 = getIntent().getStringExtra("nicknameP1");
            nicknameP2 = getIntent().getStringExtra("nicknameP2");
        }
    }

    public void click(View view) {

        NumberPicker table =  (NumberPicker) findViewById(R.id.np);

        int tableVal = table.getValue();

        if(duel)
        {
            Intent intentP1 = new Intent(this,MultiplicationsActivity.class) ;
            Intent intentP2 = new Intent(this,MultiplicationsActivity.class);
            intentP1.putExtra(MultiplicationsActivity.TABLE_KEY,tableVal);
            intentP2.putExtra(MultiplicationsActivity.TABLE_KEY,tableVal);
            intentP1.putExtra("duel",true);
            intentP2.putExtra("duel",true);
            intentP1.putExtra("nickname",nicknameP1);
            intentP1.putExtra("nicknameP2",nicknameP2);
            intentP2.putExtra("nickname",nicknameP2);
            intentP2.putExtra("joueurActuel",2);
            intentP1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intentP2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            TaskStackBuilder.create( this )
                    .addNextIntent( intentP2 )
                    .addNextIntentWithParentStack( intentP1 )
                    .startActivities();
        }
        else
            {
                Intent intent = new Intent(this,MultiplicationsActivity.class);
                intent.putExtra(MultiplicationsActivity.TABLE_KEY,tableVal);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }


    }
}
