package com.jacob.ui.game.tile;

import com.jacob.engine.board.Spot;
import com.jacob.engine.game.Game;
import com.jacob.engine.pieces.Piece;
import com.jacob.ui.game.Position;
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
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class Tile extends Label {
    private static final Paint SELECTED = Paint.valueOf("#CBCF74");
    protected Paint color;
    @Nullable
    protected Piece piece;
    protected final Position position;
    protected final Consumer<Tile> onClicked;

    protected Tile(Position position, Consumer<Tile> onClicked) {
        this.onClicked = onClicked;
        this.position = position;

        setPrefSize(75, 75);
        setAlignment(Pos.CENTER);
        setOnMouseClicked(this::tileClicked);

        updateTileImage();
    }


    private void tileClicked(MouseEvent mouseEvent) {
        onClicked.accept(this);
    }

    public void tileClicked() {
        color = SELECTED;
        updateTileImage();
    }

    protected void updateTileImage() {

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

    public abstract void setPiece(@Nullable Piece piece);

    public Position getPosition() {
        return position;
    }

    public Spot convertToSpot(Game game) {
        return game.getSpot(position.row(), position.column());
    }
}


