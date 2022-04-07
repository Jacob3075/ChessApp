package com.jacob.ui.utils;

import com.jacob.engine.pieces.*;
import javafx.util.Pair;

import java.util.Map;

public class PieceUtils {
    private PieceUtils() {}

    // TODO: FILL IN THE REST OF THE PIECE PATHS
    public static final Map<Piece, String> PIECE_IMAGES =
            Map.ofEntries(
                    Map.entry(new Rook(false), "/images/pieces/rook_black.png"),
                    Map.entry(new Rook(true), "/images/pieces/rook_white.png"),
                    Map.entry(new Pawn(false), "/images/pieces/pawn_white.png")
                    );

    public static final Map<Pair<Integer, Integer>, Piece> DEFAULT_PIECE_POSITIONS =
            Map.ofEntries(
                    Map.entry(new Pair<>(0, 0), new Rook(true)),
                    Map.entry(new Pair<>(7, 7), new Rook(false)));
}
