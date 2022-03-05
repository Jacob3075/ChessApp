package com.jacob.engine.pieces;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Spot;
import org.jetbrains.annotations.NotNull;

public class Queen extends Piece {
    public Queen(boolean white) {
        super(white, "q", 9);
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

        // since a queen moves either like a bishop or a rook, check to see if either is possible
        Bishop queenAsBishop = new Bishop(this.isWhite());
        boolean canQueenMoveAsBishop = queenAsBishop.canMove(board, start, end);
        Rook queenAsRook = new Rook(this.isWhite());
        boolean canQueenMoveAsRook = queenAsRook.canMove(board, start, end);

        if(!(canQueenMoveAsBishop || canQueenMoveAsRook)) {
            return false;
        }

        // move is legal if player's king is not attacked after making it
        return !this.isKingAttackedAfterMove(board, start, end);
    }
}
