package com.example.mbcrocci.amov1718.pieces;

import com.example.mbcrocci.amov1718.board.Grid;
import com.example.mbcrocci.amov1718.game.Game;
import com.example.mbcrocci.amov1718.board.Location;

import java.util.ArrayList;

public abstract class Piece {
    private boolean color;
    private Game game;
    private Location location;
    private ArrayList<Location> moves;

    public Piece(boolean isWhite, Game game, Location location) {
        super();
        if(game == null)
            throw new IllegalArgumentException("Invalid game object.");

        this.color = isWhite;
        this.game = game;
        this.location = location;
        if(location != null)
            this.moves = getMoveLocations();
    }

    public boolean canMove(Location loc) {
        moves = getMoveLocations();
        return moves.contains(loc);
    }

    public abstract Piece copy();

    // Duas pecas sao iguais so quando sao da mesma cor e ocupao as mesma posicao.
    @Override
    public boolean equals(Object other) {
        if(other == null || !(other instanceof Piece)) {
            return false;
        }

        Piece p = (Piece) other;
        if(location == null)
            return p.location == null && color == p.color;
        else
            return color == p.color && location.equals(p.location);
    }

    public String getAlgebraicName() {
        return new String("" + Character.toUpperCase(toFEN()));
    }

    public abstract ArrayList<Location> getAttackedLocations();

    public Game getGame() {
        return game;
    }

    public Grid<Piece> getGrid() {
        return game.getGrid();
    }

    public Location getLocation() {
        return location;
    }

    public ArrayList<Location> getMoves() {
        return moves;
    }

    public String getPieceCode() {
        StringBuilder sb = new StringBuilder(2);
        sb.append(isWhite() ? 'w' : 'b');
        sb.append(Character.toUpperCase(toFEN()));
        return sb.toString();
    }

    public abstract int getValue();

    @Override
    public int hashCode() {
        return getValue() * getLocation().hashCode();
    }

    public boolean isWhite() {
        return color;
    }

    // Funciona mesmo que segundo as regras a peca nao se possa mecher para loc
    public void setLocation(Location loc) {
        location = loc;
    }

    public abstract char toFEN();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName());
        if(location != null) {
            sb.append(" at ");
            sb.append(location.toString());
        }
        return sb.toString();
    }

    public void update() {
        moves = getMoveLocations();
    }

    protected abstract ArrayList<Location> getMoveLocations();

    boolean isSameColor(Piece p) {
        return color == p.color;
    }
}