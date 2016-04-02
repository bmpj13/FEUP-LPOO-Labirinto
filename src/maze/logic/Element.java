package maze.logic;

//import utilities.Position;

public abstract class Element {
	
	protected Position position;
	
	public Position getPosition() {
		return new Position(position.y, position.x);
	}
}
