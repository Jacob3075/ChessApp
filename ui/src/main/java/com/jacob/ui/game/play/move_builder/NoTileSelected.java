package com.jacob.ui.game.play.move_builder;

import com.jacob.engine.board.Move;
import com.jacob.engine.game.Game;
import com.jacob.ui.game.tile.Tile;

import java.util.function.IntSupplier;

public class NoTileSelected implements MoveBuilder {
    private final IntSupplier getPawnPromotionChoice;

    NoTileSelected(IntSupplier getPawnPromotionChoice) {
        this.getPawnPromotionChoice = getPawnPromotionChoice;
    }

    @Override
    public MoveBuilder addTile(Tile tile) {
        tile.tileClicked();
        return new StartTileSelected(tile, getPawnPromotionChoice);
    }

    @Override
    public Move getMove(Game game) {
        return null;
    }

    @Override
    public MoveBuilder reset() {
        return this;
    }
}
