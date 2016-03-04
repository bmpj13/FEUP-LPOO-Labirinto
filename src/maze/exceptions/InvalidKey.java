package maze.exceptions;


public class InvalidKey extends Exception {
	private static final long serialVersionUID = 1L;
	private char key;

	public InvalidKey(char key) {
		this.key = key;
	}

	public char getKey() {
		return key;
	}
	
	public String toString() {
		
		return "Invalid key: " + key;
	}
};
