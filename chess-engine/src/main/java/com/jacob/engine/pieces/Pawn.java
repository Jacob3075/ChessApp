package com.jacob.engine.pieces;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Spot;
import org.jetbrains.annotations.NotNull;

public class Pawn extends Piece {
    private boolean moved = false;
    private boolean canPromote = false;

    public Pawn(boolean white) {
        super(white, "p");
    }

    public boolean hasMoved() {
        return this.moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean isPromotionPossible() {
        return this.canPromote;
    }

    public void setPromotionPossible(boolean canPromote) {
        this.canPromote = canPromote;
    }

    @Override
    public boolean canMove(Board board, Spot start, @NotNull Spot end) {
        Piece destPiece = end.getPiece();

        int di = start.getI() - end.getI();
        int dj = start.getJ() - end.getJ();

        // white pawns move up and black pawns move down
        if((this.isWhite() && di >= 0) || (!this.isWhite() && di <= 0) || Math.abs(di) > 2) {
            return false;
        }

        // if pawn moving one spot, it either goes straight or diagonal
        if(Math.abs(di) == 1 && !(dj == 0 || Math.abs(dj) == 1)) {
            return false;
        }

        // pawn can only move two spots straight if this is it's first move
        if(Math.abs(di) == 2 && !(dj == 0 && !this.hasMoved())) {
            return false;
        }

        // pawn moves one spot straight
        if(Math.abs(di) == 1 && dj == 0) {
            if(destPiece != null) {
                return false;
            }
        }

        // pawn moves diagonal
        if(Math.abs(di) == 1) {
            if(destPiece == null) {
                // TODO: check if en passant
                return false;
            }
            else if(this.isWhite() == destPiece.isWhite()) {
                return false;
            }
        }

        // pawn moves 2 spots on its first turn
        int deltaI = this.isWhite() ? 1 : -1;
        if(Math.abs(di) == 2) {
            if(board.getSpot(start.getI() + deltaI, start.getJ()).getPiece() != null) {
                return false;
            }
            if(board.getSpot(start.getI() + 2*deltaI, start.getJ()).getPiece() != null) {
                return false;
            }
        }

        // if the final position of the pawn is on an edge rank, pawn promotion is possible
        if((this.isWhite() && end.getI() == 7) || (!this.isWhite() && end.getI() == 0)) {
            this.setPromotionPossible(true);
        }

        return true;
    }
}
