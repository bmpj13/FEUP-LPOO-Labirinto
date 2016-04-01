package maze.logic;

import utilities.Position;

public class Hero extends Element {

	public enum HERO_STATE {ALIVE, DEAD, WIN};
	private boolean hasSword;
	private HERO_STATE heroState;
	
	public Hero() {
		
		position = new Position(1,1);
		hasSword = false;
		heroState = HERO_STATE.ALIVE;
	}
	
	
	public Hero(int line, int column){
		
		position = new Position(line, column);
		hasSword = false;
		heroState = HERO_STATE.ALIVE;
	}
	public Hero(Position pos){
		
		position = pos;
		hasSword = false;
		heroState = HERO_STATE.ALIVE;
	}
		
	public void move(Position pos) {
		
		position.y = pos.y;
		position.x = pos.x;
	}
	
	
	public void pickedSword() {
		hasSword = true;
	}
	
	
	public void dies() {
		heroState = HERO_STATE.DEAD;
	}
	
	
	public void wins() {
		heroState = HERO_STATE.WIN;
	}
	
	
	public boolean hasSword() { return hasSword; }
	
	public HERO_STATE getState() { return heroState; }
}
