package com.jacob.engine.pieces;

public class PieceFactory {
    public static Piece getPiece(String pieceName) {
        if (pieceName == null)
            return null;
        else if (pieceName.equalsIgnoreCase("white pawn"))
            return new Pawn(true);
        else if (pieceName.equalsIgnoreCase("black pawn"))
            return new Pawn(false);
        else if (pieceName.equalsIgnoreCase("white rook"))
            return new Rook(true);
        else if (pieceName.equalsIgnoreCase("black rook"))
            return new Rook(false);
        else if (pieceName.equalsIgnoreCase("white knight"))
            return new Knight(true);
        else if (pieceName.equalsIgnoreCase("black knight"))
            return new Knight(false);
        else if (pieceName.equalsIgnoreCase("white bishop"))
            return new Bishop(true);
        else if (pieceName.equalsIgnoreCase("black bishop"))
            return new Bishop(false);
        else if (pieceName.equalsIgnoreCase("white queen"))
            return new Queen(true);
        else if (pieceName.equalsIgnoreCase("black queen"))
            return new Queen(false);
        else if (pieceName.equalsIgnoreCase("white king"))
            return new King(true);
        else if (pieceName.equalsIgnoreCase("black king"))
            return new King(false);
        else
            return null;
    }
}
