package maze.logic;

import java.io.Serializable;

import maze.logic.Hero.HERO_STATE;
import maze.logic.Maze.DIRECTION;

/**
 * MovementInfoHero.class - Derived class that stores additional info regarding the hero.
 * @author João Barbosa and William Fukunaga
 * @version 1.8
 */
public class MovementInfoHero extends MovementInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	

	public boolean hasSword;
	
	public HERO_STATE heroState;

	/**
	 * Creates a new movement info for the hero.
	 * <br> Admits that the hero hasn't picked the sword.
	 *
	 * @param lastPosition the hero's last position
	 * @param moveDirection the direction which the hero moved
	 */
	public MovementInfoHero(Position lastPosition, DIRECTION moveDirection) {
		super(lastPosition, moveDirection);

		hasSword = false;
		heroState = HERO_STATE.ALIVE;
	}
	
	/**
	 * Creates a new movement info for the hero.
	 *
	 * @param lastPosition the hero's last position
	 * @param moveDirection the direction which the hero moved
	 * @param hasSword if the hero has the sword already
	 */
	public MovementInfoHero(Position lastPosition, DIRECTION moveDirection, boolean hasSword) {
		super(lastPosition, moveDirection);

		this.hasSword = hasSword;
		heroState = HERO_STATE.ALIVE;
	}

}
