package com.jacob.ui.game;

import com.jacob.engine.pieces.Piece;
import com.jacob.ui.utils.PieceUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class Tile extends Label {
    private static final Paint WHITE = Paint.valueOf("#202020");
    private static final Paint BLACK = Paint.valueOf("#FFF1D9");
    private final Paint color;
    private final int index;
    @Nullable private Piece piece;

    Tile(int i, int j, BiConsumer<MouseEvent, Tile> onClicked) {
        index = i * 8 + j;
        piece = PieceUtils.DEFAULT_PIECE_POSITIONS.get(new Pair<>(i, j));
        boolean isWhiteCell = (index + (i % 2 == 0 ? 0 : 1)) % 2 == 0;
        color = isWhiteCell ? WHITE : BLACK;

        BackgroundFill tileBackground = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);

        setPrefSize(100, 100);
        setAlignment(Pos.CENTER);
        setBackground(new Background(tileBackground));
        setOnMouseClicked(mouseEvent -> onClicked.accept(mouseEvent, this));

        updateTileImage();
    }

    private void updateTileImage() {
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
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);
        setGraphic(imageView);
    }

    public @Nullable Piece getPiece() {
        return piece;
    }

    public void setPiece(@Nullable Piece piece) {
        this.piece = piece;
        updateTileImage();
    }

    public int getIndex() {
        return index;
    }

    public Paint getColor() {
        return color;
    }
}
