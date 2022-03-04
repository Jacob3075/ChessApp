package com.jacob.engine.pieces;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Spot;
import org.jetbrains.annotations.NotNull;

public class Bishop extends Piece {
    public Bishop(boolean white) {
        super(white, "b");
    }

    @Override
    public boolean canMove(Board board, Spot start, @NotNull Spot end) {
        Piece destPiece = end.getPiece();

        // we can't move the piece to a spot that has a piece of the same colour
        if(destPiece != null && destPiece.isWhite() == this.isWhite()) {
            return false;
        }

        int di = start.getI() - end.getI();
        int dj = start.getJ() - end.getJ();

        // bishops move by an equal amount in both directions
        if(Math.abs(di) != Math.abs(dj)) {
            return false;
        }

        // delta is negative if the bishop is moving down the board and positive if moving up
        int deltaI = di > 0 ? -1 : 1;
        int deltaJ = dj > 0 ? -1 : 1;

        int currentI = start.getI() + deltaI;
        int currentJ = start.getJ() + deltaJ;

        // checking the spots between start and end one by one to see if any have a piece
        while(currentI != end.getI()) {
            if(board.getSpot(currentI, currentJ).getPiece() != null) {
                return false;
            }
            currentI += deltaI;
            currentJ += deltaJ;
        }

        return true;
    }
}
