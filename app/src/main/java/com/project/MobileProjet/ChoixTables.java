package com.project.MobileProjet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

public class ChoixTables extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_tables);

        setContentView(R.layout.activity_choix_tables);


        NumberPicker picker = (NumberPicker) findViewById(R.id.np);
        picker.setMinValue(1);
        picker.setMaxValue(10);
    }

    public void click(View view) {

        NumberPicker table =  (NumberPicker) findViewById(R.id.np);

        int tableVal = table.getValue();

        Intent intent = new Intent(this,MultiplicationsActivity.class);

        intent.putExtra(MultiplicationsActivity.TABLE_KEY,tableVal);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);

    }
}
