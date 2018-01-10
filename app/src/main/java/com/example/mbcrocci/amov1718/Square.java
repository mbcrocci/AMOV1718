package com.example.mbcrocci.amov1718;

import android.content.Context;
import android.widget.ImageView;

import com.example.mbcrocci.amov1718.board.Location;
import com.example.mbcrocci.amov1718.pieces.Piece;

/*
 *  Representa um quadrado do tabuleiro de xadrez
 *  Pode conter uma peca (peca == null se estiver vazio)
 */
public class Square extends ImageView {
    private boolean isWhite, isSelected;

    private Piece piece;
    private Location location;

    public Square(Context context, Boolean isWhite, String loc) {
        super(context);

        this.isWhite = isWhite;
        this.location = new Location(loc);

        this.setClickable(true);
    }

    public boolean isWhite() {
        return isWhite;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public Location getLocation() {
        return location;
    }

    public Piece getPiece() {
        return piece;
    }

    // setter
    public void addPiece(Piece p) {
        this.piece = p;
    }

    public void removePiece() {
        if (this.piece == null)
            return;

        this.piece = null;
    }

    public void select() {
        isSelected = !isSelected;
    }


}
