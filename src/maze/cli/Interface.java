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
		int i = readInt();
		System.out.println();
		return i;
	}

	public int askNumberDragons() {

		System.out.print("Insert desired number of dragons: ");
		int i = readInt();
		System.out.println();
		return i;
	}


	public int askDragonMode() {

		int option = 0;

		System.out.println("Dragon mode:\n" + 
							"1 - Dragon doesn't move\n" + 
							"2 - Dragon can move\n" + 
							"3 - Dragon moves and falls asleep\n" + 
							"0 - Default\n" + 
							"Choose option: ");

		boolean valid = true;
		do {

			if (!valid)
				System.out.print("Choose a number between 0-3: ");

			option = readInt();
			valid = false;
		} while (option < 0 || option > 3);
		System.out.println();
		return option;
	}



	public char askDirection() {

		System.out.print("\nDesired direction (WASD): ");

		return scan.next().charAt(0);
	}

	public void WinMsg() {

		System.out.println("\nCongratulations, you won!");
	}

	public void LoseMsg() {

		System.out.println("\nYou lost. Try again?");
	}



	public <T> void display(T obj) {

		System.out.print(obj);
	}


	public int askGameMode() {

		int option = 0;

		System.out.println("Game mode:\n" + 
							"1 - Graphics\n" + 
							"2 - Console\n" + 
							"Choose option: ");

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
