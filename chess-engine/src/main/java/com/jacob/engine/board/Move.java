package com.jacob.engine.board;

import com.jacob.engine.pieces.Piece;
import com.jacob.engine.player.Player;

public class Move {
    private Player player;
    private Spot start;
    private Spot end;
    private Piece pieceMoved;
    private Piece pieceKilled;
    private boolean castlingMove;

    public Move(Player player, Spot start, Spot end) {
        this.player = player;
        this.start = start;
        this.end = end;
        this.pieceMoved = start.getPiece();
        this.pieceKilled = end.getPiece();
    }

    public Spot getStart() {
        return this.start;
    }

    public Spot getEnd() {
        return this.start;
    }

    public boolean isCastlingMove() {
        return this.castlingMove;
    }

    public void setCastlingMove(boolean castlingMove) {
        this.castlingMove = castlingMove;
    }
}