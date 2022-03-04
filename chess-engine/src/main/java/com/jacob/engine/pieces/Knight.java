package pieces;

import board.Board;
import board.Spot;

public class Knight extends Piece {
    public Knight(boolean white) {
        super(white, "n");
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        // we can't move the piece to a spot that has a piece of the same colour
        if(end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }

        int dx = Math.abs(start.getX() - end.getX());
        int dy = Math.abs(start.getY() - end.getY());

        // returns true if end spot is within bounds, and dx * dy == 2
        return ((0 <= end.getX())
                && (end.getX() < 8)
                && (0 <= end.getY())
                && (end.getY() < 8)
                && (dx * dy == 2)
        );
    }
}