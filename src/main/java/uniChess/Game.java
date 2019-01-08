package uniChess;

import java.util.List;

/**
 * A chessgame.
 */
public interface Game {

    /**
     * Starts a new game with the given FEN.
     *
     * @param player1 the first player
     * @param player2 the second player
     * @param fen     the fen
     * @return a new game
     */
    static Game newGame(Player player1, Player player2, String fen) {
        return new GameImpl(player1, player2, fen);
    }

    /**
     * Starts a new game.
     *
     * @param player1 the first player
     * @param player2 the second player
     * @return a new game
     */
    static Game newGame(Player player1, Player player2) {
        return new GameImpl(player1, player2);
    }

    /**
     * Returns the current Player.
     *
     * @return the current player
     */
    Player getCurrentPlayer();

    /**
     * Returns the last Player.
     *
     * @return the last player
     */
    Player getDormantPlayer();

    /**
     * Advances the game with the move specified by the supplied fen-string.
     * <p>
     * The string must be in the format {@code "from-to"}, eg. {@code "a2-a3"}.
     *
     * @param in the turn
     * @return the result
     */
    GameEvent advance(String in);

    /**
     * Returns all possible moves for a player.
     *
     * @param player the player
     * @return a list of moves, not null.
     */
    List<Move> getLegalMoves(Player player);

    /**
     * Retuns the current board as a string.
     *
     * @return the board
     */
    String getBoardString();
}
