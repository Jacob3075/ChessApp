package com.jacob.ui.game;

public class DisplayMoves {

    private int Moves;
    private String movebyW;
    private String movebyB;

    public DisplayMoves(int Moves, String movebyW, String movebyB) {
        this.Moves = Moves;
        this.movebyW = movebyW;
        this.movebyB = movebyB;

    }

    public int getMoves() {
        return Moves;
    }

    public String getMovebyW() {
        return movebyW;
    }

    public String getMovebyB() {
        return movebyB;
    }

}
