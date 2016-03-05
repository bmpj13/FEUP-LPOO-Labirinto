package maze.logic;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import maze.cli.Interface;
import maze.exceptions.InvalidKey;
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
		int mode = interf.askDragonMode(); 
		
		Maze maze = new Maze();
		if (mode == 1)
			maze.setDragonMode(DRAGON_MODE.FROZEN);
		else if (mode == 2)
			maze.setDragonMode(DRAGON_MODE.RANDOM);



		interf.display(maze);

		maze.play(interf, maze);
	}



	public void play(Interface interf, Maze maze) {

		while (hero.getState() == HERO_STATE.ALIVE) {

			interf.display(maze);


			boolean validKey = true;
			do {
				char key = interf.askDirection();
				try {	
					DIRECTION dir = key2direction(key);
					maze.update(dir);
					validKey = true;
				} catch (InvalidKey e) { 
					validKey = false;
					interf.display(e);
				}

			} while (!validKey);
		}


		interf.display(maze);
		if (hero.getState() == HERO_STATE.WIN)
			interf.WinMsg();
		else
			interf.LoseMsg();
	}




	private DIRECTION key2direction(char key) throws InvalidKey {

		if (key == 'w')
			return DIRECTION.UP;
		else if (key == 'a')
			return DIRECTION.LEFT;
		else if (key == 's')
			return DIRECTION.DOWN;
		else if (key == 'd')
			return DIRECTION.RIGHT;

		throw new InvalidKey(key);
	}



	public Maze() {

		HashMap<Integer,Position> freePos = generateMaze(5);
		PlaceCharacters(freePos);

		//		loadDefaultMaze();
		//
		//		hero = new Hero();
		//		dragon = new Dragon();
		//		sword = new Sword();
		//		exit = new Exit();
		//		dragonMode = DRAGON_MODE.CAN_SLEEP;
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

		//maze = new char[10][10];

		char[][] m = { 
				{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
				{'X', 'H', '.', '.', '.', '.', '.', '.', '.', 'X'},
				{'X', '.', 'X', 'X', '.', 'X', '.', 'X', '.', 'X'},
				{'X', 'D', 'X', 'X', '.', 'X', '.', 'X', '.', 'X'},
				{'X', '.', 'X', 'X', '.', 'X', '.', 'X', '.', 'X'},
				{'X', '.', '.', '.', '.', '.', '.', 'X', '.', 'S'},
				{'X', '.', 'X', 'X', '.', 'X', '.', 'X', '.', 'X'},
				{'X', '.', 'X', 'X', '.', 'X', '.', 'X', '.', 'X'},
				{'X', 'E', 'X', 'X', '.', '.', '.', '.', '.', 'X'},
				{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'}
		};

		maze = m;

		// borders of the maze
		//		for (int i = 0; i < 10; i++) {
		//			maze[i][0] = 'X';
		//			maze[0][i] = 'X';
		//			maze[i][9] = 'X';
		//			maze[9][i] = 'X';
		//		}
		//
		//
		//		for (int i = 1; i < 9; i++)
		//			for (int j = 1; j < 9; j++) {
		//				if ((j == 2 || j == 3 || j == 5 || j == 7) && (i > 1 && i < 8 && i != 5))
		//					maze[i][j] = 'X';
		//				else 
		//					maze[i][j] = '.';
		//			}
		//
		//		maze[5][7] = 'X';
		//		maze[8][2] = 'X';
		//		maze[8][3] = 'X';
		//		maze[1][1] = 'H';
		//		maze[3][1] = 'D';
		//		maze[8][1] = 'E';
		//		maze[5][9] = 'S';
	}



	//TODO generate
	public HashMap<Integer,Position> generateMaze(int dimension) {

		HashMap<Integer,Position> freePos = new HashMap<Integer,Position>();
		int hashIndex=0;
		maze = new char[dimension][dimension];
		boolean[][] cells = new boolean[dimension][dimension];  // Visited flags
		LinkedList<Position> stack = new LinkedList<Position>();
		Random rand = new Random();

		for (int i = 0; i < dimension; i++)
			for (int j = 0; j < dimension; j++) {

				if ( (i % 2 != 0) && (j % 2 != 0) 
						&& (i < dimension - 1) && (j < dimension - 1) ) {
					maze[i][j]='.';
					cells[i][j]=true;
					stack.addFirst(new Position(i,j));
					freePos.put(hashIndex++,new Position(i,j));
				}
				else maze[i][j] = 'X';
			}

		// Position exit on the maze and step to adjacent position

		int randomPos = rand.nextInt(dimension - 2) + 1;
		Position currPos = null;

		switch (rand.nextInt(4)) {
		case 0:
			maze[0][randomPos] = 'S';						// set content on maze	
			exit = new Exit(0,randomPos);
			currPos = new Position(1, randomPos);			// save starting position
			break;

		case 1:
			maze[dimension-1][randomPos] = 'S';
			exit = new Exit(dimension-1,randomPos);
			currPos = new Position(dimension-2, randomPos);
			break;

		case 2:
			maze[randomPos][0] = 'S';
			exit = new Exit(randomPos, 0);
			currPos = new Position(randomPos, 1);
			break;

		case 3:
			maze[randomPos][dimension-1] = 'S';
			exit = new Exit(randomPos, dimension-1);
			currPos = new Position(randomPos, dimension-2);
			break;
		}


		maze[currPos.y][currPos.x] = '.';
		cells[currPos.y][currPos.x] = true;

		stack.addFirst(currPos);
		freePos.put(hashIndex++,new Position(currPos));




		// Pick a neighbour and stack it

		do {


			// Examine current cell's neighbours

			DIRECTION availableNeighbours[] = new DIRECTION[4];
			int freeNeighbours = 0;


			// up neighbour
			if (currPos.y > 1 && !cells[currPos.y-1][currPos.x] && canCarve(dimension, DIRECTION.UP, currPos, cells))	
				availableNeighbours[freeNeighbours++] = DIRECTION.UP;


			// left neighbour
			if (currPos.x > 1 && !cells[currPos.y][currPos.x-1] && canCarve(dimension, DIRECTION.LEFT, currPos, cells))
				availableNeighbours[freeNeighbours++] = DIRECTION.LEFT;


			// down neighbour
			if (currPos.y < dimension - 2 && !cells[currPos.y+1][currPos.x] && canCarve(dimension, DIRECTION.DOWN, currPos, cells))
				availableNeighbours[freeNeighbours++] = DIRECTION.DOWN;


			// right neighbour
			if (currPos.x < dimension - 2 && !cells[currPos.y][currPos.x+1] && canCarve(dimension, DIRECTION.RIGHT, currPos, cells))
				availableNeighbours[freeNeighbours++] = DIRECTION.RIGHT;


			if (freeNeighbours > 0) {

				switch (availableNeighbours[rand.nextInt(freeNeighbours)]) {

				case UP:
					currPos.y--;
					maze[currPos.y][currPos.x] = '.';
					cells[currPos.y][currPos.x] = true;
					break;

				case LEFT:
					currPos.x--;
					maze[currPos.y][currPos.x] = '.';
					cells[currPos.y][currPos.x] = true;
					break;

				case DOWN:
					currPos.y++;
					maze[currPos.y][currPos.x] = '.';
					cells[currPos.y][currPos.x] = true;
					break;

				case RIGHT:
					currPos.x++;
					maze[currPos.y][currPos.x] = '.';
					cells[currPos.y][currPos.x] = true;
					break;
				}

				stack.addFirst(new Position(currPos));
				Position p = new Position (currPos);
				freePos.put(hashIndex++,p);
			}
			else {
				stack.removeFirst();

				if (!stack.isEmpty())
					currPos = stack.getFirst();
			}

		} while (!stack.isEmpty());


		return freePos;
	}



	public boolean canCarve(int dimension, DIRECTION dir, Position currPos, boolean[][] cells) {

		int x = currPos.x;
		int y = currPos.y;

		switch (dir) {

		case UP:

			if (x > 1) {
				if (cells[y][x-1] && cells[y-1][x-1])
					return false;
				else if (cells[y-2][x] && cells[y-2][x-1] && cells[y-1][x-1])
					return false;
				else if (!cells[y-1][x-1] && cells[y-2][x-1] && !cells[y-2][x])
					return false;
			}

			if (x < dimension - 1) {

				if (cells[y][x+1] && cells[y-1][x+1])
					return false;
				if (cells[y-2][x] && cells[y-2][x+1] && cells[y-1][x+1])
					return false;
				else if (!cells[y-1][x+1] && cells[y-2][x+1] && !cells[y-2][x])
					return false;
			}
			break;

		case LEFT:

			if (y > 1) {

				if (cells[y-1][x] && cells[y-1][x-1])
					return false;
				else if (cells[y][x-2] && cells[y-1][x-2] && cells[y-1][x-1])
					return false;
				else if (!cells[y-1][x-1] && cells[y-1][x-2] && !cells[y][x-2])
					return false;
			}

			if (y < dimension - 1) {

				if (cells[y+1][x] && cells[y+1][x-1])
					return false;
				else if (cells[y][x-2] && cells[y+1][x-2] && cells[y+1][x-1])
					return false;
				else if (!cells[y+1][x-1] && cells[y+1][x-2] && !cells[y][x-2])
					return false;
			}
			break;

		case DOWN:

			if (x > 1) {
				if (cells[y][x-1] && cells[y+1][x-1])
					return false;
				else if (cells[y+2][x] && cells[y+2][x-1] && cells[y+1][x-1])
					return false;
				else if (!cells[y+1][x-1] && cells[y+2][x-1] && !cells[y+2][x])
					return false;
			}

			if (x < dimension - 1) {

				if (cells[y][x+1] && cells[y+1][x+1])
					return false;
				if (cells[y+2][x] && cells[y+2][x+1] && cells[y+1][x+1])
					return false;
				else if (!cells[y+1][x+1] && cells[y+2][x+1] && !cells[y+2][x])
					return false;
			}
			break;


		case RIGHT:

			if (y > 1) {

				if (cells[y-1][x] && cells[y-1][x+1])
					return false;
				else if (cells[y][x+2] && cells[y-1][x+2] && cells[y-1][x+1])
					return false;
				else if (!cells[y-1][x+1] && cells[y-1][x+2] && !cells[y][x+2])
					return false;
			}

			if (y < dimension - 1) {

				if (cells[y+1][x] && cells[y+1][x+1])
					return false;
				else if (cells[y][x+2] && cells[y+1][x+2] && cells[y+1][x+1])
					return false;
				else if (!cells[y+1][x+1] && cells[y+1][x+2] && !cells[y][x+2])
					return false;
			}
			break;
		}


		return true;
	}

	//TODO Place characters
	public void PlaceCharacters(HashMap<Integer,Position> freePos){
		Random rand = new Random();
		int numElem = freePos.size();
		int pos = rand.nextInt(numElem);

		hero = new Hero(new Position(freePos.get(pos)));
		setMazeContent(hero.getPosition(),'H');
		freePos.put(pos,freePos.get(--numElem));

		pos = rand.nextInt(numElem);

		sword = new Sword(new Position(freePos.get(pos)));
		setMazeContent(sword.getPosition(),'E');
		freePos.put(pos,freePos.get(--numElem));

		do{
			pos = rand.nextInt(numElem);

			dragon = new Dragon(new Position(freePos.get(pos)));
			freePos.put(pos,freePos.get(--numElem));
		}while(fightAvailable());
		setMazeContent(dragon.getPosition(),'D');

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



	//TODO update
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





	private boolean fightAvailable() {

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



	//TODO moveDragon
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

		if (fightAvailable()) {

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
