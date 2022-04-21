package com.jacob.ui.game.play;

import com.jacob.database.game_data.PastGame;
import com.jacob.engine.board.Move;
import com.jacob.engine.game.Game;
import com.jacob.ui.auth.UserAuthState;
import com.jacob.ui.game.BoardController;
import com.jacob.ui.game.MoveHistory;
import com.jacob.ui.utils.DatabaseUtils;
import com.jacob.ui.utils.JavaFxUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PlayNewGameController implements Initializable {
    @FXML private BoardController boardController;
    @FXML private Label timerMinutes;
    @FXML private Label timerSeconds;
    @FXML private TableView<MoveHistory.DisplayMoves> displayMovesTable;
    private final UserAuthState userAuthState;
    private final ApplicationContext context;
    private final MoveHistory moveHistory = new MoveHistory();
    private final Game game = Game.createNewGame(true);
    private final GameController gameController =
            new GameController(
                    game,
                    new MoveBuilder(this::getPawnPromotionChoice),
                    this::gameCompleted,
                    this::updateUI);

    public PlayNewGameController(ApplicationContext context, UserAuthState userAuthState) {
        this.context = context;
        this.userAuthState = userAuthState;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new GameTimer(this::gameTimeOver, timerMinutes, timerSeconds).setTimer();
        moveHistory.initializeView(displayMovesTable);
        boardController.initializeBoard(game.getBoard(), gameController::tileClicked);
    }

    private int getPawnPromotionChoice() {
        System.out.println("PlayNewGameController.getPawnPromotionChoice");
        PawnPromotionPopupController controller = new PawnPromotionPopupController();
        JavaFxUtils.showPopupAndWait(JavaFxUtils.Views.PAWN_PROMOTION_POPUP, controller);
        return controller.getSelectedPiece();
    }

    private void gameCompleted() {
        new Alert(
                        Alert.AlertType.CONFIRMATION,
                        "Game Over, winner is: " + game.getStatus(),
                        ButtonType.OK)
                .showAndWait();

        PastGame pastGame = DatabaseUtils.createPastGame(game, userAuthState.getLoggedInUser());
        userAuthState.updateUserDetails(pastGame);

        Stage stage = (Stage) timerMinutes.getScene().getWindow();
        JavaFxUtils.changeScene(stage, JavaFxUtils.Views.HOME, context);
    }

    private void updateUI(@Nullable Move move) {
        boardController.updateBoard();

        if (move == null) return;

        moveHistory.updatePlayedMoves(move);
    }

    private void gameTimeOver() {
        game.setAndDeclareWin();
        gameCompleted();
    }
}
