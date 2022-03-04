package com.jacob.engine.pieces;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Spot;
import org.jetbrains.annotations.NotNull;

public class Knight extends Piece {
    public Knight(boolean white) {
        super(white, "n");
    }

    @Override
    public boolean canMove(Board board, Spot start, @NotNull Spot end) {
        Piece destPiece = end.getPiece();

        // we can't move the piece to a spot that has a piece of the same colour
        if(destPiece != null && destPiece.isWhite() == this.isWhite()) {
            return false;
        }

        int di = Math.abs(start.getI() - end.getI());
        int dj = Math.abs(start.getJ() - end.getJ());

        return (di * dj == 2);
    }
}
