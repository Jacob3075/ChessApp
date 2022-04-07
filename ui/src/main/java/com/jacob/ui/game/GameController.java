package com.jacob.ui.game;

import com.jacob.engine.board.Move;
import com.jacob.engine.game.Game;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
public class GameController implements Initializable {
    @FXML private GridPane gameBoard;
    @FXML private VBox sideBar;
    private final ApplicationContext context;
    private final Logger logger = LoggerFactory.getLogger(GameController.class);
    private final MoveCoordinator moveCoordinator;

    public GameController(ApplicationContext context) {
        this.context = context;
        moveCoordinator =
                new MoveCoordinator(Game.createGame(), this::updateBoard, this::showGameMessages);
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
    }

    private void updateBoard(@NotNull Move move) {
        logger.debug("move= {}", move);
    }

    private void showGameMessages(String message) {
        logger.debug("message = {}", message);
    }
}
