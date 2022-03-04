package com.jacob.engine.player;

public abstract class Player {
    public boolean whiteSide;
    public boolean humanPlayer;

    public boolean isWhiteSide() {
        return this.whiteSide;
    }

    public boolean isHumanPlayer() {
        return this.humanPlayer;
    }
}
