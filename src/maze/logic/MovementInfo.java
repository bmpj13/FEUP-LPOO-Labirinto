package maze.logic;

import java.io.Serializable;

import maze.logic.Maze.DIRECTION;

/**
 *  MovementInfo.class - class to represent the animation in the graphics mode
 *  @author João Barbosa and William Fukunaga
 *  @version 1.8
 */
public class MovementInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	

	public Position lastPosition;

	public DIRECTION moveDirection;


	/**
	 * Creates a new movement info.
	 * <br> The character hasn't moved this turn. 
	 *
	 * @param lastPosition the last position
	 */
	public MovementInfo(Position lastPosition) {

		this.lastPosition = lastPosition;
		this.moveDirection = DIRECTION.STAY;
	}
	

	/**
	 * Creates a new movement info.
	 *
	 * @param lastPosition the last position before the movement
	 * @param moveDirection the direction the element has moved
	 */
	public MovementInfo(Position lastPosition, DIRECTION moveDirection) {

		this.lastPosition = lastPosition;
		this.moveDirection = moveDirection;
	}
}
