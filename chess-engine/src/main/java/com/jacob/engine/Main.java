package com.jacob.engine;

import com.jacob.engine.board.Move;
import com.jacob.engine.board.Spot;
import com.jacob.engine.game.Game;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final Scanner in = new Scanner(System.in);
    private static final Random random = new Random();

    private static Game game;
    public static void main(String[] args) {
        boolean isPlayerWhite = isHumanPlayerWhite();

        game = Game.createNewGame(isPlayerWhite);

        while(!game.isEnd()) {
            game.getBoard().displayBoard(isPlayerWhite);

            initiateNextTurn();

            System.out.println(game.getBoard().getEvaluation());
        }
    }

    private static boolean isHumanPlayerWhite() {
        System.out.println("Enter your choice:\n1. Play as White\n2. Play as Black\n3. Play as random");
        int choice = in.nextInt();

        if(choice == 3)
            choice = random.nextInt(2) + 1;

        return (choice == 1);
    }

    private static void initiateNextTurn() {
        List<Move> possibleMoves = game.getCurrentTurn().generatePossibleMoves(game.getBoard());

        if(possibleMoves.isEmpty()) {
            if(game.getCurrentTurn().isKingUnderAttack(game.getBoard()))
                game.setAndDeclareWin();
            else
                game.setAndDeclareDraw();
        }
        else {
            if(!game.getCurrentTurn().isHumanPlayer())
                game.makeComputerMove(possibleMoves);
            else
                makeHumanMove();
        }
    }

    private static void makeHumanMove() {
        Spot[] moveSpots = getHumanMoveStartAndEndSpots();
        Move humanMove = new Move(game.getCurrentTurn(), moveSpots[0], moveSpots[1]);
        boolean isMoveLegal = game.isMovePossible(humanMove, game.getCurrentTurn());

        while(!isMoveLegal) {
            System.out.print("Illegal move. Enter a different move: ");
            moveSpots = getHumanMoveStartAndEndSpots();
            humanMove = new Move(game.getCurrentTurn(), moveSpots[0], moveSpots[1]);
            isMoveLegal = game.isMovePossible(humanMove, game.getCurrentTurn());
        }

        game.makeValidMove(humanMove);
    }

    private static Spot[] getHumanMoveStartAndEndSpots() {
        Scanner in = new Scanner(System.in);

        // subtracting 1 from the input since the game uses 0-based indexing and the input is expected to use 1-based
        int[] moveCoordinates = new int[4]; // startCol, startRow, endCol, endRow
        for(int i = 0; i < 4; i++)
            moveCoordinates[i] = in.nextInt()-1;
        Spot start = game.getBoard().getSpot(moveCoordinates[1], moveCoordinates[0]);
        Spot end = game.getBoard().getSpot(moveCoordinates[3], moveCoordinates[2]);

        while(start == null || end == null) {
            System.out.print("Illegal move. Enter a different move: ");
            for(int i = 0; i < 4; i++)
                moveCoordinates[i] = in.nextInt()-1;
            start = game.getBoard().getSpot(moveCoordinates[1], moveCoordinates[0]);
            end = game.getBoard().getSpot(moveCoordinates[3], moveCoordinates[2]);
        }

        return new Spot[]{start, end};
    }
}
