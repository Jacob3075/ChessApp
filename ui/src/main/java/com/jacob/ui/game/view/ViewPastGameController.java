package com.jacob.ui.game.view;

import com.jacob.database.game_data.PastGame;
import com.jacob.engine.game.Game;
import com.jacob.ui.game.BoardController;
import com.jacob.ui.game.MoveHistoryController;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ViewPastGameController {
    @FXML private HBox root;
    @FXML private MoveHistoryController moveHistoryController;
    @FXML private BoardController boardController;
    private PastGame pastGame;
    private final Game game;

    public ViewPastGameController() {
        game = Game.createNewGame(true);
    }

    public void initializePage() {
        assert pastGame != null;

        boardController.initializeBoard(game.getBoard(), tile -> {});
    }

    public void setDate(PastGame data) {
        this.pastGame = data;
    }

    private void showNextMove() {
        boardController.setBoard(game.getBoard());
        boardController.updateBoard();
    }

    private void showPreviousMove() {
        boardController.setBoard(game.getBoard());
        boardController.updateBoard();
    }
}
