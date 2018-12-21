package uniChess.ai;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import uniChess.*;


/**
*   An object representing a Simulated Player in a chess game. 
*/
public class Chesster <T> extends Player<T> {
    public enum StrategyType {LOG, LINEAR, EXP2, EXP4, EXP10}

    private Game game;

    /** Determines amount of layers to calculate */
    public int AI_DEPTH = 4;

    /** Determines relative weight of piece values */
    public int MATERIAL_WEIGHT = 3;

    public StrategyType STRATEGY = StrategyType.EXP4;
    public boolean dynamic=true;

    public Chesster(T id, Game.Color c){
        super(id, c);
    }

    @Override
    public void registerGame(Game g){
        this.game = g;
    }

    public Game getGame(){
        return this.game;
    }

    long sysTime;
    long avgThreadTime=0;
    int threads;

    /**
    *   Returns the best possible legal move for the bot based on individual 
    *   tactics and strategy (logarithmic sum of average tactical value of future moves).
    *
    *   @return the best move
    */
    public Move getMove(){
        SmartMove.MATERIAL_WEIGHT = this.MATERIAL_WEIGHT;
        if (Board.playerHasCheck(game.getCurrentBoard(), game.getDormantPlayer()))
            STRATEGY = StrategyType.LOG;
        else STRATEGY = StrategyType.EXP4;
        Move best;

        List<Move> legal = game.getCurrentBoard().getLegalMoves(this);
        
        List<SmartMove> smartMoves = new ArrayList<>();
        
        for (Move move : legal)
            smartMoves.add(new SmartMove(move));

        System.out.println("# Moves: "+smartMoves.size());
        // System.out.println("# Using: "+STRATEGY);

        sysTime = System.currentTimeMillis();

        List<StrategyProcessorThread> threadPool = new ArrayList<>();

        for (SmartMove sm : smartMoves)
            threadPool.add(new StrategyProcessorThread(sm, this));
        

        for (int i = 0; i < smartMoves.size(); ++i){
            threadPool.get(i).start();
            printProgress(i, smartMoves.size(), threadPool.get(i).sm.toString());
            try{
                threadPool.get(i).join();
            } catch (Exception e) {}
        }

        Collections.sort(smartMoves);
        long processTime = (System.currentTimeMillis() - sysTime);
        
        int treesize = 0;
        for (StrategyProcessorThread t : threadPool){
            treesize += t.treesize;
            avgThreadTime += t.runTime;
            ++threads;
        }

        System.out.format("\n# Time : %sms | Avg Move Process Time: %sms\n", processTime, (avgThreadTime / threads));
        System.out.format("# Total Sub Move Tree Size: %s | Avg Sub Move Process Time: %sms\n\n", treesize, (processTime / treesize));


        // if (smartMoves.get(smartMoves.size()-1).strategicValue == smartMoves.get(smartMoves.size()-2).strategicValue)
        //     best = smartMoves.get(smartMoves.size()-((new Random()).nextInt(1)+1));
            
        best = smartMoves.get(smartMoves.size()-1);

        // for (SmartMove saasd : smartMoves)
        //     System.out.println(saasd.getDataSring());

        System.out.println(((SmartMove)best).getDataSring());

        return best;
    }


    public void printProgress(int prog, int total, String sm){
        double percent = (double)prog/total;
        double percentFrom20 = 20 * percent;
        System.out.print("\rThinking [");
        for (int i = 0; i < 20; ++i){
            if (i <= (int)percentFrom20)
                System.out.print("=");
            else System.out.print(" ");
        }
        System.out.print("] "+sm);
    }
}


