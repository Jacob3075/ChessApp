package com.jacob.engine.pieces;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Spot;

public class King extends Piece {
    private boolean canCastle = false;
    private boolean moved = false;
    private boolean queenSideCastlingDone = false;
    private boolean kingSideCastlingDone = false;

    public King(boolean white) {
        super(white, "k", 0);
    }

    public boolean isCastlingPossible() {
        return this.canCastle;
    }

    public void setCastlingPossible(boolean canCastle) {
        this.canCastle = canCastle;
    }

    public boolean hasMoved() {
        return this.moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean isQueenSideCastlingDone() {
        return queenSideCastlingDone;
    }

    public void setQueenSideCastlingDone(boolean queenSideCastlingDone) {
        this.queenSideCastlingDone = queenSideCastlingDone;
    }

    public boolean isKingSideCastlingDone() {
        return kingSideCastlingDone;
    }

    public void setKingSideCastlingDone(boolean kingSideCastlingDone) {
        this.kingSideCastlingDone = kingSideCastlingDone;
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        if(start == null || end == null || start == end) {
            return false;
        }
        
        Piece capturedPiece = end.getPiece();

        // we can't move the piece to a spot that has a piece of the same colour
        if(capturedPiece != null && capturedPiece.isWhite() == this.isWhite()) {
            return false;
        }

        int di = Math.abs(start.getI() - end.getI());
        int dj = Math.abs(start.getJ() - end.getJ());

        if((di | dj) == 1) {
            // check if this move will result in the king being attacked. If so, return false
            return !isKingAttackedAfterMove(board, start, end);
        }

        if(di > 2 || dj > 2 || (di == 2 && dj > 0) || (dj == 2 && di > 0)) {
            return false;
        }

        if(this.isValidCastling(board, start, end)) {
            this.setCastlingPossible(true);
            return true;
        }
        else {
            this.setCastlingPossible(false);
            return false;
        }
    }

    private boolean isValidCastling(Board board, Spot start, Spot end) {
        int di = start.getI() - end.getI();
        int dj = start.getJ() - end.getJ();

        // castling should not have been done previously
        if(this.isKingSideCastlingDone() || this.isQueenSideCastlingDone()) {
            return false;
        }

        // king has to move horizontally by 2 spots
        if(!(Math.abs(dj) == 2 && di == 0)) {
            return false;
        }

        // king should not have been moved previously
        if(this.hasMoved()) {
            return false;
        }

        // no pieces should be between king and rook
        Spot spot;
        // king side castling
        if(dj < 0) {
            for(int j = 1; j < 3; j++) {
                spot = board.getSpot(start.getI(), start.getJ()+j);
                // a piece is between the king and rook
                if(spot.getPiece() != null) {
                    return false;
                }
                // this spot is under attack and the king has to go through it
                if(isKingAttackedAfterMove(board, start, spot)) {
                    return false;
                }
            }
            spot = board.getSpot(start.getI(), start.getJ()+3);
        }
        // queen side castling
        else {
            for(int j = 1; j < 4; j++) {
                spot = board.getSpot(start.getI(), start.getJ()-j);
                // a piece is between the king and rook
                if(spot.getPiece() != null) {
                    return false;
                }
                // this spot is under attack and the king has to go through it
                if(j < 3 && isKingAttackedAfterMove(board, start, spot)) {
                    return false;
                }
            }
            spot = board.getSpot(start.getI(), start.getJ()-4);
        }
        Piece piece = spot.getPiece();

        // if the other piece is not of the same colour
        if(piece.isWhite() != this.isWhite()) {
            return false;
        }

        // the castle side rook should not have been moved previously
        if(!(piece instanceof Rook && !((Rook) piece).hasMoved())) {
            return false;
        }

        // move is legal if player's king is not attacked after making it
        return !this.isKingAttackedAfterMove(board, start, end);
    }

}