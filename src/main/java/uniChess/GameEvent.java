package uniChess;

/**
 * Type that the Game object will return on game advancement
 */
public enum GameEvent {
    OK,
    AMBIGUOUS,
    INVALID,
    ILLEGAL,
    CHECK,
    CHECKMATE,
    STALEMATE,
    DRAW
}
