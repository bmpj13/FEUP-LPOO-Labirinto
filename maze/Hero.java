package maze;

public class Hero {

	public enum HERO_STATE {ALIVE, DEAD, WIN};
	
	private int verPosition;
	private int horPosition;
	private boolean hasSword;
	private HERO_STATE heroState;
	
	public Hero() {
		verPosition = 1;
		horPosition = 1;
		hasSword = false;
		heroState = HERO_STATE.ALIVE;
	}
	
	
	public void move(int x, int y) {
		verPosition = x;
		horPosition = y;
	}
	
	
	public void pickedSword() {
		hasSword = true;
	}
	
	public void dies() {
		heroState = HERO_STATE.DEAD;
	}
	
	
	public int getX() { return verPosition; }
	public int getY() { return horPosition; }
	public boolean hasSword() { return hasSword; }
	public HERO_STATE getState() { return heroState; }
}
