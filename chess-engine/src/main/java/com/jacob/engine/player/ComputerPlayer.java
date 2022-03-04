package com.jacob.engine.player;

public class ComputerPlayer extends Player {
    public ComputerPlayer(boolean whiteSide) {
        this.whiteSide = whiteSide;
        this.humanPlayer = false;
    }
}