package maze.logic;

import maze.cli.Interface;
import maze.logic.Dragon.DRAGON_MODE;
import maze.logic.Hero.HERO_STATE;

public class Game {

	public static void main(String[] args) {

		Interface interf = new Interface();

		int mode = interf.askDragonMode(); 

		DRAGON_MODE dm;
		if (mode == 1)
			dm = DRAGON_MODE.FROZEN;
		else if (mode == 2)
			dm = DRAGON_MODE.RANDOM;
		else
			dm = DRAGON_MODE.CAN_SLEEP;

		Maze maze = new Maze();
		Hero hero = new Hero();
		Sword sword = new Sword();
		Dragon dragon = new Dragon(dm);


		play(interf, maze, hero, dragon, sword);
	}



	public static void play(Interface interf, Maze maze, Hero hero, Dragon dragon, Sword sword){

		while (hero.getState() == HERO_STATE.ALIVE) {
			interf.display(maze);
			char direction = interf.askDirection();
			maze.update(hero, direction, dragon, sword);
		}
		
		interf.display(maze);
	}

}
