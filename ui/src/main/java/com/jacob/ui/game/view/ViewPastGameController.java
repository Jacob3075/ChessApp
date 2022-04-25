package com.jacob.ui.game.view;

import com.jacob.database.game_data.PastGame;
import com.jacob.database.game_data.PlayedMove;
import com.jacob.engine.board.Move;
import com.jacob.engine.board.Spot;
import com.jacob.engine.game.Game;
import com.jacob.ui.game.BoardController;
import com.jacob.ui.game.MoveHistoryController;
import com.jacob.ui.game.Position;
import com.jacob.ui.utils.JavaFxUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ViewPastGameController {
    @FXML private Button nextMove;
    @FXML private Button goBackButton;
    @FXML private HBox root;
    @FXML private MoveHistoryController moveHistoryController;
    @FXML private BoardController boardController;
    private final ApplicationContext context;
    private PastGame pastGame;
    private final Game game;
    private Iterator<PlayedMove> playedMoveIterator;

    public ViewPastGameController(ApplicationContext context) {
        this.context = context;
        game = Game.createNewGame(true);
    }

    public void initializePage() {
        assert pastGame != null;

        goBackButton.setOnAction(this::showHomeScreen);
        nextMove.setOnAction(this::showNextMove);

        for (PlayedMove playedMove : pastGame.getPlayedMoves()) {
            Move move = convertPlayedMoveToMove(playedMove);
            game.makeMove(move);
        }

        game.getMovesPlayed().forEach(moveHistoryController::updatePlayedMoves);
        moveHistoryController.showLeftOverMove();
        game.resetBoard();
        boardController.initializeBoard(game.getBoard(), tile -> {});
        playedMoveIterator = pastGame.getPlayedMoves().iterator();
    }

    @NotNull
    private Move convertPlayedMoveToMove(PlayedMove playedMove) {
        return new Move(
                game.getCurrentTurn(),
                getSpot(new Position(playedMove.getStartIndex())),
                getSpot(new Position(playedMove.getEndIndex())),
                playedMove::getPawnPromotionChoice);
    }

    private Spot getSpot(Position position) {
        return game.getSpot(position.row(), position.column());
    }

    private void showHomeScreen(@NotNull ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        JavaFxUtils.changeScene(stage, JavaFxUtils.Views.SAVED_GAMES, context);
    }

    public void setDate(PastGame data) {
        this.pastGame = data;
    }

    private void showNextMove(ActionEvent actionEvent) {
        if (!playedMoveIterator.hasNext()) return;

        game.makeMove(convertPlayedMoveToMove(playedMoveIterator.next()));
        boardController.updateBoard();
    }
}
