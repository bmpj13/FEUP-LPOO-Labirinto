package maze.logic;


public class Position {
	
	public int y;
	public int x;
	
	
	public Position() {
		
		y = 0;
		x = 0;
	}
	
	public Position(int y, int x) {
		
		this.y = y;
		this.x = x;
	}
	
	
	public boolean equals(Position other) {
		
		if (this.x == other.x && this.y == other.y)
			return true;
		
		return false;
	}
}
