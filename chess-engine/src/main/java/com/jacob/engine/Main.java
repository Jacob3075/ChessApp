package com.jacob.engine;

import com.jacob.engine.board.Move;
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
                game.makeHumanMove();
        }
    }
}
