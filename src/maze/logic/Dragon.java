package maze.logic;


public class Dragon extends Object {
	
	public enum DRAGON_STATE {AWAKE, SLEEPING, DEAD};
	public enum DRAGON_MODE {FROZEN, RANDOM, CAN_SLEEP};
	
	private DRAGON_STATE dragonState;
	private DRAGON_MODE dragonMode;
	
	public Dragon(DRAGON_MODE dm) {
		verPosition = 3;
		horPosition = 1;
		dragonState = DRAGON_STATE.AWAKE;
		dragonMode = dm;
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

	public DRAGON_MODE getMode() {
		return dragonMode;
	}
}
