package com.jacob.engine.pieces;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Spot;
import org.jetbrains.annotations.NotNull;

public abstract class Piece {
    private boolean white = false;
    private String symbol;

    protected Piece(boolean white, String symbol) {
        this.setWhite(white);
        this.setSymbol(symbol);
    }

    public boolean isWhite() {
        return this.white;
    }

    public void setWhite(boolean white) {
        this.white = white;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public abstract boolean canMove(Board board, Spot start, Spot end);

    public boolean isKingAttackedAfterMove(Board board, Spot start, Spot end) {
        // a piece which could be attacking the king
        Piece attackingPiece;

        // the coordinates of the attacking piece on the board
        int ti;
        int tj;

        // the spot where the king is
        Spot kingSpot = board.getSpot(0,0);
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Piece piece = board.getSpot(i, j).getPiece();
                if(piece instanceof King && piece.isWhite() == this.isWhite()) {
                    kingSpot = board.getSpot(i, j);
                }
            }
        }

        // if we are moving the king, we need to check if the end spot is being attacked
        if(start.getPiece() instanceof King) {
            kingSpot = end;
        }

        // attacked by pawn?
        ti = this.isWhite() ? kingSpot.getI()+1 : kingSpot.getI()-1;
        tj = kingSpot.getJ();
        attackingPiece = ti<8 && ti>=0 && tj-1>=0 ? board.getSpot(ti, tj-1).getPiece() : null;
        if(attackingPiece instanceof Pawn && attackingPiece.isWhite() != this.isWhite()) {
            return true;
        }
        attackingPiece = ti<8 && ti>=0 && tj+1<8 ? board.getSpot(ti, tj+1).getPiece() : null;
        if(attackingPiece instanceof Pawn && attackingPiece.isWhite() != this.isWhite()) {
            return true;
        }

        // attacked by knight?
        for(ti = kingSpot.getI()-2; ti <= kingSpot.getI()+2; ti++) {
            for(tj = kingSpot.getJ()-2; tj <= kingSpot.getJ()+2; tj++) {
                if(ti >= 0 && ti < 8 && tj >= 0 && tj < 8) {
                    attackingPiece = board.getSpot(ti, tj).getPiece();
                    int di = Math.abs(ti-kingSpot.getI());
                    int dj = Math.abs(tj-kingSpot.getJ());

                    if(di * dj == 2
                        && attackingPiece instanceof Knight
                        && attackingPiece.isWhite() != this.isWhite()) {
                        return true;
                    }
                }
            }
        }

        // straight up
        attackingPiece = null;
        ti = kingSpot.getI()+1;
        while(ti < 8) {
            attackingPiece = board.getSpot(ti, kingSpot.getJ()).getPiece();
            if(attackingPiece != null) {
                break;
            }
            ti++;
        }
        if(attackingPiece != null
            && attackingPiece.isWhite() != this.isWhite()
            && (attackingPiece instanceof Rook
                || attackingPiece instanceof Queen
                || (attackingPiece instanceof King && ti == kingSpot.getI()+1))) {
            return true;
        }

        // straight down
        attackingPiece = null;
        ti = kingSpot.getI()-1;
        while(ti >= 0) {
            attackingPiece = board.getSpot(ti, kingSpot.getJ()).getPiece();
            if(attackingPiece != null) {
                break;
            }
            ti--;
        }
        if(attackingPiece != null
            && attackingPiece.isWhite() != this.isWhite()
            && (attackingPiece instanceof Rook
                || attackingPiece instanceof Queen
                || (attackingPiece instanceof King && ti == kingSpot.getI()-1))) {
            return true;
        }

        // straight right
        attackingPiece = null;
        tj = kingSpot.getJ()+1;
        while(tj < 8) {
            attackingPiece = board.getSpot(kingSpot.getI(), tj).getPiece();
            if(attackingPiece != null) {
                break;
            }
            tj++;
        }
        if(attackingPiece != null
            && attackingPiece.isWhite() != this.isWhite()
            && (attackingPiece instanceof Rook
                || attackingPiece instanceof Queen
                || (attackingPiece instanceof King && tj == kingSpot.getJ()+1))) {
            return true;
        }

        // straight left
        attackingPiece = null;
        tj = kingSpot.getJ()-1;
        while(tj >= 0) {
            attackingPiece = board.getSpot(kingSpot.getI(), tj).getPiece();
            if(attackingPiece != null) {
                break;
            }
            tj--;
        }
        if(attackingPiece != null
            && attackingPiece.isWhite() != this.isWhite()
            && (attackingPiece instanceof Rook
                || attackingPiece instanceof Queen
                || (attackingPiece instanceof King && tj == kingSpot.getJ()-1))) {
            return true;
        }

        // diagonal up left
        attackingPiece = null;
        ti = kingSpot.getI()+1;
        tj = kingSpot.getJ()-1;
        while(ti < 8 && tj >= 0) {
            attackingPiece = board.getSpot(ti, tj).getPiece();
            if(attackingPiece != null) {
                break;
            }
            ti++;
            tj--;
        }
        if(attackingPiece != null
            && attackingPiece.isWhite() != this.isWhite()
            && (attackingPiece instanceof Bishop
                || attackingPiece instanceof Queen
                || (attackingPiece instanceof King
                    && ti == kingSpot.getI()+1
                    && tj == kingSpot.getJ()-1))) {
            return true;
        }

        // diagonal up right
        attackingPiece = null;
        ti = kingSpot.getI()+1;
        tj = kingSpot.getJ()+1;
        while(ti < 8 && tj < 8) {
            attackingPiece = board.getSpot(ti, tj).getPiece();
            if(attackingPiece != null) {
                break;
            }
            ti++;
            tj++;
        }
        if(attackingPiece != null
                && attackingPiece.isWhite() != this.isWhite()
                && (attackingPiece instanceof Bishop
                || attackingPiece instanceof Queen
                || (attackingPiece instanceof King
                && ti == kingSpot.getI()+1
                && tj == kingSpot.getJ()+1))) {
            return true;
        }

        // diagonal down left
        attackingPiece = null;
        ti = kingSpot.getI()-1;
        tj = kingSpot.getJ()-1;
        while(ti >= 0 && tj >= 0) {
            attackingPiece = board.getSpot(ti, tj).getPiece();
            if(attackingPiece != null) {
                break;
            }
            ti--;
            tj--;
        }
        if(attackingPiece != null
                && attackingPiece.isWhite() != this.isWhite()
                && (attackingPiece instanceof Bishop
                || attackingPiece instanceof Queen
                || (attackingPiece instanceof King
                && ti == kingSpot.getI()-1
                && tj == kingSpot.getJ()-1))) {
            return true;
        }

        // diagonal down right
        attackingPiece = null;
        ti = kingSpot.getI()-1;
        tj = kingSpot.getJ()+1;
        while(ti >= 0 && tj < 8) {
            attackingPiece = board.getSpot(ti, tj).getPiece();
            if(attackingPiece != null) {
                break;
            }
            ti--;
            tj++;
        }
        if(attackingPiece != null
                && attackingPiece.isWhite() != this.isWhite()
                && (attackingPiece instanceof Bishop
                || attackingPiece instanceof Queen
                || (attackingPiece instanceof King
                && ti == kingSpot.getI()-1
                && tj == kingSpot.getJ()+1))) {
            return true;
        }

        // if this line is reached, then the kingSpot is not under attack
        return false;
    }
}
