package maze.gui;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import utilities.Position;
import maze.logic.Maze;


public class MazeGraphicPlay extends MazeGraphics {
	private static final long serialVersionUID = 1L;

	private Maze maze;
	private BufferedImage closedDoor;
	private BufferedImage openDoor;

	MazeGraphicPlay() {		
		super();
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);


		if (maze != null) {

			int dimension = maze.getDimension();
			int blockWidth =  this.getWidth() / dimension;
			int blockHeight = this.getHeight() / dimension;

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
						g.drawImage(dragon, x, y, width, height, 0, 0, dragon.getWidth(), dragon.getHeight(), null);
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
				}
			}
			
			
			g.drawImage(test[3][0], 0, 0, blockWidth, blockHeight, 0, 0, test[0][0].getWidth(), test[0][0].getHeight(), null);
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
}
