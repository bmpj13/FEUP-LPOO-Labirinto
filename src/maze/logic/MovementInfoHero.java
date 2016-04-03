package maze.logic;

import maze.logic.Hero.HERO_STATE;
import maze.logic.Maze.DIRECTION;

public class MovementInfoHero extends MovementInfo {

	public boolean hasSword;
	public HERO_STATE heroState;

	public MovementInfoHero(Position lastPosition, DIRECTION moveDirection) {
		super(lastPosition, moveDirection);

		hasSword = false;
		heroState = HERO_STATE.ALIVE;
	}
	
	
	
	public MovementInfoHero(Position lastPosition, DIRECTION moveDirection, boolean hasSword) {
		super(lastPosition, moveDirection);

		this.hasSword = hasSword;
		heroState = HERO_STATE.ALIVE;
	}


	public MovementInfoHero(Position lastPosition) {
		super(lastPosition);

		hasSword = false;
		heroState = HERO_STATE.ALIVE;
	}
}
