package com.jacob.engine.game;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Move;
import com.jacob.engine.board.Spot;
import com.jacob.engine.pieces.King;
import com.jacob.engine.pieces.Pawn;
import com.jacob.engine.pieces.Piece;
import com.jacob.engine.pieces.Rook;
import com.jacob.engine.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Player[] players;
    private Board board;
    private Player currentTurn;
    private GameStatus status;
    private List<Move> movesPlayed;
    private List<Piece> piecesCapturedByPlayerZero;
    private List<Piece> piecesCapturedByPlayerOne;

    public Game(Player p1, Player p2) {
        this.players = new Player[2];
        players[0] = p1;
        players[1] = p2;

        this.board = new Board();

        this.movesPlayed = new ArrayList<>();
        this.piecesCapturedByPlayerZero = new ArrayList<>();
        this.piecesCapturedByPlayerOne = new ArrayList<>();

        if(p1.isWhiteSide()) {
            this.currentTurn = p1;
        }
        else {
            this.currentTurn = p2;
        }

        this.status = GameStatus.ACTIVE;

        board.displayBoard();
        playerMove(currentTurn, 1, 0, 3, 0);
        board.displayBoard();
        playerMove(currentTurn, 6, 1, 5, 1);
        board.displayBoard();
        playerMove(currentTurn, 3, 0, 4, 0);
        board.displayBoard();
        playerMove(currentTurn, 5, 1, 4, 0);
        board.displayBoard();

        System.out.println(getEvaluation());
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
    
    public int getEvaluation() {
        int evaluation = 0;
        int playerZeroPieceMultiplier = players[0].isWhiteSide() ? 1 : -1;
        int playerOnePieceMultiplier = players[1].isWhiteSide() ? 1 : -1;

        for(Piece piece : piecesCapturedByPlayerZero) {
            evaluation += piece.getValue()*playerZeroPieceMultiplier;
        }
        for(Piece piece : piecesCapturedByPlayerOne) {
            evaluation += piece.getValue()*playerOnePieceMultiplier;
        }
        
        return evaluation;
    }

    public boolean playerMove(Player player, int startI, int startJ, int endI, int endJ) {
        Spot startSpot = board.getSpot(startI, startJ);
        Spot endSpot = board.getSpot(endI, endJ);
        Move move = new Move(player, startSpot, endSpot);
        return this.makeMove(move, player);
    }

    private boolean makeMove(Move move, Player player) {
        Piece movedPiece = move.getPieceMoved();
        Piece capturedPiece = move.getPieceCaptured();

        if(movedPiece == null) {
            return false;
        }

        // checks if it's the player's turn
        if(player != currentTurn) {
            return false;
        }

        // checks if the player is moving their piece or opponents piece
        if(movedPiece.isWhite() != player.isWhiteSide()) {
            return false;
        }

        // checks if the move is valid
        if(!movedPiece.canMove(board, move.getStart(), move.getEnd())) {
            return false;
        }

        Spot start = move.getStart();
        Spot end = move.getEnd();

        // moving a king
        if(movedPiece instanceof King) {
            // castling
            if(((King) movedPiece).isCastlingPossible()) {
                // moving the castle side rook from its start spot to its end spot
                Spot rookSpot;
                Piece rook;
                if(start.getJ() < end.getJ()) {
                    rookSpot = board.getSpot(start.getI(), start.getJ()+3);
                    rook = rookSpot.getPiece();
                    board.getSpot(start.getI(), start.getJ()+1).setPiece(rook);

                    move.setKingSideCastlingMove(true);
                    ((King) movedPiece).setKingSideCastlingDone(true);
                }
                else {
                    rookSpot = board.getSpot(start.getI(), start.getJ()-4);
                    rook = rookSpot.getPiece();
                    board.getSpot(start.getI(), start.getJ()-1).setPiece(rook);

                    move.setQueenSideCastlingMove(true);
                    ((King) movedPiece).setQueenSideCastlingDone(true);
                }
                rookSpot.setPiece(null);
                ((Rook) rook).setMoved(true);
            }

            // updating the king to record that it has been moved
            ((King) movedPiece).setMoved(true);
        }
        // moving a pawn
        else if(movedPiece instanceof Pawn) {
            // pawn promotion
            if(((Pawn) movedPiece).isPromotionPossible()) {
                // TODO: implement pawn promotion logic

            }

            // updating the pawn to record that it has been moved
            ((Pawn) movedPiece).setMoved(true);
        }
        else {
            // get the killed piece
            if(capturedPiece instanceof King) {
                if(player.isWhiteSide()) {
                    this.setStatus(GameStatus.WHITE_WIN);
                }
                else {
                    this.setStatus(GameStatus.BLACK_WIN);
                }
            }
        }

        // moving the piece from start spot to end spot
        end.setPiece(movedPiece);
        start.setPiece(null);

        // store the captured piece
        if(capturedPiece != null) {
            if(currentTurn == players[0]) {
                piecesCapturedByPlayerZero.add(capturedPiece);
            }
            else {
                piecesCapturedByPlayerOne.add(capturedPiece);
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
