package com.jacob.ui.game;

import com.jacob.engine.board.Move;
import com.jacob.engine.board.Spot;
import com.jacob.engine.game.Game;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class MoveCoordinator {
    private final Game game;
    private Tile startTile;
    private Tile endTile;
    private final Runnable updateBoard;
    private final Runnable gameCompleted;
    private final Consumer<String> showGameMessages;

    public MoveCoordinator(
            Game game,
            Consumer<String> showGameMessages,
            Runnable updateBoard,
            Runnable gameCompleted) {
        this.game = game;
        this.updateBoard = updateBoard;
        this.showGameMessages = showGameMessages;
        this.gameCompleted = gameCompleted;
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
        initializeNextTurn();
    }

    public void playMove() {
        Spot startSpot = convertTileToSpot(this.startTile);
        Spot endSpot = convertTileToSpot(this.endTile);
        Move move = new Move(game.getCurrentTurn(), startSpot, endSpot, () -> 1);

        if (!game.isMovePossible(move, game.getCurrentTurn())) {
            showGameMessages.accept("Move is not possible, " + move);
            return;
        }

        game.makeMove(move);
        updateBoard.run();
    }

    private void initializeNextTurn() {
        List<Move> possibleMoves = game.getCurrentTurn().generatePossibleMoves(game.getBoard());

        if (possibleMoves.isEmpty()) {
            gameCompleted.run();
            return;
        }
        if (!game.getCurrentTurn().isHumanPlayer()) {
            computerMove(possibleMoves);
        }
    }

    private void computerMove(List<Move> possibleMoves) {
        game.makeComputerMove(possibleMoves);
        updateBoard.run();
        initializeNextTurn();
    }

    private Spot convertTileToSpot(@NotNull Tile tile) {
        return game.getSpot(tile.getPosition().row(), tile.getPosition().column());
    }
}
