package com.jacob.engine.board;

import com.jacob.engine.pieces.*;

public class Board {
    Spot[][] spots;

    public Board() {
        spots = new Spot[8][8];
        setBoardToStartingPosition();
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
        spots[0][0] = new Spot(0, 0, new Rook(true));
        spots[0][1] = new Spot(0, 1, new Knight(true));
        spots[0][2] = new Spot(0, 2, new Bishop(true));
        spots[0][3] = new Spot(0, 3, new Queen(true));
        spots[0][4] = new Spot(0, 4, new King(true));
        spots[0][5] = new Spot(0, 5, new Bishop(true));
        spots[0][6] = new Spot(0, 6, new Knight(true));
        spots[0][7] = new Spot(0, 7, new Rook(true));
        // initialize white pawns
        for (int j = 0; j < 8; j++) {
            spots[1][j] = new Spot(1, j, new Pawn(true));
        }

        // initialize black pieces
        spots[7][0] = new Spot(7, 0, new Rook(false));
        spots[7][1] = new Spot(7, 1, new Knight(false));
        spots[7][2] = new Spot(7, 2, new Bishop(false));
        spots[7][3] = new Spot(7, 3, new Queen(false));
        spots[7][4] = new Spot(7, 4, new King(false));
        spots[7][5] = new Spot(7, 5, new Bishop(false));
        spots[7][6] = new Spot(7, 6, new Knight(false));
        spots[7][7] = new Spot(7, 7, new Rook(false));
        // initialize black pawns
        for (int j = 0; j < 8; j++) {
            spots[6][j] = new Spot(6, j, new Pawn(false));
        }

        // initialize remaining spots without any piece
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                spots[i][j] = new Spot(i, j, null);
            }
        }
    }

    public void displayBoard() {
        for(int i = 7; i >= 0; i--) {
            for(int j = 0; j < 8; j++) {
                Piece piece = spots[i][j].getPiece();

                if(piece == null)
                    System.out.print(". ");
                else {
                    if(piece.isWhite())
                        System.out.print(piece.getSymbol().toUpperCase() + " ");
                    else
                        System.out.print(piece.getSymbol() + " ");
                }
            }
            System.out.println();
        }
        System.out.println("---------------");
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
