package com.example.mbcrocci.amov1718;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.example.mbcrocci.amov1718.board.Location;
import com.example.mbcrocci.amov1718.game.RecordedGame;
import com.example.mbcrocci.amov1718.move.IllegalMoveException;
import com.example.mbcrocci.amov1718.pieces.Piece;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class PlayAgainstAIActivity extends PlayGameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Play Against AI");
    }

    @Override
    protected View.OnClickListener newViewOnClickListeneter(final int x, final int y) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.isWhitesTurn()) {
                    if (selectedSquare != null) {
                        if (squares[x][y] == selectedSquare) {
                            if (squares[x][y].isWhite())
                                squares[x][y].setBackgroundColor(Color.WHITE);
                            else
                                squares[x][y].setBackgroundColor(Color.GRAY);

                            selectedSquare = null;
                            return;
                        } else {
                            Location src = selectedLocation;
                            Location dst = new Location(x, y);

                            try {
                                boolean inCheck = game.move(src, dst);
                                if (inCheck)
                                    Toast.makeText(getBaseContext(), "Check!", Toast.LENGTH_LONG)
                                            .show();

                                renderBoard();

                            } catch (IllegalMoveException e) {
                                StringBuilder strBuilder = new StringBuilder();

                                strBuilder.append(e.getPiece() + " cant move to " + e.getLocation());
                                strBuilder.append('\n');

                                ArrayList<Location> locs = e.getPiece().getMoves();

                                if (locs.isEmpty())
                                    strBuilder.append("This piece cant move.");

                                else {
                                    strBuilder.append(e.getPiece() + " can move to: ");
                                    for (Location loc : e.getPiece().getMoves())
                                        strBuilder.append(loc + " ");
                                }

                                Toast.makeText(getBaseContext(), strBuilder.toString(), Toast.LENGTH_LONG)
                                        .show();
                                return;

                            } finally {
                                if (selectedSquare.isWhite())
                                    selectedSquare.setBackgroundColor(Color.WHITE);
                                else
                                    selectedSquare.setBackgroundColor(Color.GRAY);

                                selectedSquare = null;
                                selectedLocation = null;
                            }
                        }
                        // if (selectedSquare != null) {
                    } else {
                        Location loc = new Location(x, y);
                        if (game.getGrid().get(loc) == null)
                            return;

                        Piece piece = game.getGrid().get(loc);

                        if (piece.isWhite() != game.isWhitesTurn()) {
                            Toast.makeText(
                                    getBaseContext(),
                                    "You can only move your pieces" + ((game.isWhitesTurn()) ? "whites" : "blacks") + " turn.",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }

                        squares[x][y].select();
                        selectedSquare = squares[x][y];
                        selectedLocation = new Location(x, y);
                        squares[x][y].setBackgroundColor(Color.RED);
                    }
                } else {
                    try {
                        game.makeAutomaticMove();

                    } catch (NoSuchElementException e) {
                        Toast.makeText(getBaseContext(), "The computer cant play.", Toast.LENGTH_SHORT).show();
                    }
                }

                if (game.isCheckmate()) {
                    new AlertDialog.Builder(getBaseContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Checkmate")
                            .setMessage((game.isWhitesTurn() ? "White" : "Black") + " player won!")
                            .setPositiveButton("End Game",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            RecordedGame recordedGame = new RecordedGame(
                                                    game.isWhitesTurn(),
                                                    game.getMoves(),
                                                    "vs ai");
                                            recordedGame.saveToSharedPreferences(getApplicationContext());
                                        }
                                    }
                            );

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        };
    }
}
