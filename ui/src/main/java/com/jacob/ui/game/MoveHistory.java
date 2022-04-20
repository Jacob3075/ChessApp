package com.jacob.ui.game;

import com.jacob.engine.board.Move;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MoveHistory {
    private final ObservableList<DisplayMoves> list = FXCollections.observableArrayList();
    private Move whiteMove;

    public void initializeView(TableView<DisplayMoves> displayMovesTable) {
        ObservableList<TableColumn<DisplayMoves, ?>> columns = displayMovesTable.getColumns();
        columns.get(0).setCellValueFactory(new PropertyValueFactory<>("moveIndex"));
        columns.get(1).setCellValueFactory(new PropertyValueFactory<>("whiteMove"));
        columns.get(2).setCellValueFactory(new PropertyValueFactory<>("blackMove"));

        displayMovesTable.setItems(list);
    }

    public void updatePlayedMoves(Move move) {
        if (whiteMove == null) {
            whiteMove = move;
            return;
        }

        list.add(new DisplayMoves(list.size() + 1, whiteMove.toString(), move.toString()));
        whiteMove = null;
    }

    public static class DisplayMoves {
        private final int moveIndex;
        private final String whiteMove;
        private final String blackMove;

        public DisplayMoves(int moveIndex, String whiteMove, String blackMove) {
            this.moveIndex = moveIndex;
            this.whiteMove = whiteMove;
            this.blackMove = blackMove;
        }

        public int getMoveIndex() {
            return moveIndex;
        }

        public String getWhiteMove() {
            return whiteMove;
        }

        public String getBlackMove() {
            return blackMove;
        }
    }
}
