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
	
	
	public int getVerPos() { return verPosition; }
	public int getHorPos() { return horPosition; }
	public boolean hasSword() { return hasSword; }
	public HERO_STATE getState() { return heroState; }
}
