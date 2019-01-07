package uniChess;

import java.util.Scanner;

import uniChess.ai.Chesster;

class ConsoleChess {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        Player<String> p1 = new Chesster<>("EXP2", Game.Color.WHITE);
        Player<String> p2 = new Chesster<>("Dynamic", Game.Color.BLACK);

        ((Chesster) p1).STRATEGY = Chesster.StrategyType.EXP2;
        ((Chesster) p2).STRATEGY = Chesster.StrategyType.LINEAR;

        Game chessGame = new Game(p1, p2);

        System.out.println(chessGame.getBoardList().size());
        chessGame.getCurrentBoard().print(p1, p2);

        while (true) {

            Player currentPlayer = chessGame.getCurrentPlayer();
            //in.nextLine();
            String input = ((currentPlayer instanceof Chesster) ? ((Chesster) currentPlayer).getMove().getFenString() : in.nextLine());

            if (input.equals("gametext")) {
                System.out.println(chessGame.getGameString());
                continue;
            }

            Game.GameEvent gameResponse = chessGame.advance(input);

            System.out.println(chessGame.getBoardList().size());
            chessGame.getCurrentBoard().print(p1, p2);

            switch (gameResponse) {

                case OK:
                    break;
                case AMBIGUOUS:
                    System.out.println("Ambiguous Move.");
                    return;
                case INVALID:
                    System.out.println("Invalid Move.");
                    return;
                case ILLEGAL:
                    System.out.println("Illegal Move.");
                    return;
                case CHECK:
                    System.out.println("You are in check!");
                    break;
                case CHECKMATE:
                    System.out.println("Checkmate. " + chessGame.getDormantPlayer().getID() + " wins!");
                    System.out.println(chessGame.getGameString());
                    System.exit(0);
                    return;
                case STALEMATE:
                    System.out.println("Stalemate. " + chessGame.getDormantPlayer().getID() + " wins!");
                    return;
                case DRAW:
                    System.out.println("Draw!");
                    return;

            }

        }
    }

}
