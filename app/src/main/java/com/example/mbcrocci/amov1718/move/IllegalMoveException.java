package com.example.mbcrocci.amov1718.move;

import com.example.mbcrocci.amov1718.board.Location;
import com.example.mbcrocci.amov1718.pieces.Piece;

public class IllegalMoveException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final Piece piece;
    private final Location location;

    public IllegalMoveException(Piece p, Location loc) {
        this(p.toString() + " cannot move to location " + loc.toString() + ".", p, loc);
    }

    public IllegalMoveException(String message, Piece p, Location loc) {
        super(message);
        this.piece = p;
        this.location = loc;
    }

    public Location getLocation() {
        return location;
    }

    public Piece getPiece() {
        return piece;
    }
}
