package maze.logic;

import utilities.Position;

public class Sword extends Element {

	public Sword() {

		position = new Position(8,1);
	}


	public Sword(int line, int column){
		
		position = new Position(line, column);
	}
	
	public Sword(Position pos){	
		position = pos;
	}
}
