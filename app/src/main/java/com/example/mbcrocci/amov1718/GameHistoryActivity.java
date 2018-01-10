package com.example.mbcrocci.amov1718;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mbcrocci.amov1718.game.RecordedGame;

import java.util.ArrayList;

public class GameHistoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);

        ListView lv = findViewById(R.id.game_history_list);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor spEdit = sp.edit();

        ArrayList<String> recordedGames = new ArrayList<>();

        // Poderia/Deveria ser feito com Parcelable
        int size = sp.getInt("recordedGamesSize", 0);
        for (int i = 0; i < size; i++)
            recordedGames.add(sp.getString("recordedGame" + i, null));


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                recordedGames
        );

        lv.setAdapter(arrayAdapter);
    }
}
