package com.jacob.engine.board;

import com.jacob.engine.pieces.Piece;
import com.jacob.engine.player.Player;

import java.util.function.IntSupplier;

public class Move {
    private final Player player;
    private final Spot start;
    private final Spot end;
    private final Piece pieceMoved;
    private Piece pieceCaptured;
    private boolean kingSideCastlingMove;
    private boolean queenSideCastlingMove;
    private final IntSupplier getPromotionChoice;
    private int promotionChoice = -1;

    public Move(Player player, Spot start, Spot end, IntSupplier getPromotionChoice) {
        this.player = player;
        this.start = start;
        this.end = end;
        this.pieceMoved = start.getPiece();
        this.pieceCaptured = end.getPiece();
        this.getPromotionChoice = getPromotionChoice;
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

    public void setPieceCaptured(Piece pieceCaptured) {
        this.pieceCaptured = pieceCaptured;
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

    public int getPromotionChoiceValue() {
        return promotionChoice;
    }

    public void getPromotionChoice() {
        promotionChoice = getPromotionChoice.getAsInt();
    }

    @Override
    public String toString() {
        // following algebraic notation
        if (isKingSideCastlingMove()) return "O-O";
        else if (isQueenSideCastlingMove()) return "O-O-O";

        // ascii value of a is 97
        char startingColumn = (char) (start.getJ() + 1 + 96);
        int startingRow = start.getI() + 1;
        char endingColumn = (char) (end.getJ() + 1 + 96);
        int endingRow = end.getI() + 1;

        String result = pieceMoved == null ? "" : pieceMoved.getSymbol();
        result += "" + startingColumn + startingRow;
        result += pieceCaptured == null ? "" : "x";
        result += "" + endingColumn + endingRow;
        return result;
    }
}
