package uniChess;

public interface Game {

    static Game newGame(Player player1, Player player2, String fen) {
        return null;
    }

    static Game newGame(Player player1, Player player2) {
        return null;
    }

    Player getCurrentPlayer();

    GameEvent advance(String in);
}
