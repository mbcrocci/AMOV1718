package com.example.mbcrocci.amov1718.pieces;

import com.example.mbcrocci.amov1718.board.Location;
import com.example.mbcrocci.amov1718.game.Game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class King extends Piece {
    private Location start;

    public King(boolean isWhite, Game game, Location location) {
        super(isWhite, game, location);
        start = location;
    }

    @Override
    public char toFEN() {
        return isWhite() ? 'K' : 'k';
    }

    @Override
    public Piece copy() {
        King k = new King(isWhite(), getGame(), getLocation().copy());
        k.start = start.copy();
        return k;
    }

    public boolean canCastle(Rook r) {
        if(hasMoved() || r.hasMoved() || isWhite() != r.isWhite())
            return false;

        int dir = r.getLocation().getFile() > getLocation().getFile() ? 90 : -90;
        ArrayList<Location> locs = new ArrayList<Location>();
        Location loc = getLocation();
        for(int i = 0; i < 3; i++) {
            locs.add(loc);
            loc = loc.getAdjacentLocation(dir);
        }

        HashSet<Location> attacked = isWhite() ? getGame().getLocsControlledByBlack() :
                getGame().getLocsControlledByWhite();

        for(Location l : locs) {
            if(attacked.contains(l) || getGrid().isOccupied(l))
                return false;
        }

        return true;
    }

    @Override
    public ArrayList<Location> getAttackedLocations() {
        ArrayList<Location> locs = new ArrayList<Location>(8);
        for(int dir = 0; dir < 360; dir += 45) {
            Location l = getLocation().getAdjacentLocation(dir);
            if(getGrid().isValid(l)) {
                locs.add(l);
            }
        }
        return locs;
    }

    @Override
    protected ArrayList<Location> getMoveLocations() {
        ArrayList<Location> locs = getAttackedLocations();
        HashSet<Location> invalid = isWhite() ?
                getGame().getLocsControlledByBlack() : getGame().getLocsControlledByWhite();

        Iterator<Location> i = locs.iterator();
        while(i.hasNext()) {
            Location loc = i.next();
            if(invalid.contains(loc) || (getGrid().isOccupied(loc) && isSameColor(getGrid().get(loc))))
                i.remove();
        }
        return locs;
    }

    @Override
    public int getValue() {
        return Integer.MAX_VALUE;
    }

    /**
     * @return True if this king has moved already; false otherwise.
     */
    public boolean hasMoved() {
        if(start == null)
            return false;
        return !start.equals(getLocation());
    }
}