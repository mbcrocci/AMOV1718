package com.example.mbcrocci.amov1718.board;

public class Location {
    private int rank, file;

    public static final int NORTH = 0;       // cima
    public static final int NORTHEAST = 45;  // diagonal cima e direita
    public static final int WEST = 270;      // esquerda
    public static final int NORTHWEST = 315; // digonal cima e esquerda
    public static final int EAST = 90;       // direita
    public static final int SOUTHEAST = 135; // diagonal baixo e direita
    public static final int SOUTH = 180;     // baixo
    public static final int SOUTHWEST = 225; // diagonal baixo e esquerda


    public Location(int rank, int file) {
        super();
        this.rank = rank;
        this.file = file;
    }

    public Location(String str) throws IllegalArgumentException {
        super();

        if(str.length() != 2) {
            throw new IllegalArgumentException("Must be in file-rank format.");
        }

        this.file = str.charAt(0) - 'a';
        this.rank = str.charAt(1) - '1';
    }


    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof Location))
            return false;

        Location loc = (Location) o;
        return loc.file == file && loc.rank == rank;
    }

    public Location getAdjacentLocation(int direction) throws IllegalArgumentException {
        while(direction < 0)
            direction += 360;
        direction %= 360;

        switch(direction) {
            case NORTH:
                return new Location(rank + 1, file);
            case NORTHEAST:
                return new Location(rank + 1, file + 1);
            case NORTHWEST:
                return new Location(rank + 1, file - 1);
            case WEST:
                return new Location(rank, file - 1);
            case EAST:
                return new Location(rank, file + 1);
            case SOUTHEAST:
                return new Location(rank - 1, file + 1);
            case SOUTH:
                return new Location(rank - 1, file);
            case SOUTHWEST:
                return new Location(rank - 1, file - 1);
            default:
                throw new IllegalArgumentException("" + direction + " is not a" +
                        " multiple of 45 degrees.");
        }
    }

    // coluna
    public int getFile() {
        return file;
    }

    // linha
    public int getRank() {
        return rank;
    }

    public Location copy() {
        return new Location(rank, file);
    }

    @Override
    public int hashCode() {
        return rank * 3737 + file;
    }

    @Override
    public String toString() {
        if(rank < 0 || rank > 7 || file < 0 || file > 7) {
            return "(" + file + ", " + rank + ")";
        }

        StringBuilder str = new StringBuilder(2);
        str.append((char)('a' + file));
        str.append((char)('1' + rank));
        return str.toString();
    }
}
