package maze.logic;

public class Exit extends Element {

	public Exit() {

		position = new Position(5,9);
	}


	public Exit(int line, int column){

		position = new Position(line, column);
	}
}