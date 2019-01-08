package uniChess;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


/**
 * An object representing a Simulated Player in a chess game.
 */
public class Chesster<T> extends Player<T> {

    /**
     * Duration of the last turn.
     *
     * @return the last duration, not null
     */
    public Duration getLastDuration() {
        return lastDuration;
    }

    enum StrategyType {LOG, LINEAR, EXP2, EXP4, EXP10}

    private GameImpl game;

    /**
     * Determines amount of layers to calculate
     */
    protected final int AI_DEPTH;

    /**
     * Determines relative weight of piece values
     */
    protected int MATERIAL_WEIGHT = 3;

    private StrategyType STRATEGY = StrategyType.EXP4;

    private boolean dynamic = true;

    private Duration lastDuration = Duration.ZERO;

    /**
     * Creates a new Chesster with strength 3.
     *
     * @param id the Player-ID
     * @param c  the color
     * @see Chesster#Chesster(Object, Color, int)
     */
    public Chesster(T id, Color c) {
        this(id, c, 3);
    }

    /**
     * Creates a new Chesster with given strength.
     *
     * @param id       the Player-ID
     * @param c        the color
     * @param strength the strength
     */
    public Chesster(final T id, final Color c, final int strength) {
        super(id, c);
        this.AI_DEPTH = strength;
    }

    @Override
    protected void registerGame(GameImpl g) {
        this.game = g;
    }

    protected GameImpl getGame() {
        return this.game;
    }


    /**
     * Returns the best possible legal move for the bot based on individual tactics and strategy.
     *
     * @return the best move
     */
    public Move getMove() {
        SmartMove.MATERIAL_WEIGHT = this.MATERIAL_WEIGHT;

        List<Move> legal = game.getCurrentBoard().getLegalMoves(this);

        List<SmartMove> smartMoves = new ArrayList<>();

        for (Move move : legal) {
            smartMoves.add(new SmartMove(move));
        }

        Instant start = Instant.now();

        long sysTime = System.currentTimeMillis();

        Optional<SmartMove> best
                = smartMoves.stream().parallel()
                .map(sm -> new StrategyProcessorThread(sm, this))
                .map(StrategyProcessorThread::call)
                .max(Comparator.naturalOrder());

        lastDuration = Duration.between(start, Instant.now());

        return best.get();
    }
}


