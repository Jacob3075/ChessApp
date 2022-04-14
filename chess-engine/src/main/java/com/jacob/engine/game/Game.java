package com.jacob.engine.game;

import com.jacob.engine.board.Board;
import com.jacob.engine.board.Move;
import com.jacob.engine.board.Spot;
import com.jacob.engine.pieces.*;
import com.jacob.engine.player.ComputerPlayer;
import com.jacob.engine.player.HumanPlayer;
import com.jacob.engine.player.Player;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private final Player[] players;
    public final Board board;
    private Player currentTurn;
    private GameStatus status;
    private final List<Move> movesPlayed;
    private final Random random;

    public Game(Player playerZero, Player playerOne) {
        random = new SecureRandom();
        players = new Player[2];
        players[0] = playerZero;
        players[1] = playerOne;

        board = new Board();
        movesPlayed = new ArrayList<>();

        // white plays first
        if(playerZero.isWhiteSide())
            currentTurn = playerZero;
        else
            currentTurn = playerOne;

        setStatus(GameStatus.ACTIVE);
    }

    public static Game createGame() {
        return new Game(new HumanPlayer(true), new ComputerPlayer(false));
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public boolean isEnd() {
        return getStatus() != GameStatus.ACTIVE;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void initiateNextTurn() {
        List<Move> possibleMoves = currentTurn.generateMoves(board);

        if(possibleMoves.isEmpty()) {
            if(isCurrentTurnsKingUnderAttack())
                setAndDeclareWin();
            else
                setAndDeclareDraw();
        }
        else {
            if(!currentTurn.isHumanPlayer())
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
        for(int row = 0; row < board.getSize(); row++) {
            for(int column = 0; column < board.getSize(); column++) {
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

    private void makeComputerMove(List<Move> possibleMoves) {
        int randomIndex = random.nextInt(possibleMoves.size());
        Move computerMove = possibleMoves.get(randomIndex);

        makeValidMove(computerMove);
    }

    private void makeHumanMove() {
        Spot[] moveSpots = getHumanMoveStartAndEndSpots();
        Move humanMove = new Move(currentTurn, moveSpots[0], moveSpots[1]);
        boolean isMoveLegal = isMovePossible(humanMove, currentTurn);

        while(!isMoveLegal) {
            System.out.print("Illegal move. Enter a different move: ");
            moveSpots = getHumanMoveStartAndEndSpots();
            humanMove = new Move(currentTurn, moveSpots[0], moveSpots[1]);
            isMoveLegal = isMovePossible(humanMove, currentTurn);
        }

        makeValidMove(humanMove);
    }

    private Spot[] getHumanMoveStartAndEndSpots() {
        Scanner in = new Scanner(System.in);

        // subtracting 1 from the input since the game uses 0-based indexing and the input is expected to use 1-based
        int[] moveCoordinates = new int[4]; // startCol, startRow, endCol, endRow
        for(int i = 0; i < 4; i++)
            moveCoordinates[i] = in.nextInt()-1;
        Spot start = board.getSpot(moveCoordinates[1], moveCoordinates[0]);
        Spot end = board.getSpot(moveCoordinates[3], moveCoordinates[2]);

        while(start == null || end == null) {
            System.out.print("Illegal move. Enter a different move: ");
            for(int i = 0; i < 4; i++)
                moveCoordinates[i] = in.nextInt()-1;
            start = board.getSpot(moveCoordinates[1], moveCoordinates[0]);
            end = board.getSpot(moveCoordinates[3], moveCoordinates[2]);
        }

        return new Spot[]{start, end};
    }

    public boolean isMovePossible(Move move, Player player) {
        Piece movedPiece = move.getPieceMoved();
        return player == currentTurn
                && movedPiece != null
                && movedPiece.isWhite() == player.isWhiteSide()
                && movedPiece.canMove(board, move.getStart(), move.getEnd());
    }

    private void makeValidMove(Move move) {
        Piece movedPiece = move.getPieceMoved();

        if(movedPiece instanceof Pawn)
            makePawnMove(move);
        else if(movedPiece instanceof King)
            makeKingMove(move);
        else {
            move.getStart().setPiece(null);
            move.getEnd().setPiece(movedPiece);
        }

        // if a pawn moved on the previous turn, we set its movedTwoSpotsOnPreviousTurn value to false.
        // we need to do this as this value is used to confirm whether an en passant move is possible
        if(!movesPlayed.isEmpty()) {
            Piece pieceMovedOnPreviousTurn = movesPlayed.get(movesPlayed.size()-1).getPieceMoved();
            if(pieceMovedOnPreviousTurn instanceof Pawn)
                ((Pawn) pieceMovedOnPreviousTurn).setMovedTwoSpotsOnPreviousTurn(false);
        }

        movesPlayed.add(move);

        passCurrentTurnToOtherPlayer();
    }

    private void makePawnMove(Move move) {
        Pawn pawn = (Pawn) move.getPieceMoved();
        Spot start = move.getStart();
        Spot end = move.getEnd();

        if(pawn.isEnPassantPossible())
            makeEnPassantMove(move);
        else if(pawn.isPromotionPossible())
            makePawnPromotionMove(move);
        else {
            start.setPiece(null);
            end.setPiece(pawn);
            pawn.setMoved(true);
        }
    }

    private void makeEnPassantMove(Move move) {
        Pawn pawn = (Pawn) move.getPieceMoved();
        Spot start = move.getStart();
        Spot end = move.getEnd();

        Spot capturedPieceSpot = board.getSpot(start.getI(), end.getJ());
        Piece capturedPiece = capturedPieceSpot.getPiece();

        move.setPieceCaptured(capturedPiece);
        capturedPieceSpot.setPiece(null);
        start.setPiece(null);
        end.setPiece(pawn);

        pawn.setEnPassantPossible(false);
        pawn.setMoved(true);
    }

    private void makePawnPromotionMove(Move move) {
        Pawn pawn = (Pawn) move.getPieceMoved();
        Spot start = move.getStart();
        Spot end = move.getEnd();

        Scanner in = new Scanner(System.in);
        System.out.println("Enter your choice:\n1. Queen\n2. Rook\n3. Bishop\n4. Knight");
        int choice = in.nextInt();

        start.setPiece(null);
        switch (choice) {
            case 1 -> end.setPiece(new Queen(pawn.isWhite()));
            case 2 -> end.setPiece(new Rook(pawn.isWhite()));
            case 3 -> end.setPiece(new Bishop(pawn.isWhite()));
            case 4 -> end.setPiece(new Knight(pawn.isWhite()));
        }

        pawn.setPromotionPossible(false);
    }

    private void makeKingMove(Move move) {
        King king = (King) move.getPieceMoved();
        Spot start = move.getStart();
        Spot end = move.getEnd();

        if(king.isTryingToCastle()) {
            if(start.getJ() < end.getJ()) // the king is moving to the right, so king side castling
                makeKingSideCastlingMove(move);
            else // the king is moving to the left, so queen side castling
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
        Rook rook = (Rook) initialRookSpot.getPiece();
        Spot finalRookSpot = board.getSpot(start.getI(), start.getJ()+1);

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
        Rook rook = (Rook) initialRookSpot.getPiece();
        Spot finalRookSpot = board.getSpot(start.getI(), start.getJ()-1);

        initialRookSpot.setPiece(null);
        finalRookSpot.setPiece(rook);
        start.setPiece(null);
        end.setPiece(king);

        rook.setMoved(true);
        king.setMoved(true);
        king.setQueenSideCastlingDone(true);
        move.setQueenSideCastlingMove(true);
    }

    private void passCurrentTurnToOtherPlayer() {
        if(currentTurn == players[0])
            currentTurn = players[1];
        else
            currentTurn = players[0];
    }

    public Player getCurrentTurn() {
        return currentTurn;
    }

    public void makeMove(Move move) {
        if (isMovePossible(move, move.getPlayer())) {
            makeValidMove(move);
        }
    }

    public Spot getSpot(int row, int column) {
        return this.board.getSpot(row, column);
    }
}
