package com.jacob.engine.pieces;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Spot;

public class Knight extends Piece {
    public Knight(boolean white) {
        super(white, "n", 3);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        if(start == null || end == null || start == end)
            return false;
        
        Piece capturedPiece = end.getPiece();

        // we can't move the piece to a spot that has a piece of the same colour
        if(capturedPiece != null && capturedPiece.isWhite() == this.isWhite())
            return false;

        int deltaI = Math.abs(start.getI() - end.getI());
        int deltaJ = Math.abs(start.getJ() - end.getJ());

        // for every possible knight move the product of deltaI and deltaJ is equal to 2
        if(deltaI * deltaJ != 2)
            return false;

        // move is legal if player's king is not attacked after making it
        return !this.isKingAttackedAfterMove(board, start, end);
    }
}
