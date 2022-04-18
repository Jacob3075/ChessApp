package com.jacob.ui.utils;

import com.jacob.database.game_data.PastGame;
import com.jacob.database.game_data.PlayedMove;
import com.jacob.database.user.User;
import com.jacob.engine.board.Move;
import com.jacob.engine.game.Game;
import com.jacob.ui.game.Position;

import java.util.List;

public class DatabaseUtils {
    private DatabaseUtils() {}

    public static PastGame createPastGame(Game game, User user) {
        PastGame pastGame = new PastGame();
        pastGame.setResult(game.getStatus().name());
        pastGame.setCreatedTime("sometime");
        pastGame.setUser(user);

        List<PlayedMove> playedMoves =
                game.getMovesPlayed().stream()
                        .map(DatabaseUtils::getPositionsFromMove)
                        .map(DatabaseUtils::createPlayedMoveFromPositions)
                        .peek(playedMove -> playedMove.setPastGame(pastGame))
                        .toList();

        pastGame.setPlayedMoves(playedMoves);
        return pastGame;
    }

    private static PositionPair getPositionsFromMove(Move move) {
        return new PositionPair(
                new Position(move.getStart().getI(), move.getStart().getJ()),
                new Position(move.getEnd().getI(), move.getEnd().getJ()));
    }

    private static PlayedMove createPlayedMoveFromPositions(PositionPair movePair) {
        return new PlayedMove(null, null, movePair.start().index(), movePair.end().index());
    }

    private record PositionPair(Position start, Position end) {}
}
