package com.jacob.engine.board;

import com.jacob.engine.pieces.Piece;

public class Spot {
    private Piece piece;
    private int i;
    private int j;

    public Spot(int i, int j, Piece piece) {
        this.setPiece(piece);
        this.setI(i);
        this.setJ(j);
    }

    public Piece getPiece() {
        return this.piece;
    }

    public void setPiece(Piece p) {
        this.piece = p;
    }

    public int getI() {
        return this.i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return this.j;
    }

    public void setJ(int j) {
        this.j = j;
    }
}
