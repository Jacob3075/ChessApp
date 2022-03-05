package com.jacob.engine.pieces;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Spot;

public class Rook extends Piece {
    private boolean moved = false;

    public Rook(boolean white) {
        super(white, "r", 5);
    }

    public boolean hasMoved() {
        return this.moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
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

        int di = start.getI() - end.getI();
        int dj = start.getJ() - end.getJ();

        // rooks move along a single direction
        if((di == 0) == (dj == 0)) {
            return false;
        }

        // positive deltaI -->  rook moving up,     negative deltaI -->  rook moving down
        // positive deltaJ -->  rook moving right,  negative deltaJ -->  rook moving left
        int deltaI;
        int deltaJ;
        if(di == 0) {
            deltaI = 0;
            deltaJ = dj > 0 ? -1 : 1;
        }
        else {
            deltaI = di > 0 ? -1 : 1;
            deltaJ = 0;
        }

        // checking the spots between start and end one by one to see if any have a piece
        int currentI = start.getI() + deltaI;
        int currentJ = start.getJ() + deltaJ;
        while(!(currentI == end.getI() && currentJ == end.getJ())) {
            if(board.getSpot(currentI, currentJ).getPiece() != null) {
                return false;
            }
            currentI += deltaI;
            currentJ += deltaJ;
        }

        // move is legal if player's king is not attacked after making it
        return !this.isKingAttackedAfterMove(board, start, end);
    }
}
