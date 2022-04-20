package com.jacob.ui.game;

import com.jacob.engine.board.Board;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.function.Consumer;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BoardController {
    @FXML private GridPane boardGrid;
    private Board board;

    public void initializeBoard(Board board, Consumer<Tile> onTileClicked) {
        this.board = board;

        ArrayList<Tile> rowCells = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            rowCells.clear();
            for (int j = 0; j < 8; j++) {
                rowCells.add(new Tile(i, j, onTileClicked));
            }
            boardGrid.addRow(7 - i, rowCells.toArray(new Tile[8]));
        }
        updateBoard();
    }

    public void updateBoard() {
        boardGrid.getChildren().stream()
                .mapMulti(this::getAllTilesInGameBoard)
                .forEach(this::updateTilesWithPieces);
    }

    private void getAllTilesInGameBoard(Node node, Consumer<Tile> stream) {
        try {
            stream.accept(((Tile) node));
        } catch (Exception exception) {
        }
    }

    private void updateTilesWithPieces(Tile tile) {
        tile.setPiece(
                board.getSpot(tile.getPosition().row(), tile.getPosition().column()).getPiece());
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
