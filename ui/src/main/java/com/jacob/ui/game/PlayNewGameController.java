package com.jacob.ui.game;

import com.jacob.database.game_data.PastGame;
import com.jacob.engine.board.Move;
import com.jacob.engine.game.Game;
import com.jacob.engine.game.GameStatus;
import com.jacob.ui.auth.UserAuthState;
import com.jacob.ui.utils.DatabaseUtils;
import com.jacob.ui.utils.JavaFxUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.IntSupplier;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PlayNewGameController implements Initializable {
    private final UserAuthState userAuthState;
    private final Timeline timeline = new Timeline();
    ObservableList<DisplayMoves> list =
            FXCollections.observableArrayList(new DisplayMoves(1, "movebyW", "movebyB"));
    @FXML private BoardController boardController;
    @FXML private Label timerMinutes;
    @FXML private Label timerSeconds;
    @FXML private TableView<DisplayMoves> displayMovesTable;

    private final ApplicationContext context;
    private final Resource pawnPromotionPopupFxml;
    private final Logger logger = LoggerFactory.getLogger(PlayNewGameController.class);
    private final Game game = Game.createNewGame(true);
    private final MoveBuilder moveBuilder = new MoveBuilder();
    private final IntSupplier getPawnPromotionChoice = this::getPawnPromotionChoice;
    @FXML private TableColumn<DisplayMoves, Integer> moveNumberDisplay;
    @FXML private TableColumn<DisplayMoves, String> whiteMoveDisplay;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardController.setOnTileClicked(this::tileClicked);
        boardController.setBoard(game.getBoard());
        boardController.initializeBoard();
        initializeNextTurn();
        startCountDown();
        displayMoves();
    }

    private void tileClicked(Tile tile) {
        if (!game.getCurrentTurn().isHumanPlayer()) {
            showGameMessages("Not players turn");
            return;
        }

        moveBuilder.addTile(tile);
        Move move = moveBuilder.buildMoveOrNull(game, getPawnPromotionChoice);

        if (move == null) return;

        playMove(move);
        initializeNextTurn();
        moveBuilder.reset();
    }

    private void playMove(Move move) {
        if (!game.isMovePossible(move, game.getCurrentTurn())) {
            showGameMessages("Move is not possible, " + move);
            boardController.updateBoard();
            return;
        }

        game.makeMove(move);
        boardController.updateBoard();
    }

    private void initializeNextTurn() {
        if (game.getStatus() != GameStatus.ACTIVE) {
            checkGameWinner();
            return;
        }

        List<Move> possibleMoves = game.getCurrentTurn().generatePossibleMoves(game.getBoard());

        if (possibleMoves.isEmpty()) {
            checkGameWinner();
            return;
        }
        if (!game.getCurrentTurn().isHumanPlayer()) {
            computerMove(possibleMoves);
        }
    }

    private void computerMove(List<Move> possibleMoves) {
        game.makeComputerMove(possibleMoves);
        boardController.updateBoard();
        initializeNextTurn();
    }

    private int getPawnPromotionChoice() {
        PawnPromotionPopupController controller = new PawnPromotionPopupController();
        JavaFxUtils.showPopupAndWait(pawnPromotionPopupFxml, controller);
        return controller.getSelectedPiece();
    }

    @FXML private TableColumn<DisplayMoves, String> blackMoveDisplay;

    public PlayNewGameController(
            ApplicationContext context,
            @Value("classpath:/view/pawn_promotion_popup.fxml") Resource pawnPromotionPopupFxml,
            UserAuthState userAuthState) {
        this.context = context;
        this.pawnPromotionPopupFxml = pawnPromotionPopupFxml;
        this.userAuthState = userAuthState;
    }

    private void showGameMessages(String message) {
        logger.info("message = {}", message);
    }

    private void checkGameWinner() {
        if (game.getCurrentTurn().isKingUnderAttack(game.getBoard())) game.setAndDeclareWin();
        else game.setAndDeclareDraw();

        gameCompleted();
    }

    private void gameCompleted() {
        showGameMessages("Game Over: " + game.getStatus());
        //        new Alert(Alert.AlertType.CONFIRMATION, "Game Over, winner is: " +
        // game.getStatus(), ButtonType.OK).showAndWait();

        PastGame pastGame = DatabaseUtils.createPastGame(game, userAuthState.getLoggedInUser());
        userAuthState.updateUserDetails(pastGame);
    }

    // Countdown timer

    private static final Integer STARTTIME = 9;
    private static final Integer STARTMIN = 0;

    private void displayMoves() {
        moveNumberDisplay.setCellValueFactory(
                new PropertyValueFactory<DisplayMoves, Integer>("Moves"));
        whiteMoveDisplay.setCellValueFactory(
                new PropertyValueFactory<DisplayMoves, String>("movebyW"));
        blackMoveDisplay.setCellValueFactory(
                new PropertyValueFactory<DisplayMoves, String>("movebyB"));

        displayMovesTable.setItems(list);
    }
    private Integer timeSeconds = STARTTIME;
    private Integer timeMinutes = STARTMIN;

    public void startCountDown() {
        if (!(timeMinutes < 0)) {
            Platform.runLater(() -> timerSeconds.setText(timeSeconds.toString()));
            if (timeMinutes < 10) {
                Platform.runLater(() -> timerMinutes.setText("0" + timeMinutes.toString()));
            } else {
                Platform.runLater(() -> timerMinutes.setText(timeMinutes.toString()));
            }
            KeyFrame keyframe =
                    new KeyFrame(
                            Duration.seconds(1),
                            new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    timeSeconds--;
                                    boolean isSecondsZero = timeSeconds == 0;
                                    boolean isMinutesZero = timeMinutes == 0;
                                    if (isSecondsZero) {
                                        timeSeconds--;
                                        timeSeconds = 60;
                                        timeMinutes--;
                                        System.out.println(timeMinutes + "+" + timeSeconds);
                                    }
                                    if (isMinutesZero && isSecondsZero) {
                                        timeline.stop();
                                        gameTimeOver();
                                        timeMinutes = 0;
                                        timeSeconds = 0;
                                    }
                                    if (timeSeconds < 10) {
                                        Platform.runLater(
                                                () ->
                                                        timerSeconds.setText(
                                                                "0" + timeSeconds.toString()));
                                    } else {
                                        Platform.runLater(
                                                () -> timerSeconds.setText(timeSeconds.toString()));
                                    }
                                    if (timeMinutes < 10) {
                                        Platform.runLater(
                                                () ->
                                                        timerMinutes.setText(
                                                                "0" + timeMinutes.toString()));
                                    } else {
                                        Platform.runLater(
                                                () -> timerMinutes.setText(timeMinutes.toString()));
                                    }
                                }
                            });
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.getKeyFrames().add(keyframe);
            timeline.playFromStart();
        }
    }

    private void gameTimeOver() {
        //        game.setAndDeclareWin();
        //        gameCompleted();
    }
}
