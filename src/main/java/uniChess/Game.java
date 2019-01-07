package uniChess;

import java.util.List;

public interface Game {

    static Game newGame(Player player1, Player player2, String fen) {
        return null;
    }

    static Game newGame(Player player1, Player player2) {
        return null;
    }

    Player getCurrentPlayer();

    Player getDormantPlayer();

    GameEvent advance(String in);

    List<Move> getLegalMoves(Player player);

    String getBoardString();
}
