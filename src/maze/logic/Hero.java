package maze.logic;

public class Hero extends Object {

	public enum HERO_STATE {ALIVE, DEAD, WIN};
	private boolean hasSword;
	private HERO_STATE heroState;
	
	public Hero() {
		verPosition = 1;
		horPosition = 1;
		hasSword = false;
		heroState = HERO_STATE.ALIVE;
	}
	public Hero(int line, int column){
		verPosition = line;
		horPosition = column;
		hasSword = false;
		heroState = HERO_STATE.ALIVE;
	}
	
	public void move(int y, int x) {
		verPosition = y;
		horPosition = x;
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
