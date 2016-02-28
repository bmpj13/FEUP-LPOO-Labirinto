package maze.logic;

public abstract class Object {

	protected int verPosition;
	protected int horPosition;
	
	public int getHorPos() {
		return horPosition;
	}
	public void setHorPos(int horPosition) {
		this.horPosition = horPosition;
	}
	public int getVerPos() {
		return verPosition;
	}
	public void setVerPos(int verPosition) {
		this.verPosition = verPosition;
	}

}
