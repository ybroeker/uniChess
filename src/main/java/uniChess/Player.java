package uniChess;

/**
*   An object representing a Player in a chess game. Each Player has a color and an Identifier of type T. 
*   Two players of opposite color are required to initiate a Game.
*/
public class Player <T> {
    private T IDENTIFIER;

    /** A general boolean switch for drawing.*/
    protected boolean draw;

    /** The color of piece that the Player can move.*/
    protected Color color;

    public Player(T id, Color c){
    	this.IDENTIFIER = id;
    	this.color = c;
    }

    /**
    *   Returns the identifier object associated with the player.
    *
    *   @return the identifier object associated with the player.
    **/
    public T getID(){
        return IDENTIFIER;
    }

    @Override
    public String toString(){
    	return String.valueOf(IDENTIFIER);
    }

    /** Compares the players based on the value of their identifier objects*/
    @Override
    public boolean equals(Object o){
        if (o instanceof Player){
            Player op = (Player)o;
            return op.getID().equals(IDENTIFIER);
        }
        return false;
    }


    protected void registerGame(GameImpl g){

    }

    public Color getColor() {
        return color;
    }
}
