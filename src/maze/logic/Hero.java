package maze.logic;

import java.io.Serializable;

/**
 *  Hero.java - Element of maze controlled by the user.
 *  @author João Barbosa and William Fukunaga
 *  @version 1.8
 */
public class Hero extends Element implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum HERO_STATE {ALIVE, DEAD, WIN};
	private boolean hasSword;
	private HERO_STATE heroState;
	
	/**
	 * Creates a new hero given the coordinates.
	 *
	 * @param line vertical coordinate
	 * @param column horizontal coordinate
	 */
	public Hero(int line, int column){
		
		position = new Position(line, column);
		hasSword = false;
		heroState = HERO_STATE.ALIVE;
	}
	
	/**
	 * Creates a new hero given the position.
	 *
	 * @param pos position to be set
	 */
	public Hero(Position pos){
		
		position = pos;
		hasSword = false;
		heroState = HERO_STATE.ALIVE;
	}
		
	/**
	 * Moves the hero to the selected position.
	 *
	 * @param pos new hero position
	 */
	public void move(Position pos) {
		
		position.y = pos.y;
		position.x = pos.x;
	}
	
	
	/**
	 * Picked sword.
	 * <p> Hero overlapped with the sword position in the maze.
	 */
	public void pickedSword() {
		hasSword = true;
	}
	
	
	/**
	 * Hero killed in action.
	 */
	public void dies() {
		heroState = HERO_STATE.DEAD;
	}
	
	
	/**
	 * Reached the exit after all the dragons were killed.
	 */
	public void wins() {
		heroState = HERO_STATE.WIN;
	}
	
	
	/**
	 * Checks if the hero already picked the sword.
	 *
	 * @return true, if he has the sword; false if he doesn't have
	 */
	public boolean hasSword() { return hasSword; }
	
	/**
	 * Gets the hero condition.
	 *
	 * @return hero state
	 */
	public HERO_STATE getState() { return heroState; }
}
