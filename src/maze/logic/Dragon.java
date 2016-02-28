package maze.logic;


public class Dragon extends Object {
	
	public enum DRAGON_STATE {AWAKE, SLEEPING, DEAD};
	
	private DRAGON_STATE dragonState;
	
	public Dragon() {
		verPosition = 3;
		horPosition = 1;
		dragonState = DRAGON_STATE.AWAKE;
	}
	
	public void move(int y, int x) {
		verPosition = y;
		horPosition = x;
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
	
	public DRAGON_STATE getState() { return dragonState; }
}
