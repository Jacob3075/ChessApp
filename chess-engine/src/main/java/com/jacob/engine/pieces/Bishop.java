package com.jacob.engine.pieces;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Spot;

public class Bishop extends Piece {
    public Bishop(boolean white) {
        super(white, "b", 3);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        if(start == null || end == null || start == end)
            return false;

        Piece capturedPiece = end.getPiece();

        // we can't move the piece to a spot that has a piece of the same colour
        if(capturedPiece != null && capturedPiece.isWhite() == this.isWhite())
            return false;

        int di = start.getI() - end.getI();
        int dj = start.getJ() - end.getJ();

        // bishops move by an equal amount in two directions
        if(Math.abs(di) != Math.abs(dj))
            return false;

        // deltaI is negative if the bishop is moving down and positive if moving up
        int deltaI = di > 0 ? -1 : 1;

        // deltaJ is negative if the bishop is moving left and positive if moving right
        int deltaJ = dj > 0 ? -1 : 1;

        int currentI = start.getI() + deltaI;
        int currentJ = start.getJ() + deltaJ;

        // checking all spots between start and end to see if any have a piece
        while(currentI != end.getI()) {
            if(board.getSpot(currentI, currentJ).getPiece() != null)
                return false;

            currentI += deltaI;
            currentJ += deltaJ;
        }

        // move is legal if player's king is not attacked after making it
        return !this.isKingAttackedAfterMove(board, start, end);
    }
}
