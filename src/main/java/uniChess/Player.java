package uniChess;

/**
*   An object representing a Player in a chess game. Each Player has a color and an Identifier of type T. 
*   Two players of opposite color are required to initiate a Game.
*/
public class Player {
    private String identifier;

    /** A general boolean switch for drawing.*/
    protected boolean draw;

    /** The color of piece that the Player can move.*/
    protected Color color;

    public Player(String id, Color c){
    	this.identifier = id;
    	this.color = c;
    }

    /**
    *   Returns the identifier object associated with the player.
    *
    *   @return the identifier object associated with the player.
    **/
    public String getID(){
        return identifier;
    }

    @Override
    public String toString(){
    	return identifier;
    }

    /** Compares the players based on the value of their identifier objects*/
    @Override
    public boolean equals(Object o){
        if (o instanceof Player){
            Player op = (Player)o;
            return op.getID().equals(identifier);
        }
        return false;
    }


    protected void registerGame(GameImpl g){

    }

    public Color getColor() {
        return color;
    }
}
