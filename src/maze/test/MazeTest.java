package maze.test;
import static org.junit.Assert.*;

import org.junit.Test;

import maze.logic.*;
import maze.logic.Dragon.DRAGON_STATE;
import maze.logic.Hero.HERO_STATE;
import maze.logic.Maze.DIRECTION;


public class MazeTest {


	char [][] m1 = {{'X', 'X', 'X', 'X', 'X'},
				{'X', '.', '.', 'H', 'S'},
				{'X', '.', 'X', '.', 'X'},
				{'X', 'E', '.', 'D', 'X'},
				{'X', 'X', 'X', 'X', 'X'}};


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
		
		maze.moveHero(DIRECTION.DOWN);
		maze.HeroVsDragon();
		
		assertEquals(HERO_STATE.DEAD, maze.getHeroState());
	}
	
	
	@Test
	public void testDragonDies() {
		Maze maze = new Maze(m1);
		
		maze.heroFastSwordPick();
		maze.moveHero(DIRECTION.DOWN);
		maze.HeroVsDragon();
		
		assertEquals(DRAGON_STATE.DEAD, maze.getDragonState());
		assertEquals(HERO_STATE.ALIVE, maze.getHeroState());
	}

	
	
	@Test
	public void testHeroWins() {
		Maze maze = new Maze(m1);
		
		maze.heroFastSwordPick();
		maze.moveHero(DIRECTION.DOWN);
		maze.HeroVsDragon();
		
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
		
		Maze maze = new Maze();
		
		maze.setDragonAsleep();
		
		maze.moveHero(DIRECTION.DOWN);
		maze.HeroVsDragon();
		
		assertEquals(DRAGON_STATE.SLEEPING, maze.getDragonState());
		assertEquals(HERO_STATE.ALIVE, maze.getHeroState());
	}
}
