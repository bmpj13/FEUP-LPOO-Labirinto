package maze.test;
import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

import maze.exceptions.EndGame;
import maze.logic.*;
import maze.logic.Dragon.DRAGON_STATE;
import maze.logic.Hero.HERO_STATE;
import maze.logic.Maze.DIRECTION;
import maze.logic.Maze.DRAGON_MODE;


public class MazeTest {


	char [][] m1 = {{'X', 'X', 'X', 'X', 'X'},
			{'X', '.', '.', 'H', 'S'},
			{'X', '.', 'X', '.', 'X'},
			{'X', 'E', '.', 'D', 'X'},
			{'X', 'X', 'X', 'X', 'X'}};

	char [][] m2 = {
			{ 'X', 'X', 'X',},
			{'X', 'D', 'X',},
			{'X', 'X', 'X'}
	};


	@Test
	public void testMoveHero() {
		Maze maze = new Maze(m1);

		assertEquals(new Position(1, 3), maze.getHeroPosition());

		// Move to free cell
		maze.moveHero(DIRECTION.LEFT);
		assertEquals(new Position(1, 2), maze.getHeroPosition());


		// Move unsuccessfully against wall
		maze.moveHero(DIRECTION.UP);
		assertEquals(new Position(1,2), maze.getHeroPosition());

	}

	@Test
	public void testDragonMove() {
		Maze maze = new Maze (m2);
		Dragon dragon = maze.getDragonList().getFirst();
		maze.moveDragon(dragon);
		
		assertEquals(dragon.getPosition(), new Position(1,1));

	}

	@Test
	public void testPosition(){

		Maze maze = new Maze(m1);


		assertEquals(new Position(1,3), maze.getHeroPosition());
		assertEquals(new Position(1,4), maze.getExitPosition());
		assertEquals(new Position(3,1), maze.getSwordPosition());

		LinkedList<Dragon> dragonList = maze.getDragonList();
		assertEquals(1, dragonList.size());
	}

	@Test
	public void testHeroPicksSword() {
		Maze maze = new Maze(m1);

		assertEquals(new Position(3,1), maze.getSwordPosition());

		while (maze.getHeroPosition().x != 1)
			maze.moveHero(DIRECTION.LEFT);

		while (maze.getHeroPosition().y != 3)
			maze.moveHero(DIRECTION.DOWN);

		assertEquals(maze.getSwordPosition(), maze.getHeroPosition());
		assertEquals(true, maze.heroHasSword());

		maze.moveHero(DIRECTION.UP);
		assertEquals('.', maze.getMazeContent(maze.getSwordPosition()));
		assertEquals('A', maze.getMazeContent(maze.getHeroPosition()));
	}



	@Test 
	public void testHeroDies() {
		Maze maze = new Maze(m1);

		assertEquals(HERO_STATE.ALIVE, maze.getHeroState());

		LinkedList<Dragon> dragonList = maze.getDragonList();
		maze.moveHero(DIRECTION.DOWN);
		maze.HeroVsDragon(dragonList.getFirst());

		assertEquals(HERO_STATE.DEAD, maze.getHeroState());
	}


	@Test
	public void testDragonDies() throws EndGame {
		Maze maze = new Maze(m1);

		maze.heroFastSwordPick();
		maze.update(DIRECTION.DOWN);

		LinkedList<Dragon> dragonList = maze.getDragonList();
		//		maze.HeroVsDragon(dragonList.getFirst());

		//		assertEquals(DRAGON_STATE.DEAD, maze.getDragonState(dragonList.getFirst()));
		assertEquals(0, dragonList.size());
		assertEquals(HERO_STATE.ALIVE, maze.getHeroState());
	}



	@Test
	public void testHeroWins() {
		Maze maze = new Maze(m1);

		maze.heroFastSwordPick();
		maze.moveHero(DIRECTION.DOWN);

		LinkedList<Dragon> dragonList = maze.getDragonList();
		maze.HeroVsDragon(dragonList.getFirst());
		assertEquals(DRAGON_STATE.DEAD, dragonList.getFirst().getState());
		dragonList.removeFirst();

		maze.moveHero(DIRECTION.UP);
		maze.moveHero(DIRECTION.RIGHT);


		assertEquals(maze.getHeroPosition(), maze.getExitPosition());
		assertEquals(HERO_STATE.WIN, maze.getHeroState());
	}


	@Test 
	public void testHeroToExitUnsuccessfully() {
		Maze maze = new Maze(m1);

		// Dragon isn't killed in this test

		// Without sword
		maze.moveHero(DIRECTION.RIGHT);
		assertEquals(new Position(1,3), maze.getHeroPosition());


		// With sword
		maze.heroFastSwordPick();
		maze.moveHero(DIRECTION.RIGHT);
		assertEquals(new Position(1,3), maze.getHeroPosition());
	}


	@Test
	public void testHeroUnharmed() {

		Maze maze = new Maze(DRAGON_MODE.CAN_SLEEP);

		LinkedList<Dragon> dragonList = maze.getDragonList();
		maze.setDragonAsleep(dragonList.getFirst());

		maze.moveHero(DIRECTION.DOWN);
		maze.HeroVsDragon(dragonList.getFirst());

		assertEquals(DRAGON_STATE.SLEEPING, maze.getDragonState(dragonList.getFirst()));
		assertEquals(HERO_STATE.ALIVE, maze.getHeroState());
	}

	@Test
	public void defaultMaze(){
		Maze maze = new Maze();

		String m = maze.toString();
		String heroPos = maze.getHeroPosition().toString();

		assertEquals("1  1", heroPos);

		Position pos = new Position (2 ,2);

		assertFalse(pos.equals(3));
		
		assertFalse(pos.equals(new Position(3,2)));

	}
	
	@Test
	public void generateMaze(){
		Maze m0 = new Maze(DRAGON_MODE.FROZEN);
		Maze m1 = new Maze(DRAGON_MODE.CAN_SLEEP);
		Maze m2 = new Maze(DRAGON_MODE.RANDOM);

		
		
		
	}
}
