package maze.test;
import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.LinkedList;

import org.junit.Test;

import junit.framework.Assert;
import maze.exceptions.EndGame;
import maze.exceptions.InvalidKey;
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

		assertEquals(new Position(1, 3), maze.getHeroPosition());

		// Move to free cell
		maze.updateHero(DIRECTION.LEFT);
		assertEquals(new Position(1, 2), maze.getHeroPosition());


		// Move unsuccessfully against wall
		maze.updateHero(DIRECTION.UP);
		assertEquals(new Position(1,2), maze.getHeroPosition());

	}

	@Test
	public void testDragonMove() {
		Maze maze1 = new Maze (m2);
		Dragon dragon = maze1.getDragonList().getFirst();
		maze1.updateDragon(dragon);
		
		assertEquals(dragon.getPosition(), new Position(1,1));

		
		
	}

	@Test
	public void testPosition(){

		Maze maze = new Maze(m1);


		assertEquals(new Position(1,3), maze.getHero().getPosition());
		assertEquals(new Position(1,4), maze.getExitPosition());
		assertEquals(new Position(3,1), maze.getSwordPosition());
		
		assertTrue(maze.getHeroPosition().hasOddCoords());
		assertFalse(new Position(1,2).hasOddCoords());
		assertFalse(new Position(2,1).hasOddCoords());
				
		assertEquals(0, new Position(1,1).compareTo(new Position(1,1)));
		assertEquals(1, new Position(2,1).compareTo(new Position(1,1)));
		assertEquals(-1, new Position(1,1).compareTo(new Position(2,1)));

		LinkedList<Dragon> dragonList = maze.getDragonList();
		assertEquals(1, dragonList.size());
	}

	@Test
	public void testHeroPicksSword() {
		Maze maze = new Maze(m1);

		assertEquals(new Position(3,1), maze.getSwordPosition());

		while (maze.getHeroPosition().x != 1)
			maze.updateHero(DIRECTION.LEFT);

		while (maze.getHeroPosition().y != 3)
			maze.updateHero(DIRECTION.DOWN);

		assertEquals(maze.getSwordPosition(), maze.getHeroPosition());
		assertEquals(true, maze.heroHasSword());

		maze.updateHero(DIRECTION.UP);
		assertEquals('.', maze.getMazeContent(maze.getSwordPosition()));
		assertEquals('A', maze.getMazeContent(maze.getHeroPosition()));
	}

	@Test 
	public void testHeroDies() {
		Maze maze = new Maze(m1);

		assertEquals(HERO_STATE.ALIVE, maze.getHeroState());

		LinkedList<Dragon> dragonList = maze.getDragonList();
		maze.updateHero(DIRECTION.DOWN);
		maze.HeroVsDragon(dragonList.getFirst());

		assertEquals(HERO_STATE.DEAD, maze.getHeroState());
	}


	@Test
	public void testDragonDies() throws EndGame {
		Maze maze = new Maze(m1);

		maze.heroFastSwordPick();
		maze.update(DIRECTION.DOWN);

		LinkedList<Dragon> dragonList = maze.getDragonList();

		System.out.println(dragonList.size());
		assertEquals(0, dragonList.size());
		assertEquals(HERO_STATE.ALIVE, maze.getHeroState());
	}

	@Test
	public void testHeroWins() {
		Maze maze = new Maze(m1);

		maze.heroFastSwordPick();
		try {
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

		

		assertEquals(maze.getHeroPosition(), maze.getExitPosition());
		assertEquals(HERO_STATE.WIN, maze.getHeroState());
	}


	@Test 
	public void testHeroToExitUnsuccessfully() {
		Maze maze = new Maze(m1);

		// Dragon isn't killed in this test

		// Without sword
		maze.updateHero(DIRECTION.RIGHT);
		assertEquals(new Position(1,3), maze.getHeroPosition());


		// With sword
		maze.heroFastSwordPick();
		maze.updateHero(DIRECTION.RIGHT);
		assertEquals(new Position(1,3), maze.getHeroPosition());
	}


	@Test
	public void testHeroUnharmed() {

		Maze maze = new Maze(7, 14, DRAGON_MODE.CAN_SLEEP);

		LinkedList<Dragon> dragonList = maze.getDragonList();
		for (Iterator<Dragon> iterator = dragonList.iterator(); iterator.hasNext();)
			iterator.next().sleep();

		
		/*try {
			maze.update(DIRECTION.DOWN);
		} catch (EndGame e) {
			fail("shouldnt enter here");
		}*/
		maze.updateHero(DIRECTION.DOWN);
		maze.HeroVsDragon(dragonList.getFirst());

		assertEquals(DRAGON_STATE.SLEEPING, maze.getDragonState(dragonList.getFirst()));
		assertEquals(HERO_STATE.ALIVE, maze.getHeroState());
	}
	
	@Test
	public void testDragonOnSword(){
		Maze m = new Maze(m1);
		Dragon dragon = m.getDragonList().getFirst();
		Position pos = new Position(dragon.getPosition());
		m.moveDragon(DIRECTION.LEFT,dragon);
		assertEquals('.', m.getMazeContent(pos));
		m.moveDragon(DIRECTION.LEFT,dragon);
		assertSame('F', m.getMazeContent(dragon.getPosition()));
		
		m.setDragonMode(DRAGON_MODE.FROZEN);
		try {
			m.update(DIRECTION.DOWN);
			assertSame('F', m.getMazeContent(m.getSwordPosition()));
			assertEquals(new Position(2,3), m.getHero().getPosition());
		} catch (EndGame e) {
			fail("Didn't expect exception");
		}
		
	}

	@Test
	public void defaultMaze(){
		Maze maze = new Maze();

		String heroPos = maze.getHeroPosition().toString();

		assertEquals("1  1", heroPos);

		Position pos = new Position (2 ,2);

		assertFalse(pos.equals(3));
		assertFalse(pos.equals(new Position(3,2)));

	}
	
	@Test
	public void testMazeBuilder(){
		TestMazeBuilder t = new TestMazeBuilder();
		t.testRandomMazeGenerator();
		
		Maze m = new Maze(3);
		String s = "X X X \n"
				 + "X X X \n"
				 + "X X X \n";
		assertEquals(s , m.toString());
	}
	
	@Test
	public void testDirections(){
		Maze m = new Maze(m1);
		try{
		assertEquals(DIRECTION.UP,m.key2direction('w'));
		assertEquals(DIRECTION.DOWN,m.key2direction('s'));
		assertEquals(DIRECTION.LEFT,m.key2direction('a'));
		assertEquals(DIRECTION.RIGHT,m.key2direction('d'));
		}
		catch (InvalidKey i){
			fail("Didn't excpect exception");
		}
	}

	
	@Test
	public void testIllegalArgumentException(){
		try{
			Maze maze = new Maze(6,5,DRAGON_MODE.FROZEN);
			fail("expected exception");
		}
		catch(IllegalArgumentException i){
			assertSame(IllegalArgumentException.class, i.getClass());
		}
		
		
		try{
			Maze maze = new Maze(5,7,DRAGON_MODE.FROZEN);
			fail("expected exception");
		}
		catch(IllegalArgumentException i){
			assertSame(IllegalArgumentException.class, i.getClass());
		}
		try{
			Maze maze = new Maze(5,0,DRAGON_MODE.FROZEN);
		}
		catch(IllegalArgumentException i){
			fail("expected exception");
			
		}
	}
}
