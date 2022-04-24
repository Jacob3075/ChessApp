package com.jacob.ui.game.play.move_builder;

import com.jacob.engine.board.Move;
import com.jacob.engine.game.Game;
import com.jacob.ui.game.tile.Tile;
import org.springframework.lang.Nullable;

import java.util.function.IntSupplier;

public interface MoveBuilder {
    static MoveBuilder getStartState(IntSupplier getPawnPromotionChoice) {
        return new NoTileSelected(getPawnPromotionChoice);
    }

    MoveBuilder addTile(Tile tile);

    @Nullable
    Move getMove(Game game);

    MoveBuilder reset();
}
