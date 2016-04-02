package maze.gui;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MazeGraphics extends JPanel {
	private static final long serialVersionUID = 1L;

	protected static BufferedImage hero;
	protected static BufferedImage armed_hero;
	protected static BufferedImage[][] dragon;
	protected static BufferedImage wall;
	protected static BufferedImage path;
	protected static BufferedImage sword;
	protected static BufferedImage background;
	protected static BufferedImage closedDoor;
	protected static BufferedImage openDoor;

	static {

		try {
			hero =  ImageIO.read(new File("res\\188.png"));
			armed_hero =  ImageIO.read(new File("res\\ya.png"));
			wall = ImageIO.read(new File("res\\wall.jpg"));
			path = ImageIO.read(new File("res\\path.jpg"));
			sword = ImageIO.read(new File("res\\sword.png"));
			background = ImageIO.read(new File("res\\background.jpg"));
			closedDoor = ImageIO.read(new File("res\\closedDoor.png"));
			openDoor = ImageIO.read(new File("res\\openDoor.png"));
			
			BufferedImage temp = ImageIO.read(new File("res\\dragonSheet.png"));
			int tileWidth, tileHeight;
			dragon = new BufferedImage[4][4];
			
			tileWidth = temp.getWidth() / dragon.length;
			tileHeight = temp.getHeight() / dragon[0].length;
			
			for (int i = 0; i < dragon.length; i++) {
				for (int j = 0; j < dragon[i].length; j++) {
					dragon[i][j] = temp.getSubimage(j*tileWidth, i*tileHeight, tileWidth, tileHeight);
				}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		};
	}


	MazeGraphics() {
		super();
	}



	protected static BufferedImage rotate(BufferedImage image, double angle) {

		AffineTransform at = AffineTransform.getRotateInstance(
				angle, image.getWidth()/2, image.getHeight()/2.0);
		return createTransformed(image, at);
	}



	protected static BufferedImage createTransformed(
			BufferedImage image, AffineTransform at)
	{
		BufferedImage newImage = new BufferedImage(
				image.getWidth(), image.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		g.transform(at);
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}
}
