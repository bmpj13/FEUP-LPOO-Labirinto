package maze.cli;

import java.util.Scanner;


public class Interface {

	//	public static void main(String[] args) {
	//
	//		Scanner scan = new Scanner(System.in);
	//
	//		while (hero.getState() == HERO_STATE.ALIVE) {
	//			//maze.display();
	//			System.out.println();
	//			System.out.print("Direcao desejada (WASD): ");
	//
	//			if (scan.hasNext()) {
	//				char direction = scan.next().charAt(0);
	//				maze.update(hero, direction, dragon, sword);
	//			}
	//		}
	//
	//
	//		maze.display();
	//
	//		if (hero.getState() == HERO_STATE.WIN)
	//			System.out.println("Congratulations, you won!");
	//		else
	//			System.out.println("You lost. Try again?");
	//
	//
	//		scan.close();
	//	}

	private Scanner scan;
	
	public Interface(){
		scan = new Scanner (System.in);
	}
	
	public void finalize(){
		scan.close();
	}
	
	public int askDragonMode() {

		int option = 0;
		Scanner scan = new Scanner(System.in);

		System.out.println("Select game mode:");
		System.out.println("1 - Dragon doesn't move");
		System.out.println("2 - Dragon can move");
		System.out.println("3 - Dragon moves and falls asleep");

		do{

			if (scan.hasNextInt())
				option = scan.nextInt();
			else
				scan.next();
		} while(option < 1 && option > 3);

		scan.nextLine();

		return option;
	}


	public char askDirection() {


		System.out.println();
		char direction = 'Q';
		System.out.print("Direcao desejada (WASD): ");
		do {
			if (scan.hasNext())
				direction = Character.toLowerCase(scan.next().charAt(0));
		} while (direction != 'w' && direction != 'a' && direction != 's' && direction != 'd');

		return direction;
	}


	public <T> void display(T obj) {

		System.out.print(obj);
	}

}
