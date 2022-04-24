package com.jacob.ui.game;

import com.jacob.engine.board.Move;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class MoveHistoryController implements Initializable {
    @FXML private TableView<DisplayMoves> displayMovesTable;
    @FXML private TableColumn<DisplayMoves, String> moveIdColumn;
    @FXML private TableColumn<DisplayMoves, String> whiteMoveColumn;
    @FXML private TableColumn<DisplayMoves, String> blackMoveColumn;
    private final ObservableList<DisplayMoves> list = FXCollections.observableArrayList();
    private String whiteMove;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        moveIdColumn.setCellValueFactory(
                param -> new ReadOnlyStringWrapper(param.getValue().getMoveIndex()));
        whiteMoveColumn.setCellValueFactory(
                param -> new ReadOnlyStringWrapper(param.getValue().whiteMove));
        blackMoveColumn.setCellValueFactory(
                param -> new ReadOnlyStringWrapper(param.getValue().blackMove));

        displayMovesTable.setItems(list);
    }

    public void updatePlayedMoves(Move move) {
        if (whiteMove == null) {
            whiteMove = move.toString();
            return;
        }

        list.add(new DisplayMoves(list.size() + 1, whiteMove, move.toString()));
        whiteMove = null;
    }

    public void updatePlayedMoves(String move) {
        if (whiteMove == null) {
            whiteMove = move;
            return;
        }

        list.add(new DisplayMoves(list.size() + 1, whiteMove, move));
        whiteMove = null;
    }
    private record DisplayMoves(int moveIndex, String whiteMove, String blackMove) {
        public String getMoveIndex() {
            return moveIndex + "";
        }
    }
}
