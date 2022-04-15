package com.jacob.ui.game;

import com.jacob.engine.game.Game;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@Component
public class GameController implements Initializable {
    private final Game game;
    @FXML private GridPane gameBoard;
    @FXML private VBox sideBar;
    private final ApplicationContext context;
    private final Logger logger = LoggerFactory.getLogger(GameController.class);
    private final MoveCoordinator moveCoordinator;

    public GameController(ApplicationContext context) {
        this.context = context;
        game = Game.createNewGame(true);
        moveCoordinator =
                new MoveCoordinator(
                        game, this::showGameMessages, this::updateBoard, this::gameCompleted);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<Tile> rowCells = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            rowCells.clear();
            for (int j = 0; j < 8; j++) {
                rowCells.add(new Tile(i, j, moveCoordinator::tileClicked));
            }
            gameBoard.addRow(7 - i, rowCells.toArray(new Tile[8]));
        }
        updateBoard();
    }

    private void updateBoard() {
        gameBoard.getChildren().stream()
                .mapMulti(this::getAllTilesInGameBoard)
                .forEach(this::updateTilesWithPieces);
    }

    private void getAllTilesInGameBoard(Node node, Consumer<Tile> stream) {
        try {
            stream.accept(((Tile) node));
        } catch (Exception ignored) {
        }
    }

    private void updateTilesWithPieces(Tile tile) {
        tile.setPiece(
                game.getBoard()
                        .getSpot(tile.getPosition().row(), tile.getPosition().column())
                        .getPiece());
    }

    private void showGameMessages(String message) {
        logger.info("message = {}", message);
    }

    private void gameCompleted() {
        if (game.getCurrentTurn().isKingUnderAttack(game.getBoard())) {
            game.setAndDeclareWin();
        } else game.setAndDeclareDraw();
        showGameMessages("Game Over: " + game.getStatus());
    }
}
