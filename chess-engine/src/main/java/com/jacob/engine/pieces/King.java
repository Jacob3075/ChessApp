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
        return this.hasMoved();
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        // we can't move the piece to a Spot that has a piece of the same color
        if(end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }

        int dx = Math.abs(start.getX() - end.getX());
        int dy = Math.abs(start.getY() - end.getY());

        if((dx == 1 && dy == 0) || (dx == 0 && dy == 1) || (dx == 1 && dy == 1)) {
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
        int dx = start.getX() - end.getX();
        int dy = start.getY() - end.getY();

        // king has to move horizontally by 2 spots
        if(!(Math.abs(dx) == 2 && dy == 0)) {
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
        if(dx > 0) {
            for(int i = 1; i < 3; i++) {
                spot = board.getBox(start.getX()+i, start.getY());
                if(spot.getPiece() != null) {
                    return false;
                }
            }
            spot = board.getBox(start.getX()+3, start.getY());
        }
        else {
            for(int i = 1; i < 4; i++) {
                spot = board.getBox(start.getX()-i, start.getY());
                if(spot.getPiece() != null) {
                    return false;
                }
            }
            spot = board.getBox(start.getX()-4, start.getY());
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

        return true;
    }
}