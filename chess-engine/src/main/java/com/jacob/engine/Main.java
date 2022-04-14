package com.jacob.engine;

import com.jacob.engine.game.Game;
import com.jacob.engine.player.HumanPlayer;
import com.jacob.engine.player.ComputerPlayer;
public class Main {
    public static void main(String[] args) {
        HumanPlayer p1 = new HumanPlayer(true);
        ComputerPlayer p2 = new ComputerPlayer(false);

        Game game = new Game(p1, p2);

        while(!game.isEnd()) {
            game.board.displayBoard();
            game.initiateNextTurn();
            System.out.println(game.board.getEvaluation());
        }
    }
}
