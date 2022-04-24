package com.jacob.ui.game.play.move_builder;

import com.jacob.engine.board.Move;
import com.jacob.engine.game.Game;
import com.jacob.ui.game.tile.Tile;
import org.springframework.lang.Nullable;

import java.util.function.IntSupplier;

public class StartTileSelected implements MoveBuilder {
    private final Tile startTile;
    private final IntSupplier getPawnPromotionChoice;

    StartTileSelected(Tile startTile, IntSupplier getPawnPromotionChoice) {
        this.startTile = startTile;
        this.getPawnPromotionChoice = getPawnPromotionChoice;
    }

    @Override
    public MoveBuilder addTile(Tile tile) {
        tile.tileClicked();
        return new EndTileSelected(startTile, tile, getPawnPromotionChoice);
    }

    @Override
    @Nullable
    public Move getMove(Game game) {
        return null;
    }

    @Override
    public MoveBuilder reset() {
        return new NoTileSelected(getPawnPromotionChoice);
    }
}
