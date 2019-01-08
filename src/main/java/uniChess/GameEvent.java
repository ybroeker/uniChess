package uniChess;

/**
 * Type that the Game object will return on game advancement
 */
public enum GameEvent {
    OK,
    /**
     * Invalid move format.
     */
    INVALID,
    /**
     * Illegal move.
     */
    ILLEGAL,
    /**
     * The other player is in check.
     */
    CHECK,
    /**
     * The other player is in checkmate.
     */
    CHECKMATE,
    /**
     * Draw.
     */
    DRAW
}
