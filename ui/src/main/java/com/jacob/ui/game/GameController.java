package com.jacob.ui.game;

import com.jacob.engine.board.Move;
import com.jacob.engine.board.Spot;
import com.jacob.engine.game.Game;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
    @FXML
    private Text timerMinutes;
    @FXML
    private Text timerSeconds;
    @FXML
    private TableView<DisplayMoves> displayMovesTable;
    @FXML
    private TableColumn<DisplayMoves, Integer> moveNumberDisplay;
    @FXML
    private TableColumn<DisplayMoves, String> whiteMoveDisplay;
    @FXML
    private TableColumn<DisplayMoves, String> blackMoveDisplay;
    @FXML
    private GridPane gameBoard;
    @FXML
    private VBox sideBar;
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
//        try {
//            startCountdown();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        initializeNextTurn();
        displayMoves();
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

    ObservableList<DisplayMoves> list = FXCollections.observableArrayList(
            new DisplayMoves(1, "movebyW", "movebyB")
    );

    private void displayMoves() {
        moveNumberDisplay.setCellValueFactory(new PropertyValueFactory<DisplayMoves, Integer>("Moves"));
        whiteMoveDisplay.setCellValueFactory(new PropertyValueFactory<DisplayMoves, String>("movebyW"));
        blackMoveDisplay.setCellValueFactory(new PropertyValueFactory<DisplayMoves, String>("movebyB"));

        displayMovesTable.setItems(list);
    }

    private void startCountdown() throws InterruptedException {
        Integer minutes = 9;
        Integer seconds = 59;
        timerMinutes.setText("10");
        timerSeconds.setText("00");
        Thread thrd = new Thread();

        for (minutes = 9; minutes >= 0; minutes--) {
            for (seconds = 59; seconds >= 0; seconds--) {
                timerMinutes.setText(String.valueOf(minutes));
                System.out.println(minutes + "+" + seconds);
                timerSeconds.setText(String.valueOf(seconds));
                thrd.sleep(1000);
            }
        }

    }



}



