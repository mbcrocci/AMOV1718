package com.example.mbcrocci.amov1718.pieces;

import com.example.mbcrocci.amov1718.board.Location;
import com.example.mbcrocci.amov1718.game.Game;

import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(boolean isWhite, Game game, Location location) {
        super(isWhite, game, location);
    }

    @Override
    public Piece copy() {
        return new Queen(isWhite(), getGame(), getLocation().copy());
    }

    @Override
    public char toFEN() {
        return isWhite() ? 'Q' : 'q';
    }

    @Override
    public ArrayList<Location> getAttackedLocations() {
        ArrayList<Location> locs = new ArrayList<Location>();
        for(int dir = 0; dir < 360; dir += 45) {
            Location l = getLocation().getAdjacentLocation(dir);
            while(getGrid().isValid(l)) {
                locs.add(l);
                if(getGrid().isOccupied(l))
                    break;
                l = l.getAdjacentLocation(dir);
            }
        }
        return locs;
    }

    @Override
    public ArrayList<Location> getMoveLocations() {
        ArrayList<Location> locs = new ArrayList<Location>();
        for(int dir = 0; dir < 360; dir += 45) {
            Location l = getLocation().getAdjacentLocation(dir);
            while(getGrid().isValid(l)) {
                if(getGrid().isOccupied(l)) {
                    if(!isSameColor(getGrid().get(l))) {
                        locs.add(l);
                    }
                    break;
                }
                locs.add(l);
                l = l.getAdjacentLocation(dir);
            }
        }
        return locs;
    }

    @Override
    public int getValue() {
        return 9;
    }
}
