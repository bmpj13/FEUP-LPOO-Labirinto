package maze;
import java.util.Scanner;

import maze.Hero.HERO_STATE;

public class Maze {

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


	public void moveHero(Hero hero, char direction) {

		int x = hero.getX();
		int y = hero.getY();

		switch (Character.toLowerCase(direction)) {
		case 'w':
			if (maze[x-1][y] != 'X') {
				maze[x][y] = '.';				
				x--;
			}
			break;
		case 's':
			if (maze[x+1][y] != 'X') {
				maze[x][y] = '.';				
				x++;
			}
			break;
		case 'a':
			if (maze[x][y-1] != 'X') {
				maze[x][y] = '.';
				y--;		
			}
			break;
		case 'd':
			if (maze[x][y+1] != 'X') {
				maze[x][y] = '.';
				y++;
			}
			break;
		}

		
		hero.move(x, y);	// moves hero, independently of the result
		
		if (maze[x][y] == 'D' && hero.hasSword())
			maze[x][y] = 'H';
		else if (maze[x][y] == 'D' && !hero.hasSword())
			hero.dies();
		else if (maze[x][y] == 'E') {
			hero.pickedSword();
			maze[x][y] = 'H';
		}
		else maze[x][y] = 'H';
	}


	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);
		
		Hero hero = new Hero();
		Maze maze = new Maze();

		while (hero.getState() == HERO_STATE.ALIVE) {
			maze.display();
			System.out.println();
			System.out.print("Direcao desejada (WASD): ");

			if (scan.hasNext()) {
				char direction = scan.next().charAt(0);
				maze.moveHero(hero, direction);
			}
		}
		
		scan.close();
	}

}
