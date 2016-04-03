package maze.gui;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MazeGraphics extends JPanel {
	private static final long serialVersionUID = 1L;

	protected BufferedImage[][] heroUnarmedImg;
	protected BufferedImage[][] heroArmedImg;
	protected BufferedImage[][] heroDyingImg;
	protected BufferedImage[][] heroWinningImg;
	
	protected BufferedImage[][] dragonActiveImg;
	protected BufferedImage[][] dragonSleepingImg;
	
	protected BufferedImage wallImg;
	protected BufferedImage pathImg;
	
	protected BufferedImage swordImg;
	
	protected BufferedImage backgroundImg;
	
	protected BufferedImage closedDoorImg;
	protected BufferedImage openDoorImg;
	


	protected static final int UP = 0;
	protected static final int LEFT = 1;
	protected static final int DOWN = 2;
	protected static final int RIGHT = 3;


	protected ArrayList<Integer> dragonActiveDirIndex;
	protected ArrayList<Integer> dragonSleepingDirIndex;
	protected ArrayList<Integer> heroDirIndex;
	protected ArrayList<Integer> heroEndDirIndex;

	MazeGraphics() {
		super();

		try {

			dragonActiveDirIndex = new ArrayList<Integer>();
			dragonSleepingDirIndex = new ArrayList<Integer>();
			heroDirIndex = new ArrayList<Integer>();
			heroEndDirIndex = new ArrayList<Integer>();


			wallImg = ImageIO.read(new File("res\\wall.png"));
			pathImg = ImageIO.read(new File("res\\path.png"));
			swordImg = ImageIO.read(new File("res\\sword.png"));
			backgroundImg = ImageIO.read(new File("res\\background.jpg"));
			closedDoorImg = ImageIO.read(new File("res\\closedDoor.png"));
			openDoorImg = pathImg;

			
			// Dragons
			dragonActiveDirIndex.add(3);
			dragonActiveDirIndex.add(1);
			dragonActiveDirIndex.add(0);
			dragonActiveDirIndex.add(2);
			
			BufferedImage temp = ImageIO.read(new File("res\\dragonActive.png"));
			dragonActiveImg = createSpriteSheet(temp, 4, 4, dragonActiveDirIndex);
			
			
			dragonSleepingDirIndex.add(0);
			dragonSleepingDirIndex.add(0);
			dragonSleepingDirIndex.add(0);
			dragonSleepingDirIndex.add(0);
			
			temp = ImageIO.read(new File("res\\dragonSleeping.png"));
			dragonSleepingImg = createSpriteSheet(temp, 1, 3, dragonSleepingDirIndex);
			
			

			
			// Hero
			heroDirIndex.add(0);
			heroDirIndex.add(1);
			heroDirIndex.add(2);
			heroDirIndex.add(3);
			
			temp = ImageIO.read(new File("res\\heroUnarmed.png"));
			heroUnarmedImg = createSpriteSheet(temp, 4, 9, heroDirIndex);
			
			temp = ImageIO.read(new File("res\\heroArmed.png"));
			heroArmedImg = createSpriteSheet(temp, 4, 9, heroDirIndex);
	
			
			heroEndDirIndex.add(0);
			heroEndDirIndex.add(0);
			heroEndDirIndex.add(0);
			heroEndDirIndex.add(0);
			
			temp = ImageIO.read(new File("res\\heroDying.png"));
			heroDyingImg = createSpriteSheet(temp, 1, 6, heroEndDirIndex);
			
			temp = ImageIO.read(new File("res\\heroWinning.png"));
			heroWinningImg = createSpriteSheet(temp, 1, 7, heroEndDirIndex);
			
		} catch (IOException e) {
			e.printStackTrace();
		};
	}



	protected BufferedImage rotate(BufferedImage image, double angle) {

		AffineTransform at = AffineTransform.getRotateInstance(
				angle, image.getWidth()/2.0, image.getHeight()/2.0);
		return createTransformed(image, at);
	}



	protected BufferedImage createTransformed(
			BufferedImage image, AffineTransform at) {
		
		BufferedImage newImage = new BufferedImage(
				image.getWidth(), image.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = newImage.createGraphics();
		g.transform(at);
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}



	protected BufferedImage[][] createSpriteSheet(BufferedImage temp, int numAnimations,
			int numImagesPerAnimation, ArrayList<Integer> moveDir) {

		int tileWidth, tileHeight;
		BufferedImage[][] sheet = new BufferedImage[4][numImagesPerAnimation];

		tileWidth = temp.getWidth() / numImagesPerAnimation;
		tileHeight = temp.getHeight() / numAnimations;

		for (int i = 0; i < sheet.length; i++) {
			for (int j = 0; j < sheet[i].length; j++) {
				sheet[i][j] = temp.getSubimage(j*tileWidth, moveDir.get(i)*tileHeight, tileWidth, tileHeight);
			}
		}

		return sheet;
	}
}
