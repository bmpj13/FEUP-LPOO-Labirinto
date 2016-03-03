package maze.logic;

public abstract class Element {
	
	protected Position position;
	
	public Position getPosition() {
		return new Position(position.y, position.x);
	}
}
