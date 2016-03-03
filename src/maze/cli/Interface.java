package maze.cli;

import java.util.Scanner;


public class Interface {

	private Scanner scan;


	public Interface(){
		scan = new Scanner (System.in);
	}


	public void finalize(){
		scan.close();
	}


	public int askDragonMode() {

		int option = 0;

		System.out.println("Select game mode:");
		System.out.println("1 - Dragon doesn't move");
		System.out.println("2 - Dragon can move");
		System.out.println("3 - Dragon moves and falls asleep");
		System.out.println("0 - Default");
		System.out.print("Choose option: ");

		do {
			if (scan.hasNextInt())
				option = scan.nextInt();
			else
				scan.next();
		} while (option < 0 && option > 3);

		scan.nextLine();
		return option;
	}


	public char askDirection() {

		boolean firstAsk = true;
		char direction = ' ';

		System.out.println();
		System.out.print("Desired direction (WASD): ");
		do {
			if (!firstAsk)
				System.out.print("Choose one of WASD keys: ");

			if (scan.hasNext())
				direction = Character.toLowerCase(scan.next().charAt(0));

			firstAsk = false;
		} while (direction != 'w' && direction != 'a' && direction != 's' && direction != 'd');

		return direction;
	}



	
	public void WinMsg() {

		System.out.println();
		System.out.println("Congratulations, you won!");
	}


	public void LoseMsg() {

		System.out.println();
		System.out.println("You lost. Try again?");
	}
	
	

	public <T> void display(T obj) {

		System.out.print(obj);
	}

}
