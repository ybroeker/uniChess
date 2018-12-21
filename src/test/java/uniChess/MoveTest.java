package uniChess;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MoveTest {

    private Board board;

    @BeforeEach
    void setUp() {
        this.board = new Board();
        board.processLegal();
    }

    @Test
    void parseANMove() throws Exception {
        String in = "pd4";

        final Move move = Move.parseANMove(board, Game.Color.WHITE, in);
        Assertions.assertThat(move.origin).isEqualTo(new Location("d2"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"d2-d4;","d2d4;","d2-d4","d2d4"})
    void parseValidFenMove(final String in) throws Exception {
        final Move move = Move.parseFenMove(board, Game.Color.WHITE, in);
        Assertions.assertThat(move.origin).isEqualTo(new Location("d2"));
        Assertions.assertThat(move.destination).isEqualTo(new Location("d4"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"d2-d5;","d2c4;"})
    void shouldThrowExceptionOnIllegalMove(final String in) throws Exception {
        Assertions.assertThatThrownBy(() -> Move.parseFenMove(board, Game.Color.WHITE, in)).isInstanceOf(GameException.class);

    }

    @ParameterizedTest
    @ValueSource(strings = {"i2-d4;", "a8-a9;"})
    void shouldThrowExceptionOnIllegalMoveFormat(String in) {
        Assertions.assertThatThrownBy(() -> Move.parseFenMove(board, Game.Color.WHITE, in)).isInstanceOf(GameException.class);
    }

    @Test
    void toFen() throws GameException {
        Board board = new Board();

        Move move = new Move(new Location("d2"), new Location("d4"), board);

        Assertions.assertThat(move.getFenString()).isEqualTo("d2-d4;");

    }
}
