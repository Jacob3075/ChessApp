package com.jacob.ui.game.tile;

import com.jacob.engine.pieces.Piece;
import com.jacob.ui.game.Position;
import javafx.scene.paint.Paint;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BlackTile extends Tile {
    private static final Paint BLACK = Paint.valueOf("#B58863");

    public BlackTile(Position position, Consumer<Tile> onClicked) {
        super(position, onClicked);
        this.color = BLACK;
    }

    public void setPiece(@Nullable Piece piece) {
        this.piece = piece;
        color = BLACK;
        updateTileImage();
    }

}
