package com.jacob.engine.game;

import com.jacob.engine.board.*;
import com.jacob.engine.pieces.*;
import com.jacob.engine.player.*;

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
        players = new Player[2];
        players[0] = p1;
        players[1] = p2;

        board = new Board();
        movesPlayed = new ArrayList<>();
        piecesCapturedByPlayerZero = new ArrayList<>();
        piecesCapturedByPlayerOne = new ArrayList<>();

        // white plays first
        if(p1.isWhiteSide())
            currentTurn = p1;
        else
            currentTurn = p2;

        // main game loop
        status = GameStatus.ACTIVE;
        while(!isEnd()) {
            board.displayBoard();
            initiateNextTurn();
        }

        for(Move move : movesPlayed)
            System.out.println(move);
    }

    private boolean isEnd() {
        return getStatus() != GameStatus.ACTIVE;
    }

    public GameStatus getStatus() {
        return status;
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
            if(currentTurn instanceof ComputerPlayer)
                makeComputerMove(possibleMoves);
            else {
                if(currentTurn == players[0])
                    System.out.print("Player Zero's Move: ");
                else
                    System.out.print("Player One's Move: ");

                makeHumanMove();
            }
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
            setStatus(GameStatus.BLACK_WIN);
        else
            setStatus(GameStatus.WHITE_WIN);
    }

    private void setAndDeclareDraw() {
        System.out.println("It's a draw.");
        setStatus(GameStatus.DRAW);
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    private void makeComputerMove(List<Move> possibleMoves) {
        Random random = new Random();
        int randomIndex = random.nextInt(possibleMoves.size());
        Move computerMove = possibleMoves.get(randomIndex);

        makeMove(computerMove);
    }

    private void makeHumanMove() {
        Spot[] moveSpots = getHumanMoveStartAndEndSpots();
        Move humanMove = new Move(currentTurn, moveSpots[0], moveSpots[1]);

        boolean isMoveLegal = isMovePossible(humanMove, currentTurn);
        while(!isMoveLegal) {
            System.out.println("Illegal move. Enter a different move: ");
            moveSpots = getHumanMoveStartAndEndSpots();
            humanMove = new Move(currentTurn, moveSpots[0], moveSpots[1]);
            isMoveLegal = isMovePossible(humanMove, currentTurn);
        }

        makeMove(humanMove);
    }

    private Spot[] getHumanMoveStartAndEndSpots() {
        Scanner in = new Scanner(System.in);

        // subtracting 1 from the input since the game uses 0-based indexing and the input is expected to use 1-based
        int[] moveCoordinates = new int[4]; // startCol, startRow, endCol, endRow
        for(int i = 0; i < 4; i++)
            moveCoordinates[i] = in.nextInt()-1;

        Spot start = board.getSpot(moveCoordinates[1], moveCoordinates[0]);
        Spot end = board.getSpot(moveCoordinates[3], moveCoordinates[2]);
        return new Spot[]{start, end};
    }

    private boolean isMovePossible(Move move, Player player) {
        Piece movedPiece = move.getPieceMoved();
        return player == currentTurn
                && movedPiece != null
                && movedPiece.isWhite() == player.isWhiteSide()
                && movedPiece.canMove(board, move.getStart(), move.getEnd());
    }

    private void makeMove(Move move) {
        Piece movedPiece = move.getPieceMoved();
        Piece capturedPiece = move.getPieceCaptured();
        Spot start = move.getStart();
        Spot end = move.getEnd();

        if(movedPiece instanceof King)
            makeKingMove(move);

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

            end.setPiece(movedPiece);
            start.setPiece(null);
        }
        else {
            start.setPiece(null);
            end.setPiece(movedPiece);
        }

        // store the captured piece
        if(capturedPiece != null) {
            if(currentTurn == players[0])
                piecesCapturedByPlayerZero.add(capturedPiece);
            else
                piecesCapturedByPlayerOne.add(capturedPiece);
        }

        // checking if a pawn moved 2 spots in the previous move
        if(!movesPlayed.isEmpty()) {
            Piece pieceMovedOnPreviousTurn = movesPlayed.get(movesPlayed.size()-1).getPieceMoved();
            if(pieceMovedOnPreviousTurn instanceof Pawn)
                ((Pawn) pieceMovedOnPreviousTurn).setMovedTwoSpotsOnPreviousTurn(false);
        }

        // store the move
        movesPlayed.add(move);

        // set the current turn to the other player
        if(currentTurn == players[0])
            currentTurn = players[1];
        else
            currentTurn = players[0];
    }

    private void makeKingMove(Move move) {
        King king = (King) move.getPieceMoved();
        Spot start = move.getStart();
        Spot end = move.getEnd();

        if(king.isCastlingPossible()) {
            if(start.getJ() < end.getJ())
                makeKingSideCastlingMove(move);
            else
                makeQueenSideCastlingMove(move);
        }
        else {
            start.setPiece(null);
            end.setPiece(king);
            king.setMoved(true);
        }
    }

    private void makeKingSideCastlingMove(Move move) {
        King king = (King) move.getPieceMoved();
        Spot start = move.getStart();
        Spot end = move.getEnd();

        Spot initialRookSpot = board.getSpot(start.getI(), start.getJ()+3);
        Spot finalRookSpot = board.getSpot(start.getI(), start.getJ()+1);

        Rook rook = (Rook) initialRookSpot.getPiece();
        initialRookSpot.setPiece(null);
        finalRookSpot.setPiece(rook);

        start.setPiece(null);
        end.setPiece(king);

        rook.setMoved(true);
        king.setMoved(true);
        king.setKingSideCastlingDone(true);
        move.setKingSideCastlingMove(true);
    }

    private void makeQueenSideCastlingMove(Move move) {
        King king = (King) move.getPieceMoved();
        Spot start = move.getStart();
        Spot end = move.getEnd();

        Spot initialRookSpot = board.getSpot(start.getI(), start.getJ()-4);
        Spot finalRookSpot = board.getSpot(start.getI(), start.getJ()-1);

        Rook rook = (Rook) initialRookSpot.getPiece();
        initialRookSpot.setPiece(null);
        finalRookSpot.setPiece(rook);

        start.setPiece(null);
        end.setPiece(king);

        rook.setMoved(true);
        king.setMoved(true);
        king.setQueenSideCastlingDone(true);
        move.setQueenSideCastlingMove(true);
    }

    public List<Piece> getPiecesCapturedByPlayerZero() {
        return piecesCapturedByPlayerZero;
    }

    public List<Piece> getPiecesCapturedByPlayerOne() {
        return piecesCapturedByPlayerOne;
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

}
