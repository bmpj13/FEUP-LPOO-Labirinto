package maze;


public class Dragon {
	
	public enum DRAGON_STATE {ALIVE, DEAD};
	
	private int verPosition;
	private int horPosition;
	private DRAGON_STATE dragonState;
	
	public Dragon() {
		verPosition = 3;
		horPosition = 1;
		dragonState = DRAGON_STATE.ALIVE;
	}
	
	
	public void move(int y, int x) {
		verPosition = y;
		horPosition = x;
	}
	
	
	public void dies() {
		dragonState = DRAGON_STATE.DEAD;
	}
	
	
	public int getVerPos() { return verPosition; }
	public int getHorPos() { return horPosition; }
	public DRAGON_STATE getState() { return dragonState; }
}
