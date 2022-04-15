package com.jacob.ui.game;

import com.jacob.engine.board.Move;
import com.jacob.engine.board.Spot;
import com.jacob.engine.game.Game;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class MoveCoordinator {
    private final Game game;
    private final Runnable onValidMove;
    private final Consumer<String> showGameMessages;
    private Tile startTile;
    private Tile endTile;

    public MoveCoordinator(Game game, Runnable onValidMove, Consumer<String> showGameMessages) {
        this.game = game;
        this.onValidMove = onValidMove;
        this.showGameMessages = showGameMessages;
    }

    public void tileClicked(Tile tile) {
        if (!game.getCurrentTurn().isHumanPlayer()) {
            showGameMessages.accept("Not players turn");
            return;
        }

        if (startTile == null) {
            startTile = tile;
            return;
        }

        endTile = tile;
        playMove();
        startTile = null;
        endTile = null;
    }

    public void playMove() {
        Spot startSpot = convertTileToSpot(this.startTile);
        Spot endSpot = convertTileToSpot(this.endTile);
        Move move = new Move(game.getCurrentTurn(), startSpot, endSpot, () -> 1);

        if (!game.isMovePossible(move, game.getCurrentTurn())) {
            showGameMessages.accept("Move is not possible");
            return;
        }

        game.makeMove(move);
        onValidMove.run();
    }

    private Spot convertTileToSpot(@NotNull Tile tile) {
        return game.getSpot(tile.getPosition().row(), tile.getPosition().column());
    }
}
