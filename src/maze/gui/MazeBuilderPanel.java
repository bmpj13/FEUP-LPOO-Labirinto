package maze.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import maze.logic.Maze;

public class MazeBuilderPanel extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;

	private int blockWidth = 0;
	private int blockHeight = 0;
	private int mazeDimension = 0;


	private char[][] board;
	private int x = 0;
	private int y = 0;


	private BufferedImage wall;
	private BufferedImage path;

	public MazeBuilderPanel(int width, int height, int mazeDimension) {
		super();
		

		setBackground(Color.WHITE);
		setVisible(true);
		setBounds(0, 0, width, height);
		setMinimumSize(getSize()); 

		blockWidth = this.getWidth() / mazeDimension;
		blockHeight = this.getHeight() / mazeDimension;
		

		try {
			wall = ImageIO.read(new File("res\\wall.jpg"));
			path = ImageIO.read(new File("res\\path.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}	


		this.mazeDimension = mazeDimension;
		board = new char[mazeDimension][mazeDimension];
		for (int i = 0; i < mazeDimension; i++)
			Arrays.fill(board[i], Maze.Symbol_Wall);

		addMouseListener(this);
		repaint();
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int startX;
		int endX;
		int startY;
		int endY;

		for (int i = 0; i < mazeDimension; i++) {

			startY = i*blockHeight;
			endY = startY + blockHeight - 1;

			for (int j = 0; j < mazeDimension; j++) {

				startX = j*blockWidth;
				endX = startX + blockWidth - 1;

				if (board[i][j] == Maze.Symbol_Wall)
					g.drawImage(wall, startX, startY, endX, endY, 0, 0, wall.getWidth(), wall.getHeight(), null);
				else
					g.drawImage(path, startX, startY, endX, endY, 0, 0, path.getWidth(), path.getHeight(), null);
			}
		}
	}


	@Override
	public void mouseClicked(MouseEvent me) {}

	@Override
	public void mouseEntered(MouseEvent me) {}

	@Override
	public void mouseExited(MouseEvent me) {}

	@Override
	public void mousePressed(MouseEvent me) {

		y = me.getY() / blockHeight;
		x = me.getX() / blockWidth;


		if (x == 0 || x == mazeDimension - 1 || y == 0 || y == mazeDimension - 1)
			return;


		if (board[y][x] == Maze.Symbol_Wall)
			board[y][x] = Maze.Symbol_Path;
		else
			board[y][x] = Maze.Symbol_Wall;

		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent me) {}

}
