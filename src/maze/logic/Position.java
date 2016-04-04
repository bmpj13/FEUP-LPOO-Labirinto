package maze.logic;


/**
 * Position.java - simple class which stores the coordinates in the maze
 * @author João Barbosa and William Fukunaga
 * @version 1.8
 *
 */
public class Position implements Comparable<Position> {

	public int y;
	public int x;

	/**
	 * Creates a coordinate given the coordinates
	 * @param y	vertical coordinate
	 * @param x horizontal coordinate
	 */
	public Position(int y, int x) {

		this.y = y;
		this.x = x;
	}

		
	/**
	 * Creates a coordinate given a position
	 * @param pos	position to be set
	 */
	public Position(Position pos) {

		this.x = pos.x;
		this.y = pos.y;
	}


	/**
	 * Checks if the x and y variables are odd
	 * @return	true if both are odd; else returns false
	 */
	public boolean hasOddCoords() {

		return ((x % 2 != 0) && (y % 2 != 0));
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {

		return y + "  " + x;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Position o) {
		if(y < o.y)
			return -1;
		else if(y> o.y)
			return 1;
		return 0;
		
	}



}
