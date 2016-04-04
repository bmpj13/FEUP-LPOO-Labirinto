package maze.logic;

/**
 * Dragon.java - Element of maze 
 * @author João Barbosa and William Fukunaga
 * @version 1.8
 */
public class Dragon extends Element {

	public enum DRAGON_STATE {AWAKE, SLEEPING, DEAD};

	private DRAGON_STATE dragonState;
	
	
	/**
	 * Creates a new dragon and sets the state to "awake"
	 * @param line		vertical coordinate 
	 * @param column	horizontal coordinate
	 */
	public Dragon(int line, int column) {

		position = new Position(line, column);
		dragonState = DRAGON_STATE.AWAKE;
	}
	
	/**
	 * Creates a new dragon and sets the state to "awake"
	 * @param pos	created dragon position
	 */
	public Dragon(Position pos){	
		position = pos;
		dragonState = DRAGON_STATE.AWAKE;
	}

	/**
	 * Changes the dragon's position in the maze
	 * @param pos	new dragon position
	 */
	public void move(Position pos) {

		position.y = pos.y;
		position.x = pos.x;
	}

	/**
	 * Changes the the dragon state
	 *  @param ds	new dragon state
	 */
	public void setState(DRAGON_STATE ds) {
		dragonState = ds;
	}


	/**
	 * @return current state of dragon
	 */
	public DRAGON_STATE getState() { 
		return dragonState; 
	}
}
