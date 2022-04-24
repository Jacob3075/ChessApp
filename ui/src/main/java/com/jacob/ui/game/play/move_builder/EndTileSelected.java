package com.jacob.ui.game.play.move_builder;

import com.jacob.engine.board.Move;
import com.jacob.engine.board.Spot;
import com.jacob.engine.game.Game;
import com.jacob.ui.game.tile.Tile;

import java.util.function.IntSupplier;

public class EndTileSelected implements MoveBuilder {
    private final Tile startTile;
    private final Tile endTile;
    private final IntSupplier getPawnPromotionChoice;

    public EndTileSelected(Tile startTile, Tile tile, IntSupplier getPawnPromotionChoice) {
        this.startTile = startTile;
        this.endTile = tile;
        this.getPawnPromotionChoice = getPawnPromotionChoice;
    }

    @Override
    public MoveBuilder addTile(Tile tile) {
        throw new UnsupportedOperationException(
                "Start and end tiles are already set, cannot add more");
    }

    @Override
    public Move getMove(Game game) {
        Spot startSpot = startTile.convertToSpot(game);
        Spot endSpot = endTile.convertToSpot(game);

        return new Move(game.getCurrentTurn(), startSpot, endSpot, getPawnPromotionChoice);
    }

    @Override
    public MoveBuilder reset() {
        return new NoTileSelected(getPawnPromotionChoice);
    }
}
