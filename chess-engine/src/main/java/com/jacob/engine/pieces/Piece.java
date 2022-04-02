package com.jacob.engine.pieces;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Spot;

public abstract class Piece {
    private boolean white = false;
    private String symbol;
    private int value;

    protected Piece(boolean white, String symbol, int value) {
        setWhite(white);
        setSymbol(symbol);
        setValue(value);
    }

    public boolean isWhite() {
        return white;
    }

    public void setWhite(boolean white) {
        this.white = white;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        if (isWhite())
            this.symbol = symbol.toUpperCase();
        else
            this.symbol = symbol.toLowerCase();
    }

    public abstract boolean canMove(Board board, Spot start, Spot end);

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // moves the piece from the start spot to the end spot, and then checks if king is attacked
    public boolean isKingAttackedAfterMove(Board board, Spot start, Spot end) {
        Piece pieceMoved = start.getPiece();
        Piece pieceCaptured = end.getPiece();
        Piece attackingPiece;
        start.setPiece(null);
        end.setPiece(pieceMoved);

        // the coordinates of the attacking piece on the board
        int ti;
        int tj;

        Spot kingSpot = getKingSpot(board, start, end);

        if(isKingSpotAttackedByPawn(board, kingSpot)
            || isKingSpotAttackedByKnight(board, kingSpot)) {
            start.setPiece(pieceMoved);
            end.setPiece(pieceCaptured);
            return true;
        }

        // straight up
        attackingPiece = null;
        ti = kingSpot.getI()+1;
        while (ti < 8) {
            attackingPiece = board.getSpot(ti, kingSpot.getJ()).getPiece();
            if (attackingPiece != null) {
                break;
            }
            ti++;
        }
        if (attackingPiece != null
            && attackingPiece.isWhite() != this.isWhite()
            && (attackingPiece instanceof Rook
                || attackingPiece instanceof Queen
                || (attackingPiece instanceof King && ti == kingSpot.getI()+1))) {
            start.setPiece(pieceMoved);
            end.setPiece(pieceCaptured);
            return true;
        }

        // straight down
        attackingPiece = null;
        ti = kingSpot.getI()-1;
        while (ti >= 0) {
            attackingPiece = board.getSpot(ti, kingSpot.getJ()).getPiece();
            if (attackingPiece != null) {
                break;
            }
            ti--;
        }
        if (attackingPiece != null
            && attackingPiece.isWhite() != this.isWhite()
            && (attackingPiece instanceof Rook
                || attackingPiece instanceof Queen
                || (attackingPiece instanceof King && ti == kingSpot.getI()-1))) {
            start.setPiece(pieceMoved);
            end.setPiece(pieceCaptured);
            return true;
        }

        // straight right
        attackingPiece = null;
        tj = kingSpot.getJ()+1;
        while (tj < 8) {
            attackingPiece = board.getSpot(kingSpot.getI(), tj).getPiece();
            if (attackingPiece != null) {
                break;
            }
            tj++;
        }
        if (attackingPiece != null
            && attackingPiece.isWhite() != this.isWhite()
            && (attackingPiece instanceof Rook
                || attackingPiece instanceof Queen
                || (attackingPiece instanceof King && tj == kingSpot.getJ()+1))) {
            start.setPiece(pieceMoved);
            end.setPiece(pieceCaptured);
            return true;
        }

        // straight left
        attackingPiece = null;
        tj = kingSpot.getJ()-1;
        while (tj >= 0) {
            attackingPiece = board.getSpot(kingSpot.getI(), tj).getPiece();
            if (attackingPiece != null) {
                break;
            }
            tj--;
        }
        if (attackingPiece != null
            && attackingPiece.isWhite() != this.isWhite()
            && (attackingPiece instanceof Rook
                || attackingPiece instanceof Queen
                || (attackingPiece instanceof King && tj == kingSpot.getJ()-1))) {
            start.setPiece(pieceMoved);
            end.setPiece(pieceCaptured);
            return true;
        }

        // diagonal up left
        attackingPiece = null;
        ti = kingSpot.getI()+1;
        tj = kingSpot.getJ()-1;
        while (ti < 8 && tj >= 0) {
            attackingPiece = board.getSpot(ti, tj).getPiece();
            if (attackingPiece != null) {
                break;
            }
            ti++;
            tj--;
        }
        if (attackingPiece != null
            && attackingPiece.isWhite() != this.isWhite()
            && (attackingPiece instanceof Bishop
                || attackingPiece instanceof Queen
                || (attackingPiece instanceof King
                    && ti == kingSpot.getI()+1
                    && tj == kingSpot.getJ()-1))) {
            start.setPiece(pieceMoved);
            end.setPiece(pieceCaptured);
            return true;
        }

        // diagonal up right
        attackingPiece = null;
        ti = kingSpot.getI()+1;
        tj = kingSpot.getJ()+1;
        while (ti < 8 && tj < 8) {
            attackingPiece = board.getSpot(ti, tj).getPiece();
            if (attackingPiece != null) {
                break;
            }
            ti++;
            tj++;
        }
        if (attackingPiece != null
                && attackingPiece.isWhite() != this.isWhite()
                && (attackingPiece instanceof Bishop
                || attackingPiece instanceof Queen
                || (attackingPiece instanceof King
                && ti == kingSpot.getI()+1
                && tj == kingSpot.getJ()+1))) {
            start.setPiece(pieceMoved);
            end.setPiece(pieceCaptured);
            return true;
        }

        // diagonal down left
        attackingPiece = null;
        ti = kingSpot.getI()-1;
        tj = kingSpot.getJ()-1;
        while (ti >= 0 && tj >= 0) {
            attackingPiece = board.getSpot(ti, tj).getPiece();
            if (attackingPiece != null) {
                break;
            }
            ti--;
            tj--;
        }
        if (attackingPiece != null
                && attackingPiece.isWhite() != this.isWhite()
                && (attackingPiece instanceof Bishop
                || attackingPiece instanceof Queen
                || (attackingPiece instanceof King
                && ti == kingSpot.getI()-1
                && tj == kingSpot.getJ()-1))) {
            start.setPiece(pieceMoved);
            end.setPiece(pieceCaptured);
            return true;
        }

        // diagonal down right
        attackingPiece = null;
        ti = kingSpot.getI()-1;
        tj = kingSpot.getJ()+1;
        while (ti >= 0 && tj < 8) {
            attackingPiece = board.getSpot(ti, tj).getPiece();
            if (attackingPiece != null) {
                break;
            }
            ti--;
            tj++;
        }
        if (attackingPiece != null
                && attackingPiece.isWhite() != this.isWhite()
                && (attackingPiece instanceof Bishop
                || attackingPiece instanceof Queen
                || (attackingPiece instanceof King
                && ti == kingSpot.getI()-1
                && tj == kingSpot.getJ()+1))) {
            start.setPiece(pieceMoved);
            end.setPiece(pieceCaptured);
            return true;
        }

        // if this line is reached, then the kingSpot is not under attack
        start.setPiece(pieceMoved);
        end.setPiece(pieceCaptured);
        return false;
    }

