package com.jacob.ui.game.play;

import com.jacob.database.game_data.PastGame;
import com.jacob.engine.board.Move;
import com.jacob.engine.game.Game;
import com.jacob.engine.game.GameStatus;
import com.jacob.ui.auth.UserAuthState;
import com.jacob.ui.game.BoardController;
import com.jacob.ui.game.MoveHistory;
import com.jacob.ui.game.Tile;
import com.jacob.ui.utils.DatabaseUtils;
import com.jacob.ui.utils.JavaFxUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
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
    @FXML private TableView<MoveHistory.DisplayMoves> displayMovesTable;
    private final UserAuthState userAuthState;
    private MoveHistory moveHistory;
    private final ApplicationContext context;
    private final Resource pawnPromotionPopupFxml;
    private final Logger logger = LoggerFactory.getLogger(PlayNewGameController.class);
    private final Game game = Game.createNewGame(true);
    private final MoveBuilder moveBuilder = new MoveBuilder();
    private final IntSupplier getPawnPromotionChoice = this::getPawnPromotionChoice;

    public PlayNewGameController(
            ApplicationContext context,
            @Value("classpath:/view/game/play/pawn_promotion_popup.fxml")
                    Resource pawnPromotionPopupFxml,
            UserAuthState userAuthState) {
        this.context = context;
        this.pawnPromotionPopupFxml = pawnPromotionPopupFxml;
        this.userAuthState = userAuthState;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new GameTimer(this::gameTimeOver, timerMinutes, timerSeconds).startCountDown();
        moveHistory = new MoveHistory(displayMovesTable);
        moveHistory.initializeView();

        boardController.setOnTileClicked(this::tileClicked);
        boardController.setBoard(game.getBoard());
        boardController.initializeBoard();

        initializeNextTurn();
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
        moveHistory.updatePlayedMoves(move);
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
        Move computerMove = game.getComputerMove(possibleMoves);
        game.makeValidMove(computerMove);
        boardController.updateBoard();
        moveHistory.updatePlayedMoves(computerMove);
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
        showGameMessages("Game Over: " + game.getStatus());
        Alert a =
                new Alert(
                        Alert.AlertType.CONFIRMATION,
                        "Game Over, winner is: " + game.getStatus(),
                        ButtonType.OK);
        a.show();

        final Button btnFoo = (Button) a.getDialogPane().lookupButton(ButtonType.OK);
        btnFoo.setOnAction(
                event -> {
                    Stage stage = (Stage) (timerMinutes).getScene().getWindow();
                    JavaFxUtils.changeScene(stage, JavaFxUtils.Views.HOME, context);
                });

        PastGame pastGame = DatabaseUtils.createPastGame(game, userAuthState.getLoggedInUser());
        userAuthState.updateUserDetails(pastGame);
        showGameMessages("Game Over: " + game.getStatus());
    }

    private void gameTimeOver() {
        game.setAndDeclareWin();
        gameCompleted();
    }
}
