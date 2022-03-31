package com.jacob.engine.game;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Move;
import com.jacob.engine.board.Spot;
import com.jacob.engine.pieces.*;
import com.jacob.engine.player.HumanPlayer;
import com.jacob.engine.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
        this.players = new Player[2];
        players[0] = p1;
        players[1] = p2;

        this.board = new Board();
        this.movesPlayed = new ArrayList<>();
        this.piecesCapturedByPlayerZero = new ArrayList<>();
        this.piecesCapturedByPlayerOne = new ArrayList<>();

        // white plays first
        if(p1.isWhiteSide())
            this.currentTurn = p1;
        else
            this.currentTurn = p2;

        // main game loop
        this.status = GameStatus.ACTIVE;
        while(!isEnd()) {
            board.displayBoard();
            initiateNextTurn();
        }
    }

    private boolean isEnd() {
        return this.getStatus() != GameStatus.ACTIVE;
    }
    
    private void initiateNextTurn() {
        List<Move> possibleMoves = currentTurn.generateMoves(board);

        if(possibleMoves.isEmpty()) {
            if(isCurrentTurnsKingUnderAttack())
                setAndDeclareWin();
            else
                setAndDeclareDraw();
        }
        else {
            if(currentTurn == players[0])
                System.out.println("Player Zero's Move: ");
            else
                System.out.println("Player One's Move: ");

            if(currentTurn instanceof HumanPlayer)
                makeHumanMove();
            else
                makeComputerMove(possibleMoves);
        }
    }

    private boolean isCurrentTurnsKingUnderAttack() {
        Spot currentTurnsKingSpot = getCurrentTurnsKingSpot();
        Piece currentTurnsKing = currentTurnsKingSpot.getPiece();
        return currentTurnsKing.isKingAttackedAfterMove(board, currentTurnsKingSpot, currentTurnsKingSpot);
    }

    private Spot getCurrentTurnsKingSpot() {
        for(int row = 0; row < board.getSIZE(); row++) {
            for(int column = 0; column < board.getSIZE(); column++) {
                Piece pieceOnSpot = board.getSpot(row, column).getPiece();

                if(pieceOnSpot instanceof King && pieceOnSpot.isWhite() == currentTurn.isWhiteSide())
                    return board.getSpot(row, column);
            }
        }

        // this line will never be reached. just returning a random spot on the board.
        return board.getSpot(0, 0);
    }

    private void setAndDeclareWin() {
        if(currentTurn == players[0])
            System.out.println("Player 1 wins!");
        else
            System.out.println("Player 0 Wins!");

        if(currentTurn.isWhiteSide())
            this.setStatus(GameStatus.BLACK_WIN);
        else
            this.setStatus(GameStatus.WHITE_WIN);
    }

    private void setAndDeclareDraw() {
        System.out.println("It's a draw.");
        this.setStatus(GameStatus.DRAW);
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    private void makeHumanMove() {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter your move: ");

        // subtracting 1 from the input since the game uses 0-based indexing and the input is expected to use 1-based
        int[] moveCoordinates = new int[4]; // startCol, startRow, endCol, endRow
        for(int i = 0; i < 4; i++)
            moveCoordinates[i] = in.nextInt()-1;

        boolean isMoveLegal = playerMove(currentTurn, moveCoordinates[0], moveCoordinates[1], moveCoordinates[2], moveCoordinates[3]);
        while(!isMoveLegal) {
            System.out.println("Illegal move. Enter a different move: ");
            for(int i = 0; i < 4; i++)
                moveCoordinates[i] = in.nextInt()-1;
            isMoveLegal = playerMove(currentTurn, moveCoordinates[0], moveCoordinates[1], moveCoordinates[2], moveCoordinates[3]);
        }
    }

    private void makeComputerMove(List<Move> possibleMoves) {
        Random random = new Random();
        int randomIndex = random.nextInt(possibleMoves.size());

        Move computerMove = possibleMoves.get(randomIndex);
        Spot startSpot = computerMove.getStart();
        Spot endSpot = computerMove.getEnd();

        playerMove(currentTurn, startSpot.getJ(), startSpot.getI(), endSpot.getJ(), endSpot.getI());
    }

    private boolean playerMove(Player player, int startJ, int startI, int endJ, int endI) {
        Spot startSpot = board.getSpot(startI, startJ);
        Spot endSpot = board.getSpot(endI, endJ);
        Move move = new Move(player, startSpot, endSpot);
        return this.makeMove(move, player);
    }

    public GameStatus getStatus() {
        return this.status;
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
        for(int row = 0; row < 8; row++) {
            for(int column = 0; column < 8; column++) {
                Piece currentPiece = board.getSpot(row, column).getPiece();

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

    private boolean makeMove(Move move, Player player) {
        Piece movedPiece = move.getPieceMoved();
        Piece capturedPiece = move.getPieceCaptured();

        if(movedPiece == null)
            return false;

        // checks if it's the player's turn
        if(player != currentTurn)
            return false;

        // checks if the player is moving their piece or opponents piece
        if(movedPiece.isWhite() != player.isWhiteSide())
            return false;

        // checks if the move is valid
        if(!movedPiece.canMove(board, move.getStart(), move.getEnd()))
            return false;

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
            if(currentTurn == players[0])
                piecesCapturedByPlayerZero.add(capturedPiece);
            else
                piecesCapturedByPlayerOne.add(capturedPiece);
        }

        // moving the piece from start spot to end spot
        end.setPiece(movedPiece);
        start.setPiece(null);

        // checking if a pawn moved 2 spots in the previous move
        if(movesPlayed.size() > 0) {
            Piece pieceMovedOnPreviousTurn = movesPlayed.get(movesPlayed.size()-1).getPieceMoved();
            if(pieceMovedOnPreviousTurn instanceof Pawn)
                ((Pawn) pieceMovedOnPreviousTurn).setMovedTwoSpotsOnPreviousTurn(false);
        }

        // store the move
        movesPlayed.add(move);

        // set the current turn to the other player
        if(this.currentTurn == players[0])
            this.currentTurn = players[1];
        else
            this.currentTurn = players[0];

        return true;
    }
}
