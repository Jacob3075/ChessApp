package pieces;

import board.Board;
import board.Spot;

public class Rook extends Piece {
    public Rook(boolean white) {
        super(white, "r");
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