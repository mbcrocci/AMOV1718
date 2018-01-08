package com.example.mbcrocci.amov1718.game;

import com.example.mbcrocci.amov1718.board.Chessboard;
import com.example.mbcrocci.amov1718.board.Location;
import com.example.mbcrocci.amov1718.pieces.Pawn;

// Representa um dado momento do Jogo
public class GameState {
    public final boolean isWhitesTurn;
    public final Chessboard board;
    public final Location ep_loc;

    // caso existir algum peao em en passant
    public final Pawn ep_pawn;

    public GameState(boolean isWhitesTurn, Chessboard board, Location ep_loc, Pawn ep_pawn) {
        super();
        this.isWhitesTurn = isWhitesTurn;
        this.board = board;
        this.ep_loc = ep_loc;
        this.ep_pawn = ep_pawn;
    }
}
