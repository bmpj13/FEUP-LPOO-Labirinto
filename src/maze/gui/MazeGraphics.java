package maze.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import maze.exceptions.EndGame;
import maze.logic.Maze;
import maze.logic.Maze.DIRECTION;

public class MazeGraphics extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DIRECTION dir;
	private Maze maze;
	
	MazeGraphics() {		
		super();
		
		this.setBackground(Color.WHITE);
		
		addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent k) {
				switch(k.getKeyCode()){
				case KeyEvent.VK_LEFT: 
					dir = DIRECTION.LEFT; 
					break;
					
				case KeyEvent.VK_RIGHT: 
					dir = DIRECTION.RIGHT; 
					break;

				case KeyEvent.VK_UP: 
					dir = DIRECTION.UP; 
					break;

				case KeyEvent.VK_DOWN: 
					dir = DIRECTION.DOWN; 
					break;
				}
				
				try{
					maze.update(dir);
				}
				catch (EndGame e){	
				}
				finally {
					repaint();
				}
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.drawString("cenas\nfghd", 0, 10);
		
		//g.fillOval(0, 0, this.getWidth(), this.getHeight());
		
		if (maze != null)
			g.drawString(maze.toString(), 25, 100);
//		g.drawImage((Image)maze.toString(), 0, 0, maze.getMaze().length, maze.getMaze().length, 0, 0, maze.getMaze().length, maze.getMaze().length, null);
	}
	
	
	public void setMaze(Maze maze) {
		
		this.maze = maze;
	}
}
