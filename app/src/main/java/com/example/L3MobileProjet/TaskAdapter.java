package com.example.L3MobileProjet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.L3MobileProjet.DB.User;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<User> {

    public TaskAdapter(Context mCtx, List<User> taskList) {
        super(mCtx, R.layout.list_elem_layout, taskList);
    }

    /**
     * Remplit une ligne de la listView avec les informations de la multiplication associée
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Récupération de la multiplication²
        final User user = getItem(position);

        // Charge le template XML
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.list_elem_layout, parent, false);

        // Récupération des objets graphiques dans le template
        TextView pseudo = (TextView) rowView.findViewById(R.id.pseudo);
        ImageView image = (ImageView) rowView.findViewById(R.id.image);
        TextView score = (TextView) rowView.findViewById(R.id.score);


        //
        pseudo.setText(user.getPseudo() + " " + "(" + user.getAge() + " " + "ans" + ")");
        image.setImageResource(user.getPhoto());
        score.setText("Score : " + " " +user.getScore());

        //
        return rowView;
    }

}