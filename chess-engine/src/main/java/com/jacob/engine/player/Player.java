package com.jacob.engine.player;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Move;
import com.jacob.engine.board.Spot;
import com.jacob.engine.pieces.King;
import com.jacob.engine.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    protected boolean whiteSide;
    protected boolean humanPlayer;

    public boolean isWhiteSide() {
        return this.whiteSide;
    }

    public boolean isHumanPlayer() {
        return this.humanPlayer;
    }

    // return the list of moves that the player can make
    public List<Move> generatePossibleMoves(Board board) {
        List<Move> possibleMoves = new ArrayList<>();

        for (int startRow = 0; startRow < board.getSize(); startRow++) {
            for (int startColumn = 0; startColumn < board.getSize(); startColumn++) {
                Piece currentPiece = board.getSpot(startRow, startColumn).getPiece();

                if (currentPiece != null && currentPiece.isWhite() == this.isWhiteSide()) {
                    Spot start = board.getSpot(startRow, startColumn);

                    for (int endRow = 0; endRow < board.getSize(); endRow++) {
                        for (int endColumn = 0; endColumn < board.getSize(); endColumn++) {
                            Spot end = board.getSpot(endRow, endColumn);

                            if (currentPiece.canMove(board, start, end))
                                possibleMoves.add(new Move(this, start, end, () -> 1));
                        }
                    }
                }
            }
        }

        return possibleMoves;
    }

    public boolean isKingUnderAttack(Board board) {
        Spot kingSpot = getKingSpot(board);
        Piece king = kingSpot.getPiece();
        return king.isKingAttackedAfterMove(board, kingSpot, kingSpot);
    }

    private Spot getKingSpot(Board board) {
        Spot kingSpot = null;

        for (int row = 0; row < board.getSize(); row++) {
            for (int column = 0; column < board.getSize(); column++) {
                Piece pieceOnSpot = board.getSpot(row, column).getPiece();

                if (pieceOnSpot instanceof King && pieceOnSpot.isWhite() == this.isWhiteSide())
                    kingSpot = board.getSpot(row, column);
            }
        }

        assert kingSpot != null;
        return kingSpot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        if (whiteSide != player.whiteSide) return false;
        return humanPlayer == player.humanPlayer;
    }

    @Override
    public int hashCode() {
        int result = (whiteSide ? 1 : 0);
        result = 31 * result + (humanPlayer ? 1 : 0);
        return result;
    }
}
