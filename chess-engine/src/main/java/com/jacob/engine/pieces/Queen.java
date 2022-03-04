package pieces;

import board.Board;
import board.Spot;

public class Queen extends Piece {
    public Queen(boolean white) {
        super(white, "q");
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