package com.jacob.ui.game;

public class DisplayMoves {
    private final int moves;
    private final String moveByW;
    private final String moveByB;

    public DisplayMoves(int moves, String moveByW, String moveByB) {
        this.moves = moves;
        this.moveByW = moveByW;
        this.moveByB = moveByB;
    }

    public int getMoves() {
        return moves;
    }

    public String getMoveByW() {
        return moveByW;
    }

    public String getMoveByB() {
        return moveByB;
    }
}
