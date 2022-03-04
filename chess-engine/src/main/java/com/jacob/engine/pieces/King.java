package com.jacob.engine.pieces;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Spot;

public class King extends Piece {
    private boolean castlingDone = false;
    private boolean canCastle = false;
    private boolean moved = false;

    public King(boolean white) {
        super(white, "k");
    }

    public boolean isCastlingDone() {
        return this.castlingDone;
    }

    public void setCastlingDone(boolean castlingDone) {
        this.castlingDone = castlingDone;
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

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        Piece destPiece = end.getPiece();

        // we can't move the piece to a spot that has a piece of the same colour
        if(destPiece != null && destPiece.isWhite() == this.isWhite()) {
            return false;
        }

        int di = Math.abs(start.getI() - end.getI());
        int dj = Math.abs(start.getJ() - end.getJ());

        if((di | dj) == 1) {
            // TODO: check if this move will not result in the king being attacked. If so, return true
            return true;
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

        // king has to move horizontally by 2 spots
        if(!(Math.abs(dj) == 2 && di == 0)) {
            return false;
        }

        // castling should not have been done already
        if(this.isCastlingDone()) {
            return false;
        }

        // king should not have been moved previously
        if(this.hasMoved()) {
            return false;
        }

        // TODO: king should not go through an attacked spot
        // no pieces should be between king and rook
        Spot spot;
        if(dj > 0) {
            for(int j = 1; j < 3; j++) {
                spot = board.getSpot(start.getI(), start.getJ()+j);
                if(spot.getPiece() != null) {
                    return false;
                }
            }
            spot = board.getSpot(start.getI(), start.getJ()+3);
        }
        else {
            for(int j = 1; j < 4; j++) {
                spot = board.getSpot(start.getI(), start.getJ()-j);
                if(spot.getPiece() != null) {
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
        return (!(piece instanceof Rook && !((Rook) piece).hasMoved()));
    }
}