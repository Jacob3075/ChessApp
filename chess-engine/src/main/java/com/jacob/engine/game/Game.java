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

public class Game {
    private final Player[] players;
    private final Board board;
    private Player currentTurn;
    private GameStatus status;
    private  List<Move> movesPlayed;
    private  List<Spot[][]> boardPositions;
    private final Random random;

    public Game(Player playerZero, Player playerOne) {
        random = new SecureRandom();
        players = new Player[2];
        players[0] = playerZero;
        players[1] = playerOne;

        board = new Board();
        resetBoard();
    }

    public void resetBoard() {
        movesPlayed = new ArrayList<>();
        boardPositions = new ArrayList<>();

        // white plays first
        if(players[0].isWhiteSide())
            currentTurn = players[0];
        else
            currentTurn = players[1];

        setStatus(GameStatus.ACTIVE);
    }

    public List<Move> getMovesPlayed() {
        return movesPlayed;
    }

    public static Game createNewGame(boolean isHumanWhite) {
        return new Game(new HumanPlayer(isHumanWhite), new ComputerPlayer(!isHumanWhite));
    }

    public Board getBoard() {
        return this.board;
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

    public void setAndDeclareWin() {
        if(currentTurn.isWhiteSide())
            setStatus(GameStatus.BLACK_WIN);
        else
            setStatus(GameStatus.WHITE_WIN);
    }

    public void setAndDeclareDraw() {
        setStatus(GameStatus.DRAW);
    }

    @Deprecated
    public void makeComputerMove(Move move) {
        makeValidMove(move);
    }

    public Move getComputerMove(List<Move> possibleMoves){

        int randomIndex = random.nextInt(possibleMoves.size());
        return possibleMoves.get(randomIndex);
}

    public boolean isMovePossible(Move move, Player player) {
        Piece movedPiece = move.getPieceMoved();
        boolean equals1 = player.equals(currentTurn);
        boolean equals2 = movedPiece != null;
        boolean equals3 = movedPiece.isWhite() == player.isWhiteSide();
        boolean equals4 = movedPiece.canMove(board, move.getStart(), move.getEnd());
        System.out.println("move = " + move);
        System.out.println("equals1 = " + equals1);
        System.out.println("equals2 = " + equals2);
        System.out.println("equals3 = " + equals3);
        System.out.println("equals4 = " + equals4);
        return equals1
                && equals2
                && equals3
                && equals4;
    }

    public void makeValidMove(Move move) {
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
        boardPositions.add(board.getSpots());
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

        move.getPromotionChoice();
        int choice = move.getPromotionChoiceValue();

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
        if (!isMovePossible(move, move.getPlayer())) return;

        System.out.println("Game.makeMove");

        makeValidMove(move);
    }

    public Spot getSpot(int row, int column) {
        return this.board.getSpot(row, column);
    }
}
