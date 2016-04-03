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


	public int readInt() {

		int number = 0;

		boolean valid = true;
		do {

			if (!valid)
				System.out.print("Invalid entry. Insert again: ");

			if (scan.hasNextInt()) {
				number = scan.nextInt();
				valid = true;
			}
			else {
				scan.next();
				valid = false;
			}
		} while (!valid);

		scan.nextLine();
		return number;
	}


	public int askMazeDimension() {

		System.out.print("Insert desired maze dimension: ");
		return readInt();
	}



	public int askNumberDragons() {

		System.out.print("Insert desired number of dragons: ");
		return readInt();
	}



	public int askDragonMode() {

		int option = 0;

		System.out.println("Dragon mode:");
		System.out.println("1 - Dragon doesn't move");
		System.out.println("2 - Dragon can move");
		System.out.println("3 - Dragon moves and falls asleep");
		System.out.println("0 - Default");
		System.out.print("Choose option: ");

		boolean valid = true;
		do {

			if (!valid)
				System.out.print("Choose a number between 0-3: ");

			option = readInt();
			valid = false;
		} while (option < 0 || option > 3);

		return option;
	}



	public char askDirection() {

		System.out.println();
		System.out.print("Desired direction (WASD): ");

		return scan.next().charAt(0);
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


	public int askGameMode() {

		int option = 0;

		System.out.println("Game mode:");
		System.out.println("1 - Graphics");
		System.out.println("2 - Console");
		System.out.print("Choose option: ");

		boolean valid = true;
		do {

			if (!valid)
				System.out.print("Insert number 1 or 2: ");

			option = readInt();
			valid = false;
		} while (option < 1 || option > 2);

		return option;
	}

}
