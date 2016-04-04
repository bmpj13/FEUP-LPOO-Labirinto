package maze.logic;

import java.io.Serializable;

/**
 * Exit.java - Element of maze that declares exit position
 * @author João Barbosa and William Fukunaga
 * @version 1.8
 */
public class Exit extends Element implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new exit in the maze
	 * @param line		vertical coordinate 
	 * @param column	horizontal coordinate
	 */
	public Exit(int line, int column){

		position = new Position(line, column);
	}

}
