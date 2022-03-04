package com.jacob.engine.pieces;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Spot;

public class Pawn extends Piece {
    public Pawn(boolean white) {
        super(white, "p");
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        // we can't move the piece to a spot that has a piece of the same colour
        if(end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }

        // implement canMove logic
        return true;
    }
}