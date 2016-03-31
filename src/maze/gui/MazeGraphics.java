package maze.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import maze.logic.Maze;

public class MazeGraphics extends JPanel {
	private static final long serialVersionUID = 1L;

	
	Timer timer;

	private Maze maze;

	private BufferedImage hero;
	private BufferedImage dragon;
	private BufferedImage wall;
	private BufferedImage path;
	private BufferedImage test;
	
	int testX = 0;
	int testY = 0;

	MazeGraphics() {		
		super();
		this.setBackground(Color.WHITE);


		try {
			hero =  ImageIO.read(new File("res\\hero.png"));
			dragon = ImageIO.read(new File("res\\dragon.png"));
			wall = ImageIO.read(new File("res\\wall.jpg"));
			path = ImageIO.read(new File("res\\path.jpg"));
			test = ImageIO.read(new File("res\\test.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent me) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent me) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent me) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent me) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent me) {
				// TODO Auto-generated method stub
				
			}
		});
		
//		timer = new Timer(500, new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				
//				testX += test.getWidth()/4;
//				
//				if (testX >= test.getWidth())
//					testX = 0;
//				
//				repaint();
//			}
//		});
//
//		timer.start();
//		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);


		if (maze != null) {

			int dimension = maze.getDimension();
			int x = 0;
			int y = 0;
			int width = this.getWidth()/dimension;
			int height = this.getHeight()/dimension;

			for (int i = 0; i < dimension; i++) {
				for (int j = 0; j < dimension; j++) {

					char symbol = maze.getMazeContent(i, j);
					
					if (symbol == Maze.Symbol_Wall)
						g.drawImage(wall, x, y, x + width - 1, y + height - 1, 0, 0, wall.getWidth(), wall.getHeight(), null);
					else if (symbol == Maze.Symbol_Path)
						g.drawImage(path, x, y, x + width - 1, y + height - 1, 0, 0, path.getWidth(), path.getHeight(), null);

					x += width;
				}

				x = 0;
				y += height;
			}

			
			x = maze.getHeroPosition().x*width;
			y = maze.getHeroPosition().y*height;
			g.drawImage(hero, x, y, x + width - 1, y + height - 1, 0, 0, hero.getWidth(), hero.getHeight(), null);
			
			
			int numDragons = maze.getNumberDragons();
			for (int i = 0; i < numDragons; i++) {
				
				x = maze.getDragonPosition(i).x*width;
				y = maze.getDragonPosition(i).y*height;
				
				g.drawImage(dragon, x, y, x + width - 1, y + height - 1, 0, 0, dragon.getWidth(), dragon.getHeight(), null);
			}
		}
		
		
//		g.drawImage(test, 0, 0, this.getWidth()/11 - 1, this.getHeight()/11 - 1, testX, testY,
//				testX + test.getWidth()/4 - 1, testY + test.getHeight()/4 - 1, null);
	}


	public void setMaze(Maze maze) {

		this.maze = maze;
	}
}
