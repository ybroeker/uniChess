package uniChess;

import java.util.Objects;

/**
 * An object representing a point in a two dimensional grid. This is used for the internal coordinate system of the
 * Board, as well as parsing algebraic locations to integer format.
 */
public class Location {
    public final int x, y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Location(String in) throws GameException {
        try {
            in = in.toLowerCase();
            final String col = "abcdefgh";
            final String row = "12345678";
            this.x = col.indexOf(in.charAt(0));
            this.y = row.indexOf(in.charAt(1));

            if (x < 0 || y < 0) {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            throw new GameException(GameException.INVALID_MOVE, "Could not parse location from '" + in + "'");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Location that = (Location) o;
        return this.x == that.x &&
               this.y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        final String col = "abcdefgh";
        final String row = "12345678";
        return String.format("%c%c", col.charAt(x), row.charAt(y));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
