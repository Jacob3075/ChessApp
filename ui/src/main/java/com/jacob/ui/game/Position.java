package com.jacob.ui.game;

public record Position(int row, int column, int index) {
    public Position(int row, int column) {
        this(row, column, row * 8 + column);
    }
}
