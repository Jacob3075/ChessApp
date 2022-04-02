package com.jacob.ui.game;

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

@Component
public class GameController implements Initializable {
    @FXML private GridPane gameBoard;
    @FXML private VBox sideBar;
    private final ApplicationContext context;
    private final Logger logger = LoggerFactory.getLogger(GameController.class);

    public GameController(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<Tile> rowCells = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            rowCells.clear();
            for (int j = 0; j < 8; j++) {
                rowCells.add(new Tile(i, j));
            }
            gameBoard.addRow(i, rowCells.toArray(new Tile[8]));
        }
    }

    /**
     * @see <a href="https://stackoverflow.com/a/41348291/13181948">Stackoverflow Answer</a>
     */
    private Node getBoardCell(int i, int j) {
        return gameBoard.getChildren().get(i * 8 + j);
    }
}
