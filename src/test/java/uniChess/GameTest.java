package uniChess;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class GameTest {

    @Test
    void testKurzeRochadeWeiß() {
        Player p1 = new Player("1", Color.WHITE);
        Player p2 = new Player("2", Color.BLACK);
        Game game = Game.newGame(p1, p2, "rnbqkbnr/pppppppp/8/8/8/1NBPQ1N1/PP1PPBPP/R3K2R w");

        System.out.println(game.getBoardString());

        Assertions.assertThat(game.advance("e1-g1")).isEqualTo(GameEvent.OK);

        System.out.println(game.getBoardString());
    }

    @Test
    void testIllegalKurzeRochadeWeiß() {
        Player p1 = new Player("1", Color.WHITE);
        Player p2 = new Player("2", Color.BLACK);
        Game game = Game.newGame(p1, p2, "rnbqkbnr/pppppppp/8/8/8/1NBPQ1N1/PP1PPBPP/RB2K1BR w");

        System.out.println(game.getBoardString());

        Assertions.assertThat(game.advance("e1-c1")).isEqualTo(GameEvent.INVALID);

        System.out.println(game.getBoardString());
    }

    @Test
    void testLangeRochadeWeiß() {
        Player p1 = new Player("1", Color.WHITE);
        Player p2 = new Player("2", Color.BLACK);
        Game game = Game.newGame(p1, p2, "rnbqkbnr/pppppppp/8/8/8/1NBPQ1N1/PP1PPBPP/R3K2R w");

        System.out.println(game.getBoardString());

        Assertions.assertThat(game.advance("e1-c1")).isEqualTo(GameEvent.OK);

        System.out.println(game.getBoardString());
    }

    @Test
    void testKurzeRochadeSchwarz() {
        Player p1 = new Player("1", Color.WHITE);
        Player p2 = new Player("2", Color.BLACK);
        Game game = Game.newGame(p1, p2, "r3k2r/pppppppp/8/8/8/1NBPQ1N1/PP1PPBPP/R3K2R b");

        System.out.println(game.getBoardString());

        Assertions.assertThat(game.advance("e8-g8")).isEqualTo(GameEvent.OK);
        System.out.println(game.getBoardString());
    }

    @Test
    void testLangeRochadeSchwarz() {
        Player p1 = new Player("1", Color.WHITE);
        Player p2 = new Player("2", Color.BLACK);
        Game game = Game.newGame(p1, p2, "r3k2r/pppppppp/8/8/8/1NBPQ1N1/PP1PPBPP/R3K2R b");

        System.out.println(game.getBoardString());

        Assertions.assertThat(game.advance("e8-c8")).isEqualTo(GameEvent.OK);

        System.out.println(game.getBoardString());
    }


    @Test
    void testEnPassantWeiß() {
        Player p1 = new Player("1", Color.WHITE);
        Player p2 = new Player("2", Color.BLACK);
        Game game = Game.newGame(p1, p2, "rnbqkbnr/pp1ppppp/8/2pP4/8/8/PPP1PPPP/RNBQKBNR w");

        System.out.println(game.getBoardString());

        Assertions.assertThat(game.advance("d5-c6")).isEqualTo(GameEvent.OK);

        System.out.println(game.getBoardString());
    }


    @Test
    void testEmptyfield() {
        Player p1 = new Player("1", Color.WHITE);
        Player p2 = new Player("2", Color.BLACK);
        Game game = Game.newGame(p1, p2, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w");

        Assertions.assertThat(game.advance("d5-c6")).isEqualTo(GameEvent.INVALID);
    }


}
