package com.jacob.ui.utils;

import com.jacob.engine.pieces.Bishop;
import com.jacob.engine.pieces.King;
import com.jacob.engine.pieces.Pawn;
import com.jacob.engine.pieces.Piece;
import javafx.util.Pair;

import java.util.Map;

public class PieceUtils {
    private PieceUtils() {}

    // TODO: FILL IN THE REST OF THE PIECE PATHS
    public static final Map<Piece, String> PIECE_IMAGES =
            Map.ofEntries(
                    Map.entry(new Bishop(false), "/images/pieces/bishop_black.png"),
                    Map.entry(new King(true), "/images/pieces/king_black.png"),
                    Map.entry(new Pawn(false), "/images/pieces/pawn_white.png")
                    );

    public static final Map<Pair<Integer, Integer>, Piece> DEFAULT_PIECE_POSITIONS =
            Map.ofEntries(
                    Map.entry(new Pair<>(1, 1), new Bishop(false)),
                    Map.entry(new Pair<>(2, 1), new Bishop(false)),
                    Map.entry(new Pair<>(3, 1), new Bishop(false)));
}
