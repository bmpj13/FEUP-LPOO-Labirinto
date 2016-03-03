package maze.logic;

public abstract class Object {
	
	protected Position position;
	
	public Position getPosition() {
		return new Position(position.y, position.x);
	}
}
