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



	public Position(Position pos) {

		this.x = pos.x;
		this.y = pos.y;
	}



	@Override
	public boolean equals(Object obj) {

		if (this.getClass() != obj.getClass())
			return false;


		Position other = (Position) obj;
		return (this.x == other.x && this.y == other.y);
	}

	public String toString() {

		return y + "  " + x;
	}

	@Override
	public int hashCode() {

		return y + x;
	}

}
