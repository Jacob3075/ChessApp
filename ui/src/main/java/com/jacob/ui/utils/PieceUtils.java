package com.jacob.ui.utils;

import com.jacob.engine.pieces.*;

import java.util.Map;

public class PieceUtils {
    private PieceUtils() {}

    public static final Map<Piece, String> PIECE_IMAGES =
            Map.ofEntries(
                    Map.entry(new King(true), "/images/pieces/king_white.png"),
                    Map.entry(new King(false), "/images/pieces/king_black.png"),
                    Map.entry(new Queen(true), "/images/pieces/queen_white.png"),
                    Map.entry(new Queen(false), "/images/pieces/queen_black.png"),
                    Map.entry(new Bishop(true), "/images/pieces/bishop_white.png"),
                    Map.entry(new Bishop(false), "/images/pieces/bishop_black.png"),
                    Map.entry(new Knight(true), "/images/pieces/knight_white.png"),
                    Map.entry(new Knight(false), "/images/pieces/knight_black.png"),
                    Map.entry(new Rook(true), "/images/pieces/rook_white.png"),
                    Map.entry(new Rook(false), "/images/pieces/rook_black.png"),
                    Map.entry(new Pawn(true), "/images/pieces/pawn_white.png"),
                    Map.entry(new Pawn(false), "/images/pieces/pawn_black.png"));
}
