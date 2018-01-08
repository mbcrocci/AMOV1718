package com.example.mbcrocci.amov1718.move;

import com.example.mbcrocci.amov1718.board.Location;

public class InvalidLocationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final Location loc;

    public InvalidLocationException(Location loc) {
        this("Location " + loc.toString() + " is not valid.", loc);
    }

    public InvalidLocationException(String message, Location loc) {
        super(message);
        this.loc = loc;
    }

    public Location getLocation() {
        return loc;
    }
}
