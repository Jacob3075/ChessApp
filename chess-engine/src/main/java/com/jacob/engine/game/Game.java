package com.jacob.engine.game;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Move;
import com.jacob.engine.board.Spot;
import com.jacob.engine.pieces.King;
import com.jacob.engine.pieces.Piece;
import com.jacob.engine.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Player[] players;
    private Board board;
    private Player currentTurn;
    private GameStatus status;
    private List<Move> movesPlayed;

    public Game(Player p1, Player p2) {
        this.players = new Player[2];
        players[0] = p1;
        players[1] = p2;

        this.board = new Board();

        this.movesPlayed = new ArrayList<>();

        if(p1.isWhiteSide()) {
            this.currentTurn = p1;
        }
        else {
            this.currentTurn = p2;
        }

        this.status = GameStatus.ACTIVE;

        // main game loop
//        while(!this.isEnd()) {
//
//        }
    }

    public void displayBoard() {
        this.board.displayBoard();
    }

    public boolean isEnd() {
        return this.getStatus() != GameStatus.ACTIVE;
    }

    public GameStatus getStatus() {
        return this.status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public boolean playerMove(Player player, int startI, int startJ, int endI, int endJ) {
        Spot startSpot = board.getSpot(startI, startJ);
        Spot endSpot = board.getSpot(endI, endJ);
        Move move = new Move(player, startSpot, endSpot);
        return this.makeMove(move, player);
    }

    private boolean makeMove(Move move, Player player) {
        Piece sourcePiece = move.getStart().getPiece();

        if(sourcePiece == null) {
            return false;
        }

        // checks if it's the player's turn
        if(player != currentTurn) {
            return false;
        }

        // checks if the player is moving their piece or opponents piece
        if(sourcePiece.isWhite() != player.isWhiteSide()) {
            return false;
        }

        // checks if the move is valid
        if(!sourcePiece.canMove(board, move.getStart(), move.getEnd())) {
            return false;
        }

        // castling
        if(sourcePiece instanceof King && ((King) sourcePiece).isCastlingPossible()) {
            Spot start = move.getStart();
            Spot end = move.getEnd();

            // moving the castle side rook from its start spot to its end spot
            Spot rookSpot;
            if(start.getJ() < end.getJ()) {
                rookSpot = board.getSpot(start.getI(), start.getJ()+3);
                Piece rook = rookSpot.getPiece();
                board.getSpot(start.getI(), start.getJ()+1).setPiece(rook);
            }
            else {
                rookSpot = board.getSpot(start.getI(), start.getJ()-4);
                Piece rook = rookSpot.getPiece();
                board.getSpot(start.getI(), start.getJ()-1).setPiece(rook);
            }
            rookSpot.setPiece(null);

            // moving the king from the start spot to the end spot
            end.setPiece(sourcePiece);
            start.setPiece(null);

            move.setCastlingMove(true);
            ((King) sourcePiece).setCastlingDone(true);
            ((King) sourcePiece).setMoved(true);
        }
        else {
            // get the killed piece
            Piece destPiece = move.getEnd().getPiece();

            // move piece from the start spot to end spot
            move.getEnd().setPiece(move.getStart().getPiece());
            move.getStart().setPiece(null);

            if(destPiece instanceof King) {
                if(player.isWhiteSide()) {
                    this.setStatus(GameStatus.WHITE_WIN);
                }
                else {
                    this.setStatus(GameStatus.BLACK_WIN);
                }
            }
        }

        // store the move
        movesPlayed.add(move);

        // set the current turn to the other player
        if(this.currentTurn == players[0]) {
            this.currentTurn = players[1];
        }
        else {
            this.currentTurn = players[0];
        }

        return true;
    }
}
