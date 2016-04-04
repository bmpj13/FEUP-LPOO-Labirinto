package maze.test;
import static org.junit.Assert.*;

import java.util.ArrayList;
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
			{'X', 'X', 'X', 'S', 'X',},
			{'X', 'D', 'X', 'X', 'X',},
			{'X', 'X', 'X', 'H', 'X',},
			{'X', 'E', 'X', 'X', 'X',},
			{'X', 'X', 'X', 'X', 'X',}
	};

	@Test
	public void testMoveHero() {
		Maze maze = new Maze(m1);

		assertEquals(new Position(1, 3), maze.getHero().getPosition());

		// Move to free cell
		maze.updateHero(DIRECTION.LEFT);
		assertEquals(new Position(1, 2), maze.getHero().getPosition());


		// Move unsuccessfully against wall
		maze.updateHero(DIRECTION.UP);
		assertEquals(new Position(1,2), maze.getHero().getPosition());

	}

	@Test
	public void testDragonMove() {
		Maze maze2 = new Maze (m2);
		assertEquals(DRAGON_MODE.CAN_SLEEP, maze2.getDragonMode());
		Dragon dragon2 = maze2.getDragonList().getFirst();
		maze2.updateDragon(dragon2);

		assertEquals(dragon2.getPosition(), new Position(1,1));

		Maze maze1 = new Maze (m1);
		Dragon dragon = maze1.getDragonList().getFirst();
		maze1.moveDragon(dragon, DIRECTION.UP);
		assertEquals(dragon.getPosition(), new Position(2,3));

		maze1.moveDragon(dragon, DIRECTION.DOWN);
		assertEquals(dragon.getPosition(), new Position(3,3));

		maze1.moveDragon(dragon, DIRECTION.LEFT);
		assertEquals(dragon.getPosition(), new Position(3,2));

		maze1.moveDragon(dragon, DIRECTION.RIGHT);
		assertEquals(dragon.getPosition(), new Position(3,3));
	}

	@Test
	public void testPosition(){

		Maze maze = new Maze(m1);

		assertEquals(5,maze.getDimension());

		assertEquals(new Position(1,3), maze.getHero().getPosition());
		assertEquals(new Position(1,4), maze.getExitPosition());
		assertEquals(new Position(3,1), maze.getSwordPosition());

		assertTrue(maze.getHero().getPosition().hasOddCoords());
		assertFalse(new Position(1,2).hasOddCoords());
		assertFalse(new Position(2,1).hasOddCoords());

		assertEquals(0, new Position(1,1).compareTo(new Position(1,1)));
		assertEquals(1, new Position(2,1).compareTo(new Position(1,1)));
		assertEquals(-1, new Position(1,1).compareTo(new Position(2,1)));

		assertEquals('X',maze.getMazeContent(new Position(0,0)));
		assertEquals('X',maze.getMazeContent(new Position(maze.getDimension()-1,maze.getDimension()-1)));
		LinkedList<Dragon> dragonList = maze.getDragonList();
		assertEquals(1, dragonList.size());
	}

	@Test
	public void testHeroPicksSword() {
		Maze maze = new Maze(m1);
		maze.setDragonMode(DRAGON_MODE.FROZEN);
		assertEquals(new Position(3,1), maze.getSwordPosition());
		try{
			for(int i=0; i< maze.getDimension();i++)
				maze.update(DIRECTION.LEFT);

			assertEquals('H', maze.getMazeContent(maze.getHero().getPosition()));
			assertEquals(new Position(1,1), maze.getHero().getPosition());

			for(int i=0; i< maze.getDimension();i++)
				maze.update(DIRECTION.DOWN);
		}
		catch(EndGame e){
			fail("Didn't expect exception");
		}
		assertEquals(maze.getSwordPosition(), maze.getHero().getPosition());
		assertEquals('A', maze.getMazeContent(maze.getHero().getPosition()));
		//assertEquals(true, maze.heroHasSword());

		try {
			maze.update(DIRECTION.UP);
		} catch (EndGame e) {
			fail("Didn't expect exception");		}
		assertEquals('.', maze.getMazeContent(maze.getSwordPosition()));
		assertEquals('A', maze.getMazeContent(maze.getHero().getPosition()));
	}

	@Test 
	public void testHeroDies() {
		Maze maze = new Maze(m1);

		assertEquals(HERO_STATE.ALIVE, maze.getHero().getState());

		LinkedList<Dragon> dragonList = maze.getDragonList();
		maze.updateHero(DIRECTION.DOWN);
		maze.HeroVsDragon(dragonList.getFirst());

		assertEquals(HERO_STATE.DEAD, maze.getHero().getState());
	}


	@Test
	public void testDragonDies() throws EndGame {
		Maze maze = new Maze(m1);
		maze.setDragonMode(DRAGON_MODE.FROZEN);
		maze.heroFastSwordPick();
		maze.update(DIRECTION.DOWN);

		ArrayList<MovementInfo> movementInfo = maze.getMovementInfo();
		LinkedList<Dragon> dragonList = maze.getDragonList();

		assertEquals(0, dragonList.size());
		assertNotEquals(0, movementInfo.size());
		assertEquals(HERO_STATE.ALIVE, maze.getHero().getState());
	}

	@Test
	public void testHeroWins() {
		Maze maze = new Maze(m1);

		maze.heroFastSwordPick();

		try {
			maze.setDragonMode(DRAGON_MODE.FROZEN);
			maze.update(DIRECTION.DOWN);
		} catch (EndGame e) {
			fail("Didn't expect exception");
		}
		LinkedList<Dragon> dragonList = maze.getDragonList();
		//maze.HeroVsDragon(dragonList.getFirst());
		assertTrue(dragonList.isEmpty());
		//		dragonList.removeFirst();

		maze.updateHero(DIRECTION.UP);
		maze.updateHero(DIRECTION.RIGHT);



		assertEquals(maze.getHero().getPosition(), maze.getExitPosition());
		assertEquals(HERO_STATE.WIN, maze.getHero().getState());
	}


	@Test 
	public void testHeroToExitUnsuccessfully() {
		Maze maze = new Maze(m1);

		// Dragon isn't killed in this test

		// Without sword
		maze.updateHero(DIRECTION.RIGHT);
		assertEquals(new Position(1,3), maze.getHero().getPosition());


		// With sword
		maze.heroFastSwordPick();
		maze.updateHero(DIRECTION.RIGHT);
		assertEquals(new Position(1,3), maze.getHero().getPosition());
	}


	@Test
	public void testHeroUnharmed() {

		Maze maze = new Maze(m1);

		LinkedList<Dragon> dragonList = maze.getDragonList();
		assertEquals(1, dragonList.size());
		dragonList.getFirst().setState(DRAGON_STATE.SLEEPING);

		maze.updateHero(DIRECTION.DOWN);
		maze.HeroVsDragon(dragonList.getFirst());

		assertEquals(DRAGON_STATE.SLEEPING, maze.getDragonState(dragonList.getFirst()));
		assertEquals(HERO_STATE.ALIVE, maze.getHero().getState());
	}

	@Test
	public void testDragonOnSword(){
		Maze m = new Maze(m1);
		Dragon dragon = m.getDragonList().getFirst();
		Position pos = new Position(dragon.getPosition());
		m.moveDragon(dragon, DIRECTION.LEFT);
		assertEquals('.', m.getMazeContent(pos));
		m.moveDragon(dragon, DIRECTION.LEFT);
		assertSame('F', m.getMazeContent(dragon.getPosition()));

		dragon.setState(DRAGON_STATE.SLEEPING);


		m.setDragonMode(DRAGON_MODE.FROZEN);
		try {
			m.update(DIRECTION.DOWN);
			assertSame('F', m.getMazeContent(m.getSwordPosition()));
			assertEquals(new Position(2,3), m.getHero().getPosition());

			m.moveDragon(dragon, DIRECTION.RIGHT);
			m.update(DIRECTION.RIGHT);
			assertSame('E', m.getMazeContent(m.getSwordPosition()));

		} catch (EndGame e) {
			fail("Didn't expect exception");
		}

	}

	@Test
	public void defaultMaze(){
		Maze maze = new Maze();

		String heroPos = maze.getHero().getPosition().toString();

		assertEquals("1  1", heroPos);

		Position pos = new Position (2 ,2);

		assertFalse(pos.equals(3));
		assertFalse(pos.equals(new Position(3,2)));

	}

	@Test
	public void testMazeBuilder(){
		//TestMazeBuilder t = new TestMazeBuilder();
		//t.testRandomMazeGenerator();

		Maze m = new Maze(5);
		String s = "X X X X X \n"
				+ "X X X X X \n"
				+ "X X X X X \n"
				+ "X X X X X \n"
				+ "X X X X X \n";
		assertEquals(s , m.toString());
	}

	@Test
	public void testIllegalArgumentException(){
		try{
			Maze maze = new Maze(6,2,DRAGON_MODE.FROZEN);
			fail("expected exception");
		}
		catch(IllegalArgumentException i){
			assertSame(IllegalArgumentException.class, i.getClass());
		}
		try{
			Maze maze = new Maze(6);
			fail("expected exception");
		}
		catch(IllegalArgumentException i){
			assertSame(IllegalArgumentException.class, i.getClass());
		}

		try{
			Maze maze = new Maze(5,2,DRAGON_MODE.FROZEN);
			fail("expected exception");
		}
		catch(IllegalArgumentException i){
			assertSame(IllegalArgumentException.class, i.getClass());
		}
		try{
			Maze maze = new Maze(5,0,DRAGON_MODE.FROZEN);
			fail("expected exception");
		}
		catch(IllegalArgumentException i){
			assertSame(IllegalArgumentException.class, i.getClass());
		}
		
		Maze m = new Maze(m1);
		try{
			m.getMazeContent(new Position(5,4));
			fail("expected exception");
		}
		catch(IllegalArgumentException i){
			assertSame(IllegalArgumentException.class, i.getClass());
		}
		try{
			m.getMazeContent(new Position(4,5));
			fail("expected exception");
		}
		catch(IllegalArgumentException i){
			assertSame(IllegalArgumentException.class, i.getClass());
		}
	}
}
