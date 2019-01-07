package uniChess;

import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

/**
 * Type that the Game object will return on game advancement
 */
public enum GameEvent {OK, AMBIGUOUS, INVALID, ILLEGAL, CHECK, CHECKMATE, STALEMATE, DRAW}
