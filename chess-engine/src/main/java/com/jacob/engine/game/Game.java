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

        // top left of the board is x = 0, y = 0
        // x increases towards the right, y increases towards the bottom
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
//        while(this.status == GameStatus.ACTIVE) {
//
//        }
    }

    public void displayBoard() {
        for(int y = 7; y >= 0; y--) {
            for(int x = 0; x < 8; x++) {
                Spot spot = board.getBox(y, x);
                Piece piece = spot.getPiece();

                if(piece == null) {
                    System.out.print(". ");
                }
                else {
                    if(piece.isWhite()) {
                        System.out.print(piece.getSymbol().toUpperCase() + " ");
                    }
                    else {
                        System.out.print(piece.getSymbol() + " ");
                    }
                }
            }
            System.out.println();
        }
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

    public boolean playerMove(Player player, int startX, int startY, int endX, int endY) {
        Spot startBox = board.getBox(startX, startY);
        Spot endBox = board.getBox(endX, endY);
        Move move = new Move(player, startBox, endBox);
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
            if(start.getX() < end.getX()) {
                rookSpot = board.getBox(start.getX()+3, start.getY());
                Piece rook = rookSpot.getPiece();
                board.getBox(start.getX()+1, start.getY()).setPiece(rook);
            }
            else {
                rookSpot = board.getBox(start.getX()-4, start.getY());
                Piece rook = rookSpot.getPiece();
                board.getBox(start.getX()-1, start.getY()).setPiece(rook);
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
