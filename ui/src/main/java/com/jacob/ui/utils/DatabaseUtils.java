package com.jacob.ui.utils;

import com.jacob.database.game_data.PastGame;
import com.jacob.database.game_data.PlayedMove;
import com.jacob.database.user.User;
import com.jacob.engine.board.Move;
import com.jacob.engine.game.Game;
import com.jacob.ui.game.Position;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DatabaseUtils {
    private DatabaseUtils() {}

    public static PastGame createPastGame(Game game, User user) {
        PastGame pastGame = new PastGame();
        pastGame.setResult(game.getStatus().name());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime now = LocalDateTime.now();
        String dateTime = dtf.format(now);
        pastGame.setCreatedTime(dateTime);

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

    private static PositionedMove getPositionsFromMove(Move move) {
        return new PositionedMove(
                new Position(move.getStart().getI(), move.getStart().getJ()),
                new Position(move.getEnd().getI(), move.getEnd().getJ()),
                move.getPromotionChoiceValue());
    }

    private static PlayedMove createPlayedMoveFromPositions(PositionedMove positionedMove) {
        return new PlayedMove(
                null,
                null,
                positionedMove.start().index(),
                positionedMove.end().index(),
                positionedMove.promotionChoice);
    }

    public record PositionedMove(Position start, Position end, int promotionChoice) {}
}