    private Spot getKingSpot(Board board, Spot start, Spot end) {
        if (start.getPiece() instanceof King)
            return end;

        for (int row = 0; row < board.getSize(); row++) {
            for (int column = 0; column < board.getSize(); column++) {
                Piece piece = board.getSpot(row, column).getPiece();
                if (piece instanceof King && piece.isWhite() == this.isWhite())
                    return board.getSpot(row, column);
            }
        }

        return board.getSpot(0,0); // this line will never be reached in a valid game
    }

    private boolean isKingSpotAttackedByPawn(Board board, Spot kingSpot) {
        int ti = this.isWhite() ? kingSpot.getI()+1 : kingSpot.getI()-1; // the row on which an attacking pawn could be on
        int tj = kingSpot.getJ(); // the column on which an attacking pawn could be on


        Piece attackingPiece = board.getSpot(ti, tj-1) == null ? null : board.getSpot(ti, tj-1).getPiece();
        if (attackingPiece instanceof Pawn && attackingPiece.isWhite() != this.isWhite())
            return true;

        attackingPiece = board.getSpot(ti, tj+1) == null ? null : board.getSpot(ti, tj+1).getPiece();
        return (attackingPiece instanceof Pawn && attackingPiece.isWhite() != this.isWhite());
    }

    private boolean isKingSpotAttackedByKnight(Board board, Spot kingSpot) {
        for (int ti = kingSpot.getI()-2; ti <= kingSpot.getI()+2; ti++) {
            for (int tj = kingSpot.getJ()-2; tj <= kingSpot.getJ()+2; tj++) {
                if (board.getSpot(ti, tj) != null) {
                    Piece attackingPiece = board.getSpot(ti, tj).getPiece();
                    int di = Math.abs(ti-kingSpot.getI());
                    int dj = Math.abs(tj-kingSpot.getJ());

                    if (di * dj == 2
                            && attackingPiece instanceof Knight
                            && attackingPiece.isWhite() != this.isWhite())
                        return true;
                }
            }
        }

        return false;
    }


}
