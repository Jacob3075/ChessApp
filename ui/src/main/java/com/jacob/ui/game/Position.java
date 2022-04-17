package com.jacob.ui.game;

public record Position(int row, int column, int index, boolean isWhiteCell) {
    public Position(int row, int column) {
        this(row, column, row * 8 + column);
    }

    private Position(int row, int column, int index) {
        this(row, column, row * 8 + column, (index + (row % 2 == 0 ? 0 : 1)) % 2 == 0);
    }
}