package maze.logic;

public class Sword extends Element {

	public Sword() {

		position = new Position(8,1);
	}


	public Sword(int line, int column){
		
		position = new Position(line, column);
	}
}
