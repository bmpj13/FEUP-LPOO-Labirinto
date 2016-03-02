/*package maze.logic;

import static org.junit.Assert.*;
import org.junit.Test;

import maze.logic.Hero;


public class MazeTest {
	char [][] m1 = {{'X', 'X', 'X', 'X', 'X'},
			{'X', ' ', ' ', 'H', 'S'},
			{'X', ' ', 'X', ' ', 'X'},
			{'X', 'E', ' ', 'D', 'X'},
			{'X', 'X', 'X', 'X', 'X'}};
	@Test
	public void testMoveHeroToFreeCell() {
		Maze maze = new Maze(m1);
		Hero hero = new Hero(1,3);
		assertEquals(new Point(1, 3), maze.getHeroPosition());
		assertEquals(3, )
		maze.moveHeroLeft();
		assertEquals(new Point(1, 2), maze.getHeroPosition());
	}
	@Test
	public void testHeroDies() {
		Maze maze = new Maze(m1);
		assertEquals(MazeStatus.HeroUnarmed, maze.getStatus());
		maze.moveHeroDown();
		assertEquals(MazeStatus.HeroDied, maze.getStatus());*/