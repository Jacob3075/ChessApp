package com.jacob.engine.board;

import com.jacob.engine.pieces.*;

public class Board {
    Spot[][] spots;

    public Board() {
        spots = new Spot[8][8];
        setBoardToStartingPosition();
    }

    public Spot[][] getSpots() {
        return spots;
    }

    public Spot getSpot(int i, int j) {
        if(i < 0 || i > 7 || j < 0 || j > 7)
            return null;

        return spots[i][j];
    }

    public int getSize() {
        return 8;
    }

    public void setBoardToStartingPosition() {
        // initialize white pieces
        spots[0][0] = new Spot(0, 0, PieceFactory.getPiece("white rook"));
        spots[0][1] = new Spot(0, 1, PieceFactory.getPiece("white knight"));
        spots[0][2] = new Spot(0, 2, PieceFactory.getPiece("white bishop"));
        spots[0][3] = new Spot(0, 3, PieceFactory.getPiece("white queen"));
        spots[0][4] = new Spot(0, 4, PieceFactory.getPiece("white king"));
        spots[0][5] = new Spot(0, 5, PieceFactory.getPiece("white bishop"));
        spots[0][6] = new Spot(0, 6, PieceFactory.getPiece("white knight"));
        spots[0][7] = new Spot(0, 7, PieceFactory.getPiece("white rook"));
        // initialize white pawns
        for (int column = 0; column < 8; column++)
            spots[1][column] = new Spot(1, column, PieceFactory.getPiece("white pawn"));

        // initialize black pieces
        spots[7][0] = new Spot(7, 0, PieceFactory.getPiece("black rook"));
        spots[7][1] = new Spot(7, 1, PieceFactory.getPiece("black knight"));
        spots[7][2] = new Spot(7, 2, PieceFactory.getPiece("black bishop"));
        spots[7][3] = new Spot(7, 3, PieceFactory.getPiece("black queen"));
        spots[7][4] = new Spot(7, 4, PieceFactory.getPiece("black king"));
        spots[7][5] = new Spot(7, 5, PieceFactory.getPiece("black bishop"));
        spots[7][6] = new Spot(7, 6, PieceFactory.getPiece("black knight"));
        spots[7][7] = new Spot(7, 7, PieceFactory.getPiece("black rook"));
        // initialize black pawns
        for (int column = 0; column < 8; column++)
            spots[6][column] = new Spot(6, column, PieceFactory.getPiece("black pawn"));

        // initialize remaining spots without any piece
        for (int row = 2; row < 6; row++) {
            for (int column = 0; column < 8; column++) {
                spots[row][column] = new Spot(row, column, PieceFactory.getPiece(null));
            }
        }
    }

    public void displayBoard(boolean isPlayerWhite) {
        if(isPlayerWhite)
            displayBoardInWhitesPerspective();
        else
            displayBoardInBlacksPerspective();
        System.out.println("---------------");
    }

    private void displayBoardInWhitesPerspective() {
        for(int row = getSize()-1; row >= 0; row--) {
            for(int column = 0; column < getSize(); column++) {
                Piece piece = spots[row][column].getPiece();

                if(piece == null)
                    System.out.print(". ");
                else
                    System.out.print(piece.getSymbol() + " ");
            }
            System.out.println();
        }
    }

    private void displayBoardInBlacksPerspective() {
        for(int row = 0; row < getSize(); row++) {
            for(int column = getSize()-1; column >= 0; column--) {
                Piece piece = spots[row][column].getPiece();

                if(piece == null)
                    System.out.print(". ");
                else
                    System.out.print(piece.getSymbol() + " ");
            }
            System.out.println();
        }
    }

    public int getEvaluation() {
        /*
           by convention, white pieces increase the evaluation by the corresponding piece's value,
           and black pieces decrease the evaluation. In the end, if the evaluation is positive it
           implies that white has a better position and if its negative then black has a better
           position
         */

        int evaluation = 0;

        for(int row = 0; row < 8; row++) {
            for(int column = 0; column < 8; column++) {
                Piece currentPiece = getSpot(row, column).getPiece();

                if(currentPiece != null) {
                    if(currentPiece.isWhite())
                        evaluation += currentPiece.getValue();
                    else
                        evaluation -= currentPiece.getValue();
                }
            }
        }

        return evaluation;
    }
}
