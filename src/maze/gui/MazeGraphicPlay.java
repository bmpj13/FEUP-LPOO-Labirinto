package maze.gui;


import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

import utilities.Position;
import maze.logic.Maze;
import maze.logic.Maze.DIRECTION;


public class MazeGraphicPlay extends MazeGraphics {
	private static final long serialVersionUID = 1L;

	private Maze maze;
	private BufferedImage closedDoor;
	private BufferedImage openDoor;

	private HashMap<Position, DIRECTION> movementInfo;
	private Timer timer;
	private int animateOffset;
	private int animateIteration;
	private static final int numAnimations = 8;

	private int blockWidth;
	private int blockHeight;


	MazeGraphicPlay() {		
		super();


		animateOffset = 0;
		animateIteration = 0;
		timer = new Timer(100, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {

				animateIteration++;
				if (animateIteration > numAnimations)
					timer.stop();
				
				repaint();
			}
		});
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);


		if (maze != null) {

			int dimension = maze.getDimension();
			blockWidth =  this.getWidth() / dimension;
			blockHeight = this.getHeight() / dimension;

			for (int i = 0; i < dimension; i++) {

				int y = i*blockHeight;
				int height = y + blockHeight;

				for (int j = 0; j < dimension; j++) {

					int x = j*blockWidth;
					int width = x + blockWidth;

					char symbol = maze.getMazeContent(i, j);

					if (symbol == Maze.Symbol_Wall) {
						g.drawImage(wall, x, y, width, height, 0, 0, wall.getWidth(), wall.getHeight(), null);
					}
					else if (symbol == Maze.Symbol_Path) {
						g.drawImage(path, x, y, width, height, 0, 0, path.getWidth(), path.getHeight(), null);
					}
					else if (symbol == Maze.Symbol_DragonActive) {
						g.drawImage(path, x, y, width, height, 0, 0, path.getWidth(), path.getHeight(), null);
						//g.drawImage(dragon, x, y, width, height, 0, 0, dragon.getWidth(), dragon.getHeight(), null);
					}
					else if (symbol == Maze.Symbol_HeroUnarmed) {
						g.drawImage(path, x, y, width, height, 0, 0, path.getWidth(), path.getHeight(), null);
						g.drawImage(hero, x, y, width, height, 0, 0, hero.getWidth(), hero.getHeight(), null);
					}
					else if (symbol == Maze.Symbol_Sword) {
						g.drawImage(path, x, y, width, height, 0, 0, path.getWidth(), path.getHeight(), null);
						g.drawImage(sword, x, y, width, height, 0, 0, sword.getWidth(), sword.getHeight(), null);
					}
					else if (symbol == Maze.Symbol_Exit) {
						g.drawImage(wall, x, y, width, height, 0, 0, wall.getWidth(), wall.getHeight(), null);
						g.drawImage(this.closedDoor, width, y, x, height, 0, 0, closedDoor.getWidth(), closedDoor.getHeight(), null);
					}
					else if (symbol == Maze.Symbol_DragonOnSword);
					else {
						// Dragons and Hero
						g.drawImage(path, x, y, width, height, 0, 0, path.getWidth(), path.getHeight(), null);
					}
				}


				if (movementInfo != null) {
					for (Map.Entry<Position, DIRECTION> entry : movementInfo.entrySet()) {
						animate(g, dragon, entry.getKey(), entry.getValue());
					}
				}
			}
		}
	}


	public void setMaze(Maze maze) {

		this.maze = maze;

		Position exitPos = maze.getExitPosition();

		if (exitPos.x == 0) {
			this.closedDoor = rotate(super.closedDoor, Math.PI/2);
		}
		else if (exitPos.x == maze.getDimension() - 1) {
			this.closedDoor = rotate(super.closedDoor, -Math.PI/2);
		}
		else if (exitPos.y == maze.getDimension() - 1) {
			this.closedDoor = rotate(super.closedDoor, Math.PI);
		}
		else
			this.closedDoor = super.closedDoor;
	}


	public void setMovementInfo(HashMap<Position, DIRECTION> moveInfo) {
		movementInfo = moveInfo;
	}


	public void animate(Graphics g, BufferedImage[][] image, Position currentPos, DIRECTION lastMove) {

		Position lastPosition;

		int x = 0;
		int y = 0;
		
		int dirIndex;
		int animIndex = animateIteration % image[0].length;

		switch (lastMove) {
		case UP:
			lastPosition = new Position(currentPos.y+1, currentPos.x);
			animateOffset = -blockHeight / numAnimations;
			y += animateIteration * animateOffset;
			dirIndex = 3;
			break;
			
		case LEFT:
			lastPosition = new Position(currentPos.y, currentPos.x+1);
			animateOffset = -blockWidth / numAnimations;
			x += animateIteration * animateOffset;
			dirIndex = 1;
			break;
			
		case DOWN:
			lastPosition = new Position(currentPos.y-1, currentPos.x);
			animateOffset = blockHeight / numAnimations;
			y +=  animateIteration * animateOffset;
			dirIndex = 0;
			break;
			
		case RIGHT:
			lastPosition = new Position(currentPos.y, currentPos.x-1);
			animateOffset = blockWidth / numAnimations;
			x += animateIteration * animateOffset;
			dirIndex = 2;
			break;
			
		default:
			lastPosition = new Position(currentPos);
			dirIndex = 0;
			break;
		}

		x += lastPosition.x * blockWidth;
		y += lastPosition.y * blockHeight;

		g.drawImage(image[dirIndex][animIndex], x, y, x + blockWidth, y + blockHeight, 0, 0,
				image[dirIndex][animIndex].getWidth(), image[dirIndex][animIndex].getHeight(), null);
	}


	public void update() {

		animateIteration = 0;
		timer.start();
	}
}
