package com.jacob.ui.game.tile;

import com.jacob.ui.game.Position;

import java.util.function.Consumer;

public class TileFactory {
    public static Tile createTile(int row, int column, Consumer<Tile> onClicked) {
        Position position = new Position(row, column);
        if (position.isWhiteCell()) {
            return new WhiteTile(position, onClicked);
        } else {
            return new BlackTile(position, onClicked);
        }
    }
}
