package maze.exceptions;

public class EndGame extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean win;
	
	public EndGame(boolean win) {
		this.win = win;
	}

	public boolean Won() {
		return win;
	}
}
