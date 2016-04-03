package maze.logic;

import maze.logic.Maze.DIRECTION;

public class MovementInfo {

	public Position lastPosition;
	public DIRECTION moveDirection;


	public MovementInfo(Position lastPosition) {

		this.lastPosition = lastPosition;
		this.moveDirection = DIRECTION.STAY;
	}
	

	public MovementInfo(Position lastPosition, DIRECTION moveDirection) {

		this.lastPosition = lastPosition;
		this.moveDirection = moveDirection;
	}
}
