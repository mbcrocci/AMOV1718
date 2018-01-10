package com.example.mbcrocci.amov1718.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.mbcrocci.amov1718.move.Move;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecordedGame {

    private String date;
    private String moves;
    private String winner;
    private String gameMode;

    public RecordedGame(boolean winner, String moves, String gameMode) {
        this.moves = moves;
        this.winner = winner ? "White" : "Black";
        this.date = DateFormat.getDateTimeInstance().format(new Date());
        this.gameMode = gameMode;
    }

    @Override
    public String toString() {
        return date + " | Winner | " + winner + " | " + moves;
    }

    public void saveToSharedPreferences(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spEdit = sp.edit();

        int size = sp.getInt("recordedGamesSize", 0);
        size++;
        spEdit.putInt("recordedGamesSize", size);

        spEdit.putString("recordedGame" + size, this.toString());

        spEdit.commit();
    }
}