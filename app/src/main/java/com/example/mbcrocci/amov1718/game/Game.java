package com.example.mbcrocci.amov1718.game;


import com.example.mbcrocci.amov1718.board.*;
import com.example.mbcrocci.amov1718.game.*;
import com.example.mbcrocci.amov1718.pieces.*;
import com.example.mbcrocci.amov1718.move.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

public class Game {
    private ArrayList<Piece> pieces;
    private Chessboard grid;
    private HashSet<Location> white_locs, black_locs;

    private MoveSequence moves;
    private boolean whiteTurn;

    private GameState previousState;
    private boolean canUndo;

    private Location ep_capture;
    private Pawn ep_pawn;

    private King K, k;

    private HashSet<Move> escape_moves;

    public Game(Chessboard grid) {
        super();
        this.grid = grid;
        this.black_locs = new HashSet<Location>(32);
        this.white_locs = new HashSet<Location>(32);
        this.whiteTurn = true;
        this.moves = new MoveSequence();
        this.canUndo = false;
        defaultPosition();
        updateControlledLocations();
        for(Piece p : pieces) {
            p.update();
        }
    }

    public boolean canUndo() {
        return canUndo;
    }

    public ArrayList<Piece> getActivePieces() {
        return pieces;
    }

    public Location getEnPassantLocation() {
        return ep_capture;
    }

    public Pawn getEnPassantPawn() {
        return ep_pawn;
    }

    public Chessboard getGrid() {
        return grid;
    }

    // locs atacadas por uma peca preta
    // nao inclui pecas ocupadas por uma peca preta nao defendida por outra peca
    public HashSet<Location> getLocsControlledByBlack() {
        return black_locs;
    }

    public HashSet<Location> getLocsControlledByWhite() {
        return white_locs;
    }

    public boolean isCheckmate() {
        return isInCheck() && escape_moves.isEmpty();
    }

    public boolean isWhitesTurn() {
        return whiteTurn;
    }

    // escolhe uma Move valida aleatoriamente
    public void makeAutomaticMove() {
        ArrayList<Move> moves = new ArrayList<Move>();
        for(Entry<Location, Piece> e : grid.getEntrySet()) {
            Piece p = e.getValue();
            if(isWhitesTurn() != p.isWhite())
                continue;

            for(Location loc : p.getMoves()) {
                if(loc != null)
                    moves.add(new Move(p.getLocation(), loc));
            }
        }

        if(moves.isEmpty())
            throw new NoSuchElementException("No moves.");
        Move m = moves.get((int)(Math.random() * moves.size()));
        // TODO  castle, promocao
        move(m.getSource(), m.getDestination());
    }

    public boolean move(Location src, Location dest) throws InvalidLocationException, IllegalMoveException {
        if(!grid.isValid(src))
            throw new InvalidLocationException(src);
        if(!grid.isValid(dest))
            throw new InvalidLocationException(dest);

        Piece p = grid.get(src);
        if(p instanceof Pawn && dest.getRank() == (p.isWhite() ? 7 : 0)) {
            // promocao peao
            return move(src, dest, 'Q');
        }
        if(p instanceof King && !p.getAttackedLocations().contains(dest)) {
            // testar se e um castle
            King k = (King) p;
            Rook r;
            Piece temp;
            if(dest.equals(new Location("g1")))
                temp = grid.get(new Location("h1"));
            else if(dest.equals(new Location("c1")))
                temp = grid.get(new Location("a1"));
            else if(dest.equals(new Location("c8")))
                temp = grid.get(new Location("a8"));
            else if(dest.equals(new Location("g8")))
                temp = grid.get(new Location("h8"));
            else
                throw new IllegalMoveException(p, dest);
            if(!(temp instanceof Rook))
                throw new IllegalMoveException(p, dest);

            r = (Rook) temp;
            if(!k.canCastle(r)) {
                throw new IllegalMoveException("That castling move is not allowed.", p, dest);
            }

            previousState = new GameState(whiteTurn, new Chessboard(grid.getEntrySet()),
                    ep_capture, ep_pawn);

            // Faz o castle
            ep_capture = null;
            ep_pawn = null;
            int dir = r.getLocation().getFile() > k.getLocation().getFile() ? 90 : -90;
            Location loc = k.getLocation().getAdjacentLocation(dir).getAdjacentLocation(dir);
            grid.put(loc, k);
            grid.put(loc.getAdjacentLocation(-dir), r);

            for (Piece piece : pieces)
                piece.getMoves();
            updateControlledLocations();

            Move.Special spec = (dest.getFile() == 2
                    ? Move.Special.QUEENSIDE_CASTLE
                    : Move.Special.KINGSIDE_CASTLE);

            moves.append(new Move(src, dest, spec));

        } else {
            if(!p.canMove(dest))
                throw new IllegalMoveException(p, dest);
            Move m = new Move(src, dest);

            if(isInCheck() && !escape_moves.contains(m)) {
                System.out.println(m);
                throw new InCheckException(escape_moves);
            }

            Move move = new Move(src, dest);

            GameState prev = previousState;
            previousState = new GameState(whiteTurn, new Chessboard(grid.getEntrySet()),
                    ep_capture, ep_pawn);

            Piece target = grid.put(dest, p);
            if(p instanceof Pawn) {
                if (dest.equals(previousState.ep_loc)) {
                    // captura En passant
                    target = previousState.ep_pawn;
                    grid.remove(ep_pawn.getLocation());
                    move.setSpecial(Move.Special.EN_PASSANT);
                }
                else if (Math.abs(src.getRank() - dest.getRank()) == 2) {
                    // movimento En passant
                    ep_capture = new Location(dest.getRank() + (p.isWhite() ? -1 : 1), src.getFile());
                    ep_pawn = (Pawn) p;
                }
            }
            if(target != null) {
                pieces.remove(target);
            }
            updateControlledLocations();

            // Nao se podem fazer movimentos que ponham o rei em check
            if(isInCheck()) {
                ep_capture = previousState.ep_loc;
                ep_pawn = previousState.ep_pawn;
                grid.put(src, p);
                previousState = prev;
                if(target != null) {
                    grid.put(dest, target);
                    pieces.add(target);
                }
                else
                    grid.remove(dest);
                updateControlledLocations();
                throw new IllegalMoveException("Moving " + p + " will place the king in check.",
                        p, dest);
            }

            moves.append(move);
        }

        // muda o turno
        whiteTurn = !whiteTurn;
        canUndo = true;

        boolean inCheck = isInCheck();
        for(Piece piece : pieces)
            piece.update();

        if(inCheck) {
            escape_moves = getEscapeMoves();
        } else {
            escape_moves = null;
            updateAllowedMoves();
        }

        return inCheck;
    }

