package maze;
import java.util.Random;
import java.util.Scanner;

import maze.Dragon.DRAGON_STATE;
import maze.Hero.HERO_STATE;


public class Maze {

	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);

		Maze maze = new Maze();
		Hero hero = new Hero();
		Dragon dragon = new Dragon();

		while (hero.getState() == HERO_STATE.ALIVE) {
			maze.display();
			System.out.println();
			System.out.print("Direcao desejada (WASD): ");

			if (scan.hasNext()) {
				char direction = scan.next().charAt(0);
				maze.update(hero, direction, dragon);
			}
		}


		maze.display();
		scan.close();
	}




	private char[][] maze;

	public Maze() {
		loadMaze();
	}

	public void loadMaze() {

		maze = new char[10][10];

		// borders of the maze
		for (int i = 0; i < 10; i++) {
			maze[i][0] = 'X';
			maze[0][i] = 'X';
			maze[i][9] = 'X';
			maze[9][i] = 'X';
		}


		for (int i = 1; i < 9; i++)
			for (int j = 1; j < 9; j++) {
				if ((j == 2 || j == 3 || j == 5 || j == 7) && (i > 1 && i < 8 && i != 5))
					maze[i][j] = 'X';
				else 
					maze[i][j] = '.';
			}

		maze[5][7] = 'X';
		maze[8][2] = 'X';
		maze[8][3] = 'X';
		maze[1][1] = 'H';
		maze[3][1] = 'D';
		maze[8][1] = 'E';
		maze[5][9] = 'S';
	}


	public void display() {

		for (int i = 0; i < maze.length ; i++) {
			for (int j = 0; j < maze.length; j++)
				System.out.print(maze[i][j] + " ");
			System.out.println();
		}
	}


	public void update(Hero hero, char direction, Dragon dragon) {

		int yHero = hero.getVerPos();
		int xHero = hero.getHorPos();

		switch (direction) {
		case 'w':
			if (maze[yHero-1][xHero] != 'X') {
				maze[yHero][xHero] = '.';				
				yHero--;
			}
			break;
		case 's':
			if (maze[yHero+1][xHero] != 'X') {
				maze[yHero][xHero] = '.';				
				yHero++;
			}
			break;
		case 'a':
			if (maze[yHero][xHero-1] != 'X') {
				maze[yHero][xHero] = '.';
				xHero--;		
			}
			break;
		case 'd':
			if (maze[yHero][xHero+1] != 'X') {
				maze[yHero][xHero] = '.';
				xHero++;
			}
			break;
		}


		hero.move(yHero, xHero);	// moves hero, independently of the result


		if (maze[yHero][xHero] == '.')
			maze[yHero][xHero] = 'H';
		else if (maze[yHero][xHero] == 'E') {
			maze[yHero][xHero] = 'H';
			hero.pickedSword();
		}
		else if (maze[yHero][xHero] == 'S' && dragon.getState() == DRAGON_STATE.DEAD) {
			maze[yHero][xHero] = 'H';
			hero.wins();
		}


		if (dragon.getState() == DRAGON_STATE.ALIVE && hero.getState() == HERO_STATE.ALIVE) {

			int yDragon = dragon.getVerPos();
			int xDragon = dragon.getHorPos();

			if (fighting(hero, dragon)) {

				if (hero.hasSword()) {
					maze[yHero][xHero] = 'H';
					maze[yDragon][xDragon] = '.';
					dragon.dies();
				}
				else hero.dies();
			}
			else {
				
				boolean movement = false;
				do {
					Random rand = new Random();
					int posRand = rand.nextInt() % 4;
					
					if (posRand == 0 && maze[yDragon+1][xDragon] != 'X') {
						
						movement = true;
						maze[yDragon][xDragon] = '.';
						yDragon++;
						maze[yDragon][xDragon] = 'D';
					}
					else if (posRand == 1 && maze[yDragon-1][xDragon] != 'X') {
						
						movement = true;
						maze[yDragon][xDragon] = '.';
						yDragon--;
						maze[yDragon][xDragon] = 'D';
					}
					else if (posRand == 2 && maze[yDragon][xDragon+1] != 'X') {
						
						movement = true;
						maze[yDragon][xDragon] = '.';
						xDragon++;
						maze[yDragon][xDragon] = 'D';
					}
					else if (posRand == 3 && maze[yDragon][xDragon-1] != 'X') {
						
						movement = true;
						maze[yDragon][xDragon] = '.';
						xDragon--;
						maze[yDragon][xDragon] = 'D';
					}
				} while (!movement);
				
				dragon.move(yDragon, xDragon);
			}
		}
	}

	private boolean fighting(Hero hero, Dragon dragon) {

		int yHero = hero.getVerPos();
		int xHero = hero.getHorPos();
		int yDragon = dragon.getVerPos();
		int xDragon = dragon.getHorPos();

		if (xHero == xDragon && Math.abs(yHero - yDragon) == 1)
			return true;
		else if (yHero == yDragon && Math.abs(xHero - xDragon) == 1)
			return true;

		return false;
	}

}
