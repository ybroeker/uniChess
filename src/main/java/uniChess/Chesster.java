package uniChess;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


/**
 * An object representing a Simulated Player in a chess game.
 */
public class Chesster<T> extends Player<T> {
    enum StrategyType {LOG, LINEAR, EXP2, EXP4, EXP10}

    private GameImpl game;

    /**
     * Determines amount of layers to calculate
     */
    protected int AI_DEPTH = 4;

    /**
     * Determines relative weight of piece values
     */
    protected int MATERIAL_WEIGHT = 3;

    private StrategyType STRATEGY = StrategyType.EXP4;

    private boolean dynamic = true;

    /**
     * Creates a new Chesster with strength 3.
     *
     * @param id the Player-ID
     * @param c  the color
     * @see Chesster#Chesster(Object, Color, int)
     */
    public Chesster(T id, Color c) {
        super(id, c);
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
     * Returns the best possible legal move for the bot based on individual
     * tactics and strategy (logarithmic sum of average tactical value of future moves).
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

        System.out.println("# Possible Moves: " + smartMoves.size());

        long sysTime = System.currentTimeMillis();

        Optional<SmartMove> best
                = smartMoves.stream().parallel()
                .map(sm -> new StrategyProcessorThread(sm, this))
                .map(StrategyProcessorThread::call)
                .max(Comparator.naturalOrder());

        long processTime = (System.currentTimeMillis() - sysTime);

        System.out.format("\n# Time : %sms\n", processTime);
        return best.get();
    }
}


