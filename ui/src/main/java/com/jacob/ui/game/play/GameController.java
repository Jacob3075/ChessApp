package com.jacob.ui.game.play;

import com.jacob.engine.board.Move;
import com.jacob.engine.game.Game;
import com.jacob.engine.game.GameStatus;
import com.jacob.ui.game.play.move_builder.MoveBuilder;
import com.jacob.ui.game.tile.Tile;

import java.util.List;
import java.util.function.Consumer;

public class GameController {
    private final Game game;
    private final Runnable gameCompleted;
    private final Consumer<Move> updateUI;
    private MoveBuilder moveBuilderState;

    public GameController(
            Game game,
            MoveBuilder moveBuilderState,
            Runnable gameCompleted,
            Consumer<Move> updateUI) {
        this.game = game;
        this.gameCompleted = gameCompleted;
        this.updateUI = updateUI;
        this.moveBuilderState = moveBuilderState;
        initializeNextTurn();
    }

    public void tileClicked(Tile tile) {
        if (!game.getCurrentTurn().isHumanPlayer()) return;

        moveBuilderState = moveBuilderState.addTile(tile);
        Move move = moveBuilderState.getMove(game);

        if (move == null) return;

        playMove(move);
        moveBuilderState = moveBuilderState.reset();
    }

    public void playMove(Move move) {
        if (!game.isMovePossible(move, game.getCurrentTurn())) {
            updateUI.accept(null);
            return;
        }

        game.makeMove(move);
        updateUI.accept(move);
        initializeNextTurn();
    }

    private void initializeNextTurn() {
        if (game.getStatus() != GameStatus.ACTIVE) {
            checkGameWinner();
            return;
        }

        List<Move> possibleMoves = game.getCurrentTurn().generatePossibleMoves(game.getBoard());

        if (possibleMoves.isEmpty()) {
            checkGameWinner();
            return;
        }
        if (!game.getCurrentTurn().isHumanPlayer()) {
            computerMove(possibleMoves);
        }
    }

    private void computerMove(List<Move> possibleMoves) {
        Move computerMove = game.getComputerMove(possibleMoves);
        game.makeValidMove(computerMove);
        updateUI.accept(computerMove);
        initializeNextTurn();
    }

    private void checkGameWinner() {
        if (game.getCurrentTurn().isKingUnderAttack(game.getBoard())) game.setAndDeclareWin();
        else game.setAndDeclareDraw();

        gameCompleted.run();
    }
}
