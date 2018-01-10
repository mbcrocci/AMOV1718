package com.example.mbcrocci.amov1718.move;


import com.example.mbcrocci.amov1718.board.Location;

public class Move {

    // Moves especiais (promocoes, checks, castles...)
    public static enum Special {
        BISHOP("B"), // promocao peao-bispo
        CHECK("+"),
        CHECKMATE("#"),
        /*
        Na ocasião do avanço por duas casas do peão,
        caso haja um peão adversário na coluna adjacente na quarta fileira para as brancas,
        ou quinta para as pretas, este pode capturar o peão como se "de passagem"
        (retirado da wikipedia */
        EN_PASSANT("e.p."),

        KINGSIDE_CASTLE("O-O"),
        KNIGHT("N"), // promocao peao-cavalo
        NONE(""), // jogada normal
        QUEEN("Q"), // promocao peao-rainha
        QUEENSIDE_CASTLE("O-O-O"),
        ROOK("R"); // promocao peao-torre

        private String desc;
        private Special(String desc) {
            this.desc = desc;
        }

        public String getDescription() {
            return desc;
        }

        public static Special parseChar(char c) {
            switch(Character.toLowerCase(c)) {
                case 'q':
                    return QUEEN;
                case 'r':
                    return ROOK;
                case 'n':
                    return KNIGHT;
                case 'b':
                    return BISHOP;
                default:
                    throw new IllegalArgumentException("Unexpected argument.");
            }
        }
    }

    private Special special;
    private Location src, dest;

    public Move(Location src, Location dest) {
        this(src, dest, Special.NONE);
    }

    public Move(String src, String dest) {
        this(src, dest, Special.NONE);
    }

    // Usado para movimentos especiais
    public Move(Location src, Location dest, Special spec) {
        super();
        this.src = src;
        this.dest = dest;
        this.special = spec;
    }

    public Move(String src, String dest, Special spec) {
        super();
        this.src = new Location(src);
        this.dest = new Location(dest);
        this.special = spec;
    }

    public boolean isCastle() {
        return special == Special.KINGSIDE_CASTLE ||
                special == Special.QUEENSIDE_CASTLE;
    }

    public boolean isPromotion() {
        return special == Special.QUEEN || special == Special.BISHOP ||
                special == Special.ROOK || special == Special.KNIGHT;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof Move))
            return false;

        Move m = (Move) o;
        boolean srceq = src.equals(m.getSource());
        boolean desteq = dest.equals(m.getDestination());

        return srceq && desteq;
    }

    @Override
    public int hashCode() {
        return src.hashCode() + dest.hashCode();
    }

    public Location getDestination() {
        return dest;
    }

    public Location getSource() {
        return src;
    }

    public Special getSpecial() {
        return special;
    }

    public void setSpecial(Special special) {
        this.special = special;
    }

    @Override
    public String toString() {
        String str =  getSource() + "-" + getDestination();
        switch(special) {
            case BISHOP:
            case QUEEN:
            case KNIGHT:
            case ROOK:
                str += "=" + special.desc;
                break;
//			case KINGSIDE_CASTLE:
//			case QUEENSIDE_CASTLE:
//				return special.desc;
//			case EN_PASSANT:
//				str += " " + special.desc;
//				break;
//			case CHECK:
//			case CHECKMATE:
//				str += special.desc;
            default:
                break;
        }
        return str;
    }
}
