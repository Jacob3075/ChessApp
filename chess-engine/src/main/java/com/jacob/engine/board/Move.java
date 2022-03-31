package com.jacob.engine.board;

import com.jacob.engine.pieces.Piece;
import com.jacob.engine.player.Player;

public class Move {
    private final Player player;
    private final Spot start;
    private final Spot end;
    private final Piece pieceMoved;
    private final Piece pieceCaptured;
    private boolean kingSideCastlingMove;
    private boolean queenSideCastlingMove;

    public Move(Player player, Spot start, Spot end) {
        this.player = player;
        this.start = start;
        this.end = end;
        this.pieceMoved = start.getPiece();
        this.pieceCaptured = end.getPiece();
    }

    public Spot getStart() {
        return start;
    }

    public Spot getEnd() {
        return end;
    }

    public Piece getPieceMoved() {
        return pieceMoved;
    }

    public Piece getPieceCaptured() {
        return pieceCaptured;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isKingSideCastlingMove() {
        return kingSideCastlingMove;
    }

    public void setKingSideCastlingMove(boolean kingSideCastlingMove) {
        this.kingSideCastlingMove = kingSideCastlingMove;
    }

    public boolean isQueenSideCastlingMove() {
        return queenSideCastlingMove;
    }

    public void setQueenSideCastlingMove(boolean queenSideCastlingMove) {
        this.queenSideCastlingMove = queenSideCastlingMove;
    }

    @Override
    public String toString() {
        if(isKingSideCastlingMove())
            return "O-O";
        else if(isQueenSideCastlingMove())
            return "O-O-O";

        // ascii value of a is 97
        char startingColumn = (char) (start.getJ()+1+96);
        int startingRow = start.getI()+1;
        char endingColumn = (char) (end.getJ()+1+96);
        int endingRow = end.getI()+1;
        pieceMoved.getSymbol();

        String result = "" + pieceMoved.getSymbol() + startingColumn + startingRow;
        result += pieceCaptured != null ? "x" : "";
        result += endingColumn + endingRow;
        return result;
    }
}
