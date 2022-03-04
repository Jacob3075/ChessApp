package com.jacob.engine.player;

public class HumanPlayer extends Player {
    public HumanPlayer(boolean whiteSide) {
        this.whiteSide = whiteSide;
        this.humanPlayer = true;
    }
}