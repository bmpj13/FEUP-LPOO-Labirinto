package maze.logic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Iterator;

import maze.cli.Interface;
import maze.exceptions.EndGame;
import maze.exceptions.InvalidKey;
import maze.gui.GUI;
import maze.logic.Dragon.DRAGON_STATE;
import maze.logic.Hero.HERO_STATE;



public class Maze {

	public enum DIRECTION {UP, LEFT, DOWN, RIGHT, STAY};
	public enum DRAGON_MODE {FROZEN, RANDOM, CAN_SLEEP};


	private static final char wallSymbol = 'X';
	private static final char pathSymbol = '.';

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

		int option = interf.askGameMode();

		if (option == 1) {

			GUI gui = new GUI();
			gui.setVisible(true);
		}
		else {

			int[] dimension = new int[1];
			int[] numDragons = new int[1];
			DRAGON_MODE dm = null;
			Maze maze = null;

			boolean valid = true;
			do {
				Maze.askSettings(interf, dimension, numDragons, dm);
				valid = true;

				try {
					maze = new Maze(dimension[0], numDragons[0], dm);
				} catch (IllegalArgumentException e) {
					interf.display("Error ocurred: " + e.getMessage() + "\n");
					valid = false;
				}

			} while (!valid);

			maze.consolePlay(interf, maze);
		}
	}



	public static void askSettings(Interface interf, int[] dimension, int[] numDragons, DRAGON_MODE dragonMode) {

		dimension[0] = interf.askMazeDimension();
		interf.display("\n");
		
		numDragons[0] = interf.askNumberDragons();
		interf.display("\n");

		int dm = interf.askDragonMode();
		if (dm == 1)
			dragonMode = DRAGON_MODE.FROZEN;
		else if (dm == 2)
			dragonMode = DRAGON_MODE.CAN_SLEEP;
		else
			dragonMode = DRAGON_MODE.RANDOM;
		interf.display("\n");
	}


	//TODO play
	public void consolePlay(Interface interf, Maze maze) {

		while (true) {

			interf.display(maze);

			boolean validKey = true;
			do {
				char key = interf.askDirection();
				try {

					DIRECTION dir = key2direction(key);
					validKey = true;
					maze.update(dir);

				} catch (InvalidKey e) { 
					validKey = false;
					interf.display(e);
				} catch (EndGame e) {

					interf.display(maze);
					if (hero.getState() == HERO_STATE.WIN)
						interf.WinMsg();
					else
						interf.LoseMsg();
					return;
				}

			} while (!validKey);
		}
	}


	private DIRECTION key2direction(char key) throws InvalidKey {

		key = Character.toLowerCase(key);

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
	public Maze(int dimension, int dragonNum, DRAGON_MODE dm){

		if (dimension < 5 || dimension % 2 == 0)
			throw new IllegalArgumentException("Dimension cannot be even or inferior to 5.");
		if (dragonNum > 7*dimension - 28)
			throw new IllegalArgumentException("Can't place that many dragons! Choose another amount.");

		dragonMode = dm;

		maze = new char[dimension][dimension];
		ArrayList<Position> freePos = generateMaze(dimension);
		PlaceCharacters(freePos, dragonNum);

	}

	public Maze(int dimension){
		
		int dragonNum = 1;
		if (dimension < 5 || dimension % 2 == 0)
			throw new IllegalArgumentException("Dimension cannot be even or inferior to 5.");
		
		dragonMode = DRAGON_MODE.RANDOM;

		maze = new char[dimension][dimension];
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
	private ArrayList<Position> generateMaze(int dimension) {


		Random rand = new Random();
		ArrayDeque<Position> stack = new ArrayDeque<Position>();
		boolean[][] visitedCells = new boolean[(dimension-1)/2][(dimension-1)/2];
		ArrayList<Position> freePositions = new ArrayList<Position>();
		HashSet<Position> walls = new HashSet<Position>();



		initMaze(freePositions, walls);


		int randomPos = rand.nextInt(visitedCells.length);
		Position currPos = null;

		switch (rand.nextInt(4)) {
		case 0:
			maze[0][randomPos*2 + 1] = exitSymbol;	
			exit = new Exit(0, randomPos*2 + 1);// set content on maze	
			currPos = new Position(0, randomPos);							// save starting position
			break;

		case 1:
			maze[dimension-1][randomPos*2 + 1] = exitSymbol;
			exit = new Exit(dimension-1, randomPos*2 + 1);
			currPos = new Position(visitedCells.length - 1, randomPos);
			break;

		case 2:
			maze[randomPos*2 + 1][0] = exitSymbol;
			exit = new Exit(randomPos*2 + 1, 0);
			currPos = new Position(randomPos, 0);
			break;

		case 3:
			maze[randomPos*2 + 1][dimension-1] = exitSymbol;
			exit = new Exit(randomPos*2 + 1, dimension-1);
			currPos = new Position(randomPos, visitedCells.length - 1);
			break;
		}


		visitedCells[currPos.y][currPos.x] = true;
		stack.addFirst(new Position(currPos));

		do {

			DIRECTION availableNeighbours[] = new DIRECTION[4];
			int freeNeighbours = 0;

			if (currPos.y > 0 && !visitedCells[currPos.y-1][currPos.x]) {
				availableNeighbours[freeNeighbours++] = DIRECTION.UP;
			}

			if (currPos.x > 0 && !visitedCells[currPos.y][currPos.x-1]) {
				availableNeighbours[freeNeighbours++] = DIRECTION.LEFT;
			}

			if (currPos.y < visitedCells.length - 1 && !visitedCells[currPos.y+1][currPos.x]) {
				availableNeighbours[freeNeighbours++] = DIRECTION.DOWN;
			}

			if (currPos.x < visitedCells.length - 1 && !visitedCells[currPos.y][currPos.x+1]) {
				availableNeighbours[freeNeighbours++] = DIRECTION.RIGHT;
			}


			if (freeNeighbours > 0) {

				int y = currPos.y * 2 + 1;
				int x = currPos.x * 2 + 1;

				switch (availableNeighbours[rand.nextInt(freeNeighbours)]) {

				case UP:
					maze[y-1][x] = pathSymbol;
					updateAvailables(new Position(y-1, x), freePositions, walls);
					currPos.y--;
					break;

				case LEFT:
					maze[y][x-1] = pathSymbol;
					updateAvailables(new Position(y, x-1), freePositions, walls);
					currPos.x--;
					break;

				case DOWN:
					maze[y+1][x] = pathSymbol;
					updateAvailables(new Position(y+1, x), freePositions, walls);
					currPos.y++;
					break;

				case RIGHT:
					maze[y][x+1] = pathSymbol;
					updateAvailables(new Position(y, x+1), freePositions, walls);
					currPos.x++;
					break;

				default:
					break;
				}

				stack.addFirst(new Position(currPos));
				visitedCells[currPos.y][currPos.x] = true; 
			}		
			else {

				stack.removeFirst();

				if (!stack.isEmpty())
					currPos = stack.getFirst();
			}

		} while (!stack.isEmpty());


		openWalls(freePositions, walls);
		return freePositions;
	}



	private void openWalls(ArrayList<Position> freePositions,
			HashSet<Position> walls) {

		int walls2destroy = walls.size() / 3;

		Position[] wallsArray = walls.toArray(new Position[walls.size()]);
		int numWalls = wallsArray.length;

		Random rand = new Random();

		while (walls2destroy != 0 && numWalls > 0) {

			int posIndex = rand.nextInt(numWalls);
			Position pos = wallsArray[posIndex];
			wallsArray[posIndex] = wallsArray[--numWalls];

			if (destroyable(pos)) {

				maze[pos.y][pos.x] = pathSymbol;
				freePositions.add(new Position(pos));
				walls2destroy--;
			}
		}
	}

	private boolean destroyable(Position pos) {

		int y = pos.y;
		int x = pos.x;

		if ( (y > 0 && maze[y-1][x] == wallSymbol) && 
				(y < maze.length - 1 && maze[y+1][x] == wallSymbol) ) {

			if ( (x > 0 && maze[y][x-1] == pathSymbol) && 
					(x < maze.length - 1 && maze[y][x+1] == pathSymbol) )
				return true;
		}
		else if ( (x > 0 && maze[y][x-1] == wallSymbol) && 
				(x < maze.length - 1 && maze[y][x+1] == wallSymbol) ) {

			if ( (y > 0 && maze[y-1][x] == pathSymbol) && 
					(y < maze.length - 1 && maze[y+1][x] == pathSymbol) ) {
				return true;
			}
		}

		return false;
	}

	private void updateAvailables(Position position,
			ArrayList<Position> freePositions, HashSet<Position> walls) {

		freePositions.add(position);
		walls.remove(position);
	}

	private void initMaze(ArrayList<Position> freePositions, HashSet<Position> walls) {

		for (int i = 0; i < maze.length; i++) {

			for (int j = 0; j < maze[i].length; j++) {

				if ((i % 2  != 0) && (j % 2 != 0)) {
					maze[i][j] = pathSymbol;
					freePositions.add(new Position(i, j));
				}
				else {

					maze[i][j] = wallSymbol;

					if (i > 0 && j > 0 && i < maze.length - 1 && j < maze.length - 1)
						walls.add(new Position(i, j));
				}
			}
		}
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
	public void update(DIRECTION direction) throws EndGame {

		updateHero(direction);

		if (hero.getState() == HERO_STATE.WIN)
			throw new EndGame(true);


		boolean dragonOnSword = false;
		for (Iterator<Dragon> iterator = dragonList.iterator(); iterator.hasNext();){

			Dragon dragon = iterator.next();

			HeroVsDragon(dragon);

			if (hero.getState() == HERO_STATE.DEAD)
				throw new EndGame(false);
			else if (dragon.getState() != DRAGON_STATE.DEAD) {

				if (dragonMode != DRAGON_MODE.FROZEN)
					updateDragon(dragon);

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

		return;
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
	public void updateHero(DIRECTION direction){

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


	public void updateDragon(Dragon dragon) {

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

	public char[][]getMaze(){
		return maze;
	}
	
}
