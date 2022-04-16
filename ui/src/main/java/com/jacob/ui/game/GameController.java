package com.jacob.ui.game;

import com.jacob.engine.board.Move;
import com.jacob.engine.board.Spot;
import com.jacob.engine.game.Game;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@Component
public class GameController implements Initializable {
    @FXML private GridPane gameBoard;
    @FXML private VBox sideBar;
    private final ApplicationContext context;
    private final Logger logger = LoggerFactory.getLogger(GameController.class);
    private final Game game = Game.createNewGame(true);
    private Tile startTile;
    private Tile endTile;
    private GameTimer ttimer;

    public GameController(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<Tile> rowCells = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            rowCells.clear();
            for (int j = 0; j < 8; j++) {
                rowCells.add(new Tile(i, j, this::tileClicked));
            }
            gameBoard.addRow(7 - i, rowCells.toArray(new Tile[8]));
        }
        updateBoard();
        initializeNextTurn();
    }

    private void tileClicked(Tile tile) {
        if (!game.getCurrentTurn().isHumanPlayer()) {
            showGameMessages("Not players turn");
            return;
        }

        if (startTile == null) {
            startTile = tile;
            return;
        }

        endTile = tile;
        playMove();
        start();
        startTile = null;
        endTile = null;
        initializeNextTurn();
    }

    private void updateBoard() {
        gameBoard.getChildren().stream()
                .mapMulti(this::getAllTilesInGameBoard)
                .forEach(this::updateTilesWithPieces);
    }

    private void getAllTilesInGameBoard(Node node, Consumer<Tile> stream) {
        try {
            stream.accept(((Tile) node));
        } catch (Exception ignored) {
        }
    }

    private void updateTilesWithPieces(Tile tile) {
        tile.setPiece(
                game.getBoard()
                        .getSpot(tile.getPosition().row(), tile.getPosition().column())
                        .getPiece());
    }

    private void playMove() {
        Spot startSpot = startTile.convertToSpot(game);
        Spot endSpot = endTile.convertToSpot(game);
        Move move = new Move(game.getCurrentTurn(), startSpot, endSpot, () -> 1);

        if (!game.isMovePossible(move, game.getCurrentTurn())) {
            showGameMessages("Move is not possible, " + move);
            startTile.resetColor();
            endTile.resetColor();
            return;
        }

        game.makeMove(move);
        updateBoard();
    }

    private void initializeNextTurn() {
        List<Move> possibleMoves = game.getCurrentTurn().generatePossibleMoves(game.getBoard());

        if (possibleMoves.isEmpty()) {
            gameCompleted();
            return;
        }
        if (!game.getCurrentTurn().isHumanPlayer()) {
            computerMove(possibleMoves);
        }
    }

    private void computerMove(List<Move> possibleMoves) {
        game.makeComputerMove(possibleMoves);
        updateBoard();
        initializeNextTurn();
    }

    private void gameCompleted() {
        if (game.getCurrentTurn().isKingUnderAttack(game.getBoard())) {
            game.setAndDeclareWin();
        } else game.setAndDeclareDraw();
        showGameMessages("Game Over: " + game.getStatus());
    }

    private void showGameMessages(String message) {
        logger.info("message = {}", message);
    }

    public void start()
    {
        try {
            Stage stage = new Stage();
            Label label = new Label("this is VBox");
            GameTimer ttimer = new GameTimer();
            sideBar.getChildren().add(label);
            sideBar.getChildren().add(ttimer.GameTimer());
            Scene scene = new Scene(sideBar, 100, 100);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

