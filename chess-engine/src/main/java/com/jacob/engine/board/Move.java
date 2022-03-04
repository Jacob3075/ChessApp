package com.jacob.engine.board;

import com.jacob.engine.pieces.Piece;
import com.jacob.engine.player.Player;
import org.jetbrains.annotations.NotNull;

public class Move {
    private final Player player;
    private final Spot start;
    private final Spot end;
    private final Piece pieceMoved;
    private final Piece pieceKilled;
    private boolean castlingMove;

    public Move(Player player, @NotNull Spot start, @NotNull Spot end) {
        this.player = player;
        this.start = start;
        this.end = end;
        this.pieceMoved = start.getPiece();
        this.pieceKilled = end.getPiece();
    }

    public Spot getStart() {
        return start;
    }

    public Spot getEnd() {
        return end;
    }

    public boolean isCastlingMove() {
        return castlingMove;
    }

    public void setCastlingMove(boolean castlingMove) {
        this.castlingMove = castlingMove;
    }
}