    // move uma peca. tenta promover o peao
    public boolean move(Location src, Location dest, char promotion) {
        if(!grid.isValid(src))
            throw new InvalidLocationException(src);
        if(!grid.isValid(dest))
            throw new InvalidLocationException(dest);

        Piece p = grid.get(src);
        if(!(p instanceof Pawn))
            throw new IllegalArgumentException("\"" + promotion + "\" is not a proper input "
                    + " for this move.");
        if(dest.getRank() != 7)
            throw new IllegalMoveException("That's not a valid promotion.", p, dest);
        Pawn pawn = (Pawn) p;
        if(!pawn.canMove(dest))
            throw new IllegalMoveException(pawn, dest);

        Piece upgrade;
        switch(Character.toUpperCase(promotion)) {
            case 'Q':
                upgrade = new Queen(pawn.isWhite(), this, dest);
                break;
            case 'R':
                upgrade = new Rook(pawn.isWhite(), this, dest);
                break;
            case 'B':
                upgrade = new Bishop(pawn.isWhite(), this, dest);
                break;
            case 'N':
                upgrade = new Knight(pawn.isWhite(), this, dest);
                break;
            default:
                throw new IllegalArgumentException("" + promotion + " does not stand for a "
                        + " valid piece you may promote to. Must be Q, R, B, or N.");
        }

        previousState = new GameState(whiteTurn, new Chessboard(grid.getEntrySet()), ep_capture, ep_pawn);

        Piece target = grid.put(dest, upgrade);
        grid.remove(src);
        if(target != null) {
            pieces.remove(target);
        }

        whiteTurn = !whiteTurn;
        canUndo = true;

        moves.append(new Move(src, dest, Move.Special.parseChar(promotion)));

        boolean inCheck = isInCheck();
        for(Piece piece : pieces)
            piece.update();

        if(inCheck) {
            escape_moves = getEscapeMoves();
//			moves.append(escape_moves.isEmpty() ? '#' : '+');
        }
        else {
            escape_moves = null;
            updateAllowedMoves();
        }

        return inCheck;
    }

    public void setGrid(Chessboard grid) {
        this.grid = grid;
        pieces.clear();
        for(Entry<Location, Piece> e : grid.getEntrySet()) {
            e.getValue().setLocation(e.getKey());
            pieces.add(e.getValue());
        }
    }

    // Guarda o jogo num formato a poder ser jogado outravez
    /*public Playback toPlayback(String title) {
        return new Playback(moves, title);
    }
    */

