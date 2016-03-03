package maze.logic;
import java.util.Random;

import maze.cli.Interface;
import maze.logic.Dragon.DRAGON_STATE;
import maze.logic.Hero.HERO_STATE;



public class Maze {

	public enum DIRECTION {UP, LEFT, DOWN, RIGHT};
	public enum DRAGON_MODE {FROZEN, RANDOM, CAN_SLEEP};



	private DRAGON_MODE dragonMode;
	private Hero hero;
	private Dragon dragon;
	private Sword sword;
	private Exit exit;
	private char[][] maze;




	public static void main(String[] args) {

		Interface interf = new Interface();
		Maze maze = new Maze();


		int mode = interf.askDragonMode(); 
		if (mode == 1)
			maze.setDragonMode(DRAGON_MODE.FROZEN);
		else if (mode == 2)
			maze.setDragonMode(DRAGON_MODE.RANDOM);

		maze.play(interf, maze);
	}



	public void play(Interface interf, Maze maze) {

		while (hero.getState() == HERO_STATE.ALIVE) {

			interf.display(maze);
			char key = interf.askDirection();
			maze.update(key2direction(key));
		}

		interf.display(maze);

		if (hero.getState() == HERO_STATE.WIN)
			interf.WinMsg();
		else
			interf.LoseMsg();
	}




	private DIRECTION key2direction(char key) {
		
		if (key == 'w')
			return DIRECTION.UP;
		else if (key == 'a')
			return DIRECTION.LEFT;
		else if (key == 's')
			return DIRECTION.DOWN;
		else
			return DIRECTION.RIGHT;
	}



	public Maze() {

		loadDefaultMaze();

		hero = new Hero();
		dragon = new Dragon();
		sword = new Sword();
		exit = new Exit();
		dragonMode = DRAGON_MODE.CAN_SLEEP;
	}



	public Maze(char[][] m) {

		maze = m;
		dragonMode = DRAGON_MODE.CAN_SLEEP;

		int counter = 0;
		outer_loop:
			for(int i = 0; i < maze.length; i++) {
				for(int j = 0; j < maze[i].length; j++) {
					if (maze[i][j] == 'H') {				
						hero = new Hero(i,j);
						counter++;
					}
					else if (maze[i][j] == 'D') {
						dragon = new Dragon(i, j);
						counter++;
					}
					else if (maze[i][j] == 'E') {
						sword = new Sword(i,j);
						counter++;
					}
					else if (maze[i][j] == 'S') {
						exit = new Exit(i,j);
						counter++;
					}

					if (counter == 4)
						break outer_loop;
				}
			}
	}




	public void loadDefaultMaze() {

		maze = new char[10][10];

		// borders of the maze
		for (int i = 0; i < 10; i++) {
			maze[i][0] = 'X';
			maze[0][i] = 'X';
			maze[i][9] = 'X';
			maze[9][i] = 'X';
		}


		for (int i = 1; i < 9; i++)
			for (int j = 1; j < 9; j++) {
				if ((j == 2 || j == 3 || j == 5 || j == 7) && (i > 1 && i < 8 && i != 5))
					maze[i][j] = 'X';
				else 
					maze[i][j] = '.';
			}

		maze[5][7] = 'X';
		maze[8][2] = 'X';
		maze[8][3] = 'X';
		maze[1][1] = 'H';
		maze[3][1] = 'D';
		maze[8][1] = 'E';
		maze[5][9] = 'S';
	}




	public String toString() {

		String str = "";
		for (int i = 0; i < maze.length ; i++) {
			for (int j = 0; j < maze.length; j++)
				str += maze[i][j] + " ";
			str += "\n";
		}
		return str;
	}




	public void update(DIRECTION direction) {

		moveHero(direction);
		
		HeroVsDragon();

		if (dragon.getState() != DRAGON_STATE.DEAD) {

			if (dragonMode != DRAGON_MODE.FROZEN)
				moveDragon();


			Position swordPos = sword.getPosition();
			Position dragonPos = dragon.getPosition();


			if (!hero.hasSword()) {
				if(getMazeContent(dragonPos) == getMazeContent(swordPos))
					maze[dragonPos.y][dragonPos.x] = 'F';
				else
					maze[swordPos.y][swordPos.x] = 'E';
			}

			HeroVsDragon();
		}
	}





	private boolean fight() {

		Position heroPos = hero.getPosition();
		Position dragonPos = dragon.getPosition();

		if (heroPos.x == dragonPos.x && Math.abs(heroPos.y - dragonPos.y) <= 1)
			return true;
		else if (heroPos.y == dragonPos.y && Math.abs(heroPos.x - dragonPos.x) <= 1)
			return true;

		return false;
	}





