package maze.logic;

import maze.logic.Dragon.DRAGON_STATE;
import maze.logic.Maze.DIRECTION;

/**
 * MovementInfoDragon.class - Derived class that stores additional info regarding the dragon.
 * @author João Barbosa and William Fukunaga
 * @version 1.8
 */
public class MovementInfoDragon extends MovementInfo {
	
	public DRAGON_STATE dragonState;

	/**
	 * Creates a new movement info for the dragon.
	 * <p> Sets the state as "awake" by default
	 * @param lastPosition the dragon's last position
	 * @param moveDirection the direction which the dragon moved
	 */
	public MovementInfoDragon(Position lastPosition, DIRECTION moveDirection) {
		super(lastPosition, moveDirection);
		dragonState = DRAGON_STATE.AWAKE;
	}

	/**
	 * Creates a new movement info for the dragon.
	 *
	 * @param lastPosition the dragon's last position
	 * @param moveDirection the direction which the dragon moved
	 * @param dragonState the current dragon state
	 */
	public MovementInfoDragon(Position lastPosition, DIRECTION moveDirection, DRAGON_STATE dragonState) {
		super(lastPosition, moveDirection);
		this.dragonState = dragonState;
	}

}
