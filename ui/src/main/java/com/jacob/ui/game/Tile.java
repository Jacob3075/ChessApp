package com.jacob.ui.game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.util.Pair;

import java.util.Map;

public class Tile extends Label {
    private static final Paint WHITE = Paint.valueOf("#202020");
    private static final Paint BLACK = Paint.valueOf("#FFF1D9");
    // TODO: FILL IN THE REST OF THE PIECE PATHS
    private static final Map<Pair<Integer, Integer>, String> DEFAULT_PIECE_POSITIONS =
            Map.ofEntries(
                    Map.entry(new Pair<>(1, 1), "/images/pieces/bishop_black.png"),
                    Map.entry(new Pair<>(9, 1), ""),
                    Map.entry(new Pair<>(9, 2), ""),
                    Map.entry(new Pair<>(9, 3), ""),
                    Map.entry(new Pair<>(9, 4), ""));

    private final Paint color;
    private final int index;
    private Image pieceImage;

    Tile(int i, int j) {
        index = i * 8 + j;
        boolean isWhiteCell = (index + (i % 2 == 0 ? 1 : 0)) % 2 == 0;
        color = isWhiteCell ? WHITE : BLACK;
        BackgroundFill tileBackground = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
        String pieceResourcePath =
                DEFAULT_PIECE_POSITIONS.getOrDefault(
                        new Pair<>(i, j), "/images/pieces/bishop_black.png");

        setPrefSize(50, 50);
        setAlignment(Pos.CENTER);
        setBackground(new Background(tileBackground));

        Image image = new Image(getClass().getResource(pieceResourcePath).toExternalForm());
        setPieceImage(image);
    }

    public void setPieceImage(Image image) {
        this.pieceImage = image;

        ImageView value = new ImageView(image);
        value.setFitHeight(50);
        value.setFitWidth(50);
        value.setPreserveRatio(true);
        setGraphic(value);
    }
}
