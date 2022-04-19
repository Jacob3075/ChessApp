package com.jacob.ui.game;

import com.jacob.engine.board.Move;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MoveHistory {
    private final TableView<DisplayMoves> displayMovesTable;
    private final ObservableList<DisplayMoves> list = FXCollections.observableArrayList();
    private Move whiteMove;

    public MoveHistory(TableView<DisplayMoves> displayMovesTable) {
        this.displayMovesTable = displayMovesTable;
    }

    public void initializeView() {
        ObservableList<TableColumn<DisplayMoves, ?>> columns = displayMovesTable.getColumns();
        columns.get(0).setCellValueFactory(new PropertyValueFactory<>("moves"));
        columns.get(1).setCellValueFactory(new PropertyValueFactory<>("moveByW"));
        columns.get(2).setCellValueFactory(new PropertyValueFactory<>("moveByB"));

        displayMovesTable.setItems(list);
    }

    public void updatePlayedMoves(Move move) {
        if (whiteMove == null) {
            whiteMove = move;
            return;
        }

        list.add(new DisplayMoves(list.size() + 1, whiteMove.toString(), move.toString()));
    }
}
