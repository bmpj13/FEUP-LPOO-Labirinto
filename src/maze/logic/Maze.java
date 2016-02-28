package maze.logic;
import java.util.Random;
import java.util.Scanner;

import maze.logic.Dragon.DRAGON_STATE;
import maze.logic.Hero.HERO_STATE;


public class Maze {

	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);

		Maze maze = new Maze();
		Hero hero = new Hero();
		Sword sword = new Sword();
		Dragon dragon = new Dragon();


		while (hero.getState() == HERO_STATE.ALIVE) {
			maze.display();
			System.out.println();
			char direction = 'Q';
			do{
				System.out.print("Direcao desejada (WASD): ");
	
				if (scan.hasNext()) {
					direction = Character.toLowerCase(scan.next().charAt(0));
				}
			}while(direction != 'w' && direction != 'a' && direction != 's' && direction != 'd');
			maze.update(hero, direction, dragon, sword);
		}


		maze.display();

		if (hero.getState() == HERO_STATE.WIN)
			System.out.println("Congratulations, you won!");
		else
			System.out.println("You lost. Try again?");


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


	public void update(Hero hero, char direction, Dragon dragon, Sword sword) {

		int yHero = hero.getVerPos();
		int xHero = hero.getHorPos();

		switch (direction) {
		case 'w':
			if (maze[yHero-1][xHero] != 'X' && maze[yHero-1][xHero] != 'd') {
				maze[yHero][xHero] = '.';				
				yHero--;
			}
			break;
		case 's':
			if (maze[yHero+1][xHero] != 'X' && maze[yHero+1][xHero] != 'd') {
				maze[yHero][xHero] = '.';				
				yHero++;
			}
			break;
		case 'a':
			if (maze[yHero][xHero-1] != 'X' && maze[yHero][xHero-1] != 'd') {
				maze[yHero][xHero] = '.';
				xHero--;		
			}
			break;
		case 'd':
			if (maze[yHero][xHero+1] != 'X' && maze[yHero][xHero+1] != 'd') {
				maze[yHero][xHero] = '.';
				xHero++;
			}
			break;
		}


		hero.move(yHero, xHero);	// moves hero, independently of the result


		if (maze[yHero][xHero] == '.')
			if(hero.hasSword())
				maze[yHero][xHero] = 'A';
			else maze[yHero][xHero] = 'H';
		else if (maze[yHero][xHero] == 'E') {
			maze[yHero][xHero] = 'A';
			hero.pickedSword();
		}
		else if (maze[yHero][xHero] == 'S' && dragon.getState() == DRAGON_STATE.DEAD) {
			maze[yHero][xHero] = 'A';
			hero.wins();
		}



		DRAGON_STATE dragonState = dragon.getState();

		if (dragonState != DRAGON_STATE.DEAD) {

			int yDragon = dragon.getVerPos();
			int xDragon = dragon.getHorPos();

			if (fighting(hero, dragon)) {

				if (hero.hasSword()) {
					maze[yDragon][xDragon] = '.';
					maze[yHero][xHero] = 'A';
					dragon.dies();
				}
				else if (dragonState == DRAGON_STATE.AWAKE) 
					hero.dies();
			}
			else {

				Random rand = new Random();

				if (dragonState == DRAGON_STATE.AWAKE) {

					int posRand = rand.nextInt() % 5;

					if (yDragon == 8 && xDragon == 1)
						posRand = 1;

					if (posRand == 0 && maze[yDragon+1][xDragon] != 'X') {

						maze[yDragon][xDragon] = '.';
						yDragon++;
					}
					else if (posRand == 1 && maze[yDragon-1][xDragon] != 'X') {

						maze[yDragon][xDragon] = '.';
						yDragon--;
					}
					else if (posRand == 2 && maze[yDragon][xDragon+1] != 'X') {

						maze[yDragon][xDragon] = '.';
						xDragon++;
					}
					else if (posRand == 3 && maze[yDragon][xDragon-1] != 'X') {

						maze[yDragon][xDragon] = '.';
						xDragon--;
					}						
				}					


				dragon.move(yDragon, xDragon);


				int stateRand = rand.nextInt() % 3;
				if (stateRand == 0) {
					dragon.sleep();
					dragonState = DRAGON_STATE.SLEEPING;
				}
				else {
					dragon.wakeUp();
					dragonState = DRAGON_STATE.AWAKE;
				}

				if (dragonState == DRAGON_STATE.AWAKE)
					maze[yDragon][xDragon] = 'D';
				else if (dragonState == DRAGON_STATE.SLEEPING)
					maze[yDragon][xDragon] = 'd';


				if (fighting(hero, dragon)) {

					if (hero.hasSword()) {
						maze[yDragon][xDragon] = '.';
						maze[yHero][xHero] = 'A';
						dragon.dies();
					}
					else if (dragonState == DRAGON_STATE.AWAKE) 
						hero.dies();
				}


				int ySword = sword.getVerPos();
				int xSword = sword.getHorPos();
				if(maze[yDragon][xDragon] == maze[ySword][xSword])
					maze[yDragon][xDragon] = 'F';
				else if(!hero.hasSword())
					maze[ySword][xSword] = 'E';

			}
		}
	}

	private boolean fighting(Hero hero, Dragon dragon) {

		int yHero = hero.getVerPos();
		int xHero = hero.getHorPos();
		int yDragon = dragon.getVerPos();
		int xDragon = dragon.getHorPos();

		if (xHero == xDragon && Math.abs(yHero - yDragon) <= 1)
			return true;
		else if (yHero == yDragon && Math.abs(xHero - xDragon) <= 1)
			return true;

		return false;
	}

}
