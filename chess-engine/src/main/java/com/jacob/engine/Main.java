package com.jacob.engine;

import com.jacob.engine.game.Game;
import com.jacob.engine.player.HumanPlayer;

public class Main {
    public static void main(String[] args) {
        HumanPlayer p1 = new HumanPlayer(true);
        HumanPlayer p2 = new HumanPlayer(false);

        Game game = new Game(p1, p2);

        game.displayBoard();
    }
}