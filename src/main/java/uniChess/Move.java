package uniChess;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An object representing a replacement of one Tile in a Board object with another.
 */
public class Move {
    /**
     * Boolean flag for special move type.
     */
    boolean ENPASSE, QCASTLE, KCASTLE, PROMOTION, CHECKMATE;

    /**
     * Location of Tile containing piece to move
     */
    Location origin;

    /**
     * Location of Tile to move piece to
     */
    Location destination;

    Board board, sim = null;

    Piece movingPiece;

    /**
     * Value of the piece this move would capture
     */
    double materialValue;


    protected Move(Location a, Location b, Board bo) {
        origin = a;
        destination = b;
        board = bo;
        movingPiece = bo.getTile(origin).getOccupator();
    }

    protected Move(Move m) {
        this.ENPASSE = m.ENPASSE;
        this.QCASTLE = m.QCASTLE;
        this.KCASTLE = m.KCASTLE;
        this.PROMOTION = m.PROMOTION;
        this.CHECKMATE = m.CHECKMATE;
        this.origin = m.origin;
        this.destination = m.destination;
        this.board = m.board;
        this.sim = (m.sim != null) ? m.sim : null;
        this.movingPiece = m.movingPiece;
        this.materialValue = m.materialValue;
    }


    protected Board getSimulation() {
        if (sim == null) {
            sim = this.board.performMove(this);
        }
        return sim;
    }

    protected boolean isSpecial() {
        return (ENPASSE || QCASTLE || KCASTLE || PROMOTION || CHECKMATE);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Move) {
            Move om = (Move) o;
            return (om.origin.equals(this.origin) && om.destination.equals(this.destination));
        }
        return false;
    }

    /**
     * Creates a Move instance from a valid Algebraic Noation string.
     *
     * @param board The board this move is to be executed on.
     * @param color The color of the team/player the move is to be created for
     * @param in    The algebraic notation string detailing the move
     * @return A new Move instance.
     * @throws GameException
     **/
    protected static Move parseANMove(Board board, Color color, String in) throws GameException {
        if (in.equals("0-0")) {
            in = (color.equals(Color.BLACK) ? "kg8" : "kg1");
        } else if (in.equals("0-0-0")) {
            in = (color.equals(Color.BLACK) ? "kc8" : "kc1");
        } else if (in.length() == 2) {
            in = "p" + in;
        }

        String[] tokens = new String[in.length()];
        for (int i = 0; i < in.length(); ++i) {
            tokens[i] = String.valueOf(in.charAt(i));
        }

        String pieceSymbol = tokens[0];

        int rank = -1, file = -1;

        //[piece][rank OR file][destination] (e.g. Bfg6)
        if (tokens.length == 4) {
            char mysteryToken = tokens[1].toLowerCase().charAt(0);
            if (Character.isLetter(mysteryToken)) {
                file = "abcdefgh".indexOf(mysteryToken);
                if (file < 0) {
                    throw new GameException(GameException.INVALID_MOVE, "Invalid move.");
                }
            } else if (Character.isDigit(mysteryToken)) {
                rank = Character.getNumericValue(mysteryToken) - 1;
                if (rank < 0 || rank > 7) {
                    throw new GameException(GameException.INVALID_MOVE, "Invalid move.");
                }
            }
        }

        // [piece][rank AND file][destination] (e.g. Bf5g6)
        if (tokens.length == 5) {
            file = "abcdefgh".indexOf(tokens[1].toLowerCase().charAt(0));
            rank = Character.getNumericValue(tokens[2].charAt(0)) - 1;
            if ((file < 0) || (rank < 0 || rank > 7)) {
                throw new GameException(GameException.INVALID_MOVE, "Invalid move.");
            }
        }

        Location dest;
        try {
            dest = new Location(tokens[tokens.length - 2] + tokens[tokens.length - 1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new GameException(GameException.INVALID_MOVE, "Invalid move.");
        }
        // System.out.format("pieceSymbol: %s\ndest: %s\n", pieceSymbol, dest);

        List<Board.Tile> potentialLocations = new ArrayList<>();

        for (Board.Tile t : board.getTileList()) {

            Piece p = t.getOccupator();

            if (p == null || !p.color.equals(color)) {
                continue;
            }

            if (p.ofType(pieceSymbol) && board.isValidMove(new Move(t.getLocale(), dest, board))) {
                if (((rank < 0 ^ t.getLocale().y == rank)) && ((file < 0 ^ t.getLocale().x
                                                                           == file))) // if the rank or file have been specified, add only matching pieces
                {
                    potentialLocations.add(t);
                }
            }
        }

        if (potentialLocations.size() < 1) {
            throw new GameException(GameException.INVALID_MOVE, "Invalid move.");
        }

        if (potentialLocations.size() > 1) {
            throw new GameException(GameException.AMBIGUOUS_MOVE, "yep");
        }

        Move m = new Move(potentialLocations.get(0).getLocale(), dest, board);
        board.isValidMove(m); // adds special move flags
        return m;

    }

    protected static Move parseFenMove(final Board board, final Color color, final String in) throws GameException {
        Matcher moveMatcher = Pattern.compile("([a-h][1-8])-?([a-h][1-8]);?").matcher(in);

        if (!moveMatcher.find()) {
            throw new GameException(GameException.INVALID_MOVE, "Invalid move-pattern: " + in);
        }

        String fromString = moveMatcher.group(1);
        String toString = moveMatcher.group(2);

        final Move move = new Move(new Location(fromString), new Location(toString), board);

        if (board.getTile(move.origin).getOccupator() == null) {
            throw new GameException(GameException.INVALID_MOVE, "No Piece on Field: " + in);
        }
        if (board.getTile(move.origin).getOccupator().color != color) {
            throw new GameException(GameException.INVALID_MOVE, "Invalid piece-color: " + in);
        }
        if (!board.isValidMove(move)) {
            throw new GameException(GameException.INVALID_MOVE, "Invalid move: " + in);
        }
        return move;
    }

    /**
     * @return A full algebraic notation representation of this move, including rank and file specifiers.
     */
    protected String getANString() {
        if (KCASTLE) {
            return ("0-0");
        }
        if (QCASTLE) {
            return ("0-0-0");
        }
        return String.format("%s%s%s", board.getTile(origin).getOccupator().getSymbol(false).toLowerCase(), origin, destination);
    }

    public String getFenString() {
        return String.format("%s-%s;", origin, destination);
    }

    @Override
    public String toString() {
        return String.format("%s > %s%s", board.getTile(origin).getOccupator(), (
                materialValue > 0 ? board.getTile(destination).getOccupator() : ""), destination);
    }

    protected boolean isCheckmate() {
        return CHECKMATE;
    }

    protected double getMaterialValue() {
        return materialValue;
    }

    public Location getOrigin() {
        return origin;
    }

    public Location getDestination() {
        return destination;
    }
}
