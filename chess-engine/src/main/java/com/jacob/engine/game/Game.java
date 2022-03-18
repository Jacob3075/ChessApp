package com.jacob.engine.game;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Move;
import com.jacob.engine.board.Spot;
import com.jacob.engine.pieces.*;
import com.jacob.engine.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private final Player[] players;
    private final Board board;
    private Player currentTurn;
    private GameStatus status;
    private final List<Move> movesPlayed;
    private final List<Piece> piecesCapturedByPlayerZero;
    private final List<Piece> piecesCapturedByPlayerOne;

    public Game(Player p1, Player p2) {
        // initializing the players
        this.players = new Player[2];
        players[0] = p1;
        players[1] = p2;

        // creating the board and other required fields
        this.board = new Board();
        this.movesPlayed = new ArrayList<>();
        this.piecesCapturedByPlayerZero = new ArrayList<>();
        this.piecesCapturedByPlayerOne = new ArrayList<>();

        // assigning the current turn
        if(p1.isWhiteSide()) {
            this.currentTurn = p1;
        }
        else {
            this.currentTurn = p2;
        }
        
        // setting the game status as ACTIVE
        this.status = GameStatus.ACTIVE;

        // main game loop
        Scanner in = new Scanner(System.in);
        while(!this.isEnd()) {
            if(currentTurn == players[0]) {
                System.out.println("Player Zero's Move: ");
            }
            else {
                System.out.println("Player One's Move: ");
            }

            boolean isLegal = playerMove(currentTurn, in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt());
            while(!isLegal) {
                System.out.println("Illegal move. Enter a different move: ");
                isLegal = playerMove(currentTurn, in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt());
            }
            
            board.displayBoard();
            System.out.println(this.getEvaluation());

            // checking if the game is still active
            // generating all possible moves for the current player
            List<Move> possibleMoves = currentTurn.generateMoves(board);

            // if the current player cannot make a move
            if(possibleMoves.isEmpty()) {
                if(currentTurn == players[0]) {
                    System.out.println("Player 1 wins!");
                }
                else {
                    System.out.println("Player 0 Wins!");
                }

                if(currentTurn.isWhiteSide()) {
                    this.setStatus(GameStatus.BLACK_WIN);
                }
                else {
                    this.setStatus(GameStatus.WHITE_WIN);
                }
            }
        }
    }

    private boolean isEnd() {
        return this.getStatus() != GameStatus.ACTIVE;
    }

    public GameStatus getStatus() {
        return this.status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public List<Piece> getPiecesCapturedByPlayerZero() {
        return this.piecesCapturedByPlayerZero;
    }

    public List<Piece> getPiecesCapturedByPlayerOne() {
        return this.piecesCapturedByPlayerOne;
    }
    
    private int getEvaluation() {
        int evaluation = 0;
        
        // iterating through all the spots and updating the rating based on the pieces on the board
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                // getting the piece on the current spot
                Piece currentPiece = board.getSpot(i, j).getPiece();

                // if the piece is white, increase the evaluation by the piece's value
                if(currentPiece != null && currentPiece.isWhite()) {
                    evaluation += currentPiece.getValue();
                }

                // if the piece is black, decrease the evaluation by the piece's value
                else if(currentPiece != null && !currentPiece.isWhite()) {
                    evaluation -= currentPiece.getValue();
                }
            }
        }
        
        return evaluation;
    }

    private boolean playerMove(Player player, int startJ, int startI, int endJ, int endI) {
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
                // king side castle
                if(start.getJ() < end.getJ()) {
                    rookSpot = board.getSpot(start.getI(), start.getJ()+3);
                    rook = rookSpot.getPiece();
                    board.getSpot(start.getI(), start.getJ()+1).setPiece(rook);

                    move.setKingSideCastlingMove(true);
                    ((King) movedPiece).setKingSideCastlingDone(true);
                }
                // queen side castle
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
            // updating the pawn to record that it has been moved
            ((Pawn) movedPiece).setMoved(true);

            // en passant
            if(((Pawn) movedPiece).isEnPassantPossible()) {
                ((Pawn) movedPiece).setEnPassantPossible(false);
                capturedPiece = board.getSpot(start.getI(), end.getJ()).getPiece();
                board.getSpot(start.getI(), end.getJ()).setPiece(null);
            }

            // pawn promotion
            if(((Pawn) movedPiece).isPromotionPossible()) {
                ((Pawn) movedPiece).setPromotionPossible(false);

                // finding what piece to promote the pawn to
                Scanner in = new Scanner(System.in);
                System.out.println("Enter your choice:\n1. Queen\n2. Rook\n3. Bishop\n4. Knight");
                int choice = in.nextInt();

                // promoting the pawn
                switch (choice) {
                    case 1 -> movedPiece = new Queen(movedPiece.isWhite());
                    case 2 -> movedPiece = new Rook(movedPiece.isWhite());
                    case 3 -> movedPiece = new Bishop(movedPiece.isWhite());
                    case 4 -> movedPiece = new Knight(movedPiece.isWhite());
                }
            }
        }

        // store the captured piece
        if(capturedPiece != null) {
            if(currentTurn == players[0]) {
                piecesCapturedByPlayerZero.add(capturedPiece);
            }
            else {
                piecesCapturedByPlayerOne.add(capturedPiece);
            }
        }

        // moving the piece from start spot to end spot
        end.setPiece(movedPiece);
        start.setPiece(null);

        // checking if a pawn moved 2 spots in the previous move
        if(movesPlayed.size() > 0) {
            Piece pieceMovedOnPreviousTurn = movesPlayed.get(movesPlayed.size()-1).getPieceMoved();
            if(pieceMovedOnPreviousTurn instanceof Pawn) {
                ((Pawn) pieceMovedOnPreviousTurn).setMovedTwoSpotsOnPreviousTurn(false);
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
