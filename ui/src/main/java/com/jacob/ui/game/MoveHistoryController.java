
package com.jacob.ui.game;

import com.jacob.engine.board.Move;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MoveHistoryController implements Initializable {
    @FXML private TableView<DisplayMoves> displayMovesTable;
    @FXML private TableColumn<DisplayMoves, String> moveIdColumn;
    @FXML private TableColumn<DisplayMoves, String> whiteMoveColumn;
    @FXML private TableColumn<DisplayMoves, String> blackMoveColumn;
    private final ObservableList<DisplayMoves> list = FXCollections.observableArrayList();
    private Move whiteMove;

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
            whiteMove = move;
            return;
        }

        list.add(new DisplayMoves(list.size() + 1, whiteMove.toString(), move.toString()));
        whiteMove = null;
    }
    public void showLeftOverMove(){
        if (whiteMove == null) return;

        list.add(new DisplayMoves(list.size() + 1, whiteMove.toString(), ""));
    }

    private record DisplayMoves(int moveIndex, String whiteMove, String blackMove) {
        public String getMoveIndex() {
            return moveIndex + "";
        }
    }
}
