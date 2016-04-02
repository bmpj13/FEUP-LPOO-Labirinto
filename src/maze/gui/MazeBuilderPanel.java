package maze.gui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;

import utilities.Position;
import maze.logic.Maze;


public class MazeBuilderPanel extends MazeGraphics implements MouseListener {
	private static final long serialVersionUID = 1L;
	
	private BufferedImage closedDoor;
	private BufferedImage openDoor;

	public static final int sidebarWidth = 100;
	private static final int optionLength = sidebarWidth - 20;


	private enum ACTIVE_OPTION {PATH, DRAGON, HERO, SWORD};
	ACTIVE_OPTION activeOpt;


	private boolean hasHero;	
	private boolean hasSword;
	Position lastHeroPos;
	Position lastSwordPos;


	Position pathOptPos;
	Position dragonOptPos;
	Position heroOptPos;
	Position swordOptPos;

	private int blockWidth;
	private int blockHeight;
	private int mazeDimension;

	private char[][] board;



	public MazeBuilderPanel(int mazeDimension) {
		super();

		activeOpt = ACTIVE_OPTION.PATH;

		hasHero = false;
		hasSword = false;

		pathOptPos = new Position(15, 10);
		dragonOptPos = new Position(pathOptPos.y + optionLength + 40, 10);
		heroOptPos = new Position(dragonOptPos.y + optionLength + 40, 10);
		swordOptPos = new Position(heroOptPos.y + optionLength + 40, 10);


		this.mazeDimension = mazeDimension;
		board = new char[mazeDimension][mazeDimension];
		for (int i = 0; i < mazeDimension; i++)
			Arrays.fill(board[i], Maze.Symbol_Wall);


		Random rand = new Random();
		int randomPos = rand.nextInt(mazeDimension - 2) + 1;

		switch (rand.nextInt(4)) {
		case 0:
			board[0][randomPos] = Maze.Symbol_Exit;
			this.closedDoor = super.closedDoor;
			break;

		case 1:
			board[mazeDimension-1][randomPos] = Maze.Symbol_Exit;
			this.closedDoor = rotate(super.closedDoor, Math.PI);
			break;

		case 2:
			board[randomPos][0] = Maze.Symbol_Exit;
			this.closedDoor = rotate(super.closedDoor, -Math.PI/2);
			break;

		case 3:
			board[randomPos][mazeDimension-1] = Maze.Symbol_Exit;
			this.closedDoor = rotate(super.closedDoor, Math.PI/2);
			break;
		}
		

		addMouseListener(this);
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), 0, 0, background.getWidth(), background.getHeight(), null);

		blockWidth =  (this.getWidth() - sidebarWidth) / mazeDimension;
		blockHeight = this.getHeight() / mazeDimension;

		for (int i = 0; i < mazeDimension; i++) {

			int y = i*blockHeight;
			int height = y + blockHeight;

			for (int j = 0; j < mazeDimension; j++) {

				int x = j*blockWidth + sidebarWidth;
				int width = x + blockWidth - 1;

				if (board[i][j] == Maze.Symbol_Wall)
					g.drawImage(wall, x, y, width, height, 0, 0, wall.getWidth(), wall.getHeight(), null);
				else if (board[i][j] == Maze.Symbol_Path)
					g.drawImage(path, x, y, width, height, 0, 0, path.getWidth(), path.getHeight(), null);
				else if (board[i][j] == Maze.Symbol_DragonActive) {
					g.drawImage(path, x, y, width, height, 0, 0, path.getWidth(), path.getHeight(), null);
					//g.drawImage(dragon, x, y, width, height, 0, 0, dragon.getWidth(), dragon.getHeight(), null);
				}
				else if (board[i][j] == Maze.Symbol_HeroUnarmed) {
					g.drawImage(path, x, y, width, height, 0, 0, path.getWidth(), path.getHeight(), null);
					g.drawImage(hero, x, y, width, height, 0, 0, hero.getWidth(), hero.getHeight(), null);
				}
				else if (board[i][j] == Maze.Symbol_Sword) {
					g.drawImage(path, x, y, width, height, 0, 0, path.getWidth(), path.getHeight(), null);
					g.drawImage(sword, x, y, width, height, 0, 0, sword.getWidth(), sword.getHeight(), null);
				}
				else if (board[i][j] == Maze.Symbol_Exit) {
					g.drawImage(wall, x, y, width, height, 0, 0, wall.getWidth(), wall.getHeight(), null);
					g.drawImage(closedDoor, x, y, width, height, 0, 0, closedDoor.getWidth(), closedDoor.getHeight(), null);
				}
				else if (board[i][j] == Maze.Symbol_DragonOnSword);
			}
		}


		g.drawImage(path, pathOptPos.x, pathOptPos.y, pathOptPos.x + optionLength, pathOptPos.y + optionLength, 
				0, 0, wall.getWidth(), wall.getHeight(), null);