    // Devolve todas as jogadas feitas
    @Override
    public String toString() {
        if(moves.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        moves.reset();

        int i = 1;
        while (moves.currentMove() != null) {
            if(i % 2 == 1) {
                sb.append((i + 1) / 2);
                sb.append(". ");
            }

            sb.append(moves.currentMove().toString());
            sb.append(' ');
            moves.nextMove();
            i++;
        }

        return sb.toString();
    }

    public void undo() {
        if(!canUndo)
            return;

        // Revert to the previous state.
        setGrid(previousState.board);
        ep_capture = previousState.ep_loc;
        ep_pawn = previousState.ep_pawn;
        whiteTurn = previousState.isWhitesTurn;
        moves.delete();

        // Prevent another takeback.
        previousState = null;
        canUndo = false;

    }

    // Novo jogo de xadrez. Cria todas as pecas e coloca-as na grid
    private void defaultPosition() {
        pieces = new ArrayList<Piece>(32);
        // Kings
        K = new King(true, this, new Location("e1"));
        k = new King(false, this, new Location("e8"));
        pieces.add(k); pieces.add(K);

        // Pawns
        for(int i = 0; i < 8; i++) {
            Pawn p1 = new Pawn(true, this, new Location("" + (char)('a' + i) + "2"));
            Pawn p2 = new Pawn(false, this, new Location("" + (char)('a' + i) + "7"));
            pieces.add(p1);
            pieces.add(p2);
        }

        // Knights
        Knight n1, n2, n3, n4;
        n1 = new Knight(true, this, new Location("b1"));
        n2 = new Knight(true, this, new Location("g1"));
        n3 = new Knight(false, this, new Location("b8"));
        n4 = new Knight(false, this, new Location("g8"));
        pieces.add(n1); pieces.add(n2); pieces.add(n3); pieces.add(n4);

        // Bishops
        Bishop b1, b2, b3, b4;
        b1 = new Bishop(true, this, new Location("c1"));
        b2 = new Bishop(true, this, new Location("f1"));
        b3 = new Bishop(false, this, new Location("c8"));
        b4 = new Bishop(false, this, new Location("f8"));
        pieces.add(b1); pieces.add(b2); pieces.add(b3); pieces.add(b4);

        // Rooks
        Rook r1, r2, r3, r4;
        r1 = new Rook(true, this, new Location("a1"));
        r2 = new Rook(true, this, new Location("h1"));
        r3 = new Rook(false, this, new Location("a8"));
        r4 = new Rook(false, this, new Location("h8"));
        pieces.add(r1); pieces.add(r2); pieces.add(r3); pieces.add(r4);

        // Queens
        Queen q1, q2;
        q1 = new Queen(true, this, new Location("d1"));
        q2 = new Queen(false, this, new Location("d8"));
        pieces.add(q1); pieces.add(q2);

        for(Piece p : pieces)
            grid.put(p.getLocation(), p);
    }

    // Moves que tiram o rei da posicao de check
    private HashSet<Move> getEscapeMoves() {
        HashSet<Move> result = new HashSet<Move>(3);
        ArrayList<Piece> all_pieces = new ArrayList<Piece>(pieces.size());
        for(Piece p : pieces)
            all_pieces.add(p);

        for(Piece p : all_pieces) {
            if(p.isWhite() != isWhitesTurn())
                continue;
            Location prev = p.getLocation();
            for(Location loc : p.getMoves()) {
                Piece captured = grid.put(loc, p);
                if(captured != null)
                    pieces.remove(captured);
                updateControlledLocations();
                if(!isInCheck())
                    result.add(new Move(prev, loc));
                grid.put(prev, p);
                if(captured != null) {
                    grid.put(loc, captured);
                    pieces.add(captured);
                }
                else
                    grid.remove(loc);
                updateControlledLocations();
            }
        }
        return result;
    }

    private boolean isInCheck() {
        return isWhitesTurn() ? getLocsControlledByBlack().contains(K.getLocation()) :
                getLocsControlledByWhite().contains(k.getLocation());
    }

    private void updateAllowedMoves() {
        HashSet<Location> enemy = isWhitesTurn() ? black_locs : white_locs;
        King our_king = isWhitesTurn() ? K : k;
        ArrayList<Piece> all_pieces = new ArrayList<Piece>(pieces.size());
        for(Piece p : pieces) {
            all_pieces.add(p);
        }

        for(Piece p : all_pieces) {
            if((isWhitesTurn() != p.isWhite()) || p instanceof King)
                continue;

            Location prev = p.getLocation();
            Iterator<Location> i = p.getMoves().iterator();
            while(i.hasNext()) {
                Location loc = i.next();
                Piece captured = grid.put(loc, p);
                if(captured != null)
                    pieces.remove(captured);
                updateControlledLocations();
                if(enemy.contains(our_king.getLocation())) {
                    System.out.println("Removing location " + loc + " for " + p);
                    i.remove();
                }
                grid.put(prev, p);
                if(captured != null) {
                    grid.put(loc, captured);
                    pieces.add(captured);
                }
                else
                    grid.remove(loc);
                updateControlledLocations();
            }
        }
    }

    private void updateControlledLocations() {
        white_locs.clear();
        black_locs.clear();
        for(Piece p : pieces) {
            if(p.isWhite())
                white_locs.addAll(p.getAttackedLocations());
            else
                black_locs.addAll(p.getAttackedLocations());
        }
    }
}
