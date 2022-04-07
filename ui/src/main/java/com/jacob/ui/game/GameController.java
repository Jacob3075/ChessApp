package com.jacob.ui.game;

import com.jacob.engine.pieces.Pawn;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
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

    public GameController(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<Tile> rowCells = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            rowCells.clear();
            for (int j = 0; j < 8; j++) {
                rowCells.add(new Tile(i, j, this::onTileClicked));
            }
            gameBoard.addRow(7 - i, rowCells.toArray(new Tile[8]));
        }
    }

    private void onTileClicked(MouseEvent mouseEvent, @NotNull Tile tile) {
        getBoardCell(tile.getIndex()).setPiece(new Pawn(false));
    }

    /**
     * @see <a href="https://stackoverflow.com/a/41348291/13181948">Stackoverflow Answer</a>
     */
    private Tile getBoardCell(int index) {
        return (Tile) gameBoard.getChildren().get(index + 1);
    }
}
