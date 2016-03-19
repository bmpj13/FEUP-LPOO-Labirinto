package maze.logic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Iterator;

import maze.cli.Interface;
import maze.exceptions.InvalidKey;
import maze.logic.Dragon.DRAGON_STATE;
import maze.logic.Hero.HERO_STATE;



public class Maze {

	public enum DIRECTION {UP, LEFT, DOWN, RIGHT, STAY};
	public enum DRAGON_MODE {FROZEN, RANDOM, CAN_SLEEP};


	private static final char wallSymbol = 'X';
	private static final char pathSymbol = ' ';

	private char heroSymbol = 'H';
	private char dragonSymbol = 'D';
	private static final char swordSymbol = 'E';
	private static final char exitSymbol = 'S';
	private static final char dragonOnSwordSymbol = 'F';


	private DRAGON_MODE dragonMode;
	private Hero hero;
	private LinkedList<Dragon> dragonList;
	private Sword sword;
	private Exit exit;
	private char[][] maze;

	//TODO main
	public static void main(String[] args) {

		Interface interf = new Interface();
	/*	int mode = interf.askDragonMode(); 


		DRAGON_MODE dm;
		if (mode == 1) {
			dm = DRAGON_MODE.FROZEN;
		}
		else if (mode == 2)
			dm = DRAGON_MODE.RANDOM;
		else
			dm = DRAGON_MODE.CAN_SLEEP;
*/
		
		DRAGON_MODE dm= DRAGON_MODE.RANDOM;
		//Maze maze = new Maze(dm);
		//interf.display(maze);
		//maze.play(interf, maze);
	}

	//TODO play
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

	//TODO constructor
	public Maze(int dimension, int dragonNum, DRAGON_MODE dm) {


		if (dm == DRAGON_MODE.FROZEN) {
			dragonSymbol = 'd';
			dragonMode = dm;
		}
		else if (dm == DRAGON_MODE.RANDOM)
			dragonMode = dm;
		else 
			dragonMode = dm;


		ArrayList<Position> freePos = generateMaze(dimension);
		PlaceCharacters(freePos, dragonNum);

	}

	public Maze(char[][] m) {

		maze = m;
		dragonMode = DRAGON_MODE.CAN_SLEEP;
		dragonList = new LinkedList<Dragon>();

		for(int i = 0; i < maze.length; i++) {
			for(int j = 0; j < maze[i].length; j++) {
				if (maze[i][j] == dragonSymbol) {
					Dragon dragon = new Dragon(i, j);
					dragonList.add(dragon);
				}
				else if (maze[i][j] == heroSymbol) {				
					hero = new Hero(i,j);
				}
				else if (maze[i][j] == swordSymbol) {
					sword = new Sword(i,j);
				}
				else if (maze[i][j] == exitSymbol) {
					exit = new Exit(i,j);
				}
			}
		}
	}

	public Maze(){
		loadDefaultMaze();
	}

