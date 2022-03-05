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

}
