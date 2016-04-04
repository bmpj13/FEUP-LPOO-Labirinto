package maze.logic;

/**
 * Exit.java - Element of maze that declares exit position
 * @author João Barbosa and William Fukunaga
 * @version 1.8
 */
public class Exit extends Element {

	
	/**
	 * Creates a new exit in the maze
	 * @param line		vertical coordinate 
	 * @param column	horizontal coordinate
	 */
	public Exit(int line, int column){

		position = new Position(line, column);
	}

}
