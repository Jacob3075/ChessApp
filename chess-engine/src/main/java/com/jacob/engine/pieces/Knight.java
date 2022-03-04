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
        // we can't move the piece to a spot that has a piece of the same colour
        if (end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }

        int di = Math.abs(start.getI() - end.getI());
        int dj = Math.abs(start.getJ() - end.getJ());

        // returns true if end spot is within bounds, and dx * dy == 2
        return ((0 <= end.getJ())
                && (end.getJ() < 8)
                && (0 <= end.getI())
                && (end.getI() < 8)
                && (di * dj == 2));
    }
}
