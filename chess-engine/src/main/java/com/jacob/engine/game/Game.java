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
    private final Player[] players;
    private final Board board;
    private final List<Move> movesPlayed;
    private Player currentTurn;
    private GameStatus status;

    public Game(Player p1, Player p2) {
        this.players = new Player[2];
        players[0] = p1;
        players[1] = p2;

        // top left of the board is x = 0, y = 0
        // x increases towards the right, y increases towards the bottom
        this.board = new Board();

        this.movesPlayed = new ArrayList<>();

        if (p1.isWhiteSide()) {
            this.currentTurn = p1;
        } else {
            this.currentTurn = p2;
        }

        this.status = GameStatus.ACTIVE;

        // main game loop
        //        while(this.status == GameStatus.ACTIVE) {
        //
        //        }
    }

    public void displayBoard() {
        for (int y = 7; y >= 0; y--) {
            for (int x = 0; x < 8; x++) {
                Spot spot = board.getBox(y, x);
                Piece piece = spot.getPiece();

                if (piece == null) {
                    System.out.print(". ");
                } else {
                    if (piece.isWhite()) {
                        System.out.print(piece.getSymbol().toUpperCase() + " ");
                    } else {
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
        if (sourcePiece == null) {
            return false;
        }

        // valid player
        if (player != currentTurn) {
            return false;
        }

        if (sourcePiece.isWhite() != player.isWhiteSide()) {
            return false;
        }

        // valid move?
        if (!sourcePiece.canMove(board, move.getStart(), move.getEnd())) {
            return false;
        }

        // kill?
        Piece destPiece = move.getStart().getPiece();
        if (destPiece != null) {
            //            destPiece.setKilled(true);
            //            move.setPieceKilled(destPiece);
        }

        // castling?
        if (sourcePiece instanceof King && move.isCastlingMove()) {
            move.setCastlingMove(true);
        }

        // store the move
        movesPlayed.add(move);

        // move piece from the start box to end box
        move.getEnd().setPiece(move.getStart().getPiece());
        move.getStart().setPiece(null);

        if (destPiece instanceof King) {
            if (player.isWhiteSide()) {
                this.setStatus(GameStatus.WHITE_WIN);
            } else {
                this.setStatus(GameStatus.BLACK_WIN);
            }
        }

        // set the current turn to the other player
        if (this.currentTurn == players[0]) {
            this.currentTurn = players[1];
        } else {
            this.currentTurn = players[0];
        }

        return true;
    }
}
