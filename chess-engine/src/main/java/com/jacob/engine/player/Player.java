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

        // going through all the spots to find a start spot
        for(int si = 0; si < 8; si++) {
            for(int sj = 0; sj < 8; sj++) {
                Piece currentPiece = board.getSpot(si, sj).getPiece();
                if(currentPiece != null && currentPiece.isWhite() == this.isWhiteSide()) {
                    Spot start = board.getSpot(si, sj);

                    // going through all the spots to find an end spot
                    for(int ei = 0; ei < 8; ei++) {
                        for(int ej = 0; ej < 8; ej++) {
                            Spot end = board.getSpot(ei, ej);

                            // if the piece can move from the start spot to the end spot
                            if(currentPiece.canMove(board, start, end)) {

                                // adding the move to the list
                                possibleMoves.add(new Move(this, start, end));
                            }
                        }
                    }
                }
            }
        }

        return possibleMoves;
    }
}
