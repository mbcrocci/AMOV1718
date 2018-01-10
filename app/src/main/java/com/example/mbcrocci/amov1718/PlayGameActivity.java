package com.example.mbcrocci.amov1718;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.mbcrocci.amov1718.board.Chessboard;
import com.example.mbcrocci.amov1718.board.Location;
import com.example.mbcrocci.amov1718.game.Game;
import com.example.mbcrocci.amov1718.move.IllegalMoveException;
import com.example.mbcrocci.amov1718.pieces.Piece;

import java.util.ArrayList;

public class PlayGameActivity extends Activity {

    protected Display display;
    protected int displayHeight, displayWidth;

    protected Game game;

    protected Square [][]squares = new Square[8][8];
    protected Square selectedSquare;

    protected Location selectedLocation;

    protected LinearLayout linearLayout;
    protected TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        display = this.getWindowManager().getDefaultDisplay();

        display.getMetrics(displayMetrics);
        displayHeight = displayMetrics.heightPixels;
        displayWidth = displayMetrics.widthPixels;

        makeBoard();

        game = new Game(new Chessboard());
    }

    protected void makeBoard() {
        linearLayout = (LinearLayout) findViewById(R.id.game_layout);
        linearLayout.removeAllViews();
        linearLayout.invalidate();
        linearLayout.refreshDrawableState();

        tableLayout = new TableLayout(this);
        tableLayout.removeAllViews();
        tableLayout.invalidate();
        tableLayout.refreshDrawableState();

        linearLayout.addView(tableLayout);

        tableLayout.setLayoutParams(
                new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT
                )
        );
        tableLayout.setStretchAllColumns(true);
        tableLayout.setOrientation(LinearLayout.VERTICAL);

        makeBoardSquares();

        // decrescente pq as filas mais altas (pretas) ficam em cima
        for (int l = 7; l >= 0; l--) {
            TableRow tr = new TableRow(this);
            tr.removeAllViews();
            tr.invalidate();
            tr.refreshDrawableState();

            tableLayout.addView(tr);

            tr.setLayoutParams(
                    new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.WRAP_CONTENT,
                            TableLayout.LayoutParams.WRAP_CONTENT
                    )
            );

            // a -> h
            for (int c = 0; c < 8; c++) {
                final int x = l, y = c;
                Square square = squares[l][c];

                // num metodo na classe nao estava a funcionar
                square.setOnClickListener(newViewOnClickListeneter(x, y));

                // TODO: separar isto para um metodo
                // fila preta
                if (l == 7) {
                    if (c == 0 || c == 7) // torre
                        square.setImageResource(R.drawable.blackrook);

                    else if (c == 1 || c == 6) // cavalo
                        square.setImageResource(R.drawable.blackknight);

                    else if (c == 2 || c == 5) // bispo
                        square.setImageResource(R.drawable.blackbishop);

                    else if (c == 3) // queen
                        square.setImageResource(R.drawable.blackqueen);

                    else if (c == 4) // king
                        square.setImageResource(R.drawable.blackking);

                    else // nao deveria chegar aqui
                        square.setImageResource(R.drawable.ic_launcher_foreground);

                } else if (l == 6) { // fila dos peoes pretos
                    square.setImageResource(R.drawable.blackpawn);
                } else if (l == 1) { // fila dos peoes brancos
                    square.setImageResource(R.drawable.whitepawn);
                } else if (l == 0) { // fila branca
                    if (c == 0 || c == 7) // torre
                        square.setImageResource(R.drawable.whiterook);

                    else if (c == 1 || c == 6) // cavalo
                        square.setImageResource(R.drawable.whiteknight);

                    else if (c == 2 || c == 5) // bispo
                        square.setImageResource(R.drawable.whitebishop);

                    else if (c == 3) // queen
                        square.setImageResource(R.drawable.whitequeen);

                    else if (c == 4) // king
                        square.setImageResource(R.drawable.whiteking);
                    else
                        square.setImageResource(R.drawable.ic_launcher_foreground);
                } else {
                    square.setImageResource(R.drawable.transparent);
                }

                square.setAdjustViewBounds(true);
                square.setMinimumHeight(displayHeight / 8);
                square.setMinimumWidth(20);
                square.setMaxHeight(displayHeight / 8);
                square.setMaxWidth(20);

                if (square.isWhite())
                    square.setBackgroundColor(Color.WHITE);

                else
                    square.setBackgroundColor(Color.GRAY);

                square.setLayoutParams(
                        new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        )
                );

                tr.addView(square);
            }
        }
        setContentView(linearLayout);
        return;
    }

    protected void makeBoardSquares() {
        for (int x = 7; x >= 0; x--) {
            for (int y = 0; y < 8; y++) {

                // y + 97 => letra correspondente a y esima casa (x=0 => a)
                String location = "" + ((char) (y + 97)) + "" + (x + 1);

                // casa e branca se a soma das coordendas for par
                boolean white = (x + y) % 2 != 0;

                squares[x][y] = new Square(this, white, location);

            }
        }
    }

    protected void renderBoard() {
        Chessboard chessboard = game.getGrid();
        for (int rank = 7; rank >= 0; rank--) {
            for (int file = 0; file < 8; file++) {
                Square square = squares[rank][file];
                Location loc = new Location(rank, file);

                if (chessboard.isOccupied(loc)) {
                    Piece p = chessboard.get(loc);

                    if (p.getAlgebraicName().equals(""))
                        square.setImageResource(
                                p.isWhite() ? R.drawable.whitepawn : R.drawable.blackpawn
                        );

                    else if (p.getAlgebraicName().equals("K"))
                        square.setImageResource(
                                p.isWhite() ? R.drawable.whiteking : R.drawable.blackking
                        );

                    else if (p.getAlgebraicName().equals("Q"))
                        square.setImageResource(
                                p.isWhite() ? R.drawable.whitequeen : R.drawable.blackqueen
                        );

                    else if (p.getAlgebraicName().equals("R"))
                        square.setImageResource(
                                p.isWhite() ? R.drawable.whiterook : R.drawable.blackrook
                        );

                    else if (p.getAlgebraicName().equals("N"))
                        square.setImageResource(
                                p.isWhite() ? R.drawable.whiteknight : R.drawable.blackknight
                        );

                    else if (p.getAlgebraicName().equals("B"))
                        square.setImageResource(
                                p.isWhite() ? R.drawable.whitebishop : R.drawable.blackbishop
                        );
                } else {
                        square.setImageResource(R.drawable.transparent);
                }
            }
        }
    }

    protected View.OnClickListener newViewOnClickListeneter(final int x, final int y) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }
}