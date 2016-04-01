package utilities;


public class Position implements Comparable<Position> {

	public int y;
	public int x;

	public Position(int y, int x) {

		this.y = y;
		this.x = x;
	}

	public Position(Position pos) {

		this.x = pos.x;
		this.y = pos.y;
	}


	boolean hasOddCoords() {

		return ((x % 2 != 0) && (y % 2 != 0));
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}



	public String toString() {

		return y + "  " + x;
	}

	@Override
	public int compareTo(Position o) {
		if(y < o.y)
			return -1;
		else if(y> o.y)
			return 1;
		return 0;
		
	}



}
