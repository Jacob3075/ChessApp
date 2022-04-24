package com.jacob.ui.game.view;

import com.jacob.database.game_data.PastGame;
import com.jacob.engine.game.Game;
import com.jacob.ui.game.BoardController;
import com.jacob.ui.game.MoveHistoryController;
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

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ViewPastGameController {
    @FXML private Button goBackButton;
    @FXML private HBox root;
    @FXML private MoveHistoryController moveHistoryController;
    @FXML private BoardController boardController;
    private final ApplicationContext context;
    private PastGame pastGame;
    private final Game game;

    public ViewPastGameController(ApplicationContext context) {
        this.context = context;
        game = Game.createNewGame(true);
    }

    public void initializePage() {
        assert pastGame != null;

        boardController.initializeBoard(game.getBoard(), tile -> {});
        goBackButton.setOnAction(this::showHomeScreen);
    }

    private void showHomeScreen(@NotNull ActionEvent event) {
        Stage stage = (Stage) goBackButton.getScene().getWindow();
        JavaFxUtils.changeScene(stage, JavaFxUtils.Views.SAVED_GAMES, context);
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
