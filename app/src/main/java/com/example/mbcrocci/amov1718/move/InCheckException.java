package com.example.mbcrocci.amov1718.move;

import java.util.HashSet;

public class InCheckException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private HashSet<Move> availableMoves;

    public InCheckException(HashSet<Move> availableMoves) {
        super("You must make a move that takes the king out of check.");
        this.availableMoves = availableMoves;
    }

    public HashSet<Move> getAvailableMoves() {
        return availableMoves;
    }
}
