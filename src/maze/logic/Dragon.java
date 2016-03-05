package maze.logic;


public class Dragon extends Element {

	public enum DRAGON_STATE {AWAKE, SLEEPING, DEAD};

	private DRAGON_STATE dragonState;


	public Dragon() {

		position = new Position(3,1);
		dragonState = DRAGON_STATE.AWAKE;
	}

	
	
	public Dragon(int line, int column) {

		position = new Position(line, column);
		dragonState = DRAGON_STATE.AWAKE;
	}
	
	
	public Dragon(Position pos){	
		position = pos;
		dragonState = DRAGON_STATE.AWAKE;
	}

	public void move(int y, int x) {
		position.y = y;
		position.x = x;
	}


	public void move(Position pos) {

		position.y = pos.y;
		position.x = pos.x;
	}


	public void wakeUp() {
		dragonState = DRAGON_STATE.AWAKE;
	}



	public void sleep() {
		dragonState = DRAGON_STATE.SLEEPING;
	}


	public void dies() {
		dragonState = DRAGON_STATE.DEAD;
	}


	public DRAGON_STATE getState() { 
		return dragonState; 
	}
}
