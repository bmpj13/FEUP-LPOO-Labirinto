package maze.logic;

import maze.logic.Dragon.DRAGON_STATE;
import maze.logic.Maze.DIRECTION;

public class MovementInfoDragon extends MovementInfo {
	
	public DRAGON_STATE dragonState;

	public MovementInfoDragon(Position lastPosition, DIRECTION moveDirection) {
		super(lastPosition, moveDirection);
		dragonState = DRAGON_STATE.AWAKE;
	}

	public MovementInfoDragon(Position lastPosition, DIRECTION moveDirection, DRAGON_STATE dragonState) {
		super(lastPosition, moveDirection);
		this.dragonState = dragonState;
	}

}
