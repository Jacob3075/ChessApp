package com.jacob.ui.game;

import com.jacob.engine.board.Spot;
import com.jacob.engine.game.Game;
import com.jacob.engine.pieces.Piece;
import com.jacob.ui.utils.PieceUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class Tile extends Label {
    private static final Paint WHITE = Paint.valueOf("#F0D9B5");
    private static final Paint BLACK = Paint.valueOf("#B58863");
    private static final Paint SELECTED = Paint.valueOf("#CBCF74");
    private Paint color;
    @Nullable private Piece piece;
    private final Position position;
    private final Consumer<Tile> onClicked;

    Tile(int row, int column, Consumer<Tile> onClicked) {
        this.onClicked = onClicked;
        this.position = new Position(row, column);
        this.color = position.isWhiteCell() ? WHITE : BLACK;

        setPrefSize(75, 75);
        setAlignment(Pos.CENTER);
        setOnMouseClicked(mouseEvent -> tileClicked());

        updateTileImage();
    }

    private void tileClicked() {
        this.color = SELECTED;
        this.onClicked.accept(this);
        updateTileImage();
    }

    private void updateTileImage() {
        BackgroundFill tileBackground = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
        setBackground(new Background(tileBackground));

        if (piece == null) {
            setGraphic(null);
            return;
        }

        String pieceResourcePath = PieceUtils.PIECE_IMAGES.get(piece);

        if (pieceResourcePath == null || pieceResourcePath.isBlank()) {
            setGraphic(null);
            return;
        }

        Image image = new Image(getClass().getResource(pieceResourcePath).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(75);
        imageView.setFitWidth(75);
        imageView.setPreserveRatio(true);
        setGraphic(imageView);
    }

    public void setPiece(@Nullable Piece piece) {
        this.piece = piece;
        this.color = position.isWhiteCell() ? WHITE : BLACK;
        updateTileImage();
    }

    public void resetColor() {
        this.color = position.isWhiteCell() ? WHITE : BLACK;
        updateTileImage();
    }

    public Position getPosition() {
        return position;
    }

    public Spot convertToSpot(Game game) {
        return game.getSpot(position.row(), position.column());
    }
}
