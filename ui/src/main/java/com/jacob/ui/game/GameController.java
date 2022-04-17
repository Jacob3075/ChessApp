package com.jacob.ui.game;

import com.jacob.engine.board.Move;
import com.jacob.engine.game.Game;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class GameController implements Initializable {
    @FXML private BoardController boardController;
    @FXML private VBox sideBar;
    private final ApplicationContext context;
    private final Logger logger = LoggerFactory.getLogger(GameController.class);
    private final Game game = Game.createNewGame(true);
    private final MoveBuilder moveBuilder = new MoveBuilder();

    public GameController(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardController.setOnTileClicked(this::tileClicked);
        boardController.setBoard(game.getBoard());
        boardController.initializeBoard();
        initializeNextTurn();
    }

    private void tileClicked(Tile tile) {
        if (!game.getCurrentTurn().isHumanPlayer()) {
            showGameMessages("Not players turn");
            return;
        }

        moveBuilder.addTile(tile);
        Move move = moveBuilder.buildMoveOrNull(game, () -> 1);

        if (move == null) return;

        playMove(move);
        initializeNextTurn();
        moveBuilder.reset();
    }

    private void playMove(Move move) {
        if (!game.isMovePossible(move, game.getCurrentTurn())) {
            showGameMessages("Move is not possible, " + move);
            boardController.updateBoard();
            return;
        }

        game.makeMove(move);
        boardController.updateBoard();
    }

    private void initializeNextTurn() {
        List<Move> possibleMoves = game.getCurrentTurn().generatePossibleMoves(game.getBoard());

        if (possibleMoves.isEmpty()) {
            gameCompleted();
            return;
        }
        if (!game.getCurrentTurn().isHumanPlayer()) {
            computerMove(possibleMoves);
        }
    }

    private void computerMove(List<Move> possibleMoves) {
        game.makeComputerMove(possibleMoves);
        boardController.updateBoard();
        initializeNextTurn();
    }

    private void gameCompleted() {
        if (game.getCurrentTurn().isKingUnderAttack(game.getBoard())) {
            game.setAndDeclareWin();
        } else game.setAndDeclareDraw();
        showGameMessages("Game Over: " + game.getStatus());
    }

    private void showGameMessages(String message) {
        logger.info("message = {}", message);
    }
}
