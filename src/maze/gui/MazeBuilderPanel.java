package maze.gui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;

import maze.logic.Position;
import maze.logic.Maze;


public class MazeBuilderPanel extends MazeGraphics implements MouseListener {
	private static final long serialVersionUID = 1L;

	private BufferedImage closedDoorImg;

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

	private Maze maze;

	
	
	public MazeBuilderPanel() {
		super();
	}


	public MazeBuilderPanel(Maze maze) {
		super();

		activeOpt = ACTIVE_OPTION.PATH;

		hasHero = false;
		hasSword = false;

		pathOptPos = new Position(15, 10);
		dragonOptPos = new Position(pathOptPos.y + optionLength + 40, 10);
		heroOptPos = new Position(dragonOptPos.y + optionLength + 40, 10);
		swordOptPos = new Position(heroOptPos.y + optionLength + 40, 10);


		this.mazeDimension = maze.getDimension();
		this.maze = maze;


		Random rand = new Random();
		int randomPos = rand.nextInt(mazeDimension - 2) + 1;

		switch (rand.nextInt(4)) {
		case 0:
			this.maze.setMazeContent(0, randomPos, Maze.Symbol_Exit);
			this.closedDoorImg = super.closedDoorImg;
			break;

		case 1:
			this.maze.setMazeContent(mazeDimension-1, randomPos, Maze.Symbol_Exit);
			this.closedDoorImg = rotate(super.closedDoorImg, Math.PI);
			break;

		case 2:
			this.maze.setMazeContent(randomPos, 0, Maze.Symbol_Exit);
			this.closedDoorImg = rotate(super.closedDoorImg, -Math.PI/2);
			break;

		case 3:
			this.maze.setMazeContent(randomPos, mazeDimension-1, Maze.Symbol_Exit);
			this.closedDoorImg = rotate(super.closedDoorImg, Math.PI/2);
			break;
		}


		addMouseListener(this);
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(backgroundImg, 0, 0, this.getWidth(), this.getHeight(), 0, 0, backgroundImg.getWidth(), backgroundImg.getHeight(), null);

		blockWidth =  (this.getWidth() - sidebarWidth) / mazeDimension;
		blockHeight = this.getHeight() / mazeDimension;

		for (int i = 0; i < mazeDimension; i++) {

			int y = i*blockHeight;
			int height = y + blockHeight;

			for (int j = 0; j < mazeDimension; j++) {

				int x = j*blockWidth + sidebarWidth;
				int width = x + blockWidth - 1;

				if (maze.getMazeContent(i, j) == Maze.Symbol_Wall) {
					g.drawImage(wallImg, x, y, width, height, 0, 0, wallImg.getWidth(), wallImg.getHeight(), null);
				}
				else if (maze.getMazeContent(i, j) == Maze.Symbol_Path) {
					g.drawImage(pathImg, x, y, width, height, 0, 0, pathImg.getWidth(), pathImg.getHeight(), null);
				}
				else if (maze.getMazeContent(i, j) == Maze.Symbol_DragonActive) {
					g.drawImage(pathImg, x, y, width, height, 0, 0, pathImg.getWidth(), pathImg.getHeight(), null);
					g.drawImage(dragonActiveImg[DOWN][0], x, y, width, height, 
							0, 0, dragonActiveImg[DOWN][0].getWidth(), dragonActiveImg[DOWN][0].getHeight(), null);
				}
				else if (maze.getMazeContent(i, j) == Maze.Symbol_HeroUnarmed) {
					g.drawImage(pathImg, x, y, width, height, 0, 0, pathImg.getWidth(), pathImg.getHeight(), null);
					g.drawImage(heroUnarmedImg[DOWN][0], x, y, width, height,
							0, 0, heroUnarmedImg[DOWN][0].getWidth(), heroUnarmedImg[DOWN][0].getHeight(), null);
				}
				else if (maze.getMazeContent(i, j) == Maze.Symbol_Sword) {
					g.drawImage(pathImg, x, y, width, height, 0, 0, pathImg.getWidth(), pathImg.getHeight(), null);
					g.drawImage(swordImg, x, y, width, height, 0, 0, swordImg.getWidth(), swordImg.getHeight(), null);
				}
				else if (maze.getMazeContent(i, j) == Maze.Symbol_Exit) {
					g.drawImage(wallImg, x, y, width, height, 0, 0, wallImg.getWidth(), wallImg.getHeight(), null);
					g.drawImage(closedDoorImg, x, y, width, height, 0, 0, closedDoorImg.getWidth(), closedDoorImg.getHeight(), null);
				}

			}
		}


		g.drawImage(pathImg, pathOptPos.x, pathOptPos.y, pathOptPos.x + optionLength, pathOptPos.y + optionLength, 
				0, 0, wallImg.getWidth(), wallImg.getHeight(), null);

		g.drawImage(dragonActiveImg[DOWN][0], dragonOptPos.x, dragonOptPos.y, dragonOptPos.x + optionLength, dragonOptPos.y + optionLength, 
				0, 0, dragonActiveImg[DOWN][0].getWidth(), dragonActiveImg[DOWN][0].getHeight(), null);

		g.drawImage(heroUnarmedImg[DOWN][0], heroOptPos.x, heroOptPos.y, heroOptPos.x + optionLength, heroOptPos.y + optionLength,
				0, 0, heroUnarmedImg[DOWN][0].getWidth(), heroUnarmedImg[DOWN][0].getHeight(), null);

		g.drawImage(swordImg, swordOptPos.x, swordOptPos.y, swordOptPos.x + optionLength, swordOptPos.y + optionLength,
				0, 0, swordImg.getWidth(), swordImg.getHeight(), null);
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

			updateBoard(mouseX, mouseY, activeOpt);
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


	private void updateBoard(int mouseX, int mouseY, ACTIVE_OPTION activeOption) {
		
		int y = mouseY / blockHeight;
		int x = (mouseX - sidebarWidth) / blockWidth;


		if (x == 0 || x == mazeDimension - 1 || y == 0 || y == mazeDimension - 1)
			return;


		switch (activeOption) {

		case PATH:
			if (maze.getMazeContent(y, x) == Maze.Symbol_Path)	
				maze.setMazeContent(y, x, Maze.Symbol_Wall);
			else
				maze.setMazeContent(y, x, Maze.Symbol_Path);
			break;

		case DRAGON:
			if (maze.getMazeContent(y, x) == Maze.Symbol_DragonActive)	
				maze.setMazeContent(y, x, Maze.Symbol_Wall);
			else if (!adjacentTo(y, x, Maze.Symbol_HeroUnarmed))
				maze.setMazeContent(y, x, Maze.Symbol_DragonActive);
			break;

		case HERO:
			if (maze.getMazeContent(y, x) == Maze.Symbol_HeroUnarmed)
				maze.setMazeContent(y, x, Maze.Symbol_Wall);
			else if (!hasHero && !adjacentTo(y, x, Maze.Symbol_DragonActive)) {
				maze.setMazeContent(y, x, Maze.Symbol_HeroUnarmed);
				lastHeroPos = new Position(y, x);
				hasHero = true;
			}
			else if (hasHero && !adjacentTo(y, x, Maze.Symbol_DragonActive)) {
				maze.setMazeContent(lastHeroPos.y, lastHeroPos.x, Maze.Symbol_Wall);
				maze.setMazeContent(y, x, Maze.Symbol_HeroUnarmed);
				lastHeroPos = new Position(y, x);
			}
			break;

		case SWORD:
			if (maze.getMazeContent(y, x) == Maze.Symbol_Sword)		
				maze.setMazeContent(y, x, Maze.Symbol_Wall);
			else if (!hasSword) {
				maze.setMazeContent(y, x, Maze.Symbol_Sword);
				lastSwordPos = new Position(y, x);
				hasSword = true;
			}
			else {
				maze.setMazeContent(lastSwordPos.y, lastSwordPos.x, Maze.Symbol_Wall);
				maze.setMazeContent(y, x, Maze.Symbol_Sword);
				lastSwordPos = new Position(y, x);
			}
			break;
		}
	}


	@Override
	public void mouseReleased(MouseEvent me) {}


	private boolean between(int x, int xi, int xf) {

		return (x >= xi && x <= xf);
	}


	private boolean adjacentTo(int y, int x, char symbol) {

		return (maze.getMazeContent(y-1, x) == symbol || maze.getMazeContent(y+1, x) == symbol
				|| maze.getMazeContent(y, x-1) == symbol || maze.getMazeContent(y, x+1) == symbol);
	}


	public char[][] getBoard() {

		return maze.getBoard();
	}

}
