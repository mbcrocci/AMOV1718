package com.example.mbcrocci.amov1718.pieces;

import com.example.mbcrocci.amov1718.board.Location;
import com.example.mbcrocci.amov1718.game.Game;

import java.util.ArrayList;
import java.util.Iterator;

public class Pawn extends Piece {
    private Location start, ep;

    public Pawn(boolean isWhite, Game game, Location loc) {
        super(isWhite, game, loc);
        start = loc;
        ep = getLocation().getAdjacentLocation(isWhite ? 0 : 180);
    }

    private Pawn(boolean isWhite, Game game) {
        super(isWhite, game, null);
    }

    @Override
    public Piece copy() {
        Pawn p = new Pawn(isWhite(), getGame());
        p.overrideLocation(getLocation().copy());
        p.start = start.copy();
        p.ep = ep.copy();
        return p;
    }

    @Override
    public String getAlgebraicName() {
        return "";
    }

    @Override
    public ArrayList<Location> getAttackedLocations() {
        ArrayList<Location> locs = new ArrayList<Location>(2);
        Location loc = getLocation();

        Location l1, l2;
        if (isWhite()) {
            l1 = loc.getAdjacentLocation(Location.NORTHEAST);
            l2 = loc.getAdjacentLocation(Location.NORTHWEST);
        } else {
            l1 = loc.getAdjacentLocation(Location.SOUTHEAST);
            l2 = loc.getAdjacentLocation(Location.SOUTHWEST);
        }

        if(getGrid().isValid(l1)) {
            locs.add(l1);
        }
        if(getGrid().isValid(l2)) {
            locs.add(l2);
        }
        return locs;
    }

    @Override
    public int getValue() {
        return 1;
    }

    public boolean hasMoved() {
        if(start == null)
            return false;
        return !start.equals(getLocation());
    }

    public void promote(Piece p) {
        if(getLocation().getRank() != (isWhite() ? 7 : 0))
            throw new IllegalStateException("Cannot promote when not on the eighth rank.");

        if(p == null)
            p = new Queen(isWhite(), getGame(), getLocation());
        if(p instanceof King || p instanceof Pawn)
            throw new IllegalArgumentException
                    ("You must promote to a queen, rook, knight, or bishop.");

        p.setLocation(getLocation());
        setLocation(null);
    }

    @Override
    public char toFEN() {
        return isWhite() ? 'P' : 'p';
    }

    @Override
    protected ArrayList<Location> getMoveLocations() {
        // First let's deal with the diagonal squares.
        ArrayList<Location> result = getAttackedLocations();
        Iterator<Location> i = result.iterator();
        while(i.hasNext()) {
            Location loc = i.next();
            if(getGrid().isOccupied(loc)) {
                Piece p = getGrid().get(loc);
                if(isSameColor(p))
                    i.remove();
            }
            else {
                // Being extra safe
                Location ep = getGame().getEnPassantLocation();
                if(ep == null || !ep.equals(loc) || isSameColor(getGame().getEnPassantPawn()))
                    i.remove();
            }
        }

        // Next come the squares in front.
        int dir = isWhite() ? 0 : 180;
        Location l1 = getLocation().getAdjacentLocation(dir);
        if(getGrid().isValid(l1) && !getGrid().isOccupied(l1)) {
            result.add(l1);
            Location l2 = l1.getAdjacentLocation(dir);
            if(!hasMoved() && !getGrid().isOccupied(l2)) {
                result.add(l2);
            }
        }
        return result;
    }

    private void overrideLocation(Location loc) {
        super.setLocation(loc);
    }
}
