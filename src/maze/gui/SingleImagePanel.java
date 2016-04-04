package maze.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;



public class SingleImagePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private BufferedImage image;


	public SingleImagePanel(String path) {

		try {
			image = ImageIO.read(new File(path));
		}
		catch (IOException e) {
			e.printStackTrace();
		};
	}


	public SingleImagePanel(String path, int y, int x, int numAnimations, int numImagesPerAnimation) {

		try {
			image = ImageIO.read(new File(path));
		}
		catch (IOException e) {
			e.printStackTrace();
		};
		
		int tileWidth = image.getWidth() / numImagesPerAnimation;
		int tileHeight = image.getHeight() / numAnimations;
		image = image.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
	}
}
