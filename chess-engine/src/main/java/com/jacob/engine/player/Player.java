package com.jacob.engine.player;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Move;
import com.jacob.engine.board.Spot;
import com.jacob.engine.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    public boolean whiteSide;
    public boolean humanPlayer;

    public boolean isWhiteSide() {
        return this.whiteSide;
    }

    public boolean isHumanPlayer() {
        return this.humanPlayer;
    }

    // return the list of moves that the player can make
    public List<Move> generateMoves(Board board) {
        List<Move> possibleMoves = new ArrayList<>();

        for(int startRow = 0; startRow < board.getSIZE(); startRow++) {
            for(int startColumn = 0; startColumn < board.getSIZE(); startColumn++) {
                Piece currentPiece = board.getSpot(startRow, startColumn).getPiece();

                if(currentPiece != null && currentPiece.isWhite() == this.isWhiteSide()) {
                    Spot start = board.getSpot(startRow, startColumn);

                    for(int endRow = 0; endRow < board.getSIZE(); endRow++) {
                        for(int endColumn = 0; endColumn < board.getSIZE(); endColumn++) {
                            Spot end = board.getSpot(endRow, endColumn);

                            if(currentPiece.canMove(board, start, end))
                                possibleMoves.add(new Move(this, start, end));

                        }
                    }

                }

            }
        }

        return possibleMoves;
    }
}
