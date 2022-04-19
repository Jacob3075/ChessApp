package com.jacob.engine;

import com.jacob.engine.board.Move;
import com.jacob.engine.board.Spot;
import com.jacob.engine.game.Game;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.function.IntSupplier;

public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final Random random = new SecureRandom();
    private final IntSupplier getPawnPromotionChoice = this::getPawnPromotionChoice;

    private Game game;

    public static void main(String[] args) {
        Main main = new Main();
        main.playGame();
        System.out.println(main.game.getStatus());
    }

    private void playGame() {
        boolean isPlayerWhite = isHumanPlayerWhite();

        game = Game.createNewGame(isPlayerWhite);

        while (!game.isEnd()) {
            game.getBoard().displayBoard(isPlayerWhite);

            initiateNextTurn();

            System.out.println(game.getBoard().getEvaluation());
        }
    }

    private boolean isHumanPlayerWhite() {
        System.out.println("""
                Enter your choice:
                1. Play as White
                2. Play as Black
                3. Play as random
                """);
        int choice = scanner.nextInt();

        if (choice == 3) choice = random.nextInt(2) + 1;

        return (choice == 1);
    }

    private void initiateNextTurn() {
        List<Move> possibleMoves = game.getCurrentTurn().generatePossibleMoves(game.getBoard());

        if (possibleMoves.isEmpty()) {
            if (game.getCurrentTurn().isKingUnderAttack(game.getBoard())) game.setAndDeclareWin();
            else game.setAndDeclareDraw();
        } else {
//            if (!game.getCurrentTurn().isHumanPlayer()) game.makeComputerMove(possibleMoves);
//            else makeHumanMove();
        }
    }

    private void makeHumanMove() {
        Spot[] moveSpots = getHumanMoveStartAndEndSpots();
        Move humanMove = new Move(
                        game.getCurrentTurn(),
                        moveSpots[0],
                        moveSpots[1],
                getPawnPromotionChoice
        );
        boolean isMoveLegal = game.isMovePossible(humanMove, game.getCurrentTurn());

        while (!isMoveLegal) {
            System.out.print("Illegal move. Enter a different move: ");
            moveSpots = getHumanMoveStartAndEndSpots();
            humanMove = new Move(
                            game.getCurrentTurn(),
                            moveSpots[0],
                            moveSpots[1],
                    getPawnPromotionChoice
            );
            isMoveLegal = game.isMovePossible(humanMove, game.getCurrentTurn());
        }

        game.makeValidMove(humanMove);
    }

    private Spot[] getHumanMoveStartAndEndSpots() {
        // subtracting 1 from the input since the game uses 0-based indexing and the input is
        // expected to use 1-based
        int[] moveCoordinates = new int[4]; // startCol, startRow, endCol, endRow
        for (int i = 0; i < 4; i++) moveCoordinates[i] = scanner.nextInt() - 1;
        Spot start = game.getBoard().getSpot(moveCoordinates[1], moveCoordinates[0]);
        Spot end = game.getBoard().getSpot(moveCoordinates[3], moveCoordinates[2]);

        while (start == null || end == null) {
            System.out.print("Illegal move. Enter a different move: ");
            for (int i = 0; i < 4; i++) moveCoordinates[i] = scanner.nextInt() - 1;
            start = game.getBoard().getSpot(moveCoordinates[1], moveCoordinates[0]);
            end = game.getBoard().getSpot(moveCoordinates[3], moveCoordinates[2]);
        }

        return new Spot[] {start, end};
    }

    private int getPawnPromotionChoice() {
        System.out.println("Enter your choice:\n1. Queen\n2. Rook\n3. Bishop\n4. Knight");
        return scanner.nextInt();
    }
}
