package maze.logic;

import java.awt.EventQueue;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
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

/**
 * Maze.java - The class which contains all methods to play the game.
 * @author João Barbosa and William Fukunaga
 * @version 1.8
 */
public class Maze {

	public enum DIRECTION {UP, LEFT, DOWN, RIGHT, STAY};
	
	public enum DRAGON_MODE {FROZEN, RANDOM, CAN_SLEEP};


	public static final char Symbol_Wall = 'X';
	public static final char Symbol_Path = '.';
	public static final char Symbol_HeroUnarmed = 'H';
	public static final char Symbol_HeroArmed = 'A';
	public static final char Symbol_DragonActive = 'D';
	public static final char Symbol_DragonAsleep = 'd';
	public static final char Symbol_Sword = 'E';
	public static final char Symbol_Exit = 'S';
	public static final char Symbol_DragonOnSword = 'F';
	
	private char heroSymbol = Symbol_HeroUnarmed;
	private char dragonSymbol = Symbol_DragonActive;


	private DRAGON_MODE dragonMode;
	private Hero hero;
	private LinkedList<Dragon> dragonList;
	private Sword sword;
	private Exit exit;
	private char[][] maze;
	private ArrayList<MovementInfo> movementInfo;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		Interface interf = new Interface();

		int option = interf.askGameMode();

