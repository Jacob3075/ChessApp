package com.jacob.ui.game;

import com.jacob.engine.game.Game;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ViewPastGameController {
    @FXML private BoardController boardController;
    @FXML private VBox sideBar;
    private Integer data;
    private final Game game;

    public ViewPastGameController() {
        game = Game.createNewGame(true);
    }

    public void initializePage() {
        assert data != null;

        boardController.setBoard(game.getBoard());
        boardController.setOnTileClicked(tile -> {});
        boardController.initializeBoard();
    }

    public void setDate(int data) {
        this.data = data;
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
