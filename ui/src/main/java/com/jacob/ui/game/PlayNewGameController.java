package com.jacob.ui.game;

import com.jacob.database.game_data.PastGame;
import com.jacob.engine.board.Move;
import com.jacob.engine.game.Game;
import com.jacob.engine.game.GameStatus;
import com.jacob.ui.auth.UserAuthState;
import com.jacob.ui.utils.DatabaseUtils;
import com.jacob.ui.utils.JavaFxUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML private BoardController boardController;
    @FXML private Label timerMinutes;
    @FXML private Label timerSeconds;
    @FXML private TableView<DisplayMoves> displayMovesTable;
    private final UserAuthState userAuthState;
    private GameTimer gameTimer;
    ObservableList<DisplayMoves> list =
            FXCollections.observableArrayList(new DisplayMoves(1, "whiteMove", "blackMove"));
    @FXML private TableColumn<DisplayMoves, String> blackMoveDisplay;
    private final ApplicationContext context;
    private final Resource pawnPromotionPopupFxml;
    private final Logger logger = LoggerFactory.getLogger(PlayNewGameController.class);
    private final Game game = Game.createNewGame(true);
    private final MoveBuilder moveBuilder = new MoveBuilder();
    @FXML private TableColumn<DisplayMoves, Integer> moveNumberDisplay;
    private final IntSupplier getPawnPromotionChoice = this::getPawnPromotionChoice;
    @FXML private TableColumn<DisplayMoves, String> whiteMoveDisplay;

    public PlayNewGameController(
            ApplicationContext context,
            @Value("classpath:/view/pawn_promotion_popup.fxml") Resource pawnPromotionPopupFxml,
            UserAuthState userAuthState) {
        this.context = context;
        this.pawnPromotionPopupFxml = pawnPromotionPopupFxml;
        this.userAuthState = userAuthState;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameTimer = new GameTimer(this::gameTimeOver, timerMinutes, timerSeconds);
        boardController.setOnTileClicked(this::tileClicked);
        boardController.setBoard(game.getBoard());
        boardController.initializeBoard();
        initializeNextTurn();
        gameTimer.startCountDown();
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

    private void showGameMessages(String message) {
        logger.info("message = {}", message);
    }

    private void checkGameWinner() {
        if (game.getCurrentTurn().isKingUnderAttack(game.getBoard())) game.setAndDeclareWin();
        else game.setAndDeclareDraw();

        gameCompleted();
    }

    private void gameCompleted() {
        //        new Alert(
        //                        Alert.AlertType.CONFIRMATION,
        //                        "Game Over, winner is: " + game.getStatus(),
        //                        ButtonType.OK)
        //                .showAndWait();

        // FIXME: TAKES TOO LONG TO SAVE THE GAME, COULD BE DONE IN BACKGROUND THREAD.
        PastGame pastGame = DatabaseUtils.createPastGame(game, userAuthState.getLoggedInUser());
        userAuthState.updateUserDetails(pastGame);
        showGameMessages("Game Over: " + game.getStatus());
    }

    private void gameTimeOver() {
        game.setAndDeclareWin();
        gameCompleted();
    }

    public void displayMoves() {
        moveNumberDisplay.setCellValueFactory(new PropertyValueFactory<>("moves"));
        whiteMoveDisplay.setCellValueFactory(new PropertyValueFactory<>("moveByW"));
        blackMoveDisplay.setCellValueFactory(new PropertyValueFactory<>("moveByB"));

        displayMovesTable.setItems(list);
    }
}
