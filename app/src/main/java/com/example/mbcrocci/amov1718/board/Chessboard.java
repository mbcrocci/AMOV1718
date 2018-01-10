package com.example.mbcrocci.amov1718.board;

import com.example.mbcrocci.amov1718.pieces.Piece;
import com.example.mbcrocci.amov1718.board.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Chessboard implements Grid<Piece> {
    private HashMap<Location, Piece> map;
    private boolean inverted;

    public Chessboard() {
        super();
        map = new HashMap<Location, Piece>(32);
    }

    public Chessboard(Set<Map.Entry<Location, Piece>> entrySet) {
        super();
        map = new HashMap<Location, Piece>(32);
        for(Entry<Location, Piece> e : entrySet) {
            map.put(e.getKey().copy(), e.getValue().copy());
        }
    }

    public Set<Entry<Location, Piece>> getEntrySet() {
        return map.entrySet();
    }

    @Override
    public void invert() {
        inverted = !inverted;
    }

    @Override
    public boolean isInverted() {
        return inverted;
    }

    @Override
    public String draw() {
        StringBuilder sb = new StringBuilder(200);
        int rMax = getRows(), cMax = getCols();
        boolean isWhite = true;
        for(int row = !inverted ? rMax - 1 : 0, i = 0; i < rMax; i++, row += !inverted ? -1 : 1) {
            for(int col = inverted ? cMax - 1 : 0, j = 0; j < cMax; j++, col += inverted ? -1 : 1) {
                Location loc = new Location(row, col);
                Piece p = get(loc);
                if(p == null)
                    sb.append(isWhite ? "  " : "##");
                else
                    sb.append(p.getPieceCode());
                sb.append(' ');
                isWhite = !isWhite;
            }
            isWhite = !isWhite;
            sb.append(" " + (row + 1) + "\n");
        }

        sb.append(' ');
        for(int i = !inverted ? 0 : cMax - 1, j = 0; j < cMax; j++, i += !inverted ? 1 : -1) {
            sb.append("" + (char) (i + 'a') + "  ");
        }
        sb.trimToSize();
        return sb.toString();
    }

    @Override
    public Piece get(Location loc) {
        return map.get(loc);
    }

    @Override
    public int getCols() {
        return 8;
    }

    @Override
    public int getRows() {
        return 8;
    }

    @Override
    public boolean isOccupied(Location loc) {
        return map.containsKey(loc);
    }

    @Override
    public boolean isValid(Location loc) {
        return (loc.getFile() >= 0 && loc.getFile() < 8) &&
                (loc.getRank() >= 0 && loc.getRank() < 8);
    }

    @Override
    public Piece put(Location loc, Piece piece) {
        if(piece == null)
            throw new IllegalArgumentException("Piece cannot be null.");

        if(piece.getLocation() != null && !piece.getLocation().equals(loc)) {
            Location prev = piece.getLocation();
            map.remove(prev);
        }

        Piece other = map.get(loc);
        piece.setLocation(loc);
        if(other != null)
            other.setLocation(null);
        map.put(loc, piece);

        return other;
    }

    @Override
    public void remove(Location loc) {
        map.remove(loc);
    }

    @Override
    public String toString() {
        return draw();
    }
}
