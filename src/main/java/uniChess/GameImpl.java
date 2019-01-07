package uniChess;

import java.util.ArrayList;
import java.util.List;

/**
 * An object for creating, maintaining, and communicating with a chess game.
 */
public class GameImpl implements Game {

    /**
     * Type of game pieces.
     */
    public enum PieceType {PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING}

    /**
     * Enables unicode output in string representations.
     */
    public static boolean unicode = true;

    /**
     * Restricts unicode characters to filled in type.
     */
    public static boolean useDarkChars = false;

    private boolean whiteMove = true;

    private String gameString = "";

    private List<Board> boards = new ArrayList<>();

    private Player white, black;

    /**
     * Creates a new Game between the supplied players starting from the gamestate
     * supplied through the gameString. This allows a game to be continued from
     * a specific state. (See {@link #getGameString() getGameString} method).
     *
     * @param player1    The first player
     * @param player2    The second player
     * @param gameString The series of moves to be performed
     *                   before the game starts.
     */
    public GameImpl(Player player1, Player player2, String gameString/*TODO FEN*/) {
        this(player1, player2);

        for (String move : gameString.split(",")) {
            advance(move);
        }
    }

    /**
     * Creates a Game between the supplied players. One player must have a Color
     * of {@code Game.Color.WHITE} and one must have a Color of {@code Game.Color.BLACK}.
     *
     * @param player1 The first player
     * @param player2 The second player
     */
    public GameImpl(Player player1, Player player2) {
        white = (player1.color.equals(Color.WHITE) ? player1 : player2);
        black = (player1.color.equals(Color.WHITE) ? player2 : player1);
        white.registerGame(this);
        black.registerGame(this);

        boards.add(new Board());
        getCurrentBoard().processLegal();
    }

    /**
     * Returns the moves made (in algebraic notation) so far in the game, separated
     * by commas. The resulting string can be used to make a new Game object that
     * will repeat the moves outlined in this String upon creation.
     *
     * @return The game string
     */
    public String getGameString() {
        if (gameString.isEmpty()) {
            return "";
        }
        return gameString.substring(0, gameString.length() - 1);
    }

    /**
     * Returns a List of all generated boards in the game.
     *
     * @return The list of boards.
     */
    public List<Board> getBoardList() {
        return boards;
    }

    /**
     * Sets the current game board to the supplied Board.
     *
     * @param board The board to be used.
     */
    public void setcurrentBoard(Board board) {
        boards.add(board);
    }

    /**
     * Returns the current game board.
     *
     * @return The current board.
     */
    public Board getCurrentBoard() {
        return boards.get(boards.size() - 1);
    }

    /**
     * Returns the Player-to-move.
     *
     * @return The current player.
     */
    @Override
    public Player getCurrentPlayer() {
        return whiteMove ? white : black;
    }

    /**
     * Returns the Player who moved last.
     *
     * @return The player who moved last.
     */
    public Player getDormantPlayer() {
        return whiteMove ? black : white;
    }

    /**
     * Returns the Player controlling a given color.
     *
     * @param color The color to get the player for
     * @return The player controlling the given color.
     */
    public Player getPlayer(Color color) {
        return color.equals(Color.BLACK) ? black : white;
    }

    /**
     * Returns the opposite of a given color.
     *
     * @param c The opposite of the desired color
     * @return The opposite of the supplied color
     */
    public static Color getOpposite(Color c) {
        return (c.equals(Color.BLACK) ? Color.WHITE : Color.BLACK);
    }

    /**
     * <p>
     * Attempts to advance the game by one move specified by the supplied algebraic
     * notation string. If the move is valid, legal, and does not put the other
     * player in check, checkmate, or stalemate, then this method will perform the
     * move, add the newly generated board the the board list, and switch the current
     * player, and return {@code GameEvent.OK}. Otherwise, it will return the
     * {@code GameEvent} type associated with the corresponding event.
     * <p>
     * If the returned value is {@code GameEvent.ILLEGAL},
     * {@code GameEvent.INVALID}, or {@code GameEvent.AMBIGUOUS} then the game
     * was not successfully advanced and the turn still needs to be completed with
     * a valid move.
     * <p>
     * {@code GameEvent.DRAW} will be returned only when the draw field of each
     * Player is true. Since there is no standard convention for Draws, how this
     * is implemented is up to the developer.
     *
     * @param in The algebraic notation move
     * @return The player who moved last.
     */
    @Override
    public GameEvent advance(String in) {
        try {

            if (white.draw && black.draw) {
                return GameEvent.DRAW;
            }

            Move move = Move.parseFenMove(getCurrentBoard(), getCurrentPlayer().color, in);

            List<Move> legal = getCurrentBoard().getLegalMoves(getCurrentPlayer());

            if (!legal.contains(move)) {
                return GameEvent.ILLEGAL;
            }

            boards.add(move.getSimulation());
            getCurrentBoard().processLegal();

            whiteMove = !whiteMove;

            gameString += move.getFenString();

            if (Board.playerHasCheck(getCurrentBoard(), getDormantPlayer())
                && getCurrentBoard().getLegalMoves(getCurrentPlayer()).isEmpty()) {
                return GameEvent.CHECKMATE;
            } else if (getCurrentBoard().getLegalMoves(getCurrentPlayer()).isEmpty()) { // This is a stalemate, which results in Draw
                return GameEvent.DRAW;
            } else if (Board.playerHasCheck(getCurrentBoard(), getDormantPlayer())) {
                return GameEvent.CHECK;
            }


        } catch (GameException ge) {

            switch (ge.getType()) {

                case GameException.INVALID_MOVE:
                    System.err.println(ge.getMessage());
                    return GameEvent.INVALID;

                case GameException.AMBIGUOUS_MOVE:
                    return GameEvent.AMBIGUOUS;

            }
        }

        return GameEvent.OK;
    }
}