		switch(option){
		case 1:

			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						GUI gui = new GUI();
						gui.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			break;
			
		case 2:
			int[] dimension = new int[1];
			int[] numDragons = new int[1];
			DRAGON_MODE[] dm = new DRAGON_MODE[1];
			Maze maze = null;

			boolean valid = true;
			do {
				Maze.askSettings(interf, dimension, numDragons, dm);
				valid = true;

				try {
					maze = new Maze(dimension[0], numDragons[0], dm[0]);
				} catch (IllegalArgumentException e) {
					interf.display("Error ocurred: " + e.getMessage() + "\n");
					valid = false;
				}

			} while (!valid);

			maze.consolePlay(interf, maze);
			break;
			
		default:
			throw new IllegalArgumentException();
		}
	}

	private static void askSettings(Interface interf, int[] dimension, int[] numDragons, DRAGON_MODE[] dragonMode) {

		dimension[0] = interf.askMazeDimension();

		numDragons[0] = interf.askNumberDragons();

		int dm = interf.askDragonMode();

		switch (dm) {
		case 1:
			dragonMode[0] = DRAGON_MODE.FROZEN;
			break;

		case 2:
			dragonMode[0] = DRAGON_MODE.RANDOM;
			break;

		case 3:
			dragonMode[0] = DRAGON_MODE.CAN_SLEEP;
			break;

		default:
			throw new IllegalArgumentException();
		}
	}
	//TODO Play through the console

	private void consolePlay(Interface interf, Maze maze) {

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
					switch(hero.getState()){
					case WIN:
						interf.WinMsg();
						break;
					case DEAD:
						interf.LoseMsg();
						break;
					default:
						throw new IllegalArgumentException();
					}
					return;
				}
			} while (!validKey);
		}
	}

	//TODO Analyse keyboard movement input
	
	private DIRECTION key2direction(char key) throws InvalidKey {

		key = Character.toLowerCase(key);
		DIRECTION dir;
		switch(key){
		case 'w':
			dir = DIRECTION.UP;
			break;
		case 'a':
			dir = DIRECTION.LEFT;
			break;
		case 's':
			dir = DIRECTION.DOWN;
			break;
		case 'd':
			dir = DIRECTION.RIGHT;
			break;
		default:
			throw new InvalidKey(key);
		}
		return dir;
	}



	//TODO Constructors
	/**
	 * Creates a new maze
	 * <p>
	 * This constructor limits the number of dragons, the maze dimension and returns an exception
	 * if these requirements are not met.
	 *
	 * @param dimension 	maze width and height
	 * @param dragonNum 	the number of dragons to insert on the maze
	 * @param dm 		the behaviour of all dragons
	 */
	public Maze(int dimension, int dragonNum, DRAGON_MODE dm){

		if (dimension <= 4 || dimension % 2 == 0)
			throw new IllegalArgumentException("Dimension cannot be even or inferior to 5.");
		if (dragonNum >= (6*(dimension) - 28) || dragonNum <=0)
			throw new IllegalArgumentException("Can't place that many dragons! Choose another amount.");

		dragonMode = dm;

		maze = new char[dimension][dimension];
		ArrayList<Position> freePos = generateMaze(dimension);
		PlaceCharacters(freePos, dragonNum);

	}

	
	/**
	 * Creates a new maze by filling it with walls.
	 *
	 * @param dimension the dimension of the new maze
	 */
	public Maze(int dimension) {
		
		if (dimension < 5 || dimension % 2 == 0)
			throw new IllegalArgumentException("Dimension cannot be even or inferior to 5.");


		maze = new char[dimension][dimension];
		for (int i = 0; i < dimension; i++)
			Arrays.fill(maze[i], Symbol_Wall);

		dragonMode = DRAGON_MODE.CAN_SLEEP;
	}


	/**
	 * Copies a given maze.
	 *
	 * @param m maze to be used.
	 */
	public Maze(char[][] m) {

		maze = m;
		dragonMode = DRAGON_MODE.CAN_SLEEP;
		dragonList = new LinkedList<Dragon>();

		for(int i = 0; i < maze.length; i++) {
			for(int j = 0; j < maze[i].length; j++) {
				if (maze[i][j] == Symbol_DragonActive) {
					Dragon dragon = new Dragon(i, j);
					dragonList.add(dragon);
				}
				else if (maze[i][j] == Symbol_HeroUnarmed) {				
					hero = new Hero(i,j);
				}
				else if (maze[i][j] == Symbol_Sword) {
					sword = new Sword(i,j);
				}
				else if (maze[i][j] == Symbol_Exit) {
					exit = new Exit(i,j);
				}
			}
		}
		if(sword == null || hero == null)
			throw new IllegalArgumentException();
		if(!Reachable(hero.getPosition(), sword.getPosition()))
			throw new IllegalArgumentException();
		if(!Reachable(sword.getPosition(), exit.getPosition()))
			throw new IllegalArgumentException();
		for (Iterator<Dragon> iterator = dragonList.iterator(); iterator.hasNext();) {
			if(!Reachable(iterator.next().getPosition(), exit.getPosition()))
				throw new IllegalArgumentException();
		}

	}

	
	/**
	 * Imports the maze defined in class.
	 * <br> This maze was given to us in the 2nd iteration of the class.
	 */
	public Maze(){
		loadDefaultMaze();
	}

	private void loadDefaultMaze() {


		char[][] m = { 
				{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
				{'X', '.', '.', '.', '.', '.', '.', '.', '.', 'X'},
				{'X', '.', 'X', 'X', '.', 'X', '.', 'X', '.', 'X'},
				{'X', '.', 'X', 'X', '.', 'X', '.', 'X', '.', 'X'},
				{'X', '.', 'X', 'X', '.', 'X', '.', 'X', '.', 'X'},
				{'X', '.', '.', '.', '.', '.', '.', 'X', '.', 'X'},
				{'X', '.', 'X', 'X', '.', 'X', '.', 'X', '.', 'X'},
				{'X', '.', 'X', 'X', '.', 'X', '.', 'X', '.', 'X'},
				{'X', '.', 'X', 'X', '.', '.', '.', '.', '.', 'X'},
				{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'}
		};

		maze = m;

		maze[1][1] = Symbol_HeroUnarmed;
		maze[3][1] = Symbol_DragonActive;
		maze[8][1] = Symbol_Sword;
		maze[5][9] = Symbol_Exit;

		hero = new Hero(1,1);
		Dragon dragon = new Dragon(1,1);
		dragonList = new LinkedList<Dragon>();
		dragonList.add(dragon);

		sword = new Sword(1,1);
		exit = new Exit(1,1);
	}


	//TODO Maze algorithm
	//TODO Create maze 
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
			maze[0][randomPos*2 + 1] = Symbol_Exit;	
			exit = new Exit(0, randomPos*2 + 1);// set content on maze	
			currPos = new Position(0, randomPos);							// save starting position
			break;

		case 1:
			maze[dimension-1][randomPos*2 + 1] = Symbol_Exit;
			exit = new Exit(dimension-1, randomPos*2 + 1);
			currPos = new Position(visitedCells.length - 1, randomPos);
			break;

		case 2:
			maze[randomPos*2 + 1][0] = Symbol_Exit;
			exit = new Exit(randomPos*2 + 1, 0);
			currPos = new Position(randomPos, 0);
			break;

		case 3:
			maze[randomPos*2 + 1][dimension-1] = Symbol_Exit;
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
					maze[y-1][x] = Symbol_Path;
					updateAvailables(new Position(y-1, x), freePositions, walls);
					currPos.y--;
					break;

				case LEFT:
					maze[y][x-1] = Symbol_Path;
					updateAvailables(new Position(y, x-1), freePositions, walls);
					currPos.x--;
					break;

				case DOWN:
					maze[y+1][x] = Symbol_Path;
					updateAvailables(new Position(y+1, x), freePositions, walls);
					currPos.y++;
					break;

				case RIGHT:
					maze[y][x+1] = Symbol_Path;
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

	private void initMaze(ArrayList<Position> freePositions, HashSet<Position> walls) {

		for (int i = 0; i < maze.length; i++) {

			for (int j = 0; j < maze[i].length; j++) {

				if (new Position(i,j).hasOddCoords()) {
					maze[i][j] = Symbol_Path;
					freePositions.add(new Position(i, j));
				}
				else {
					maze[i][j] = Symbol_Wall;

					if (i > 0 && j > 0 && i < maze.length - 1 && j < maze.length - 1)
						walls.add(new Position(i, j));
				}
			}
		}
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

				maze[pos.y][pos.x] = Symbol_Path;
				freePositions.add(new Position(pos));
				walls2destroy--;
			}
		}
	}

	private boolean destroyable(Position pos) {

		int y = pos.y;
		int x = pos.x;

		if ( (y > 0 && maze[y-1][x] == Symbol_Wall) && 
				(y < maze.length - 1 && maze[y+1][x] == Symbol_Wall) ) {

			if ( (x > 0 && maze[y][x-1] == Symbol_Path) && 
					(x < maze.length - 1 && maze[y][x+1] == Symbol_Path) )
				return true;
		}
		else if ( (x > 0 && maze[y][x-1] == Symbol_Wall) && 
				(x < maze.length - 1 && maze[y][x+1] == Symbol_Wall) ) {

			if ( (y > 0 && maze[y-1][x] == Symbol_Path) && 
					(y < maze.length - 1 && maze[y+1][x] == Symbol_Path) ) {
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

	//TODO Place characters in maze
	private void PlaceCharacters(ArrayList<Position> freePos, int dragonNum) {

		Random rand = new Random();
		int numElem = freePos.size();
		dragonList = new LinkedList<Dragon>();

		int pos = rand.nextInt(numElem);
		hero = new Hero(freePos.get(pos));
		setMazeContent(hero.getPosition(), Symbol_HeroUnarmed);
		freePos.set(pos, freePos.get(--numElem));

		pos = rand.nextInt(numElem);
		sword = new Sword(freePos.get(pos));
		setMazeContent(sword.getPosition(), Symbol_Sword);
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

	private void visit(int i, int j, boolean [][] visited) {
		if (i < 0 || i >= maze.length || j < 0 || j >= maze.length)
			return;
		if (maze[i][j] == Symbol_Wall || visited[i][j])
			return;
		visited[i][j] = true;
		visit(i-1, j, visited);
		visit(i+1, j, visited);
		visit(i, j-1, visited);
		visit(i, j+1, visited);
	}
	
	/*private void visit(int i, int j, boolean [][] visited, Hero hero) {
		if (i < 0 || i >= maze.length || j < 0 || j >= maze.length)
			return;
		if (maze[i][j] == Symbol_Wall || visited[i][j])
			return;
		visited[i][j] = true;
		visit(i-1, j, visited);
		visit(i+1, j, visited);
		visit(i, j-1, visited);
		visit(i, j+1, visited);
	}*/
	
	
	private boolean Reachable(Position src, Position dest){
		boolean [][] visited = new boolean[maze.length] [maze.length];
		
		visit(src.y, src.x, visited);
		
		for (int i = 0; i < maze.length; i++)
			for (int j = 0; j < maze.length; j++)
				if (! visited[dest.y][dest.x] )
					return false;
		
		return true; 
	}
	
	
	//TODO update
	
	
	/**
	 * Moves the hero.
	 * <br> Also moves all the dragons if possible.
	 * <br> Settles any fights that may occur.
	 *
	 * @param direction the direction chosen for the hero 
	 * @throws EndGame if game is over
	 */
	public void update(DIRECTION direction) throws EndGame {

		movementInfo = new ArrayList<MovementInfo>();

		MovementInfoHero heroInfo = updateHero(direction);
		movementInfo.add(heroInfo);

		if (hero.getState() == HERO_STATE.WIN)
			throw new EndGame(true);

		Position swordPos = sword.getPosition();
		boolean dragonOnSword = false;
		for (Iterator<Dragon> iterator = dragonList.iterator(); iterator.hasNext();) {

			MovementInfoDragon dragonInfo;
			Dragon dragon = iterator.next();

			dragonInfo = updateDragon(dragon);
			
			movementInfo.add(dragonInfo);
			HeroVsDragon(dragon);

			if (dragon.getState() == DRAGON_STATE.DEAD) {
				dragonInfo.dragonState = DRAGON_STATE.DEAD;
				iterator.remove();
			}
			else if (hero.getState() == HERO_STATE.DEAD) {
				heroInfo.heroState = HERO_STATE.DEAD;
				throw new EndGame(false);
			}

			

			if (!hero.hasSword() && !dragonOnSword) {
				if (dragon.getPosition().equals(swordPos))
					dragonOnSword = true;
			}

		}


		if (!hero.hasSword() && !dragonOnSword)
			setMazeContent(swordPos, Symbol_Sword);
	}


	
	//TODO move characters
	/**
	 * Moves the hero in the selected direction.
	 *
	 * @param direction direction to move the hero
	 * @return information about the movement made by the hero
	 */
	public MovementInfoHero updateHero(DIRECTION direction){

		Position currHeroPos = hero.getPosition();
		MovementInfoHero heroInfo = new MovementInfoHero(currHeroPos, DIRECTION.STAY);

		Position newHeroPos = hero.getPosition();

		boolean checkMove = false;

		switch (direction) {
		case UP:
			if (maze[currHeroPos.y-1][currHeroPos.x]  != Symbol_Wall && maze[currHeroPos.y-1][currHeroPos.x] != Symbol_DragonAsleep) {
				checkMove = true;
				newHeroPos.y--;
				heroInfo.moveDirection = DIRECTION.UP;
			}
			break;

		case DOWN:
			if (maze[currHeroPos.y+1][currHeroPos.x] != Symbol_Wall && maze[currHeroPos.y+1][currHeroPos.x] != Symbol_DragonAsleep) {
				checkMove = true;
				newHeroPos.y++;
				heroInfo.moveDirection = DIRECTION.DOWN;
			}
			break;

		case LEFT:
			if (maze[currHeroPos.y][currHeroPos.x-1] != Symbol_Wall && maze[currHeroPos.y][currHeroPos.x-1] != Symbol_DragonAsleep) {
				checkMove = true;
				newHeroPos.x--;		
				heroInfo.moveDirection = DIRECTION.LEFT;
			}
			break;

		case RIGHT:
			if (maze[currHeroPos.y][currHeroPos.x+1] != Symbol_Wall && maze[currHeroPos.y][currHeroPos.x+1] != Symbol_DragonAsleep) {
				checkMove = true;
				newHeroPos.x++;
				heroInfo.moveDirection = DIRECTION.RIGHT;
			}
			break;

		default:
			break;
		}



		if (checkMove) {
			if (getMazeContent(newHeroPos) == Symbol_Path) {

				setMazeContent(currHeroPos, Symbol_Path);

				if(hero.hasSword()) {
					setMazeContent(newHeroPos, Symbol_HeroArmed);
				}
				else 
					setMazeContent(newHeroPos, Symbol_HeroUnarmed);

				hero.move(newHeroPos);
			}
			else if ( newHeroPos.equals(sword.getPosition()) ) {

				setMazeContent(currHeroPos, Symbol_Path); 

				hero.pickedSword();
				heroSymbol = Symbol_HeroArmed;
				setMazeContent(newHeroPos, heroSymbol);

				hero.move(newHeroPos);
			}
			else if (newHeroPos.equals(exit.getPosition()) && dragonList.size()==0) {

				setMazeContent(currHeroPos, Symbol_Path); 
				setMazeContent(newHeroPos, heroSymbol);
				hero.move(newHeroPos);
				hero.wins();
			}
			else
				heroInfo.moveDirection = DIRECTION.STAY;
		}


		if (hero.hasSword())
			heroInfo.hasSword = true;

		return heroInfo;
	}


	/**
	 * Evaluates possible directions the dragon may take.
	 * <br>Moves dragon in a random direction if possible.
	 *
	 * @param dragon the dragon to be analysed
	 * @return information about the movement made by the dragon
	 */
	public MovementInfoDragon updateDragon(Dragon dragon) {

		Random rand = new Random();
		Position dragonPos = dragon.getPosition();

		switch(dragonMode){
		case FROZEN:
			return new MovementInfoDragon(dragonPos, DIRECTION.STAY);
			
		case CAN_SLEEP:
			if(sword.getPosition().equals(dragonPos))
				break;
			int stateRand = rand.nextInt() % 3;
			if (stateRand == 0)
				dragon.setState(DRAGON_STATE.SLEEPING);
			else dragon.setState(DRAGON_STATE.AWAKE);
			break;
			
		default:
			break;
		
		}
		
		if (dragon.getState() == DRAGON_STATE.AWAKE) {

			DIRECTION direction[] = new DIRECTION[5];
			int counter = 0;

			if (maze[dragonPos.y+1][dragonPos.x] == Symbol_Path || 
					maze[dragonPos.y+1][dragonPos.x] == Symbol_Sword) {
				direction[counter++] = DIRECTION.DOWN;
			}

			if (maze[dragonPos.y-1][dragonPos.x] == Symbol_Path ||
					maze[dragonPos.y-1][dragonPos.x] == Symbol_Sword) {
				direction[counter++] = DIRECTION.UP;
			}

			if (maze[dragonPos.y][dragonPos.x+1] == Symbol_Path ||
					maze[dragonPos.y][dragonPos.x+1] == Symbol_Sword) {
				direction[counter++] = DIRECTION.RIGHT;
			}

			if (maze[dragonPos.y][dragonPos.x-1] == Symbol_Path || 
					maze[dragonPos.y][dragonPos.x-1] == Symbol_Sword) {
				direction[counter++] = DIRECTION.LEFT;
			}

			direction[counter++] = DIRECTION.STAY;

			DIRECTION dir = direction[rand.nextInt(counter)];
			moveDragon(dragon, dir);

			return new MovementInfoDragon(dragonPos, dir);
		}
		else {

			dragonSymbol = Symbol_DragonAsleep;
			setMazeContent(dragonPos, dragonSymbol);

			return new MovementInfoDragon(dragonPos, DIRECTION.STAY, DRAGON_STATE.SLEEPING);
		}
	}


	/**
	 * Moves dragon.
	 * <p> Also updates the contents in the maze
	 * @param dragon the dragon to be moved
	 * @param direction the direction to move the dragon
	 */
	public void moveDragon(Dragon dragon, DIRECTION direction) {

		Position dragonPos = dragon.getPosition();

		switch(direction) {

		case DOWN:
			setMazeContent(dragonPos, Symbol_Path);
			dragonPos.y++;
			break;

		case UP: 	
			setMazeContent(dragonPos, Symbol_Path);
			dragonPos.y--;
			break;

		case RIGHT:
			setMazeContent(dragonPos, Symbol_Path);
			dragonPos.x++;
			break;

		case LEFT:
			setMazeContent(dragonPos, Symbol_Path);
			dragonPos.x--;
			break;

		default:
			break;
		}

		dragon.move(dragonPos);

		if(!hero.hasSword() && sword.getPosition().equals(dragonPos))
			dragonSymbol = Symbol_DragonOnSword;
		else dragonSymbol = Symbol_DragonActive;
		setMazeContent(dragonPos, dragonSymbol);
	}


	//TODO check fight
	/**
	 * If the hero and the dragon are within fighting distance resolves the dispute
	 * <p>
	 * Hero wins if he has the sword.
	 * <br>
	 * Dragon wins if hero doesn't have the sword.
	 *
	 * @param dragon the dragon to check
	 */
	public void HeroVsDragon(Dragon dragon) {

		if (fightAvailable(dragon)) {
			if (hero.hasSword()) {

				maze[dragon.getPosition().y][dragon.getPosition().x] = Symbol_Path;
				maze[hero.getPosition().y][hero.getPosition().x] = heroSymbol;
				dragon.setState(DRAGON_STATE.DEAD);
			}
			else if (dragon.getState() == DRAGON_STATE.AWAKE) 
				hero.dies();
		}
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


	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {

		String str = "";
		for (int i = 0; i < maze.length ; i++) {
			for (int j = 0; j < maze.length; j++)
				str += maze[i][j] + " ";
			str += "\n";
		}
		return str;
	}
	
	//TODO get and set
	
	//TODO getters and setters
	
	/**
	 * Gets the dragon mode.
	 *
	 * @return the dragon mode
	 */
	public DRAGON_MODE getDragonMode() {
		return dragonMode;
	}
	
	/**
	 * Sets the dragon mode.
	 *
	 * @param dragonMode the new dragon mode
	 */
	public void setDragonMode(DRAGON_MODE dragonMode) {
		this.dragonMode = dragonMode;
	}


	/**
	 * Gets the dragon state.
	 *
	 * @param dragon the dragon
	 * @return the dragon state
	 */
	public DRAGON_STATE getDragonState(Dragon dragon) {
		return dragon.getState();
	}
	
	/**
	 * Gets the dragon list.
	 *
	 * @return the dragon list
	 */
	public LinkedList<Dragon> getDragonList(){
		return dragonList;
	}
	
	
	/**
	 * Gets the maze content.
	 *
	 * @param pos the pos
	 * @return the maze content
	 */
	public char getMazeContent(Position pos) {
		return getMazeContent(pos.y,pos.x);
	}
	
	/**
	 * Gets the maze content.
	 *
	 * @param y the y
	 * @param x the x
	 * @return the maze content
	 */
	public char getMazeContent(int y, int x) {

		if (y < 0 || x < 0 || y > maze.length -1 || x > maze.length-1)
			throw new IllegalArgumentException();

		return maze[y][x];
	}
	
	
	/**
	 * Sets the maze content.
	 *
	 * @param pos the pos
	 * @param content the content
	 */
	public void setMazeContent(Position pos, char content) {

		setMazeContent(pos.y, pos.x, content);
	}
	
	/**
	 * Sets the maze content.
	 *
	 * @param i the i
	 * @param j the j
	 * @param content the content
	 */
	public void setMazeContent(int i, int j, char content) {

		maze[i][j] = content;
	}

	
	/**
	 * Gets the hero.
	 *
	 * @return the hero
	 */
	public Hero getHero(){
		return hero;
	}
	
	/**
	 * Gets the sword position.
	 *
	 * @return the sword position
	 */
	public Position getSwordPosition() {
		return sword.getPosition();
	}
	
	/**
	 * Gets the exit position.
	 *
	 * @return the exit position
	 */
	public Position getExitPosition() {
		return exit.getPosition();
	}
	
	/**
	 * Gets the board dimension.
	 *
	 * @return the dimension
	 */
	public int getDimension() {
		return maze.length;
	}

	
	/**
	 * Picks up the sword immediately.
	 * <p>
	 * This method is used only to simplify unitary tests
	 */
	public void heroFastSwordPick() {
		hero.pickedSword();
	}

	/**
	 * Gets the movement info.
	 *
	 * @return the movement info
	 */
	public ArrayList<MovementInfo> getMovementInfo() {

		return movementInfo;
	}

	
	/**
	 * Gets the board.
	 *
	 * @return maze
	 */
	public char[][] getBoard() {
		return maze;
	}


}