	public void moveHero(DIRECTION direction){

		Position currHeroPos = hero.getPosition();
		Position newHeroPos = hero.getPosition();

		boolean checkMove = false;
		switch (direction) {
		case UP:
			if (maze[currHeroPos.y-1][currHeroPos.x]  != 'X' && maze[currHeroPos.y-1][currHeroPos.x] != 'd') {
				checkMove = true;
				newHeroPos.y--;
			}
			break;
		case DOWN:
			if (maze[currHeroPos.y+1][currHeroPos.x] != 'X' && maze[currHeroPos.y+1][currHeroPos.x] != 'd') {
				checkMove = true;
				newHeroPos.y++;
			}
			break;
		case LEFT:
			if (maze[currHeroPos.y][currHeroPos.x-1] != 'X' && maze[currHeroPos.y][currHeroPos.x-1] != 'd') {
				checkMove = true;
				newHeroPos.x--;		
			}
			break;
		case RIGHT:
			if (maze[currHeroPos.y][currHeroPos.x+1] != 'X' && maze[currHeroPos.y][currHeroPos.x+1] != 'd') {
				checkMove = true;
				newHeroPos.x++;
			}
			break;
		}



		if (checkMove) {
			if (getMazeContent(newHeroPos) == '.') {

				setMazeContent(currHeroPos, '.');

				if(hero.hasSword())
					setMazeContent(newHeroPos, 'A');
				else 
					setMazeContent(newHeroPos, 'H');

				hero.move(newHeroPos);
			}
			else if ( newHeroPos.equals(sword.getPosition()) ) {

				setMazeContent(currHeroPos, '.'); 
				setMazeContent(newHeroPos, 'A');

				hero.move(newHeroPos);
				hero.pickedSword();
			}
			else if (newHeroPos.equals(exit.getPosition()) && dragon.getState() == DRAGON_STATE.DEAD) {

				setMazeContent(currHeroPos, '.'); 
				setMazeContent(newHeroPos, 'A');

				hero.move(newHeroPos);
				hero.wins();
			}
		}
	}




	public void moveDragon() {

		Random rand = new Random();

		Position dragonPos = dragon.getPosition();

		DRAGON_STATE dragonState = dragon.getState();

		if (dragonState == DRAGON_STATE.AWAKE) {
			boolean dragonMove = false;;
			do {
				int posRand = rand.nextInt() % 5;

				if (dragonPos.y == 8 && dragonPos.x == 1)
					posRand = 1;

				if (posRand == 0) {

					if (maze[dragonPos.y+1][dragonPos.x] != 'X' && maze[dragonPos.y+1][dragonPos.x] != 'S') {

						dragonMove = true;
						setMazeContent(dragonPos, '.');
						dragonPos.y++;
					}
				}
				else if (posRand == 1) {

					if (maze[dragonPos.y-1][dragonPos.x] != 'X' && maze[dragonPos.y-1][dragonPos.x] != 'S') {

						dragonMove = true;
						setMazeContent(dragonPos, '.');
						dragonPos.y--;
					}
				}
				else if (posRand == 2) {

					if (maze[dragonPos.y][dragonPos.x+1] != 'X' && maze[dragonPos.y][dragonPos.x+1] != 'S') {

						dragonMove = true;
						setMazeContent(dragonPos, '.');
						dragonPos.x++;
					}
				}
				else if (posRand == 3) {

					if (maze[dragonPos.y][dragonPos.x-1] != 'X' && maze[dragonPos.y+1][dragonPos.x-1] != 'S') {

						dragonMove = true;
						setMazeContent(dragonPos, '.');
						dragonPos.x--;
					}
				}				
				else if(posRand == 4)
					dragonMove = true;

			} while(dragonMove == false);
		}					

		dragon.move(dragonPos);

		if(dragonMode == DRAGON_MODE.CAN_SLEEP){

			int stateRand = rand.nextInt() % 3;
			if (stateRand == 0) {
				dragon.sleep();
				dragonState = DRAGON_STATE.SLEEPING;
			}
			else {
				dragon.wakeUp();
				dragonState = DRAGON_STATE.AWAKE;
			}
		}

		if (dragonState == DRAGON_STATE.AWAKE)
			maze[dragonPos.y][dragonPos.x] = 'D';
		else if (dragonState == DRAGON_STATE.SLEEPING)
			maze[dragonPos.y][dragonPos.x] = 'd';
	}




	public void HeroVsDragon() {

		if (fight()) {

			if (hero.hasSword()) {

				maze[dragon.getPosition().y][dragon.getPosition().x] = '.';
				maze[hero.getPosition().y][hero.getPosition().x] = 'A';
				dragon.dies();
			}
			else if (dragon.getState() == DRAGON_STATE.AWAKE) 
				hero.dies();
		}
	}



	public DRAGON_MODE getDragonMode() {
		return dragonMode;
	}


	public void setDragonMode(DRAGON_MODE dragonMode) {
		this.dragonMode = dragonMode;
	}




	public char getMazeContent(Position pos) {

		return maze[pos.y][pos.x];
	}


	public void setMazeContent(Position pos, char content) {

		maze[pos.y][pos.x] = content;
	}



	
	
	
	
	
	
	public Position getHeroPosition() {
		
		return hero.getPosition();
	}



	public HERO_STATE getHeroState() {
		
		return hero.getState();
	}



	public Position getSwordPosition() {
		
		return sword.getPosition();
	}



	public boolean heroHasSword() {
		
		return hero.hasSword();
	}



	public void heroFastSwordPick() {
		
		hero.pickedSword();
	}



	public DRAGON_STATE getDragonState() {
		
		return dragon.getState();
	}



	public Position getExitPosition() {

		return exit.getPosition();
	}



	public void setDragonAsleep() {
		
		dragon.sleep();
	}

}
