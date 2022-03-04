package com.jacob.engine.pieces;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Spot;

public abstract class Piece {
    private boolean white = false;
    private String symbol;

    protected Piece(boolean white, String symbol) {
        this.setWhite(white);
        this.setSymbol(symbol);
    }

    public boolean isWhite() {
        return this.white;
    }

    public void setWhite(boolean white) {
        this.white = white;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public abstract boolean canMove(Board board, Spot start, Spot end);
}