	public void loadDefaultMaze() {


		char[][] m = { 
				{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
				{'X', ' ', '.', '.', '.', '.', '.', '.', '.', 'X'},
				{'X', '.', 'X', 'X', '.', 'X', '.', 'X', '.', 'X'},
				{'X', ' ', 'X', 'X', '.', 'X', '.', 'X', '.', 'X'},
				{'X', '.', 'X', 'X', '.', 'X', '.', 'X', '.', 'X'},
				{'X', '.', '.', '.', '.', '.', '.', 'X', '.', ' '},
				{'X', '.', 'X', 'X', '.', 'X', '.', 'X', '.', 'X'},
				{'X', '.', 'X', 'X', '.', 'X', '.', 'X', '.', 'X'},
				{'X', ' ', 'X', 'X', '.', '.', '.', '.', '.', 'X'},
				{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'}
		};

		maze = m;

		maze[1][1] = heroSymbol;
		maze[3][1] = dragonSymbol;
		maze[8][1] = swordSymbol;
		maze[5][9] = exitSymbol;

		hero = new Hero();
		Dragon dragon = new Dragon();
		dragonList = new LinkedList<Dragon>();
		dragonList.add(dragon);

		sword = new Sword();
		exit = new Exit();
	}



	//TODO generate
	public ArrayList<Position> generateMaze(int dimension) {

		ArrayList<Position> freePos = new ArrayList<Position>();		// Position that are free
		maze = new char[dimension][dimension];
		ArrayDeque<Position> stack = new ArrayDeque<Position>();		// Store visited flag's position



		startMazeGen(dimension, stack, freePos);


		// Position exit on the maze and step to adjacent position

		Random rand = new Random();
		int randomPos = rand.nextInt(dimension - 2) + 1;
		Position currPos = null;

		switch (rand.nextInt(4)) {
		case 0:
			maze[0][randomPos] = exitSymbol;						// set content on maze	
			exit = new Exit(0,randomPos);
			currPos = new Position(1, randomPos);			// save starting position
			break;

		case 1:
			maze[dimension-1][randomPos] = exitSymbol;
			exit = new Exit(dimension-1,randomPos);
			currPos = new Position(dimension-2, randomPos);
			break;

		case 2:
			maze[randomPos][0] = exitSymbol;
			exit = new Exit(randomPos, 0);
			currPos = new Position(randomPos, 1);
			break;

		case 3:
			maze[randomPos][dimension-1] = exitSymbol;
			exit = new Exit(randomPos, dimension-1);
			currPos = new Position(randomPos, dimension-2);
			break;
		}


		setMazeContent(currPos, pathSymbol);
		stack.addFirst(currPos);
		freePos.add(new Position(currPos));



		// Pick a neighbour and stack it

		do {

			// Examine current cell's neighbours

			DIRECTION availableNeighbours[] = new DIRECTION[4];
			int freeNeighbours = 0;


			// up neighbour
			if (currPos.y > 1 && maze[currPos.y-1][currPos.x] == wallSymbol 
					&& canCarve(dimension, DIRECTION.UP, currPos))	
				availableNeighbours[freeNeighbours++] = DIRECTION.UP;


			// left neighbour
			if (currPos.x > 1 && maze[currPos.y][currPos.x-1] == wallSymbol 
					&& canCarve(dimension, DIRECTION.LEFT, currPos))
				availableNeighbours[freeNeighbours++] = DIRECTION.LEFT;


			// down neighbour
			if (currPos.y < dimension - 2 && maze[currPos.y+1][currPos.x] == wallSymbol
					&& canCarve(dimension, DIRECTION.DOWN, currPos))
				availableNeighbours[freeNeighbours++] = DIRECTION.DOWN;


			// right neighbour
			if (currPos.x < dimension - 2 && maze[currPos.y][currPos.x+1] == wallSymbol 
					&& canCarve(dimension, DIRECTION.RIGHT, currPos))
				availableNeighbours[freeNeighbours++] = DIRECTION.RIGHT;


			if (freeNeighbours > 0) {

				switch (availableNeighbours[rand.nextInt(freeNeighbours)]) {

				case UP:
					currPos.y--;
					break;

				case LEFT:
					currPos.x--;
					break;

				case DOWN:
					currPos.y++;
					break;

				case RIGHT:
					currPos.x++;
					break;
				
				default:
					break;
				}


				setMazeContent(currPos, pathSymbol);
				stack.addFirst(new Position(currPos));
				freePos.add(new Position (currPos));
			}
			else {
				stack.removeFirst();

				if (!stack.isEmpty())
					currPos = stack.getFirst();
			}

		} while (!stack.isEmpty());


		return freePos;
	}

	public void startMazeGen(int dimension, ArrayDeque<Position> stack, ArrayList<Position> freePos) {

		for (int i = 0; i < dimension; i++)
			for (int j = 0; j < dimension; j++) {

				if ( (i % 2 != 0) && (j % 2 != 0)
						&& (i < dimension - 1) && (j < dimension - 1) ) {

					maze[i][j] = pathSymbol;
					stack.addFirst(new Position(i,j));
					freePos.add(new Position(i,j));
				}
				else 
					maze[i][j] = wallSymbol;
			}
	}

	public boolean canCarve(int dimension, DIRECTION dir, Position currPos) {

		int x = currPos.x;
		int y = currPos.y;

		switch (dir) {

		case UP:

			if (x > 1) {
				if (maze[y][x-1] == pathSymbol && maze[y-1][x-1] == pathSymbol)
					return false;
				else if (maze[y-2][x] == pathSymbol && maze[y-2][x-1] == pathSymbol && maze[y-1][x-1] == pathSymbol)
					return false;
				else if (maze[y-1][x-1] == wallSymbol && maze[y-2][x-1] == pathSymbol && maze[y-2][x] == wallSymbol)
					return false;
			}

			if (x < dimension - 1) {

				if (maze[y][x+1] == pathSymbol && maze[y-1][x+1] == pathSymbol)
					return false;
				if (maze[y-2][x] == pathSymbol && maze[y-2][x+1] == pathSymbol && maze[y-1][x+1] == pathSymbol)
					return false;
				else if (maze[y-1][x+1] == wallSymbol && maze[y-2][x+1] == pathSymbol && maze[y-2][x] == wallSymbol)
					return false;
			}
			break;

		case LEFT:

			if (y > 1) {

				if (maze[y-1][x] == pathSymbol && maze[y-1][x-1] == pathSymbol)
					return false;
				else if (maze[y][x-2] == pathSymbol && maze[y-1][x-2] == pathSymbol && maze[y-1][x-1] == pathSymbol)
					return false;
				else if (maze[y-1][x-1] == wallSymbol && maze[y-1][x-2] == pathSymbol && maze[y][x-2] == wallSymbol)
					return false;
			}

			if (y < dimension - 1) {

				if (maze[y+1][x] == pathSymbol && maze[y+1][x-1] == pathSymbol)
					return false;
				else if (maze[y][x-2] == pathSymbol && maze[y+1][x-2] == pathSymbol && maze[y+1][x-1] == pathSymbol)
					return false;
				else if (maze[y+1][x-1] == wallSymbol && maze[y+1][x-2] == pathSymbol && maze[y][x-2] == wallSymbol)
					return false;
			}
			break;

		case DOWN:

			if ((dimension % 2 == 0) && currPos.hasOddCoords() && (y + 1 == dimension - 2) && maze[y-1][x] == wallSymbol)
				return false;


			if (x > 1) {
				if (maze[y][x-1] == pathSymbol && maze[y+1][x-1] == pathSymbol)
					return false;
				else if (maze[y+2][x] == pathSymbol && maze[y+2][x-1] == pathSymbol && maze[y+1][x-1] == pathSymbol)
					return false;
				else if (maze[y+1][x-1] == wallSymbol && maze[y+2][x-1] == pathSymbol && maze[y+2][x] == wallSymbol)
					return false;
			}

			if (x < dimension - 1) {

				if (maze[y][x+1] == pathSymbol && maze[y+1][x+1] == pathSymbol)
					return false;
				if (maze[y+2][x] == pathSymbol && maze[y+2][x+1] == pathSymbol && maze[y+1][x+1] == pathSymbol)
					return false;
				else if (maze[y+1][x+1] == wallSymbol && maze[y+2][x+1] == pathSymbol && maze[y+2][x] == wallSymbol)
					return false;
			}
			break;


		case RIGHT:

			if ((dimension % 2 == 0) && currPos.hasOddCoords() && (x + 1 == dimension - 2) && maze[y][x-1] == wallSymbol)
				return false;


			if (y > 1) {

				if (maze[y-1][x] == pathSymbol && maze[y-1][x+1] == pathSymbol)
					return false;
				else if (maze[y][x+2] == pathSymbol && maze[y-1][x+2] == pathSymbol && maze[y-1][x+1] == pathSymbol)
					return false;
				else if (maze[y-1][x+1] == wallSymbol && maze[y-1][x+2] == pathSymbol && maze[y][x+2] == wallSymbol)
					return false;
			}

			if (y < dimension - 1) {

				if (maze[y+1][x] == pathSymbol && maze[y+1][x+1] == pathSymbol)
					return false;
				else if (maze[y][x+2] == pathSymbol && maze[y+1][x+2] == pathSymbol && maze[y+1][x+1] == pathSymbol)
					return false;
				else if (maze[y+1][x+1] == wallSymbol && maze[y+1][x+2] == pathSymbol && maze[y][x+2] == wallSymbol)
					return false;
			}
			break;
			
		case STAY:
			break;
		}


		return true;
	}



	//TODO Place characters
	public void PlaceCharacters(ArrayList<Position> freePos, int dragonNum){
		Random rand = new Random();
		int numElem = freePos.size();
		dragonList = new LinkedList<Dragon>();

		int pos = rand.nextInt(numElem);
		hero = new Hero(freePos.get(pos));
		setMazeContent(hero.getPosition(), heroSymbol);
		freePos.set(pos, freePos.get(--numElem));

		pos = rand.nextInt(numElem);
		sword = new Sword(freePos.get(pos));
		setMazeContent(sword.getPosition(), swordSymbol);
		freePos.set(pos, freePos.get(--numElem));

		for(int i = 0; i < dragonNum; i++){
			Dragon dragon;
			do {

				pos = rand.nextInt(numElem);
				dragon = new Dragon(freePos.get(pos));
				freePos.set(pos, freePos.get(--numElem));
			} while(fightAvailable(dragon));

			setMazeContent(dragon.getPosition(), dragonSymbol);
			dragonList.add(dragon);

		}

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
	public HERO_STATE update(DIRECTION direction) {

		moveHero(direction);
		
		if (hero.getState() == HERO_STATE.WIN)
			return HERO_STATE.WIN;
		
		
		boolean dragonOnSword = false;
		for (Iterator<Dragon> iterator = dragonList.iterator(); iterator.hasNext();){

			Dragon dragon = iterator.next();

			HeroVsDragon(dragon);

			if (hero.getState() == HERO_STATE.DEAD)
				return HERO_STATE.DEAD;
			else if (dragon.getState() != DRAGON_STATE.DEAD) {

				if (dragonMode != DRAGON_MODE.FROZEN)
					moveDragon(dragon);

				Position dragonPos = dragon.getPosition();
				Position swordPos = sword.getPosition();


				if (!hero.hasSword() && !dragonOnSword) {

					if (getMazeContent(dragonPos) == getMazeContent(swordPos)){
						maze[dragonPos.y][dragonPos.x] = dragonOnSwordSymbol;
						dragonOnSword = true;
					}
					else
						maze[swordPos.y][swordPos.x] = swordSymbol;
				}

				HeroVsDragon(dragon);
			}
			else iterator.remove();
		}
		
		return HERO_STATE.ALIVE;
	}

	private boolean fightAvailable(Dragon dragon) {

		Position heroPos = hero.getPosition();
		Position dragonPos = dragon.getPosition();

		if (heroPos.x == dragonPos.x && Math.abs(heroPos.y - dragonPos.y) <= 1)
			return true;
		else if (heroPos.y == dragonPos.y && Math.abs(heroPos.x - dragonPos.x) <= 1)
			return true;

		return false;
	}


	//TODO move
	public void moveHero(DIRECTION direction){

		Position currHeroPos = hero.getPosition();
		Position newHeroPos = hero.getPosition();

		boolean checkMove = false;
		switch (direction) {
		case UP:
			if (maze[currHeroPos.y-1][currHeroPos.x]  != wallSymbol && maze[currHeroPos.y-1][currHeroPos.x] != 'd') {
				checkMove = true;
				newHeroPos.y--;
			}
			break;
		case DOWN:
			if (maze[currHeroPos.y+1][currHeroPos.x] != wallSymbol && maze[currHeroPos.y+1][currHeroPos.x] != 'd') {
				checkMove = true;
				newHeroPos.y++;
			}
			break;
		case LEFT:
			if (maze[currHeroPos.y][currHeroPos.x-1] != wallSymbol && maze[currHeroPos.y][currHeroPos.x-1] != 'd') {
				checkMove = true;
				newHeroPos.x--;		
			}
			break;
		case RIGHT:
			if (maze[currHeroPos.y][currHeroPos.x+1] != wallSymbol && maze[currHeroPos.y][currHeroPos.x+1] != 'd') {
				checkMove = true;
				newHeroPos.x++;
			}
			break;
		default:
			break;
		}



		if (checkMove) {
			if (getMazeContent(newHeroPos) == pathSymbol) {

				setMazeContent(currHeroPos, pathSymbol);

				if(hero.hasSword())
					setMazeContent(newHeroPos, heroSymbol);
				else 
					setMazeContent(newHeroPos, heroSymbol);

				hero.move(newHeroPos);
			}
			else if ( newHeroPos.equals(sword.getPosition()) ) {

				setMazeContent(currHeroPos, pathSymbol); 

				hero.pickedSword();
				heroSymbol = 'A';
				setMazeContent(newHeroPos, heroSymbol);

				hero.move(newHeroPos);
			}
			else if (newHeroPos.equals(exit.getPosition()) && dragonList.size()==0) {

				setMazeContent(currHeroPos, pathSymbol); 
				setMazeContent(newHeroPos, heroSymbol);

				hero.move(newHeroPos);
				hero.wins();
			}
		}
	}

	public void moveDragon(Dragon dragon) {

		Random rand = new Random();

		Position dragonPos = dragon.getPosition();

		DRAGON_STATE dragonState = dragon.getState();

		if (dragonState == DRAGON_STATE.AWAKE) {

			DIRECTION direction[] = new DIRECTION[5];
			int counter = 0;

			if (maze[dragonPos.y+1][dragonPos.x] == pathSymbol || 
					maze[dragonPos.y+1][dragonPos.x] == swordSymbol) {
				direction[counter++] = DIRECTION.DOWN;
			}

			if (maze[dragonPos.y-1][dragonPos.x] == pathSymbol ||
					maze[dragonPos.y-1][dragonPos.x] == swordSymbol) {
				direction[counter++] = DIRECTION.UP;
			}

			if (maze[dragonPos.y][dragonPos.x+1] == pathSymbol ||
					maze[dragonPos.y][dragonPos.x+1] == swordSymbol) {
				direction[counter++] = DIRECTION.RIGHT;
			}

			if (maze[dragonPos.y][dragonPos.x-1] == pathSymbol || 
					maze[dragonPos.y+1][dragonPos.x-1] == swordSymbol) {
				direction[counter++] = DIRECTION.LEFT;
			}

			direction[counter++] = DIRECTION.STAY;

			switch(direction[rand.nextInt(counter)]){

			case DOWN:
				setMazeContent(dragonPos, pathSymbol);
				dragonPos.y++;
				break;

			case UP: 	
				setMazeContent(dragonPos, pathSymbol);
				dragonPos.y--;
				break;

			case RIGHT:
				setMazeContent(dragonPos, pathSymbol);
				dragonPos.x++;
				break;
				
			case LEFT:
				setMazeContent(dragonPos, pathSymbol);
				dragonPos.x--;
				break;
			
			case STAY:
				break;
			}

	}					

	dragon.move(dragonPos);

	if (dragonMode == DRAGON_MODE.CAN_SLEEP){

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
		dragonSymbol = 'D';
	else if (dragonState == DRAGON_STATE.SLEEPING)
		dragonSymbol = 'd';


	setMazeContent(dragonPos, dragonSymbol);
}


//TODO HeroVsDragon
public void HeroVsDragon(Dragon dragon) {

	if (fightAvailable(dragon)) {
		if (hero.hasSword()) {

			maze[dragon.getPosition().y][dragon.getPosition().x] = pathSymbol;
			maze[hero.getPosition().y][hero.getPosition().x] = heroSymbol;
			dragon.dies();
			//dragonList.remove(dragon);
		}
		else if (dragon.getState() == DRAGON_STATE.AWAKE) 
			hero.dies();
	}
}



//TODO get and set
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

public DRAGON_STATE getDragonState(Dragon dragon) {

	return dragon.getState();
}

public Position getExitPosition() {

	return exit.getPosition();
}

public void setDragonAsleep(Dragon dragon) {

	dragon.sleep();
}

public LinkedList<Dragon> getDragonListCopy(){
	return (LinkedList<Dragon>) dragonList.clone();
}

public LinkedList<Dragon> getDragonList(){
	return dragonList;
}

}
