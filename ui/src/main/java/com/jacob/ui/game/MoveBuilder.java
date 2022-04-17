package com.jacob.ui.game;

import com.jacob.engine.board.Move;
import com.jacob.engine.board.Spot;
import com.jacob.engine.game.Game;
import org.springframework.lang.Nullable;

import java.util.function.IntSupplier;

public class MoveBuilder {
    private Tile startTile;
    private Tile endTile;

    public void addTile(Tile tile) {
        tile.tileClicked();

        if (startTile == null) {
            startTile = tile;
            return;
        }

        endTile = tile;
    }

    public void reset() {
        startTile = null;
        endTile = null;
    }

    @Nullable
    public Move buildMoveOrNull(Game game, IntSupplier getPromotionChoice) {
        if (endTile == null) return null;

        Spot startSpot = startTile.convertToSpot(game);
        Spot endSpot = endTile.convertToSpot(game);

        return new Move(game.getCurrentTurn(), startSpot, endSpot, getPromotionChoice);
    }
}
