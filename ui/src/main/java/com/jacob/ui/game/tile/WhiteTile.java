package com.jacob.ui.game.tile;

import com.jacob.engine.pieces.Piece;
import com.jacob.ui.game.Position;
import javafx.scene.paint.Paint;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class WhiteTile extends Tile {
    private static final Paint WHITE = Paint.valueOf("#F0D9B5");

    public WhiteTile(Position position, Consumer<Tile> onClicked) {
        super(position, onClicked);
        this.color = WHITE;
    }

    public void setPiece(@Nullable Piece piece) {
        this.piece = piece;
        color = WHITE;
        updateTileImage();
    }
}
