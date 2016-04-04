package maze.logic;

import java.io.Serializable;

/**
 * Sword.java - Element of maze
 * @author João Barbosa and William Fukunaga
 * @version 1.8
 */
public class Sword extends Element implements Serializable {
	private static final long serialVersionUID = 1L;


	/**
	 * Creates a new sword given the coordinates
	 *
	 * @param line vertical coordinate
	 * @param column horizontal coordinate
	 */
	public Sword(int line, int column){
		
		position = new Position(line, column);
	}
	
	
	/**
	 * Creates a new sword given the position
	 *
	 * @param pos position to be set
	 */
	public Sword(Position pos){	
		position = pos;
	}
}
