
package maze.logic;

/**
 * Element.java - abstract superclass that stores the position of the character/element
 * @author João Barbosa and William Fukunaga
 * @version 1.8
 */
public abstract class Element {
	
	protected Position position;
	
	/** 
	 * @return Position of the character in the maze
	 */
	public Position getPosition() {
		return new Position(position.y, position.x);
	}
}
