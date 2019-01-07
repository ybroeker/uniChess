package uniChess.ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import uniChess.Color;
import uniChess.GameImpl;
import uniChess.Move;
import uniChess.Player;


/**
 * An object representing a Simulated Player in a chess game.
 */
public class Chesster<T> extends Player<T> {
    public enum StrategyType {LOG, LINEAR, EXP2, EXP4, EXP10}

    private GameImpl game;

    /**
     * Determines amount of layers to calculate
     */
    public int AI_DEPTH = 4;

    /**
     * Determines relative weight of piece values
     */
    public int MATERIAL_WEIGHT = 3;

    public StrategyType STRATEGY = StrategyType.EXP4;

    public boolean dynamic = true;

    public Chesster(T id, Color c) {
        super(id, c);
    }

    @Override
    public void registerGame(GameImpl g) {
        this.game = g;
    }

    public GameImpl getGame() {
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


