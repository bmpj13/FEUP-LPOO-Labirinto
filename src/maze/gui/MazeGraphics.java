package maze.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MazeGraphics extends JPanel {
	private static final long serialVersionUID = 1L;

	protected static BufferedImage hero;
	protected static BufferedImage dragon;
	protected static BufferedImage wall;
	protected static BufferedImage path;
	protected static BufferedImage sword;
	protected static BufferedImage background;
	protected static BufferedImage closedDoor;
	protected static BufferedImage openDoor;

	static {

		try {
			hero =  ImageIO.read(new File("res\\hero.png"));
			dragon = ImageIO.read(new File("res\\dragon.png"));
			wall = ImageIO.read(new File("res\\wall.jpg"));
			path = ImageIO.read(new File("res\\path.jpg"));
			sword = ImageIO.read(new File("res\\sword.png"));
			background = ImageIO.read(new File("res\\background.jpg"));
			closedDoor = ImageIO.read(new File("res\\closedDoor.png"));
			openDoor = ImageIO.read(new File("res\\openDoor.png"));
		} catch (IOException e) {
			e.printStackTrace();
		};
	}


	MazeGraphics() {
		super();
	}
}