//		g.drawImage(dragon, dragonOptPos.x, dragonOptPos.y, dragonOptPos.x + optionLength, dragonOptPos.y + optionLength, 
//				0, 0, dragon.getWidth(), dragon.getHeight(), null);

		g.drawImage(hero, heroOptPos.x, heroOptPos.y, heroOptPos.x + optionLength, heroOptPos.y + optionLength,
				0, 0, hero.getWidth(), hero.getHeight(), null);

		g.drawImage(sword, swordOptPos.x, swordOptPos.y, swordOptPos.x + optionLength, swordOptPos.y + optionLength,
				0, 0, sword.getWidth(), sword.getHeight(), null);
	}


	@Override
	public void mouseClicked(MouseEvent me) {}

	@Override
	public void mouseEntered(MouseEvent me) {}

	@Override
	public void mouseExited(MouseEvent me) {}

	@Override
	public void mousePressed(MouseEvent me) {

		int mouseX = me.getX();
		int mouseY = me.getY();


		if (mouseX >= sidebarWidth) {

			int y = mouseY / blockHeight;
			int x = (mouseX - sidebarWidth) / blockWidth;


			if (x == 0 || x == mazeDimension - 1 || y == 0 || y == mazeDimension - 1)
				return;


			switch (activeOpt) {

			case PATH:
				if (board[y][x] == Maze.Symbol_Path)	
					board[y][x] = Maze.Symbol_Wall;
				else	
					board[y][x] = Maze.Symbol_Path;
				break;

			case DRAGON:
				if (board[y][x] == Maze.Symbol_DragonActive)	
					board[y][x] = Maze.Symbol_Wall;
				else if (!adjacentTo(y, x, Maze.Symbol_HeroUnarmed))
					board[y][x] = Maze.Symbol_DragonActive;
				break;

			case HERO:
				if (board[y][x] == Maze.Symbol_HeroUnarmed)		
					board[y][x] = Maze.Symbol_Wall;
				else if (!hasHero && !adjacentTo(y, x, Maze.Symbol_DragonActive)) {
					board[y][x] = Maze.Symbol_HeroUnarmed;
					lastHeroPos = new Position(y, x);
					hasHero = true;
				}
				else if (hasHero && !adjacentTo(y, x, Maze.Symbol_DragonActive)) {
					board[lastHeroPos.y][lastHeroPos.x] = Maze.Symbol_Wall;
					board[y][x] = Maze.Symbol_HeroUnarmed;
					lastHeroPos = new Position(y, x);
				}
				break;

			case SWORD:
				if (board[y][x] == Maze.Symbol_Sword)		
					board[y][x] = Maze.Symbol_Wall;
				else if (!hasSword) {
					board[y][x] = Maze.Symbol_Sword;
					lastSwordPos = new Position(y, x);
					hasSword = true;
				}
				else {
					board[lastSwordPos.y][lastSwordPos.x] = Maze.Symbol_Wall;
					board[y][x] = Maze.Symbol_Sword;
					lastSwordPos = new Position(y, x);
				}
				break;
			}

			repaint();
		}
		else {
			if (between(mouseX, pathOptPos.x, pathOptPos.x + optionLength)
					&& between(mouseY, pathOptPos.y, pathOptPos.y + optionLength)) {
				activeOpt = ACTIVE_OPTION.PATH;
			}
			else if (between(mouseX, dragonOptPos.x, dragonOptPos.x + optionLength)
					&& between(mouseY, dragonOptPos.y, dragonOptPos.y + optionLength)) {
				activeOpt = ACTIVE_OPTION.DRAGON;
			}
			else if (between(mouseX, heroOptPos.x, heroOptPos.x + optionLength)
					&& between(mouseY, heroOptPos.y, heroOptPos.y + optionLength)) {
				activeOpt = ACTIVE_OPTION.HERO;
			}
			else if (between(mouseX, swordOptPos.x, swordOptPos.x + optionLength)
					&& between(mouseY, swordOptPos.y, swordOptPos.y + optionLength)) {
				activeOpt = ACTIVE_OPTION.SWORD;
			}
		}
	}


	@Override
	public void mouseReleased(MouseEvent me) {}


	private boolean between(int x, int xi, int xf) {

		return (x >= xi && x <= xf);
	}
	
	
	private boolean adjacentTo(int y, int x, char symbol) {
		
		return (board[y-1][x] == symbol || board[y+1][x] == symbol
				|| board[y][x-1] == symbol || board[y][x+1] == symbol);
	}


	public char[][] getBoard() {

		return board;
	}


	public void resetBoard() {
		// TODO Auto-generated method stub
		
	}

}
