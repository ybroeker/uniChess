package com.example.uniChess;

import java.util.Scanner;

import uniChess.*;

class ConsoleChess {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        Chesster<String> p1 = new Chesster<>("White", Color.WHITE,1);
        Chesster<String> p2 = new Chesster<>("Black", Color.BLACK,5);

        //((Chesster) p1).STRATEGY = Chesster.StrategyType.EXP2;
        //((Chesster) p2).STRATEGY = Chesster.StrategyType.LINEAR;

        Game chessGame = Game.newGame(p1, p2);

        System.out.println(chessGame.getBoardString());

        while (true) {

            Player currentPlayer = chessGame.getCurrentPlayer();
            //in.nextLine();


            String input = ((currentPlayer instanceof Chesster) ? ((Chesster) currentPlayer).getMove().getFenString() : in.nextLine());

            if (input.equals("gametext")) {
                //System.out.println(chessGame.getGameString());
                continue;
            }

            GameEvent gameResponse = chessGame.advance(input);

         //   System.out.println(chessGame.getBoardList().size());
         //   chessGame.getCurrentBoard().print(p1, p2);
            System.out.println(chessGame.getBoardString());

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
                    //System.out.println(chessGame.getGameString());
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
