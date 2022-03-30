package com.jacob.engine.pieces;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Spot;

public class Pawn extends Piece {
    private boolean moved = false;
    private boolean canPromote = false;
    private boolean enPassant = false;
    private boolean movedTwoSpotsOnPreviousTurn = false;

    public Pawn(boolean white) {
        super(white, "p", 1);
    }

    public boolean hasMoved() {
        return this.moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean isEnPassantPossible() {
        return this.enPassant;
    }

    public void setEnPassantPossible(boolean enPassant) {
        this.enPassant = enPassant;
    }

    public boolean isPromotionPossible() {
        return this.canPromote;
    }

    public void setPromotionPossible(boolean canPromote) {
        this.canPromote = canPromote;
    }

    public boolean isMovedTwoSpotsOnPreviousTurn() {
        return movedTwoSpotsOnPreviousTurn;
    }

    public void setMovedTwoSpotsOnPreviousTurn(boolean movedTwoSpotsOnPreviousTurn) {
        this.movedTwoSpotsOnPreviousTurn = movedTwoSpotsOnPreviousTurn;
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        if(start == null || end == null || start == end)
            return false;
        
        Piece capturedPiece = end.getPiece();

        int di = start.getI() - end.getI();
        int dj = start.getJ() - end.getJ();

        // white pawns move up and black pawns move down
        if((this.isWhite() && di >= 0) || (!this.isWhite() && di <= 0) || Math.abs(di) > 2)
            return false;

        // pawn moves one spot
        if(Math.abs(di) == 1) {
            // going straight
            if(dj == 0) {
                if(capturedPiece != null)
                    return false;
            }
            // going diagonal
            else if(Math.abs(dj) == 1) {
                // en passant
                if(capturedPiece == null) {
                    capturedPiece = board.getSpot(start.getI(), end.getJ()).getPiece();

                    if(!(capturedPiece instanceof Pawn
                            && capturedPiece.isWhite() != this.isWhite()
                            && ((Pawn) capturedPiece).isMovedTwoSpotsOnPreviousTurn())) {
                        return false;
                    }
                    this.setEnPassantPossible(true);
                }
                // regular pawn capture
                else if(this.isWhite() == capturedPiece.isWhite())
                    return false;
            }
            // pawns can't move horizontally by more than 2 spots in a single move
            else
                return false;
        }

        // pawn moves 2 spots
        int deltaI = this.isWhite() ? 1 : -1;
        if(Math.abs(di) == 2) {
            if(this.hasMoved())
                return false;
            if(dj != 0)
                return false;
            else {
                if(board.getSpot(start.getI() + deltaI, start.getJ()).getPiece() != null)
                    return false;
                if(board.getSpot(start.getI() + 2*deltaI, start.getJ()).getPiece() != null)
                    return false;

                this.setMovedTwoSpotsOnPreviousTurn(true);
            }
        }

        if(this.isKingAttackedAfterMove(board, start, end))
            return false;

        // if the final position of the pawn is on an edge rank, pawn promotion is possible
        if((this.isWhite() && end.getI() == 7) || (!this.isWhite() && end.getI() == 0))
            this.setPromotionPossible(true);

        return true;
    }

}
